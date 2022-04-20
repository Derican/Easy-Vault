package iskallia.vault.entity;

import iskallia.vault.entity.ai.CowDashAttackGoal;
import iskallia.vault.entity.ai.MobAttackGoal;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Predicate;

public class AggressiveCowEntity extends CowEntity {
    protected int dashCooldown;

    public AggressiveCowEntity(final EntityType<? extends CowEntity> type, final World worldIn) {
        super((EntityType) type, worldIn);
        this.dashCooldown = 0;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 100.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ATTACK_KNOCKBACK, 3.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.4).add(Attributes.ARMOR, 2.0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(8, (Goal) new WaterAvoidingRandomWalkingGoal((CreatureEntity) this, 1.5));
        this.goalSelector.addGoal(8, (Goal) new LookAtGoal((MobEntity) this, (Class) PlayerEntity.class, 16.0f));
        this.goalSelector.addGoal(0, (Goal) new CowDashAttackGoal(this, 0.3f));
        this.goalSelector.addGoal(1, (Goal) new MobAttackGoal((CreatureEntity) this, 1.5, true));
        this.targetSelector.addGoal(0, (Goal) new NearestAttackableTargetGoal((MobEntity) this, (Class) PlayerEntity.class, 0, true, false, (Predicate) null));
    }

    protected void dropFromLootTable(final DamageSource source, final boolean attackedRecently) {
        final ServerWorld world = (ServerWorld) this.level;
        final VaultRaid vault = VaultRaidData.get(world).getAt(world, this.blockPosition());
        if (vault != null) {
            vault.getProperties().getBase(VaultRaid.HOST).flatMap(vault::getPlayer).ifPresent(player -> {
                final int level = player.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
                final ResourceLocation id = ModConfigs.LOOT_TABLES.getForLevel(level).getCow();
                final LootTable loot = this.level.getServer().getLootTables().get(id);
                final LootContext.Builder builder = this.createLootContext(attackedRecently, source);
                final LootContext ctx = builder.create(LootParameterSets.ENTITY);
                loot.getRandomItems(ctx).forEach(this::spawnAtLocation);
                return;
            });
        }
        super.dropFromLootTable(source, attackedRecently);
    }

    public void aiStep() {
        super.aiStep();
        this.setAge(0);
        if (this.dashCooldown > 0) {
            --this.dashCooldown;
        }
    }

    public boolean isInvulnerableTo(final DamageSource source) {
        return super.isInvulnerableTo(source) || source == DamageSource.FALL || source == DamageSource.DROWN;
    }

    public boolean canDash() {
        return this.dashCooldown <= 0;
    }

    public void onDash() {
        this.dashCooldown = 60;
        this.navigation.stop();
        this.playAmbientSound();
    }
}
