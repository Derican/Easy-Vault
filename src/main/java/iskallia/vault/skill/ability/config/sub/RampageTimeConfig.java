package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.RampageConfig;

public class RampageTimeConfig extends RampageConfig {
    @Expose
    private final int tickTimeIncreasePerHit;

    public RampageTimeConfig(int cost, int damageIncrease, int durationTicks, int cooldown, int tickTimeIncreasePerHit) {
        super(cost, damageIncrease, durationTicks, cooldown);
        this.tickTimeIncreasePerHit = tickTimeIncreasePerHit;
    }

    public int getTickTimeIncreasePerHit() {
        return this.tickTimeIncreasePerHit;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\RampageTimeConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */