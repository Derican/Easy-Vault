package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class ThornsChanceTalent extends PlayerTalent {
    @Expose
    private final float additionalThornsChance;

    public ThornsChanceTalent(int cost, float additionalThornsChance) {
        super(cost);
        this.additionalThornsChance = additionalThornsChance;
    }

    public float getAdditionalThornsChance() {
        return this.additionalThornsChance;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\ThornsChanceTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */