// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;

public class AggressiveCowBossRenderer extends CowRenderer
{
    public AggressiveCowBossRenderer(final EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }
    
    protected void preRenderCallback(final CowEntity entitylivingbaseIn, final MatrixStack matrixStackIn, final float partialTickTime) {
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
        matrixStackIn.scale(3.0f, 3.0f, 3.0f);
    }
}
