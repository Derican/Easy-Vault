package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModEffects;

public class TankConfig
        extends EffectConfig {
    @Expose
    private final int durationTicks;

    public TankConfig(int cost, int durationTicks, float damageReductionPercent) {
        super(cost, ModEffects.TANK, 0, EffectConfig.Type.ICON_ONLY, AbilityConfig.Behavior.RELEASE_TO_PERFORM);
        this.durationTicks = durationTicks;
        this.damageReductionPercent = damageReductionPercent;
    }

    @Expose
    private final float damageReductionPercent;

    public int getDurationTicks() {
        return this.durationTicks;
    }

    public float getDamageReductionPercent() {
        return this.damageReductionPercent;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\TankConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */