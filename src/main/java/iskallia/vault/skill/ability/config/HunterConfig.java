// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;

public class HunterConfig extends AbilityConfig
{
    @Expose
    private final double searchRadius;
    @Expose
    private final int color;
    @Expose
    private final int tickDuration;
    
    public HunterConfig(final int learningCost, final double searchRadius, final int color, final int tickDuration) {
        super(learningCost, Behavior.RELEASE_TO_PERFORM);
        this.searchRadius = searchRadius;
        this.color = color;
        this.tickDuration = tickDuration;
    }
    
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
