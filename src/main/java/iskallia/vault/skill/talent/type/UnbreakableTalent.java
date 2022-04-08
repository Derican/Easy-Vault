package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class UnbreakableTalent extends PlayerTalent {
    @Expose
    private final float extraUnbreaking;

    public UnbreakableTalent(int cost, int extraUnbreaking) {
        super(cost);
        this.extraUnbreaking = extraUnbreaking;
    }

    public float getExtraUnbreaking() {
        return this.extraUnbreaking;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\UnbreakableTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */