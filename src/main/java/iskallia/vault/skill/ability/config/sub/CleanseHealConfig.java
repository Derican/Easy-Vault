package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.CleanseConfig;

public class CleanseHealConfig
        extends CleanseConfig {
    public CleanseHealConfig(int learningCost, AbilityConfig.Behavior behavior, int cooldown, float healthPerEffectRemoved) {
        super(learningCost, behavior, cooldown);
        this.healthPerEffectRemoved = healthPerEffectRemoved;
    }

    @Expose
    private final float healthPerEffectRemoved;

    public float getHealthPerEffectRemoved() {
        return this.healthPerEffectRemoved;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\CleanseHealConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */