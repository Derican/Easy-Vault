package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.SummonEternalConfig;

public class SummonEternalDebuffConfig extends SummonEternalConfig {
    @Expose
    private final float applyDebuffChance;
    @Expose
    private final int debuffDurationTicks;
    @Expose
    private final int debuffAmplifier;

    public SummonEternalDebuffConfig(final int cost, final int cooldown, final int numberOfEternals, final int despawnTime, final boolean vaultOnly, final float ancientChance, final float applyDebuffChance, final int debuffDurationTicks, final int debuffAmplifier) {
        super(cost, cooldown, numberOfEternals, despawnTime, ancientChance, vaultOnly);
        this.applyDebuffChance = applyDebuffChance;
        this.debuffDurationTicks = debuffDurationTicks;
        this.debuffAmplifier = debuffAmplifier;
    }

    public float getApplyDebuffChance() {
        return this.applyDebuffChance;
    }

    public int getDebuffDurationTicks() {
        return this.debuffDurationTicks;
    }

    public int getDebuffAmplifier() {
        return this.debuffAmplifier;
    }
}
