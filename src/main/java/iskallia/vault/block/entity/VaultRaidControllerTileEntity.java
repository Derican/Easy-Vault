package iskallia.vault.block.entity;

import iskallia.vault.block.StabilizerBlock;
import iskallia.vault.config.RaidModifierConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModParticles;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.raid.RaidChallengeObjective;
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
import net.minecraft.state.Property;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
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
    private static final Random rand = new Random();
    private static final AxisAlignedBB RENDER_BOX = new AxisAlignedBB(-1.0D, -1.0D, -1.0D, 1.0D, 2.0D, 1.0D);

    private boolean triggeredRaid = false;
    private int activeTimeout = 0;

    private final LinkedHashMap<String, Float> raidModifiers = new LinkedHashMap<>();

    private final List<Object> particleReferences = new ArrayList();

    public VaultRaidControllerTileEntity() {
        super(ModBlocks.RAID_CONTROLLER_TILE_ENTITY);
    }

    public boolean isActive() {
        return (this.activeTimeout > 0);
    }


    public void tick() {
        if (!getLevel().isClientSide()) {
            BlockState up = getLevel().getBlockState(getBlockPos().above());
            if (!(up.getBlock() instanceof iskallia.vault.block.VaultRaidControllerBlock)) {
                getLevel().setBlockAndUpdate(getBlockPos().above(), (BlockState) ModBlocks.RAID_CONTROLLER_BLOCK.defaultBlockState().setValue((Property) StabilizerBlock.HALF, (Comparable) DoubleBlockHalf.UPPER));
            }

            if (this.activeTimeout > 0) {
                this.activeTimeout--;
                if (this.activeTimeout <= 0) {
                    markForUpdate();
                }
            }

            if (getLevel() instanceof ServerWorld) {
                ServerWorld sWorld = (ServerWorld) getLevel();
                VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, getBlockPos());
                if (vault != null) {
                    if (vault.getActiveRaid() != null && vault.getActiveRaid().getController().equals(getBlockPos())) {
                        boolean needsUpdate = (this.activeTimeout <= 0);
                        this.activeTimeout = 20;
                        if (needsUpdate) {
                            markForUpdate();
                        }
                    }

                    vault.getActiveObjective(RaidChallengeObjective.class).ifPresent(raidObjective -> {
                        if (this.raidModifiers.isEmpty()) {
                            boolean cannotGetArtifact = vault.getActiveModifiersFor(PlayerFilter.any(), InventoryRestoreModifier.class).stream().anyMatch(InventoryRestoreModifier::preventsArtifact);


                            int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();


                            RaidModifier addedModifier = ModConfigs.RAID_MODIFIER_CONFIG.getRandomModifier(level, true, cannotGetArtifact).map((RaidModifierConfig.RollableModifier modifier) -> {
                                return modifier.getModifier();
                            }).orElse(null);


                            if (addedModifier != null && !(addedModifier instanceof iskallia.vault.world.vault.logic.objective.raid.modifier.ModifierDoublingModifier)) {
                                ModConfigs.RAID_MODIFIER_CONFIG.getRandomModifier(level, false, cannotGetArtifact).ifPresent((RaidModifierConfig.RollableModifier modifier) -> {
                                });
                            }


                            markForUpdate();
                        }
                    });
                }
            }
        } else {
            setupParticles();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void setupParticles() {
        if (this.particleReferences.size() < 3) {
            int toAdd = 3 - this.particleReferences.size();
            for (int i = 0; i < toAdd; i++) {
                ParticleManager particleManager = (Minecraft.getInstance()).particleEngine;
                Particle p = particleManager.createParticle((IParticleData) ModParticles.RAID_EFFECT_CUBE.get(), this.worldPosition
                        .getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);

                this.particleReferences.add(p);
            }
        }
        this.particleReferences.removeIf(ref -> !((Particle) ref).isAlive());


        if (!isActive()) {
            return;
        }

        ParticleManager mgr = (Minecraft.getInstance()).particleEngine;
        Color c = new Color(11932948);

        if (rand.nextInt(3) == 0) {


            Vector3d pPos = new Vector3d(this.worldPosition.getX() + 0.5D + rand.nextFloat() * 3.5D * (rand.nextBoolean() ? 1 : -1), this.worldPosition.getY() + 2.1D + rand.nextFloat() * 3.5D * (rand.nextBoolean() ? 1 : -1), this.worldPosition.getZ() + 0.5D + rand.nextFloat() * 3.5D * (rand.nextBoolean() ? 1 : -1));

            SimpleAnimatedParticle fwParticle = (SimpleAnimatedParticle) mgr.createParticle((IParticleData) ParticleTypes.FIREWORK, pPos
                    .x(), pPos.y(), pPos.z(), 0.0D, 0.0D, 0.0D);

            fwParticle.setColor(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F);
//            fwParticle.baseGravity = -0.001F;
            fwParticle.setLifetime(fwParticle.getLifetime() / 2);


            pPos = new Vector3d(this.worldPosition.getX() + 0.5D + rand.nextFloat() * 0.3D * (rand.nextBoolean() ? 1 : -1), this.worldPosition.getY() + 2.25D + rand.nextFloat() * 0.3D * (rand.nextBoolean() ? 1 : -1), this.worldPosition.getZ() + 0.5D + rand.nextFloat() * 0.3D * (rand.nextBoolean() ? 1 : -1));
            fwParticle = (SimpleAnimatedParticle) mgr.createParticle((IParticleData) ParticleTypes.FIREWORK, pPos
                    .x(), pPos.y(), pPos.z(), 0.0D, 0.0D, 0.0D);

            fwParticle.setColor(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F);
//            fwParticle.baseGravity = 0.0F;
        }
    }

    private void markForUpdate() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
            this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
            setChanged();
        }
    }

    public boolean didTriggerRaid() {
        return this.triggeredRaid;
    }

    public void setTriggeredRaid(boolean triggeredRaid) {
        this.triggeredRaid = triggeredRaid;
        markForUpdate();
    }

    public LinkedHashMap<String, Float> getRaidModifiers() {
        return this.raidModifiers;
    }

    public List<ITextComponent> getModifierDisplay() {
        return (List<ITextComponent>) this.raidModifiers.entrySet().stream()
                .map(modifierEntry -> {
                    RaidModifier modifier = ModConfigs.RAID_MODIFIER_CONFIG.getByName((String) modifierEntry.getKey());


                    return (modifier == null) ? null : new Tuple(modifier, modifierEntry.getValue());
                }).filter(Objects::nonNull)
                .map(tpl -> ((RaidModifier) tpl.getA()).getDisplay(((Float) tpl.getB()).floatValue()))
                .collect(Collectors.toList());
    }


    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        this.activeTimeout = tag.getInt("timeout");
        this.triggeredRaid = tag.getBoolean("triggeredRaid");

        this.raidModifiers.clear();
        ListNBT modifiers = tag.getList("raidModifiers", 10);
        for (int i = 0; i < modifiers.size(); i++) {
            CompoundNBT modifierTag = modifiers.getCompound(i);
            String modifier = modifierTag.getString("name");
            float value = modifierTag.getFloat("value");
            this.raidModifiers.put(modifier, Float.valueOf(value));
        }
    }


    public CompoundNBT save(CompoundNBT tag) {
        tag.putInt("timeout", this.activeTimeout);
        tag.putBoolean("triggeredRaid", this.triggeredRaid);

        ListNBT modifiers = new ListNBT();
        this.raidModifiers.forEach((modifier, value) -> {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("name", modifier);
            nbt.putFloat("value", value.floatValue());
            modifiers.add(nbt);
        });
        tag.put("raidModifiers", (INBT) modifiers);
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\VaultRaidControllerTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */