// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class SoulShardTalent extends PlayerTalent
{
    @Expose
    protected final float additionalSoulShardChance;
    
    public SoulShardTalent(final int cost, final float additionalSoulShardChance) {
        super(cost);
        this.additionalSoulShardChance = additionalSoulShardChance;
    }
    
    public float getAdditionalSoulShardChance() {
        return this.additionalSoulShardChance;
    }
}
