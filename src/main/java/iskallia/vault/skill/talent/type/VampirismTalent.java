// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class VampirismTalent extends PlayerTalent
{
    @Expose
    private final float leechRatio;
    
    public VampirismTalent(final int cost, final float leechRatio) {
        super(cost);
        this.leechRatio = leechRatio;
    }
    
    public float getLeechRatio() {
        return this.leechRatio;
    }
}
