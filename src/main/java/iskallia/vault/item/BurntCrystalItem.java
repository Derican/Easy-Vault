package iskallia.vault.item;

import iskallia.vault.Vault;
import iskallia.vault.block.OtherSidePortalBlock;
import iskallia.vault.block.VaultPortalSize;
import iskallia.vault.block.entity.OtherSidePortalTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Arrays;
import java.util.Optional;

public class BurntCrystalItem extends Item {
    public BurntCrystalItem(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties()).tab(group).stacksTo(1));
        setRegistryName(id);
    }


    public ActionResultType useOn(ItemUseContext context) {
        if ((context.getLevel()).isClientSide || context.getPlayer() == null) {
            return super.useOn(context);
        }

        ItemStack stack = context.getPlayer().getItemInHand(context.getHand());

        if (stack.getItem() == ModItems.BURNT_CRYSTAL) {
            BlockPos pos = context.getClickedPos();

            if (tryCreatePortal((ServerWorld) context.getLevel(), pos, context.getClickedFace())) {
                context.getLevel().playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.VAULT_PORTAL_OPEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

                context.getItemInHand().shrink(1);
                return ActionResultType.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    private boolean tryCreatePortal(ServerWorld world, BlockPos pos, Direction facing) {
        Optional<VaultPortalSize> optional = VaultPortalSize.getPortalSize((IWorld) world, pos.relative(facing), Direction.Axis.X, (state, reader, p) -> Arrays.<Block>stream(ModConfigs.OTHER_SIDE.getValidFrameBlocks()).anyMatch((Block block)->block.defaultBlockState()==state));


        ServerWorld target = world.getServer().getLevel((world.dimension() == Vault.OTHER_SIDE_KEY) ? World.OVERWORLD : Vault.OTHER_SIDE_KEY);

        if (optional.isPresent()) {
            VaultPortalSize portal = optional.get();
            OtherSideData data = new OtherSideData(null);

            BlockPos start = portal.getBottomLeft();
            BlockPos match = forcePlace(world, start, target, portal);

            data.setLinkedPos(match);
            data.setLinkedDim(target.dimension());

            BlockState state = (BlockState) ModBlocks.OTHER_SIDE_PORTAL.defaultBlockState().setValue((Property) OtherSidePortalBlock.AXIS, (Comparable) portal.getAxis());

            portal.placePortalBlocks(blockPos -> {
                world.setBlock(blockPos, state, 3);
                TileEntity te = world.getBlockEntity(blockPos);
                if (!(te instanceof OtherSidePortalTileEntity))
                    return;
                OtherSidePortalTileEntity portalTE = (OtherSidePortalTileEntity) te;
                portalTE.setOtherSideData(data);
            });
            return true;
        }

        return false;
    }

    public static BlockPos forcePlace(ServerWorld source, BlockPos sourcePos, ServerWorld target, VaultPortalSize current) {
        BlockPos match = null;
        int i;
        for (i = 0; i < target.getMaxBuildHeight(); ) {
            BlockPos p = new BlockPos(sourcePos.getX(), i, sourcePos.getZ());
            Block block = target.getBlockState(p).getBlock();
            if (block != ModBlocks.OTHER_SIDE_PORTAL) {
                i++;
                continue;
            }
            match = p;
        }


        if (match == null) {
            for (i = 0; i < target.getMaxBuildHeight(); i++) {
                int yUp = sourcePos.getY() + i;
                int yDo = sourcePos.getY() - i;

                if (place(source, sourcePos, target, new BlockPos(sourcePos.getX(), yUp, sourcePos.getZ()), current, false)) {
                    match = new BlockPos(sourcePos.getX(), yUp, sourcePos.getZ());
                    break;
                }
                if (place(source, sourcePos, target, new BlockPos(sourcePos.getX(), yDo, sourcePos.getZ()), current, false)) {
                    match = new BlockPos(sourcePos.getX(), yDo, sourcePos.getZ());


                    break;
                }
            }
        }


        if (match == null) {
            place(source, sourcePos, target, new BlockPos(sourcePos.getX(), 128, sourcePos.getZ()), current, true);
            match = new BlockPos(sourcePos.getX(), 128, sourcePos.getZ());
        }

        return match;
    }

    private static boolean place(ServerWorld source, BlockPos sourcePos, ServerWorld target, BlockPos targetPos, VaultPortalSize current, boolean force) {
        if (!force) {
            if (targetPos.getY() >= target.getMaxBuildHeight() || targetPos.getY() < 0) {
                return false;
            }

            if (!target.getBlockState(targetPos).isAir() ||
                    !target.getBlockState(targetPos.above()).isAir() ||
                    !target.getBlockState(targetPos.below()).canOcclude()) {
                return false;
            }
        }

        BlockState state = (BlockState) ModBlocks.OTHER_SIDE_PORTAL.defaultBlockState().setValue((Property) OtherSidePortalBlock.AXIS, (Comparable) current.getAxis());
        OtherSideData data = new OtherSideData(null);
        data.setLinkedDim(source.dimension());
        data.setLinkedPos(sourcePos);

        BlockPos.betweenClosed(targetPos.relative(Direction.DOWN).relative(current.getRightDir().getOpposite()), targetPos
                        .relative(Direction.UP, current.getHeight()).relative(current.getRightDir(), current.getWidth()))
                .forEach(blockPos -> target.setBlock(blockPos, Blocks.QUARTZ_BRICKS.defaultBlockState(), 3));


        BlockPos.betweenClosed(targetPos, targetPos.relative(Direction.UP, current.getHeight() - 1)
                        .relative(current.getRightDir(), current.getWidth() - 1))
                .forEach(blockPos -> {
                    target.setBlock(blockPos, state, 3);
                    TileEntity te = target.getBlockEntity(blockPos);
                    if (!(te instanceof OtherSidePortalTileEntity))
                        return;
                    OtherSidePortalTileEntity portalTE = (OtherSidePortalTileEntity) te;
                    portalTE.setOtherSideData(data);
                });
        return true;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\BurntCrystalItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */