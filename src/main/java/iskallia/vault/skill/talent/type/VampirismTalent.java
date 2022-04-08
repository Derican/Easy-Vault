package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;


public class VampirismTalent
        extends PlayerTalent {
    @Expose
    private final float leechRatio;

    public VampirismTalent(int cost, float leechRatio) {
        super(cost);
        this.leechRatio = leechRatio;
    }

    public float getLeechRatio() {
        return this.leechRatio;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\VampirismTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */