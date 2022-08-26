//
// Decompiled by Procyon v0.6.0
//

package iskallia.vault.entity;

import net.minecraftforge.common.util.INBTSerializable;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.enchantment.EnchantmentHelper;
import iskallia.vault.init.ModConfigs;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.entity.Entity;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.LivingEntity;

import java.util.Map;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;

import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.player.VaultRunner;

import java.util.List;
import java.util.ArrayList;

import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.entity.EntityType;

import java.util.UUID;

import iskallia.vault.nbt.VMapNBT;
import net.minecraft.entity.monster.VexEntity;

public class EyestalkEntity extends VexEntity {
    public VMapNBT<UUID, TargetData> targetCooldown;
    public UUID currentTarget;
    public UUID mother;

    public EyestalkEntity(final EntityType<? extends VexEntity> p_i50190_1_, final World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.targetCooldown = VMapNBT.ofUUID(TargetData::new);
        this.currentTarget = null;
        this.mother = null;
    }

    protected void registerGoals() {
    }

    public void addAdditionalSaveData(final CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        if (this.currentTarget != null) {
            nbt.putString("CurrentTarget", this.currentTarget.toString());
        }
        if (this.mother != null) {
            nbt.putString("Mother", this.mother.toString());
        }
        nbt.put("Cooldowns", this.targetCooldown.serializeNBT());
    }

    public void readAdditionalSaveData(final CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("CurrentTarget", 8)) {
            this.currentTarget = UUID.fromString(nbt.getString("CurrentTarget"));
        }
        if (nbt.contains("Mother", 8)) {
            this.mother = UUID.fromString(nbt.getString("Mother"));
        }
        this.targetCooldown.deserializeNBT(nbt.getList("Cooldowns", 10));
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (this.currentTarget == null || this.currentTarget.equals(this.mother)) {
                final VaultRaid vault = VaultRaidData.get((ServerWorld) this.level).getAt((ServerWorld) this.level, this.blockPosition());
                List<UUID> players = new ArrayList<UUID>();
                if (vault != null) {
                    players = vault.getPlayers().stream().filter(player -> player instanceof VaultRunner).map(VaultPlayer::getPlayerId).collect(Collectors.toList());
                    players.removeAll(this.targetCooldown.entrySet().stream().filter(e -> e.getValue().cooldown > 0).map(Map.Entry::getKey).collect(Collectors.toList()));
                }
                if (players.isEmpty()) {
                    this.currentTarget = this.mother;
                } else {
                    this.currentTarget = players.get(this.random.nextInt(players.size()));
                }
            }
            if (this.currentTarget != null) {
                final Entity entity = ((ServerWorld) this.level).getEntity(this.currentTarget);
                if (entity != null) {
                    if (entity instanceof LivingEntity) {
                        final LivingEntity living = (LivingEntity) entity;
                        this.setTarget(living.getUUID().equals(this.mother) ? null : living);
                        final Vector3d vector3d = living.getEyePosition(1.0f);
                        this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0);
                    }
                    if (this.getBoundingBox().intersects(entity.getBoundingBox()) && !entity.getUUID().equals(this.mother)) {
                        this.doHurtTarget(entity);
                    }
                    if (entity.isSpectator()) {
                        this.currentTarget = null;
                    }
                } else {
                    this.currentTarget = null;
                }
            }
            this.targetCooldown.values().forEach(targetData -> --targetData.cooldown);
        }
    }

    public boolean doHurtTarget(final Entity entity) {
        float f = ModConfigs.EYESORE.eyeStalk.getDamage(this);
        float f2 = ModConfigs.EYESORE.eyeStalk.knockback;
        if (entity instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) entity).getMobType());
            f2 += EnchantmentHelper.getKnockbackBonus(this);
        }
        final int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            entity.setSecondsOnFire(i * 4);
        }
        final boolean flag = entity.hurt(DamageSource.mobAttack(this), f);
        if (flag) {
            if (f2 > 0.0f && entity instanceof LivingEntity) {
                this.applyKnockback(entity, f2 * 0.5f, MathHelper.sin(this.yRot * 0.017453292f), -MathHelper.cos(this.yRot * 0.017453292f));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }
            this.doEnchantDamageEffects(this, entity);
            this.setLastHurtMob(entity);
        }
        return flag;
    }

    public void applyKnockback(final Entity target, final float strength, final double ratioX, final double ratioZ) {
        if (strength > 0.0f) {
            target.hasImpulse = true;
            final Vector3d vector3d = target.getDeltaMovement();
            final Vector3d vector3d2 = new Vector3d(ratioX, 0.0, ratioZ).normalize().scale(strength);
            target.setDeltaMovement(vector3d.x / 2.0 - vector3d2.x, this.onGround ? Math.min(0.4, vector3d.y / 2.0 + strength) : vector3d.y, vector3d.z / 2.0 - vector3d2.z);
        }
    }

    protected void actuallyHurt(final DamageSource source, final float damageAmount) {
        final Entity direct = source.getDirectEntity();
        final Entity indirect = source.getEntity();
        if ((direct != null && direct.getUUID().equals(this.currentTarget)) || (indirect != null && indirect.getUUID().equals(this.currentTarget))) {
            this.targetCooldown.computeIfAbsent(this.currentTarget, id -> new TargetData()).cooldown = 600;
            this.currentTarget = null;
        }
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 100.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ATTACK_KNOCKBACK, 3.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.4).add(Attributes.ARMOR, 2.0);
    }

    public static class TargetData implements INBTSerializable<CompoundNBT> {
        public int cooldown;

        public CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("Cooldown", this.cooldown);
            return nbt;
        }

        public void deserializeNBT(final CompoundNBT nbt) {
            this.cooldown = nbt.getInt("Cooldown");
        }
    }
}
