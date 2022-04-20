package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

public class VaultGuardianRenderer extends PiglinRenderer {
    public static final ResourceLocation TEXTURE;

    public VaultGuardianRenderer(final EntityRendererManager renderManager) {
        super(renderManager, false);
    }

    protected void preRenderCallback(final MobEntity entity, final MatrixStack matrixStack, final float partialTickTime) {
        super.scale(entity, matrixStack, partialTickTime);
        matrixStack.scale(1.5f, 1.5f, 1.5f);
    }

    public ResourceLocation getTextureLocation(final MobEntity entity) {
        return VaultGuardianRenderer.TEXTURE;
    }

    static {
        TEXTURE = Vault.id("textures/entity/vault_guardian.png");
    }
}
