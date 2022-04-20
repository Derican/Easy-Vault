package iskallia.vault.block.entity;

import iskallia.vault.block.StabilizerBlock;
import iskallia.vault.block.VaultRaidControllerBlock;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModParticles;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.raid.RaidChallengeObjective;
import iskallia.vault.world.vault.logic.objective.raid.modifier.ModifierDoublingModifier;
import iskallia.vault.world.vault.logic.objective.raid.modifier.RaidModifier;
import iskallia.vault.world.vault.modifier.InventoryRestoreModifier;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class VaultRaidControllerTileEntity extends TileEntity implements ITickableTileEntity {
    private static final Random rand;
    private static final AxisAlignedBB RENDER_BOX;
    private boolean triggeredRaid;
    private int activeTimeout;
    private final LinkedHashMap<String, Float> raidModifiers;
    private final List<Object> particleReferences;

    public VaultRaidControllerTileEntity() {
        super((TileEntityType) ModBlocks.RAID_CONTROLLER_TILE_ENTITY);
        this.triggeredRaid = false;
        this.activeTimeout = 0;
        this.raidModifiers = new LinkedHashMap<String, Float>();
        this.particleReferences = new ArrayList<Object>();
    }

    public boolean isActive() {
        return this.activeTimeout > 0;
    }

    public void tick() {
        if (!this.getLevel().isClientSide()) {
            final BlockState up = this.getLevel().getBlockState(this.getBlockPos().above());
            if (!(up.getBlock() instanceof VaultRaidControllerBlock)) {
                this.getLevel().setBlockAndUpdate(this.getBlockPos().above(), ModBlocks.RAID_CONTROLLER_BLOCK.defaultBlockState().setValue(StabilizerBlock.HALF, DoubleBlockHalf.UPPER));
            }
            if (this.activeTimeout > 0) {
                --this.activeTimeout;
                if (this.activeTimeout <= 0) {
                    this.markForUpdate();
                }
            }
            if (this.getLevel() instanceof ServerWorld) {
                final ServerWorld sWorld = (ServerWorld) this.getLevel();
                final VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, this.getBlockPos());
                if (vault != null) {
                    if (vault.getActiveRaid() != null && vault.getActiveRaid().getController().equals(this.getBlockPos())) {
                        final boolean needsUpdate = this.activeTimeout <= 0;
                        this.activeTimeout = 20;
                        if (needsUpdate) {
                            this.markForUpdate();
                        }
                    }
                    vault.getActiveObjective(RaidChallengeObjective.class).ifPresent(raidObjective -> {
                        if (this.raidModifiers.isEmpty()) {
                            final boolean cannotGetArtifact = vault.getActiveModifiersFor(PlayerFilter.any(), InventoryRestoreModifier.class).stream().anyMatch(InventoryRestoreModifier::preventsArtifact);
                            final int level = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
                            final RaidModifier addedModifier = ModConfigs.RAID_MODIFIER_CONFIG.getRandomModifier(level, true, cannotGetArtifact).map(modifier -> {
                                final RaidModifier mod = modifier.getModifier();
                                if (mod != null) {
                                    this.raidModifiers.put(mod.getName(), modifier.getRandomValue());
                                }
                                return (RaidModifier) (RaidModifier) mod;
                            }).orElse(null);
                            if (addedModifier != null && !(addedModifier instanceof ModifierDoublingModifier)) {
                                ModConfigs.RAID_MODIFIER_CONFIG.getRandomModifier(level, false, cannotGetArtifact).ifPresent(modifier -> {
                                    final RaidModifier mod2 = modifier.getModifier();
                                    if (mod2 != null) {
                                        this.raidModifiers.put(mod2.getName(), modifier.getRandomValue());
                                    }
                                    return;
                                });
                            }
                            this.markForUpdate();
                        }
                    });
                }
            }
        } else {
            this.setupParticles();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void setupParticles() {
        if (this.particleReferences.size() < 3) {
            for (int toAdd = 3 - this.particleReferences.size(), i = 0; i < toAdd; ++i) {
                final ParticleManager mgr = Minecraft.getInstance().particleEngine;
                final Particle p = mgr.createParticle((IParticleData) ModParticles.RAID_EFFECT_CUBE.get(), this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, 0.0, 0.0, 0.0);
                this.particleReferences.add(p);
            }
        }
        this.particleReferences.removeIf(ref -> !((Particle) ref).isAlive());
        if (!this.isActive()) {
            return;
        }
        final ParticleManager mgr2 = Minecraft.getInstance().particleEngine;
        final Color c = new Color(11932948);
        if (VaultRaidControllerTileEntity.rand.nextInt(3) == 0) {
            Vector3d pPos = new Vector3d(this.worldPosition.getX() + 0.5 + VaultRaidControllerTileEntity.rand.nextFloat() * 3.5 * (VaultRaidControllerTileEntity.rand.nextBoolean() ? 1 : -1), this.worldPosition.getY() + 2.1 + VaultRaidControllerTileEntity.rand.nextFloat() * 3.5 * (VaultRaidControllerTileEntity.rand.nextBoolean() ? 1 : -1), this.worldPosition.getZ() + 0.5 + VaultRaidControllerTileEntity.rand.nextFloat() * 3.5 * (VaultRaidControllerTileEntity.rand.nextBoolean() ? 1 : -1));
            SimpleAnimatedParticle fwParticle = (SimpleAnimatedParticle) mgr2.createParticle((IParticleData) ParticleTypes.FIREWORK, pPos.x(), pPos.y(), pPos.z(), 0.0, 0.0, 0.0);
            fwParticle.setColor(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f);
//            TODO: check if the omit is acceptable
//            fwParticle.baseGravity = -0.001f;
            fwParticle.setLifetime(fwParticle.getLifetime() / 2);
            pPos = new Vector3d(this.worldPosition.getX() + 0.5 + VaultRaidControllerTileEntity.rand.nextFloat() * 0.3 * (VaultRaidControllerTileEntity.rand.nextBoolean() ? 1 : -1), this.worldPosition.getY() + 2.25 + VaultRaidControllerTileEntity.rand.nextFloat() * 0.3 * (VaultRaidControllerTileEntity.rand.nextBoolean() ? 1 : -1), this.worldPosition.getZ() + 0.5 + VaultRaidControllerTileEntity.rand.nextFloat() * 0.3 * (VaultRaidControllerTileEntity.rand.nextBoolean() ? 1 : -1));
            fwParticle = (SimpleAnimatedParticle) mgr2.createParticle((IParticleData) ParticleTypes.FIREWORK, pPos.x(), pPos.y(), pPos.z(), 0.0, 0.0, 0.0);
            fwParticle.setColor(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f);
//            TODO: check if the omit is acceptable
//            fwParticle.baseGravity = 0.0f;
        }
    }

    private void markForUpdate() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
            this.setChanged();
        }
    }

    public boolean didTriggerRaid() {
        return this.triggeredRaid;
    }

    public void setTriggeredRaid(final boolean triggeredRaid) {
        this.triggeredRaid = triggeredRaid;
        this.markForUpdate();
    }

    public LinkedHashMap<String, Float> getRaidModifiers() {
        return this.raidModifiers;
    }

    public List<ITextComponent> getModifierDisplay() {
        return this.raidModifiers.entrySet().stream().map(modifierEntry -> {
            final RaidModifier modifier = ModConfigs.RAID_MODIFIER_CONFIG.getByName(modifierEntry.getKey());
            if (modifier == null) {
                return null;
            } else {
                return new Tuple(modifier, modifierEntry.getValue());
            }
        }).filter(Objects::nonNull).map(tpl -> ((RaidModifier) tpl.getA()).getDisplay((float) tpl.getB())).collect(Collectors.toList());
    }

    public void load(final BlockState state, final CompoundNBT tag) {
        super.load(state, tag);
        this.activeTimeout = tag.getInt("timeout");
        this.triggeredRaid = tag.getBoolean("triggeredRaid");
        this.raidModifiers.clear();
        final ListNBT modifiers = tag.getList("raidModifiers", 10);
        for (int i = 0; i < modifiers.size(); ++i) {
            final CompoundNBT modifierTag = modifiers.getCompound(i);
            final String modifier = modifierTag.getString("name");
            final float value = modifierTag.getFloat("value");
            this.raidModifiers.put(modifier, value);
        }
    }

    public CompoundNBT save(final CompoundNBT tag) {
        tag.putInt("timeout", this.activeTimeout);
        tag.putBoolean("triggeredRaid", this.triggeredRaid);
        final ListNBT modifiers = new ListNBT();
        this.raidModifiers.forEach((modifier, value) -> {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putString("name", modifier);
            nbt.putFloat("value", (float) value);
            modifiers.add(nbt);
            return;
        });
        tag.put("raidModifiers", (INBT) modifiers);
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
        return VaultRaidControllerTileEntity.RENDER_BOX.move(this.getBlockPos());
    }

    static {
        rand = new Random();
        RENDER_BOX = new AxisAlignedBB(-1.0, -1.0, -1.0, 1.0, 2.0, 1.0);
    }
}
