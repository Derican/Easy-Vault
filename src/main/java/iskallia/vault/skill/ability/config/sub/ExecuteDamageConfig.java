package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.ExecuteConfig;

public class ExecuteDamageConfig
        extends ExecuteConfig {
    public ExecuteDamageConfig(int cost, AbilityConfig.Behavior behavior, int effectDuration, float damagePercentMissingHealth) {
        super(cost, behavior, 0.0F, effectDuration);
        this.damagePercentMissingHealth = damagePercentMissingHealth;
    }

    @Expose
    private final float damagePercentMissingHealth;

    public float getDamagePercentPerMissingHealthPercent() {
        return this.damagePercentMissingHealth;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\ExecuteDamageConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */