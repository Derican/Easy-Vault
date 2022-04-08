package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;

public class ArtifactChanceModifier extends TexturedVaultModifier {
    @Expose
    private final float artifactChanceIncrease;

    public ArtifactChanceModifier(String name, ResourceLocation icon, float chanceIncrease) {
        super(name, icon);
        this.artifactChanceIncrease = chanceIncrease;
    }

    public float getArtifactChanceIncrease() {
        return this.artifactChanceIncrease;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\ArtifactChanceModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */