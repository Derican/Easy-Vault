package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class GolemSet
        extends PlayerSet {
    @Expose
    private final float bonusResistance;

    public GolemSet(float bonusResistance, float bonusResistanceCap) {
        super(VaultGear.Set.GOLEM);
        this.bonusResistance = bonusResistance;
        this.bonusResistanceCap = bonusResistanceCap;
    }

    @Expose
    private final float bonusResistanceCap;

    public float getBonusResistance() {
        return this.bonusResistance;
    }

    public float getBonusResistanceCap() {
        return this.bonusResistanceCap;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\GolemSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */