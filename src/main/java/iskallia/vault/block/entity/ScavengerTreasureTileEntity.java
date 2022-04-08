package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ScavengerTreasureTileEntity
        extends TileEntity implements ITickableTileEntity {
    private static final Random rand = new Random();

    protected ScavengerTreasureTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public ScavengerTreasureTileEntity() {
        super(ModBlocks.SCAVENGER_TREASURE_TILE_ENTITY);
    }


    public void tick() {
        if (this.level.isClientSide()) {
            playEffects();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        if (rand.nextInt(4) == 0) {
            ParticleManager mgr = (Minecraft.getInstance()).particleEngine;
            BlockPos pos = getBlockPos();


            Vector3d rPos = new Vector3d(pos.getX() + 0.5D + (rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 1.5D, pos.getY() + 0.5D + (rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 1.5D, pos.getZ() + 0.5D + (rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 1.5D);
            SimpleAnimatedParticle p = (SimpleAnimatedParticle) mgr.createParticle((IParticleData) ParticleTypes.FIREWORK, rPos.x, rPos.y, rPos.z, 0.0D, 0.0D, 0.0D);


            if (p != null) {
//                p.baseGravity = 0.0F;
                p.setColor(MiscUtils.blendColors(-3241472, -3229440, rand.nextFloat()));
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\ScavengerTreasureTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */