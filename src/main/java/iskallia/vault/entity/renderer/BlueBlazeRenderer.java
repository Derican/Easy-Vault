// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import net.minecraft.client.renderer.entity.BlazeRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.util.ResourceLocation;

public class BlueBlazeRenderer extends BlazeRenderer
{
    public static final ResourceLocation TEXTURE;
    
    public BlueBlazeRenderer(final EntityRendererManager renderManager) {
        super(renderManager);
    }
    
    protected void preRenderCallback(final BlazeEntity entitylivingbase, final MatrixStack matrixStack, final float partialTickTime) {
        super.scale(entitylivingbase, matrixStack, partialTickTime);
        matrixStack.scale(2.0f, 2.0f, 2.0f);
    }
    
    public ResourceLocation getTextureLocation(final BlazeEntity entity) {
        return BlueBlazeRenderer.TEXTURE;
    }
    
    static {
        TEXTURE = Vault.id("textures/entity/blue_blaze.png");
    }
}
