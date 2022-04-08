package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.SummonEternalConfig;

public class SummonEternalDamageConfig extends SummonEternalConfig {
    @Expose
    private final float increasedDamagePercent;

    public SummonEternalDamageConfig(int cost, int cooldown, int numberOfEternals, int despawnTime, boolean vaultOnly, float ancientChance, float increasedDamagePercent) {
        super(cost, cooldown, numberOfEternals, despawnTime, ancientChance, vaultOnly);
        this.increasedDamagePercent = increasedDamagePercent;
    }

    public float getIncreasedDamagePercent() {
        return this.increasedDamagePercent;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\SummonEternalDamageConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */