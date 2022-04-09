// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

import java.util.UUID;

public class DryadSet extends PlayerSet
{
    public static final UUID HEALTH_MODIFIER_ID;
    @Expose
    private float extraHealth;
    
    public DryadSet(final float extraHealth) {
        super(VaultGear.Set.DRYAD);
        this.extraHealth = extraHealth;
    }
    
    public float getExtraHealth() {
        return this.extraHealth;
    }
    
    static {
        HEALTH_MODIFIER_ID = UUID.fromString("548aa8bb-0570-4e1c-8ca1-e9504373109c");
    }
}
