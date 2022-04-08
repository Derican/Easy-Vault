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

public class BloodAltarBlock extends FillableAltarBlock<BloodAltarTileEntity> {
    public BloodAltarTileEntity createTileEntity(BlockState state, IBlockReader world) {
        return (BloodAltarTileEntity) ModBlocks.BLOOD_ALTAR_TILE_ENTITY.create();
    }


    public IParticleData getFlameParticle() {
        return (IParticleData) ModParticles.GREEN_FLAME.get();
    }


    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.BENEVOLENT;
    }


    @Nonnull
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return (BlockState) state
                .setValue((Property) FACING, (Comparable) ((Direction) state.getValue((Property) FACING)).getOpposite());
    }


    public ActionResultType rightClicked(BlockState state, ServerWorld world, BlockPos pos, BloodAltarTileEntity tileEntity, ServerPlayerEntity player, ItemStack heldStack) {
        if (!tileEntity.initialized()) {
            return ActionResultType.SUCCESS;
        }
        if (player.isCreative()) {
            tileEntity.makeProgress(player, 1, sPlayer -> {
            });
            return ActionResultType.SUCCESS;
        }

        EntityHelper.changeHealth((LivingEntity) player, -2);

        tileEntity.makeProgress(player, 1, sPlayer -> {
            PlayerFavourData data = PlayerFavourData.get(sPlayer.getLevel());
            if (rand.nextFloat() < getFavourChance((PlayerEntity) sPlayer, PlayerFavourData.VaultGodType.BENEVOLENT)) {
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\BloodAltarBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */