package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.GhostWalkConfig;

public class GhostWalkParryConfig extends GhostWalkConfig {
    @Expose
    private final float additionalParryChance;

    public GhostWalkParryConfig(int cost, int level, int durationTicks, float additionalParryChance) {
        super(cost, level, durationTicks);
        this.additionalParryChance = additionalParryChance;
    }

    public float getAdditionalParryChance() {
        return this.additionalParryChance;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\GhostWalkParryConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */