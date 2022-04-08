package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.CleanseConfig;

public class CleanseImmuneConfig
        extends CleanseConfig {
    public CleanseImmuneConfig(int learningCost, AbilityConfig.Behavior behavior, int cooldown, int duration) {
        super(learningCost, behavior, cooldown);
        this.immunityDuration = duration;
    }

    @Expose
    private final int immunityDuration;

    public int getImmunityDuration() {
        return this.immunityDuration;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\CleanseImmuneConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */