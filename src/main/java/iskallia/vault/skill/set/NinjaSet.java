package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class NinjaSet
        extends PlayerSet {
    @Expose
    private float bonusParry;

    public NinjaSet(float bonusParry, float bonusParryCap) {
        super(VaultGear.Set.NINJA);
        this.bonusParry = bonusParry;
        this.bonusParryCap = bonusParryCap;
    }

    @Expose
    private float bonusParryCap;

    public float getBonusParry() {
        return this.bonusParry;
    }

    public float getBonusParryCap() {
        return this.bonusParryCap;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\NinjaSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */