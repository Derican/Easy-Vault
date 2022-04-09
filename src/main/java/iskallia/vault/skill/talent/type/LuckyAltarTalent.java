// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class LuckyAltarTalent extends PlayerTalent
{
    @Expose
    private final float luckyAltarChance;
    
    public LuckyAltarTalent(final int cost, final float luckyAltarChance) {
        super(cost);
        this.luckyAltarChance = luckyAltarChance;
    }
    
    public float getLuckyAltarChance() {
        return this.luckyAltarChance;
    }
}
