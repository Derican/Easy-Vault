package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class BloodSet extends PlayerSet {
    public static int MULTIPLIER_ID;
    @Expose
    private final float damageMultiplier;

    public BloodSet(final float damageMultiplier) {
        super(VaultGear.Set.BLOOD);
        this.damageMultiplier = damageMultiplier;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    static {
        BloodSet.MULTIPLIER_ID = -2;
    }
}
