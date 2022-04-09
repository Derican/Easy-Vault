// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.VeinMinerConfig;

public class VeinMinerFortuneConfig extends VeinMinerConfig
{
    @Expose
    private final int additionalFortuneLevel;
    
    public VeinMinerFortuneConfig(final int cost, final int blockLimit, final int additionalFortuneLevel) {
        super(cost, blockLimit);
        this.additionalFortuneLevel = additionalFortuneLevel;
    }
    
    public int getAdditionalFortuneLevel() {
        return this.additionalFortuneLevel;
    }
}
