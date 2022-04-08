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
    public static final Map<ResourceLocation, Supplier<VaultPlayer>> REGISTRY = new HashMap<>();

    private ResourceLocation id;

    protected UUID playerId;
    protected boolean exited;
    protected VaultTimer timer = createTimer();
    protected VListNBT<TimeExtension, CompoundNBT> addedExtensions = VListNBT.of(TimeExtension::fromNBT);
    protected VListNBT<TimeExtension, CompoundNBT> appliedExtensions = VListNBT.of(TimeExtension::fromNBT);

    protected VaultModifiers modifiers = new VaultModifiers();
    protected RaidProperties properties = new RaidProperties();
    protected VListNBT<VaultBehaviour, CompoundNBT> behaviours = VListNBT.of(VaultBehaviour::fromNBT);
    protected VListNBT<VaultObjective, CompoundNBT> objectives = VListNBT.of(VaultObjective::fromNBT);


    public VaultPlayer(ResourceLocation id, UUID playerId) {
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
        return (List) this.behaviours;
    }

    public List<VaultObjective> getObjectives() {
        return (List<VaultObjective>) this.objectives.stream().filter(objective -> !objective.isCompleted()).collect(Collectors.toList());
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

    public void exit() {
        this.exited = true;
    }

    public VaultTimer createTimer() {
        return (new VaultTimer())
                .onExtensionAdded((timer, extension) -> this.addedExtensions.add(extension))
                .onExtensionApplied((timer, extension) -> this.appliedExtensions.add(extension));
    }

    public void tick(VaultRaid vault, ServerWorld world) {
        if (hasExited())
            return;
        getModifiers().tick(vault, world, PlayerFilter.of(new VaultPlayer[]{this}));


        MinecraftServer srv = world.getServer();
        if (vault.getActiveObjectives().stream().noneMatch(objective -> objective.shouldPauseTimer(srv, vault))) {
            tickTimer(vault, world, getTimer());
        }
        tickObjectiveUpdates(vault, world);

        getBehaviours().forEach(completion -> {
            if (!hasExited()) {
                completion.tick(vault, this, world);
            }
        });

        if (hasExited())
            return;
        getAllObjectives().stream()
                .filter(objective -> (objective.isCompleted() && objective.getCompletionTime() < 0))
                .peek(objective -> objective.setCompletionTime(getTimer().getRunTime()))
                .forEach(objective -> objective.complete(vault, this, world));

        getObjectives().forEach(objective -> objective.tick(vault, PlayerFilter.of(new VaultPlayer[]{this}, ), world));
    }


    public Optional<ServerPlayerEntity> getServerPlayer(MinecraftServer srv) {
        return Optional.ofNullable(srv.getPlayerList().getPlayer(getPlayerId()));
    }

    public boolean isOnline(MinecraftServer srv) {
        return getServerPlayer(srv).isPresent();
    }

    public void runIfPresent(MinecraftServer server, Consumer<ServerPlayerEntity> action) {
        getServerPlayer(server).ifPresent(action::accept);
    }

    public void sendIfPresent(MinecraftServer server, Object message) {
        runIfPresent(server, playerEntity -> ModNetwork.CHANNEL.sendTo(message, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }


    public void grantVaultExp(MinecraftServer server, float multiplier) {
        PlayerVaultStatsData data = PlayerVaultStatsData.get(server);
        PlayerVaultStats stats = data.getVaultStats(getPlayerId());

        float expGrantedPercent = MathHelper.clamp(this.timer.getRunTime() / this.timer.getStartTime(), 0.0F, 1.0F);
        expGrantedPercent *= multiplier;

        int vaultLevel = stats.getVaultLevel();
        expGrantedPercent *= MathHelper.clamp(1.0F - vaultLevel / 100.0F, 0.0F, 1.0F);

        float remainingPercent = 1.0F - stats.getExp() / stats.getTnl();
        if (expGrantedPercent > remainingPercent) {
            expGrantedPercent -= remainingPercent;
            int remaining = stats.getTnl() - stats.getExp();
            stats.addVaultExp(server, remaining);
        }
        int expGranted = MathHelper.floor(stats.getTnl() * expGrantedPercent);
        stats.addVaultExp(server, expGranted);
        data.setDirty();
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", getId().toString());
        nbt.putString("PlayerId", getPlayerId().toString());
        nbt.putBoolean("Exited", hasExited());

        nbt.put("Timer", (INBT) this.timer.serializeNBT());
        nbt.put("AddedExtensions", (INBT) this.addedExtensions.serializeNBT());
        nbt.put("AppliedExtensions", (INBT) this.appliedExtensions.serializeNBT());

        nbt.put("Modifiers", (INBT) this.modifiers.serializeNBT());
        nbt.put("Properties", (INBT) this.properties.serializeNBT());
        nbt.put("Behaviours", (INBT) this.behaviours.serializeNBT());
        nbt.put("Objectives", (INBT) this.objectives.serializeNBT());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
        this.playerId = UUID.fromString(nbt.getString("PlayerId"));
        this.exited = nbt.getBoolean("Exited");

        this.timer = createTimer();
        this.timer.deserializeNBT(nbt.getCompound("Timer"));
        this.addedExtensions.deserializeNBT(nbt.getList("AddedExtensions", 10));
        this.appliedExtensions.deserializeNBT(nbt.getList("AppliedExtensions", 10));

        this.modifiers.deserializeNBT(nbt.getCompound("Modifiers"));
        this.properties.deserializeNBT(nbt.getCompound("Properties"));
        this.behaviours.deserializeNBT(nbt.getList("Behaviours", 10));
        this.objectives.deserializeNBT(nbt.getList("Objectives", 10));
    }

    public static VaultPlayer fromNBT(CompoundNBT nbt) {
        ResourceLocation id = new ResourceLocation(nbt.getString("Id"));
        VaultPlayer player = ((Supplier<VaultPlayer>) REGISTRY.getOrDefault(id, () -> null)).get();

        if (player == null) {
            Vault.LOGGER.error("Player <" + id + "> is not defined.");
            return null;
        }

        try {
            player.deserializeNBT(nbt);
        } catch (Exception e) {
            Vault.LOGGER.error("Player <" + id + "> with uuid <" + nbt.getString("PlayerId") + "> could not be deserialized.");
            throw e;
        }

        return player;
    }

    public VaultPlayer() {
    }

    public abstract void tickTimer(VaultRaid paramVaultRaid, ServerWorld paramServerWorld, VaultTimer paramVaultTimer);

    public abstract void tickObjectiveUpdates(VaultRaid paramVaultRaid, ServerWorld paramServerWorld);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\player\VaultPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */