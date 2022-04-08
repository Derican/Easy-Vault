package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.RampageConfig;

public class RampageLeechConfig extends RampageConfig {
    @Expose
    private float leechPercent;

    public RampageLeechConfig(int cost, int durationTicks, int cooldown, float leechPercent) {
        super(cost, 0.0F, durationTicks, cooldown);
        this.leechPercent = leechPercent;
    }

    public float getLeechPercent() {
        return this.leechPercent;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\RampageLeechConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */