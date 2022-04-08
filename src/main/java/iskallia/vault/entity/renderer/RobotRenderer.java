package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.ResourceLocation;

public class RobotRenderer extends IronGolemRenderer {
    public static final ResourceLocation TEXTURE = Vault.id("textures/entity/robot.png");

    public RobotRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }


    protected void preRenderCallback(IronGolemEntity entitylivingbase, MatrixStack matrixStack, float partialTickTime) {
        super.scale(entitylivingbase, matrixStack, partialTickTime);
        matrixStack.scale(2.0F, 2.0F, 2.0F);
    }


    public ResourceLocation getTextureLocation(IronGolemEntity entity) {
        return TEXTURE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\renderer\RobotRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */