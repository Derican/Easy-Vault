package iskallia.vault.block;

import iskallia.vault.block.base.FillableAltarBlock;
import iskallia.vault.block.entity.XpAltarTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModParticles;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.state.Property;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class XPAltarBlock extends FillableAltarBlock<XpAltarTileEntity> {
    public XpAltarTileEntity createTileEntity(BlockState state, IBlockReader world) {
        return (XpAltarTileEntity) ModBlocks.XP_ALTAR_TILE_ENTITY.create();
    }


    public IParticleData getFlameParticle() {
        return (IParticleData) ModParticles.BLUE_FLAME.get();
    }


    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.OMNISCIENT;
    }


    @Nonnull
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return (BlockState) state
                .setValue((Property) FACING, (Comparable) ((Direction) state.getValue((Property) FACING)).getOpposite());
    }


    public ActionResultType rightClicked(BlockState state, ServerWorld world, BlockPos pos, XpAltarTileEntity tileEntity, ServerPlayerEntity player, ItemStack heldStack) {
        if (!tileEntity.initialized()) {
            return ActionResultType.SUCCESS;
        }
        if (player.isCreative()) {
            tileEntity.makeProgress(player, tileEntity.getMaxProgress() - tileEntity.getCurrentProgress(), sPlayer -> {
            });
            return ActionResultType.SUCCESS;
        }
        if (player.experienceLevel <= 0) {
            return ActionResultType.FAIL;
        }

        int levelDrain = Math.min(player.experienceLevel, tileEntity.getMaxProgress() - tileEntity.getCurrentProgress());
        player.setExperienceLevels(player.experienceLevel - levelDrain);

        tileEntity.makeProgress(player, levelDrain, sPlayer -> {
            PlayerFavourData data = PlayerFavourData.get(sPlayer.getLevel());
            if (rand.nextFloat() < getFavourChance((PlayerEntity) sPlayer, PlayerFavourData.VaultGodType.OMNISCIENT)) {
                PlayerFavourData.VaultGodType vg = getAssociatedVaultGod();
                if (data.addFavour((PlayerEntity) sPlayer, vg, 1)) {
                    data.addFavour((PlayerEntity) sPlayer, vg.getOther(rand), -1);
                    FillableAltarBlock.playFavourInfo(sPlayer);
                }
            }
        });
        return ActionResultType.SUCCESS;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\XPAltarBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */