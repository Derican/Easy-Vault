package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;

public abstract class TexturedVaultModifier extends VaultModifier {
    @Expose
    private final String icon;

    public TexturedVaultModifier(String name, ResourceLocation icon) {
        super(name);
        this.icon = icon.toString();
    }

    public ResourceLocation getIcon() {
        return new ResourceLocation(this.icon);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\TexturedVaultModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */