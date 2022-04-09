// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.DashConfig;

public class DashHealConfig extends DashConfig
{
    @Expose
    private final float healPerDash;
    
    public DashHealConfig(final int cost, final int extraRadius, final float healPerDash) {
        super(cost, extraRadius);
        this.healPerDash = healPerDash;
    }
    
    public float getHealPerDash() {
        return this.healPerDash;
    }
}
