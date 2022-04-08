package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.SummonEternalConfig;

public class SummonEternalCountConfig extends SummonEternalConfig {
    @Expose
    private final int additionalCount;

    public SummonEternalCountConfig(int cost, int cooldown, int numberOfEternals, int despawnTime, boolean vaultOnly, float ancientChance, int additionalCount) {
        super(cost, cooldown, numberOfEternals, despawnTime, ancientChance, vaultOnly);
        this.additionalCount = additionalCount;
    }

    public int getAdditionalCount() {
        return this.additionalCount;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\SummonEternalCountConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */