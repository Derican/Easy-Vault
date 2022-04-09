// 
// Decompiled by Procyon v0.6.0
// 

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

public class XPAltarBlock extends FillableAltarBlock<XpAltarTileEntity>
{
    @Override
    public XpAltarTileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return (XpAltarTileEntity)ModBlocks.XP_ALTAR_TILE_ENTITY.create();
    }
    
    @Override
    public IParticleData getFlameParticle() {
        return (IParticleData)ModParticles.BLUE_FLAME.get();
    }
    
    @Override
    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.OMNISCIENT;
    }
    
    @Nonnull
    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final BlockState state = super.getStateForPlacement(context);
        return state.setValue(XPAltarBlock.FACING, (state.getValue(XPAltarBlock.FACING)).getOpposite());
    }
    
    @Override
    public ActionResultType rightClicked(final BlockState state, final ServerWorld world, final BlockPos pos, final XpAltarTileEntity tileEntity, final ServerPlayerEntity player, final ItemStack heldStack) {
        if (!tileEntity.initialized()) {
            return ActionResultType.SUCCESS;
        }
        if (player.isCreative()) {
            tileEntity.makeProgress(player, tileEntity.getMaxProgress() - tileEntity.getCurrentProgress(), sPlayer -> {});
            return ActionResultType.SUCCESS;
        }
        if (player.experienceLevel <= 0) {
            return ActionResultType.FAIL;
        }
        final int levelDrain = Math.min(player.experienceLevel, tileEntity.getMaxProgress() - tileEntity.getCurrentProgress());
        player.setExperienceLevels(player.experienceLevel - levelDrain);
        tileEntity.makeProgress(player, levelDrain, sPlayer -> {
            final PlayerFavourData data = PlayerFavourData.get(sPlayer.getLevel());
            if (XPAltarBlock.rand.nextFloat() < FillableAltarBlock.getFavourChance((PlayerEntity)sPlayer, PlayerFavourData.VaultGodType.OMNISCIENT)) {
                final PlayerFavourData.VaultGodType vg = this.getAssociatedVaultGod();
                if (data.addFavour((PlayerEntity)sPlayer, vg, 1)) {
                    data.addFavour((PlayerEntity)sPlayer, vg.getOther(XPAltarBlock.rand), -1);
                    FillableAltarBlock.playFavourInfo(sPlayer);
                }
            }
            return;
        });
        return ActionResultType.SUCCESS;
    }
}
