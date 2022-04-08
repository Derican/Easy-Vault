package iskallia.vault.skill.talent.type.archetype;

import com.google.gson.annotations.Expose;

public class BarbaricTalent
        extends ArchetypeTalent {
    @Expose
    protected int rageDegenTickDelay;

    public BarbaricTalent(int cost, int rageDegenTickDelay, float damageMultiplierPerRage, int ragePerAttack) {
        super(cost);
        this.rageDegenTickDelay = rageDegenTickDelay;
        this.damageMultiplierPerRage = damageMultiplierPerRage;
        this.ragePerAttack = ragePerAttack;
    }

    @Expose
    protected float damageMultiplierPerRage;
    @Expose
    protected int ragePerAttack;

    public int getRageDegenTickDelay() {
        return this.rageDegenTickDelay;
    }

    public float getDamageMultiplierPerRage() {
        return this.damageMultiplierPerRage;
    }

    public int getRagePerAttack() {
        return this.ragePerAttack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\archetype\BarbaricTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */