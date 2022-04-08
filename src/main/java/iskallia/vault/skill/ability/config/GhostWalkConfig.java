package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModEffects;

public class GhostWalkConfig extends EffectConfig {
    @Expose
    private final int durationTicks;

    public GhostWalkConfig(int cost, int level, int durationTicks) {
        super(cost, ModEffects.GHOST_WALK, level, EffectConfig.Type.ICON_ONLY, AbilityConfig.Behavior.RELEASE_TO_PERFORM);
        this.durationTicks = durationTicks;
    }

    public int getDurationTicks() {
        return this.durationTicks;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\GhostWalkConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */