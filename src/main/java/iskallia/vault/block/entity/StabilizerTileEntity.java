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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StabilizerTileEntity
        extends TileEntity
        implements ITickableTileEntity {
    private static final Random rand = new Random();
    private static final AxisAlignedBB RENDER_BOX = new AxisAlignedBB(-1.0D, -1.0D, -1.0D, 1.0D, 2.0D, 1.0D);

    private boolean active = false;
    private int timeout = 20;

    private final List<Object> particleReferences = new ArrayList();

    public StabilizerTileEntity() {
        super(ModBlocks.STABILIZER_TILE_ENTITY);
    }


    public void tick() {
        if (!getLevel().isClientSide()) {
            BlockState up = getLevel().getBlockState(getBlockPos().above());
            if (!(up.getBlock() instanceof StabilizerBlock)) {
                getLevel().setBlockAndUpdate(getBlockPos().above(), (BlockState) ModBlocks.STABILIZER.defaultBlockState().setValue((Property) StabilizerBlock.HALF, (Comparable) DoubleBlockHalf.UPPER));
            }

            if (this.active && this.timeout > 0) {
                this.timeout--;
                if (this.timeout <= 0) {
                    this.active = false;
                    markForUpdate();
                }
            }
        } else {
            setupParticle();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void setupParticle() {
        if (this.particleReferences.size() < 3) {
            int toAdd = 3 - this.particleReferences.size();
            for (int i = 0; i < toAdd; i++) {
                ParticleManager mgr = (Minecraft.getInstance()).particleEngine;
                Particle p = mgr.createParticle((IParticleData) ModParticles.STABILIZER_CUBE.get(), this.worldPosition
                        .getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);

                this.particleReferences.add(p);
            }
        }
        this.particleReferences.removeIf(ref -> !((Particle) ref).isAlive());

        if (isActive()) {


            Vector3d particlePos = new Vector3d((this.worldPosition.getX() + rand.nextFloat()), (this.worldPosition.getY() + rand.nextFloat() * 2.0F), (this.worldPosition.getZ() + rand.nextFloat()));

            ParticleManager mgr = (Minecraft.getInstance()).particleEngine;
            SimpleAnimatedParticle p = (SimpleAnimatedParticle) mgr.createParticle((IParticleData) ParticleTypes.FIREWORK, particlePos.x, particlePos.y, particlePos.z, 0.0D, 0.0D, 0.0D);


//            p.baseGravity = 0.0F;
            p.setColor(301982);
        }
    }

    public void setActive() {
        this.active = true;
        this.timeout = 20;
        markForUpdate();
    }

    public boolean isActive() {
        return this.active;
    }

    private void markForUpdate() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
            this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
            setChanged();
        }
    }


    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        this.active = tag.getBoolean("active");
    }


    public CompoundNBT save(CompoundNBT tag) {
        tag.putBoolean("active", this.active);
        return super.save(tag);
    }


    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        save(nbt);
        return nbt;
    }


    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        load(state, nbt);
    }


    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, getUpdateTag());
    }


    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getTag();
        handleUpdateTag(getBlockState(), nbt);
    }


    public AxisAlignedBB getRenderBoundingBox() {
        return RENDER_BOX.move(getBlockPos());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\StabilizerTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */