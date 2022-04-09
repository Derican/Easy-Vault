// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.aura.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.EternalAuraConfig;

public class ParryAuraConfig extends EternalAuraConfig.AuraConfig
{
    @Expose
    private final float additionalParryChance;
    
    public ParryAuraConfig(final float additionalParryChance) {
        super("Parry", "Parry", "Players in aura have +" + ParryAuraConfig.ROUNDING_FORMAT.format(additionalParryChance * 100.0f) + "% Parry", "parry", 5.0f);
        this.additionalParryChance = additionalParryChance;
    }
    
    public float getAdditionalParryChance() {
        return this.additionalParryChance;
    }
}
