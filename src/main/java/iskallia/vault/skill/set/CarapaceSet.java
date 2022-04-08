package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class CarapaceSet extends PlayerSet {
    @Expose
    private final float absorptionPercent;

    public CarapaceSet(float absorptionPercent) {
        super(VaultGear.Set.CARAPACE);
        this.absorptionPercent = absorptionPercent;
    }

    public float getAbsorptionPercent() {
        return this.absorptionPercent;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\CarapaceSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */