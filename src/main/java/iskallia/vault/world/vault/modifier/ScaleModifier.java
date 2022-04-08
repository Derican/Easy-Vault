package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;

public class ScaleModifier extends TexturedVaultModifier {
    @Expose
    private final float scale;

    public ScaleModifier(String name, ResourceLocation icon, float scale) {
        super(name, icon);
        this.scale = scale;
    }

    public float getScale() {
        return this.scale;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\ScaleModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */