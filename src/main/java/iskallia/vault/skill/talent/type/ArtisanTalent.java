// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class ArtisanTalent extends PlayerTalent
{
    @Expose
    private final String defaultRoll;
    
    public ArtisanTalent(final int cost, final String defaultRoll) {
        super(cost);
        this.defaultRoll = defaultRoll;
    }
    
    public String getDefaultRoll() {
        return this.defaultRoll;
    }
}
