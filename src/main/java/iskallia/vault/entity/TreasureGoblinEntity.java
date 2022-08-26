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
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class TreasureGoblinEntity extends MonsterEntity {
    protected int disappearTick;
    protected boolean shouldDisappear;
    protected PlayerEntity lastAttackedPlayer;

    public TreasureGoblinEntity(final EntityType<? extends MonsterEntity> type, final World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(3, new AvoidEntityGoal(this, PlayerEntity.class, 6.0f, 1.7000000476837158, 2.0));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 20.0f));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
    }

    public boolean isHitByPlayer() {
        return this.lastAttackedPlayer != null;
    }

    protected int calcDisappearTicks(final PlayerEntity player) {
        return 200;
    }

    public boolean hurt(final DamageSource source, final float amount) {
        final Entity entity = source.getEntity();
        if (entity instanceof PlayerEntity && !entity.level.isClientSide) {
            final PlayerEntity player = (PlayerEntity) entity;
            if (!this.isHitByPlayer()) {
                this.lastAttackedPlayer = player;
                this.disappearTick = this.calcDisappearTicks(player);
                this.addEffect(new EffectInstance(Effects.GLOWING, this.disappearTick));
            }
        }
        return super.hurt(source, amount);
    }

    protected void dropFromLootTable(final DamageSource source, final boolean attackedRecently) {
        final ServerWorld world = (ServerWorld) this.level;
        final VaultRaid vault = VaultRaidData.get(world).getAt(world, this.blockPosition());
        if (vault != null) {
            vault.getProperties().getBase(VaultRaid.HOST).flatMap(vault::getPlayer).ifPresent(player -> {
                final int level = player.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
                final ResourceLocation id = ModConfigs.LOOT_TABLES.getForLevel(level).getTreasureGoblin();
                final LootTable loot = this.level.getServer().getLootTables().get(id);
                final LootContext.Builder builder = this.createLootContext(attackedRecently, source);
                final LootContext ctx = builder.create(LootParameterSets.ENTITY);
                loot.getRandomItems(ctx).forEach(this::spawnAtLocation);
                return;
            });
        }
        super.dropFromLootTable(source, attackedRecently);
    }

    public void tick() {
        super.tick();
        if (this.isAlive() && this.isHitByPlayer()) {
            if (this.disappearTick <= 0) {
                this.shouldDisappear = true;
            }
            --this.disappearTick;
        }
    }

    public void disappear(final ServerWorld world) {
        world.despawn(this);
        if (this.lastAttackedPlayer != null) {
            final StringTextComponent bailText = (StringTextComponent) new StringTextComponent("Treasure Goblin escaped from you.").withStyle(Style.EMPTY.withColor(Color.fromRgb(8042883)));
            this.lastAttackedPlayer.displayClientMessage(bailText, true);
            this.lastAttackedPlayer.playNotifySound(ModSounds.GOBLIN_BAIL, SoundCategory.MASTER, 0.7f, 1.0f);
            world.playSound(this.lastAttackedPlayer, this.getX(), this.getY(), this.getZ(), ModSounds.GOBLIN_BAIL, SoundCategory.MASTER, 0.7f, 1.0f);
        } else {
            world.playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.GOBLIN_BAIL, SoundCategory.MASTER, 0.7f, 1.0f);
        }
    }

    @SubscribeEvent
    public static void onWorldTick(final TickEvent.WorldTickEvent event) {
        if (event.world.isClientSide() || event.phase == TickEvent.Phase.START) {
            return;
        }
        final ServerWorld world = (ServerWorld) event.world;
        final List<TreasureGoblinEntity> goblins = world.getEntities().filter(entity -> entity instanceof TreasureGoblinEntity).map(entity -> (TreasureGoblinEntity) entity).collect(Collectors.toList());
        goblins.stream().filter(goblin -> goblin.shouldDisappear).forEach(goblin -> goblin.disappear(world));
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return ModSounds.GOBLIN_IDLE;
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.GOBLIN_DEATH;
    }

    protected SoundEvent getHurtSound(@Nonnull final DamageSource damageSourceIn) {
        return ModSounds.GOBLIN_HURT;
    }
}
