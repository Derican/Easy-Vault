package iskallia.vault.entity;

import iskallia.vault.entity.ai.RegenAfterAWhile;
import iskallia.vault.entity.ai.TeleportGoal;
import iskallia.vault.entity.ai.TeleportRandomly;
import iskallia.vault.entity.ai.ThrowProjectilesGoal;
import iskallia.vault.init.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;


public class BlueBlazeEntity
        extends BlazeEntity
        implements VaultBoss {
    public TeleportRandomly<BlueBlazeEntity> teleportTask;
    public final ServerBossInfo bossInfo;
    public RegenAfterAWhile<BlueBlazeEntity> regenAfterAWhile;

    public BlueBlazeEntity(EntityType<? extends BlazeEntity> type, World world) {
        super(type, world);
        this.teleportTask = new TeleportRandomly((LivingEntity) this, new TeleportRandomly.Condition[]{(entity, source, amount) -> !(source.getEntity() instanceof LivingEntity) ? 0.2D : 0.0D});
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\BlueBlazeEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */