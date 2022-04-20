package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.ExecuteConfig;

public class ExecuteBuffConfig extends ExecuteConfig {
    @Expose
    private final float regainBuffChance;

    public ExecuteBuffConfig(final int cost, final Behavior behavior, final float healthPercentage, final int effectDuration, final float regainBuffChance) {
        super(cost, behavior, healthPercentage, effectDuration);
        this.regainBuffChance = regainBuffChance;
    }

    public float getRegainBuffChance() {
        return this.regainBuffChance;
    }
}
