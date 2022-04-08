package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;

public class SummonEternalConfig
        extends AbilityConfig {
    @Expose
    private final int numberOfEternals;
    @Expose
    private final int despawnTime;

    public SummonEternalConfig(int cost, int cooldown, int numberOfEternals, int despawnTime, float ancientChance, boolean vaultOnly) {
        super(cost, AbilityConfig.Behavior.RELEASE_TO_PERFORM, cooldown);
        this.numberOfEternals = numberOfEternals;
        this.despawnTime = despawnTime;
        this.ancientChance = ancientChance;
        this.vaultOnly = vaultOnly;
    }

    @Expose
    private final float ancientChance;
    @Expose
    private final boolean vaultOnly;

    public int getNumberOfEternals() {
        return this.numberOfEternals;
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\SummonEternalConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */