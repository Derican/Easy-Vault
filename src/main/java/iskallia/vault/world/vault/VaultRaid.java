package iskallia.vault.world.vault;

import iskallia.vault.Vault;
import iskallia.vault.attribute.*;
import iskallia.vault.entity.AggressiveCowEntity;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.PhoenixModifierSnapshotData;
import iskallia.vault.world.data.PhoenixSetSnapshotData;
import iskallia.vault.world.raid.RaidProperties;
import iskallia.vault.world.vault.event.VaultEvent;
import iskallia.vault.world.vault.event.VaultListener;
import iskallia.vault.world.vault.gen.*;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.influence.MobAttributeInfluence;
import iskallia.vault.world.vault.influence.VaultInfluences;
import iskallia.vault.world.vault.logic.VaultChestPity;
import iskallia.vault.world.vault.logic.VaultCowOverrides;
import iskallia.vault.world.vault.logic.VaultLogic;
import iskallia.vault.world.vault.logic.VaultSpawner;
import iskallia.vault.world.vault.logic.condition.IVaultCondition;
import iskallia.vault.world.vault.logic.condition.VaultCondition;
import iskallia.vault.world.vault.logic.objective.*;
import iskallia.vault.world.vault.logic.objective.ancient.AncientObjective;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import iskallia.vault.world.vault.logic.objective.raid.ActiveRaid;
import iskallia.vault.world.vault.logic.objective.raid.RaidChallengeObjective;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.modifier.*;
import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.player.VaultPlayerType;
import iskallia.vault.world.vault.player.VaultRunner;
import iskallia.vault.world.vault.player.VaultSpectator;
import iskallia.vault.world.vault.time.VaultTimer;
import iskallia.vault.world.vault.time.extension.TimeExtension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VaultRaid implements INBTSerializable<CompoundNBT> {
    protected VaultTimer timer = (new VaultTimer()).start(2147483647);

    protected VaultGenerator generator;
    protected VaultTask initializer;
    protected VaultModifiers modifiers = new VaultModifiers();
    protected RaidProperties properties = new RaidProperties();
    protected VaultInfluences influence = new VaultInfluences();
    protected ActiveRaid activeRaid = null;

    protected final VListNBT<VaultObjective, CompoundNBT> objectives = VListNBT.of(VaultObjective::fromNBT);
    protected final VListNBT<VaultEvent<?>, CompoundNBT> events = NonNullVListNBT.of(VaultEvent::fromNBT);
    protected final VListNBT<VaultPlayer, CompoundNBT> players = VListNBT.of(VaultPlayer::fromNBT);

    protected long creationTime = System.currentTimeMillis();

    public VaultRaid() {
        VaultListener.listen(this);
    }


    public VaultRaid(VaultGenerator generator, VaultTask initializer, RaidProperties properties, List<VaultEvent<?>> events, Iterable<VaultPlayer> players) {
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


    public <T extends VaultModifier> List<T> getActiveModifiersFor(PlayerFilter filter, Class<T> modifierClass) {
        List<T> modifiers = (List<T>) getModifiers().stream().filter(modifier -> modifierClass.isAssignableFrom(modifier.getClass())).map(modifier -> modifier).collect(Collectors.toList());
        for (VaultPlayer player : getPlayers()) {
            if (!(player instanceof VaultRunner) &&
                    filter.test(player.getPlayerId())) {
                player.getModifiers().stream()
                        .filter(modifier -> modifierClass.isAssignableFrom(modifier.getClass()))
                        .map(modifier -> modifier)
                        .forEach(modifiers::add);
            }
        }

        return modifiers;
    }

    public boolean canExit(VaultPlayer player) {
        return getActiveModifiersFor(PlayerFilter.of(new VaultPlayer[]{player} ), NoExitModifier.class).isEmpty();
    }

    public boolean triggerRaid(ServerWorld world, BlockPos controller) {
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
        return (List<VaultObjective>) getAllObjectives().stream().filter(objective -> !objective.isCompleted()).collect(Collectors.toList());
    }

    public List<VaultObjective> getAllObjectives() {
        return (List) this.objectives;
    }

    public <T extends VaultObjective> Optional<T> getActiveObjective(Class<T> objectiveClass) {
        return getAllObjectives().stream()
                .filter(objective -> !objective.isCompleted())
                .filter(objective -> objectiveClass.isAssignableFrom(objective.getClass()))
                .findFirst()
                .map(vaultObjective -> vaultObjective);
    }

    public boolean hasActiveObjective(VaultPlayer player, Class<? extends VaultObjective> objectiveClass) {
        return (getActiveObjective(objectiveClass).isPresent() || player.getActiveObjective(objectiveClass).isPresent());
    }

    public List<VaultEvent<?>> getEvents() {
        return (List) this.events;
    }

    public List<VaultPlayer> getPlayers() {
        return (List) this.players;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public Optional<VaultPlayer> getPlayer(PlayerEntity player) {
        return getPlayer(player.getUUID());
    }

    public Optional<VaultPlayer> getPlayer(UUID playerId) {
        return this.players.stream().filter(player -> player.getPlayerId().equals(playerId)).findFirst();
    }

    public void tick(ServerWorld world) {
        getGenerator().tick(world, this);
        if (isFinished())
            return;
        MinecraftServer srv = world.getServer();
        if (getActiveObjectives().stream().noneMatch(objective -> objective.shouldPauseTimer(srv, this))) {
            getTimer().tick();
        }

        getModifiers().tick(this, world, PlayerFilter.any());

        (new ArrayList((Collection<?>) this.players)).forEach(player -> {
            player.tick(this, world);

            player.sendIfPresent(world.getServer(), new VaultModifierMessage(this, player));
        });
        getAllObjectives().stream()
                .filter(objective -> (objective.isCompleted() && objective.getCompletionTime() < 0))
                .peek(objective -> objective.setCompletionTime(getTimer().getRunTime()))
                .forEach(objective -> objective.complete(this, world));

        getActiveObjectives().forEach(objective -> objective.tick(this, PlayerFilter.any(), world));


        if (this.activeRaid != null) {
            this.activeRaid.tick(this, world);
            if (this.activeRaid.isFinished()) {
                this.activeRaid.finish(this, world);
                this.activeRaid = null;
            }
        }
    }

    public boolean isFinished() {
        return (this.players.isEmpty() || this.players.stream().allMatch(VaultPlayer::hasExited));
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Timer", (INBT) this.timer.serializeNBT());
        nbt.put("Generator", (INBT) this.generator.serializeNBT());
        nbt.put("Modifiers", (INBT) this.modifiers.serializeNBT());
        nbt.put("influence", (INBT) this.influence.serializeNBT());
        nbt.put("Properties", (INBT) this.properties.serializeNBT());
        nbt.put("Objectives", (INBT) this.objectives.serializeNBT());
        nbt.put("Events", (INBT) this.events.serializeNBT());
        nbt.put("Players", (INBT) this.players.serializeNBT());
        nbt.putLong("CreationTime", getCreationTime());
        NBTHelper.writeOptional(nbt, "activeRaid", this.activeRaid, (tag, raid) -> raid.serialize(tag));
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.timer.deserializeNBT(nbt.getCompound("Timer"));
        this.generator = VaultGenerator.fromNBT(nbt.getCompound("Generator"));
        this.modifiers.deserializeNBT(nbt.getCompound("Modifiers"));
        this.influence.deserializeNBT(nbt.getCompound("influence"));
        this.properties.deserializeNBT(nbt.getCompound("Properties"));
        this.objectives.deserializeNBT(nbt.getList("Objectives", 10));
        this.events.deserializeNBT(nbt.getList("Events", 10));
        this.players.deserializeNBT(nbt.getList("Players", 10));
        this.creationTime = nbt.getLong("CreationTime");
        this.activeRaid = (ActiveRaid) NBTHelper.readOptional(nbt, "activeRaid", ActiveRaid::deserializeNBT);
    }


    public static VaultRaid classic(VaultGenerator generator, VaultTask initializer, RaidProperties properties, VaultObjective objective, List<VaultEvent<?>> events, Map<VaultPlayerType, Set<ServerPlayerEntity>> playersMap) {
        MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);


        VaultRaid vault = new VaultRaid(generator, initializer, properties, events, (Iterable<VaultPlayer>) playersMap.entrySet().stream().flatMap(entry -> {
            VaultPlayerType type = (VaultPlayerType) entry.getKey();
            Set<ServerPlayerEntity> players = (Set<ServerPlayerEntity>) entry.getValue();
            if (type == VaultPlayerType.RUNNER) return players.stream().map(());
            if (type == VaultPlayerType.SPECTATOR) ;
            return Stream.empty();
        }).collect(Collectors.toList()));

        vault.getAllObjectives().add(objective.thenComplete(LEVEL_UP_GEAR).thenComplete(VICTORY_SCENE));
        vault.getAllObjectives().forEach(obj -> obj.initialize(srv, vault));
        return vault;
    }


    public static VaultRaid coop(VaultGenerator generator, VaultTask initializer, RaidProperties properties, VaultObjective objective, List<VaultEvent<?>> events, Map<VaultPlayerType, Set<ServerPlayerEntity>> playersMap) {
        MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);


        VaultRaid vault = new VaultRaid(generator, initializer, properties, events, (Iterable<VaultPlayer>) playersMap.entrySet().stream().flatMap(entry -> {
            VaultPlayerType type = (VaultPlayerType) entry.getKey();
            Set<ServerPlayerEntity> players = (Set<ServerPlayerEntity>) entry.getValue();
            if (type == VaultPlayerType.RUNNER) return players.stream().map(());
            if (type == VaultPlayerType.SPECTATOR) ;
            return Stream.empty();
        }).collect(Collectors.toList()));

        vault.getAllObjectives().add(objective.thenComplete(LEVEL_UP_GEAR).thenComplete(VICTORY_SCENE));
        vault.getAllObjectives().forEach(obj -> obj.initialize(srv, vault));
        return vault;
    }


    static {
        TimeExtension.REGISTRY.put(FruitExtension.ID, FruitExtension::new);
        TimeExtension.REGISTRY.put(RelicSetExtension.ID, RelicSetExtension::new);
        TimeExtension.REGISTRY.put(FallbackExtension.ID, FallbackExtension::new);
        TimeExtension.REGISTRY.put(WinExtension.ID, WinExtension::new);
        TimeExtension.REGISTRY.put(ModifierExtension.ID, ModifierExtension::new);
        TimeExtension.REGISTRY.put(TimeAltarExtension.ID, TimeAltarExtension::new);
        TimeExtension.REGISTRY.put(AccelerationExtension.ID, AccelerationExtension::new);
        TimeExtension.REGISTRY.put(RoomGenerationExtension.ID, RoomGenerationExtension::new);
        TimeExtension.REGISTRY.put(FavourExtension.ID, FavourExtension::new);

        VaultPlayer.REGISTRY.put(VaultRunner.ID, VaultRunner::new);
        VaultPlayer.REGISTRY.put(VaultSpectator.ID, VaultSpectator::new);

        VaultPiece.REGISTRY.put(VaultObelisk.ID, VaultObelisk::new);
        VaultPiece.REGISTRY.put(VaultRoom.ID, VaultRoom::new);
        VaultPiece.REGISTRY.put(VaultStart.ID, VaultStart::new);
        VaultPiece.REGISTRY.put(VaultTreasure.ID, VaultTreasure::new);
        VaultPiece.REGISTRY.put(VaultTunnel.ID, VaultTunnel::new);
        VaultPiece.REGISTRY.put(VaultRaidRoom.ID, VaultRaidRoom::new);

        VaultRoomLayoutRegistry.init();
        VaultInfluenceRegistry.init();
    }


    public static void init() {
    }


    public static final Supplier<FragmentedVaultGenerator> SINGLE_STAR = VaultGenerator.register(() -> new FragmentedVaultGenerator(Vault.id("single_star")));

    public static final Supplier<ArchitectEventGenerator> ARCHITECT_GENERATOR = VaultGenerator.register(() -> new ArchitectEventGenerator(Vault.id("architect")));

    public static final Supplier<VaultTroveGenerator> TROVE_GENERATOR = VaultGenerator.register(() -> new VaultTroveGenerator(Vault.id("vault_trove")));

    public static final Supplier<RaidChallengeGenerator> RAID_CHALLENGE_GENERATOR = VaultGenerator.register(() -> new RaidChallengeGenerator(Vault.id("raid_challenge")));


    public static final VAttribute<RegistryKey<World>, RegistryKeyAttribute<World>> DIMENSION = new VAttribute(Vault.id("dimension"), RegistryKeyAttribute::new);

    public static final VAttribute<MutableBoundingBox, BoundingBoxAttribute> BOUNDING_BOX = new VAttribute(Vault.id("bounding_box"), BoundingBoxAttribute::new);

    public static final VAttribute<BlockPos, BlockPosAttribute> START_POS = new VAttribute(Vault.id("start_pos"), BlockPosAttribute::new);

    public static final VAttribute<Direction, EnumAttribute<Direction>> START_FACING = new VAttribute(Vault.id("start_facing"), () -> new EnumAttribute(Direction.class));

    public static final VAttribute<CrystalData, CompoundAttribute<CrystalData>> CRYSTAL_DATA = new VAttribute(Vault.id("crystal_data"), () -> CompoundAttribute.of(CrystalData::new));


    public static final VAttribute<Boolean, BooleanAttribute> IS_RAFFLE = new VAttribute(Vault.id("is_raffle"), BooleanAttribute::new);

    public static final VAttribute<Boolean, BooleanAttribute> COW_VAULT = new VAttribute(Vault.id("cow"), BooleanAttribute::new);

    public static final VAttribute<UUID, UUIDAttribute> HOST = new VAttribute(Vault.id("host"), UUIDAttribute::new);

    public static final VAttribute<UUID, UUIDAttribute> IDENTIFIER = new VAttribute(Vault.id("identifier"), UUIDAttribute::new);


    public static final VAttribute<String, StringAttribute> PLAYER_BOSS_NAME = new VAttribute(Vault.id("player_boss_name"), StringAttribute::new);


    @Deprecated
    public static final VAttribute<Boolean, BooleanAttribute> CAN_EXIT = new VAttribute(Vault.id("can_exit"), BooleanAttribute::new);

    public static final VAttribute<VaultSpawner, CompoundAttribute<VaultSpawner>> SPAWNER = new VAttribute(Vault.id("spawner"), () -> CompoundAttribute.of(VaultSpawner::new));

    public static final VAttribute<VaultChestPity, CompoundAttribute<VaultChestPity>> CHEST_PITY = new VAttribute(Vault.id("chest_pity"), () -> CompoundAttribute.of(VaultChestPity::new));

    public static final VAttribute<Boolean, BooleanAttribute> GRANTED_EXP = new VAttribute(Vault.id("granted_exp"), BooleanAttribute::new);


    public static final VAttribute<Integer, IntegerAttribute> LEVEL = new VAttribute(Vault.id("level"), IntegerAttribute::new);
    public static final VaultCondition IS_FINISHED;
    public static final VaultCondition IS_RUNNER;
    public static final VaultCondition IS_SPECTATOR;
    public static final VaultCondition AFTER_GRACE_PERIOD;
    public static final VaultCondition IS_DEAD;
    public static final VaultCondition HAS_EXITED;
    public static final VaultCondition TIME_LEFT;

    static {
        IS_FINISHED = VaultCondition.register(Vault.id("is_finished"), (vault, player, world) -> vault.isFinished());


        IS_RUNNER = VaultCondition.register(Vault.id("is_runner"), (vault, player, world) -> player instanceof VaultRunner);


        IS_SPECTATOR = VaultCondition.register(Vault.id("is_spectator"), (vault, player, world) -> player instanceof VaultSpectator);


        AFTER_GRACE_PERIOD = VaultCondition.register(Vault.id("after_grace_period"), (vault, player, world) -> (vault.getTimer().getRunTime() > 300));


        IS_DEAD = VaultCondition.register(Vault.id("is_dead"), (vault, player, world) -> {
            MutableBoolean dead = new MutableBoolean(false);

            player.runIfPresent(world.getServer(), ());

            return dead.booleanValue();
        });
        HAS_EXITED = VaultCondition.register(Vault.id("has_exited"), (vault, player, world) -> player.hasExited());


        TIME_LEFT = VaultCondition.register(Vault.id("time_left"), (vault, player, world) -> (player.getTimer().getTimeLeft() > 0));
    }


    public static final VaultCondition NO_TIME_LEFT = VaultCondition.register(Vault.id("no_time_left"), (IVaultCondition) TIME_LEFT
            .negate());
    public static final VaultCondition OBJECTIVES_LEFT;

    static {
        OBJECTIVES_LEFT = VaultCondition.register(Vault.id("objectives_left"), (vault, player, world) ->

                (player.getObjectives().size() > 0 || vault.getActiveObjectives().size() > 0));
    }

    public static final VaultCondition NO_OBJECTIVES_LEFT = VaultCondition.register(Vault.id("no_objectives_left"), (IVaultCondition) OBJECTIVES_LEFT
            .negate());
    public static final VaultCondition OBJECTIVES_LEFT_GLOBALLY;

    static {
        OBJECTIVES_LEFT_GLOBALLY = VaultCondition.register(Vault.id("objectives_left_globally"), (vault, player, world) -> vault.players.stream().anyMatch(()));
    }


    public static final VaultCondition NO_OBJECTIVES_LEFT_GLOBALLY = VaultCondition.register(Vault.id("no_objectives_left_globally"), (IVaultCondition) OBJECTIVES_LEFT_GLOBALLY
            .negate());
    public static final VaultCondition RUNNERS_LEFT;

    static {
        RUNNERS_LEFT = VaultCondition.register(Vault.id("runners_left"), (vault, player, world) -> vault.players.stream().anyMatch(()));
    }


    public static final VaultCondition NO_RUNNERS_LEFT = VaultCondition.register(Vault.id("no_runners_left"), (IVaultCondition) RUNNERS_LEFT
            .negate());
    public static final VaultCondition ACTIVE_RUNNERS_LEFT;

    static {
        ACTIVE_RUNNERS_LEFT = VaultCondition.register(Vault.id("active_runners_left"), (vault, player, world) -> vault.players.stream().anyMatch(()));
    }


    public static final VaultCondition NO_ACTIVE_RUNNERS_LEFT = VaultCondition.register(Vault.id("no_active_runners_left"), (IVaultCondition) ACTIVE_RUNNERS_LEFT
            .negate());
    public static final VaultTask CHECK_BAIL;
    public static final VaultTask CHECK_BAIL_COOP;
    public static final VaultTask TICK_CHEST_PITY;
    public static final VaultTask TICK_SPAWNER;
    public static final VaultTask TICK_INFLUENCES;
    public static final VaultTask TP_TO_START;
    public static final VaultTask INIT_LEVEL;
    public static final VaultTask INIT_LEVEL_COOP;
    public static final VaultTask INIT_RELIC_TIME;

    static {
        CHECK_BAIL = VaultTask.register(Vault.id("check_bail"), (vault, player, world) -> {
            if (vault.getTimer().getRunTime() < 200) {
                return;
            }


            player.runIfPresent(world.getServer(), ());
        });


        CHECK_BAIL_COOP = VaultTask.register(Vault.id("check_bail_coop"), (vault, player, world) -> {
            if (vault.getTimer().getRunTime() < 200) {
                return;
            }


            player.runIfPresent(world.getServer(), ());
        });


        TICK_CHEST_PITY = VaultTask.register(Vault.id("tick_chest_pity"), (vault, player, world) -> player.getProperties().getBase(CHEST_PITY).ifPresent(()));


        TICK_SPAWNER = VaultTask.register(Vault.id("tick_spawner"), (vault, player, world) -> {
            if (vault.getActiveObjectives().isEmpty()) {
                return;
            }


            player.getProperties().get(SPAWNER).ifPresent(());
        });


        TICK_INFLUENCES = VaultTask.register(Vault.id("tick_influences"), (vault, player, world) -> {
            if (!vault.getInfluences().isInitialized()) {
                VaultInfluenceHandler.initializeInfluences(vault, world);

                vault.getInfluences().setInitialized();
            }

            vault.getInfluences().tick(vault, player, world);
        });

        TP_TO_START = VaultTask.register(Vault.id("tp_to_start"), (vault, player, world) -> player.runIfPresent(world.getServer(), ()));


        INIT_LEVEL = VaultTask.register(Vault.id("init_level"), (vault, player, world) -> {
            int currentLevel = ((Integer) vault.getProperties().getBaseOrDefault(LEVEL, Integer.valueOf(0))).intValue();

            int playerLevel = PlayerVaultStatsData.get(world).getVaultStats(player.getPlayerId()).getVaultLevel();

            vault.getProperties().create(LEVEL, Integer.valueOf(Math.max(currentLevel, playerLevel)));
            player.getProperties().create(LEVEL, Integer.valueOf(playerLevel));
        });
        INIT_LEVEL_COOP = VaultTask.register(Vault.id("init_level_coop"), (vault, player, world) -> vault.getProperties().getBase(HOST).ifPresent(()));


        INIT_RELIC_TIME = VaultTask.register(Vault.id("init_relic_extension"), (vault, player, world) -> {
            Set<String> sets = new HashSet<>();
            for (VaultPlayer player2 : vault.getPlayers()) {
                Set<String> newSets = VaultSetsData.get(world).getCraftedSets(player2.getPlayerId());
                if (newSets.size() > sets.size()) {
                    sets = newSets;
                }
            }
            sets.stream().map(ResourceLocation::new).forEach(());
        });
    }


    @Deprecated
    public static final VaultTask INIT_FAVOUR_TIME = VaultTask.register(Vault.id("init_favour_extension"), (vault, player, world) -> {

    });
    public static final VaultTask INIT_COW_VAULT;
    public static final VaultTask INIT_GLOBAL_MODIFIERS;
    public static final VaultTask RUNNER_TO_SPECTATOR;
    public static final VaultTask HIDE_OVERLAY;
    public static final VaultTask LEVEL_UP_GEAR;
    public static final VaultTask REMOVE_SCAVENGER_ITEMS;
    public static final VaultTask SAVE_SOULBOUND_GEAR;
    public static final VaultTask REMOVE_INVENTORY_RESTORE_SNAPSHOTS;
    public static final VaultTask GRANT_EXP_COMPLETE;
    public static final VaultTask GRANT_EXP_BAIL;
    public static final VaultTask GRANT_EXP_DEATH;
    public static final VaultTask EXIT_SAFELY;
    public static final VaultTask EXIT_DEATH;
    public static final VaultTask EXIT_DEATH_ALL;
    public static final VaultTask VICTORY_SCENE;
    public static final VaultTask ENTER_DISPLAY;

    static {
        INIT_COW_VAULT = VaultTask.register(Vault.id("init_cow_vault"), (vault, player, world) -> {
            if (!vault.getProperties().exists(COW_VAULT)) {
                CrystalData crystalData = vault.getProperties().getBase(CRYSTAL_DATA).orElse(CrystalData.EMPTY);

                int level = ((Integer) vault.getProperties().getBase(LEVEL).orElse(Integer.valueOf(0))).intValue();

                if (!crystalData.getType().canBeCowVault() || crystalData.getSelectedObjective() != null || !crystalData.getModifiers().isEmpty() || ((Boolean) vault.getProperties().getBaseOrDefault(IS_RAFFLE, Boolean.valueOf(false))).booleanValue() || level < 50) {
                    vault.getProperties().create(COW_VAULT, Boolean.valueOf(false));
                } else {
                    boolean isCowVault = (world.getRandom().nextInt(300) == 0);

                    vault.getProperties().create(COW_VAULT, Boolean.valueOf(isCowVault));

                    if (isCowVault) {
                        VaultCowOverrides.setupVault(vault);

                        vault.getModifiers().setInitialized();

                        vault.getAllObjectives().clear();

                        SummonAndKillBossObjective summonAndKillBossObjective = new SummonAndKillBossObjective(Vault.id("summon_and_kill_boss"));

                        vault.getAllObjectives().add(summonAndKillBossObjective.thenComplete(LEVEL_UP_GEAR).thenComplete(VICTORY_SCENE));
                    }
                }
            }
        });
        INIT_GLOBAL_MODIFIERS = VaultTask.register(Vault.id("init_global_modifiers"), (vault, player, world) -> {
            Random rand = world.getRandom();

            if (!vault.getModifiers().isInitialized()) {
                CrystalData crystalData = vault.getProperties().getBase(CRYSTAL_DATA).orElse(CrystalData.EMPTY);

                crystalData.apply(vault, rand);

                if (!crystalData.preventsRandomModifiers()) {
                    vault.getModifiers().generateGlobal(vault, world, rand);
                }

                vault.getModifiers().setInitialized();
            }

            vault.getModifiers().apply(vault, player, world, rand);

            if (!player.getModifiers().isInitialized()) {
                player.getModifiers().setInitialized();
            }
            player.getModifiers().apply(vault, player, world, rand);
        });
        RUNNER_TO_SPECTATOR = VaultTask.register(Vault.id("runner_to_spectator"), (vault, player, world) -> {
            vault.players.remove(player);

            vault.players.add(new VaultSpectator((VaultRunner) player));
        });

        HIDE_OVERLAY = VaultTask.register(Vault.id("hide_overlay"), (vault, player, world) -> player.sendIfPresent(world.getServer(), VaultOverlayMessage.hide()));


        LEVEL_UP_GEAR = VaultTask.register(Vault.id("level_up_gear"), (vault, player, world) -> {
            if (player instanceof VaultRunner) {
                player.runIfPresent(world.getServer(), ());
            }
        });


        REMOVE_SCAVENGER_ITEMS = VaultTask.register(Vault.id("remove_scavenger_items"), (vault, player, world) -> player.runIfPresent(world.getServer(), ()));


        SAVE_SOULBOUND_GEAR = VaultTask.register(Vault.id("save_soulbound_gear"), (vault, player, world) -> player.runIfPresent(world.getServer(), ()));


        REMOVE_INVENTORY_RESTORE_SNAPSHOTS = VaultTask.register(Vault.id("remove_inventory_snapshots"), (vault, player, world) -> {
            PhoenixModifierSnapshotData modifierData = PhoenixModifierSnapshotData.get(world);

            if (modifierData.hasSnapshot(player.getPlayerId())) {
                modifierData.removeSnapshot(player.getPlayerId());
            }

            PhoenixSetSnapshotData setSnapshotData = PhoenixSetSnapshotData.get(world);
            if (setSnapshotData.hasSnapshot(player.getPlayerId())) {
                setSnapshotData.removeSnapshot(player.getPlayerId());
            }
        });
        GRANT_EXP_COMPLETE = VaultTask.register(Vault.id("public_grant_exp_complete"), (vault, player, world) -> {
            if (!player.getProperties().exists(GRANTED_EXP)) {
                player.grantVaultExp(world.getServer(), 1.0F);

                player.getProperties().create(GRANTED_EXP, Boolean.valueOf(true));
            }
        });

        GRANT_EXP_BAIL = VaultTask.register(Vault.id("public_grant_exp_bail"), (vault, player, world) -> {
            if (!player.getProperties().exists(GRANTED_EXP)) {
                player.grantVaultExp(world.getServer(), 0.5F);

                player.getProperties().create(GRANTED_EXP, Boolean.valueOf(true));
            }
        });

        GRANT_EXP_DEATH = VaultTask.register(Vault.id("public_grant_exp_death"), (vault, player, world) -> {
            if (!player.getProperties().exists(GRANTED_EXP)) {
                player.grantVaultExp(world.getServer(), 0.25F);

                player.getProperties().create(GRANTED_EXP, Boolean.valueOf(true));
            }
        });

        EXIT_SAFELY = VaultTask.register(Vault.id("exit_safely"), (vault, player, world) -> player.runIfPresent(world.getServer(), ()));


        EXIT_DEATH = VaultTask.register(Vault.id("exit_death"), (vault, player, world) -> player.runIfPresent(world.getServer(), ()));


        EXIT_DEATH_ALL = VaultTask.register(Vault.id("exit_death_all"), (vault, player, world) -> vault.players.forEach(()));


        VICTORY_SCENE = VaultTask.register(Vault.id("victory_scene"), (vault, player, world) -> {
            if (player instanceof VaultRunner) {
                player.getTimer().addTime((TimeExtension) new WinExtension(player.getTimer(), 400), 0);


                player.runIfPresent(world.getServer(), ());
            }
        });


        ENTER_DISPLAY = VaultTask.register(Vault.id("enter_display"), (vault, player, world) -> player.runIfPresent(world.getServer(), ()));
    }


    public static final Supplier<SummonAndKillBossObjective> SUMMON_AND_KILL_BOSS = VaultObjective.register(() -> new SummonAndKillBossObjective(Vault.id("summon_and_kill_boss")));


    public static final Supplier<ScavengerHuntObjective> SCAVENGER_HUNT = VaultObjective.register(() -> new ScavengerHuntObjective(Vault.id("scavenger_hunt")));


    public static final Supplier<ArchitectObjective> ARCHITECT_EVENT = VaultObjective.register(() -> new ArchitectObjective(Vault.id("architect")));


    public static final Supplier<TroveObjective> VAULT_TROVE = VaultObjective.register(() -> new TroveObjective(Vault.id("trove")));


    public static final Supplier<AncientObjective> ANCIENTS = VaultObjective.register(() -> new AncientObjective(Vault.id("ancients")));


    public static final Supplier<RaidChallengeObjective> RAID_CHALLENGE = VaultObjective.register(() -> new RaidChallengeObjective(Vault.id("raid_challenge")));


    public static final Supplier<CakeHuntObjective> CAKE_HUNT = VaultObjective.register(() -> new CakeHuntObjective(Vault.id("cake_hunt")));


    @Deprecated
    public static final VaultEvent<Event> TRIGGER_BOSS_SUMMON = VaultEvent.register(Vault.id("trigger_boss_summon"), Event.class, (vault, event) -> {

    });


    public static final VaultEvent<LivingEvent.LivingUpdateEvent> SCALE_MOB = VaultEvent.register(Vault.id("scale_mob"), LivingEvent.LivingUpdateEvent.class, EntityScaler::scaleVaultEntity);

    public static final VaultEvent<EntityJoinWorldEvent> SCALE_MOB_JOIN = VaultEvent.register(Vault.id("scale_mob_join"), EntityJoinWorldEvent.class, EntityScaler::scaleVaultEntity);
    public static final VaultEvent<LivingSpawnEvent.CheckSpawn> BLOCK_NATURAL_SPAWNING;
    public static final VaultEvent<EntityJoinWorldEvent> PREVENT_ITEM_PICKUP;
    public static final VaultEvent<EntityJoinWorldEvent> REPLACE_WITH_COW;
    public static final VaultEvent<EntityJoinWorldEvent> APPLY_SCALE_MODIFIER;
    public static final VaultEvent<EntityJoinWorldEvent> APPLY_FRENZY_MODIFIERS;
    public static final VaultEvent<EntityJoinWorldEvent> APPLY_INFLUENCE_MODIFIERS;

    static {
        BLOCK_NATURAL_SPAWNING = VaultEvent.register(Vault.id("block_natural_spawning"), LivingSpawnEvent.CheckSpawn.class, (vault, event) -> {
            if (!VaultUtils.inVault(vault, event.getEntity())) {
                return;
            }

            event.setResult(Event.Result.DENY);
        });
        PREVENT_ITEM_PICKUP = VaultEvent.register(Vault.id("prevent_item_pickup"), EntityJoinWorldEvent.class, (vault, event) -> {
            if (event.getEntity() instanceof MobEntity) {
                MobEntity me = (MobEntity) event.getEntity();


                if (VaultUtils.inVault(vault, event.getEntity())) {
                    me.setCanPickUpLoot(false);
                }
            }
        });

        REPLACE_WITH_COW = VaultEvent.register(Vault.id("replace_with_cow"), EntityJoinWorldEvent.class, (vault, event) -> {
            if (!(event.getWorld() instanceof ServerWorld)) {
                return;
            }
            Entity entity = event.getEntity();
            if (entity.getTags().contains("replaced_entity")) {
                return;
            }
            if (!VaultUtils.inVault(vault, event.getEntity()))
                return;
            if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
                AggressiveCowEntity aggressiveCowEntity = VaultCowOverrides.replaceVaultEntity(vault, (LivingEntity) entity, (ServerWorld) event.getWorld());
                if (aggressiveCowEntity != null) {
                    Vector3d pos = entity.position();
                    aggressiveCowEntity.absMoveTo(pos.x, pos.y, pos.z, entity.yRot, entity.xRot);
                    ServerScheduler.INSTANCE.schedule(1, ());
                    event.setCanceled(true);
                }
            }
        });
        APPLY_SCALE_MODIFIER = VaultEvent.register(Vault.id("apply_scale_modifier"), EntityJoinWorldEvent.class, (vault, event) -> {
            if (!(event.getWorld() instanceof ServerWorld)) {
                return;
            }
            if (!VaultUtils.inVault(vault, event.getEntity()))
                return;
            if (!(event.getEntity() instanceof LivingEntity))
                return;
            if (event.getEntity() instanceof PlayerEntity)
                return;
            if (event.getEntity() instanceof iskallia.vault.entity.EternalEntity)
                return;
            LivingEntity entity = (LivingEntity) event.getEntity();
            vault.<ScaleModifier>getActiveModifiersFor(PlayerFilter.any(), ScaleModifier.class).forEach(());
        });
        APPLY_FRENZY_MODIFIERS = VaultEvent.register(Vault.id("frenzy_modifiers"), EntityJoinWorldEvent.class, (vault, event) -> {
            if (!(event.getWorld() instanceof ServerWorld)) {
                return;
            }
            if (!VaultUtils.inVault(vault, event.getEntity())) {
                return;
            }
            if (!(event.getEntity() instanceof LivingEntity)) {
                return;
            }
            if (event.getEntity() instanceof PlayerEntity)
                return;
            if (event.getEntity() instanceof iskallia.vault.entity.EternalEntity)
                return;
            if (event.getEntity().getTags().contains("vault_boss"))
                return;
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (entity.getTags().contains("frenzy_scaled"))
                return;
            vault.<FrenzyModifier>getActiveModifiersFor(PlayerFilter.any(), FrenzyModifier.class).forEach(());
            entity.getTags().add("frenzy_scaled");
        });
        APPLY_INFLUENCE_MODIFIERS = VaultEvent.register(Vault.id("influence_modifiers"), EntityJoinWorldEvent.class, (vault, event) -> {
            if (!(event.getWorld() instanceof ServerWorld)) {
                return;
            }
            if (!VaultUtils.inVault(vault, event.getEntity())) {
                return;
            }
            if (!(event.getEntity() instanceof LivingEntity))
                return;
            if (event.getEntity() instanceof PlayerEntity)
                return;
            if (event.getEntity() instanceof iskallia.vault.entity.EternalEntity)
                return;
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (entity.getTags().contains("influenced"))
                return;
            vault.getInfluences().getInfluences(MobAttributeInfluence.class).forEach(());
            entity.getTags().add("influenced");
        });
    }

    public static Builder builder(VaultLogic logic, int vaultLevel, @Nullable VaultObjective objective) {
        return new Builder(logic, vaultLevel, objective);
    }

    @FunctionalInterface
    public static interface Factory {
        VaultRaid create(VaultGenerator param1VaultGenerator, VaultTask param1VaultTask, RaidProperties param1RaidProperties, VaultObjective param1VaultObjective, List<VaultEvent<?>> param1List, Map<VaultPlayerType, Set<ServerPlayerEntity>> param1Map);
    }

    public static class Builder {
        private final VaultLogic logic;
        private final VaultObjective objective;
        private VaultTask initializer;
        private VaultTask levelInitializer = VaultRaid.INIT_LEVEL;
        private Supplier<? extends VaultGenerator> generator;
        private final RaidProperties attributes = new RaidProperties();
        private final List<VaultEvent<?>> events = new ArrayList<>();
        private final Map<VaultPlayerType, Set<ServerPlayerEntity>> players = new HashMap<>();

        protected Builder(VaultLogic logic, int vaultLevel, @Nullable VaultObjective objective) {
            this.objective = (objective == null) ? logic.getRandomObjective(vaultLevel) : objective;
            this.generator = this.objective.getVaultGenerator();
            this.logic = logic;
        }

        public Builder setInitializer(VaultTask initializer) {
            this.initializer = initializer;
            return this;
        }

        public Builder setLevelInitializer(VaultTask initializer) {
            this.levelInitializer = initializer;
            return this;
        }

        public VaultTask getLevelInitializer() {
            return this.levelInitializer;
        }

        public Builder setGenerator(Supplier<? extends VaultGenerator> generator) {
            this.generator = generator;
            return this;
        }

        public Builder addPlayer(VaultPlayerType type, ServerPlayerEntity player) {
            return addPlayers(type, Stream.of(player));
        }

        public Builder addPlayers(VaultPlayerType type, Collection<ServerPlayerEntity> player) {
            return addPlayers(type, player.stream());
        }

        public Builder addPlayers(VaultPlayerType type, Stream<ServerPlayerEntity> player) {
            Set<ServerPlayerEntity> players = this.players.computeIfAbsent(type, key -> new HashSet());
            player.forEach(players::add);
            return this;
        }

        public Builder addEvents(VaultEvent<?>... events) {
            return addEvents(Arrays.asList(events));
        }

        public Builder addEvents(Collection<VaultEvent<?>> events) {
            this.events.addAll(events);
            return this;
        }

        public <T, I extends VAttribute.Instance<T>> boolean contains(VAttribute<T, I> attribute) {
            return this.attributes.exists(attribute);
        }

        public <T, I extends VAttribute.Instance<T>> Builder set(VAttribute<T, I> attribute, T value) {
            this.attributes.create(attribute, value);
            return this;
        }

        public VaultRaid build() {
            return this.logic.getFactory().create(this.generator.get(), this.initializer, this.attributes, this.objective, this.events, this.players);
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\VaultRaid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */