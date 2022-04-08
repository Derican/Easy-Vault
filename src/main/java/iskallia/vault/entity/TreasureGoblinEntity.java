package iskallia.vault.entity;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModSounds;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@EventBusSubscriber
public class TreasureGoblinEntity
        extends MonsterEntity {
    protected int disappearTick;

    public TreasureGoblinEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected boolean shouldDisappear;
    protected PlayerEntity lastAttackedPlayer;

    protected void registerGoals() {
        this.goalSelector.addGoal(3, (Goal) new AvoidEntityGoal((CreatureEntity) this, PlayerEntity.class, 6.0F, 1.7000000476837158D, 2.0D));
        this.goalSelector.addGoal(5, (Goal) new LookAtGoal((MobEntity) this, PlayerEntity.class, 20.0F));
        this.goalSelector.addGoal(5, (Goal) new LookRandomlyGoal((MobEntity) this));
    }

    public boolean isHitByPlayer() {
        return (this.lastAttackedPlayer != null);
    }

    protected int calcDisappearTicks(PlayerEntity player) {
        return 200;
    }


    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getEntity();

        if (entity instanceof PlayerEntity && !entity.level.isClientSide) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!isHitByPlayer()) {
                this.lastAttackedPlayer = player;
                this.disappearTick = calcDisappearTicks(player);
                addEffect(new EffectInstance(Effects.GLOWING, this.disappearTick));
            }
        }

        return super.hurt(source, amount);
    }


    protected void dropFromLootTable(DamageSource source, boolean attackedRecently) {
        ServerWorld world = (ServerWorld) this.level;
        VaultRaid vault = VaultRaidData.get(world).getAt(world, blockPosition());

        if (vault != null) {
            vault.getProperties().getBase(VaultRaid.HOST).flatMap(vault::getPlayer).ifPresent(player -> {
                int level = ((Integer) player.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();

                ResourceLocation id = ModConfigs.LOOT_TABLES.getForLevel(level).getTreasureGoblin();
                LootTable loot = this.level.getServer().getLootTables().get(id);
                LootContext.Builder builder = createLootContext(attackedRecently, source);
                LootContext ctx = builder.create(LootParameterSets.ENTITY);
                loot.getRandomItems(ctx).forEach(this::spawnAtLocation);
            });
        }
        super.dropFromLootTable(source, attackedRecently);
    }


    public void tick() {
        super.tick();

        if (isAlive() &&
                isHitByPlayer()) {
            if (this.disappearTick <= 0) {
                this.shouldDisappear = true;
            }
            this.disappearTick--;
        }
    }


    public void disappear(ServerWorld world) {
        world.despawn((Entity) this);


        if (this.lastAttackedPlayer != null) {

            StringTextComponent bailText = (StringTextComponent) (new StringTextComponent("Treasure Goblin escaped from you.")).withStyle(Style.EMPTY.withColor(Color.fromRgb(8042883)));
            this.lastAttackedPlayer.displayClientMessage((ITextComponent) bailText, true);
            this.lastAttackedPlayer.playNotifySound(ModSounds.GOBLIN_BAIL, SoundCategory.MASTER, 0.7F, 1.0F);
            world.playSound(this.lastAttackedPlayer, getX(), getY(), getZ(), ModSounds.GOBLIN_BAIL, SoundCategory.MASTER, 0.7F, 1.0F);
        } else {

            world.playSound(null, getX(), getY(), getZ(), ModSounds.GOBLIN_BAIL, SoundCategory.MASTER, 0.7F, 1.0F);
        }
    }


    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world.isClientSide() || event.phase == TickEvent.Phase.START)
            return;
        ServerWorld world = (ServerWorld) event.world;


        List<TreasureGoblinEntity> goblins = (List<TreasureGoblinEntity>) world.getEntities().filter(entity -> entity instanceof TreasureGoblinEntity).map(entity -> (TreasureGoblinEntity) entity).collect(Collectors.toList());

        goblins.stream()
                .filter(goblin -> goblin.shouldDisappear)
                .forEach(goblin -> goblin.disappear(world));
    }


    @Nullable
    protected SoundEvent getAmbientSound() {
        return ModSounds.GOBLIN_IDLE;
    }


    protected SoundEvent getDeathSound() {
        return ModSounds.GOBLIN_DEATH;
    }


    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSourceIn) {
        return ModSounds.GOBLIN_HURT;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\TreasureGoblinEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */