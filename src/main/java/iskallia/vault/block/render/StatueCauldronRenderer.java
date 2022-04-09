// 
// Decompiled by Procyon v0.6.0
// 

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

public class StatueCauldronRenderer extends TileEntityRenderer<StatueCauldronTileEntity>
{
    public StatueCauldronRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }
    
    public void render(final StatueCauldronTileEntity tileEntity, final float partialTicks, final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int combinedLightIn, final int combinedOverlayIn) {
        final BlockState state = tileEntity.getBlockState();
        final int level = (int)state.getValue(StatueCauldronBlock.LEVEL);
        final float percentage = tileEntity.getStatueCount() / (float)tileEntity.getRequiredAmount();
        final int height = 14;
        if (level < 3) {
            if (level == 1) {
                this.renderLiquid(matrixStackIn, bufferIn, 0.0f, percentage, 1.0f - percentage, height - 5);
            }
            else if (level == 2) {
                this.renderLiquid(matrixStackIn, bufferIn, 0.0f, percentage, 1.0f - percentage, height - 2);
            }
        }
        else {
            this.renderLiquid(matrixStackIn, bufferIn, 0.0f, percentage, 1.0f - percentage, height);
        }
    }
    
    private void renderLiquid(final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final float r, final float g, final float b, final int height) {
        final IVertexBuilder builder = buffer.getBuffer(RenderType.translucent());
        final TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(Fluids.WATER.getAttributes().getStillTexture());
        matrixStack.pushPose();
        this.addVertex(builder, matrixStack, this.p2f(1), this.p2f(height), this.p2f(1), sprite.getU0(), sprite.getV0(), r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), this.p2f(height), this.p2f(15), sprite.getU1(), sprite.getV0(), r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), this.p2f(height), this.p2f(15), sprite.getU1(), sprite.getV1(), r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), this.p2f(height), this.p2f(1), sprite.getU0(), sprite.getV1(), r, g, b, 1.0f);
        matrixStack.popPose();
    }
    
    private void addVertex(final IVertexBuilder renderer, final MatrixStack stack, final float x, final float y, final float z, final float u, final float v, final float r, final float g, final float b, final float a) {
        renderer.vertex(stack.last().pose(), x, y, z).color(r, g, b, 0.5f).uv(u, v).uv2(0, 240).normal(1.0f, 0.0f, 0.0f).endVertex();
    }
    
    private float p2f(final int pixel) {
        return 0.0625f * pixel;
    }
    
    public static Color getBlendedColor(final float percentage) {
        final float green = ensureRange(percentage);
        final float blue = ensureRange(1.0f - percentage);
        return new Color(0.0f, green, blue);
    }
    
    private static float ensureRange(final float value) {
        return Math.min(Math.max(value, 0.0f), 1.0f);
    }
}
