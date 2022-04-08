package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.SummonEternalConfig;


public class SummonEternalDebuffConfig
        extends SummonEternalConfig {
    @Expose
    private final float applyDebuffChance;

    public SummonEternalDebuffConfig(int cost, int cooldown, int numberOfEternals, int despawnTime, boolean vaultOnly, float ancientChance, float applyDebuffChance, int debuffDurationTicks, int debuffAmplifier) {
        super(cost, cooldown, numberOfEternals, despawnTime, ancientChance, vaultOnly);
        this.applyDebuffChance = applyDebuffChance;
        this.debuffDurationTicks = debuffDurationTicks;
        this.debuffAmplifier = debuffAmplifier;
    }

    @Expose
    private final int debuffDurationTicks;
    @Expose
    private final int debuffAmplifier;

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\SummonEternalDebuffConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */