// 
// Decompiled by Procyon v0.6.0
// 

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

public class ScavengerTreasureTileEntity extends TileEntity implements ITickableTileEntity
{
    private static final Random rand;
    
    protected ScavengerTreasureTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super((TileEntityType)tileEntityTypeIn);
    }
    
    public ScavengerTreasureTileEntity() {
        super((TileEntityType)ModBlocks.SCAVENGER_TREASURE_TILE_ENTITY);
    }
    
    public void tick() {
        if (this.level.isClientSide()) {
            this.playEffects();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        if (ScavengerTreasureTileEntity.rand.nextInt(4) == 0) {
            final ParticleManager mgr = Minecraft.getInstance().particleEngine;
            final BlockPos pos = this.getBlockPos();
            final Vector3d rPos = new Vector3d(pos.getX() + 0.5 + (ScavengerTreasureTileEntity.rand.nextFloat() - ScavengerTreasureTileEntity.rand.nextFloat()) * (ScavengerTreasureTileEntity.rand.nextFloat() * 1.5), pos.getY() + 0.5 + (ScavengerTreasureTileEntity.rand.nextFloat() - ScavengerTreasureTileEntity.rand.nextFloat()) * (ScavengerTreasureTileEntity.rand.nextFloat() * 1.5), pos.getZ() + 0.5 + (ScavengerTreasureTileEntity.rand.nextFloat() - ScavengerTreasureTileEntity.rand.nextFloat()) * (ScavengerTreasureTileEntity.rand.nextFloat() * 1.5));
            final SimpleAnimatedParticle p = (SimpleAnimatedParticle)mgr.createParticle((IParticleData)ParticleTypes.FIREWORK, rPos.x, rPos.y, rPos.z, 0.0, 0.0, 0.0);
            if (p != null) {
                p.baseGravity = 0.0f;
                p.setColor(MiscUtils.blendColors(-3241472, -3229440, ScavengerTreasureTileEntity.rand.nextFloat()));
            }
        }
    }
    
    static {
        rand = new Random();
    }
}
