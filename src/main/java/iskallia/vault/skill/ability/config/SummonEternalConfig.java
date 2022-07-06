package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;

public class SummonEternalConfig extends AbilityConfig {
    @Expose
    private final int numberOfEternals;
    @Expose
    private final int summonedEternalsCap;
    @Expose
    private final int despawnTime;
    @Expose
    private final float ancientChance;
    @Expose
    private final boolean vaultOnly;

    public SummonEternalConfig(final int cost, final int cooldown, final int numberOfEternals, final int summonedEternalsCap, final int despawnTime, final float ancientChance, final boolean vaultOnly) {
        super(cost, Behavior.RELEASE_TO_PERFORM, cooldown);
        this.numberOfEternals = numberOfEternals;
        this.summonedEternalsCap = summonedEternalsCap;
        this.despawnTime = despawnTime;
        this.ancientChance = ancientChance;
        this.vaultOnly = vaultOnly;
    }

    public int getNumberOfEternals() {
        return this.numberOfEternals;
    }

    public int getSummonedEternalsCap() {
        return this.summonedEternalsCap;
    }

    public int getDespawnTime() {
        return this.despawnTime;
    }

    public float getAncientChance() {
        return this.ancientChance;
    }

    public boolean isVaultOnly() {
        return this.vaultOnly;
    }
}
