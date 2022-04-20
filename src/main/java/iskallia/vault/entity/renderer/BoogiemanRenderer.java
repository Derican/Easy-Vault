package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;

public class BoogiemanRenderer extends ZombieRenderer {
    public static final ResourceLocation TEXTURE;

    public BoogiemanRenderer(final EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.layers.remove(this.layers.size() - 1);
    }

    protected void preRenderCallback(final ZombieEntity entitylivingbase, final MatrixStack matrixStack, final float partialTickTime) {
        super.scale(entitylivingbase, matrixStack, partialTickTime);
        matrixStack.scale(2.0f, 2.0f, 2.0f);
    }

    public ResourceLocation getTextureLocation(final ZombieEntity entity) {
        return BoogiemanRenderer.TEXTURE;
    }

    static {
        TEXTURE = Vault.id("textures/entity/boogieman.png");
    }
}
