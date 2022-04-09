// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class NinjaSet extends PlayerSet
{
    @Expose
    private float bonusParry;
    @Expose
    private float bonusParryCap;
    
    public NinjaSet(final float bonusParry, final float bonusParryCap) {
        super(VaultGear.Set.NINJA);
        this.bonusParry = bonusParry;
        this.bonusParryCap = bonusParryCap;
    }
    
    public float getBonusParry() {
        return this.bonusParry;
    }
    
    public float getBonusParryCap() {
        return this.bonusParryCap;
    }
}
