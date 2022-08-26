package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.block.base.FillableAltarTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.*;

public class FillableAltarRenderer extends TileEntityRenderer<FillableAltarTileEntity> {
    private static final Vector3f FLUID_LOWER_POS;
    private static final Vector3f FLUID_UPPER_POS;

    public FillableAltarRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    public void render(final FillableAltarTileEntity tileEntity, final float partialTicks, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int combinedLight, final int combinedOverlayIn) {
        if (!tileEntity.initialized()) {
            return;
        }
        final IVertexBuilder builder = buffer.getBuffer(RenderType.translucent());
        final float progressPercentage = tileEntity.progressPercentage();
        if (progressPercentage > 0.0f) {
            final float fluidMaxHeight = FillableAltarRenderer.FLUID_UPPER_POS.y() - FillableAltarRenderer.FLUID_LOWER_POS.y();
            final Vector3f upperPos = new Vector3f(FillableAltarRenderer.FLUID_UPPER_POS.x(), FillableAltarRenderer.FLUID_LOWER_POS.y() + fluidMaxHeight * progressPercentage, FillableAltarRenderer.FLUID_UPPER_POS.z());
            this.renderCuboid(builder, matrixStack, FillableAltarRenderer.FLUID_LOWER_POS, upperPos, tileEntity.getFillColor());
            if (buffer instanceof IRenderTypeBuffer.Impl) {
                ((IRenderTypeBuffer.Impl) buffer).endBatch(RenderType.translucent());
            }
        }
        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.hitResult != null && minecraft.hitResult.getType() == RayTraceResult.Type.BLOCK) {
            final BlockRayTraceResult result = (BlockRayTraceResult) minecraft.hitResult;
            if (tileEntity.getBlockPos().equals(result.getBlockPos())) {
                final StringTextComponent progressText = (StringTextComponent) (tileEntity.isMaxedOut() ? new StringTextComponent("Right Click to Loot!").setStyle(Style.EMPTY.withColor(Color.fromRgb(-1313364))) : new StringTextComponent(tileEntity.getCurrentProgress() + " / " + tileEntity.getMaxProgress() + " ").append(tileEntity.getRequirementUnit()));
                this.renderLabel(matrixStack, 0.5f, 2.3f, 0.5f, buffer, combinedLight, tileEntity.getRequirementName());
                this.renderLabel(matrixStack, 0.5f, 2.1f, 0.5f, buffer, combinedLight, progressText);
            }
        }
    }

    public void renderLabel(final MatrixStack matrixStack, final float x, final float y, final float z, final IRenderTypeBuffer buffer, final int lightLevel, final ITextComponent text) {
        final Minecraft minecraft = Minecraft.getInstance();
        final FontRenderer fontRenderer = minecraft.font;
        matrixStack.pushPose();
        final float scale = 0.02f;
        final int opacity = 1711276032;
        final float offset = (float) (-fontRenderer.width(text) / 2);
        final Matrix4f matrix4f = matrixStack.last().pose();
        matrixStack.translate(x, y, z);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(minecraft.getEntityRenderDispatcher().cameraOrientation());
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        fontRenderer.drawInBatch(text, offset, 0.0f, -1, false, matrix4f, buffer, true, opacity, lightLevel);
        fontRenderer.drawInBatch(text, offset, 0.0f, -1, false, matrix4f, buffer, false, 0, lightLevel);
        matrixStack.popPose();
    }

    public void renderCuboid(final IVertexBuilder builder, final MatrixStack matrixStack, final Vector3f v1, final Vector3f v2, final java.awt.Color tint) {
        final TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(Fluids.WATER.getAttributes().getStillTexture());
        final float minU = sprite.getU(0.0);
        final float maxU = sprite.getU(16.0);
        final float minV = sprite.getV(0.0);
        final float maxV = sprite.getV(16.0);
        matrixStack.pushPose();
        this.addVertex(builder, matrixStack, v1.x(), v2.y(), v1.z(), tint, minU, maxV);
        this.addVertex(builder, matrixStack, v1.x(), v2.y(), v2.z(), tint, minU, minV);
        this.addVertex(builder, matrixStack, v2.x(), v2.y(), v2.z(), tint, maxU, minV);
        this.addVertex(builder, matrixStack, v2.x(), v2.y(), v1.z(), tint, maxU, maxV);
        this.addVertex(builder, matrixStack, v1.x(), v1.y(), v1.z(), tint, minU, maxV);
        this.addVertex(builder, matrixStack, v1.x(), v2.y(), v1.z(), tint, minU, minV);
        this.addVertex(builder, matrixStack, v2.x(), v2.y(), v1.z(), tint, maxU, minV);
        this.addVertex(builder, matrixStack, v2.x(), v1.y(), v1.z(), tint, maxU, maxV);
        this.addVertex(builder, matrixStack, v2.x(), v1.y(), v1.z(), tint, minU, maxV);
        this.addVertex(builder, matrixStack, v2.x(), v2.y(), v1.z(), tint, minU, minV);
        this.addVertex(builder, matrixStack, v2.x(), v2.y(), v2.z(), tint, maxU, minV);
        this.addVertex(builder, matrixStack, v2.x(), v1.y(), v2.z(), tint, maxU, maxV);
        this.addVertex(builder, matrixStack, v1.x(), v1.y(), v2.z(), tint, minU, maxV);
        this.addVertex(builder, matrixStack, v1.x(), v2.y(), v2.z(), tint, minU, minV);
        this.addVertex(builder, matrixStack, v1.x(), v2.y(), v1.z(), tint, maxU, minV);
        this.addVertex(builder, matrixStack, v1.x(), v1.y(), v1.z(), tint, maxU, maxV);
        this.addVertex(builder, matrixStack, v2.x(), v1.y(), v2.z(), tint, minU, maxV);
        this.addVertex(builder, matrixStack, v2.x(), v2.y(), v2.z(), tint, minU, minV);
        this.addVertex(builder, matrixStack, v1.x(), v2.y(), v2.z(), tint, maxU, minV);
        this.addVertex(builder, matrixStack, v1.x(), v1.y(), v2.z(), tint, maxU, maxV);
        matrixStack.popPose();
    }

    public void addVertex(final IVertexBuilder builder, final MatrixStack matrixStack, final float x, final float y, final float z, final java.awt.Color tint, final float u, final float v) {
        builder.vertex(matrixStack.last().pose(), x / 16.0f, y / 16.0f, z / 16.0f).color(tint.getRed() / 255.0f, tint.getGreen() / 255.0f, tint.getBlue() / 255.0f, 0.8f).uv(u, v).uv2(0, 240).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    static {
        FLUID_LOWER_POS = new Vector3f(2.25f, 2.0f, 2.25f);
        FLUID_UPPER_POS = new Vector3f(13.75f, 11.0f, 13.75f);
    }
}
