// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.MegaJumpConfig;

public class MegaJumpKnockbackConfig extends MegaJumpConfig
{
    @Expose
    private final float radius;
    @Expose
    private final float knockbackStrengthMultiplier;
    
    public MegaJumpKnockbackConfig(final int cost, final int extraHeight, final float radius, final float knockbackStrengthMultiplier) {
        super(cost, extraHeight);
        this.radius = radius;
        this.knockbackStrengthMultiplier = knockbackStrengthMultiplier;
    }
    
    public float getRadius() {
        return this.radius;
    }
    
    public float getKnockbackStrengthMultiplier() {
        return this.knockbackStrengthMultiplier;
    }
}
