package iskallia.vault.block.entity;

import iskallia.vault.block.ObeliskBlock;
import iskallia.vault.block.StabilizerBlock;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.Property;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ObeliskTileEntity extends TileEntity implements ITickableTileEntity {
    private static final Random rand = new Random();

    public ObeliskTileEntity() {
        super(ModBlocks.OBELISK_TILE_ENTITY);
    }


    public void tick() {
        if (!getLevel().isClientSide()) {
            BlockState up = getLevel().getBlockState(getBlockPos().above());
            if (!(up.getBlock() instanceof ObeliskBlock)) {
                getLevel().setBlockAndUpdate(getBlockPos().above(), (BlockState) ModBlocks.OBELISK.defaultBlockState().setValue((Property) StabilizerBlock.HALF, (Comparable) DoubleBlockHalf.UPPER));
            }
        } else {
            playEffects();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        BlockPos pos = getBlockPos();
        BlockState state = getLevel().getBlockState(pos);

        if (getLevel().getGameTime() % 5L != 0L) {
            return;
        }

        ParticleManager mgr = (Minecraft.getInstance()).particleEngine;
        if (((Integer) state.getValue((Property) ObeliskBlock.COMPLETION)).intValue() > 0) {
            for (int count = 0; count < 3; count++) {
                double x = pos.getX() - 0.25D + rand.nextFloat() * 1.5D;
                double y = (pos.getY() + rand.nextFloat() * 3.0F);
                double z = pos.getZ() - 0.25D + rand.nextFloat() * 1.5D;

                Particle fwParticle = mgr.createParticle((IParticleData) ParticleTypes.FIREWORK, x, y, z, 0.0D, 0.0D, 0.0D);
                fwParticle.setColor(0.4F, 0.0F, 0.0F);
            }
        } else {
            for (int count = 0; count < 5; count++) {
                double x = (pos.getX() + rand.nextFloat());
                double y = (pos.getY() + rand.nextFloat() * 10.0F);
                double z = (pos.getZ() + rand.nextFloat());

                Particle fwParticle = mgr.createParticle((IParticleData) ParticleTypes.FIREWORK, x, y, z, 0.0D, 0.0D, 0.0D);
                fwParticle.setLifetime((int) (fwParticle.getLifetime() * 1.5F));
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\ObeliskTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */