package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModEffects;

public class ExecuteConfig extends EffectConfig {
    @Expose
    private final float healthPercentage;
    @Expose
    private final int effectDuration;

    public ExecuteConfig(int cost, AbilityConfig.Behavior behavior, float healthPercentage, int effectDuration) {
        super(cost, ModEffects.EXECUTE, 0, EffectConfig.Type.ICON_ONLY, behavior);
        this.healthPercentage = healthPercentage;
        this.effectDuration = effectDuration;
    }

    public float getHealthPercentage() {
        return this.healthPercentage;
    }

    public int getEffectDuration() {
        return this.effectDuration;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\ExecuteConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */