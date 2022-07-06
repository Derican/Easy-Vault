// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity.renderer;

import iskallia.vault.Vault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.entity.LivingEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import iskallia.vault.entity.model.EyestalkModel;
import iskallia.vault.entity.EyestalkEntity;
import net.minecraft.client.renderer.entity.MobRenderer;

public class EyestalkRenderer extends MobRenderer<EyestalkEntity, EyestalkModel>
{
    public static final ResourceLocation DEFAULT_TEXTURE;
    
    public EyestalkRenderer(final EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new EyestalkModel(), 0.2f);
    }
    
    protected void preRenderCallback(final EyestalkEntity entitylivingbaseIn, final MatrixStack matrixStack, final float partialTickTime) {
        super.scale(entitylivingbaseIn, matrixStack, partialTickTime);
        final float scale = 4.0f;
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0.0, 0.699999988079071, 0.0);
    }
    
    protected void renderName(final EyestalkEntity entityIn, final ITextComponent displayNameIn, final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int packedLightIn) {
    }
    
    protected boolean canRenderName(final EyestalkEntity entity) {
        return false;
    }
    
    public ResourceLocation getTextureLocation(final EyestalkEntity entity) {
        return EyestalkRenderer.DEFAULT_TEXTURE;
    }
    
    static {
        DEFAULT_TEXTURE = Vault.id("textures/entity/eyesore/eyestalk.png");
    }
}
