package iskallia.vault.item.catalyst;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.VaultCrystalCatalystConfig;
import iskallia.vault.init.ModConfigs;

import javax.annotation.Nullable;
import java.util.Random;


public class SingleModifierOutcome {
    @Expose
    private final ModifierRollType type;
    @Expose
    private final String pool;

    public SingleModifierOutcome(ModifierRollType type, String pool) {
        this.type = type;
        this.pool = pool;
    }

    @Nullable
    public ModifierRollResult resolve(Random rand) {
        VaultCrystalCatalystConfig.TaggedPool pool = ModConfigs.VAULT_CRYSTAL_CATALYST.getPool(this.pool);
        if (pool != null) {
            if (this.type == ModifierRollType.ADD_SPECIFIC_MODIFIER) {
                return ModifierRollResult.ofModifier(pool.getModifier(rand));
            }
            return ModifierRollResult.ofPool(this.pool);
        }

        return null;
    }

    public ModifierRollType getType() {
        return this.type;
    }

    public String getPool() {
        return this.pool;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\catalyst\SingleModifierOutcome.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */