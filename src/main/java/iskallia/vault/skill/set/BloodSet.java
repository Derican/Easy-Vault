package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class BloodSet
        extends PlayerSet {
    public static int MULTIPLIER_ID = -2;
    @Expose
    private float damageMultiplier;

    public BloodSet(float damageMultiplier) {
        super(VaultGear.Set.BLOOD);
        this.damageMultiplier = damageMultiplier;
    }


    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\BloodSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */