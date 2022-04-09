// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class PorcupineSet extends PlayerSet
{
    @Expose
    protected float additionalThornsChance;
    @Expose
    protected float additionalThornsDamage;
    
    public PorcupineSet(final float additionalThornsChance, final float additionalThornsDamage) {
        super(VaultGear.Set.PORCUPINE);
        this.additionalThornsChance = additionalThornsChance;
        this.additionalThornsDamage = additionalThornsDamage;
    }
    
    public float getAdditionalThornsChance() {
        return this.additionalThornsChance;
    }
    
    public float getAdditionalThornsDamage() {
        return this.additionalThornsDamage;
    }
}
