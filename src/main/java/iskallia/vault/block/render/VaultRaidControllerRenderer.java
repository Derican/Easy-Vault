// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.block.entity.VaultRaidControllerTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;

import java.util.List;

public class VaultRaidControllerRenderer extends TileEntityRenderer<VaultRaidControllerTileEntity>
{
    public VaultRaidControllerRenderer(final TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }
    
    public void render(final VaultRaidControllerTileEntity te, final float partialTicks, final MatrixStack renderStack, final IRenderTypeBuffer buffer, final int combinedLight, final int combinedOverlay) {
        if (te.didTriggerRaid()) {
            return;
        }
        this.drawHoveringModifiers(te.getModifierDisplay(), partialTicks, renderStack, buffer, combinedLight);
    }
    
    private void drawHoveringModifiers(final List<ITextComponent> modifiers, final float pTicks, final MatrixStack renderStack, final IRenderTypeBuffer buffer, final int combinedLight) {
        final EntityRendererManager mgr = Minecraft.getInstance().getEntityRenderDispatcher();
        final FontRenderer fr = mgr.getFont();
        renderStack.pushPose();
        renderStack.translate(0.5, 2.5, 0.5);
        renderStack.mulPose(mgr.cameraOrientation());
        renderStack.scale(-0.025f, -0.025f, 0.025f);
        final Matrix4f matr = renderStack.last().pose();
        final float textBgOpacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25f);
        final int textBgAlpha = (int)(textBgOpacity * 255.0f) << 24;
        for (final ITextComponent modifier : modifiers) {
            final float xShift = fr.width((ITextProperties)modifier) / 2.0f;
            fr.drawInBatch(modifier, -xShift, 0.0f, 553648127, false, matr, buffer, true, textBgAlpha, combinedLight);
            fr.drawInBatch(modifier, -xShift, 0.0f, -1, false, matr, buffer, false, 0, combinedLight);
            renderStack.translate(0.0, -10.0, 0.0);
        }
        renderStack.popPose();
    }
    
    private boolean isInDrawDistance(final BlockPos pos) {
        final EntityRendererManager mgr = Minecraft.getInstance().getEntityRenderDispatcher();
        return mgr.distanceToSqr((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) < 4096.0;
    }
}
