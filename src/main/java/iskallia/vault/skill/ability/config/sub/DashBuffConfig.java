// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.DashConfig;

public class DashBuffConfig extends DashConfig
{
    @Expose
    private final float damageIncreasePerDash;
    @Expose
    private final int damageIncreaseTickTime;
    
    public DashBuffConfig(final int cost, final int extraRadius, final float damageIncreasePerDash, final int damageIncreaseTickTime) {
        super(cost, extraRadius);
        this.damageIncreasePerDash = damageIncreasePerDash;
        this.damageIncreaseTickTime = damageIncreaseTickTime;
    }
    
    public float getDamageIncreasePerDash() {
        return this.damageIncreasePerDash;
    }
    
    public int getDamageIncreaseTickTime() {
        return this.damageIncreaseTickTime;
    }
}
