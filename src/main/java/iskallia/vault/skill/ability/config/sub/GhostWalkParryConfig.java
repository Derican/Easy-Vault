// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.GhostWalkConfig;

public class GhostWalkParryConfig extends GhostWalkConfig
{
    @Expose
    private final float additionalParryChance;
    
    public GhostWalkParryConfig(final int cost, final int level, final int durationTicks, final float additionalParryChance) {
        super(cost, level, durationTicks);
        this.additionalParryChance = additionalParryChance;
    }
    
    public float getAdditionalParryChance() {
        return this.additionalParryChance;
    }
}
