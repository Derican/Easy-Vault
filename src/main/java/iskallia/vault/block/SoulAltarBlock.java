package iskallia.vault.block;

import iskallia.vault.block.base.FillableAltarBlock;
import iskallia.vault.block.entity.SoulAltarTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModParticles;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.state.Property;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;


public class SoulAltarBlock
        extends FillableAltarBlock<SoulAltarTileEntity> {
    public SoulAltarTileEntity createTileEntity(BlockState state, IBlockReader world) {
        return (SoulAltarTileEntity) ModBlocks.SOUL_ALTAR_TILE_ENTITY.create();
    }


    public IParticleData getFlameParticle() {
        return (IParticleData) ModParticles.RED_FLAME.get();
    }


    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.MALEVOLENCE;
    }


    public ActionResultType rightClicked(BlockState state, ServerWorld world, BlockPos pos, SoulAltarTileEntity tileEntity, ServerPlayerEntity player, ItemStack heldStack) {
        if (!tileEntity.initialized()) {
            return ActionResultType.SUCCESS;
        }
        if (player.isCreative()) {
            tileEntity.makeProgress(player, 1, sPlayer -> {
            });
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.FAIL;
    }


    protected BlockState getSuccessChestState(BlockState altarState) {
        BlockState chestState = super.getSuccessChestState(altarState);
        return (BlockState) chestState.setValue((Property) ChestBlock.FACING, (Comparable) ((Direction) chestState.getValue((Property) FACING)).getOpposite());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\SoulAltarBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */