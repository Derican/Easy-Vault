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
    private Minecraft mc = Minecraft.getInstance();

    public VaultRuneRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    public void render(VaultRuneTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        ClientPlayerEntity player = this.mc.player;
        Vector3d eyePosition = player.getEyePosition(1.0F);
        Vector3d look = player.getViewVector(1.0F);
        Vector3d endPos = eyePosition.add(look.x * 5.0D, look.y * 5.0D, look.z * 5.0D);
        RayTraceContext context = new RayTraceContext(eyePosition, endPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, (Entity) player);


        BlockRayTraceResult result = player.level.clip(context);

        if (result.getBlockPos().equals(tileEntity.getBlockPos())) {
            StringTextComponent text = new StringTextComponent(tileEntity.getBelongsTo());
            renderLabel(matrixStack, buffer, combinedLight, text, -1);
        }
    }


    private void renderLabel(MatrixStack matrixStack, IRenderTypeBuffer buffer, int lightLevel, StringTextComponent text, int color) {
        FontRenderer fontRenderer = this.mc.font;


        matrixStack.pushPose();
        float scale = 0.02F;
        int opacity = 1711276032;
        float offset = (-fontRenderer.width((ITextProperties) text) / 2);
        Matrix4f matrix4f = matrixStack.last().pose();

        matrixStack.translate(0.5D, 1.399999976158142D, 0.5D);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(this.mc.getEntityRenderDispatcher().cameraOrientation());
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));

        fontRenderer.drawInBatch((ITextComponent) text, offset, 0.0F, color, false, matrix4f, buffer, true, opacity, lightLevel);
        fontRenderer.drawInBatch((ITextComponent) text, offset, 0.0F, -1, false, matrix4f, buffer, false, 0, lightLevel);
        matrixStack.popPose();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\render\VaultRuneRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */