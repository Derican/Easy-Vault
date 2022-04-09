// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.TankConfig;

public class TankReflectConfig extends TankConfig
{
    @Expose
    private final float damageReflectChance;
    @Expose
    private final float damageReflectPercent;
    
    public TankReflectConfig(final int cost, final int durationTicks, final float damageReductionPercent, final float damageReflectChance, final float damageReflectPercent) {
        super(cost, durationTicks, damageReductionPercent);
        this.damageReflectChance = damageReflectChance;
        this.damageReflectPercent = damageReflectPercent;
    }
    
    public float getDamageReflectChance() {
        return this.damageReflectChance;
    }
    
    public float getDamageReflectPercent() {
        return this.damageReflectPercent;
    }
}
