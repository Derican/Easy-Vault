package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.DashConfig;

public class DashBuffConfig
        extends DashConfig {
    @Expose
    private final float damageIncreasePerDash;

    public DashBuffConfig(int cost, int extraRadius, float damageIncreasePerDash, int damageIncreaseTickTime) {
        super(cost, extraRadius);
        this.damageIncreasePerDash = damageIncreasePerDash;
        this.damageIncreaseTickTime = damageIncreaseTickTime;
    }

    @Expose
    private final int damageIncreaseTickTime;

    public float getDamageIncreasePerDash() {
        return this.damageIncreasePerDash;
    }

    public int getDamageIncreaseTickTime() {
        return this.damageIncreaseTickTime;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\DashBuffConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */