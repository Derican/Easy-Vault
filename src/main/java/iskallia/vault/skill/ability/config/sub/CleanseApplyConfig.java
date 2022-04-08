package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.CleanseConfig;

public class CleanseApplyConfig
        extends CleanseConfig {
    public CleanseApplyConfig(int learningCost, AbilityConfig.Behavior behavior, int cooldown, int applyRadius) {
        super(learningCost, behavior, cooldown);
        this.applyRadius = applyRadius;
    }

    @Expose
    private final int applyRadius;

    public int getApplyRadius() {
        return this.applyRadius;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\CleanseApplyConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */