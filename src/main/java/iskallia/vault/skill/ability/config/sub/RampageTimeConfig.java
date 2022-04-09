// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.RampageConfig;

public class RampageTimeConfig extends RampageConfig
{
    @Expose
    private final int tickTimeIncreasePerHit;
    
    public RampageTimeConfig(final int cost, final int damageIncrease, final int durationTicks, final int cooldown, final int tickTimeIncreasePerHit) {
        super(cost, (float)damageIncrease, durationTicks, cooldown);
        this.tickTimeIncreasePerHit = tickTimeIncreasePerHit;
    }
    
    public int getTickTimeIncreasePerHit() {
        return this.tickTimeIncreasePerHit;
    }
}
