package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.TankConfig;

public class TankParryConfig extends TankConfig {
    @Expose
    private final float parryChance;

    public TankParryConfig(int cost, int durationTicks, float parryChance) {
        super(cost, durationTicks, 0.0F);
        this.parryChance = parryChance;
    }

    public float getParryChance() {
        return this.parryChance;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\TankParryConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */