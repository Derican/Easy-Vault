package iskallia.vault.block;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.VaultCharmUpgrade;
import iskallia.vault.world.data.VaultCharmData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class VaultCharmControllerBlock extends Block {
    public VaultCharmControllerBlock() {
        super(AbstractBlock.Properties.of(Material.METAL)
                .strength(2.0F, 3600000.0F)
                .noOcclusion());
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.VAULT_CHARM_CONTROLLER_TILE_ENTITY.create();
    }


    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (world.isClientSide()) {
            return ActionResultType.SUCCESS;
        }
        if (hand != Hand.MAIN_HAND) {
            return ActionResultType.SUCCESS;
        }
        TileEntity te = world.getBlockEntity(pos);
        if (!(te instanceof iskallia.vault.block.entity.VaultCharmControllerTileEntity)) {
            return ActionResultType.SUCCESS;
        }
        if (!(player instanceof ServerPlayerEntity)) {
            return ActionResultType.SUCCESS;
        }


        ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;
        VaultCharmData data = VaultCharmData.get(sPlayer.getLevel());
        VaultCharmData.VaultCharmInventory inventory = data.getInventory(sPlayer);

        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.getItem() instanceof VaultCharmUpgrade) {
            VaultCharmUpgrade item = (VaultCharmUpgrade) heldItem.getItem();
            int newSize = item.getTier().getSlotAmount();
            if (inventory.canUpgrade(newSize)) {
                player.level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                data.upgradeInventorySize(sPlayer, item.getTier().getSlotAmount());
                heldItem.shrink(1);

                return ActionResultType.SUCCESS;
            }
            player.level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResultType.SUCCESS;
        }


        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, buffer -> buffer.writeNbt(data.getInventory(sPlayer).serializeNBT()));

        return ActionResultType.SUCCESS;
    }


    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return VoxelShapes.or(
                Block.box(5.0D, 0.0D, 5.0D, 11.0D, 1.0D, 11.0D), new VoxelShape[]{
                        Block.box(5.0D, 0.0D, 5.0D, 11.0D, 1.0D, 11.0D),
                        Block.box(6.0D, 1.0D, 6.0D, 10.0D, 4.0D, 10.0D),
                        Block.box(5.0D, 4.0D, 5.0D, 11.0D, 7.0D, 11.0D),
                        Block.box(4.0D, 7.0D, 4.0D, 12.0D, 9.0D, 12.0D),
                        Block.box(1.0D, 9.0D, 1.0D, 15.0D, 11.0D, 15.0D),
                        Block.box(5.0D, 11.0D, 5.0D, 11.0D, 15.0D, 11.0D)
                });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultCharmControllerBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */