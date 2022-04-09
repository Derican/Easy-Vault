// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.CleanseConfig;

public class CleanseHealConfig extends CleanseConfig
{
    @Expose
    private final float healthPerEffectRemoved;
    
    public CleanseHealConfig(final int learningCost, final Behavior behavior, final int cooldown, final float healthPerEffectRemoved) {
        super(learningCost, behavior, cooldown);
        this.healthPerEffectRemoved = healthPerEffectRemoved;
    }
    
    public float getHealthPerEffectRemoved() {
        return this.healthPerEffectRemoved;
    }
}
