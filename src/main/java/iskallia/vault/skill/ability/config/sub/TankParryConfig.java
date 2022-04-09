// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.TankConfig;

public class TankParryConfig extends TankConfig
{
    @Expose
    private final float parryChance;
    
    public TankParryConfig(final int cost, final int durationTicks, final float parryChance) {
        super(cost, durationTicks, 0.0f);
        this.parryChance = parryChance;
    }
    
    public float getParryChance() {
        return this.parryChance;
    }
}
