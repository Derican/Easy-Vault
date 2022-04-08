package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.ResourceLocation;

public class MonsterEyeRenderer extends SlimeRenderer {
    public static final ResourceLocation TEXTURE = Vault.id("textures/entity/monster_eye.png");

    public MonsterEyeRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }


    protected void scale(SlimeEntity entitylivingbase, MatrixStack matrixStack, float partialTickTime) {
        super.scale(entitylivingbase, matrixStack, partialTickTime);
        matrixStack.scale(2.0F, 2.0F, 2.0F);
    }


    public ResourceLocation getTextureLocation(SlimeEntity entity) {
        return TEXTURE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\renderer\MonsterEyeRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */