package iskallia.vault.world.vault.builder;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.event.VaultEvent;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.task.VaultTask;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;


public abstract class VaultRaidBuilder {
    public abstract VaultRaid.Builder initializeBuilder(ServerWorld paramServerWorld, ServerPlayerEntity paramServerPlayerEntity, CrystalData paramCrystalData);

    protected int getVaultLevelForObjective(ServerWorld world, ServerPlayerEntity player) {
        return PlayerVaultStatsData.get(world).getVaultStats(player.getUUID()).getVaultLevel();
    }

    protected VaultRaid.Builder getDefaultBuilder(CrystalData crystal, ServerWorld world, ServerPlayerEntity player) {
        VaultObjective vObjective = null;
        if (crystal.getSelectedObjective() != null) {
            vObjective = VaultObjective.getObjective(crystal.getSelectedObjective());
        }
        if (crystal.getTargetObjectiveCount() >= 0 && vObjective != null) {
            vObjective.setObjectiveTargetCount(crystal.getTargetObjectiveCount());
        }
        return getDefaultBuilder(crystal, getVaultLevelForObjective(world, player), vObjective);
    }

    protected VaultRaid.Builder getDefaultBuilder(CrystalData crystal, int vaultLevel, @Nullable VaultObjective objective) {
        return VaultRaid.builder(crystal.getType().getLogic(), vaultLevel, objective)
                .setInitializer(getDefaultInitializer())
                .addEvents(getDefaultEvents())
                .set(VaultRaid.CRYSTAL_DATA, crystal)
                .set(VaultRaid.IDENTIFIER, UUID.randomUUID());
    }

    protected VaultTask getDefaultInitializer() {
        return VaultRaid.TP_TO_START
                .then(VaultRaid.INIT_COW_VAULT)
                .then(VaultRaid.INIT_GLOBAL_MODIFIERS)
                .then(VaultRaid.ENTER_DISPLAY)
                .then(VaultRaid.INIT_RELIC_TIME);
    }

    protected Collection<VaultEvent<?>> getDefaultEvents() {
        return Arrays.asList((VaultEvent<?>[]) new VaultEvent[]{VaultRaid.SCALE_MOB, VaultRaid.SCALE_MOB_JOIN, VaultRaid.BLOCK_NATURAL_SPAWNING, VaultRaid.PREVENT_ITEM_PICKUP, VaultRaid.APPLY_SCALE_MODIFIER, VaultRaid.APPLY_FRENZY_MODIFIERS, VaultRaid.APPLY_INFLUENCE_MODIFIERS});
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\builder\VaultRaidBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */