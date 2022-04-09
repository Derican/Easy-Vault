// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class UnbreakableTalent extends PlayerTalent
{
    @Expose
    private final float extraUnbreaking;
    
    public UnbreakableTalent(final int cost, final int extraUnbreaking) {
        super(cost);
        this.extraUnbreaking = (float)extraUnbreaking;
    }
    
    public float getExtraUnbreaking() {
        return this.extraUnbreaking;
    }
}
