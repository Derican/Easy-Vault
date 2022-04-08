package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.block.StatueCauldronBlock;
import iskallia.vault.block.entity.StatueCauldronTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.state.Property;

import java.awt.*;

public class StatueCauldronRenderer extends TileEntityRenderer<StatueCauldronTileEntity> {
    public StatueCauldronRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    public void render(StatueCauldronTileEntity tileEntity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState state = tileEntity.getBlockState();
        int level = ((Integer) state.getValue((Property) StatueCauldronBlock.LEVEL)).intValue();
        float percentage = tileEntity.getStatueCount() / tileEntity.getRequiredAmount();
        int height = 14;
        if (level < 3) {
            if (level == 1) {
                renderLiquid(matrixStackIn, bufferIn, 0.0F, percentage, 1.0F - percentage, height - 5);
            } else if (level == 2) {
                renderLiquid(matrixStackIn, bufferIn, 0.0F, percentage, 1.0F - percentage, height - 2);
            }
        } else {
            renderLiquid(matrixStackIn, bufferIn, 0.0F, percentage, 1.0F - percentage, height);
        }

    }

    private void renderLiquid(MatrixStack matrixStack, IRenderTypeBuffer buffer, float r, float g, float b, int height) {
        IVertexBuilder builder = buffer.getBuffer(RenderType.translucent());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(Fluids.WATER.getAttributes().getStillTexture());

        matrixStack.pushPose();
        addVertex(builder, matrixStack, p2f(1), p2f(height), p2f(1), sprite.getU0(), sprite.getV0(), r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), p2f(height), p2f(15), sprite.getU1(), sprite.getV0(), r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), p2f(height), p2f(15), sprite.getU1(), sprite.getV1(), r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), p2f(height), p2f(1), sprite.getU0(), sprite.getV1(), r, g, b, 1.0F);
        matrixStack.popPose();
    }

    private void addVertex(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
        renderer.vertex(stack.last().pose(), x, y, z)
                .color(r, g, b, 0.5F)
                .uv(u, v)
                .uv2(0, 240)
                .normal(1.0F, 0.0F, 0.0F)
                .endVertex();
    }

    private float p2f(int pixel) {
        return 0.0625F * pixel;
    }

    public static Color getBlendedColor(float percentage) {
        float green = ensureRange(percentage);
        float blue = ensureRange(1.0F - percentage);

        return new Color(0.0F, green, blue);
    }

    private static float ensureRange(float value) {
        return Math.min(Math.max(value, 0.0F), 1.0F);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\render\StatueCauldronRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */