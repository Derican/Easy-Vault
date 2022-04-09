// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.ResourceLocation;

public class RobotRenderer extends IronGolemRenderer
{
    public static final ResourceLocation TEXTURE;
    
    public RobotRenderer(final EntityRendererManager renderManager) {
        super(renderManager);
    }
    
    protected void preRenderCallback(final IronGolemEntity entitylivingbase, final MatrixStack matrixStack, final float partialTickTime) {
        super.scale(entitylivingbase, matrixStack, partialTickTime);
        matrixStack.scale(2.0f, 2.0f, 2.0f);
    }
    
    public ResourceLocation getTextureLocation(final IronGolemEntity entity) {
        return RobotRenderer.TEXTURE;
    }
    
    static {
        TEXTURE = Vault.id("textures/entity/robot.png");
    }
}
