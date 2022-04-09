// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.TankConfig;

public class TankSlowConfig extends TankConfig
{
    @Expose
    private final float slowAreaRadius;
    @Expose
    private final int slownessAmplifier;
    
    public TankSlowConfig(final int cost, final int durationTicks, final float damageReductionPercent, final float slowAreaRadius, final int slownessAmplifier) {
        super(cost, durationTicks, damageReductionPercent);
        this.slowAreaRadius = slowAreaRadius;
        this.slownessAmplifier = slownessAmplifier;
    }
    
    public float getSlowAreaRadius() {
        return this.slowAreaRadius;
    }
    
    public int getSlownessAmplifier() {
        return this.slownessAmplifier;
    }
}
