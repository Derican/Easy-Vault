package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class AbsorptionTalent extends PlayerTalent {
    @Expose
    private float increasedAbsorptionLimit;

    public AbsorptionTalent(int cost, float increasedAbsorptionLimit) {
        super(cost);
        this.increasedAbsorptionLimit = increasedAbsorptionLimit;
    }

    public float getIncreasedAbsorptionLimit() {
        return this.increasedAbsorptionLimit;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\AbsorptionTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */