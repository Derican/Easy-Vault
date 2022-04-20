package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.SummonEternalConfig;

public class SummonEternalDamageConfig extends SummonEternalConfig {
    @Expose
    private final float increasedDamagePercent;

    public SummonEternalDamageConfig(final int cost, final int cooldown, final int numberOfEternals, final int despawnTime, final boolean vaultOnly, final float ancientChance, final float increasedDamagePercent) {
        super(cost, cooldown, numberOfEternals, despawnTime, ancientChance, vaultOnly);
        this.increasedDamagePercent = increasedDamagePercent;
    }

    public float getIncreasedDamagePercent() {
        return this.increasedDamagePercent;
    }
}
