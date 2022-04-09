// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class FatalStrikeChanceTalent extends PlayerTalent
{
    @Expose
    private final float additionalFatalStrikeChance;
    
    public FatalStrikeChanceTalent(final int cost, final float additionalFatalStrikeChance) {
        super(cost);
        this.additionalFatalStrikeChance = additionalFatalStrikeChance;
    }
    
    public float getAdditionalFatalStrikeChance() {
        return this.additionalFatalStrikeChance;
    }
}
