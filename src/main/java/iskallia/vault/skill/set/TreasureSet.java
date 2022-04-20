package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class TreasureSet extends PlayerSet {
    @Expose
    private float increasedChestRarity;

    public TreasureSet(final float increasedChestRarity) {
        super(VaultGear.Set.TREASURE);
        this.increasedChestRarity = increasedChestRarity;
    }

    public float getIncreasedChestRarity() {
        return this.increasedChestRarity;
    }
}
