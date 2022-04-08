package iskallia.vault.world.vault.logic;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.VaultObjective;

import java.util.function.Function;
import java.util.function.Supplier;


public enum VaultLogic {
    CLASSIC(VaultRaid::classic, () -> ModConfigs.VAULT_GENERAL::generateObjective),
    RAFFLE(VaultRaid::classic, () -> ()),
    COOP(VaultRaid::coop, () -> ModConfigs.VAULT_GENERAL::generateCoopObjective);

    private final VaultRaid.Factory factory;
    private final Supplier<Function<Integer, VaultObjective>> objectiveGenerator;

    VaultLogic(VaultRaid.Factory factory, Supplier<Function<Integer, VaultObjective>> objectiveGenerator) {
        this.factory = factory;
        this.objectiveGenerator = objectiveGenerator;
    }

    public VaultRaid.Factory getFactory() {
        return this.factory;
    }

    public VaultObjective getRandomObjective(int vaultLevel) {
        return ((Function<Integer, VaultObjective>) this.objectiveGenerator.get()).apply(Integer.valueOf(vaultLevel));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\VaultLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */