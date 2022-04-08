package iskallia.vault.aura.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.EternalAuraConfig;

public class ParryAuraConfig extends EternalAuraConfig.AuraConfig {
    @Expose
    private final float additionalParryChance;

    public ParryAuraConfig(float additionalParryChance) {
        super("Parry", "Parry", "Players in aura have +" + ROUNDING_FORMAT.format((additionalParryChance * 100.0F)) + "% Parry", "parry", 5.0F);
        this.additionalParryChance = additionalParryChance;
    }

    public float getAdditionalParryChance() {
        return this.additionalParryChance;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\aura\type\ParryAuraConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */