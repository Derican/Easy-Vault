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
    private static final Vector3f FLUID_LOWER_POS = new Vector3f(2.25F, 2.0F, 2.25F);
    private static final Vector3f FLUID_UPPER_POS = new Vector3f(13.75F, 11.0F, 13.75F);

    public FillableAltarRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    public void render(FillableAltarTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlayIn) {
        if (!tileEntity.initialized()) {
            return;
        }
        IVertexBuilder builder = buffer.getBuffer(RenderType.translucent());

        float progressPercentage = tileEntity.progressPercentage();

        if (progressPercentage > 0.0F) {
            float fluidMaxHeight = FLUID_UPPER_POS.y() - FLUID_LOWER_POS.y();


            Vector3f upperPos = new Vector3f(FLUID_UPPER_POS.x(), FLUID_LOWER_POS.y() + fluidMaxHeight * progressPercentage, FLUID_UPPER_POS.z());

            renderCuboid(builder, matrixStack, FLUID_LOWER_POS, upperPos, tileEntity.getFillColor());
            if (buffer instanceof IRenderTypeBuffer.Impl) {
                ((IRenderTypeBuffer.Impl) buffer).endBatch(RenderType.translucent());
            }
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.hitResult != null && minecraft.hitResult.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult result = (BlockRayTraceResult) minecraft.hitResult;
            if (tileEntity.getBlockPos().equals(result.getBlockPos())) {


                StringTextComponent progressText = tileEntity.isMaxedOut() ? (StringTextComponent) (new StringTextComponent("Right Click to Loot!")).setStyle(Style.EMPTY.withColor(Color.fromRgb(-1313364))) : (StringTextComponent) (new StringTextComponent(tileEntity.getCurrentProgress() + " / " + tileEntity.getMaxProgress() + " ")).append(tileEntity.getRequirementUnit());
                renderLabel(matrixStack, 0.5F, 2.3F, 0.5F, buffer, combinedLight, tileEntity.getRequirementName());
                renderLabel(matrixStack, 0.5F, 2.1F, 0.5F, buffer, combinedLight, (ITextComponent) progressText);
            }
        }
    }

    public void renderLabel(MatrixStack matrixStack, float x, float y, float z, IRenderTypeBuffer buffer, int lightLevel, ITextComponent text) {
        Minecraft minecraft = Minecraft.getInstance();

        FontRenderer fontRenderer = minecraft.font;


        matrixStack.pushPose();
        float scale = 0.02F;
        int opacity = 1711276032;
        float offset = (-fontRenderer.width((ITextProperties) text) / 2);
        Matrix4f matrix4f = matrixStack.last().pose();

        matrixStack.translate(x, y, z);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(minecraft.getEntityRenderDispatcher().cameraOrientation());
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));

        fontRenderer.drawInBatch(text, offset, 0.0F, -1, false, matrix4f, buffer, true, opacity, lightLevel);
        fontRenderer.drawInBatch(text, offset, 0.0F, -1, false, matrix4f, buffer, false, 0, lightLevel);
        matrixStack.popPose();
    }


    public void renderCuboid(IVertexBuilder builder, MatrixStack matrixStack, Vector3f v1, Vector3f v2, Color tint) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(Fluids.WATER.getAttributes().getStillTexture());

        float minU = sprite.getU(0.0D);
        float maxU = sprite.getU(16.0D);
        float minV = sprite.getV(0.0D);
        float maxV = sprite.getV(16.0D);

        matrixStack.pushPose();


        addVertex(builder, matrixStack, v1.x(), v2.y(), v1.z(), tint, minU, maxV);
        addVertex(builder, matrixStack, v1.x(), v2.y(), v2.z(), tint, minU, minV);
        addVertex(builder, matrixStack, v2.x(), v2.y(), v2.z(), tint, maxU, minV);
        addVertex(builder, matrixStack, v2.x(), v2.y(), v1.z(), tint, maxU, maxV);


        addVertex(builder, matrixStack, v1.x(), v1.y(), v1.z(), tint, minU, maxV);
        addVertex(builder, matrixStack, v1.x(), v2.y(), v1.z(), tint, minU, minV);
        addVertex(builder, matrixStack, v2.x(), v2.y(), v1.z(), tint, maxU, minV);
        addVertex(builder, matrixStack, v2.x(), v1.y(), v1.z(), tint, maxU, maxV);


        addVertex(builder, matrixStack, v2.x(), v1.y(), v1.z(), tint, minU, maxV);
        addVertex(builder, matrixStack, v2.x(), v2.y(), v1.z(), tint, minU, minV);
        addVertex(builder, matrixStack, v2.x(), v2.y(), v2.z(), tint, maxU, minV);
        addVertex(builder, matrixStack, v2.x(), v1.y(), v2.z(), tint, maxU, maxV);


        addVertex(builder, matrixStack, v1.x(), v1.y(), v2.z(), tint, minU, maxV);
        addVertex(builder, matrixStack, v1.x(), v2.y(), v2.z(), tint, minU, minV);
        addVertex(builder, matrixStack, v1.x(), v2.y(), v1.z(), tint, maxU, minV);
        addVertex(builder, matrixStack, v1.x(), v1.y(), v1.z(), tint, maxU, maxV);


        addVertex(builder, matrixStack, v2.x(), v1.y(), v2.z(), tint, minU, maxV);
        addVertex(builder, matrixStack, v2.x(), v2.y(), v2.z(), tint, minU, minV);
        addVertex(builder, matrixStack, v1.x(), v2.y(), v2.z(), tint, maxU, minV);
        addVertex(builder, matrixStack, v1.x(), v1.y(), v2.z(), tint, maxU, maxV);

        matrixStack.popPose();
    }

    public void addVertex(IVertexBuilder builder, MatrixStack matrixStack, float x, float y, float z, Color tint, float u, float v) {
        java.awt.Color tint1 = new java.awt.Color(tint.getValue());
        builder.vertex(matrixStack.last().pose(), x / 16.0F, y / 16.0F, z / 16.0F)
                .color(tint1.getRed() / 255.0F, tint1.getGreen() / 255.0F, tint1.getBlue() / 255.0F, 0.8F)
                .uv(u, v)
                .uv2(0, 240)
                .normal(1.0F, 0.0F, 0.0F)
                .endVertex();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\render\FillableAltarRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */