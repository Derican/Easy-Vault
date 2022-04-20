package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;

public abstract class TexturedVaultModifier extends VaultModifier {
    @Expose
    private final String icon;

    public TexturedVaultModifier(final String name, final ResourceLocation icon) {
        super(name);
        this.icon = icon.toString();
    }

    public ResourceLocation getIcon() {
        return new ResourceLocation(this.icon);
    }
}
