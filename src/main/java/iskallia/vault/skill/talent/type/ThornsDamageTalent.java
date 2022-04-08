package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class ThornsDamageTalent extends PlayerTalent {
    @Expose
    private final float additionalThornsDamage;

    public ThornsDamageTalent(int cost, float additionalThornsDamage) {
        super(cost);
        this.additionalThornsDamage = additionalThornsDamage;
    }

    public float getAdditionalThornsDamage() {
        return this.additionalThornsDamage;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\ThornsDamageTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */