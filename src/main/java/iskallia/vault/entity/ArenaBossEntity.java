package iskallia.vault.entity;

import iskallia.vault.entity.ai.AOEGoal;
import iskallia.vault.entity.ai.TeleportGoal;
import iskallia.vault.entity.ai.TeleportRandomly;
import iskallia.vault.entity.ai.ThrowProjectilesGoal;
import iskallia.vault.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;


@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArenaBossEntity
        extends FighterEntity {
    public TeleportRandomly<ArenaBossEntity> teleportTask;

    public ArenaBossEntity(EntityType<? extends ZombieEntity> type, World world) {
        super(type, world);

        this.teleportTask = new TeleportRandomly((LivingEntity) this, new TeleportRandomly.Condition[]{(entity, source, amount) -> !(source.getEntity() instanceof LivingEntity) ? 0.2D : 0.0D});
        if (!this.level.isClientSide) {
            getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1000000.0D);
        }

        this.bossInfo.setVisible(true);
    }


    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        this.goalSelector.addGoal(1, (Goal) TeleportGoal.builder( this).start(entity ->
                        (entity.getTarget() != null && entity.tickCount % 60 == 0))
                .to(entity -> entity.getTarget().position().add((entity.random.nextDouble() - 0.5D) * 8.0D, (entity.random.nextInt(16) - 8), (entity.random.nextDouble() - 0.5D) * 8.0D))

                .then(entity -> entity.playSound(ModSounds.BOSS_TP_SFX, 1.0F, 1.0F))

                .build());

        this.goalSelector.addGoal(1, (Goal) new ThrowProjectilesGoal((MobEntity) this, 96, 10, SNOWBALLS));
        this.goalSelector.addGoal(1, (Goal) new AOEGoal((MobEntity) this, e -> !(e instanceof ArenaBossEntity)));

        getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100.0D);
    }

    private float knockbackAttack(Entity entity) {
        for (int i = 0; i < 20; i++) {
            double d0 = this.level.random.nextGaussian() * 0.02D;
            double d1 = this.level.random.nextGaussian() * 0.02D;
            double d2 = this.level.random.nextGaussian() * 0.02D;

            ((ServerWorld) this.level).sendParticles((IParticleData) ParticleTypes.POOF, entity
                    .getX() + this.level.random.nextDouble() - d0, entity
                    .getY() + this.level.random.nextDouble() - d1, entity
                    .getZ() + this.level.random.nextDouble() - d2, 10, d0, d1, d2, 1.0D);
        }

        this.level.playSound(null, entity.blockPosition(), SoundEvents.IRON_GOLEM_HURT, getSoundSource(), 1.0F, 1.0F);
        return 15.0F;
    }

    public boolean doHurtTarget(Entity entity) {
        boolean ret = false;

        if (this.random.nextInt(12) == 0) {
            double old = getAttribute(Attributes.ATTACK_KNOCKBACK).getBaseValue();
            getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(knockbackAttack(entity));
            boolean result = super.doHurtTarget(entity);
            getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(old);
            ret |= result;
        }

        if (this.random.nextInt(6) == 0) {
            this.level.broadcastEntityEvent((Entity) this, (byte) 4);
            float f = (float) getAttributeValue(Attributes.ATTACK_DAMAGE);
            float f1 = ((int) f > 0) ? (f / 2.0F + this.random.nextInt((int) f)) : f;
            boolean flag = entity.hurt(DamageSource.mobAttack((LivingEntity) this), f1);

            if (flag) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, 0.6000000238418579D, 0.0D));
                doEnchantDamageEffects((LivingEntity) this, entity);
            }

            this.level.playSound(null, entity.blockPosition(), SoundEvents.IRON_GOLEM_HURT, getSoundSource(), 1.0F, 1.0F);
            ret |= flag;
        }

        return (ret || super.doHurtTarget(entity));
    }


    public boolean hurt(DamageSource source, float amount) {
        if (!(source instanceof iskallia.vault.skill.ability.effect.sub.RampageDotAbility.PlayerDamageOverTimeSource) &&
                !(source.getEntity() instanceof net.minecraft.entity.player.PlayerEntity) &&
                !(source.getEntity() instanceof EternalEntity) && source != DamageSource.OUT_OF_WORLD) {
            return false;
        }

        if (isInvulnerableTo(source) || source == DamageSource.FALL)
            return false;
        if (this.teleportTask.attackEntityFrom(source, amount)) {
            return true;
        }

        return super.hurt(source, amount);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\ArenaBossEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */