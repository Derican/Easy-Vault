// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.DashConfig;

public class DashDamageConfig extends DashConfig
{
    @Expose
    private final float attackDamagePercentPerDash;
    @Expose
    private final float radiusOfAttack;
    
    public DashDamageConfig(final int cost, final int extraRadius, final float attackDamagePercentPerDash, final float radiusOfAttack) {
        super(cost, extraRadius);
        this.attackDamagePercentPerDash = attackDamagePercentPerDash;
        this.radiusOfAttack = radiusOfAttack;
    }
    
    public float getAttackDamagePercentPerDash() {
        return this.attackDamagePercentPerDash;
    }
    
    public float getRadiusOfAttack() {
        return this.radiusOfAttack;
    }
}
