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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Arrays;
import java.util.Optional;

public class BurntCrystalItem extends Item {
    public BurntCrystalItem(final ItemGroup group, final ResourceLocation id) {
        super(new Item.Properties().tab(group).stacksTo(1));
        this.setRegistryName(id);
    }

    public ActionResultType useOn(final ItemUseContext context) {
        if (context.getLevel().isClientSide || context.getPlayer() == null) {
            return super.useOn(context);
        }
        final ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        if (stack.getItem() == ModItems.BURNT_CRYSTAL) {
            final BlockPos pos = context.getClickedPos();
            if (this.tryCreatePortal((ServerWorld) context.getLevel(), pos, context.getClickedFace())) {
                context.getLevel().playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.VAULT_PORTAL_OPEN, SoundCategory.BLOCKS, 1.0f, 1.0f);
                context.getItemInHand().shrink(1);
                return ActionResultType.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    private boolean tryCreatePortal(final ServerWorld world, final BlockPos pos, final Direction facing) {
        final Optional<VaultPortalSize> optional = VaultPortalSize.getPortalSize(world, pos.relative(facing), Direction.Axis.X, (state, reader, p) -> Arrays.stream(ModConfigs.OTHER_SIDE.getValidFrameBlocks()).anyMatch(b -> b == state.getBlock()));
        final ServerWorld target = world.getServer().getLevel((world.dimension() == Vault.OTHER_SIDE_KEY) ? World.OVERWORLD : Vault.OTHER_SIDE_KEY);
        if (optional.isPresent()) {
            final VaultPortalSize portal = optional.get();
            final OtherSideData data = new OtherSideData(null);
            final BlockPos start = portal.getBottomLeft();
            final BlockPos match = forcePlace(world, start, target, portal);
            data.setLinkedPos(match);
            data.setLinkedDim(target.dimension());
            final BlockState state = ModBlocks.OTHER_SIDE_PORTAL.defaultBlockState().setValue(OtherSidePortalBlock.AXIS, portal.getAxis());
            portal.placePortalBlocks(blockPos -> {
                world.setBlock(blockPos, state, 3);
                final TileEntity te = world.getBlockEntity(blockPos);
                if (!(te instanceof OtherSidePortalTileEntity)) {
                    return;
                } else {
                    final OtherSidePortalTileEntity portalTE = (OtherSidePortalTileEntity) te;
                    portalTE.setOtherSideData(data);
                    return;
                }
            });
            return true;
        }
        return false;
    }

    public static BlockPos forcePlace(final ServerWorld source, final BlockPos sourcePos, final ServerWorld target, final VaultPortalSize current) {
        BlockPos match = null;
        for (int i = 0; i < target.getMaxBuildHeight(); ++i) {
            final BlockPos p = new BlockPos(sourcePos.getX(), i, sourcePos.getZ());
            final Block block = target.getBlockState(p).getBlock();
            if (block == ModBlocks.OTHER_SIDE_PORTAL) {
                match = p;
                break;
            }
        }
        if (match == null) {
            for (int i = 0; i < target.getMaxBuildHeight(); ++i) {
                final int yUp = sourcePos.getY() + i;
                final int yDo = sourcePos.getY() - i;
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

    private static boolean place(final ServerWorld source, final BlockPos sourcePos, final ServerWorld target, final BlockPos targetPos, final VaultPortalSize current, final boolean force) {
        if (!force) {
            if (targetPos.getY() >= target.getMaxBuildHeight() || targetPos.getY() < 0) {
                return false;
            }
            if (!target.getBlockState(targetPos).isAir() || !target.getBlockState(targetPos.above()).isAir() || !target.getBlockState(targetPos.below()).canOcclude()) {
                return false;
            }
        }
        final BlockState state = ModBlocks.OTHER_SIDE_PORTAL.defaultBlockState().setValue(OtherSidePortalBlock.AXIS, current.getAxis());
        final OtherSideData data = new OtherSideData(null);
        data.setLinkedDim(source.dimension());
        data.setLinkedPos(sourcePos);
        BlockPos.betweenClosed(targetPos.relative(Direction.DOWN).relative(current.getRightDir().getOpposite()), targetPos.relative(Direction.UP, current.getHeight()).relative(current.getRightDir(), current.getWidth())).forEach(blockPos -> target.setBlock(blockPos, Blocks.QUARTZ_BRICKS.defaultBlockState(), 3));
        BlockPos.betweenClosed(targetPos, targetPos.relative(Direction.UP, current.getHeight() - 1).relative(current.getRightDir(), current.getWidth() - 1)).forEach(blockPos -> {
            target.setBlock(blockPos, state, 3);
            final TileEntity te = target.getBlockEntity(blockPos);
            if (!(te instanceof OtherSidePortalTileEntity)) {
                return;
            } else {
                final OtherSidePortalTileEntity portalTE = (OtherSidePortalTileEntity) te;
                portalTE.setOtherSideData(data);
                return;
            }
        });
        return true;
    }
}
