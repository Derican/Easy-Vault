package iskallia.vault.aura.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.EternalAuraConfig;

public class ResistanceAuraConfig extends EternalAuraConfig.AuraConfig {
    @Expose
    private final float additionalResistance;

    public ResistanceAuraConfig(float additionalResistance) {
        super("Resistance", "Resistance", "Players in aura have +" + ROUNDING_FORMAT.format((additionalResistance * 100.0F)) + "% Resistance", "resistance", 5.0F);
        this.additionalResistance = additionalResistance;
    }

    public float getAdditionalResistance() {
        return this.additionalResistance;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\aura\type\ResistanceAuraConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */