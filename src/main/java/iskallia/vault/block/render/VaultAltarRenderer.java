package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.altar.AltarInfusionRecipe;
import iskallia.vault.altar.RequiredItem;
import iskallia.vault.block.entity.VaultAltarTileEntity;
import iskallia.vault.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.List;

public class VaultAltarRenderer extends TileEntityRenderer<VaultAltarTileEntity> {
    private Minecraft mc = Minecraft.getInstance();
    private float currentTick = 0.0F;

    public VaultAltarRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    public void render(VaultAltarTileEntity altar, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (altar.getAltarState() == VaultAltarTileEntity.AltarState.IDLE) {
            return;
        }
        ClientPlayerEntity player = this.mc.player;
        int lightLevel = getLightAtPos(altar.getLevel(), altar.getBlockPos().above());


        renderItem(new ItemStack((IItemProvider) ModItems.VAULT_ROCK), new double[]{0.5D, 1.35D, 0.5D}, Vector3f.YP

                .rotationDegrees(180.0F - player.yRot), matrixStack, buffer, partialTicks, combinedOverlay, lightLevel);


        if (altar.getRecipe() == null || altar.getRecipe().getRequiredItems().isEmpty())
            return;
        AltarInfusionRecipe recipe = altar.getRecipe();
        List<RequiredItem> items = recipe.getRequiredItems();
        for (int i = 0; i < items.size(); i++) {
            double[] translation = getTranslation(i);
            RequiredItem requiredItem = items.get(i);
            ItemStack stack = requiredItem.getItem();
            StringTextComponent text = new StringTextComponent(String.valueOf(requiredItem.getAmountRequired() - requiredItem.getCurrentAmount()));
            int textColor = 16777215;
            if (requiredItem.reachedAmountRequired()) {
                text = new StringTextComponent("Complete");
                textColor = 65280;
            }

            renderItem(stack, translation, Vector3f.YP
                    .rotationDegrees(getAngle(player, partialTicks) * 5.0F), matrixStack, buffer, partialTicks, combinedOverlay, lightLevel);

            renderLabel(requiredItem, matrixStack, buffer, lightLevel, translation, text, textColor);
        }
    }

    private void renderItem(ItemStack stack, double[] translation, Quaternion rotation, MatrixStack matrixStack, IRenderTypeBuffer buffer, float partialTicks, int combinedOverlay, int lightLevel) {
        matrixStack.pushPose();
        matrixStack.translate(translation[0], translation[1], translation[2]);
        matrixStack.mulPose(rotation);
        if (stack.getItem().getItem() != ModItems.VAULT_ROCK) matrixStack.scale(0.5F, 0.5F, 0.5F);
        IBakedModel ibakedmodel = this.mc.getItemRenderer().getModel(stack, null, null);
        this.mc.getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, lightLevel, combinedOverlay, ibakedmodel);
        matrixStack.popPose();
    }

    private void renderLabel(RequiredItem item, MatrixStack matrixStack, IRenderTypeBuffer buffer, int lightLevel, double[] corner, StringTextComponent text, int color) {
        FontRenderer fontRenderer = this.mc.font;
        ClientPlayerEntity player = (Minecraft.getInstance()).player;
        if (player == null) {
            return;
        }
        matrixStack.pushPose();
        float scale = 0.01F;
        int opacity = 1711276032;
        float offset = (-fontRenderer.width((ITextProperties) text) / 2);
        Matrix4f matrix4f = matrixStack.last().pose();

        matrixStack.translate(corner[0], corner[1] + 0.25D, corner[2]);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(this.mc.getEntityRenderDispatcher().cameraOrientation());
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        fontRenderer.drawInBatch((ITextComponent) text, offset, 0.0F, color, false, matrix4f, buffer, false, opacity, lightLevel);

        if (player.isShiftKeyDown()) {
            ITextComponent itemName = item.getItem().getHoverName();
            offset = (-fontRenderer.width((ITextProperties) itemName) / 2);
            matrixStack.translate(0.0D, 1.399999976158142D, 0.0D);
            matrix4f.translate(new Vector3f(0.0F, 0.15F, 0.0F));
            fontRenderer.drawInBatch(item.getItem().getHoverName(), offset, 0.0F, color, false, matrix4f, buffer, false, opacity, lightLevel);
        }

        matrixStack.popPose();
    }

    private float getAngle(ClientPlayerEntity player, float partialTicks) {
        this.currentTick = player.tickCount;
        float angle = (this.currentTick + partialTicks) % 360.0F;
        return angle;
    }

    private int getLightAtPos(World world, BlockPos pos) {
        int blockLight = world.getBrightness(LightType.BLOCK, pos);
        int skyLight = world.getBrightness(LightType.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }

    private double[] getTranslation(int index) {
        switch (index) {
            case 0:
                return new double[]{0.95D, 1.35D, 0.05D};
            case 1:
                return new double[]{0.95D, 1.35D, 0.95D};
            case 2:
                return new double[]{0.05D, 1.35D, 0.95D};
        }
        return new double[]{0.05D, 1.35D, 0.05D};
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\render\VaultAltarRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */