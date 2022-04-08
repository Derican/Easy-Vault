package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class FatalStrikeDamageTalent extends PlayerTalent {
    @Expose
    private final float additionalFatalStrikeDamage;

    public FatalStrikeDamageTalent(int cost, float additionalFatalStrikeDamage) {
        super(cost);
        this.additionalFatalStrikeDamage = additionalFatalStrikeDamage;
    }

    public float getAdditionalFatalStrikeDamage() {
        return this.additionalFatalStrikeDamage;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\FatalStrikeDamageTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */