// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.entity;

import iskallia.vault.block.StabilizerBlock;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModParticles;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.Property;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StabilizerTileEntity extends TileEntity implements ITickableTileEntity
{
    private static final Random rand;
    private static final AxisAlignedBB RENDER_BOX;
    private boolean active;
    private int timeout;
    private final List<Object> particleReferences;
    
    public StabilizerTileEntity() {
        super((TileEntityType)ModBlocks.STABILIZER_TILE_ENTITY);
        this.active = false;
        this.timeout = 20;
        this.particleReferences = new ArrayList<Object>();
    }
    
    public void tick() {
        if (!this.getLevel().isClientSide()) {
            final BlockState up = this.getLevel().getBlockState(this.getBlockPos().above());
            if (!(up.getBlock() instanceof StabilizerBlock)) {
                this.getLevel().setBlockAndUpdate(this.getBlockPos().above(), ModBlocks.STABILIZER.defaultBlockState().setValue(StabilizerBlock.HALF, DoubleBlockHalf.UPPER));
            }
            if (this.active && this.timeout > 0) {
                --this.timeout;
                if (this.timeout <= 0) {
                    this.active = false;
                    this.markForUpdate();
                }
            }
        }
        else {
            this.setupParticle();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void setupParticle() {
        if (this.particleReferences.size() < 3) {
            for (int toAdd = 3 - this.particleReferences.size(), i = 0; i < toAdd; ++i) {
                final ParticleManager mgr = Minecraft.getInstance().particleEngine;
                final Particle p = mgr.createParticle((IParticleData)ModParticles.STABILIZER_CUBE.get(), this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, 0.0, 0.0, 0.0);
                this.particleReferences.add(p);
            }
        }
        this.particleReferences.removeIf(ref -> !((Particle)ref).isAlive());
        if (this.isActive()) {
            final Vector3d particlePos = new Vector3d((double)(this.worldPosition.getX() + StabilizerTileEntity.rand.nextFloat()), (double)(this.worldPosition.getY() + StabilizerTileEntity.rand.nextFloat() * 2.0f), (double)(this.worldPosition.getZ() + StabilizerTileEntity.rand.nextFloat()));
            final ParticleManager mgr2 = Minecraft.getInstance().particleEngine;
            final SimpleAnimatedParticle p2 = (SimpleAnimatedParticle)mgr2.createParticle((IParticleData)ParticleTypes.FIREWORK, particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
//            p2.baseGravity = 0.0f;
            p2.setColor(301982);
        }
    }
    
    public void setActive() {
        this.active = true;
        this.timeout = 20;
        this.markForUpdate();
    }
    
    public boolean isActive() {
        return this.active;
    }
    
    private void markForUpdate() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
            this.setChanged();
        }
    }
    
    public void load(final BlockState state, final CompoundNBT tag) {
        super.load(state, tag);
        this.active = tag.getBoolean("active");
    }
    
    public CompoundNBT save(final CompoundNBT tag) {
        tag.putBoolean("active", this.active);
        return super.save(tag);
    }
    
    public CompoundNBT getUpdateTag() {
        final CompoundNBT nbt = super.getUpdateTag();
        this.save(nbt);
        return nbt;
    }
    
    public void handleUpdateTag(final BlockState state, final CompoundNBT nbt) {
        this.load(state, nbt);
    }
    
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
    }
    
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        final CompoundNBT nbt = pkt.getTag();
        this.handleUpdateTag(this.getBlockState(), nbt);
    }
    
    public AxisAlignedBB getRenderBoundingBox() {
        return StabilizerTileEntity.RENDER_BOX.move(this.getBlockPos());
    }
    
    static {
        rand = new Random();
        RENDER_BOX = new AxisAlignedBB(-1.0, -1.0, -1.0, 1.0, 2.0, 1.0);
    }
}
