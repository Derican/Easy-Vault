package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class AbsorptionTalent extends PlayerTalent {
    @Expose
    private final float increasedAbsorptionLimit;

    public AbsorptionTalent(final int cost, final float increasedAbsorptionLimit) {
        super(cost);
        this.increasedAbsorptionLimit = increasedAbsorptionLimit;
    }

    public float getIncreasedAbsorptionLimit() {
        return this.increasedAbsorptionLimit;
    }
}
