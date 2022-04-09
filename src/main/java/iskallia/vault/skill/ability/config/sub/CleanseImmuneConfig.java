// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.CleanseConfig;

public class CleanseImmuneConfig extends CleanseConfig
{
    @Expose
    private final int immunityDuration;
    
    public CleanseImmuneConfig(final int learningCost, final Behavior behavior, final int cooldown, final int duration) {
        super(learningCost, behavior, cooldown);
        this.immunityDuration = duration;
    }
    
    public int getImmunityDuration() {
        return this.immunityDuration;
    }
}
