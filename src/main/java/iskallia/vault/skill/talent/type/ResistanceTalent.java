package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;


public class ResistanceTalent
        extends PlayerTalent {
    @Expose
    protected float additionalResistanceLimit;

    public ResistanceTalent(int cost, float additionalResistanceLimit) {
        super(cost);
        this.additionalResistanceLimit = additionalResistanceLimit;
    }

    public float getAdditionalResistanceLimit() {
        return this.additionalResistanceLimit;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\ResistanceTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */