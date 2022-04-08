package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.TankConfig;

public class TankReflectConfig
        extends TankConfig {
    @Expose
    private final float damageReflectChance;

    public TankReflectConfig(int cost, int durationTicks, float damageReductionPercent, float damageReflectChance, float damageReflectPercent) {
        super(cost, durationTicks, damageReductionPercent);
        this.damageReflectChance = damageReflectChance;
        this.damageReflectPercent = damageReflectPercent;
    }

    @Expose
    private final float damageReflectPercent;

    public float getDamageReflectChance() {
        return this.damageReflectChance;
    }

    public float getDamageReflectPercent() {
        return this.damageReflectPercent;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\TankReflectConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */