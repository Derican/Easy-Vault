package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;

public class HunterConfig
        extends AbilityConfig {
    @Expose
    private final double searchRadius;

    public HunterConfig(int learningCost, double searchRadius, int color, int tickDuration) {
        super(learningCost, AbilityConfig.Behavior.RELEASE_TO_PERFORM);
        this.searchRadius = searchRadius;
        this.color = color;
        this.tickDuration = tickDuration;
    }

    @Expose
    private final int color;
    @Expose
    private final int tickDuration;

    public double getSearchRadius() {
        return this.searchRadius;
    }

    public int getColor() {
        return this.color;
    }

    public int getTickDuration() {
        return this.tickDuration;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\HunterConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */