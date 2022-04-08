package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;

import java.util.UUID;

public class DryadSet
        extends PlayerSet {
    public static final UUID HEALTH_MODIFIER_ID = UUID.fromString("548aa8bb-0570-4e1c-8ca1-e9504373109c");
    @Expose
    private float extraHealth;

    public DryadSet(float extraHealth) {
        super(VaultGear.Set.DRYAD);
        this.extraHealth = extraHealth;
    }


    public float getExtraHealth() {
        return this.extraHealth;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\DryadSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */