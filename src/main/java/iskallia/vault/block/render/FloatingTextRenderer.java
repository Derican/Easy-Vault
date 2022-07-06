// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.client.gui.FontRenderer;
import java.util.List;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import com.mojang.blaze3d.matrix.MatrixStack;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import iskallia.vault.block.entity.FloatingTextTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

public class FloatingTextRenderer extends TileEntityRenderer<FloatingTextTileEntity>
{
    public FloatingTextRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }
    
    public void render(@Nonnull final FloatingTextTileEntity tileEntity, final float partialTicks, @Nonnull final MatrixStack matrixStack, @Nonnull final IRenderTypeBuffer buffer, final int combinedLightIn, final int combinedOverlayIn) {
        final List<String> lines = tileEntity.getLines();
        final int length = lines.size();
        final Minecraft minecraft = Minecraft.getInstance();
        final FontRenderer fontRenderer = minecraft.font;
        for (int i = length - 1; i >= 0; --i) {
            final String line = lines.get(i);
            final IFormattableTextComponent text = this.parseTextComponent(line);
            if (text != null) {
                final float scale = 0.02f;
                final int color = -1;
                final int opacity = 1711276032;
                matrixStack.pushPose();
                final Matrix4f matrix4f = matrixStack.last().pose();
                final float offset = (float)(-fontRenderer.width((ITextProperties)text) / 2);
                matrixStack.translate(0.5, (double)(1.7f + 0.25f * (length - i)), 0.5);
                matrixStack.scale(scale, scale, scale);
                matrixStack.mulPose(minecraft.getEntityRenderDispatcher().cameraOrientation());
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
                fontRenderer.drawInBatch((ITextComponent)text, offset, 0.0f, color, false, matrix4f, buffer, false, opacity, combinedLightIn);
                fontRenderer.drawInBatch((ITextComponent)text, offset, 0.0f, -1, false, matrix4f, buffer, false, 0, combinedLightIn);
                matrixStack.popPose();
            }
        }
    }
    
    public IFormattableTextComponent parseTextComponent(final String line) {
        try {
            return ITextComponent.Serializer.fromJsonLenient(line);
        }
        catch (final Exception exception) {
            return new StringTextComponent("#!Parse Error!#").withStyle(TextFormatting.RED);
        }
    }
}
