package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.Vault;
import iskallia.vault.block.LootStatueBlock;
import iskallia.vault.block.entity.LootStatueTileEntity;
import iskallia.vault.block.entity.TrophyStatueTileEntity;
import iskallia.vault.block.model.OmegaStatueModel;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.util.LightmapUtil;
import iskallia.vault.client.util.ShaderUtil;
import iskallia.vault.client.util.VBOUtil;
import iskallia.vault.entity.model.StatuePlayerModel;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.StatueType;
import iskallia.vault.util.WeekKey;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.lwjgl.opengl.ARBShaderObjects;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

public class LootStatueRenderer extends TileEntityRenderer<LootStatueTileEntity> {
    protected static final StatuePlayerModel<PlayerEntity> PLAYER_MODEL;
    protected static final OmegaStatueModel OMEGA_STATUE_MODEL;
    protected static Map<Integer, VertexBuffer> lightVBOMap;
    private final Minecraft mc;

    public LootStatueRenderer(final TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
        this.mc = Minecraft.getInstance();
    }

    public void render(final LootStatueTileEntity tileEntity, final float partialTicks, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int combinedLight, final int combinedOverlay) {
        final BlockState blockState = tileEntity.getBlockState();
        final Direction direction = blockState.getValue(LootStatueBlock.FACING);
        final Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        if (tileEntity.getStatueType() == StatueType.OMEGA && tileEntity.isMaster()) {
            this.renderOmegaStatueCrystals(tileEntity, matrixStack, buffer, combinedLight, combinedOverlay);
            if (tileEntity.getChipCount() > 0) {
                final ClientPlayerEntity player = mc.player;
                final int lightLevel = this.getLightAtPos(tileEntity.getLevel(), tileEntity.getBlockPos().above());
                this.renderItemWithLabel(new ItemStack(ModItems.ACCELERATION_CHIP), this.getTranslation(direction), Vector3f.YP.rotationDegrees(((direction == Direction.EAST || direction == Direction.WEST) ? 270 : 180) - player.yRot), matrixStack, buffer, combinedOverlay, lightLevel, direction, new StringTextComponent("" + tileEntity.getChipCount()), -1);
            }
        } else if (tileEntity.getStatueType() == StatueType.OMEGA_VARIANT) {
            if (tileEntity.getChipCount() > 0) {
                final ClientPlayerEntity player = mc.player;
                final int lightLevel = this.getLightAtPos(tileEntity.getLevel(), tileEntity.getBlockPos().above());
                this.renderItemWithLabel(new ItemStack(ModItems.ACCELERATION_CHIP), this.getVariantTranslation(direction), Vector3f.YP.rotationDegrees(((direction == Direction.EAST || direction == Direction.WEST) ? 270 : 180) - player.yRot), matrixStack, buffer, combinedOverlay, lightLevel, direction, new StringTextComponent(String.valueOf(tileEntity.getChipCount())), -1);
            }
            final ItemStack loot = tileEntity.getLootItem();
            if (!loot.isEmpty()) {
                matrixStack.pushPose();
                matrixStack.translate(0.5, 0.4, 0.5);
                matrixStack.translate(direction.getStepX() * -0.2, 0.0, direction.getStepZ() * -0.2);
                matrixStack.scale(1.6f, 1.6f, 1.6f);
                final IBakedModel ibakedmodel = mc.getItemRenderer().getModel(loot, null, null);
                mc.getItemRenderer().render(loot, ItemCameraTransforms.TransformType.GROUND, false, matrixStack, buffer, combinedLight, combinedOverlay, ibakedmodel);
                matrixStack.popPose();
            }
        }
        final String latestNickname = tileEntity.getSkin().getLatestNickname();
        if (StringUtils.isNullOrEmpty(latestNickname)) {
            return;
        }
        this.drawPlayerModel(matrixStack, buffer, tileEntity, combinedLight, combinedOverlay);
        final StatueType statueType = tileEntity.getStatueType();
        if (statueType == StatueType.GIFT_MEGA) {
            this.drawStatueBowHat(matrixStack, buffer, direction, combinedLight, combinedOverlay);
        }
        if (statueType == StatueType.TROPHY) {
            this.drawRecordDisplay(matrixStack, buffer, direction, tileEntity, combinedLight, combinedOverlay);
        }
        if (statueType == StatueType.OMEGA_VARIANT) {
            this.drawStatueNameplate(matrixStack, buffer, latestNickname, direction, tileEntity, combinedLight, combinedOverlay);
        }
        if (mc.hitResult != null && mc.hitResult.getType() == RayTraceResult.Type.BLOCK) {
            final BlockRayTraceResult result = (BlockRayTraceResult) mc.hitResult;
            if (tileEntity.getBlockPos().equals(result.getBlockPos())) {
                ITextComponent text = new StringTextComponent(latestNickname).withStyle(TextFormatting.WHITE);
                if (statueType.hasLimitedItems() && tileEntity.getItemsRemaining() <= 0) {
                    text = new StringTextComponent("\u2620 ").withStyle(TextFormatting.RED).append(text);
                }
                this.renderLabel(matrixStack, buffer, combinedLight, text, -1);
            }
        }
    }

