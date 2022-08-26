package iskallia.vault.world.vault.player;

import iskallia.vault.Vault;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.raid.RaidProperties;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.behaviour.VaultBehaviour;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.modifier.VaultModifiers;
import iskallia.vault.world.vault.time.VaultTimer;
import iskallia.vault.world.vault.time.extension.TimeExtension;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class VaultPlayer implements INBTSerializable<CompoundNBT> {
    public static final Map<ResourceLocation, Supplier<VaultPlayer>> REGISTRY;
    private ResourceLocation id;
    protected UUID playerId;
    protected boolean exited;
    protected VaultTimer timer;
    protected VListNBT<TimeExtension, CompoundNBT> addedExtensions;
    protected VListNBT<TimeExtension, CompoundNBT> appliedExtensions;
    protected VaultModifiers modifiers;
    protected RaidProperties properties;
    protected VListNBT<VaultBehaviour, CompoundNBT> behaviours;
    protected VListNBT<VaultObjective, CompoundNBT> objectives;

    public VaultPlayer() {
        this.timer = this.createTimer();
        this.addedExtensions = VListNBT.of(TimeExtension::fromNBT);
        this.appliedExtensions = VListNBT.of(TimeExtension::fromNBT);
        this.modifiers = new VaultModifiers();
        this.properties = new RaidProperties();
        this.behaviours = VListNBT.of(VaultBehaviour::fromNBT);
        this.objectives = VListNBT.of(VaultObjective::fromNBT);
    }

    public VaultPlayer(final ResourceLocation id, final UUID playerId) {
        this.timer = this.createTimer();
        this.addedExtensions = VListNBT.of(TimeExtension::fromNBT);
        this.appliedExtensions = VListNBT.of(TimeExtension::fromNBT);
        this.modifiers = new VaultModifiers();
        this.properties = new RaidProperties();
        this.behaviours = VListNBT.of(VaultBehaviour::fromNBT);
        this.objectives = VListNBT.of(VaultObjective::fromNBT);
        this.id = id;
        this.playerId = playerId;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public boolean hasExited() {
        return this.exited;
    }

    public VaultTimer getTimer() {
        return this.timer;
    }

    public VaultModifiers getModifiers() {
        return this.modifiers;
    }

    public RaidProperties getProperties() {
        return this.properties;
    }

    public List<VaultBehaviour> getBehaviours() {
        return this.behaviours;
    }

    public List<VaultObjective> getObjectives() {
        return this.objectives.stream().filter(objective -> !objective.isCompleted()).collect(Collectors.toList());
    }

    public List<VaultObjective> getAllObjectives() {
        return this.objectives;
    }

    public <T extends VaultObjective> Optional<T> getActiveObjective(final Class<T> objectiveClass) {
        return (Optional<T>) this.getAllObjectives().stream().filter(objective -> !objective.isCompleted()).filter(objective -> objectiveClass.isAssignableFrom(objective.getClass())).findFirst().map(vaultObjective -> vaultObjective);
    }

    public void exit() {
        this.exited = true;
    }

    public VaultTimer createTimer() {
        return new VaultTimer().onExtensionAdded((timer, extension) -> this.addedExtensions.add(extension)).onExtensionApplied((timer, extension) -> this.appliedExtensions.add(extension));
    }

    public void tick(final VaultRaid vault, final ServerWorld world) {
        if (this.hasExited()) {
            return;
        }
        this.getModifiers().tick(vault, world, PlayerFilter.of(this));
        final MinecraftServer srv = world.getServer();
        if (vault.getActiveObjectives().stream().noneMatch(objective -> objective.shouldPauseTimer(srv, vault))) {
            this.tickTimer(vault, world, this.getTimer());
        }
        this.tickObjectiveUpdates(vault, world);
        this.getBehaviours().forEach(completion -> {
            if (!this.hasExited()) {
                completion.tick(vault, this, world);
            }
            return;
        });
        if (this.hasExited()) {
            return;
        }
        this.getAllObjectives().stream().filter(objective -> objective.isCompleted() && objective.getCompletionTime() < 0).peek(objective -> objective.setCompletionTime(this.getTimer().getRunTime())).forEach(objective -> objective.complete(vault, this, world));
        this.getObjectives().forEach(objective -> objective.tick(vault, PlayerFilter.of(this), world));
    }

    public abstract void tickTimer(final VaultRaid p0, final ServerWorld p1, final VaultTimer p2);

    public abstract void tickObjectiveUpdates(final VaultRaid p0, final ServerWorld p1);

    public Optional<ServerPlayerEntity> getServerPlayer(final MinecraftServer srv) {
        return Optional.ofNullable(srv.getPlayerList().getPlayer(this.getPlayerId()));
    }

    public boolean isOnline(final MinecraftServer srv) {
        return this.getServerPlayer(srv).isPresent();
    }

    public void runIfPresent(final MinecraftServer server, final Consumer<ServerPlayerEntity> action) {
        this.getServerPlayer(server).ifPresent(action::accept);
    }

    public void sendIfPresent(final MinecraftServer server, final Object message) {
        this.runIfPresent(server, playerEntity -> ModNetwork.CHANNEL.sendTo(message, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }

    public void grantVaultExp(final MinecraftServer server, final float multiplier) {
        final PlayerVaultStatsData data = PlayerVaultStatsData.get(server);
        final PlayerVaultStats stats = data.getVaultStats(this.getPlayerId());
        float expGrantedPercent = MathHelper.clamp(this.timer.getRunTime() / (float) this.timer.getStartTime(), 0.0f, 1.0f);
        expGrantedPercent *= multiplier;
        final int vaultLevel = stats.getVaultLevel();
        expGrantedPercent *= MathHelper.clamp(1.0f - vaultLevel / 100.0f, 0.0f, 1.0f);
        final float remainingPercent = 1.0f - stats.getExp() / (float) stats.getTnl();
        if (expGrantedPercent > remainingPercent) {
            expGrantedPercent -= remainingPercent;
            final int remaining = stats.getTnl() - stats.getExp();
            stats.addVaultExp(server, remaining);
        }
        final int expGranted = MathHelper.floor(stats.getTnl() * expGrantedPercent);
        stats.addVaultExp(server, expGranted);
        data.setDirty();
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", this.getId().toString());
        nbt.putString("PlayerId", this.getPlayerId().toString());
        nbt.putBoolean("Exited", this.hasExited());
        nbt.put("Timer", this.timer.serializeNBT());
        nbt.put("AddedExtensions", this.addedExtensions.serializeNBT());
        nbt.put("AppliedExtensions", this.appliedExtensions.serializeNBT());
        nbt.put("Modifiers", this.modifiers.serializeNBT());
        nbt.put("Properties", this.properties.serializeNBT());
        nbt.put("Behaviours", this.behaviours.serializeNBT());
        nbt.put("Objectives", this.objectives.serializeNBT());
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
        this.playerId = UUID.fromString(nbt.getString("PlayerId"));
        this.exited = nbt.getBoolean("Exited");
        (this.timer = this.createTimer()).deserializeNBT(nbt.getCompound("Timer"));
        this.addedExtensions.deserializeNBT(nbt.getList("AddedExtensions", 10));
        this.appliedExtensions.deserializeNBT(nbt.getList("AppliedExtensions", 10));
        this.modifiers.deserializeNBT(nbt.getCompound("Modifiers"));
        this.properties.deserializeNBT(nbt.getCompound("Properties"));
        this.behaviours.deserializeNBT(nbt.getList("Behaviours", 10));
        this.objectives.deserializeNBT(nbt.getList("Objectives", 10));
    }

    public static VaultPlayer fromNBT(final CompoundNBT nbt) {
        final ResourceLocation id = new ResourceLocation(nbt.getString("Id"));
        final VaultPlayer player = VaultPlayer.REGISTRY.getOrDefault(id, () -> null).get();
        if (player == null) {
            Vault.LOGGER.error("Player <" + id + "> is not defined.");
            return null;
        }
        try {
            player.deserializeNBT(nbt);
        } catch (final Exception e) {
            Vault.LOGGER.error("Player <" + id + "> with uuid <" + nbt.getString("PlayerId") + "> could not be deserialized.");
            throw e;
        }
        return player;
    }

    static {
        REGISTRY = new HashMap<ResourceLocation, Supplier<VaultPlayer>>();
    }
}
