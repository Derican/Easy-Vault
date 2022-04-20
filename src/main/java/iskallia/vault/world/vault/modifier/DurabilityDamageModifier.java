package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;

public class DurabilityDamageModifier extends TexturedVaultModifier {
    @Expose
    protected float durabilityDamageTakenMultiplier;

    public DurabilityDamageModifier(final String name, final ResourceLocation icon, final float durabilityDamageTakenMultiplier) {
        super(name, icon);
        this.durabilityDamageTakenMultiplier = durabilityDamageTakenMultiplier;
    }

    public float getDurabilityDamageTakenMultiplier() {
        return this.durabilityDamageTakenMultiplier;
    }
}
