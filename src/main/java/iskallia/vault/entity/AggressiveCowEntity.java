package iskallia.vault.entity;

import iskallia.vault.entity.ai.CowDashAttackGoal;
import iskallia.vault.entity.ai.MobAttackGoal;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
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

public class AggressiveCowEntity extends CowEntity {
    protected int dashCooldown = 0;

    public AggressiveCowEntity(EntityType<? extends CowEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 3.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4D)
                .add(Attributes.ARMOR, 2.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(8, (Goal) new WaterAvoidingRandomWalkingGoal((CreatureEntity) this, 1.5D));
        this.goalSelector.addGoal(8, (Goal) new LookAtGoal((MobEntity) this, PlayerEntity.class, 16.0F));

        this.goalSelector.addGoal(0, (Goal) new CowDashAttackGoal(this, 0.3F));
        this.goalSelector.addGoal(1, (Goal) new MobAttackGoal((CreatureEntity) this, 1.5D, true));

        this.targetSelector.addGoal(0, (Goal) new NearestAttackableTargetGoal((MobEntity) this, PlayerEntity.class, 0, true, false, null));
    }


    protected void dropFromLootTable(DamageSource source, boolean attackedRecently) {
        ServerWorld world = (ServerWorld) this.level;
        VaultRaid vault = VaultRaidData.get(world).getAt(world, blockPosition());

        if (vault != null) {
            vault.getProperties().getBase(VaultRaid.HOST).flatMap(vault::getPlayer).ifPresent(player -> {
                int level = ((Integer) player.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();

                ResourceLocation id = ModConfigs.LOOT_TABLES.getForLevel(level).getCow();
                LootTable loot = this.level.getServer().getLootTables().get(id);
                LootContext.Builder builder = createLootContext(attackedRecently, source);
                LootContext ctx = builder.create(LootParameterSets.ENTITY);
                loot.getRandomItems(ctx).forEach(this::spawnAtLocation);
            });
        }
        super.dropFromLootTable(source, attackedRecently);
    }


    public void aiStep() {
        super.aiStep();
        setAge(0);

        if (this.dashCooldown > 0) {
            this.dashCooldown--;
        }
    }


    public boolean isInvulnerableTo(DamageSource source) {
        return (super.isInvulnerableTo(source) || source == DamageSource.FALL || source == DamageSource.DROWN);
    }

    public boolean canDash() {
        return (this.dashCooldown <= 0);
    }

    public void onDash() {
        this.dashCooldown = 60;
        this.navigation.stop();
        playAmbientSound();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\AggressiveCowEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */