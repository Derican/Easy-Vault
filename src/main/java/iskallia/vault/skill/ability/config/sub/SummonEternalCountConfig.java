package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.SummonEternalConfig;

public class SummonEternalCountConfig extends SummonEternalConfig {
    @Expose
    private final int additionalCount;

    public SummonEternalCountConfig(final int cost, final int cooldown, final int numberOfEternals, final int summonedEternalsCap, final int despawnTime, final boolean vaultOnly, final float ancientChance, final int additionalCount) {
        super(cost, cooldown, numberOfEternals, summonedEternalsCap, despawnTime, ancientChance, vaultOnly);
        this.additionalCount = additionalCount;
    }

    public int getAdditionalCount() {
        return this.additionalCount;
    }
}
