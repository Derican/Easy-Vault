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
    public static final ResourceLocation ID = Vault.id("spectator");

    private VaultRunner delegate = new VaultRunner(null);
    public GameType oldGameType = GameType.NOT_SET;


    private boolean initialized = false;


    public VaultSpectator(VaultRunner delegate) {
        this(ID, delegate);
    }

    public VaultSpectator(ResourceLocation id, VaultRunner delegate) {
        super(id, delegate.getPlayerId());
        this.delegate = delegate;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public UUID getPlayerId() {
        return this.delegate.getPlayerId();
    }

    public boolean hasExited() {
        return this.delegate.hasExited();
    }

    public VaultTimer getTimer() {
        return this.delegate.getTimer();
    }

    public RaidProperties getProperties() {
        return this.delegate.getProperties();
    }

    public List<VaultBehaviour> getBehaviours() {
        return this.delegate.getBehaviours();
    }

    public List<VaultObjective> getObjectives() {
        return this.delegate.getObjectives();
    }

    public List<VaultObjective> getAllObjectives() {
        return this.delegate.getAllObjectives();
    }

    public <T extends VaultObjective> Optional<T> getActiveObjective(Class<T> type) {
        return this.delegate.getActiveObjective(type);
    }

    public void setInitialized() {
        this.initialized = true;
    }

    public void exit() {
        this.delegate.exit();
    }


    public void tick(VaultRaid vault, ServerWorld world) {
        if (hasExited())
            return;
        if (!isInitialized()) {
            runIfPresent(world.getServer(), playerEntity -> {
                this.oldGameType = playerEntity.gameMode.getGameModeForPlayer();

                playerEntity.setGameMode(GameType.SPECTATOR);
                setInitialized();
            });
        }
        super.tick(vault, world);
    }


    public void tickTimer(VaultRaid vault, ServerWorld world, VaultTimer timer) {
    }


    public void tickObjectiveUpdates(VaultRaid vault, ServerWorld world) {
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("OldGameType", this.oldGameType.ordinal());
        nbt.putBoolean("Initialized", this.initialized);
        nbt.put("Delegate", (INBT) this.delegate.serializeNBT());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.oldGameType = GameType.values()[nbt.getInt("OldGameType")];
        this.initialized = nbt.getBoolean("Initialized");
        this.delegate.deserializeNBT(nbt.getCompound("Delegate"));
        super.deserializeNBT(nbt);
    }

    public VaultSpectator() {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\player\VaultSpectator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */