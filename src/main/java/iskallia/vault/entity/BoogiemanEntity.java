package iskallia.vault.entity;

import iskallia.vault.entity.ai.*;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.effect.sub.RampageDotAbility;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.raid.RaidChallengeObjective;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

public class BoogiemanEntity extends ZombieEntity implements VaultBoss {
    public TeleportRandomly<BoogiemanEntity> teleportTask;
    public final ServerBossInfo bossInfo;
    public RegenAfterAWhile<BoogiemanEntity> regenAfterAWhile;

    public BoogiemanEntity(final EntityType<? extends ZombieEntity> type, final World worldIn) {
        super((EntityType) type, worldIn);
        this.teleportTask = new TeleportRandomly<BoogiemanEntity>(this, (TeleportRandomly.Condition<BoogiemanEntity>[]) new TeleportRandomly.Condition[]{(entity, source, amount) -> {
            if (!(source.getEntity() instanceof LivingEntity)) {
                return 0.2;
            } else {
                return 0.0;
            }
        }});
        this.bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS);
        this.regenAfterAWhile = new RegenAfterAWhile<BoogiemanEntity>(this);
    }

    protected void dropFromLootTable(final DamageSource damageSource, final boolean attackedRecently) {
    }

    protected void doUnderWaterConversion() {
    }

    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        this.goalSelector.addGoal(1, (Goal) TeleportGoal.builder(this).start(entity -> entity.getTarget() != null && entity.tickCount % 60 == 0).to(entity -> entity.getTarget().position().add((entity.random.nextDouble() - 0.5) * 8.0, (double) (entity.random.nextInt(16) - 8), (entity.random.nextDouble() - 0.5) * 8.0)).then(entity -> entity.playSound(ModSounds.BOSS_TP_SFX, 1.0f, 1.0f)).build());
        this.goalSelector.addGoal(1, (Goal) new ThrowProjectilesGoal<>(this, 96, 10, FighterEntity.SNOWBALLS));
        this.goalSelector.addGoal(1, (Goal) new AOEGoal<>(this, e -> !(e instanceof VaultBoss)));
        this.targetSelector.addGoal(1, (Goal) new NearestAttackableTargetGoal((MobEntity) this, (Class) PlayerEntity.class, false));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100.0);
    }

    protected boolean isSunSensitive() {
        return false;
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

    public ServerBossInfo getServerBossInfo() {
        return this.bossInfo;
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
            this.regenAfterAWhile.tick();
            final VaultRaid vault = VaultRaidData.get((ServerWorld)this.level).getAt((ServerWorld)this.level, this.blockPosition());
            this.bossInfo.setVisible(vault == null || !vault.getActiveObjective(RaidChallengeObjective.class).isPresent());
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

    public SoundCategory getSoundSource() {
        return SoundCategory.HOSTILE;
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.BOOGIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return ModSounds.BOOGIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.BOOGIE_DEATH;
    }
}
