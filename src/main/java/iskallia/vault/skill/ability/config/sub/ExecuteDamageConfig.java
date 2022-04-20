package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.ExecuteConfig;

public class ExecuteDamageConfig extends ExecuteConfig {
    @Expose
    private final float damagePercentMissingHealth;

    public ExecuteDamageConfig(final int cost, final Behavior behavior, final int effectDuration, final float damagePercentMissingHealth) {
        super(cost, behavior, 0.0f, effectDuration);
        this.damagePercentMissingHealth = damagePercentMissingHealth;
    }

    public float getDamagePercentPerMissingHealthPercent() {
        return this.damagePercentMissingHealth;
    }
}
