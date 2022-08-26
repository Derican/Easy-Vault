package iskallia.vault.entity;

import iskallia.vault.entity.ai.AOEGoal;
import iskallia.vault.entity.ai.TeleportGoal;
import iskallia.vault.entity.ai.TeleportRandomly;
import iskallia.vault.entity.ai.ThrowProjectilesGoal;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.effect.sub.RampageDotAbility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArenaBossEntity extends FighterEntity {
    public TeleportRandomly<ArenaBossEntity> teleportTask;

    public ArenaBossEntity(final EntityType<? extends ZombieEntity> type, final World world) {
        super(type, world);
        this.teleportTask = new TeleportRandomly<ArenaBossEntity>(this, new TeleportRandomly.Condition[]{(entity, source, amount) -> {
            if (!(source.getEntity() instanceof LivingEntity)) {
                return 0.2;
            } else {
                return 0.0;
            }
        }});
        if (!this.level.isClientSide) {
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1000000.0);
        }
        this.bossInfo.setVisible(true);
    }

    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        this.goalSelector.addGoal(1, TeleportGoal.builder(this).start(entity -> entity.getTarget() != null && entity.tickCount % 60 == 0).to(entity -> entity.getTarget().position().add((entity.random.nextDouble() - 0.5) * 8.0, entity.random.nextInt(16) - 8, (entity.random.nextDouble() - 0.5) * 8.0)).then(entity -> entity.playSound(ModSounds.BOSS_TP_SFX, 1.0f, 1.0f)).build());
        this.goalSelector.addGoal(1, new ThrowProjectilesGoal(this, 96, 10, ArenaBossEntity.SNOWBALLS));
        this.goalSelector.addGoal(1, new AOEGoal(this, e -> !(e instanceof ArenaBossEntity)));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100.0);
    }

    private float knockbackAttack(final Entity entity) {
        for (int i = 0; i < 20; ++i) {
            final double d0 = this.level.random.nextGaussian() * 0.02;
            final double d2 = this.level.random.nextGaussian() * 0.02;
            final double d3 = this.level.random.nextGaussian() * 0.02;
            ((ServerWorld) this.level).sendParticles((IParticleData) ParticleTypes.POOF, entity.getX() + this.level.random.nextDouble() - d0, entity.getY() + this.level.random.nextDouble() - d2, entity.getZ() + this.level.random.nextDouble() - d3, 10, d0, d2, d3, 1.0);
        }
        this.level.playSound(null, entity.blockPosition(), SoundEvents.IRON_GOLEM_HURT, this.getSoundSource(), 1.0f, 1.0f);
        return 15.0f;
    }

    @Override
    public boolean doHurtTarget(final Entity entity) {
        boolean ret = false;
        if (this.random.nextInt(12) == 0) {
            final double old = this.getAttribute(Attributes.ATTACK_KNOCKBACK).getBaseValue();
            this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(this.knockbackAttack(entity));
            final boolean result = super.doHurtTarget(entity);
            this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(old);
            ret |= result;
        }
        if (this.random.nextInt(6) == 0) {
            this.level.broadcastEntityEvent(this, (byte) 4);
            final float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            final float f2 = ((int) f > 0) ? (f / 2.0f + this.random.nextInt((int) f)) : f;
            final boolean flag = entity.hurt(DamageSource.mobAttack(this), f2);
            if (flag) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.6000000238418579, 0.0));
                this.doEnchantDamageEffects(this, entity);
            }
            this.level.playSound(null, entity.blockPosition(), SoundEvents.IRON_GOLEM_HURT, this.getSoundSource(), 1.0f, 1.0f);
            ret |= flag;
        }
        return ret || super.doHurtTarget(entity);
    }

    public boolean hurt(final DamageSource source, final float amount) {
        return (source instanceof RampageDotAbility.PlayerDamageOverTimeSource || source.getEntity() instanceof PlayerEntity || source.getEntity() instanceof EternalEntity || source == DamageSource.OUT_OF_WORLD) && !this.isInvulnerableTo(source) && source != DamageSource.FALL && (this.teleportTask.attackEntityFrom(source, amount) || super.hurt(source, amount));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 35.0).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ARMOR, 2.0).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }
}
