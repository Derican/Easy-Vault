// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.GhostWalkConfig;

public class GhostWalkDamageConfig extends GhostWalkConfig
{
    @Expose
    private final float damageMultiplierInGhostWalk;
    
    public GhostWalkDamageConfig(final int cost, final int level, final int durationTicks, final float damageMultiplierInGhostWalk) {
        super(cost, level, durationTicks);
        this.damageMultiplierInGhostWalk = damageMultiplierInGhostWalk;
    }
    
    public float getDamageMultiplierInGhostWalk() {
        return this.damageMultiplierInGhostWalk;
    }
}
