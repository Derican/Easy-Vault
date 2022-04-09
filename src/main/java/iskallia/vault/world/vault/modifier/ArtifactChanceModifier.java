// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;

public class ArtifactChanceModifier extends TexturedVaultModifier
{
    @Expose
    private final float artifactChanceIncrease;
    
    public ArtifactChanceModifier(final String name, final ResourceLocation icon, final float chanceIncrease) {
        super(name, icon);
        this.artifactChanceIncrease = chanceIncrease;
    }
    
    public float getArtifactChanceIncrease() {
        return this.artifactChanceIncrease;
    }
}
