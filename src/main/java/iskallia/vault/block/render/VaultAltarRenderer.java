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
import net.minecraft.entity.LivingEntity;
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
    private Minecraft mc;
    private float currentTick;

    public VaultAltarRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        this.mc = Minecraft.getInstance();
        this.currentTick = 0.0f;
    }

    public void render(final VaultAltarTileEntity altar, final float partialTicks, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int combinedLight, final int combinedOverlay) {
        if (altar.getAltarState() == VaultAltarTileEntity.AltarState.IDLE) {
            return;
        }
        final ClientPlayerEntity player = this.mc.player;
        final int lightLevel = this.getLightAtPos(altar.getLevel(), altar.getBlockPos().above());
        this.renderItem(new ItemStack((IItemProvider) ModItems.VAULT_ROCK), new double[]{0.5, 1.35, 0.5}, Vector3f.YP.rotationDegrees(180.0f - player.yRot), matrixStack, buffer, partialTicks, combinedOverlay, lightLevel);
        if (altar.getRecipe() == null || altar.getRecipe().getRequiredItems().isEmpty()) {
            return;
        }
        final AltarInfusionRecipe recipe = altar.getRecipe();
        final List<RequiredItem> items = recipe.getRequiredItems();
        for (int i = 0; i < items.size(); ++i) {
            final double[] translation = this.getTranslation(i);
            final RequiredItem requiredItem = items.get(i);
            final ItemStack stack = requiredItem.getItem();
            StringTextComponent text = new StringTextComponent(String.valueOf(requiredItem.getAmountRequired() - requiredItem.getCurrentAmount()));
            int textColor = 16777215;
            if (requiredItem.reachedAmountRequired()) {
                text = new StringTextComponent("Complete");
                textColor = 65280;
            }
            this.renderItem(stack, translation, Vector3f.YP.rotationDegrees(this.getAngle(player, partialTicks) * 5.0f), matrixStack, buffer, partialTicks, combinedOverlay, lightLevel);
            this.renderLabel(requiredItem, matrixStack, buffer, lightLevel, translation, text, textColor);
        }
        if (recipe.isPogInfused()) {
            final boolean infusing = altar.getAltarState() == VaultAltarTileEntity.AltarState.INFUSING;
            final ItemStack pogStack = new ItemStack((IItemProvider)ModItems.POG);
            final IBakedModel ibakedmodel = this.mc.getItemRenderer().getModel(pogStack, (World)null, (LivingEntity)null);
            for (int j = 0; j < 3; ++j) {
                final double r = 1.0;
                matrixStack.pushPose();
                matrixStack.translate(0.5, 0.85, 0.5);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(j * 120 + this.getAngle(player, partialTicks) * (infusing ? 25.0f : 15.0f)));
                matrixStack.translate(r, Math.sin(this.getAngle(player, partialTicks) * 0.25 + j) * 0.2, 0.0);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(this.getAngle(player, partialTicks) * 10.0f));
                this.mc.getItemRenderer().render(pogStack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, lightLevel, combinedOverlay, ibakedmodel);
                matrixStack.popPose();
            }
        }
    }

    private void renderItem(final ItemStack stack, final double[] translation, final Quaternion rotation, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final float partialTicks, final int combinedOverlay, final int lightLevel) {
        matrixStack.pushPose();
        matrixStack.translate(translation[0], translation[1], translation[2]);
        matrixStack.mulPose(rotation);
        if (stack.getItem().getItem() != ModItems.VAULT_ROCK) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        }
        final IBakedModel ibakedmodel = this.mc.getItemRenderer().getModel(stack, (World) null, (LivingEntity) null);
        this.mc.getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, lightLevel, combinedOverlay, ibakedmodel);
        matrixStack.popPose();
    }

    private void renderLabel(final RequiredItem item, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int lightLevel, final double[] corner, final StringTextComponent text, final int color) {
        final FontRenderer fontRenderer = this.mc.font;
        final ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        matrixStack.pushPose();
        final float scale = 0.01f;
        final int opacity = 1711276032;
        float offset = (float) (-fontRenderer.width((ITextProperties) text) / 2);
        final Matrix4f matrix4f = matrixStack.last().pose();
        matrixStack.translate(corner[0], corner[1] + 0.25, corner[2]);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(this.mc.getEntityRenderDispatcher().cameraOrientation());
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        fontRenderer.drawInBatch((ITextComponent) text, offset, 0.0f, color, false, matrix4f, buffer, false, opacity, lightLevel);
        if (player.isShiftKeyDown()) {
            final ITextComponent itemName = item.getItem().getHoverName();
            offset = (float) (-fontRenderer.width((ITextProperties) itemName) / 2);
            matrixStack.translate(0.0, 1.399999976158142, 0.0);
            matrix4f.translate(new Vector3f(0.0f, 0.15f, 0.0f));
            fontRenderer.drawInBatch(item.getItem().getHoverName(), offset, 0.0f, color, false, matrix4f, buffer, false, opacity, lightLevel);
        }
        matrixStack.popPose();
    }

    private float getAngle(final ClientPlayerEntity player, final float partialTicks) {
        this.currentTick = (float) player.tickCount;
        final float angle = (this.currentTick + partialTicks) % 360.0f;
        return angle;
    }

    private int getLightAtPos(final World world, final BlockPos pos) {
        final int blockLight = world.getBrightness(LightType.BLOCK, pos);
        final int skyLight = world.getBrightness(LightType.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }

    private double[] getTranslation(final int index) {
        switch (index) {
            case 0: {
                return new double[]{0.95, 1.35, 0.05};
            }
            case 1: {
                return new double[]{0.95, 1.35, 0.95};
            }
            case 2: {
                return new double[]{0.05, 1.35, 0.95};
            }
            default: {
                return new double[]{0.05, 1.35, 0.05};
            }
        }
    }
}
