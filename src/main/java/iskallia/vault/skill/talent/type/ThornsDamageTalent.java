// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class ThornsDamageTalent extends PlayerTalent
{
    @Expose
    private final float additionalThornsDamage;
    
    public ThornsDamageTalent(final int cost, final float additionalThornsDamage) {
        super(cost);
        this.additionalThornsDamage = additionalThornsDamage;
    }
    
    public float getAdditionalThornsDamage() {
        return this.additionalThornsDamage;
    }
}
