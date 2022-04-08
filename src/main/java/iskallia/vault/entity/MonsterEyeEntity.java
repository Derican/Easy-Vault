package iskallia.vault.entity;

import iskallia.vault.entity.ai.*;
import iskallia.vault.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;

public class MonsterEyeEntity
        extends SlimeEntity implements VaultBoss {
    public TeleportRandomly<MonsterEyeEntity> teleportTask;
    public boolean shouldBlockSlimeSplit;
    public final ServerBossInfo bossInfo;
    public RegenAfterAWhile<MonsterEyeEntity> regenAfterAWhile;

    public MonsterEyeEntity(EntityType<? extends SlimeEntity> type, World worldIn) {
        super(type, worldIn);
        this.teleportTask = new TeleportRandomly((LivingEntity) this, new TeleportRandomly.Condition[]{(entity, source, amount) -> !(source.getEntity() instanceof LivingEntity) ? 0.2D : 0.0D});
        setSize(3, false);
        this.bossInfo = new ServerBossInfo(getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS);
        this.regenAfterAWhile = new RegenAfterAWhile((LivingEntity) this);
    }


    protected void dropFromLootTable(DamageSource damageSource, boolean attackedRecently) {
    }


    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, (Goal) TeleportGoal.builder( this).start(entity ->
                        (entity.getTarget() != null && entity.tickCount % 60 == 0))
                .to(entity -> entity.getTarget().position().add((entity.random.nextDouble() - 0.5D) * 8.0D, (entity.random.nextInt(16) - 8), (entity.random.nextDouble() - 0.5D) * 8.0D))

                .then(entity -> entity.playSound(ModSounds.BOSS_TP_SFX, 1.0F, 1.0F))

                .build());

        this.goalSelector.addGoal(1, (Goal) new ThrowProjectilesGoal((MobEntity) this, 96, 10, FighterEntity.SNOWBALLS));
        this.goalSelector.addGoal(1, (Goal) new AOEGoal((MobEntity) this, e -> !(e instanceof VaultBoss)));

        this.targetSelector.addGoal(1, (Goal) new NearestAttackableTargetGoal((MobEntity) this, PlayerEntity.class, false));
        getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100.0D);
    }


    public boolean hurt(DamageSource source, float amount) {
        if (!(source instanceof iskallia.vault.skill.ability.effect.sub.RampageDotAbility.PlayerDamageOverTimeSource) &&
                !(source.getEntity() instanceof PlayerEntity) &&
                !(source.getEntity() instanceof EternalEntity) && source != DamageSource.OUT_OF_WORLD) {
            return false;
        }

        if (isInvulnerableTo(source) || source == DamageSource.FALL)
            return false;
        if (this.teleportTask.attackEntityFrom(source, amount)) {
            return true;
        }

        this.regenAfterAWhile.onDamageTaken();
        return super.hurt(source, amount);
    }


    protected void dealDamage(LivingEntity entityIn) {
        if (isAlive()) {
            int i = getSize();
            if (distanceToSqr((Entity) entityIn) < 0.8D * i * 0.8D * i && canSee((Entity) entityIn) && entityIn.hurt(DamageSource.mobAttack((LivingEntity) this), getAttackDamage())) {
                playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                doEnchantDamageEffects((LivingEntity) this, (Entity) entityIn);
            }
        }
    }


    public void remove(boolean keepData) {
        this.shouldBlockSlimeSplit = true;
        super.remove(keepData);
    }


    public int getSize() {
        return this.shouldBlockSlimeSplit ? 0 : super.getSize();
    }


    public ServerBossInfo getServerBossInfo() {
        return this.bossInfo;
    }


    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {
            this.bossInfo.setPercent(getHealth() / getMaxHealth());
            this.regenAfterAWhile.tick();
        }
    }


    public void startSeenByPlayer(ServerPlayerEntity player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }


    public void stopSeenByPlayer(ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\MonsterEyeEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */