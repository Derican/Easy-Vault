package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;

public class DurabilityDamageModifier extends TexturedVaultModifier {
    @Expose
    protected float durabilityDamageTakenMultiplier;

    public DurabilityDamageModifier(String name, ResourceLocation icon, float durabilityDamageTakenMultiplier) {
        super(name, icon);
        this.durabilityDamageTakenMultiplier = durabilityDamageTakenMultiplier;
    }

    public float getDurabilityDamageTakenMultiplier() {
        return this.durabilityDamageTakenMultiplier;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\DurabilityDamageModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */