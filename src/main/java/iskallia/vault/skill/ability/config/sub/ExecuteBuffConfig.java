package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.ExecuteConfig;

public class ExecuteBuffConfig
        extends ExecuteConfig {
    public ExecuteBuffConfig(int cost, AbilityConfig.Behavior behavior, float healthPercentage, int effectDuration, float regainBuffChance) {
        super(cost, behavior, healthPercentage, effectDuration);
        this.regainBuffChance = regainBuffChance;
    }

    @Expose
    private final float regainBuffChance;

    public float getRegainBuffChance() {
        return this.regainBuffChance;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\ExecuteBuffConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */