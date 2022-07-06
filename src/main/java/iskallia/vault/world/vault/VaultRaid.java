package iskallia.vault.world.vault;

import java.util.Arrays;
import java.util.HashMap;

import iskallia.vault.entity.AggressiveCowEntity;

import java.util.function.BiConsumer;

import iskallia.vault.entity.EntityScaler;

import java.util.function.Predicate;

import iskallia.vault.world.vault.logic.condition.IVaultCondition;
import iskallia.vault.world.vault.influence.VaultInfluenceRegistry;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutRegistry;
import iskallia.vault.world.vault.gen.piece.FinalVaultBoss;
import iskallia.vault.world.vault.gen.piece.VaultGodEye;
import iskallia.vault.world.vault.gen.piece.VaultPortal;
import iskallia.vault.world.vault.gen.piece.FinalVaultLobby;
import iskallia.vault.world.vault.gen.piece.VaultRaidRoom;
import iskallia.vault.world.vault.gen.piece.VaultTunnel;
import iskallia.vault.world.vault.gen.piece.VaultTreasure;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.vault.gen.piece.VaultObelisk;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.time.extension.SandExtension;
import iskallia.vault.world.vault.time.extension.FavourExtension;
import iskallia.vault.world.vault.time.extension.RoomGenerationExtension;
import iskallia.vault.world.vault.time.extension.AccelerationExtension;
import iskallia.vault.world.vault.time.extension.TimeAltarExtension;
import iskallia.vault.world.vault.time.extension.ModifierExtension;
import iskallia.vault.world.vault.time.extension.FallbackExtension;
import iskallia.vault.world.vault.time.extension.FruitExtension;
import org.apache.commons.lang3.mutable.MutableBoolean;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.AxisAlignedBB;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.vault.gen.piece.VaultStart;
import iskallia.vault.world.vault.logic.VaultInfluenceHandler;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.vault.time.extension.RelicSetExtension;
import iskallia.vault.util.RelicSet;
import net.minecraft.util.ResourceLocation;
import iskallia.vault.world.data.VaultSetsData;

import java.util.HashSet;
import java.util.Random;

import iskallia.vault.network.message.VaultOverlayMessage;
import iskallia.vault.item.gear.VaultGear;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.util.NonNullList;
import iskallia.vault.block.VaultCrateBlock;
import net.minecraft.item.BlockItem;
import net.minecraftforge.items.CapabilityItemHandler;
import iskallia.vault.item.BasicScavengerItem;
import iskallia.vault.world.data.SoulboundSnapshotData;
import iskallia.vault.world.data.PhoenixSetSnapshotData;
import iskallia.vault.world.data.PhoenixModifierSnapshotData;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.util.DamageSource;
import net.minecraft.inventory.IInventory;
import iskallia.vault.init.ModSounds;
import iskallia.vault.world.vault.player.VaultSpectator;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.IItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import iskallia.vault.world.vault.time.extension.TimeExtension;
import iskallia.vault.world.vault.time.extension.WinExtension;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.event.HoverEvent;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.util.text.TextFormatting;

import java.util.function.Function;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.StringTextComponent;
import iskallia.vault.Vault;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.entity.Entity;
import iskallia.vault.util.ServerScheduler;
import iskallia.vault.world.vault.logic.VaultCowOverrides;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.world.vault.modifier.ScaleModifier;
import iskallia.vault.world.vault.modifier.FrenzyModifier;
import iskallia.vault.world.vault.influence.MobAttributeInfluence;
import iskallia.vault.entity.EternalEntity;
import net.minecraft.entity.LivingEntity;
import iskallia.vault.world.vault.logic.VaultLogic;
import iskallia.vault.world.vault.player.VaultMember;

import java.util.stream.Stream;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.vault.logic.behaviour.VaultBehaviour;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Set;

import iskallia.vault.world.vault.player.VaultPlayerType;

import java.util.Map;

import iskallia.vault.util.nbt.NBTHelper;
import net.minecraft.nbt.INBT;
import net.minecraft.server.MinecraftServer;
import iskallia.vault.network.message.VaultModifierMessage;

import java.util.Collection;
import java.util.ArrayList;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;
import javax.annotation.Nullable;

import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.modifier.NoExitModifier;

import java.util.Iterator;

import iskallia.vault.world.vault.player.VaultRunner;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import iskallia.vault.world.vault.modifier.VaultModifier;
import iskallia.vault.util.PlayerFilter;

import java.util.function.Consumer;
import java.util.List;

import iskallia.vault.world.vault.event.VaultListener;
import iskallia.vault.nbt.NonNullVListNBT;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event;
import iskallia.vault.world.vault.logic.objective.KillTheBossObjective;
import iskallia.vault.world.vault.logic.objective.TreasureHuntObjective;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectSummonAndKillBossesObjective;
import iskallia.vault.world.vault.logic.objective.SummonAndKillAllBossesObjective;
import iskallia.vault.world.vault.logic.objective.CakeHuntObjective;
import iskallia.vault.world.vault.logic.objective.raid.RaidChallengeObjective;
import iskallia.vault.world.vault.logic.objective.ancient.AncientObjective;
import iskallia.vault.world.vault.logic.objective.TroveObjective;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import iskallia.vault.world.vault.logic.objective.SummonAndKillBossObjective;
import iskallia.vault.world.vault.logic.condition.VaultCondition;
import iskallia.vault.world.vault.logic.VaultSandEvent;
import iskallia.vault.world.vault.logic.VaultChestPity;
import iskallia.vault.world.vault.logic.VaultSpawner;
import iskallia.vault.attribute.StringAttribute;
import iskallia.vault.attribute.IntegerAttribute;
import iskallia.vault.world.vault.logic.VaultLobby;
import iskallia.vault.attribute.UUIDAttribute;

import java.util.UUID;

import iskallia.vault.attribute.BooleanAttribute;
import iskallia.vault.attribute.CompoundAttribute;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.attribute.EnumAttribute;
import net.minecraft.util.Direction;
import iskallia.vault.attribute.BlockPosAttribute;
import net.minecraft.util.math.BlockPos;
import iskallia.vault.attribute.BoundingBoxAttribute;
import net.minecraft.util.math.MutableBoundingBox;
import iskallia.vault.attribute.RegistryKeyAttribute;
import net.minecraft.world.World;
import net.minecraft.util.RegistryKey;
import iskallia.vault.attribute.VAttribute;
import iskallia.vault.world.vault.gen.FinalBossGenerator;
import iskallia.vault.world.vault.gen.FinalLobbyGenerator;
import iskallia.vault.world.vault.gen.RaidChallengeGenerator;
import iskallia.vault.world.vault.gen.VaultTroveGenerator;
import iskallia.vault.world.vault.gen.ArchitectEventGenerator;
import iskallia.vault.world.vault.gen.FragmentedVaultGenerator;

import java.util.function.Supplier;

