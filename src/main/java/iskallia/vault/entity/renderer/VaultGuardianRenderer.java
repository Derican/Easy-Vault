package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

public class VaultGuardianRenderer extends PiglinRenderer {
    public static final ResourceLocation TEXTURE = Vault.id("textures/entity/vault_guardian.png");

    public VaultGuardianRenderer(EntityRendererManager renderManager) {
        super(renderManager, false);
    }


    protected void preRenderCallback(MobEntity entity, MatrixStack matrixStack, float partialTickTime) {
        super.scale( entity, matrixStack, partialTickTime);

        matrixStack.scale(1.5F, 1.5F, 1.5F);
    }


    public ResourceLocation getTextureLocation(MobEntity entity) {
        return TEXTURE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\renderer\VaultGuardianRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */