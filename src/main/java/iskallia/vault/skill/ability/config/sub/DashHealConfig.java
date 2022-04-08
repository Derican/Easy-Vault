package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.DashConfig;

public class DashHealConfig extends DashConfig {
    @Expose
    private final float healPerDash;

    public DashHealConfig(int cost, int extraRadius, float healPerDash) {
        super(cost, extraRadius);
        this.healPerDash = healPerDash;
    }

    public float getHealPerDash() {
        return this.healPerDash;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\DashHealConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */