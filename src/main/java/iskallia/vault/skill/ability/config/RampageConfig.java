package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModEffects;

public class RampageConfig extends EffectConfig {
    @Expose
    private final int durationTicks;
    @Expose
    private final float damageIncrease;

    public RampageConfig(final int cost, final float damageIncrease, final int durationTicks, final int cooldown) {
        super(cost, ModEffects.RAMPAGE, 0, Type.ICON_ONLY, Behavior.RELEASE_TO_PERFORM, cooldown);
        this.damageIncrease = damageIncrease;
        this.durationTicks = durationTicks;
    }

    public int getDurationTicks() {
        return this.durationTicks;
    }

    public float getDamageIncrease() {
        return this.damageIncrease;
    }
}