    private void drawStatueNameplate(final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final String latestNickname, final Direction direction, final LootStatueTileEntity tileEntity, final int combinedLight, final int combinedOverlay) {
        final IReorderingProcessor text = new StringTextComponent(latestNickname).withStyle(TextFormatting.BLACK).getVisualOrderText();
        final FontRenderer fr = this.renderer.getFont();
        final int xOffset = fr.width(text);
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.35, 0.5);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0f));
        matrixStack.translate(0.0, 0.0, 0.51);
        matrixStack.scale(0.01f, -0.01f, 0.01f);
        fr.drawInBatch(text, -xOffset / 2.0f, 0.0f, -16777216, false, matrixStack.last().pose(), buffer, false, 0, combinedLight);
        matrixStack.popPose();
    }

    private void drawRecordDisplay(final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final Direction direction, final LootStatueTileEntity tileEntity, final int combinedLight, final int combinedOverlay) {
        if (!(tileEntity instanceof TrophyStatueTileEntity)) {
            return;
        }
        final TrophyStatueTileEntity trophyTile = (TrophyStatueTileEntity) tileEntity;
        final WeekKey week = trophyTile.getWeek();
        final PlayerVaultStatsData.PlayerRecordEntry recordEntry = trophyTile.getRecordEntry();
        final FontRenderer fr = this.renderer.getFont();
        LocalDateTime ldt = LocalDateTime.now();
        ldt = ldt.with(IsoFields.WEEK_BASED_YEAR, week.getYear()).with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week.getWeek()).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        final String from = ldt.getDayOfMonth() + "." + ldt.getMonthValue() + "." + ldt.getYear() + " -";
        ldt = ldt.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        final String to = ldt.getDayOfMonth() + "." + ldt.getMonthValue() + "." + ldt.getYear();
        final IReorderingProcessor fromCmp = new StringTextComponent(from).getVisualOrderText();
        final IReorderingProcessor toCmp = new StringTextComponent(to).getVisualOrderText();
        final IReorderingProcessor timeStr = new StringTextComponent(UIHelper.formatTimeString(recordEntry.getTickCount())).getVisualOrderText();
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0f));
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.24, 0.22);
        matrixStack.scale(0.0055f, -0.0055f, 0.0055f);
        int xOffset = fr.width(fromCmp);
        fr.drawInBatch(fromCmp, -xOffset / 2.0f, 0.0f, -16777216, false, matrixStack.last().pose(), buffer, false, 0, combinedLight);
        xOffset = fr.width(toCmp);
        fr.drawInBatch(toCmp, -xOffset / 2.0f, 10.0f, -16777216, false, matrixStack.last().pose(), buffer, false, 0, combinedLight);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.1, 0.19);
        matrixStack.scale(0.008f, -0.008f, 0.008f);
        xOffset = fr.width(timeStr);
        fr.drawInBatch(timeStr, -xOffset / 2.0f, 0.0f, -16777216, false, matrixStack.last().pose(), buffer, false, 0, combinedLight);
        matrixStack.popPose();
        matrixStack.popPose();
    }

    private void drawStatueBowHat(final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final Direction direction, final int combinedLight, final int combinedOverlay) {
        final float hatScale = 3.0f;
        matrixStack.pushPose();
        matrixStack.translate(0.5, 1.1, 0.5);
        matrixStack.scale(hatScale, hatScale, hatScale);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0f));
        final ItemStack stack = new ItemStack(ModBlocks.BOW_HAT);
        final IBakedModel ibakedmodel = this.mc.getItemRenderer().getModel(stack, null, null);
        this.mc.getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, combinedLight, combinedOverlay, ibakedmodel);
        matrixStack.popPose();
    }

    private void drawPlayerModel(final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final LootStatueTileEntity tileEntity, final int combinedLight, final int combinedOverlay) {
        final BlockState blockState = tileEntity.getBlockState();
        final Direction direction = blockState.getValue(LootStatueBlock.FACING);
        final StatueType statueType = tileEntity.getStatueType();
        final ResourceLocation skinLocation = tileEntity.getSkin().getLocationSkin();
        final RenderType renderType = LootStatueRenderer.PLAYER_MODEL.renderType(skinLocation);
        final IVertexBuilder vertexBuilder = buffer.getBuffer(renderType);
        float scale = 0.4f;
        float headScale = 1.75f;
        final float yOffset = statueType.getPlayerRenderYOffset();
        float statueOffset = 0.0f;
        if (statueType.doGrayscaleShader()) {
            ShaderUtil.useShader(ShaderUtil.GRAYSCALE_SHADER, () -> {
                final float factor = tileEntity.getItemsRemaining() / (float) tileEntity.getTotalItems();
                final int grayScaleFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "grayFactor");
                ARBShaderObjects.glUniform1fARB(grayScaleFactor, factor);
                final float brightness = LightmapUtil.getLightmapBrightness(combinedLight);
                final int brightnessFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "brightness");
                ARBShaderObjects.glUniform1fARB(brightnessFactor, brightness);
                return;
            });
        }
        matrixStack.pushPose();
        if (statueType == StatueType.OMEGA) {
            final float playerScale = tileEntity.getPlayerScale();
            matrixStack.translate(0.0, 1.0f + playerScale, 0.0);
            scale += playerScale;
        }
        if (statueType == StatueType.OMEGA_VARIANT) {
            matrixStack.translate(0.0, 1.5499999523162842, 0.0);
            scale = 1.3f;
            headScale = 1.0f;
            statueOffset = 0.2f;
        }
        if (statueType == StatueType.TROPHY) {
            scale -= (float) 0.04;
        }
        matrixStack.translate(0.5, yOffset, 0.5);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0f));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0f));
        matrixStack.translate(0.0, 0.0, statueOffset);
        matrixStack.scale(scale, scale, scale);
        LootStatueRenderer.PLAYER_MODEL.body.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        LootStatueRenderer.PLAYER_MODEL.leftLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        LootStatueRenderer.PLAYER_MODEL.rightLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        LootStatueRenderer.PLAYER_MODEL.leftArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        LootStatueRenderer.PLAYER_MODEL.rightArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        LootStatueRenderer.PLAYER_MODEL.jacket.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        LootStatueRenderer.PLAYER_MODEL.leftPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        LootStatueRenderer.PLAYER_MODEL.rightPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        LootStatueRenderer.PLAYER_MODEL.leftSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.0, -0.6200000047683716);
        LootStatueRenderer.PLAYER_MODEL.rightSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.popPose();
        matrixStack.scale(headScale, headScale, headScale);
        LootStatueRenderer.PLAYER_MODEL.hat.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        LootStatueRenderer.PLAYER_MODEL.head.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.popPose();
        if (buffer instanceof IRenderTypeBuffer.Impl) {
            ((IRenderTypeBuffer.Impl) buffer).endBatch(renderType);
        }
        if (statueType.doGrayscaleShader()) {
            ShaderUtil.releaseShader();
        }
    }

    private void renderOmegaStatueCrystals(final LootStatueTileEntity tileEntity, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int combinedLight, final int combinedOverlay) {
        final RenderType omegaType = LootStatueRenderer.OMEGA_STATUE_MODEL.renderType(Vault.id("textures/block/mega_statue3.png"));
        final VertexBuffer vbo = LootStatueRenderer.lightVBOMap.computeIfAbsent(combinedLight, lightLvl -> VBOUtil.batch(LootStatueRenderer.OMEGA_STATUE_MODEL, omegaType, lightLvl, OverlayTexture.NO_OVERLAY));
        matrixStack.pushPose();
        matrixStack.translate(0.5, 1.5, 0.5);
        final BlockState blockState = tileEntity.getBlockState();
        final Direction direction = blockState.getValue(LootStatueBlock.FACING);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0f));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0f));
        omegaType.setupRenderState();
        vbo.bind();
        omegaType.format().setupBufferState(0L);
        vbo.draw(matrixStack.last().pose(), omegaType.mode());
        omegaType.format().clearBufferState();
        VertexBuffer.unbind();
        omegaType.clearRenderState();
        matrixStack.popPose();
    }

    private void renderLabel(final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int lightLevel, final ITextComponent text, final int color) {
        final FontRenderer fontRenderer = this.mc.font;
        matrixStack.pushPose();
        final float scale = 0.02f;
        final int opacity = 1711276032;
        final float offset = (float) (-fontRenderer.width(text) / 2);
        final Matrix4f matrix4f = matrixStack.last().pose();
        matrixStack.translate(0.5, 1.7000000476837158, 0.5);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(this.mc.getEntityRenderDispatcher().cameraOrientation());
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        fontRenderer.drawInBatch(text, offset, 0.0f, color, false, matrix4f, buffer, true, opacity, lightLevel);
        fontRenderer.drawInBatch(text, offset, 0.0f, -1, false, matrix4f, buffer, false, 0, lightLevel);
        matrixStack.popPose();
    }

    private void renderItemWithLabel(final ItemStack stack, final double[] translation, final Quaternion rotation, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int combinedOverlay, final int lightLevel, final Direction direction, final StringTextComponent text, final int color) {
        matrixStack.pushPose();
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + ((direction == Direction.NORTH || direction == Direction.EAST) ? 0 : 180)));
        matrixStack.translate(translation[0], translation[1], translation[2]);
        matrixStack.mulPose(rotation);
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        final IBakedModel ibakedmodel = this.mc.getItemRenderer().getModel(stack, null, null);
        this.mc.getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, lightLevel, combinedOverlay, ibakedmodel);
        final float scale = -0.025f;
        final float offset = (float) (-Minecraft.getInstance().font.width(text) / 2);
        matrixStack.translate(0.0, 0.75, 0.0);
        matrixStack.scale(scale, scale, scale);
        final Matrix4f matrix4f = matrixStack.last().pose();
        final int opacity = 1711276032;
        Minecraft.getInstance().font.drawInBatch(text, offset, 0.0f, color, false, matrix4f, buffer, true, opacity, lightLevel);
        Minecraft.getInstance().font.drawInBatch(text, offset, 0.0f, -1, false, matrix4f, buffer, false, 0, lightLevel);
        matrixStack.popPose();
    }

    private int getLightAtPos(final World world, final BlockPos pos) {
        final int blockLight = world.getBrightness(LightType.BLOCK, pos);
        final int skyLight = world.getBrightness(LightType.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }

    private double[] getTranslation(final Direction direction) {
        switch (direction) {
            case NORTH: {
                return new double[]{-0.5, 1.0, -1.5};
            }
            case EAST: {
                return new double[]{-0.5, 1.0, -0.5};
            }
            case WEST: {
                return new double[]{-0.5, 1.0, 1.5};
            }
            default: {
                return new double[]{-0.5, 1.0, 0.5};
            }
        }
    }

    private double[] getVariantTranslation(final Direction direction) {
        switch (direction) {
            case NORTH: {
                return new double[]{-0.5, 1.1, -0.65};
            }
            case EAST: {
                return new double[]{-0.5, 1.1, 0.35};
            }
            case WEST: {
                return new double[]{-0.5, 1.1, 0.65};
            }
            default: {
                return new double[]{-0.5, 1.1, -0.35};
            }
        }
    }

    static {
        PLAYER_MODEL = new StatuePlayerModel<PlayerEntity>(0.0f, false);
        OMEGA_STATUE_MODEL = new OmegaStatueModel();
        LootStatueRenderer.lightVBOMap = new HashMap<Integer, VertexBuffer>();
    }
}
