// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;

public class MegaJumpConfig extends AbilityConfig
{
    @Expose
    private final int height;
    
    public MegaJumpConfig(final int cost, final int height) {
        super(cost, Behavior.RELEASE_TO_PERFORM);
        this.height = height;
    }
    
    public int getHeight() {
        return this.height;
    }
}
