package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.RampageConfig;

public class RampageDotConfig extends RampageConfig {
    @Expose
    private final int dotSecondDuration;

    public RampageDotConfig(int cost, int damageIncrease, int durationTicks, int cooldown, int dotSecondDuration) {
        super(cost, damageIncrease, durationTicks, cooldown);
        this.dotSecondDuration = dotSecondDuration;
    }

    public int getDotSecondDuration() {
        return this.dotSecondDuration;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\RampageDotConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */