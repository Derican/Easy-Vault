package iskallia.vault.block;

import iskallia.vault.block.base.FillableAltarBlock;
import iskallia.vault.block.entity.TimeAltarTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModParticles;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.time.extension.TimeAltarExtension;
import iskallia.vault.world.vault.time.extension.TimeExtension;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.state.Property;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

public class TimeAltarBlock extends FillableAltarBlock<TimeAltarTileEntity> {
    public TimeAltarTileEntity createTileEntity(BlockState state, IBlockReader world) {
        return (TimeAltarTileEntity) ModBlocks.TIME_ALTAR_TILE_ENTITY.create();
    }


    public IParticleData getFlameParticle() {
        return (IParticleData) ModParticles.YELLOW_FLAME.get();
    }


    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.TIMEKEEPER;
    }


    public ActionResultType rightClicked(BlockState state, ServerWorld world, BlockPos pos, TimeAltarTileEntity tileEntity, ServerPlayerEntity player, ItemStack heldStack) {
        if (!tileEntity.initialized()) {
            return ActionResultType.SUCCESS;
        }
        if (player.isCreative()) {
            tileEntity.makeProgress(player, 1, sPlayer -> {
            });
            return ActionResultType.SUCCESS;
        }

        VaultRaid vault = VaultRaidData.get(world).getActiveFor(player);
        if (vault == null) {
            return ActionResultType.FAIL;
        }

        tileEntity.makeProgress(player, 1, sPlayer -> {
            PlayerFavourData data = PlayerFavourData.get(sPlayer.getLevel());
            if (rand.nextFloat() < getFavourChance((PlayerEntity) sPlayer, PlayerFavourData.VaultGodType.TIMEKEEPER)) {
                PlayerFavourData.VaultGodType vg = getAssociatedVaultGod();
                if (data.addFavour((PlayerEntity) sPlayer, vg, 1)) {
                    data.addFavour((PlayerEntity) sPlayer, vg.getOther(rand), -1);
                    FillableAltarBlock.playFavourInfo(sPlayer);
                }
            }
        });
        vault.getPlayers().forEach(vaultPlayer -> vaultPlayer.getTimer().addTime((TimeExtension) new TimeAltarExtension(-1200), 0));
        return ActionResultType.SUCCESS;
    }


    protected BlockState getSuccessChestState(BlockState altarState) {
        BlockState chestState = super.getSuccessChestState(altarState);
        return (BlockState) chestState.setValue((Property) ChestBlock.FACING, (Comparable) ((Direction) chestState.getValue((Property) FACING)).getOpposite());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\TimeAltarBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */