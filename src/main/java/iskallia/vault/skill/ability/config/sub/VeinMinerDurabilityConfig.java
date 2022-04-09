// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.VeinMinerConfig;

public class VeinMinerDurabilityConfig extends VeinMinerConfig
{
    @Expose
    private final float noDurabilityUsageChance;
    
    public VeinMinerDurabilityConfig(final int cost, final int blockLimit, final float noDurabilityUsageChance) {
        super(cost, blockLimit);
        this.noDurabilityUsageChance = noDurabilityUsageChance;
    }
    
    public float getNoDurabilityUsageChance() {
        return this.noDurabilityUsageChance;
    }
}
