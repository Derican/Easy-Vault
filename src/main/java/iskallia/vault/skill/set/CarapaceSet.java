// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class CarapaceSet extends PlayerSet
{
    @Expose
    private final float absorptionPercent;
    
    public CarapaceSet(final float absorptionPercent) {
        super(VaultGear.Set.CARAPACE);
        this.absorptionPercent = absorptionPercent;
    }
    
    public float getAbsorptionPercent() {
        return this.absorptionPercent;
    }
}
