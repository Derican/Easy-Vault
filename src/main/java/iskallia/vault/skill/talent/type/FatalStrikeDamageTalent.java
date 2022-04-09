// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class FatalStrikeDamageTalent extends PlayerTalent
{
    @Expose
    private final float additionalFatalStrikeDamage;
    
    public FatalStrikeDamageTalent(final int cost, final float additionalFatalStrikeDamage) {
        super(cost);
        this.additionalFatalStrikeDamage = additionalFatalStrikeDamage;
    }
    
    public float getAdditionalFatalStrikeDamage() {
        return this.additionalFatalStrikeDamage;
    }
}