import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.event.VaultEvent;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.world.vault.logic.objective.raid.ActiveRaid;
import iskallia.vault.world.vault.influence.VaultInfluences;
import iskallia.vault.world.raid.RaidProperties;
import iskallia.vault.world.vault.modifier.VaultModifiers;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.gen.VaultGenerator;
import iskallia.vault.world.vault.time.VaultTimer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class VaultRaid implements INBTSerializable<CompoundNBT> {
    protected VaultTimer timer;
    protected VaultGenerator generator;
    protected VaultTask initializer;
    protected VaultModifiers modifiers;
    protected RaidProperties properties;
    protected VaultInfluences influence;
    protected ActiveRaid activeRaid;
    protected final VListNBT<VaultObjective, CompoundNBT> objectives;
    protected final VListNBT<VaultEvent<?>, CompoundNBT> events;
    protected final VListNBT<VaultPlayer, CompoundNBT> players;
    protected long creationTime;
    public static final Supplier<FragmentedVaultGenerator> SINGLE_STAR;
    public static final Supplier<ArchitectEventGenerator> ARCHITECT_GENERATOR;
    public static final Supplier<VaultTroveGenerator> TROVE_GENERATOR;
    public static final Supplier<RaidChallengeGenerator> RAID_CHALLENGE_GENERATOR;
    public static final Supplier<FinalLobbyGenerator> FINAL_LOBBY;
    public static final Supplier<FinalBossGenerator> FINAL_BOSS;
    public static final VAttribute<RegistryKey<World>, RegistryKeyAttribute<World>> DIMENSION;
    public static final VAttribute<MutableBoundingBox, BoundingBoxAttribute> BOUNDING_BOX;
    public static final VAttribute<BlockPos, BlockPosAttribute> START_POS;
    public static final VAttribute<Direction, EnumAttribute<Direction>> START_FACING;
    public static final VAttribute<CrystalData, CompoundAttribute<CrystalData>> CRYSTAL_DATA;
    public static final VAttribute<Boolean, BooleanAttribute> IS_RAFFLE;
    public static final VAttribute<Boolean, BooleanAttribute> COW_VAULT;
    public static final VAttribute<UUID, UUIDAttribute> HOST;
    public static final VAttribute<UUID, UUIDAttribute> IDENTIFIER;
    public static final VAttribute<UUID, UUIDAttribute> PARENT;
    public static final VAttribute<VaultLobby, CompoundAttribute<VaultLobby>> LOBBY;
    public static final VAttribute<Boolean, BooleanAttribute> FORCE_ACTIVE;
    public static final VAttribute<Integer, IntegerAttribute> RAID_INDEX;
    public static final VAttribute<Boolean, BooleanAttribute> GRANTED_EXP;
    public static final VAttribute<String, StringAttribute> PLAYER_BOSS_NAME;
    @Deprecated
    public static final VAttribute<Boolean, BooleanAttribute> CAN_EXIT;
    public static final VAttribute<VaultSpawner, CompoundAttribute<VaultSpawner>> SPAWNER;
    public static final VAttribute<VaultChestPity, CompoundAttribute<VaultChestPity>> CHEST_PITY;
    public static final VAttribute<VaultSandEvent, CompoundAttribute<VaultSandEvent>> SAND_EVENT;
    public static final VAttribute<Boolean, BooleanAttribute> SHOW_TIMER;
    public static final VAttribute<Boolean, BooleanAttribute> CAN_HEAL;
    public static final VAttribute<Integer, IntegerAttribute> LEVEL;
    public static final VaultCondition IS_FINISHED;
    public static final VaultCondition IS_RUNNER;
    public static final VaultCondition IS_SPECTATOR;
    public static final VaultCondition IS_OUTSIDE;
    public static final VaultCondition AFTER_GRACE_PERIOD;
    public static final VaultCondition IS_DEAD;
    public static final VaultCondition HAS_EXITED;
    public static final VaultCondition TIME_LEFT;
    public static final VaultCondition NO_TIME_LEFT;
    public static final VaultCondition OBJECTIVES_LEFT;
    public static final VaultCondition NO_OBJECTIVES_LEFT;
    public static final VaultCondition OBJECTIVES_LEFT_GLOBALLY;
    public static final VaultCondition NO_OBJECTIVES_LEFT_GLOBALLY;
    public static final VaultCondition RUNNERS_LEFT;
    public static final VaultCondition NO_RUNNERS_LEFT;
    public static final VaultCondition ACTIVE_RUNNERS_LEFT;
    public static final VaultCondition NO_ACTIVE_RUNNERS_LEFT;
    public static final VaultTask GRANT_EXP_COMPLETE;
    public static final VaultTask GRANT_EXP_BAIL;
    public static final VaultTask GRANT_EXP_DEATH;
    public static final VaultTask CHECK_BAIL;
    public static final VaultTask CHECK_BAIL_COOP;
    public static final VaultTask CHECK_BAIL_FINAL;
    public static final VaultTask TICK_SAND_EVENT;
    public static final VaultTask TICK_CHEST_PITY;
    public static final VaultTask TICK_SPAWNER;
    public static final VaultTask TICK_LOBBY;
    public static final VaultTask TICK_INFLUENCES;
    public static final VaultTask TP_TO_START;
    public static final VaultTask INIT_LEVEL;
    public static final VaultTask INIT_LEVEL_COOP;
    public static final VaultTask INIT_LEVEL_FINAL;
    public static final VaultTask INIT_RELIC_TIME;
    @Deprecated
    public static final VaultTask INIT_FAVOUR_TIME;
    public static final VaultTask INIT_SANDS_EVENT;
    public static final VaultTask INIT_COW_VAULT;
    public static final VaultTask INIT_GLOBAL_MODIFIERS;
    public static final VaultTask RUNNER_TO_SPECTATOR;
    public static final VaultTask HIDE_OVERLAY;
    public static final VaultTask PAUSE_IN_ARENA;
    public static final VaultTask LEVEL_UP_GEAR;
    public static final VaultTask REMOVE_SCAVENGER_ITEMS;
    public static final VaultTask SAVE_SOULBOUND_GEAR;
    public static final VaultTask REMOVE_INVENTORY_RESTORE_SNAPSHOTS;
    public static final VaultTask FINAL_VICTORY_SCENE;
    public static final VaultTask EXIT_SAFELY;
    public static final VaultTask EXIT_DEATH;
    public static final VaultTask EXIT_DEATH_ALL;
    public static final VaultTask EXIT_DEATH_ALL_NO_SAVE;
    public static final VaultTask VICTORY_SCENE;
    public static final VaultTask ENTER_DISPLAY;
    public static final Supplier<SummonAndKillBossObjective> SUMMON_AND_KILL_BOSS;
    public static final Supplier<ScavengerHuntObjective> SCAVENGER_HUNT;
    public static final Supplier<ArchitectObjective> ARCHITECT_EVENT;
    public static final Supplier<TroveObjective> VAULT_TROVE;
    public static final Supplier<AncientObjective> ANCIENTS;
    public static final Supplier<RaidChallengeObjective> RAID_CHALLENGE;
    public static final Supplier<CakeHuntObjective> CAKE_HUNT;
    public static final Supplier<SummonAndKillAllBossesObjective> SUMMON_AND_KILL_ALL_BOSSES;
    public static final Supplier<ArchitectSummonAndKillBossesObjective> ARCHITECT_KILL_ALL_BOSSES;
    public static final Supplier<TreasureHuntObjective> TREASURE_HUNT;
    public static final Supplier<KillTheBossObjective> KILL_THE_BOSS;
    @Deprecated
    public static final VaultEvent<Event> TRIGGER_BOSS_SUMMON;
    public static final VaultEvent<LivingEvent.LivingUpdateEvent> SCALE_MOB;
    public static final VaultEvent<EntityJoinWorldEvent> SCALE_MOB_JOIN;
    public static final VaultEvent<LivingSpawnEvent.CheckSpawn> BLOCK_NATURAL_SPAWNING;
    public static final VaultEvent<EntityJoinWorldEvent> PREVENT_ITEM_PICKUP;
    public static final VaultEvent<EntityJoinWorldEvent> REPLACE_WITH_COW;
    public static final VaultEvent<EntityJoinWorldEvent> APPLY_SCALE_MODIFIER;
    public static final VaultEvent<EntityJoinWorldEvent> APPLY_FRENZY_MODIFIERS;
    public static final VaultEvent<EntityJoinWorldEvent> APPLY_INFLUENCE_MODIFIERS;

    public VaultRaid() {
        this.timer = new VaultTimer().start(Integer.MAX_VALUE);
        this.modifiers = new VaultModifiers();
        this.properties = new RaidProperties();
        this.influence = new VaultInfluences();
        this.activeRaid = null;
        this.objectives = VListNBT.of(VaultObjective::fromNBT);
        this.events = NonNullVListNBT.of(VaultEvent::fromNBT);
        this.players = VListNBT.of(VaultPlayer::fromNBT);
        this.creationTime = System.currentTimeMillis();
        VaultListener.listen(this);
    }

    public VaultRaid(final VaultGenerator generator, final VaultTask initializer, final RaidProperties properties, final List<VaultEvent<?>> events, final Iterable<VaultPlayer> players) {
        this.timer = new VaultTimer().start(Integer.MAX_VALUE);
        this.modifiers = new VaultModifiers();
        this.properties = new RaidProperties();
        this.influence = new VaultInfluences();
        this.activeRaid = null;
        this.objectives = VListNBT.of(VaultObjective::fromNBT);
        this.events = NonNullVListNBT.of(VaultEvent::fromNBT);
        this.players = VListNBT.of(VaultPlayer::fromNBT);
        this.creationTime = System.currentTimeMillis();
        this.generator = generator;
        this.initializer = initializer;
        this.properties = properties;
        events.forEach(this.events::add);
        players.forEach(this.players::add);
        VaultListener.listen(this);
    }

    public VaultTimer getTimer() {
        return this.timer;
    }

    public VaultGenerator getGenerator() {
        return this.generator;
    }

    public VaultTask getInitializer() {
        return this.initializer;
    }

    public VaultInfluences getInfluences() {
        return this.influence;
    }

    public VaultModifiers getModifiers() {
        return this.modifiers;
    }

    public <T extends VaultModifier> List<T> getActiveModifiersFor(final PlayerFilter filter, final Class<T> modifierClass) {
        final List<T> modifiers = (List<T>) this.getModifiers().stream().filter(modifier -> modifierClass.isAssignableFrom(modifier.getClass())).map(modifier -> modifier).collect(Collectors.toList());
        for (final VaultPlayer player : this.getPlayers()) {
            if (!(player instanceof VaultRunner) && filter.test(player.getPlayerId())) {
                player.getModifiers()
                        .stream()
                        .filter(modifier -> modifierClass.isAssignableFrom(modifier.getClass()))
                        .map(modifier -> modifier)
                        .forEach(modifier -> modifiers.add((T) modifier));
            }
        }
        return modifiers;
    }

    public boolean canExit(final VaultPlayer player) {
        return this.getActiveModifiersFor(PlayerFilter.of(player), NoExitModifier.class).isEmpty();
    }

    public boolean triggerRaid(final ServerWorld world, final BlockPos controller) {
        if (this.activeRaid != null) {
            return false;
        }
        this.activeRaid = ActiveRaid.create(this, world, controller);
        return true;
    }

    @Nullable
    public ActiveRaid getActiveRaid() {
        return this.activeRaid;
    }

    public RaidProperties getProperties() {
        return this.properties;
    }

    public List<VaultObjective> getActiveObjectives() {
        return this.getAllObjectives().stream().filter(objective -> !objective.isCompleted()).collect(Collectors.toList());
    }

    public List<VaultObjective> getAllObjectives() {
        return this.objectives;
    }

    public <T extends VaultObjective> Optional<T> getActiveObjective(final Class<T> objectiveClass) {
        return (Optional<T>) this.getAllObjectives().stream().filter(objective -> !objective.isCompleted()).filter(objective -> objectiveClass.isAssignableFrom(objective.getClass())).findFirst().map(vaultObjective -> vaultObjective);
    }

    public <T extends VaultObjective> Optional<T> getObjective(final Class<T> objectiveClass) {
        return (Optional<T>) this.getAllObjectives().stream().filter(objective -> objectiveClass.isAssignableFrom(objective.getClass())).findFirst().map(vaultObjective -> vaultObjective);
    }

    public boolean hasActiveObjective(final VaultPlayer player, final Class<? extends VaultObjective> objectiveClass) {
        return this.getActiveObjective(objectiveClass).isPresent() || player.getActiveObjective(objectiveClass).isPresent();
    }

    public List<VaultEvent<?>> getEvents() {
        return this.events;
    }

    public List<VaultPlayer> getPlayers() {
        return this.players;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public Optional<VaultPlayer> getPlayer(final PlayerEntity player) {
        return this.getPlayer(player.getUUID());
    }

    public Optional<VaultPlayer> getPlayer(final UUID playerId) {
        return this.players.stream().filter(player -> player.getPlayerId().equals(playerId)).findFirst();
    }

    public void tick(final ServerWorld world) {
        this.getGenerator().tick(world, this);
        if (this.isFinished()) {
            return;
        }
        final MinecraftServer srv = world.getServer();
        if (this.getActiveObjectives().stream().noneMatch(objective -> objective.shouldPauseTimer(srv, this))) {
            this.getTimer().tick();
        }
        this.getModifiers().tick(this, world, PlayerFilter.any());
        new ArrayList<>(this.players).forEach((player) -> {
            ((VaultPlayer) player).tick(this, world);
            ((VaultPlayer) player).sendIfPresent(world.getServer(), new VaultModifierMessage(this, ((VaultPlayer) player)));
            return;
        });
        this.getAllObjectives().stream().filter(objective -> objective.isCompleted() && objective.getCompletionTime() < 0).peek(objective -> objective.setCompletionTime(this.getTimer().getRunTime())).forEach(objective -> objective.complete(this, world));
        this.getActiveObjectives().forEach(objective -> objective.tick(this, PlayerFilter.any(), world));
        if (this.activeRaid != null) {
            this.activeRaid.tick(this, world);
            if (this.activeRaid.isFinished()) {
                this.activeRaid.finish(this, world);
                this.activeRaid = null;
            }
        }
    }

    public boolean isFinished() {
        return !this.getProperties().getBaseOrDefault(VaultRaid.FORCE_ACTIVE, false) && (this.players.isEmpty() || this.players.stream().allMatch(VaultPlayer::hasExited));
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.put("Timer", (INBT) this.timer.serializeNBT());
        nbt.put("Generator", (INBT) this.generator.serializeNBT());
        nbt.put("Modifiers", (INBT) this.modifiers.serializeNBT());
        nbt.put("influence", (INBT) this.influence.serializeNBT());
        nbt.put("Properties", (INBT) this.properties.serializeNBT());
        nbt.put("Objectives", (INBT) this.objectives.serializeNBT());
        nbt.put("Events", (INBT) this.events.serializeNBT());
        nbt.put("Players", (INBT) this.players.serializeNBT());
        nbt.putLong("CreationTime", this.getCreationTime());
        NBTHelper.writeOptional(nbt, "activeRaid", this.activeRaid, (tag, raid) -> raid.serialize(tag));
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.timer.deserializeNBT(nbt.getCompound("Timer"));
        this.generator = VaultGenerator.fromNBT(nbt.getCompound("Generator"));
        this.modifiers.deserializeNBT(nbt.getCompound("Modifiers"));
        this.influence.deserializeNBT(nbt.getCompound("influence"));
        this.properties.deserializeNBT(nbt.getCompound("Properties"));
        this.objectives.deserializeNBT(nbt.getList("Objectives", 10));
        this.events.deserializeNBT(nbt.getList("Events", 10));
        this.players.deserializeNBT(nbt.getList("Players", 10));
        this.creationTime = nbt.getLong("CreationTime");
        this.activeRaid = NBTHelper.readOptional(nbt, "activeRaid", ActiveRaid::deserializeNBT);
    }

    public static VaultRaid classic(final VaultGenerator generator, final VaultTask initializer, final RaidProperties properties, final VaultObjective objective, final List<VaultEvent<?>> events, final Map<VaultPlayerType, Set<ServerPlayerEntity>> playersMap) {
        final MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        final VaultRaid vault = new VaultRaid(generator, initializer, properties, events, playersMap.entrySet().stream().flatMap(entry -> {
            final VaultPlayerType type = entry.getKey();
            final Set<ServerPlayerEntity> players = entry.getValue();
            if (type == VaultPlayerType.RUNNER) {
                return players.stream().map(player -> {
                    final VaultRunner runner = new VaultRunner(player.getUUID());
                    runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_RUNNER.and(VaultRaid.IS_DEAD.or(VaultRaid.NO_TIME_LEFT.and(VaultRaid.OBJECTIVES_LEFT))), VaultRaid.RUNNER_TO_SPECTATOR));
                    runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_OBJECTIVES_LEFT_GLOBALLY.and(VaultRaid.NO_TIME_LEFT.or(VaultRaid.NO_RUNNERS_LEFT)), VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(VaultRaid.EXIT_SAFELY)));
                    runner.getBehaviours().add(new VaultBehaviour(VaultRaid.OBJECTIVES_LEFT_GLOBALLY.and(VaultRaid.NO_RUNNERS_LEFT), VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.SAVE_SOULBOUND_GEAR.then(VaultRaid.EXIT_DEATH))));
                    runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_FINISHED.negate(), VaultRaid.TICK_SPAWNER.then(VaultRaid.TICK_CHEST_PITY)));
                    runner.getBehaviours().add(new VaultBehaviour(VaultRaid.AFTER_GRACE_PERIOD.and(VaultRaid.IS_FINISHED.negate()), VaultRaid.TICK_INFLUENCES));
                    runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_RUNNER, VaultRaid.PAUSE_IN_ARENA.then(VaultRaid.CHECK_BAIL)));
                    runner.getProperties().create(VaultRaid.SPAWNER, new VaultSpawner());
                    runner.getProperties().create(VaultRaid.CHEST_PITY, new VaultChestPity());
                    runner.getTimer().start(objective.getVaultTimerStart(ModConfigs.VAULT_GENERAL.getTickCounter()));
                    return runner;
                });
            } else {
                if (type == VaultPlayerType.SPECTATOR) {
                }
                return Stream.empty();
            }
        }).collect(Collectors.toList()));
        vault.getAllObjectives().add(objective.thenComplete(VaultRaid.LEVEL_UP_GEAR).thenComplete(VaultRaid.VICTORY_SCENE));
        vault.getAllObjectives().forEach(obj -> obj.initialize(srv, vault));
        return vault;
    }

    public static VaultRaid coop(final VaultGenerator generator, final VaultTask initializer, final RaidProperties properties, final VaultObjective objective, final List<VaultEvent<?>> events, final Map<VaultPlayerType, Set<ServerPlayerEntity>> playersMap) {
        final MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        final VaultRaid vault = new VaultRaid(generator, initializer, properties, events, playersMap.entrySet().stream().flatMap(entry -> {
            final VaultPlayerType type = entry.getKey();
            final Set<ServerPlayerEntity> players = entry.getValue();
            if (type == VaultPlayerType.RUNNER) {
                return players.stream().map(player -> {
                    final VaultRunner runner = new VaultRunner(player.getUUID());
                    runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_OBJECTIVES_LEFT_GLOBALLY.and(VaultRaid.NO_TIME_LEFT), VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(VaultRaid.GRANT_EXP_COMPLETE.then(VaultRaid.EXIT_SAFELY))));
                    runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_RUNNER.and(VaultRaid.IS_DEAD.or(VaultRaid.NO_TIME_LEFT)), VaultRaid.EXIT_DEATH_ALL));
                    runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_FINISHED.negate(), VaultRaid.TICK_SPAWNER.then(VaultRaid.TICK_CHEST_PITY)));
                    runner.getBehaviours().add(new VaultBehaviour(VaultRaid.AFTER_GRACE_PERIOD.and(VaultRaid.IS_FINISHED.negate()), VaultRaid.TICK_INFLUENCES));
                    runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_RUNNER, VaultRaid.CHECK_BAIL_COOP));
                    runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_ACTIVE_RUNNERS_LEFT, VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(VaultRaid.GRANT_EXP_BAIL.then(VaultRaid.EXIT_SAFELY))));
                    runner.getProperties().create(VaultRaid.SPAWNER, new VaultSpawner());
                    runner.getProperties().create(VaultRaid.CHEST_PITY, new VaultChestPity());
                    runner.getTimer().start(objective.getVaultTimerStart(ModConfigs.VAULT_GENERAL.getTickCounter()));
                    return runner;
                });
            } else {
                if (type == VaultPlayerType.SPECTATOR) {
                }
                return Stream.empty();
            }
        }).collect(Collectors.toList()));
        vault.getAllObjectives().add(objective.thenComplete(VaultRaid.LEVEL_UP_GEAR).thenComplete(VaultRaid.VICTORY_SCENE));
        vault.getAllObjectives().forEach(obj -> obj.initialize(srv, vault));
        return vault;
    }

    public static VaultRaid lobby(final VaultGenerator generator, final VaultTask initializer, final RaidProperties properties, final VaultObjective objective, final List<VaultEvent<?>> events, final Map<VaultPlayerType, Set<ServerPlayerEntity>> playersMap) {
        final MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        final VaultRaid vault = new VaultRaid(generator, initializer, properties, events, playersMap.entrySet().stream().flatMap(entry -> {
            final Set<ServerPlayerEntity> players = entry.getValue();
            return players.stream().map(player -> {
                final VaultMember member = new VaultMember(player.getUUID());
                member.getProperties().create(VaultRaid.CAN_HEAL, true);
                member.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_OUTSIDE, VaultRaid.TP_TO_START));
                member.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_DEAD.negate(), VaultRaid.TICK_LOBBY));
                return member;
            });
        }).collect(Collectors.toList()));
        vault.getProperties().create(VaultRaid.LOBBY, new VaultLobby());
        vault.getProperties().create(VaultRaid.FORCE_ACTIVE, true);
        vault.getAllObjectives().forEach(obj -> obj.initialize(srv, vault));
        return vault;
    }

    public static VaultRaid boss(final VaultGenerator generator, final VaultTask initializer, final RaidProperties properties, final VaultObjective objective, final List<VaultEvent<?>> events, final Map<VaultPlayerType, Set<ServerPlayerEntity>> playersMap) {
        final MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        final VaultRaid vault = new VaultRaid(generator, initializer, properties, events, playersMap.entrySet().stream().flatMap(entry -> {
            final Set<ServerPlayerEntity> players = entry.getValue();
            return players.stream().map(player -> {
                final VaultRunner runner = new VaultRunner(player.getUUID());
                runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_OUTSIDE, VaultRaid.TP_TO_START));
                runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_DEAD.and(VaultRaid.IS_RUNNER), VaultRaid.RUNNER_TO_SPECTATOR));
                runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_OBJECTIVES_LEFT_GLOBALLY, VaultRaid.EXIT_SAFELY));
                runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_RUNNERS_LEFT, VaultRaid.EXIT_DEATH));
                runner.getProperties().create(VaultRaid.SPAWNER, new VaultSpawner());
                runner.getProperties().create(VaultRaid.SHOW_TIMER, false);
                runner.getTimer().start(30000);
                return runner;
            });
        }).collect(Collectors.toList()));
        vault.getAllObjectives().add(objective);
        vault.getAllObjectives().forEach(obj -> obj.initialize(srv, vault));
        return vault;
    }

    public static void init() {
    }

    public static Builder builder(final VaultLogic logic, final int vaultLevel, @Nullable final VaultObjective objective) {
        return new Builder(logic, vaultLevel, objective);
    }

    static {
        TimeExtension.REGISTRY.put(FruitExtension.ID, (Supplier<TimeExtension>) FruitExtension::new);
        TimeExtension.REGISTRY.put(RelicSetExtension.ID, (Supplier<TimeExtension>) RelicSetExtension::new);
        TimeExtension.REGISTRY.put(FallbackExtension.ID, (Supplier<TimeExtension>) FallbackExtension::new);
        TimeExtension.REGISTRY.put(WinExtension.ID, (Supplier<TimeExtension>) WinExtension::new);
        TimeExtension.REGISTRY.put(ModifierExtension.ID, (Supplier<TimeExtension>) ModifierExtension::new);
        TimeExtension.REGISTRY.put(TimeAltarExtension.ID, (Supplier<TimeExtension>) TimeAltarExtension::new);
        TimeExtension.REGISTRY.put(AccelerationExtension.ID, (Supplier<TimeExtension>) AccelerationExtension::new);
        TimeExtension.REGISTRY.put(RoomGenerationExtension.ID, (Supplier<TimeExtension>) RoomGenerationExtension::new);
        TimeExtension.REGISTRY.put(FavourExtension.ID, (Supplier<TimeExtension>) FavourExtension::new);
        TimeExtension.REGISTRY.put(SandExtension.ID, (Supplier<TimeExtension>) SandExtension::new);
        VaultPlayer.REGISTRY.put(VaultRunner.ID, (Supplier<VaultPlayer>) VaultRunner::new);
        VaultPlayer.REGISTRY.put(VaultSpectator.ID, (Supplier<VaultPlayer>) VaultSpectator::new);
        VaultPlayer.REGISTRY.put(VaultMember.ID, (Supplier<VaultPlayer>) VaultMember::new);
        VaultPiece.REGISTRY.put(VaultObelisk.ID, (Supplier<VaultPiece>) VaultObelisk::new);
        VaultPiece.REGISTRY.put(VaultRoom.ID, (Supplier<VaultPiece>) VaultRoom::new);
        VaultPiece.REGISTRY.put(VaultStart.ID, (Supplier<VaultPiece>) VaultStart::new);
        VaultPiece.REGISTRY.put(VaultTreasure.ID, (Supplier<VaultPiece>) VaultTreasure::new);
        VaultPiece.REGISTRY.put(VaultTunnel.ID, (Supplier<VaultPiece>) VaultTunnel::new);
        VaultPiece.REGISTRY.put(VaultRaidRoom.ID, (Supplier<VaultPiece>) VaultRaidRoom::new);
        VaultPiece.REGISTRY.put(FinalVaultLobby.ID, (Supplier<VaultPiece>) FinalVaultLobby::new);
        VaultPiece.REGISTRY.put(VaultPortal.ID, (Supplier<VaultPiece>) VaultPortal::new);
        VaultPiece.REGISTRY.put(VaultGodEye.ID, (Supplier<VaultPiece>) VaultGodEye::new);
        VaultPiece.REGISTRY.put(FinalVaultBoss.ID, (Supplier<VaultPiece>) FinalVaultBoss::new);
        VaultRoomLayoutRegistry.init();
        VaultInfluenceRegistry.init();
        SINGLE_STAR = VaultGenerator.register(() -> new FragmentedVaultGenerator(Vault.id("single_star")));
        ARCHITECT_GENERATOR = VaultGenerator.register(() -> new ArchitectEventGenerator(Vault.id("architect")));
        TROVE_GENERATOR = VaultGenerator.register(() -> new VaultTroveGenerator(Vault.id("vault_trove")));
        RAID_CHALLENGE_GENERATOR = VaultGenerator.register(() -> new RaidChallengeGenerator(Vault.id("raid_challenge")));
        FINAL_LOBBY = VaultGenerator.register(() -> new FinalLobbyGenerator(Vault.id("final_lobby")));
        FINAL_BOSS = VaultGenerator.register(() -> new FinalBossGenerator(Vault.id("final_boss")));
        DIMENSION = new VAttribute<RegistryKey<World>, RegistryKeyAttribute<World>>(Vault.id("dimension"), RegistryKeyAttribute::new);
        BOUNDING_BOX = new VAttribute<MutableBoundingBox, BoundingBoxAttribute>(Vault.id("bounding_box"), BoundingBoxAttribute::new);
        START_POS = new VAttribute<BlockPos, BlockPosAttribute>(Vault.id("start_pos"), BlockPosAttribute::new);
        START_FACING = new VAttribute<Direction, EnumAttribute<Direction>>(Vault.id("start_facing"), () -> new EnumAttribute(Direction.class));
        CRYSTAL_DATA = new VAttribute<CrystalData, CompoundAttribute<CrystalData>>(Vault.id("crystal_data"), () -> CompoundAttribute.of(CrystalData::new));
        IS_RAFFLE = new VAttribute<Boolean, BooleanAttribute>(Vault.id("is_raffle"), BooleanAttribute::new);
        COW_VAULT = new VAttribute<Boolean, BooleanAttribute>(Vault.id("cow"), BooleanAttribute::new);
        HOST = new VAttribute<UUID, UUIDAttribute>(Vault.id("host"), UUIDAttribute::new);
        IDENTIFIER = new VAttribute<UUID, UUIDAttribute>(Vault.id("identifier"), UUIDAttribute::new);
        PARENT = new VAttribute<UUID, UUIDAttribute>(Vault.id("parent"), UUIDAttribute::new);
        LOBBY = new VAttribute<VaultLobby, CompoundAttribute<VaultLobby>>(Vault.id("lobby"), () -> CompoundAttribute.of(VaultLobby::new));
        FORCE_ACTIVE = new VAttribute<Boolean, BooleanAttribute>(Vault.id("force_active"), BooleanAttribute::new);
        RAID_INDEX = new VAttribute<Integer, IntegerAttribute>(Vault.id("raid_index"), IntegerAttribute::new);
        GRANTED_EXP = new VAttribute<Boolean, BooleanAttribute>(Vault.id("granted_exp"), BooleanAttribute::new);
        PLAYER_BOSS_NAME = new VAttribute<String, StringAttribute>(Vault.id("player_boss_name"), StringAttribute::new);
        CAN_EXIT = new VAttribute<Boolean, BooleanAttribute>(Vault.id("can_exit"), BooleanAttribute::new);
        SPAWNER = new VAttribute<VaultSpawner, CompoundAttribute<VaultSpawner>>(Vault.id("spawner"), () -> CompoundAttribute.of(VaultSpawner::new));
        CHEST_PITY = new VAttribute<VaultChestPity, CompoundAttribute<VaultChestPity>>(Vault.id("chest_pity"), () -> CompoundAttribute.of(VaultChestPity::new));
        SAND_EVENT = new VAttribute<VaultSandEvent, CompoundAttribute<VaultSandEvent>>(Vault.id("sand_event"), () -> CompoundAttribute.of(VaultSandEvent::new));
        SHOW_TIMER = new VAttribute<Boolean, BooleanAttribute>(Vault.id("show_timer"), BooleanAttribute::new);
        CAN_HEAL = new VAttribute<Boolean, BooleanAttribute>(Vault.id("can_heal"), BooleanAttribute::new);
        LEVEL = new VAttribute<Integer, IntegerAttribute>(Vault.id("level"), IntegerAttribute::new);
        IS_FINISHED = VaultCondition.register(Vault.id("is_finished"), (vault, player, world) -> vault.isFinished());
        IS_RUNNER = VaultCondition.register(Vault.id("is_runner"), (vault, player, world) -> player instanceof VaultRunner);
        IS_SPECTATOR = VaultCondition.register(Vault.id("is_spectator"), (vault, player, world) -> player instanceof VaultSpectator);
        IS_OUTSIDE = VaultCondition.register(Vault.id("is_outside"), (vault, player, world) -> {
            final boolean[] outside = {false};
            player.runIfPresent(world.getServer(), sPlayer -> outside[0] = !VaultUtils.inVault(vault, (Entity) sPlayer));
            return outside[0];
        });
        AFTER_GRACE_PERIOD = VaultCondition.register(Vault.id("after_grace_period"), (vault, player, world) -> vault.getTimer().getRunTime() > 300);
        IS_DEAD = VaultCondition.register(Vault.id("is_dead"), (vault, player, world) -> {
            final MutableBoolean dead = new MutableBoolean(false);
            player.runIfPresent(world.getServer(), playerEntity -> dead.setValue(playerEntity.isDeadOrDying()));
            return dead.booleanValue();
        });
        HAS_EXITED = VaultCondition.register(Vault.id("has_exited"), (vault, player, world) -> player.hasExited());
        TIME_LEFT = VaultCondition.register(Vault.id("time_left"), (vault, player, world) -> player.getTimer().getTimeLeft() > 0);
        NO_TIME_LEFT = VaultCondition.register(Vault.id("no_time_left"), VaultRaid.TIME_LEFT.negate());
        OBJECTIVES_LEFT = VaultCondition.register(Vault.id("objectives_left"), (vault, player, world) -> player.getObjectives().size() > 0 || vault.getActiveObjectives().size() > 0);
        NO_OBJECTIVES_LEFT = VaultCondition.register(Vault.id("no_objectives_left"), VaultRaid.OBJECTIVES_LEFT.negate());
        OBJECTIVES_LEFT_GLOBALLY = VaultCondition.register(Vault.id("objectives_left_globally"), (vault, player, world) -> vault.players.stream().anyMatch(player1 -> VaultRaid.OBJECTIVES_LEFT.test(vault, player1, world)));
        NO_OBJECTIVES_LEFT_GLOBALLY = VaultCondition.register(Vault.id("no_objectives_left_globally"), VaultRaid.OBJECTIVES_LEFT_GLOBALLY.negate());
        RUNNERS_LEFT = VaultCondition.register(Vault.id("runners_left"), (vault, player, world) -> vault.players.stream().anyMatch(player1 -> player1 instanceof VaultRunner));
        NO_RUNNERS_LEFT = VaultCondition.register(Vault.id("no_runners_left"), VaultRaid.RUNNERS_LEFT.negate());
        ACTIVE_RUNNERS_LEFT = VaultCondition.register(Vault.id("active_runners_left"), (vault, player, world) -> vault.players.stream().anyMatch(player1 -> player1 instanceof VaultRunner && !player1.hasExited()));
        NO_ACTIVE_RUNNERS_LEFT = VaultCondition.register(Vault.id("no_active_runners_left"), VaultRaid.ACTIVE_RUNNERS_LEFT.negate());
        GRANT_EXP_COMPLETE = VaultTask.register(Vault.id("public_grant_exp_complete"), (vault, player, world) -> {
            if (!player.getProperties().exists(VaultRaid.GRANTED_EXP)) {
                player.grantVaultExp(world.getServer(), 1.0f);
                player.getProperties().create(VaultRaid.GRANTED_EXP, true);
            }
            return;
        });
        GRANT_EXP_BAIL = VaultTask.register(Vault.id("public_grant_exp_bail"), (vault, player, world) -> {
            if (!player.getProperties().exists(VaultRaid.GRANTED_EXP)) {
                player.grantVaultExp(world.getServer(), 0.5f);
                player.getProperties().create(VaultRaid.GRANTED_EXP, true);
            }
            return;
        });
        GRANT_EXP_DEATH = VaultTask.register(Vault.id("public_grant_exp_death"), (vault, player, world) -> {
            if (!player.getProperties().exists(VaultRaid.GRANTED_EXP)) {
                player.grantVaultExp(world.getServer(), 0.25f);
                player.getProperties().create(VaultRaid.GRANTED_EXP, true);
            }
            return;
        });
        CHECK_BAIL = VaultTask.register(Vault.id("check_bail"), (vault, player, world) -> {
            if (vault.getTimer().getRunTime() < 200) {
                return;
            } else {
                player.runIfPresent(world.getServer(), sPlayer -> {
                    if (!vault.getGenerator().getPiecesAt(sPlayer.blockPosition(), VaultStart.class).isEmpty()) {
                        final AxisAlignedBB box = sPlayer.getBoundingBox();
                        final BlockPos min = new BlockPos(box.minX + 0.001, box.minY + 0.001, box.minZ + 0.001);
                        final BlockPos max = new BlockPos(box.maxX - 0.001, box.maxY - 0.001, box.maxZ - 0.001);
                        final BlockPos.Mutable pos = new BlockPos.Mutable();
                        if (world.hasChunksAt(min, max) && !sPlayer.isOnPortalCooldown()) {
                            for (int xx = min.getX(); xx <= max.getX(); ++xx) {
                                for (int yy = min.getY(); yy <= max.getY(); ++yy) {
                                    for (int zz = min.getZ(); zz <= max.getZ(); ++zz) {
                                        final BlockState state = world.getBlockState((BlockPos) pos.set(xx, yy, zz));
                                        if (state.getBlock() == ModBlocks.VAULT_PORTAL) {
                                            if (sPlayer.isOnPortalCooldown()) {
                                                sPlayer.setPortalCooldown();
                                                return;
                                            } else if (!vault.canExit(player)) {
                                                final StringTextComponent text = new StringTextComponent("You cannot exit this Vault!");
                                                text.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)));
                                                sPlayer.displayClientMessage((ITextComponent) text, true);
                                                return;
                                            } else {
                                                vault.getAllObjectives().forEach(objective -> objective.notifyBail(vault, player, world));
                                                sPlayer.setPortalCooldown();
                                                VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(VaultRaid.EXIT_SAFELY).execute(vault, player, world);
                                                final IFormattableTextComponent playerName = sPlayer.getDisplayName().copy();
                                                playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));
                                                final StringTextComponent suffix = new StringTextComponent(" bailed.");
                                                world.getServer().getPlayerList().broadcastMessage((ITextComponent) new StringTextComponent("").append((ITextComponent) playerName).append((ITextComponent) suffix), ChatType.CHAT, player.getPlayerId());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                return;
            }
        });
        CHECK_BAIL_COOP = VaultTask.register(Vault.id("check_bail_coop"), (vault, player, world) -> {
            if (vault.getTimer().getRunTime() < 200) {
                return;
            } else {
                player.runIfPresent(world.getServer(), sPlayer -> {
                    if (!vault.getGenerator().getPiecesAt(sPlayer.blockPosition(), VaultStart.class).isEmpty()) {
                        final AxisAlignedBB box2 = sPlayer.getBoundingBox();
                        final BlockPos min2 = new BlockPos(box2.minX + 0.001, box2.minY + 0.001, box2.minZ + 0.001);
                        final BlockPos max2 = new BlockPos(box2.maxX - 0.001, box2.maxY - 0.001, box2.maxZ - 0.001);
                        final BlockPos.Mutable pos2 = new BlockPos.Mutable();
                        if (!(!world.hasChunksAt(min2, max2))) {
                            for (int i = min2.getX(); i <= max2.getX(); ++i) {
                                for (int j = min2.getY(); j <= max2.getY(); ++j) {
                                    for (int k = min2.getZ(); k <= max2.getZ(); ++k) {
                                        final BlockState state2 = world.getBlockState((BlockPos) pos2.set(i, j, k));
                                        if (state2.getBlock() == ModBlocks.VAULT_PORTAL) {
                                            if (sPlayer.isOnPortalCooldown()) {
                                                sPlayer.setPortalCooldown();
                                                return;
                                            } else if (!vault.canExit(player)) {
                                                final StringTextComponent text2 = new StringTextComponent("You cannot exit this Vault!");
                                                text2.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)));
                                                sPlayer.displayClientMessage((ITextComponent) text2, true);
                                                return;
                                            } else {
                                                vault.getAllObjectives().forEach(objective -> objective.notifyBail(vault, player, world));
                                                sPlayer.setPortalCooldown();
                                                VaultRaid.RUNNER_TO_SPECTATOR.execute(vault, player, world);
                                                VaultRaid.HIDE_OVERLAY.execute(vault, player, world);
                                                final IFormattableTextComponent playerName2 = sPlayer.getDisplayName().copy();
                                                playerName2.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));
                                                final StringTextComponent suffix2 = new StringTextComponent(" bailed.");
                                                world.getServer().getPlayerList().broadcastMessage((ITextComponent) new StringTextComponent("").append((ITextComponent) playerName2).append((ITextComponent) suffix2), ChatType.CHAT, player.getPlayerId());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                return;
            }
        });
        CHECK_BAIL_FINAL = VaultTask.register(Vault.id("check_bail_final"), (vault, player, world) -> player.runIfPresent(world.getServer(), sPlayer -> {
            if (!vault.getGenerator().getPiecesAt(sPlayer.blockPosition(), VaultStart.class).isEmpty()) {
                final AxisAlignedBB box3 = sPlayer.getBoundingBox();
                final BlockPos min3 = new BlockPos(box3.minX + 0.001, box3.minY + 0.001, box3.minZ + 0.001);
                final BlockPos max3 = new BlockPos(box3.maxX - 0.001, box3.maxY - 0.001, box3.maxZ - 0.001);
                final BlockPos.Mutable pos3 = new BlockPos.Mutable();
                if (!(!world.hasChunksAt(min3, max3))) {
                    for (int l = min3.getX(); l <= max3.getX(); ++l) {
                        for (int m = min3.getY(); m <= max3.getY(); ++m) {
                            for (int k2 = min3.getZ(); k2 <= max3.getZ(); ++k2) {
                                final BlockState state3 = world.getBlockState((BlockPos) pos3.set(l, m, k2));
                                if (state3.getBlock() == ModBlocks.VAULT_PORTAL) {
                                    if (sPlayer.isOnPortalCooldown()) {
                                        sPlayer.setPortalCooldown();
                                        return;
                                    } else if (!vault.canExit(player)) {
                                        final StringTextComponent text3 = new StringTextComponent("You cannot exit this Vault!");
                                        text3.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)));
                                        sPlayer.displayClientMessage((ITextComponent) text3, true);
                                        return;
                                    } else {
                                        vault.getAllObjectives().forEach(objective -> objective.notifyBail(vault, player, world));
                                        sPlayer.setPortalCooldown();
                                        new ArrayList<>(vault.getPlayers()).forEach(vaultPlayer -> {
                                            if (vaultPlayer instanceof VaultRunner) {
                                                VaultRaid.RUNNER_TO_SPECTATOR.execute(vault, vaultPlayer, world);
                                            }
                                            return;
                                        });
                                        vault.getProperties().create(VaultRaid.FORCE_ACTIVE, false);
                                        VaultRaid.HIDE_OVERLAY.execute(vault, player, world);
                                        final IFormattableTextComponent playerName3 = sPlayer.getDisplayName().copy();
                                        playerName3.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));
                                        final StringTextComponent suffix3 = new StringTextComponent(" bailed.");
                                        world.getServer().getPlayerList().broadcastMessage((ITextComponent) new StringTextComponent("").append((ITextComponent) playerName3).append((ITextComponent) suffix3), ChatType.CHAT, player.getPlayerId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }));
        TICK_SAND_EVENT = VaultTask.register(Vault.id("tick_sand_event"), (vault, player, world) -> vault.getProperties().getBase(VaultRaid.SAND_EVENT).ifPresent(event -> event.execute(vault, player, world)));
        TICK_CHEST_PITY = VaultTask.register(Vault.id("tick_chest_pity"), (vault, player, world) -> player.getProperties().getBase(VaultRaid.CHEST_PITY).ifPresent(event -> event.execute(vault, player, world)));
        TICK_SPAWNER = VaultTask.register(Vault.id("tick_spawner"), (vault, player, world) -> {
            if (vault.getActiveObjectives().isEmpty()) {
                return;
            } else {
                player.getProperties().get(VaultRaid.SPAWNER).ifPresent(attribute -> {
                    final VaultSpawner spawner = (VaultSpawner) attribute.getBaseValue();
                    if (player.getTimer().getRunTime() >= 300) {
                        final int level = player.getProperties().getValue(VaultRaid.LEVEL);
                        final VaultSpawner.Config c = ModConfigs.VAULT_MOBS.getForLevel(level).MOB_MISC.SPAWNER;
                        spawner.configure(config -> config.withStartMaxMobs(c.getStartMaxMobs()).withMinDistance(c.getMinDistance()).withMaxDistance(c.getMaxDistance()).withDespawnDistance(c.getDespawnDistance()));
                    }
                    spawner.execute(vault, player, world);
                    attribute.updateNBT();
                });
                return;
            }
        });
        TICK_LOBBY = VaultTask.register(Vault.id("tick_lobby"), (vault, player, world) -> vault.getProperties().get(VaultRaid.LOBBY).ifPresent(attribute -> {
            final VaultLobby lobby = (VaultLobby) attribute.getBaseValue();
            lobby.execute(vault, player, world);
            vault.getProperties().create(VaultRaid.LOBBY, lobby);
        }));
        TICK_INFLUENCES = VaultTask.register(Vault.id("tick_influences"), (vault, player, world) -> {
            if (!vault.getInfluences().isInitialized()) {
                VaultInfluenceHandler.initializeInfluences(vault, world);
                vault.getInfluences().setInitialized();
            }
            vault.getInfluences().tick(vault, player, world);
            return;
        });
        TP_TO_START = VaultTask.register(Vault.id("tp_to_start"), (vault, player, world) -> player.runIfPresent(world.getServer(), playerEntity -> {
            final BlockPos start = vault.getProperties().getBaseOrDefault(VaultRaid.START_POS, (BlockPos) null);
            final Direction facing = vault.getProperties().getBaseOrDefault(VaultRaid.START_FACING, (Direction) null);
            final MutableBoundingBox box4 = vault.getProperties().getValue(VaultRaid.BOUNDING_BOX);
            if (start == null) {
                Vault.LOGGER.warn("No vault start was found.");
                playerEntity.teleportTo(world, (double) (box4.x0 + box4.getXSpan() / 2.0f), 256.0, (double) (box4.z0 + box4.getZSpan() / 2.0f), playerEntity.yRot, playerEntity.xRot);
            } else {
                playerEntity.teleportTo(world, start.getX() + 0.5, start.getY() + 0.2, start.getZ() + 0.5, (facing == null) ? (world.getRandom().nextFloat() * 360.0f) : facing.getClockWise().toYRot(), 0.0f);
            }
            playerEntity.setPortalCooldown();
            playerEntity.setOnGround(true);
        }));
        INIT_LEVEL = VaultTask.register(Vault.id("init_level"), (vault, player, world) -> {
            final int currentLevel = vault.getProperties().getBaseOrDefault(VaultRaid.LEVEL, 0);
            final int playerLevel = PlayerVaultStatsData.get(world).getVaultStats(player.getPlayerId()).getVaultLevel();
            vault.getProperties().create(VaultRaid.LEVEL, Math.max(currentLevel, playerLevel));
            player.getProperties().create(VaultRaid.LEVEL, playerLevel);
            return;
        });
        INIT_LEVEL_COOP = VaultTask.register(Vault.id("init_level_coop"), (vault, player, world) -> vault.getProperties().getBase(VaultRaid.HOST).ifPresent(hostId -> {
            final int vaultLevel = PlayerVaultStatsData.get(world).getVaultStats(hostId).getVaultLevel();
            final int vaultLevel2 = vaultLevel + Math.max(vault.getPlayers().size() - 1, 0) * 2;
            vault.getProperties().create(VaultRaid.LEVEL, vaultLevel2);
            player.getProperties().create(VaultRaid.LEVEL, vaultLevel2);
        }));
        INIT_LEVEL_FINAL = VaultTask.register(Vault.id("init_level_final"), (vault, player, world) -> {
            vault.getProperties().create(VaultRaid.LEVEL, 1000);
            player.getProperties().create(VaultRaid.LEVEL, 1000);
            return;
        });
        INIT_RELIC_TIME = VaultTask.register(Vault.id("init_relic_extension"), (vault, player, world) -> {
            Set<String> sets = new HashSet<>();

            for (VaultPlayer player2 : vault.getPlayers()) {
                final Set<String> newSets = VaultSetsData.get(world).getCraftedSets(player2.getPlayerId());
                if (newSets.size() > sets.size()) {
                    sets = newSets;
                }
            }
            sets.stream().map(ResourceLocation::new).forEach(set -> player.getTimer().addTime(new RelicSetExtension(RelicSet.REGISTRY.get(set), ModConfigs.VAULT_RELICS.getExtraTickPerSet()), 0));
            return;
        });
        INIT_FAVOUR_TIME = VaultTask.register(Vault.id("init_favour_extension"), (vault, player, world) -> {
        });
        INIT_SANDS_EVENT = VaultTask.register(Vault.id("init_sand_event"), (vault, player, world) -> {
            if (ModConfigs.SAND_EVENT.isEnabled()) {
                vault.getProperties().create(VaultRaid.SAND_EVENT, new VaultSandEvent());
                player.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_FINISHED.negate(), VaultRaid.TICK_SAND_EVENT));
            }
            return;
        });
        INIT_COW_VAULT = VaultTask.register(Vault.id("init_cow_vault"), (vault, player, world) -> {
            if (!vault.getProperties().exists(VaultRaid.COW_VAULT)) {
                final CrystalData crystalData = vault.getProperties().getBase(VaultRaid.CRYSTAL_DATA).orElse(CrystalData.EMPTY);
                if (!crystalData.getType().canBeCowVault() || crystalData.getSelectedObjective() != null || !crystalData.getModifiers().isEmpty() || vault.getProperties().getBaseOrDefault(VaultRaid.IS_RAFFLE, false)) {
                    vault.getProperties().create(VaultRaid.COW_VAULT, false);
                } else {
                    final boolean isCowVault = VaultCowOverrides.forceSpecialVault;
                    vault.getProperties().create(VaultRaid.COW_VAULT, isCowVault);
                    if (isCowVault) {
                        VaultCowOverrides.setupVault(vault);
                        vault.getModifiers().setInitialized();
                        vault.getAllObjectives().clear();
                        final SummonAndKillBossObjective objective2 = new SummonAndKillBossObjective(Vault.id("summon_and_kill_boss"));
                        vault.getAllObjectives().add(objective2.thenComplete(VaultRaid.LEVEL_UP_GEAR).thenComplete(VaultRaid.VICTORY_SCENE));
                    }
                }
            }
            VaultCowOverrides.forceSpecialVault = false;
            return;
        });
        INIT_GLOBAL_MODIFIERS = VaultTask.register(Vault.id("init_global_modifiers"), (vault, player, world) -> {
            final Random rand = world.getRandom();
            if (!vault.getModifiers().isInitialized()) {
                final CrystalData crystalData2 = vault.getProperties().getBase(VaultRaid.CRYSTAL_DATA).orElse(CrystalData.EMPTY);
                crystalData2.apply(vault, rand);
                if (!crystalData2.preventsRandomModifiers()) {
                    vault.getModifiers().generateGlobal(vault, world, rand);
                }
                vault.getModifiers().setInitialized();
            }
            vault.getModifiers().apply(vault, player, world, rand);
            if (!player.getModifiers().isInitialized()) {
                player.getModifiers().setInitialized();
            }
            player.getModifiers().apply(vault, player, world, rand);
            return;
        });
        RUNNER_TO_SPECTATOR = VaultTask.register(Vault.id("runner_to_spectator"), (vault, player, world) -> {
            vault.players.remove(player);
            vault.players.add(new VaultSpectator((VaultRunner) player));
            return;
        });
        HIDE_OVERLAY = VaultTask.register(Vault.id("hide_overlay"), (vault, player, world) -> player.sendIfPresent(world.getServer(), VaultOverlayMessage.hide()));
        PAUSE_IN_ARENA = VaultTask.register(Vault.id("pause_in_arena"), (vault, player, world) -> {
        });
        LEVEL_UP_GEAR = VaultTask.register(Vault.id("level_up_gear"), (vault, player, world) -> {
            if (player instanceof VaultRunner) {
                player.runIfPresent(world.getServer(), playerEntity -> {

                    final EquipmentSlotType[] array = EquipmentSlotType.values();
                    int n = 0;
                    for (int length = array.length; n < length; ++n) {
                        final EquipmentSlotType slot = array[n];
                        final ItemStack stack = playerEntity.getItemBySlot(slot);
                        if (stack.getItem() instanceof VaultGear && ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {
                            VaultGear.addLevel(stack, 1.0f);
                        }
                    }
                });
            }
            return;
        });
        REMOVE_SCAVENGER_ITEMS = VaultTask.register(Vault.id("remove_scavenger_items"), (vault, player, world) -> player.runIfPresent(world.getServer(), playerEntity -> {
            final PlayerInventory inventory = playerEntity.inventory;
            for (int slot2 = 0; slot2 < inventory.getContainerSize(); ++slot2) {
                final ItemStack stack2 = inventory.getItem(slot2);
                if (stack2.getItem() instanceof BasicScavengerItem) {
                    inventory.setItem(slot2, ItemStack.EMPTY);
                }
                final LazyOptional itemHandler = stack2.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                itemHandler.ifPresent(handler -> {
                    if (handler instanceof IItemHandlerModifiable) {
                        final IItemHandlerModifiable invHandler = (IItemHandlerModifiable) handler;
                        for (int nestedSlot = 0; nestedSlot < invHandler.getSlots(); ++nestedSlot) {
                            final ItemStack nestedStack = invHandler.getStackInSlot(nestedSlot);
                            if (nestedStack.getItem() instanceof BasicScavengerItem) {
                                invHandler.setStackInSlot(nestedSlot, ItemStack.EMPTY);
                            }
                        }
                    }
                });
                if (stack2.getItem() instanceof BlockItem && ((BlockItem) stack2.getItem()).getBlock() instanceof VaultCrateBlock) {
                    final CompoundNBT tag = stack2.getTagElement("BlockEntityTag");
                    if (tag != null) {
                        final NonNullList stacks = NonNullList.withSize(54, (Object) ItemStack.EMPTY);
                        ItemStackHelper.loadAllItems(tag, stacks);
                        for (int i2 = 0; i2 < stacks.size(); ++i2) {
                            if (((ItemStack) stacks.get(i2)).getItem() instanceof BasicScavengerItem) {
                                stacks.set(i2, (Object) ItemStack.EMPTY);
                            }
                        }
                        ItemStackHelper.saveAllItems(tag, stacks);
                    }
                }
            }
        }));
        SAVE_SOULBOUND_GEAR = VaultTask.register(Vault.id("save_soulbound_gear"), (vault, player, world) -> player.runIfPresent(world.getServer(), sPlayer -> {
            if (!vault.getProperties().exists(VaultRaid.PARENT)) {
                final SoulboundSnapshotData data = SoulboundSnapshotData.get(world);
                if (!data.hasSnapshot((PlayerEntity) sPlayer)) {
                    data.createSnapshot((PlayerEntity) sPlayer);
                }
            }
        }));
        REMOVE_INVENTORY_RESTORE_SNAPSHOTS = VaultTask.register(Vault.id("remove_inventory_snapshots"), (vault, player, world) -> {
            final PhoenixModifierSnapshotData modifierData = PhoenixModifierSnapshotData.get(world);
            if (modifierData.hasSnapshot(player.getPlayerId())) {
                modifierData.removeSnapshot(player.getPlayerId());
            }
            final PhoenixSetSnapshotData setSnapshotData = PhoenixSetSnapshotData.get(world);
            if (setSnapshotData.hasSnapshot(player.getPlayerId())) {
                setSnapshotData.removeSnapshot(player.getPlayerId());
            }
            return;
        });
        FINAL_VICTORY_SCENE = VaultTask.register(Vault.id("final_victory_scene"), (vault, player, world) -> {
            if (player instanceof VaultRunner) {
                player.getTimer().addTime(new WinExtension(player.getTimer(), 400), 0);
                player.runIfPresent(world.getServer(), playerEntity -> {

                    final Entity fireworks = new FireworkRocketEntity((World) world, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), new ItemStack((IItemProvider) Items.FIREWORK_ROCKET));
                    world.addFreshEntity(fireworks);
                    world.playSound((PlayerEntity) null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1.0f, 1.0f);
                    StringTextComponent title = new StringTextComponent("Branch Cleared!");
                    title.setStyle(Style.EMPTY.withColor(Color.fromRgb(14536734)));
                    final StringTextComponent subtitle = new StringTextComponent("Place your essence in the eye.");
                    subtitle.setStyle(Style.EMPTY.withColor(Color.fromRgb(14536734)));
                    if (vault.getProperties().getValue(VaultRaid.CRYSTAL_DATA).getType() == CrystalData.Type.FINAL_BOSS) {
                        title = new StringTextComponent("The End...");
                    }
                    final STitlePacket titlePacket = new STitlePacket(STitlePacket.Type.TITLE, (ITextComponent) title);
                    playerEntity.connection.send((IPacket) titlePacket);
                });
            }
            return;
        });
        EXIT_SAFELY = VaultTask.register(Vault.id("exit_safely"), (vault, player, world) -> player.runIfPresent(world.getServer(), playerEntity -> {
            if (player instanceof VaultSpectator) {
                playerEntity.gameMode.setGameModeForPlayer(((VaultSpectator) player).oldGameType);
            }
            world.playSound((PlayerEntity) null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), ModSounds.VAULT_PORTAL_LEAVE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            world.playSound((PlayerEntity) null, (Entity) playerEntity, ModSounds.VAULT_PORTAL_LEAVE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            player.exit();
            VaultRaid.HIDE_OVERLAY.execute(vault, player, world);
            final UUID parent = vault.getProperties().getBase(VaultRaid.PARENT).orElse(null);
            final VaultRaid parentVault = (parent == null) ? null : VaultRaidData.get(world).get(parent);
            if (parentVault != null) {
                parentVault.getProperties().getBase(VaultRaid.LOBBY).ifPresent(lobby -> {
                    final VaultMember member = new VaultMember(player.getPlayerId());
                    member.getProperties().create(VaultRaid.CAN_HEAL, true);
                    member.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_OUTSIDE, VaultRaid.TP_TO_START));
                    member.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_DEAD.negate(), VaultRaid.TICK_LOBBY));
                    parentVault.getPlayers().add(member);
                    VaultRaid.TP_TO_START.execute(parentVault, member, world.getServer().getLevel((RegistryKey) parentVault.getProperties().getValue(VaultRaid.DIMENSION)));
                    if (vault.getActiveObjectives().stream().allMatch(VaultObjective::isCompleted)) {
                        VaultRaid.FINAL_VICTORY_SCENE.execute(vault, player, world);
                        player.runIfPresent(world.getServer(), sPlayer -> lobby.snapshots.removeSnapshot((PlayerEntity) sPlayer));
                    } else {
                        player.runIfPresent(world.getServer(), sPlayer -> {
                            lobby.snapshots.restoreSnapshot((PlayerEntity) sPlayer);
                            lobby.snapshots.removeSnapshot((PlayerEntity) sPlayer);
                            return;
                        });
                    }
                    parentVault.getProperties().create(VaultRaid.LOBBY, lobby);
                    lobby.exitVault(vault, world, parentVault, member, playerEntity, false);
                    vault.getPlayers().remove(player);
                    return;
                });
                vault.getProperties().create(VaultRaid.FORCE_ACTIVE, false);
            } else {
                VaultUtils.exitSafely(world.getServer().getLevel(World.OVERWORLD), playerEntity);
            }
        }));
        EXIT_DEATH = VaultTask.register(Vault.id("exit_death"), (vault, player, world) -> player.runIfPresent(world.getServer(), playerEntity -> {
            if (player instanceof VaultSpectator) {
                playerEntity.gameMode.setGameModeForPlayer(((VaultSpectator) player).oldGameType);
            }
            world.playSound((PlayerEntity) null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), ModSounds.TIMER_KILL_SFX, SoundCategory.PLAYERS, 1.0f, 1.0f);
            world.playSound((PlayerEntity) null, (Entity) playerEntity, ModSounds.TIMER_KILL_SFX, SoundCategory.PLAYERS, 1.0f, 1.0f);
            playerEntity.inventory.clearOrCountMatchingItems(stack -> true, -1, (IInventory) playerEntity.inventoryMenu.getCraftSlots());
            playerEntity.containerMenu.broadcastChanges();
            playerEntity.inventoryMenu.slotsChanged((IInventory) playerEntity.inventory);
            playerEntity.broadcastCarriedItem();
            playerEntity.hurt(new DamageSource("vaultFailed").bypassArmor().bypassInvul(), 1.0E8f);
            player.exit();
            VaultRaid.HIDE_OVERLAY.execute(vault, player, world);
            final UUID parent2 = vault.getProperties().getBase(VaultRaid.PARENT).orElse(null);
            final VaultRaid parentVault2 = (parent2 == null) ? null : VaultRaidData.get(world).get(parent2);
            if (parentVault2 != null) {
                parentVault2.getProperties().getBase(VaultRaid.LOBBY).ifPresent(lobby -> {
                    final VaultMember member2 = new VaultMember(player.getPlayerId());
                    member2.getProperties().create(VaultRaid.CAN_HEAL, true);
                    member2.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_OUTSIDE, VaultRaid.TP_TO_START));
                    member2.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_DEAD.negate(), VaultRaid.TICK_LOBBY));
                    parentVault2.getPlayers().add(member2);
                    player.runIfPresent(world.getServer(), sPlayer -> lobby.snapshots.restoreSnapshot((PlayerEntity) sPlayer));
                    parentVault2.getProperties().create(VaultRaid.LOBBY, lobby);
                    lobby.exitVault(vault, world, parentVault2, member2, playerEntity, true);
                    vault.getPlayers().remove(player);
                    return;
                });
                vault.getProperties().create(VaultRaid.FORCE_ACTIVE, false);
            }
        }));
        EXIT_DEATH_ALL = VaultTask.register(Vault.id("exit_death_all"), (vault, player, world) -> new ArrayList(vault.players).forEach(vPlayer -> VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.SAVE_SOULBOUND_GEAR.then(VaultRaid.GRANT_EXP_DEATH.then(VaultRaid.EXIT_DEATH))).execute(vault, (VaultPlayer) vPlayer, world)));
        EXIT_DEATH_ALL_NO_SAVE = VaultTask.register(Vault.id("exit_death_all_no_save"), (vault, player, world) -> new ArrayList(vault.players).forEach(vPlayer -> VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.EXIT_DEATH).execute(vault, (VaultPlayer) vPlayer, world)));
        VICTORY_SCENE = VaultTask.register(Vault.id("victory_scene"), (vault, player, world) -> {
            if (player instanceof VaultRunner) {
                player.getTimer().addTime(new WinExtension(player.getTimer(), 400), 0);
                player.runIfPresent(world.getServer(), playerEntity -> {

                    final Entity fireworks2 = new FireworkRocketEntity((World) world, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), new ItemStack((IItemProvider) Items.FIREWORK_ROCKET));
                    world.addFreshEntity(fireworks2);
                    world.playSound((PlayerEntity) null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1.0f, 1.0f);
                    final StringTextComponent title2 = new StringTextComponent("Vault Cleared!");
                    title2.setStyle(Style.EMPTY.withColor(Color.fromRgb(14536734)));
                    final StringTextComponent subtitle2 = new StringTextComponent("You'll be teleported back soon...");
                    subtitle2.setStyle(Style.EMPTY.withColor(Color.fromRgb(14536734)));
                    final STitlePacket titlePacket2 = new STitlePacket(STitlePacket.Type.TITLE, (ITextComponent) title2);
                    final STitlePacket subtitlePacket = new STitlePacket(STitlePacket.Type.SUBTITLE, (ITextComponent) subtitle2);
                    playerEntity.connection.send((IPacket) titlePacket2);
                    playerEntity.connection.send((IPacket) subtitlePacket);
                });
            }
            return;
        });
        ENTER_DISPLAY = VaultTask.register(Vault.id("enter_display"), (vault, player, world) -> player.runIfPresent(world.getServer(), playerEntity -> {
            final CrystalData data2 = vault.getProperties().getBaseOrDefault(VaultRaid.CRYSTAL_DATA, CrystalData.EMPTY);
            if (!data2.getType().isFinalType()) {
                final StringTextComponent title3 = new StringTextComponent("The Vault");
                title3.setStyle(Style.EMPTY.withColor(Color.fromRgb(14536734)));
                IFormattableTextComponent formattableTextComponent;
                if (vault.canExit(player)) {
                    formattableTextComponent = new StringTextComponent("Good luck, ").append(playerEntity.getName()).append((ITextComponent) new StringTextComponent("!"));
                } else {
                    formattableTextComponent = new StringTextComponent("No exit this time, ").append(playerEntity.getName()).append((ITextComponent) new StringTextComponent("!"));
                }
                final ITextComponent subtitle3 = (ITextComponent) formattableTextComponent;
                ((IFormattableTextComponent) subtitle3).setStyle(Style.EMPTY.withColor(Color.fromRgb(14536734)));
                final STitlePacket titlePacket3 = new STitlePacket(STitlePacket.Type.TITLE, (ITextComponent) title3);
                final STitlePacket subtitlePacket2 = new STitlePacket(STitlePacket.Type.SUBTITLE, subtitle3);
                playerEntity.connection.send((IPacket) titlePacket3);
                playerEntity.connection.send((IPacket) subtitlePacket2);
            }
            final StringTextComponent text4 = new StringTextComponent("");
            final AtomicBoolean startsWithVowel = new AtomicBoolean(false);
            vault.getModifiers().forEach((i, modifier) -> {
                text4.append(modifier.getNameComponent());
                if (i == 0) {
                    final char c3 = modifier.getName().toLowerCase().charAt(0);
                    startsWithVowel.set(c3 == 'a' || c3 == 'e' || c3 == 'i' || c3 == 'o' || c3 == 'u');
                }
                if (i != vault.getModifiers().size() - 1) {
                    text4.append((ITextComponent) new StringTextComponent(", "));
                }
                return;
            });
            Object vaultName = vault.getActiveObjectives().stream().findFirst().map(VaultObjective::getVaultName).orElse((ITextComponent) new StringTextComponent("Vault"));
            if (vault.getModifiers().isEmpty()) {
                final char c2 = ((ITextComponent) vaultName).getString().toLowerCase().charAt(0);
                startsWithVowel.set(c2 == 'a' || c2 == 'e' || c2 == 'i' || c2 == 'o' || c2 == 'u');
            }

            StringTextComponent prefix = new StringTextComponent(startsWithVowel.get() ? " entered an " : " entered a ");
            switch (data2.getType()) {
                case FINAL_LOBBY: {
                    prefix = new StringTextComponent(" entered ");
                    vaultName = new StringTextComponent("the Final Vault").withStyle(TextFormatting.DARK_PURPLE);
                    break;
                }
                case FINAL_BOSS: {
                    prefix = new StringTextComponent(" is facing ");
                    vaultName = new StringTextComponent("the Final Challenge").withStyle(TextFormatting.DARK_RED);
                    break;
                }
                case FINAL_VELARA: {
                    prefix = new StringTextComponent(" entered ");
                    vaultName = new StringTextComponent("Velara's Gluttony").withStyle(PlayerFavourData.VaultGodType.BENEVOLENT.getChatColor());
                    break;
                }
                case FINAL_TENOS: {
                    prefix = new StringTextComponent(" entered ");
                    vaultName = new StringTextComponent("Tenos' Puzzle").withStyle(PlayerFavourData.VaultGodType.OMNISCIENT.getChatColor());
                    break;
                }
                case FINAL_WENDARR: {
                    prefix = new StringTextComponent(" entered ");
                    vaultName = new StringTextComponent("Wendarr's Passage").withStyle(PlayerFavourData.VaultGodType.TIMEKEEPER.getChatColor());
                    break;
                }
                case FINAL_IDONA: {
                    prefix = new StringTextComponent(" entered ");
                    vaultName = new StringTextComponent("Idona's Wrath").withStyle(PlayerFavourData.VaultGodType.MALEVOLENCE.getChatColor());
                    break;
                }
            }
            if (!vault.getModifiers().isEmpty()) {
                text4.append((ITextComponent) new StringTextComponent(" "));
            }
            if (vault.getProperties().getBaseOrDefault(VaultRaid.COW_VAULT, false) && !vault.getProperties().exists(VaultRaid.PARENT)) {
                final StringTextComponent txt = new StringTextComponent("Vault that doesn't exist!");
                final StringTextComponent hoverText = new StringTextComponent("A vault that doesn't exist.\nThe Vault gods are not responsible for events that transpire here.\n\nThis realm may also harbor additional riches.");
                ((IFormattableTextComponent) txt).setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)).withColor(Color.fromRgb(9974168)));
                text4.append((ITextComponent) txt);
            } else {
                text4.append((ITextComponent) vaultName).append("!");
            }
            prefix.setStyle(Style.EMPTY.withColor(Color.fromRgb(16777215)));
            text4.setStyle(Style.EMPTY.withColor(Color.fromRgb(16777215)));
            final IFormattableTextComponent playerName4 = playerEntity.getDisplayName().copy();
            playerName4.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));
            world.getServer().getPlayerList().broadcastMessage((ITextComponent) playerName4.append((ITextComponent) prefix).append((ITextComponent) text4), ChatType.CHAT, player.getPlayerId());
        }));
        SUMMON_AND_KILL_BOSS = VaultObjective.register(() -> new SummonAndKillBossObjective(Vault.id("summon_and_kill_boss")));
        SCAVENGER_HUNT = VaultObjective.register(() -> new ScavengerHuntObjective(Vault.id("scavenger_hunt")));
        ARCHITECT_EVENT = VaultObjective.register(() -> new ArchitectObjective(Vault.id("architect")));
        VAULT_TROVE = VaultObjective.register(() -> new TroveObjective(Vault.id("trove")));
        ANCIENTS = VaultObjective.register(() -> new AncientObjective(Vault.id("ancients")));
        RAID_CHALLENGE = VaultObjective.register(() -> new RaidChallengeObjective(Vault.id("raid_challenge")));
        CAKE_HUNT = VaultObjective.register(() -> new CakeHuntObjective(Vault.id("cake_hunt")));
        SUMMON_AND_KILL_ALL_BOSSES = VaultObjective.register(() -> new SummonAndKillAllBossesObjective(Vault.id("summon_and_kill_all_bosses")));
        ARCHITECT_KILL_ALL_BOSSES = VaultObjective.register(() -> new ArchitectSummonAndKillBossesObjective(Vault.id("architect_kill_all_bosses")));
        TREASURE_HUNT = VaultObjective.register(() -> new TreasureHuntObjective(Vault.id("treasure_hunt")));
        KILL_THE_BOSS = VaultObjective.register(() -> new KillTheBossObjective(Vault.id("kill_the_boss")));
        TRIGGER_BOSS_SUMMON = VaultEvent.register(Vault.id("trigger_boss_summon"), Event.class, (vault, event) -> {
        });
        SCALE_MOB = VaultEvent.register(Vault.id("scale_mob"), LivingEvent.LivingUpdateEvent.class, (BiConsumer<VaultRaid, LivingEvent.LivingUpdateEvent>) EntityScaler::scaleVaultEntity);
        SCALE_MOB_JOIN = VaultEvent.register(Vault.id("scale_mob_join"), EntityJoinWorldEvent.class, (BiConsumer<VaultRaid, EntityJoinWorldEvent>) EntityScaler::scaleVaultEntity);
        BLOCK_NATURAL_SPAWNING = VaultEvent.register(Vault.id("block_natural_spawning"), LivingSpawnEvent.CheckSpawn.class, (vault, event) -> {
            if (!VaultUtils.inVault(vault, event.getEntity())) {
                return;
            } else {
                event.setResult(Event.Result.DENY);
                return;
            }
        });
        PREVENT_ITEM_PICKUP = VaultEvent.register(Vault.id("prevent_item_pickup"), EntityJoinWorldEvent.class, (vault, event) -> {
            if (event.getEntity() instanceof MobEntity) {
                final MobEntity me = (MobEntity) event.getEntity();
                if (VaultUtils.inVault(vault, event.getEntity())) {
                    me.setCanPickUpLoot(false);
                }
            }
            return;
        });
        REPLACE_WITH_COW = VaultEvent.register(Vault.id("replace_with_cow"), EntityJoinWorldEvent.class, (vault, event) -> {
            if (!(event.getWorld() instanceof ServerWorld)) {
                return;
            } else {
                final Entity entity = event.getEntity();
                if (entity.getTags().contains("replaced_entity")) {
                    return;
                } else if (!VaultUtils.inVault(vault, event.getEntity())) {
                    return;
                } else {
                    if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
                        final AggressiveCowEntity replaced = VaultCowOverrides.replaceVaultEntity(vault, (LivingEntity) entity, (ServerWorld) event.getWorld());
                        if (replaced != null) {
                            final Vector3d pos4 = entity.position();
                            ((LivingEntity) replaced).absMoveTo(pos4.x, pos4.y, pos4.z, entity.yRot, entity.xRot);
                            ServerScheduler.INSTANCE.schedule(1, () -> event.getWorld().addFreshEntity((Entity) replaced));
                            event.setCanceled(true);
                        }
                    }
                    return;
                }
            }
        });
        APPLY_SCALE_MODIFIER = VaultEvent.register(Vault.id("apply_scale_modifier"), EntityJoinWorldEvent.class, (vault, event) -> {
            if (!(event.getWorld() instanceof ServerWorld)) {
                return;
            } else if (!VaultUtils.inVault(vault, event.getEntity())) {
                return;
            } else if (!(event.getEntity() instanceof LivingEntity)) {
                return;
            } else if (event.getEntity() instanceof PlayerEntity) {
                return;
            } else if (event.getEntity() instanceof EternalEntity) {
                return;
            } else {
                final LivingEntity entity2 = (LivingEntity) event.getEntity();
                vault.getActiveModifiersFor(PlayerFilter.any(), ScaleModifier.class).forEach(modifier -> entity2.getAttribute(ModAttributes.SIZE_SCALE).setBaseValue((double) modifier.getScale()));
                return;
            }
        });
        APPLY_FRENZY_MODIFIERS = VaultEvent.register(Vault.id("frenzy_modifiers"), EntityJoinWorldEvent.class, (vault, event) -> {
            if (!(event.getWorld() instanceof ServerWorld)) {
                return;
            } else if (!VaultUtils.inVault(vault, event.getEntity())) {
                return;
            } else if (!(event.getEntity() instanceof LivingEntity)) {
                return;
            } else if (event.getEntity() instanceof PlayerEntity) {
                return;
            } else if (event.getEntity() instanceof EternalEntity) {
                return;
            } else if (event.getEntity().getTags().contains("vault_boss")) {
                return;
            } else {
                final LivingEntity entity3 = (LivingEntity) event.getEntity();
                if (entity3.getTags().contains("frenzy_scaled")) {
                    return;
                } else {
                    vault.getActiveModifiersFor(PlayerFilter.any(), FrenzyModifier.class).forEach(modifier -> modifier.applyToEntity(entity3));
                    entity3.getTags().add("frenzy_scaled");
                    return;
                }
            }
        });
        APPLY_INFLUENCE_MODIFIERS = VaultEvent.register(Vault.id("influence_modifiers"), EntityJoinWorldEvent.class, (vault, event) -> {
            if (!(!(event.getWorld() instanceof ServerWorld))) {
                if (!(!VaultUtils.inVault(vault, event.getEntity()))) {
                    if (!(!(event.getEntity() instanceof LivingEntity))) {
                        if (!(event.getEntity() instanceof PlayerEntity)) {
                            if (!(event.getEntity() instanceof EternalEntity)) {
                                final LivingEntity entity4 = (LivingEntity) event.getEntity();
                                if (!entity4.getTags().contains("influenced")) {
                                    vault.getInfluences().getInfluences(MobAttributeInfluence.class).forEach(influence -> ((MobAttributeInfluence) influence).applyTo(entity4));
                                    entity4.getTags().add("influenced");
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public static class Builder {
        private final VaultLogic logic;
        private final VaultObjective objective;
        private VaultTask initializer;
        private VaultTask levelInitializer;
        private Supplier<? extends VaultGenerator> generator;
        private final RaidProperties attributes;
        private final List<VaultEvent<?>> events;
        private final Map<VaultPlayerType, Set<ServerPlayerEntity>> players;

        protected Builder(final VaultLogic logic, final int vaultLevel, @Nullable final VaultObjective objective) {
            this.levelInitializer = VaultRaid.INIT_LEVEL;
            this.attributes = new RaidProperties();
            this.events = new ArrayList<VaultEvent<?>>();
            this.players = new HashMap<VaultPlayerType, Set<ServerPlayerEntity>>();
            this.objective = ((objective == null) ? logic.getRandomObjective(vaultLevel) : objective);
            this.generator = ((this.objective == null) ? null : this.objective.getVaultGenerator());
            this.logic = logic;
        }

        public Builder setInitializer(final VaultTask initializer) {
            this.initializer = initializer;
            return this;
        }

        public Builder setLevelInitializer(final VaultTask initializer) {
            this.levelInitializer = initializer;
            return this;
        }

        public VaultTask getLevelInitializer() {
            return this.levelInitializer;
        }

        public Builder setGenerator(final Supplier<? extends VaultGenerator> generator) {
            this.generator = generator;
            return this;
        }

        public Builder addPlayer(final VaultPlayerType type, final ServerPlayerEntity player) {
            return this.addPlayers(type, Stream.of(player));
        }

        public Builder addPlayers(final VaultPlayerType type, final Collection<ServerPlayerEntity> player) {
            return this.addPlayers(type, player.stream());
        }

        public Builder addPlayers(final VaultPlayerType type, final Stream<ServerPlayerEntity> player) {
            final Set<ServerPlayerEntity> players = this.players.computeIfAbsent(type, key -> new HashSet());
            player.forEach(players::add);
            return this;
        }

        public Builder addEvents(final VaultEvent<?>... events) {
            return this.addEvents(Arrays.asList(events));
        }

        public Builder addEvents(final Collection<VaultEvent<?>> events) {
            this.events.addAll(events);
            return this;
        }

        public <T, I extends VAttribute.Instance<T>> boolean contains(final VAttribute<T, I> attribute) {
            return this.attributes.exists(attribute);
        }

        public <T, I extends VAttribute.Instance<T>> Builder set(final VAttribute<T, I> attribute, final T value) {
            this.attributes.create(attribute, value);
            return this;
        }

        public VaultRaid build() {
            return this.logic.getFactory().create((VaultGenerator) this.generator.get(), this.initializer, this.attributes, this.objective, this.events, this.players);
        }
    }

    @FunctionalInterface
    public interface Factory {
        VaultRaid create(final VaultGenerator p0, final VaultTask p1, final RaidProperties p2, final VaultObjective p3, final List<VaultEvent<?>> p4, final Map<VaultPlayerType, Set<ServerPlayerEntity>> p5);
    }
}
