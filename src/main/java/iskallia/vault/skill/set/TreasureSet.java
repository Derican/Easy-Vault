package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class TreasureSet extends PlayerSet {
    @Expose
    private float increasedChestRarity;

    public TreasureSet(float increasedChestRarity) {
        super(VaultGear.Set.TREASURE);
        this.increasedChestRarity = increasedChestRarity;
    }

    public float getIncreasedChestRarity() {
        return this.increasedChestRarity;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\TreasureSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */