package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class AssassinSet extends PlayerSet {
    @Expose
    private final float increasedFatalStrikeChance;

    public AssassinSet(final float increasedFatalStrikeChance) {
        super(VaultGear.Set.ASSASSIN);
        this.increasedFatalStrikeChance = increasedFatalStrikeChance;
    }

    public float getIncreasedFatalStrikeChance() {
        return this.increasedFatalStrikeChance;
    }
}
