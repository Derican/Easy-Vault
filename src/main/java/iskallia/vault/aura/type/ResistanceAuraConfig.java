// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.aura.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.EternalAuraConfig;

public class ResistanceAuraConfig extends EternalAuraConfig.AuraConfig
{
    @Expose
    private final float additionalResistance;
    
    public ResistanceAuraConfig(final float additionalResistance) {
        super("Resistance", "Resistance", "Players in aura have +" + ResistanceAuraConfig.ROUNDING_FORMAT.format(additionalResistance * 100.0f) + "% Resistance", "resistance", 5.0f);
        this.additionalResistance = additionalResistance;
    }
    
    public float getAdditionalResistance() {
        return this.additionalResistance;
    }
}
