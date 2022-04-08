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

public class VaultRaidControllerRenderer
        extends TileEntityRenderer<VaultRaidControllerTileEntity> {
    public VaultRaidControllerRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }


    public void render(VaultRaidControllerTileEntity te, float partialTicks, MatrixStack renderStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (te.didTriggerRaid()) {
            return;
        }
        drawHoveringModifiers(te.getModifierDisplay(), partialTicks, renderStack, buffer, combinedLight);
    }

    private void drawHoveringModifiers(List<ITextComponent> modifiers, float pTicks, MatrixStack renderStack, IRenderTypeBuffer buffer, int combinedLight) {
        EntityRendererManager mgr = Minecraft.getInstance().getEntityRenderDispatcher();
        FontRenderer fr = mgr.getFont();

        renderStack.pushPose();
        renderStack.translate(0.5D, 2.5D, 0.5D);
        renderStack.mulPose(mgr.cameraOrientation());
        renderStack.scale(-0.025F, -0.025F, 0.025F);

        Matrix4f matr = renderStack.last().pose();
        float textBgOpacity = (Minecraft.getInstance()).options.getBackgroundOpacity(0.25F);
        int textBgAlpha = (int) (textBgOpacity * 255.0F) << 24;

        for (ITextComponent modifier : modifiers) {
            float xShift = fr.width((ITextProperties) modifier) / 2.0F;
            fr.drawInBatch(modifier, -xShift, 0.0F, 553648127, false, matr, buffer, true, textBgAlpha, combinedLight);
            fr.drawInBatch(modifier, -xShift, 0.0F, -1, false, matr, buffer, false, 0, combinedLight);
            renderStack.translate(0.0D, -10.0D, 0.0D);
        }

        renderStack.popPose();
    }

    private boolean isInDrawDistance(BlockPos pos) {
        EntityRendererManager mgr = Minecraft.getInstance().getEntityRenderDispatcher();
        return (mgr.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 4096.0D);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\render\VaultRaidControllerRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */