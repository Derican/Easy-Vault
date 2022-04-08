package iskallia.vault.world.vault.player;

import iskallia.vault.Vault;
import iskallia.vault.network.message.VaultOverlayMessage;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.SummonAndKillBossObjective;
import iskallia.vault.world.vault.time.VaultTimer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class VaultRunner
        extends VaultPlayer {
    public static final ResourceLocation ID = Vault.id("runner");

    public VaultRunner() {
    }

    public VaultRunner(UUID playerId) {
        this(ID, playerId);
    }

    public VaultRunner(ResourceLocation id, UUID playerId) {
        super(id, playerId);
    }


    public void tickTimer(VaultRaid vault, ServerWorld world, VaultTimer timer) {
        timer.tick();
        runIfPresent(world.getServer(), player -> {
            this.addedExtensions.clear();
            this.appliedExtensions.clear();
        });
    }


    public void tickObjectiveUpdates(VaultRaid vault, ServerWorld world) {
        runIfPresent(world.getServer(), player -> {
            boolean earlyKill = false;
            if (vault.hasActiveObjective(this, SummonAndKillBossObjective.class)) {
                boolean isRaffle = ((Boolean) vault.getProperties().getBase(VaultRaid.IS_RAFFLE).orElse(Boolean.valueOf(false))).booleanValue();
                if (isRaffle) {
                    PlayerVaultStatsData.PlayerRecordEntry fastestVault = PlayerVaultStatsData.get(world).getFastestVaultTime();
                    earlyKill = (this.timer.getRunTime() < fastestVault.getTickCount());
                }
            }
            sendIfPresent(world.getServer(), VaultOverlayMessage.forVault(this.timer.getTimeLeft(), earlyKill));
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\player\VaultRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */