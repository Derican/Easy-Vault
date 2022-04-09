// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;

public class AbilityConfig
{
    @Expose
    private final int learningCost;
    @Expose
    private final Behavior behavior;
    @Expose
    private final int cooldown;
    @Expose
    private final int levelRequirement;
    
    public AbilityConfig(final int learningCost, final Behavior behavior) {
        this(learningCost, behavior, 200);
    }
    
    public AbilityConfig(final int learningCost, final Behavior behavior, final int cooldown) {
        this(learningCost, behavior, cooldown, 0);
    }
    
    public AbilityConfig(final int learningCost, final Behavior behavior, final int cooldown, final int levelRequirement) {
        this.learningCost = learningCost;
        this.behavior = behavior;
        this.cooldown = cooldown;
        this.levelRequirement = levelRequirement;
    }
    
    public int getLearningCost() {
        return this.learningCost;
    }
    
    public Behavior getBehavior() {
        return this.behavior;
    }
    
    public int getCooldown() {
        return this.cooldown;
    }
    
    public int getLevelRequirement() {
        return this.levelRequirement;
    }
    
    public enum Behavior
    {
        HOLD_TO_ACTIVATE, 
        PRESS_TO_TOGGLE, 
        RELEASE_TO_PERFORM;
    }
}
