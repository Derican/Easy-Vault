package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModEffects;

public class RampageConfig
        extends EffectConfig {
    @Expose
    private final int durationTicks;

    public RampageConfig(int cost, float damageIncrease, int durationTicks, int cooldown) {
        super(cost, ModEffects.RAMPAGE, 0, EffectConfig.Type.ICON_ONLY, AbilityConfig.Behavior.RELEASE_TO_PERFORM, cooldown);
        this.damageIncrease = damageIncrease;
        this.durationTicks = durationTicks;
    }

    @Expose
    private final float damageIncrease;

    public int getDurationTicks() {
        return this.durationTicks;
    }

    public float getDamageIncrease() {
        return this.damageIncrease;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\RampageConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */