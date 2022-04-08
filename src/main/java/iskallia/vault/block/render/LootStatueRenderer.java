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
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.*;
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
    protected static final StatuePlayerModel<PlayerEntity> PLAYER_MODEL = new StatuePlayerModel(0.0F, false);

    protected static final OmegaStatueModel OMEGA_STATUE_MODEL = new OmegaStatueModel();
    protected static Map<Integer, VertexBuffer> lightVBOMap = new HashMap<>();

    private final Minecraft mc = Minecraft.getInstance();

    public LootStatueRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }


    public void render(LootStatueTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        BlockState blockState = tileEntity.getBlockState();
        Direction direction = (Direction) blockState.getValue((Property) LootStatueBlock.FACING);
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        if (tileEntity.getStatueType() == StatueType.OMEGA && tileEntity.isMaster()) {
            renderOmegaStatueCrystals(tileEntity, matrixStack, buffer, combinedLight, combinedOverlay);

            if (tileEntity.getChipCount() > 0) {
                ClientPlayerEntity player = mc.player;
                int lightLevel = getLightAtPos(tileEntity.getLevel(), tileEntity.getBlockPos().above());

                renderItemWithLabel(new ItemStack((IItemProvider) ModItems.ACCELERATION_CHIP),
                        getTranslation(direction), Vector3f.YP
                                .rotationDegrees(((direction == Direction.EAST || direction == Direction.WEST) ? 'Ď' : '´') - player.yRot), matrixStack, buffer, combinedOverlay, lightLevel, direction, new StringTextComponent("" + tileEntity

                                .getChipCount()), -1);
            }
        } else if (tileEntity.getStatueType() == StatueType.OMEGA_VARIANT) {
            if (tileEntity.getChipCount() > 0) {
                ClientPlayerEntity player = mc.player;
                int lightLevel = getLightAtPos(tileEntity.getLevel(), tileEntity.getBlockPos().above());

                renderItemWithLabel(new ItemStack((IItemProvider) ModItems.ACCELERATION_CHIP),
                        getVariantTranslation(direction), Vector3f.YP
                                .rotationDegrees(((direction == Direction.EAST || direction == Direction.WEST) ? 'Ď' : '´') - player.yRot), matrixStack, buffer, combinedOverlay, lightLevel, direction, new StringTextComponent(

                                String.valueOf(tileEntity.getChipCount())), -1);
            }

            ItemStack loot = tileEntity.getLootItem();
            if (!loot.isEmpty()) {
                matrixStack.pushPose();
                matrixStack.translate(0.5D, 0.4D, 0.5D);
                matrixStack.translate(direction.getStepX() * -0.2D, 0.0D, direction.getStepZ() * -0.2D);
                matrixStack.scale(1.6F, 1.6F, 1.6F);
                IBakedModel ibakedmodel = mc.getItemRenderer().getModel(loot, null, null);
                mc.getItemRenderer().render(loot, ItemCameraTransforms.TransformType.GROUND, false, matrixStack, buffer, combinedLight, combinedOverlay, ibakedmodel);

                matrixStack.popPose();
            }
        }

        String latestNickname = tileEntity.getSkin().getLatestNickname();
        if (StringUtils.isNullOrEmpty(latestNickname)) {
            return;
        }

        drawPlayerModel(matrixStack, buffer, tileEntity, combinedLight, combinedOverlay);
        StatueType statueType = tileEntity.getStatueType();

        if (statueType == StatueType.GIFT_MEGA) {
            drawStatueBowHat(matrixStack, buffer, direction, combinedLight, combinedOverlay);
        }
        if (statueType == StatueType.TROPHY) {
            drawRecordDisplay(matrixStack, buffer, direction, tileEntity, combinedLight, combinedOverlay);
        }
        if (statueType == StatueType.OMEGA_VARIANT) {
            drawStatueNameplate(matrixStack, buffer, latestNickname, direction, tileEntity, combinedLight, combinedOverlay);
        }

        if (mc.hitResult != null && mc.hitResult.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult result = (BlockRayTraceResult) mc.hitResult;
            if (tileEntity.getBlockPos().equals(result.getBlockPos())) {
                IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent(latestNickname)).withStyle(TextFormatting.WHITE);
                if (statueType.hasLimitedItems() && tileEntity.getItemsRemaining() <= 0) {
                    iFormattableTextComponent = (new StringTextComponent("☠ ")).withStyle(TextFormatting.RED).append((ITextComponent) iFormattableTextComponent);
                }
                renderLabel(matrixStack, buffer, combinedLight, (ITextComponent) iFormattableTextComponent, -1);
            }
        }
    }

    private void drawStatueNameplate(MatrixStack matrixStack, IRenderTypeBuffer buffer, String latestNickname, Direction direction, LootStatueTileEntity tileEntity, int combinedLight, int combinedOverlay) {
        IReorderingProcessor text = (new StringTextComponent(latestNickname)).withStyle(TextFormatting.BLACK).getVisualOrderText();
        FontRenderer fr = this.renderer.getFont();
        int xOffset = fr.width(text);

        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.35D, 0.5D);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0F));
        matrixStack.translate(0.0D, 0.0D, 0.51D);
        matrixStack.scale(0.01F, -0.01F, 0.01F);

        fr.drawInBatch(text, -xOffset / 2.0F, 0.0F, -16777216, false, matrixStack
                .last().pose(), buffer, false, 0, combinedLight);


        matrixStack.popPose();
    }

    private void drawRecordDisplay(MatrixStack matrixStack, IRenderTypeBuffer buffer, Direction direction, LootStatueTileEntity tileEntity, int combinedLight, int combinedOverlay) {
        if (!(tileEntity instanceof TrophyStatueTileEntity)) {
            return;
        }
        TrophyStatueTileEntity trophyTile = (TrophyStatueTileEntity) tileEntity;
        WeekKey week = trophyTile.getWeek();
        PlayerVaultStatsData.PlayerRecordEntry recordEntry = trophyTile.getRecordEntry();
        FontRenderer fr = this.renderer.getFont();

        LocalDateTime ldt = LocalDateTime.now();


        ldt = ldt.with(IsoFields.WEEK_BASED_YEAR, week.getYear()).with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week.getWeek()).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        String from = ldt.getDayOfMonth() + "." + ldt.getMonthValue() + "." + ldt.getYear() + " -";
        ldt = ldt.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        String to = ldt.getDayOfMonth() + "." + ldt.getMonthValue() + "." + ldt.getYear();

        IReorderingProcessor fromCmp = (new StringTextComponent(from)).getVisualOrderText();
        IReorderingProcessor toCmp = (new StringTextComponent(to)).getVisualOrderText();
        IReorderingProcessor timeStr = (new StringTextComponent(UIHelper.formatTimeString(recordEntry.getTickCount()))).getVisualOrderText();

        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0F));

        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.24D, 0.22D);
        matrixStack.scale(0.0055F, -0.0055F, 0.0055F);
        int xOffset = fr.width(fromCmp);
        fr.drawInBatch(fromCmp, -xOffset / 2.0F, 0.0F, -16777216, false, matrixStack
                .last().pose(), buffer, false, 0, combinedLight);

        xOffset = fr.width(toCmp);
        fr.drawInBatch(toCmp, -xOffset / 2.0F, 10.0F, -16777216, false, matrixStack
                .last().pose(), buffer, false, 0, combinedLight);

        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.1D, 0.19D);
        matrixStack.scale(0.008F, -0.008F, 0.008F);
        xOffset = fr.width(timeStr);
        fr.drawInBatch(timeStr, -xOffset / 2.0F, 0.0F, -16777216, false, matrixStack
                .last().pose(), buffer, false, 0, combinedLight);

        matrixStack.popPose();

        matrixStack.popPose();
    }

    private void drawStatueBowHat(MatrixStack matrixStack, IRenderTypeBuffer buffer, Direction direction, int combinedLight, int combinedOverlay) {
        float hatScale = 3.0F;
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1.1D, 0.5D);
        matrixStack.scale(hatScale, hatScale, hatScale);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0F));
        ItemStack stack = new ItemStack((IItemProvider) ModBlocks.BOW_HAT);
        IBakedModel ibakedmodel = this.mc.getItemRenderer().getModel(stack, null, null);
        this.mc.getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, combinedLight, combinedOverlay, ibakedmodel);

        matrixStack.popPose();
    }

    private void drawPlayerModel(MatrixStack matrixStack, IRenderTypeBuffer buffer, LootStatueTileEntity tileEntity, int combinedLight, int combinedOverlay) {
        BlockState blockState = tileEntity.getBlockState();
        Direction direction = (Direction) blockState.getValue((Property) LootStatueBlock.FACING);
        StatueType statueType = tileEntity.getStatueType();
        ResourceLocation skinLocation = tileEntity.getSkin().getLocationSkin();
        RenderType renderType = PLAYER_MODEL.renderType(skinLocation);
        IVertexBuilder vertexBuilder = buffer.getBuffer(renderType);

        float scale = 0.4F;
        float headScale = 1.75F;
        float yOffset = statueType.getPlayerRenderYOffset();
        float statueOffset = 0.0F;

        if (statueType.doGrayscaleShader()) {
            ShaderUtil.useShader(ShaderUtil.GRAYSCALE_SHADER, () -> {
                float factor = tileEntity.getItemsRemaining() / tileEntity.getTotalItems();

                int grayScaleFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "grayFactor");

                ARBShaderObjects.glUniform1fARB(grayScaleFactor, factor);
                float brightness = LightmapUtil.getLightmapBrightness(combinedLight);
                int brightnessFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "brightness");
                ARBShaderObjects.glUniform1fARB(brightnessFactor, brightness);
            });
        }
        matrixStack.pushPose();

        if (statueType == StatueType.OMEGA) {
            float playerScale = tileEntity.getPlayerScale();
            matrixStack.translate(0.0D, (1.0F + playerScale), 0.0D);
            scale += playerScale;
        }
        if (statueType == StatueType.OMEGA_VARIANT) {
            matrixStack.translate(0.0D, 1.5499999523162842D, 0.0D);
            scale = 1.3F;
            headScale = 1.0F;
            statueOffset = 0.2F;
        }
        if (statueType == StatueType.TROPHY) {
            scale = (float) (scale - 0.04D);
        }

        matrixStack.translate(0.5D, yOffset, 0.5D);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0F));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        matrixStack.translate(0.0D, 0.0D, statueOffset);
        matrixStack.scale(scale, scale, scale);
        PLAYER_MODEL.body.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.leftLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.rightLeg.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.leftArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.rightArm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        PLAYER_MODEL.jacket.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.leftPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.rightPants.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.leftSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.0D, -0.6200000047683716D);
        PLAYER_MODEL.rightSleeve.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();

        matrixStack.scale(headScale, headScale, headScale);
        PLAYER_MODEL.hat.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        PLAYER_MODEL.head.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.popPose();

        if (buffer instanceof IRenderTypeBuffer.Impl) {
            ((IRenderTypeBuffer.Impl) buffer).endBatch(renderType);
        }
        if (statueType.doGrayscaleShader()) {
            ShaderUtil.releaseShader();
        }
    }

    private void renderOmegaStatueCrystals(LootStatueTileEntity tileEntity, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        RenderType omegaType = OMEGA_STATUE_MODEL.renderType(Vault.id("textures/block/mega_statue3.png"));
        VertexBuffer vbo = lightVBOMap.computeIfAbsent(Integer.valueOf(combinedLight), lightLvl -> VBOUtil.batch((EntityModel) OMEGA_STATUE_MODEL, omegaType, lightLvl.intValue(), OverlayTexture.NO_OVERLAY));


        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1.5D, 0.5D);
        BlockState blockState = tileEntity.getBlockState();
        Direction direction = (Direction) blockState.getValue((Property) LootStatueBlock.FACING);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0F));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));

        omegaType.setupRenderState();
        vbo.bind();
        omegaType.format().setupBufferState(0L);
        vbo.draw(matrixStack.last().pose(), omegaType.mode());
        omegaType.format().clearBufferState();
        VertexBuffer.unbind();
        omegaType.clearRenderState();

        matrixStack.popPose();
    }

    private void renderLabel(MatrixStack matrixStack, IRenderTypeBuffer buffer, int lightLevel, ITextComponent text, int color) {
        FontRenderer fontRenderer = this.mc.font;


        matrixStack.pushPose();
        float scale = 0.02F;
        int opacity = 1711276032;
        float offset = (-fontRenderer.width((ITextProperties) text) / 2);
        Matrix4f matrix4f = matrixStack.last().pose();

        matrixStack.translate(0.5D, 1.7000000476837158D, 0.5D);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(this.mc.getEntityRenderDispatcher().cameraOrientation());
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));

        fontRenderer.drawInBatch(text, offset, 0.0F, color, false, matrix4f, buffer, true, opacity, lightLevel);
        fontRenderer.drawInBatch(text, offset, 0.0F, -1, false, matrix4f, buffer, false, 0, lightLevel);
        matrixStack.popPose();
    }


    private void renderItemWithLabel(ItemStack stack, double[] translation, Quaternion rotation, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedOverlay, int lightLevel, Direction direction, StringTextComponent text, int color) {
        matrixStack.pushPose();
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + ((direction == Direction.NORTH || direction == Direction.EAST) ? 0 : '´')));
        matrixStack.translate(translation[0], translation[1], translation[2]);
        matrixStack.mulPose(rotation);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        IBakedModel ibakedmodel = this.mc.getItemRenderer().getModel(stack, null, null);
        this.mc.getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, lightLevel, combinedOverlay, ibakedmodel);

        float scale = -0.025F;
        float offset = (-(Minecraft.getInstance()).font.width((ITextProperties) text) / 2);
        matrixStack.translate(0.0D, 0.75D, 0.0D);
        matrixStack.scale(scale, scale, scale);
        Matrix4f matrix4f = matrixStack.last().pose();

        int opacity = 1711276032;
        (Minecraft.getInstance()).font.drawInBatch((ITextComponent) text, offset, 0.0F, color, false, matrix4f, buffer, true, opacity, lightLevel);
        (Minecraft.getInstance()).font.drawInBatch((ITextComponent) text, offset, 0.0F, -1, false, matrix4f, buffer, false, 0, lightLevel);
        matrixStack.popPose();
    }

    private int getLightAtPos(World world, BlockPos pos) {
        int blockLight = world.getBrightness(LightType.BLOCK, pos);
        int skyLight = world.getBrightness(LightType.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }

    private double[] getTranslation(Direction direction) {
        switch (direction) {
            case NORTH:
                return new double[]{-0.5D, 1.0D, -1.5D};
            case EAST:
                return new double[]{-0.5D, 1.0D, -0.5D};
            case WEST:
                return new double[]{-0.5D, 1.0D, 1.5D};
        }
        return new double[]{-0.5D, 1.0D, 0.5D};
    }


    private double[] getVariantTranslation(Direction direction) {
        switch (direction) {
            case NORTH:
                return new double[]{-0.5D, 1.1D, -0.65D};
            case EAST:
                return new double[]{-0.5D, 1.1D, 0.35D};
            case WEST:
                return new double[]{-0.5D, 1.1D, 0.65D};
        }
        return new double[]{-0.5D, 1.1D, -0.35D};
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\render\LootStatueRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */