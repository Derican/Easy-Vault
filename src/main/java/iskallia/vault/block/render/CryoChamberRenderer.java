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

public class CryoChamberRenderer extends TileEntityRenderer<CryoChamberTileEntity> {
    public static final Minecraft mc;
    public static final ResourceLocation INFUSED_PLAYER_SKIN;
    public static final StatuePlayerModel<PlayerEntity> PLAYER_MODEL;
    private final Color[] colors;
    private int index;
    private boolean wait;
    private Color currentColor;
    private float currentRed;
    private float currentGreen;
    private float currentBlue;
    private final float colorChangeDelay = 3.0f;

    public CryoChamberRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        this.colors = new Color[]{Color.WHITE, Color.YELLOW, Color.MAGENTA, Color.GREEN};
        this.index = 0;
        this.wait = false;
        this.currentColor = Color.WHITE;
        this.currentRed = 1.0f;
        this.currentGreen = 1.0f;
        this.currentBlue = 1.0f;
    }

    public IVertexBuilder getPlayerVertexBuilder(final ResourceLocation skinTexture, final IRenderTypeBuffer buffer) {
        final RenderType renderType = CryoChamberRenderer.PLAYER_MODEL.renderType(skinTexture);
        return buffer.getBuffer(renderType);
    }

    public void render(final CryoChamberTileEntity tileEntity, final float partialTicks, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int combinedLight, final int combinedOverlay) {
        if (tileEntity.isInfusing()) {
            final float maxTime = (float) ModConfigs.CRYO_CHAMBER.getInfusionTime();
            final float scale = Math.min(tileEntity.getInfusionTimeRemaining() / maxTime, 0.85f);
            tileEntity.updateSkin();
            final ResourceLocation skinTexture = tileEntity.getSkin().getLocationSkin();
            final IVertexBuilder vertexBuilder = this.getPlayerVertexBuilder(skinTexture, buffer);
            this.renderPlayerModel(matrixStack, tileEntity, scale, 0.5f, vertexBuilder, combinedLight, combinedOverlay);
        } else if (tileEntity.isGrowingEternal()) {
            final float maxTime = (float) ModConfigs.CRYO_CHAMBER.getGrowEternalTime();
            final float scale = Math.min(1.0f - tileEntity.getGrowEternalTimeRemaining() / maxTime, 0.85f);
            final IVertexBuilder vertexBuilder2 = this.getPlayerVertexBuilder(CryoChamberRenderer.INFUSED_PLAYER_SKIN, buffer);
            this.renderPlayerModel(matrixStack, tileEntity, scale, 0.5f, vertexBuilder2, combinedLight, combinedOverlay);
        } else if (tileEntity.getEternalId() != null) {
            final EternalDataSnapshot snapshot = ClientEternalData.getSnapshot(tileEntity.getEternalId());
            if (snapshot != null && snapshot.getName() != null) {
                tileEntity.updateSkin();
                if (buffer instanceof IRenderTypeBuffer.Impl) {
                    ((IRenderTypeBuffer.Impl) buffer).endBatch();
                }
                if (!snapshot.isAlive()) {
                    ShaderUtil.useShader(ShaderUtil.GRAYSCALE_SHADER, () -> {
                        final int grayScaleFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "grayFactor");
                        ARBShaderObjects.glUniform1fARB(grayScaleFactor, 0.0f);
                        final float brightness = LightmapUtil.getLightmapBrightness(combinedLight);
                        final int brightnessFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "brightness");
                        ARBShaderObjects.glUniform1fARB(brightnessFactor, brightness);
                        return;
                    });
                }
                final ResourceLocation skinTexture2 = tileEntity.getSkin().getLocationSkin();
                final IVertexBuilder vertexBuilder2 = this.getPlayerVertexBuilder(skinTexture2, buffer);
                this.renderPlayerModel(matrixStack, tileEntity, 0.85f, 1.0f, vertexBuilder2, combinedLight, combinedOverlay);
                if (buffer instanceof IRenderTypeBuffer.Impl) {
                    ((IRenderTypeBuffer.Impl) buffer).endBatch();
                }
                if (!snapshot.isAlive()) {
                    ShaderUtil.releaseShader();
                }
            }
        } else if (tileEntity instanceof AncientCryoChamberTileEntity) {
            tileEntity.updateSkin();
            final ResourceLocation skinTexture3 = tileEntity.getSkin().getLocationSkin();
            final IVertexBuilder vertexBuilder3 = this.getPlayerVertexBuilder(skinTexture3, buffer);
            this.renderPlayerModel(matrixStack, tileEntity, 0.85f, 1.0f, vertexBuilder3, combinedLight, combinedOverlay);
            if (buffer instanceof IRenderTypeBuffer.Impl) {
                ((IRenderTypeBuffer.Impl) buffer).endBatch();
            }
        }
        this.renderLiquid(matrixStack, tileEntity, buffer, partialTicks);
        if (CryoChamberRenderer.mc.hitResult != null && CryoChamberRenderer.mc.hitResult.getType() == RayTraceResult.Type.BLOCK) {
            String eternalName = null;
            final EternalDataSnapshot snapshot2 = ClientEternalData.getSnapshot(tileEntity.getEternalId());
            if (snapshot2 != null && snapshot2.getName() != null) {
                eternalName = snapshot2.getName();
            }
            if (tileEntity instanceof AncientCryoChamberTileEntity) {
                eternalName = ((AncientCryoChamberTileEntity) tileEntity).getEternalName();
            }
            if (eternalName != null) {
                final BlockRayTraceResult result = (BlockRayTraceResult) CryoChamberRenderer.mc.hitResult;
                if (tileEntity.getBlockPos().equals(result.getBlockPos()) || tileEntity.getBlockPos().above().equals(result.getBlockPos())) {
                    this.renderLabel(matrixStack, buffer, combinedLight, new StringTextComponent(eternalName), -1, tileEntity.getLevel().getBlockState(result.getBlockPos()).getValue(CryoChamberBlock.HALF) == DoubleBlockHalf.UPPER);
                }
            }
        }
    }

    private void renderLabel(final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int lightLevel, final StringTextComponent text, final int color, final boolean topBlock) {
        final FontRenderer fontRenderer = CryoChamberRenderer.mc.font;
        matrixStack.pushPose();
        final float scale = 0.02f;
        final int opacity = 1711276032;
        final float offset = (float) (-fontRenderer.width(text) / 2);
        final Matrix4f matrix4f = matrixStack.last().pose();
        matrixStack.translate(0.5, 2.299999952316284, 0.5);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(CryoChamberRenderer.mc.getEntityRenderDispatcher().cameraOrientation());
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        fontRenderer.drawInBatch(text, offset, 0.0f, color, false, matrix4f, buffer, true, opacity, lightLevel);
        fontRenderer.drawInBatch(text, offset, 0.0f, -1, false, matrix4f, buffer, false, 0, lightLevel);
        matrixStack.popPose();
    }

    public void renderPlayerModel(final MatrixStack matrixStack, final CryoChamberTileEntity tileEntity, final float scale, final float alpha, final IVertexBuilder vertexBuilder, final int combinedLight, final int combinedOverlay) {
        final BlockState blockState = tileEntity.getBlockState();
        final Direction direction = blockState.getValue(CryoChamberBlock.FACING);
        matrixStack.pushPose();
        matrixStack.translate(0.5, 1.3, 0.5);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0f));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0f));
        CryoChamberRenderer.PLAYER_MODEL.body.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        CryoChamberRenderer.PLAYER_MODEL.leftLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        CryoChamberRenderer.PLAYER_MODEL.rightLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        CryoChamberRenderer.PLAYER_MODEL.leftArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        CryoChamberRenderer.PLAYER_MODEL.rightArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        CryoChamberRenderer.PLAYER_MODEL.jacket.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        CryoChamberRenderer.PLAYER_MODEL.leftPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        CryoChamberRenderer.PLAYER_MODEL.rightPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        CryoChamberRenderer.PLAYER_MODEL.leftSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.0, -0.6200000047683716);
        CryoChamberRenderer.PLAYER_MODEL.rightSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        matrixStack.popPose();
        CryoChamberRenderer.PLAYER_MODEL.hat.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        CryoChamberRenderer.PLAYER_MODEL.head.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, alpha);
        matrixStack.popPose();
    }

    private Quaternion getRotationFromDirection(final Direction direction) {
        switch (direction) {
            case NORTH:
            case SOUTH: {
                return Vector3f.YP.rotationDegrees(direction.getOpposite().toYRot());
            }
            default: {
                return Vector3f.YP.rotationDegrees(direction.toYRot());
            }
        }
    }

    private double[] getRootTranslation(final Direction direction) {
        switch (direction) {
            case SOUTH: {
                return new double[]{-1.0, 0.0, -1.0};
            }
            case WEST: {
                return new double[]{-1.0, 0.0, 0.0};
            }
            case EAST: {
                return new double[]{0.0, 0.0, -1.0};
            }
            default: {
                return new double[]{0.0, 0.0, 0.0};
            }
        }
    }

    private void renderLiquid(final MatrixStack matrixStack, final CryoChamberTileEntity tileEntity, final IRenderTypeBuffer buffer, final float partialTicks) {
        if (tileEntity.getMaxCores() == 0) {
            return;
        }
        final IVertexBuilder builder = buffer.getBuffer(RenderType.translucent());
        final TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(Fluids.WATER.getAttributes().getStillTexture());
        final BlockState blockState = tileEntity.getBlockState();
        final Direction direction = blockState.getValue(CryoChamberBlock.FACING);
        final float max = (float) tileEntity.getMaxCores();
        final float difference = tileEntity.getCoreCount() - tileEntity.lastCoreCount;
        tileEntity.lastCoreCount += difference * 0.02f;
        final float scale = tileEntity.lastCoreCount / max;
        this.updateIndex(CryoChamberRenderer.mc.player.tickCount);
        this.updateColor(partialTicks, tileEntity);
        final float r = this.currentColor.getRed() / 255.0f;
        final float g = this.currentColor.getGreen() / 255.0f;
        final float b = this.currentColor.getBlue() / 255.0f;
        final float minU = sprite.getU(0.0);
        final float maxU = sprite.getU(16.0);
        final float minV = sprite.getV(0.0);
        final float maxVBottom = sprite.getV((scale < 0.5) ? (scale * 2.0f * 16.0) : 16.0);
        final float maxVTop = sprite.getV((scale >= 0.5) ? ((scale * 2.0f - 1.0f) * 16.0) : 0.0);
        final float bottomHeight = (scale < 0.5f) ? (scale * 2.0f) : 1.0f;
        final float topHeight = (scale < 0.5f) ? 0.0f : Math.min(scale * 2.0f, 1.9f);
        matrixStack.pushPose();
        this.renderSides(matrixStack, builder, scale, r, g, b, minU, maxU, minV, maxVBottom, maxVTop, bottomHeight, topHeight, direction);
        this.renderTop(matrixStack, builder, scale, r, g, b, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), bottomHeight, topHeight);
        matrixStack.popPose();
    }

    private void renderTop(final MatrixStack matrixStack, final IVertexBuilder builder, final float scale, final float r, final float g, final float b, final float minU, final float maxU, final float minV, final float maxV, final float bottomHeight, final float topHeight) {
        this.addVertex(builder, matrixStack, this.p2f(1), (scale < 0.5f) ? bottomHeight : topHeight, this.p2f(1), minU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), (scale < 0.5f) ? bottomHeight : topHeight, this.p2f(9), maxU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), (scale < 0.5f) ? bottomHeight : topHeight, this.p2f(9), maxU, maxV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), (scale < 0.5f) ? bottomHeight : topHeight, this.p2f(1), minU, maxV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), (scale < 0.5f) ? bottomHeight : topHeight, this.p2f(9), minU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(4), (scale < 0.5f) ? bottomHeight : topHeight, this.p2f(15), maxU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(12), (scale < 0.5f) ? bottomHeight : topHeight, this.p2f(15), maxU, maxV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), (scale < 0.5f) ? bottomHeight : topHeight, this.p2f(9), minU, maxV, r, g, b, 1.0f);
    }

    private void renderSides(final MatrixStack matrixStack, final IVertexBuilder builder, final float scale, final float r, final float g, final float b, final float minU, final float maxU, final float minV, final float maxVBottom, final float maxVTop, final float bottomHeight, final float topHeight, final Direction direction) {
        final double[] translation = this.getRootTranslation(direction);
        matrixStack.mulPose(this.getRotationFromDirection(direction));
        matrixStack.translate(translation[0], translation[1], translation[2]);
        this.addVertex(builder, matrixStack, this.p2f(4), this.p2f(1), this.p2f(15), minU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(12), this.p2f(1), this.p2f(15), maxU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(12), bottomHeight, this.p2f(15), maxU, maxVBottom, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(4), bottomHeight, this.p2f(15), minU, maxVBottom, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), this.p2f(1), this.p2f(9), minU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(4), this.p2f(1), this.p2f(15), maxU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(4), bottomHeight, this.p2f(15), maxU, maxVBottom, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), bottomHeight, this.p2f(9), minU, maxVBottom, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(12), this.p2f(1), this.p2f(15), minU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), this.p2f(1), this.p2f(9), maxU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), bottomHeight, this.p2f(9), maxU, maxVBottom, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(12), bottomHeight, this.p2f(15), minU, maxVBottom, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), this.p2f(1), this.p2f(1), minU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), this.p2f(1), this.p2f(9), maxU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), bottomHeight, this.p2f(9), maxU, maxVBottom, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), bottomHeight, this.p2f(1), minU, maxVBottom, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), this.p2f(1), this.p2f(9), minU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), this.p2f(1), this.p2f(1), maxU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), bottomHeight, this.p2f(1), maxU, maxVBottom, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), bottomHeight, this.p2f(9), minU, maxVBottom, r, g, b, 1.0f);
        if (scale < 0.5f) {
            return;
        }
        this.addVertex(builder, matrixStack, this.p2f(4), this.p2f(16), this.p2f(15), minU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(12), this.p2f(16), this.p2f(15), maxU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(12), topHeight, this.p2f(15), maxU, maxVTop, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(4), topHeight, this.p2f(15), minU, maxVTop, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), this.p2f(16), this.p2f(9), minU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(4), this.p2f(16), this.p2f(15), maxU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(4), topHeight, this.p2f(15), maxU, maxVTop, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), topHeight, this.p2f(9), minU, maxVTop, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(12), this.p2f(16), this.p2f(15), minU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), this.p2f(16), this.p2f(9), maxU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), topHeight, this.p2f(9), maxU, maxVTop, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(12), topHeight, this.p2f(15), minU, maxVTop, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), this.p2f(16), this.p2f(1), minU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), this.p2f(16), this.p2f(9), maxU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), topHeight, this.p2f(9), maxU, maxVTop, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(1), topHeight, this.p2f(1), minU, maxVTop, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), this.p2f(16), this.p2f(9), minU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), this.p2f(16), this.p2f(1), maxU, minV, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), topHeight, this.p2f(1), maxU, maxVTop, r, g, b, 1.0f);
        this.addVertex(builder, matrixStack, this.p2f(15), topHeight, this.p2f(9), minU, maxVTop, r, g, b, 1.0f);
    }

    private void addVertex(final IVertexBuilder renderer, final MatrixStack stack, final float x, final float y, final float z, final float u, final float v, final float r, final float g, final float b, final float a) {
        renderer.vertex(stack.last().pose(), x, y, z).color(r, g, b, 0.5f).uv(u, v).uv2(0, 240).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    private float p2f(final int pixel) {
        return 0.0625f * pixel;
    }

    private void updateIndex(final int ticksExisted) {
        if (ticksExisted % 60.0f == 0.0f) {
            if (this.wait) {
                return;
            }
            this.wait = true;
            if (this.index++ == this.colors.length - 1) {
                this.index = 0;
            }
        } else {
            this.wait = false;
        }
    }

    private void updateColor(final float partialTicks, final CryoChamberTileEntity tileEntity) {
        if (tileEntity.getBlockState().getValue(CryoChamberBlock.CHAMBER_STATE) == CryoChamberBlock.ChamberState.RUSTY) {
            this.currentColor = new Color(139, 69, 19);
        } else {
            int nextIndex = this.index + 1;
            if (nextIndex == this.colors.length) {
                nextIndex = 0;
            }
            this.currentColor = this.getBlendedColor(this.colors[this.index], this.colors[nextIndex], partialTicks);
        }
    }

    private Color getBlendedColor(final Color prev, final Color next, final float partialTicks) {
        final float prevRed = prev.getRed() / 255.0f;
        final float prevGreen = prev.getGreen() / 255.0f;
        final float prevBlue = prev.getBlue() / 255.0f;
        final float nextRed = next.getRed() / 255.0f;
        final float nextGreen = next.getGreen() / 255.0f;
        final float nextBlue = next.getBlue() / 255.0f;
        final float percentage = 0.01f;
        final float transitionTime = 0.90000004f;
        final float red = Math.abs((nextRed - prevRed) * percentage / transitionTime * partialTicks);
        final float green = Math.abs((nextGreen - prevGreen) * percentage / transitionTime * partialTicks);
        final float blue = Math.abs((nextBlue - prevBlue) * percentage / transitionTime * partialTicks);
        this.currentRed = ((nextRed > prevRed) ? (this.currentRed + red) : (this.currentRed - red));
        this.currentGreen = ((nextGreen > prevGreen) ? (this.currentGreen + green) : (this.currentGreen - green));
        this.currentBlue = ((nextBlue > prevBlue) ? (this.currentBlue + blue) : (this.currentBlue - blue));
        this.currentRed = this.ensureRange(this.currentRed);
        this.currentGreen = this.ensureRange(this.currentGreen);
        this.currentBlue = this.ensureRange(this.currentBlue);
        return new Color(this.currentRed, this.currentGreen, this.currentBlue);
    }

    private float ensureRange(final float value) {
        return Math.min(Math.max(value, 0.0f), 1.0f);
    }

    static {
        mc = Minecraft.getInstance();
        INFUSED_PLAYER_SKIN = Vault.id("textures/entity/infusion_skin_white.png");
        PLAYER_MODEL = new StatuePlayerModel<PlayerEntity>(0.1f, true);
    }
}
