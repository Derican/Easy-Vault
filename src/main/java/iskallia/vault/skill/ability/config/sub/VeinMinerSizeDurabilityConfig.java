// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.VeinMinerConfig;

public class VeinMinerSizeDurabilityConfig extends VeinMinerConfig
{
    @Expose
    private final float doubleDurabilityCostChance;
    
    public VeinMinerSizeDurabilityConfig(final int cost, final int blockLimit, final float doubleDurabilityCostChance) {
        super(cost, blockLimit);
        this.doubleDurabilityCostChance = doubleDurabilityCostChance;
    }
    
    public float getDoubleDurabilityCostChance() {
        return this.doubleDurabilityCostChance;
    }
}
