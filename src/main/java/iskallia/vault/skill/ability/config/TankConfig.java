// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModEffects;

public class TankConfig extends EffectConfig
{
    @Expose
    private final int durationTicks;
    @Expose
    private final float damageReductionPercent;
    
    public TankConfig(final int cost, final int durationTicks, final float damageReductionPercent) {
        super(cost, ModEffects.TANK, 0, Type.ICON_ONLY, Behavior.RELEASE_TO_PERFORM);
        this.durationTicks = durationTicks;
        this.damageReductionPercent = damageReductionPercent;
    }
    
    public int getDurationTicks() {
        return this.durationTicks;
    }
    
    public float getDamageReductionPercent() {
        return this.damageReductionPercent;
    }
}
