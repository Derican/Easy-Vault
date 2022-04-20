package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.block.entity.VaultRuneTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;

public class VaultRuneRenderer extends TileEntityRenderer<VaultRuneTileEntity> {
    private Minecraft mc;

    public VaultRuneRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        this.mc = Minecraft.getInstance();
    }

    public void render(final VaultRuneTileEntity tileEntity, final float partialTicks, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int combinedLight, final int combinedOverlay) {
        final ClientPlayerEntity player = this.mc.player;
        final Vector3d eyePosition = player.getEyePosition(1.0f);
        final Vector3d look = player.getViewVector(1.0f);
        final Vector3d endPos = eyePosition.add(look.x * 5.0, look.y * 5.0, look.z * 5.0);
        final RayTraceContext context = new RayTraceContext(eyePosition, endPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, (Entity) player);
        final BlockRayTraceResult result = player.level.clip(context);
        if (result.getBlockPos().equals(tileEntity.getBlockPos())) {
            final StringTextComponent text = new StringTextComponent(tileEntity.getBelongsTo());
            this.renderLabel(matrixStack, buffer, combinedLight, text, -1);
        }
    }

    private void renderLabel(final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int lightLevel, final StringTextComponent text, final int color) {
        final FontRenderer fontRenderer = this.mc.font;
        matrixStack.pushPose();
        final float scale = 0.02f;
        final int opacity = 1711276032;
        final float offset = (float) (-fontRenderer.width((ITextProperties) text) / 2);
        final Matrix4f matrix4f = matrixStack.last().pose();
        matrixStack.translate(0.5, 1.399999976158142, 0.5);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(this.mc.getEntityRenderDispatcher().cameraOrientation());
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        fontRenderer.drawInBatch((ITextComponent) text, offset, 0.0f, color, false, matrix4f, buffer, true, opacity, lightLevel);
        fontRenderer.drawInBatch((ITextComponent) text, offset, 0.0f, -1, false, matrix4f, buffer, false, 0, lightLevel);
        matrixStack.popPose();
    }
}
