// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;

public class VeinMinerConfig extends AbilityConfig
{
    @Expose
    private final int blockLimit;
    
    public VeinMinerConfig(final int cost, final int blockLimit) {
        super(cost, Behavior.HOLD_TO_ACTIVATE);
        this.blockLimit = blockLimit;
    }
    
    public int getBlockLimit() {
        return this.blockLimit;
    }
}
