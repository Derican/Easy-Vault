// 
// Decompiled by Procyon v0.6.0
// 

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
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ObeliskTileEntity extends TileEntity implements ITickableTileEntity
{
    private static final Random rand;
    
    public ObeliskTileEntity() {
        super((TileEntityType)ModBlocks.OBELISK_TILE_ENTITY);
    }
    
    public void tick() {
        if (!this.getLevel().isClientSide()) {
            final BlockState up = this.getLevel().getBlockState(this.getBlockPos().above());
            if (!(up.getBlock() instanceof ObeliskBlock)) {
                this.getLevel().setBlockAndUpdate(this.getBlockPos().above(), ModBlocks.OBELISK.defaultBlockState().setValue(StabilizerBlock.HALF, DoubleBlockHalf.UPPER));
            }
        }
        else {
            this.playEffects();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        final BlockPos pos = this.getBlockPos();
        final BlockState state = this.getLevel().getBlockState(pos);
        if (this.getLevel().getGameTime() % 5L != 0L) {
            return;
        }
        final ParticleManager mgr = Minecraft.getInstance().particleEngine;
        if ((int)state.getValue(ObeliskBlock.COMPLETION) > 0) {
            for (int count = 0; count < 3; ++count) {
                final double x = pos.getX() - 0.25 + ObeliskTileEntity.rand.nextFloat() * 1.5;
                final double y = pos.getY() + ObeliskTileEntity.rand.nextFloat() * 3.0f;
                final double z = pos.getZ() - 0.25 + ObeliskTileEntity.rand.nextFloat() * 1.5;
                final Particle fwParticle = mgr.createParticle((IParticleData)ParticleTypes.FIREWORK, x, y, z, 0.0, 0.0, 0.0);
                fwParticle.setColor(0.4f, 0.0f, 0.0f);
            }
        }
        else {
            for (int count = 0; count < 5; ++count) {
                final double x = pos.getX() + ObeliskTileEntity.rand.nextFloat();
                final double y = pos.getY() + ObeliskTileEntity.rand.nextFloat() * 10.0f;
                final double z = pos.getZ() + ObeliskTileEntity.rand.nextFloat();
                final Particle fwParticle = mgr.createParticle((IParticleData)ParticleTypes.FIREWORK, x, y, z, 0.0, 0.0, 0.0);
                fwParticle.setLifetime((int)(fwParticle.getLifetime() * 1.5f));
            }
        }
    }
    
    static {
        rand = new Random();
    }
}
