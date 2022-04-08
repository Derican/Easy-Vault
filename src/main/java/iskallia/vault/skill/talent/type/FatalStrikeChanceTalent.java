package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class FatalStrikeChanceTalent extends PlayerTalent {
    @Expose
    private final float additionalFatalStrikeChance;

    public FatalStrikeChanceTalent(int cost, float additionalFatalStrikeChance) {
        super(cost);
        this.additionalFatalStrikeChance = additionalFatalStrikeChance;
    }

    public float getAdditionalFatalStrikeChance() {
        return this.additionalFatalStrikeChance;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\FatalStrikeChanceTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */