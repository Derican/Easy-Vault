package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;

public class BoogiemanRenderer extends ZombieRenderer {
    public static final ResourceLocation TEXTURE = Vault.id("textures/entity/boogieman.png");

    public BoogiemanRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);


        this.layers.remove(this.layers.size() - 1);
    }


    protected void preRenderCallback(ZombieEntity entitylivingbase, MatrixStack matrixStack, float partialTickTime) {
        super.scale(entitylivingbase, matrixStack, partialTickTime);
        matrixStack.scale(2.0F, 2.0F, 2.0F);
    }


    public ResourceLocation getTextureLocation(ZombieEntity entity) {
        return TEXTURE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\renderer\BoogiemanRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */