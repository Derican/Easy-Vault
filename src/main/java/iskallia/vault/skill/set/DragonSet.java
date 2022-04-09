// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

public class DragonSet extends PlayerSet
{
    public static int MULTIPLIER_ID;
    @Expose
    private float damageMultiplier;
    
    public DragonSet(final float damageMultiplier) {
        super(VaultGear.Set.DRAGON);
        this.damageMultiplier = damageMultiplier;
    }
    
    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }
    
    static {
        DragonSet.MULTIPLIER_ID = -1;
    }
}
