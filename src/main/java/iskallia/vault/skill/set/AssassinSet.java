package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class AssassinSet
        extends PlayerSet {
    @Expose
    private float increasedFatalStrikeChance;

    public AssassinSet(float increasedFatalStrikeChance) {
        super(VaultGear.Set.ASSASSIN);
        this.increasedFatalStrikeChance = increasedFatalStrikeChance;
    }

    public float getIncreasedFatalStrikeChance() {
        return this.increasedFatalStrikeChance;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\AssassinSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */