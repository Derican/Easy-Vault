// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity;

import iskallia.vault.entity.ai.*;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.effect.sub.RampageDotAbility;
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

public class MonsterEyeEntity extends SlimeEntity implements VaultBoss
{
    public TeleportRandomly<MonsterEyeEntity> teleportTask;
    public boolean shouldBlockSlimeSplit;
    public final ServerBossInfo bossInfo;
    public RegenAfterAWhile<MonsterEyeEntity> regenAfterAWhile;
    
    public MonsterEyeEntity(final EntityType<? extends SlimeEntity> type, final World worldIn) {
        super((EntityType)type, worldIn);
        this.teleportTask = new TeleportRandomly<MonsterEyeEntity>(this, (TeleportRandomly.Condition<MonsterEyeEntity>[])new TeleportRandomly.Condition[] { (entity, source, amount) -> {
                if (!(source.getEntity() instanceof LivingEntity)) {
                    return 0.2;
                }
                else {
                    return 0.0;
                }
            } });
        this.setSize(3, false);
        this.bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS);
        this.regenAfterAWhile = new RegenAfterAWhile<MonsterEyeEntity>(this);
    }
    
    protected void dropFromLootTable(final DamageSource damageSource, final boolean attackedRecently) {
    }
    
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, (Goal)TeleportGoal.builder(this).start(entity -> entity.getTarget() != null && entity.tickCount % 60 == 0).to(entity -> entity.getTarget().position().add((entity.random.nextDouble() - 0.5) * 8.0, (double)(entity.random.nextInt(16) - 8), (entity.random.nextDouble() - 0.5) * 8.0)).then(entity -> entity.playSound(ModSounds.BOSS_TP_SFX, 1.0f, 1.0f)).build());
        this.goalSelector.addGoal(1, (Goal)new ThrowProjectilesGoal<>(this, 96, 10, FighterEntity.SNOWBALLS));
        this.goalSelector.addGoal(1, (Goal)new AOEGoal<>(this, e -> !(e instanceof VaultBoss)));
        this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((MobEntity)this, (Class)PlayerEntity.class, false));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100.0);
    }
    
    public boolean hurt(final DamageSource source, final float amount) {
        if (!(source instanceof RampageDotAbility.PlayerDamageOverTimeSource) && !(source.getEntity() instanceof PlayerEntity) && !(source.getEntity() instanceof EternalEntity) && source != DamageSource.OUT_OF_WORLD) {
            return false;
        }
        if (this.isInvulnerableTo(source) || source == DamageSource.FALL) {
            return false;
        }
        if (this.teleportTask.attackEntityFrom(source, amount)) {
            return true;
        }
        this.regenAfterAWhile.onDamageTaken();
        return super.hurt(source, amount);
    }
    
    protected void dealDamage(final LivingEntity entityIn) {
        if (this.isAlive()) {
            final int i = this.getSize();
            if (this.distanceToSqr((Entity)entityIn) < 0.8 * i * 0.8 * i && this.canSee((Entity)entityIn) && entityIn.hurt(DamageSource.mobAttack((LivingEntity)this), this.getAttackDamage())) {
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
                this.doEnchantDamageEffects((LivingEntity)this, (Entity)entityIn);
            }
        }
    }
    
    public void remove(final boolean keepData) {
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
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
            this.regenAfterAWhile.tick();
        }
    }
    
    public void startSeenByPlayer(final ServerPlayerEntity player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }
    
    public void stopSeenByPlayer(final ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }
}
