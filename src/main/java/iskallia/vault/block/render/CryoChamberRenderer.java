package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.Vault;
import iskallia.vault.block.CryoChamberBlock;
import iskallia.vault.block.entity.AncientCryoChamberTileEntity;
import iskallia.vault.block.entity.CryoChamberTileEntity;
import iskallia.vault.client.ClientEternalData;
import iskallia.vault.client.util.LightmapUtil;
import iskallia.vault.client.util.ShaderUtil;
import iskallia.vault.entity.eternal.EternalDataSnapshot;
import iskallia.vault.entity.model.StatuePlayerModel;
import iskallia.vault.init.ModConfigs;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.state.Property;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.ARBShaderObjects;

import java.awt.*;


public class CryoChamberRenderer
        extends TileEntityRenderer<CryoChamberTileEntity> {
    public static final Minecraft mc = Minecraft.getInstance();
    public static final ResourceLocation INFUSED_PLAYER_SKIN = Vault.id("textures/entity/infusion_skin_white.png");
    public static final StatuePlayerModel<PlayerEntity> PLAYER_MODEL = new StatuePlayerModel(0.1F, true);
    private final Color[] colors;
    private int index;
    private boolean wait;
    private Color currentColor;
    private float currentRed;
    private float currentGreen;
    private float currentBlue;
    private final float colorChangeDelay = 3.0F;

    public CryoChamberRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);


        this.colors = new Color[]{Color.WHITE, Color.YELLOW, Color.MAGENTA, Color.GREEN};


        this.index = 0;
        this.wait = false;

        this.currentColor = Color.WHITE;
        this.currentRed = 1.0F;
        this.currentGreen = 1.0F;
        this.currentBlue = 1.0F;
