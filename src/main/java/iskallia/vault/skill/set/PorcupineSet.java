package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class PorcupineSet
        extends PlayerSet {
    @Expose
    protected float additionalThornsChance;

    public PorcupineSet(float additionalThornsChance, float additionalThornsDamage) {
        super(VaultGear.Set.PORCUPINE);
        this.additionalThornsChance = additionalThornsChance;
        this.additionalThornsDamage = additionalThornsDamage;
    }

    @Expose
    protected float additionalThornsDamage;

    public float getAdditionalThornsChance() {
        return this.additionalThornsChance;
    }

    public float getAdditionalThornsDamage() {
        return this.additionalThornsDamage;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\PorcupineSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */