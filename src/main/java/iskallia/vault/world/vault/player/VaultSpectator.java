package iskallia.vault.world.vault.player;

import iskallia.vault.Vault;
import iskallia.vault.world.raid.RaidProperties;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.behaviour.VaultBehaviour;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.time.VaultTimer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VaultSpectator extends VaultPlayer {
    public static final ResourceLocation ID;
    private VaultRunner delegate;
    public GameType oldGameType;
    private boolean initialized;

    public VaultSpectator() {
        this.delegate = new VaultRunner(null);
        this.oldGameType = GameType.NOT_SET;
        this.initialized = false;
    }

    public VaultSpectator(final VaultRunner delegate) {
        this(VaultSpectator.ID, delegate);
    }

    public VaultSpectator(final ResourceLocation id, final VaultRunner delegate) {
        super(id, delegate.getPlayerId());
        this.delegate = new VaultRunner(null);
        this.oldGameType = GameType.NOT_SET;
        this.initialized = false;
        this.delegate = delegate;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    @Override
    public UUID getPlayerId() {
        return this.delegate.getPlayerId();
    }

    @Override
    public boolean hasExited() {
        return this.delegate.hasExited();
    }

    @Override
    public VaultTimer getTimer() {
        return this.delegate.getTimer();
    }

    @Override
    public RaidProperties getProperties() {
        return this.delegate.getProperties();
    }

    @Override
    public List<VaultBehaviour> getBehaviours() {
        return this.delegate.getBehaviours();
    }

    @Override
    public List<VaultObjective> getObjectives() {
        return this.delegate.getObjectives();
    }

    @Override
    public List<VaultObjective> getAllObjectives() {
        return this.delegate.getAllObjectives();
    }

    @Override
    public <T extends VaultObjective> Optional<T> getActiveObjective(final Class<T> type) {
        return this.delegate.getActiveObjective(type);
    }

    public void setInitialized() {
        this.initialized = true;
    }

    @Override
    public void exit() {
        this.delegate.exit();
    }

    @Override
    public void tick(final VaultRaid vault, final ServerWorld world) {
        if (this.hasExited()) {
            return;
        }
        if (!this.isInitialized()) {
            this.runIfPresent(world.getServer(), playerEntity -> {
                this.oldGameType = playerEntity.gameMode.getGameModeForPlayer();
                playerEntity.setGameMode(GameType.SPECTATOR);
                this.setInitialized();
                return;
            });
        }
        super.tick(vault, world);
    }

    @Override
    public void tickTimer(final VaultRaid vault, final ServerWorld world, final VaultTimer timer) {
    }

    @Override
    public void tickObjectiveUpdates(final VaultRaid vault, final ServerWorld world) {
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("OldGameType", this.oldGameType.ordinal());
        nbt.putBoolean("Initialized", this.initialized);
        nbt.put("Delegate", (INBT) this.delegate.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        this.oldGameType = GameType.values()[nbt.getInt("OldGameType")];
        this.initialized = nbt.getBoolean("Initialized");
        this.delegate.deserializeNBT(nbt.getCompound("Delegate"));
        super.deserializeNBT(nbt);
    }

    static {
        ID = Vault.id("spectator");
    }
}
