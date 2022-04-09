// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class ResistanceTalent extends PlayerTalent
{
    @Expose
    protected float additionalResistanceLimit;
    
    public ResistanceTalent(final int cost, final float additionalResistanceLimit) {
        super(cost);
        this.additionalResistanceLimit = additionalResistanceLimit;
    }
    
    public float getAdditionalResistanceLimit() {
        return this.additionalResistanceLimit;
    }
}
