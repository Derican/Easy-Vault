package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;

public class AggressiveCowBossRenderer extends CowRenderer {
    public AggressiveCowBossRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }


    protected void preRenderCallback(CowEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
        matrixStackIn.scale(3.0F, 3.0F, 3.0F);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\renderer\AggressiveCowBossRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */