package iskallia.vault.block.entity;

import net.minecraft.network.NetworkManager;

import javax.annotation.Nullable;

import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import iskallia.vault.util.nbt.NBTHelper;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.CompoundNBT;

import java.util.Collection;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Iterator;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.client.particle.Particle;
import iskallia.vault.init.ModParticles;
import net.minecraft.particles.IParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.world.World;
import iskallia.vault.block.StabilizerCompassBlock;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectSummonAndKillBossesObjective;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.state.Property;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.block.BlockState;
import iskallia.vault.block.StabilizerBlock;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.tileentity.TileEntityType;
import iskallia.vault.init.ModBlocks;

import java.util.List;

import net.minecraft.util.Direction;

import java.util.Set;

import net.minecraft.util.math.AxisAlignedBB;

import java.util.Random;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class StabilizerTileEntity extends TileEntity implements ITickableTileEntity {
    private static final Random rand;
    private static final AxisAlignedBB RENDER_BOX;
    private boolean active;
    private int timeout;
    private final Set<Direction> highlightDirections;
    private final List<Object> particleReferences;

    public StabilizerTileEntity() {
        super((TileEntityType) ModBlocks.STABILIZER_TILE_ENTITY);
        this.active = false;
        this.timeout = 20;
        this.highlightDirections = new HashSet<Direction>();
        this.particleReferences = new ArrayList<Object>();
    }

    public void tick() {
        final World world = this.getLevel();
        if (world instanceof ServerWorld) {
            final ServerWorld sWorld = (ServerWorld) world;
            final BlockState up = world.getBlockState(this.getBlockPos().above());
            if (!(up.getBlock() instanceof StabilizerBlock)) {
                world.setBlockAndUpdate(this.getBlockPos().above(), (BlockState) ModBlocks.STABILIZER.defaultBlockState().setValue((Property) StabilizerBlock.HALF, (Comparable) DoubleBlockHalf.UPPER));
            }
            final VaultRaid raid = VaultRaidData.get(sWorld).getAt(sWorld, this.getBlockPos());
            if (raid != null) {
                raid.getActiveObjective(ArchitectSummonAndKillBossesObjective.class).ifPresent(objective -> Direction.Plane.HORIZONTAL.stream().forEach(dir -> {
                    final BlockPos compassPos = this.getBlockPos().below().relative(dir);
                    sWorld.setBlockAndUpdate(compassPos, (BlockState) ModBlocks.STABILIZER_COMPASS.defaultBlockState().setValue((Property) StabilizerCompassBlock.DIRECTION, (Comparable) dir));
                }));
            }
            if (this.active && this.timeout > 0) {
                --this.timeout;
                if (this.timeout <= 0) {
                    this.active = false;
                    this.markForUpdate();
                }
            }
        } else {
            this.setupParticle();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void setupParticle() {
        if (this.particleReferences.size() < 3) {
            for (int toAdd = 3 - this.particleReferences.size(), i = 0; i < toAdd; ++i) {
                final ParticleManager mgr = Minecraft.getInstance().particleEngine;
                final Particle p = mgr.createParticle((IParticleData) ModParticles.STABILIZER_CUBE.get(), this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, 0.0, 0.0, 0.0);
                this.particleReferences.add(p);
            }
        }
        this.particleReferences.removeIf(ref -> !((Particle) ref).isAlive());
        if (this.isActive()) {
            final Vector3d particlePos = new Vector3d((double) (this.worldPosition.getX() + StabilizerTileEntity.rand.nextFloat()), (double) (this.worldPosition.getY() + StabilizerTileEntity.rand.nextFloat() * 2.0f), (double) (this.worldPosition.getZ() + StabilizerTileEntity.rand.nextFloat()));
            final ParticleManager mgr2 = Minecraft.getInstance().particleEngine;
            SimpleAnimatedParticle p2 = (SimpleAnimatedParticle) mgr2.createParticle((IParticleData) ParticleTypes.FIREWORK, particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
//            TODO: check if the omit is acceptable
//            p2.baseGravity = 0.0f;
            p2.setColor(301982);
            for (final Direction voteDirection : this.highlightDirections) {
                final Vector3d dirPos = new Vector3d((double) (this.worldPosition.getX() + StabilizerTileEntity.rand.nextFloat()), this.worldPosition.getY() + StabilizerTileEntity.rand.nextFloat() * 0.1, (double) (this.worldPosition.getZ() + StabilizerTileEntity.rand.nextFloat())).add((double) voteDirection.getStepX(), (double) voteDirection.getStepY(), (double) voteDirection.getStepZ());
                p2 = (SimpleAnimatedParticle) mgr2.createParticle((IParticleData) ParticleTypes.FIREWORK, dirPos.x, dirPos.y, dirPos.z, voteDirection.getStepX() * StabilizerTileEntity.rand.nextFloat() * 0.18, voteDirection.getStepY() * StabilizerTileEntity.rand.nextFloat() * 0.18, voteDirection.getStepZ() * StabilizerTileEntity.rand.nextFloat() * 0.18);
//                TODO: check if the omit is acceptable
//                p2.baseGravity = 4.0E-4f;
                p2.setColor(this.getDirectionColor(voteDirection));
            }
        }
    }

    private int getDirectionColor(final Direction direction) {
        switch (direction) {
            case NORTH: {
                return 13375004;
            }
            case SOUTH: {
                return 1636588;
            }
            case WEST: {
                return 14985778;
            }
            case EAST: {
                return 2805028;
            }
            default: {
                return 16777215;
            }
        }
    }

    public void setActive() {
        this.active = true;
        this.timeout = 20;
        this.markForUpdate();
    }

    public void setHighlightDirections(final Collection<Direction> directions) {
        this.highlightDirections.clear();
        this.highlightDirections.addAll(directions);
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
        this.highlightDirections.clear();
        if (tag.contains("directions", 9)) {
            this.highlightDirections.addAll(NBTHelper.readList(tag, "directions", IntNBT.class, nbt -> Direction.values()[nbt.getAsInt()]));
        }
    }

    public CompoundNBT save(final CompoundNBT tag) {
        tag.putBoolean("active", this.active);
        NBTHelper.writeList(tag, "directions", (Collection<Direction>) this.highlightDirections, IntNBT.class, dir -> IntNBT.valueOf(dir.ordinal()));
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
