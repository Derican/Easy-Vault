package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import iskallia.vault.entity.EtchingVendorEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.util.ResourceLocation;

public class EtchingVendorRenderer extends MobRenderer<EtchingVendorEntity, VillagerModel<EtchingVendorEntity>> {
    private static final ResourceLocation TEXTURES = Vault.id("textures/entity/etching_trader.png");

    public EtchingVendorRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn,  new VillagerModel(0.0F), 0.5F);
        addLayer((LayerRenderer) new HeadLayer((IEntityRenderer) this));
        addLayer((LayerRenderer) new CrossedArmsItemLayer((IEntityRenderer) this));
    }


    public ResourceLocation getTextureLocation(EtchingVendorEntity entity) {
        return TEXTURES;
    }


    protected void preRenderCallback(EtchingVendorEntity entity, MatrixStack matrixStack, float pTicks) {
        this.shadowRadius = 0.5F;

        float size = 0.9375F;
        matrixStack.scale(size, size, size);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\renderer\EtchingVendorRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */