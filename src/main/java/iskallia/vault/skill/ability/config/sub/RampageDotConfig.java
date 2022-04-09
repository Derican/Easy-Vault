// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.RampageConfig;

public class RampageDotConfig extends RampageConfig
{
    @Expose
    private final int dotSecondDuration;
    
    public RampageDotConfig(final int cost, final int damageIncrease, final int durationTicks, final int cooldown, final int dotSecondDuration) {
        super(cost, (float)damageIncrease, durationTicks, cooldown);
        this.dotSecondDuration = dotSecondDuration;
    }
    
    public int getDotSecondDuration() {
        return this.dotSecondDuration;
    }
}
