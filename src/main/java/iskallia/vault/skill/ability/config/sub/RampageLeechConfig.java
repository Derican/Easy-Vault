// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.RampageConfig;

public class RampageLeechConfig extends RampageConfig
{
    @Expose
    private float leechPercent;
    
    public RampageLeechConfig(final int cost, final int durationTicks, final int cooldown, final float leechPercent) {
        super(cost, 0.0f, durationTicks, cooldown);
        this.leechPercent = leechPercent;
    }
    
    public float getLeechPercent() {
        return this.leechPercent;
    }
}
