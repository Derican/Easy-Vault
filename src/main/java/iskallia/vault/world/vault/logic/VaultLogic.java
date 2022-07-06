package iskallia.vault.world.vault.logic;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.VaultObjective;

import java.util.function.Function;
import java.util.function.Supplier;

public enum VaultLogic {
    CLASSIC(VaultRaid::classic, () -> ModConfigs.VAULT_GENERAL::generateObjective),
    RAFFLE(VaultRaid::classic, () -> vaultLevel -> VaultRaid.SUMMON_AND_KILL_BOSS.get()),
    COOP(VaultRaid::coop, () -> ModConfigs.VAULT_GENERAL::generateCoopObjective),
    FINAL_LOBBY(VaultRaid::lobby, () -> vaultLevel -> null),
    FINAL_BOSS(VaultRaid::boss, () -> vaultLevel -> VaultRaid.KILL_THE_BOSS.get());

    private final VaultRaid.Factory factory;
    private final Supplier<Function<Integer, VaultObjective>> objectiveGenerator;

    private VaultLogic(final VaultRaid.Factory factory, final Supplier<Function<Integer, VaultObjective>> objectiveGenerator) {
        this.factory = factory;
        this.objectiveGenerator = objectiveGenerator;
    }

    public VaultRaid.Factory getFactory() {
        return this.factory;
    }

    public VaultObjective getRandomObjective(final int vaultLevel) {
        return this.objectiveGenerator.get().apply(vaultLevel);
    }
}
