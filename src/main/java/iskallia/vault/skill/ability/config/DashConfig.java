// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;

public class DashConfig extends AbilityConfig
{
    @Expose
    private final int extraRadius;
    
    public DashConfig(final int cost, final int extraRadius) {
        super(cost, Behavior.RELEASE_TO_PERFORM);
        this.extraRadius = extraRadius;
    }
    
    public int getExtraRadius() {
        return this.extraRadius;
    }
}
