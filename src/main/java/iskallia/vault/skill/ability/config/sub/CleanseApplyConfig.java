// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.CleanseConfig;

public class CleanseApplyConfig extends CleanseConfig
{
    @Expose
    private final int applyRadius;
    
    public CleanseApplyConfig(final int learningCost, final Behavior behavior, final int cooldown, final int applyRadius) {
        super(learningCost, behavior, cooldown);
        this.applyRadius = applyRadius;
    }
    
    public int getApplyRadius() {
        return this.applyRadius;
    }
}
