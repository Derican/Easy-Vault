package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;

public class CatalystChanceModifier extends TexturedVaultModifier {
    @Expose
    private final float catalystChanceIncrease;

    public CatalystChanceModifier(String name, ResourceLocation icon, float chanceIncrease) {
        super(name, icon);
        this.catalystChanceIncrease = chanceIncrease;
    }

    public float getCatalystChanceIncrease() {
        return this.catalystChanceIncrease;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\CatalystChanceModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */