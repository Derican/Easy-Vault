package iskallia.vault.entity.renderer;

import iskallia.vault.entity.EffectCloudEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

public class EffectCloudRenderer extends EntityRenderer<EffectCloudEntity> {
    public EffectCloudRenderer(EntityRendererManager manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(EffectCloudEntity entity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\renderer\EffectCloudRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */