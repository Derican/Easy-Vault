// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity.renderer;

import iskallia.vault.entity.EffectCloudEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

public class EffectCloudRenderer extends EntityRenderer<EffectCloudEntity>
{
    public EffectCloudRenderer(final EntityRendererManager manager) {
        super(manager);
    }
    
    public ResourceLocation getTextureLocation(final EffectCloudEntity entity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}
