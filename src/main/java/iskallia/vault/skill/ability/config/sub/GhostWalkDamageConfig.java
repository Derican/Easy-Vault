package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.GhostWalkConfig;

public class GhostWalkDamageConfig extends GhostWalkConfig {
    @Expose
    private final float damageMultiplierInGhostWalk;

    public GhostWalkDamageConfig(int cost, int level, int durationTicks, float damageMultiplierInGhostWalk) {
        super(cost, level, durationTicks);
        this.damageMultiplierInGhostWalk = damageMultiplierInGhostWalk;
    }

    public float getDamageMultiplierInGhostWalk() {
        return this.damageMultiplierInGhostWalk;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\GhostWalkDamageConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */