package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class DragonSet
        extends PlayerSet {
    public static int MULTIPLIER_ID = -1;
    @Expose
    private float damageMultiplier;

    public DragonSet(float damageMultiplier) {
        super(VaultGear.Set.DRAGON);
        this.damageMultiplier = damageMultiplier;
    }


    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\DragonSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */