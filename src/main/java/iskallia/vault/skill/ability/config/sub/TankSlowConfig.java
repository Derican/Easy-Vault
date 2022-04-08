package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.TankConfig;

public class TankSlowConfig
        extends TankConfig {
    @Expose
    private final float slowAreaRadius;

    public TankSlowConfig(int cost, int durationTicks, float damageReductionPercent, float slowAreaRadius, int slownessAmplifier) {
        super(cost, durationTicks, damageReductionPercent);
        this.slowAreaRadius = slowAreaRadius;
        this.slownessAmplifier = slownessAmplifier;
    }

    @Expose
    private final int slownessAmplifier;

    public float getSlowAreaRadius() {
        return this.slowAreaRadius;
    }

    public int getSlownessAmplifier() {
        return this.slownessAmplifier;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\TankSlowConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */