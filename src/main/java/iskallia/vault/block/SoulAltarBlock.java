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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

public class SoulAltarBlock extends FillableAltarBlock<SoulAltarTileEntity> {
    @Override
    public SoulAltarTileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return (SoulAltarTileEntity) ModBlocks.SOUL_ALTAR_TILE_ENTITY.create();
    }

    @Override
    public IParticleData getFlameParticle() {
        return (IParticleData) ModParticles.RED_FLAME.get();
    }

    @Override
    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.MALEVOLENCE;
    }

    @Override
    public ActionResultType rightClicked(final BlockState state, final ServerWorld world, final BlockPos pos, final SoulAltarTileEntity tileEntity, final ServerPlayerEntity player, final ItemStack heldStack) {
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

    @Override
    protected BlockState getSuccessChestState(final BlockState altarState) {
        final BlockState chestState = super.getSuccessChestState(altarState);
        return chestState.setValue(ChestBlock.FACING, (chestState.getValue(SoulAltarBlock.FACING)).getOpposite());
    }
}
