package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;

public class DashConfig extends AbilityConfig {
    @Expose
    private final int extraRadius;

    public DashConfig(int cost, int extraRadius) {
        super(cost, AbilityConfig.Behavior.RELEASE_TO_PERFORM);
        this.extraRadius = extraRadius;
    }

    public int getExtraRadius() {
        return this.extraRadius;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\DashConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */