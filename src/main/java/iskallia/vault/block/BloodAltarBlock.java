// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import iskallia.vault.block.base.FillableAltarBlock;
import iskallia.vault.block.entity.BloodAltarTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModParticles;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
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

public class BloodAltarBlock extends FillableAltarBlock<BloodAltarTileEntity>
{
    @Override
    public BloodAltarTileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return (BloodAltarTileEntity)ModBlocks.BLOOD_ALTAR_TILE_ENTITY.create();
    }
    
    @Override
    public IParticleData getFlameParticle() {
        return (IParticleData)ModParticles.GREEN_FLAME.get();
    }
    
    @Override
    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.BENEVOLENT;
    }
    
    @Nonnull
    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final BlockState state = super.getStateForPlacement(context);
        return state.setValue(BloodAltarBlock.FACING, (state.getValue(BloodAltarBlock.FACING)).getOpposite());
    }
    
    @Override
    public ActionResultType rightClicked(final BlockState state, final ServerWorld world, final BlockPos pos, final BloodAltarTileEntity tileEntity, final ServerPlayerEntity player, final ItemStack heldStack) {
        if (!tileEntity.initialized()) {
            return ActionResultType.SUCCESS;
        }
        if (player.isCreative()) {
            tileEntity.makeProgress(player, 1, sPlayer -> {});
            return ActionResultType.SUCCESS;
        }
        EntityHelper.changeHealth((LivingEntity)player, -2);
        tileEntity.makeProgress(player, 1, sPlayer -> {
            final PlayerFavourData data = PlayerFavourData.get(sPlayer.getLevel());
            if (BloodAltarBlock.rand.nextFloat() < FillableAltarBlock.getFavourChance((PlayerEntity)sPlayer, PlayerFavourData.VaultGodType.BENEVOLENT)) {
                final PlayerFavourData.VaultGodType vg = this.getAssociatedVaultGod();
                if (data.addFavour((PlayerEntity)sPlayer, vg, 1)) {
                    data.addFavour((PlayerEntity)sPlayer, vg.getOther(BloodAltarBlock.rand), -1);
                    FillableAltarBlock.playFavourInfo(sPlayer);
                }
            }
            return;
        });
        return ActionResultType.SUCCESS;
    }
}
