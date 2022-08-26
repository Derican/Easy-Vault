package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class NinjaSet extends PlayerSet {
    @Expose
    private final float bonusParry;
    @Expose
    private final float bonusParryCap;

    public NinjaSet(final float bonusParry, final float bonusParryCap) {
        super(VaultGear.Set.NINJA);
        this.bonusParry = bonusParry;
        this.bonusParryCap = bonusParryCap;
    }

    public float getBonusParry() {
        return this.bonusParry;
    }

    public float getBonusParryCap() {
        return this.bonusParryCap;
    }
}