//        this.colorChangeDelay = 3.0F;
    }

    public IVertexBuilder getPlayerVertexBuilder(ResourceLocation skinTexture, IRenderTypeBuffer buffer) {
        RenderType renderType = PLAYER_MODEL.renderType(skinTexture);
        return buffer.getBuffer(renderType);
    }

    public void render(CryoChamberTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (tileEntity.isInfusing()) {
            float maxTime = ModConfigs.CRYO_CHAMBER.getInfusionTime();
            float scale = Math.min(tileEntity.getInfusionTimeRemaining() / maxTime, 0.85F);
            tileEntity.updateSkin();
            ResourceLocation skinTexture = tileEntity.getSkin().getLocationSkin();
            IVertexBuilder vertexBuilder = getPlayerVertexBuilder(skinTexture, buffer);
            renderPlayerModel(matrixStack, tileEntity, scale, 0.5F, vertexBuilder, combinedLight, combinedOverlay);
        } else if (tileEntity.isGrowingEternal()) {
            float maxTime = ModConfigs.CRYO_CHAMBER.getGrowEternalTime();
            float scale = Math.min(1.0F - tileEntity.getGrowEternalTimeRemaining() / maxTime, 0.85F);
            IVertexBuilder vertexBuilder = getPlayerVertexBuilder(INFUSED_PLAYER_SKIN, buffer);
            renderPlayerModel(matrixStack, tileEntity, scale, 0.5F, vertexBuilder, combinedLight, combinedOverlay);
        } else if (tileEntity.getEternalId() != null) {
            EternalDataSnapshot snapshot = ClientEternalData.getSnapshot(tileEntity.getEternalId());
            if (snapshot != null && snapshot.getName() != null) {
                tileEntity.updateSkin();
                if (buffer instanceof IRenderTypeBuffer.Impl) ((IRenderTypeBuffer.Impl) buffer).endBatch();
                if (!snapshot.isAlive()) ShaderUtil.useShader(ShaderUtil.GRAYSCALE_SHADER, () -> {
                    int grayScaleFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "grayFactor");
                    ARBShaderObjects.glUniform1fARB(grayScaleFactor, 0.0F);
                    float brightness = LightmapUtil.getLightmapBrightness(combinedLight);
                    int brightnessFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "brightness");
                    ARBShaderObjects.glUniform1fARB(brightnessFactor, brightness);
                });
                ResourceLocation skinTexture = tileEntity.getSkin().getLocationSkin();
                IVertexBuilder vertexBuilder = getPlayerVertexBuilder(skinTexture, buffer);
                renderPlayerModel(matrixStack, tileEntity, 0.85F, 1.0F, vertexBuilder, combinedLight, combinedOverlay);
                if (buffer instanceof IRenderTypeBuffer.Impl) ((IRenderTypeBuffer.Impl) buffer).endBatch();
                if (!snapshot.isAlive()) ShaderUtil.releaseShader();
            }
        } else if (tileEntity instanceof AncientCryoChamberTileEntity) {
            tileEntity.updateSkin();
            ResourceLocation skinTexture = tileEntity.getSkin().getLocationSkin();
            IVertexBuilder vertexBuilder = getPlayerVertexBuilder(skinTexture, buffer);
            renderPlayerModel(matrixStack, tileEntity, 0.85F, 1.0F, vertexBuilder, combinedLight, combinedOverlay);
            if (buffer instanceof IRenderTypeBuffer.Impl) ((IRenderTypeBuffer.Impl) buffer).endBatch();
        }
        renderLiquid(matrixStack, tileEntity, buffer, partialTicks);
        if (mc.hitResult != null && mc.hitResult.getType() == RayTraceResult.Type.BLOCK) {
            String eternalName = null;
            EternalDataSnapshot snapshot = ClientEternalData.getSnapshot(tileEntity.getEternalId());
            if (snapshot != null && snapshot.getName() != null) eternalName = snapshot.getName();
            if (tileEntity instanceof AncientCryoChamberTileEntity)
                eternalName = ((AncientCryoChamberTileEntity) tileEntity).getEternalName();
            if (eternalName != null) {
                BlockRayTraceResult result = (BlockRayTraceResult) mc.hitResult;
                if (tileEntity.getBlockPos().equals(result.getBlockPos()) || tileEntity.getBlockPos().above().equals(result.getBlockPos()))
                    renderLabel(matrixStack, buffer, combinedLight, new StringTextComponent(eternalName), -1, (tileEntity.getLevel().getBlockState(result.getBlockPos()).getValue((Property) CryoChamberBlock.HALF) == DoubleBlockHalf.UPPER));
            }
        }
    }

    private void renderLabel(MatrixStack matrixStack, IRenderTypeBuffer buffer, int lightLevel, StringTextComponent text, int color, boolean topBlock) {
        FontRenderer fontRenderer = mc.font;
        matrixStack.pushPose();
        float scale = 0.02F;
        int opacity = 1711276032;
        float offset = (-fontRenderer.width((ITextProperties) text) / 2);
        Matrix4f matrix4f = matrixStack.last().pose();
        matrixStack.translate(0.5D, 2.299999952316284D, 0.5D);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        fontRenderer.drawInBatch((ITextComponent) text, offset, 0.0F, color, false, matrix4f, buffer, true, opacity, lightLevel);
        fontRenderer.drawInBatch((ITextComponent) text, offset, 0.0F, -1, false, matrix4f, buffer, false, 0, lightLevel);
        matrixStack.popPose();
    }

    public void renderPlayerModel(MatrixStack matrixStack, CryoChamberTileEntity tileEntity, float scale, float alpha, IVertexBuilder vertexBuilder, int combinedLight, int combinedOverlay) {
        BlockState blockState = tileEntity.getBlockState();
        Direction direction = (Direction) blockState.getValue((Property) CryoChamberBlock.FACING);
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1.3D, 0.5D);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0F));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        PLAYER_MODEL.body.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        PLAYER_MODEL.leftLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        PLAYER_MODEL.rightLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        PLAYER_MODEL.leftArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        PLAYER_MODEL.rightArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        PLAYER_MODEL.jacket.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        PLAYER_MODEL.leftPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        PLAYER_MODEL.rightPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        PLAYER_MODEL.leftSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.0D, -0.6200000047683716D);
        PLAYER_MODEL.rightSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        matrixStack.popPose();
        PLAYER_MODEL.hat.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        PLAYER_MODEL.head.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, alpha);
        matrixStack.popPose();
    }

    private Quaternion getRotationFromDirection(Direction direction) {
        switch (direction) {
            case NORTH:
            case SOUTH:
                return Vector3f.YP.rotationDegrees(direction.getOpposite().toYRot());
        }
        return Vector3f.YP.rotationDegrees(direction.toYRot());
    }

    private void updateIndex(int ticksExisted) {
        if (ticksExisted % 60.0F == 0.0F) {
            if (this.wait)
                return;
            this.wait = true;
            if (this.index++ == this.colors.length - 1) this.index = 0;
        } else {
            this.wait = false;
        }
    }

    private double[] getRootTranslation(Direction direction) {
        switch (direction) {
            case SOUTH:
                return new double[]{-1.0D, 0.0D, -1.0D};
            case WEST:
                return new double[]{-1.0D, 0.0D, 0.0D};
            case EAST:
                return new double[]{0.0D, 0.0D, -1.0D};
        }
        return new double[]{0.0D, 0.0D, 0.0D};
    }

    private void renderLiquid(MatrixStack matrixStack, CryoChamberTileEntity tileEntity, IRenderTypeBuffer buffer, float partialTicks) {
        if (tileEntity.getMaxCores() == 0) return;
        IVertexBuilder builder = buffer.getBuffer(RenderType.translucent());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(Fluids.WATER.getAttributes().getStillTexture());
        BlockState blockState = tileEntity.getBlockState();
        Direction direction = (Direction) blockState.getValue((Property) CryoChamberBlock.FACING);
        float max = tileEntity.getMaxCores();
        float difference = tileEntity.getCoreCount() - tileEntity.lastCoreCount;
        tileEntity.lastCoreCount += difference * 0.02F;
        float scale = tileEntity.lastCoreCount / max;
        updateIndex(mc.player.tickCount);
        updateColor(partialTicks, tileEntity);
        float r = this.currentColor.getRed() / 255.0F;
        float g = this.currentColor.getGreen() / 255.0F;
        float b = this.currentColor.getBlue() / 255.0F;
        float minU = sprite.getU(0.0D);
        float maxU = sprite.getU(16.0D);
        float minV = sprite.getV(0.0D);
        float maxVBottom = sprite.getV((scale < 0.5D) ? ((scale * 2.0F) * 16.0D) : 16.0D);
        float maxVTop = sprite.getV((scale >= 0.5D) ? ((scale * 2.0F - 1.0F) * 16.0D) : 0.0D);
        float bottomHeight = (scale < 0.5F) ? (scale * 2.0F) : 1.0F;
        float topHeight = (scale < 0.5F) ? 0.0F : Math.min(scale * 2.0F, 1.9F);
        matrixStack.pushPose();
        renderSides(matrixStack, builder, scale, r, g, b, minU, maxU, minV, maxVBottom, maxVTop, bottomHeight, topHeight, direction);
        renderTop(matrixStack, builder, scale, r, g, b, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), bottomHeight, topHeight);
        matrixStack.popPose();
    }

    private void renderTop(MatrixStack matrixStack, IVertexBuilder builder, float scale, float r, float g, float b, float minU, float maxU, float minV, float maxV, float bottomHeight, float topHeight) {
        addVertex(builder, matrixStack, p2f(1), (scale < 0.5F) ? bottomHeight : topHeight, p2f(1), minU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), (scale < 0.5F) ? bottomHeight : topHeight, p2f(9), maxU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), (scale < 0.5F) ? bottomHeight : topHeight, p2f(9), maxU, maxV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), (scale < 0.5F) ? bottomHeight : topHeight, p2f(1), minU, maxV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), (scale < 0.5F) ? bottomHeight : topHeight, p2f(9), minU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(4), (scale < 0.5F) ? bottomHeight : topHeight, p2f(15), maxU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(12), (scale < 0.5F) ? bottomHeight : topHeight, p2f(15), maxU, maxV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), (scale < 0.5F) ? bottomHeight : topHeight, p2f(9), minU, maxV, r, g, b, 1.0F);
    }

    private void renderSides(MatrixStack matrixStack, IVertexBuilder builder, float scale, float r, float g, float b, float minU, float maxU, float minV, float maxVBottom, float maxVTop, float bottomHeight, float topHeight, Direction direction) {
        double[] translation = getRootTranslation(direction);
        matrixStack.mulPose(getRotationFromDirection(direction));
        matrixStack.translate(translation[0], translation[1], translation[2]);
        addVertex(builder, matrixStack, p2f(4), p2f(1), p2f(15), minU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(12), p2f(1), p2f(15), maxU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(12), bottomHeight, p2f(15), maxU, maxVBottom, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(4), bottomHeight, p2f(15), minU, maxVBottom, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), p2f(1), p2f(9), minU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(4), p2f(1), p2f(15), maxU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(4), bottomHeight, p2f(15), maxU, maxVBottom, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), bottomHeight, p2f(9), minU, maxVBottom, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(12), p2f(1), p2f(15), minU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), p2f(1), p2f(9), maxU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), bottomHeight, p2f(9), maxU, maxVBottom, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(12), bottomHeight, p2f(15), minU, maxVBottom, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), p2f(1), p2f(1), minU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), p2f(1), p2f(9), maxU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), bottomHeight, p2f(9), maxU, maxVBottom, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), bottomHeight, p2f(1), minU, maxVBottom, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), p2f(1), p2f(9), minU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), p2f(1), p2f(1), maxU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), bottomHeight, p2f(1), maxU, maxVBottom, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), bottomHeight, p2f(9), minU, maxVBottom, r, g, b, 1.0F);
        if (scale < 0.5F) return;
        addVertex(builder, matrixStack, p2f(4), p2f(16), p2f(15), minU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(12), p2f(16), p2f(15), maxU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(12), topHeight, p2f(15), maxU, maxVTop, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(4), topHeight, p2f(15), minU, maxVTop, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), p2f(16), p2f(9), minU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(4), p2f(16), p2f(15), maxU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(4), topHeight, p2f(15), maxU, maxVTop, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), topHeight, p2f(9), minU, maxVTop, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(12), p2f(16), p2f(15), minU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), p2f(16), p2f(9), maxU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), topHeight, p2f(9), maxU, maxVTop, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(12), topHeight, p2f(15), minU, maxVTop, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), p2f(16), p2f(1), minU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), p2f(16), p2f(9), maxU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), topHeight, p2f(9), maxU, maxVTop, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(1), topHeight, p2f(1), minU, maxVTop, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), p2f(16), p2f(9), minU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), p2f(16), p2f(1), maxU, minV, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), topHeight, p2f(1), maxU, maxVTop, r, g, b, 1.0F);
        addVertex(builder, matrixStack, p2f(15), topHeight, p2f(9), minU, maxVTop, r, g, b, 1.0F);
    }

    private void addVertex(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
        renderer.vertex(stack.last().pose(), x, y, z).color(r, g, b, 0.5F).uv(u, v).uv2(0, 240).normal(1.0F, 0.0F, 0.0F).endVertex();
    }

    private float p2f(int pixel) {
        return 0.0625F * pixel;
    }

    private void updateColor(float partialTicks, CryoChamberTileEntity tileEntity) {
        if (tileEntity.getBlockState().getValue((Property) CryoChamberBlock.CHAMBER_STATE) == CryoChamberBlock.ChamberState.RUSTY) {
            this.currentColor = new Color(139, 69, 19);
        } else {
            int nextIndex = this.index + 1;
            if (nextIndex == this.colors.length) nextIndex = 0;
            this.currentColor = getBlendedColor(this.colors[this.index], this.colors[nextIndex], partialTicks);
        }
    }


    private Color getBlendedColor(Color prev, Color next, float partialTicks) {
        float prevRed = prev.getRed() / 255.0F;
        float prevGreen = prev.getGreen() / 255.0F;
        float prevBlue = prev.getBlue() / 255.0F;

        float nextRed = next.getRed() / 255.0F;
        float nextGreen = next.getGreen() / 255.0F;
        float nextBlue = next.getBlue() / 255.0F;

        float percentage = 0.01F;
        float transitionTime = 0.90000004F;
        float red = Math.abs((nextRed - prevRed) * percentage / transitionTime * partialTicks);
        float green = Math.abs((nextGreen - prevGreen) * percentage / transitionTime * partialTicks);
        float blue = Math.abs((nextBlue - prevBlue) * percentage / transitionTime * partialTicks);

        this.currentRed = (nextRed > prevRed) ? (this.currentRed + red) : (this.currentRed - red);
        this.currentGreen = (nextGreen > prevGreen) ? (this.currentGreen + green) : (this.currentGreen - green);
        this.currentBlue = (nextBlue > prevBlue) ? (this.currentBlue + blue) : (this.currentBlue - blue);

        this.currentRed = ensureRange(this.currentRed);
        this.currentGreen = ensureRange(this.currentGreen);
        this.currentBlue = ensureRange(this.currentBlue);

        return new Color(this.currentRed, this.currentGreen, this.currentBlue);
    }

    private float ensureRange(float value) {
        return Math.min(Math.max(value, 0.0F), 1.0F);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\render\CryoChamberRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */