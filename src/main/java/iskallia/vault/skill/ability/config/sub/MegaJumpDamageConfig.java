// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.MegaJumpConfig;

public class MegaJumpDamageConfig extends MegaJumpConfig
{
    @Expose
    private final float radius;
    @Expose
    private final float percentAttackDamageDealt;
    @Expose
    private final float knockbackStrengthMultiplier;
    
    public MegaJumpDamageConfig(final int cost, final int extraHeight, final float radius, final float percentAttackDamageDealt, final float knockbackStrengthMultiplier) {
        super(cost, extraHeight);
        this.radius = radius;
        this.percentAttackDamageDealt = percentAttackDamageDealt;
        this.knockbackStrengthMultiplier = knockbackStrengthMultiplier;
    }
    
    public float getRadius() {
        return this.radius;
    }
    
    public float getPercentAttackDamageDealt() {
        return this.percentAttackDamageDealt;
    }
    
    public float getKnockbackStrengthMultiplier() {
        return this.knockbackStrengthMultiplier;
    }
}
