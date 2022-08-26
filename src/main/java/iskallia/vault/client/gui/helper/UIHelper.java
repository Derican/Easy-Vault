package iskallia.vault.client.gui.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.*;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class UIHelper {
    public static final ResourceLocation UI_RESOURCE;
    private static final int[] LINE_BREAK_VALUES;

    public static void renderOverflowHidden(final MatrixStack matrixStack, final Consumer<MatrixStack> backgroundRenderer, final Consumer<MatrixStack> innerRenderer) {
        matrixStack.pushPose();
        RenderSystem.enableDepthTest();
        matrixStack.translate(0.0, 0.0, 950.0);
        RenderSystem.colorMask(false, false, false, false);
        AbstractGui.fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        matrixStack.translate(0.0, 0.0, -950.0);
        RenderSystem.depthFunc(518);
        backgroundRenderer.accept(matrixStack);
        RenderSystem.depthFunc(515);
        innerRenderer.accept(matrixStack);
        RenderSystem.depthFunc(518);
        matrixStack.translate(0.0, 0.0, -950.0);
        RenderSystem.colorMask(false, false, false, false);
        AbstractGui.fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        matrixStack.translate(0.0, 0.0, 950.0);
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
        matrixStack.popPose();
    }

    public static void drawFacingPlayer(final MatrixStack renderStack, final int containerMouseX, final int containerMouseY) {
        final PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            drawFacingEntity(player, renderStack, containerMouseX, containerMouseY);
        }
    }

    public static void drawFacingEntity(final LivingEntity entity, final MatrixStack renderStack, final int containerMouseX, final int containerMouseY) {
        final float xYaw = (float) (-Math.atan(containerMouseX / 40.0f));
        final float yPitch = (float) (-Math.atan((containerMouseY + 50) / 40.0f));
        renderStack.pushPose();
        renderStack.scale(1.0f, 1.0f, -1.0f);
        renderStack.translate(0.0, 0.0, -500.0);
        renderStack.scale(30.0f, 30.0f, 30.0f);
        final Quaternion rotation = Vector3f.ZP.rotationDegrees(180.0f);
        final Quaternion viewRotation = Vector3f.XP.rotationDegrees(yPitch * 20.0f);
        rotation.mul(viewRotation);
        renderStack.mulPose(rotation);
        final float f2 = entity.yBodyRot;
        final float f3 = entity.yRot;
        final float f4 = entity.xRot;
        final float f5 = entity.yHeadRotO;
        final float f6 = entity.yHeadRot;
        entity.yBodyRot = 180.0f + xYaw * 20.0f;
        entity.yRot = 180.0f + xYaw * 40.0f;
        entity.xRot = -yPitch * 20.0f;
        entity.yHeadRot = entity.yRot;
        entity.yHeadRotO = entity.yRot;
        final EntityRendererManager entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        viewRotation.conj();
        entityrenderermanager.overrideCameraOrientation(viewRotation);
        entityrenderermanager.setRenderShadow(false);
        RenderHelper.setupForFlatItems();
        final IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        entityrenderermanager.render((Entity) entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, renderStack, buffers, LightmapHelper.getPackedFullbrightCoords());
        buffers.endBatch();
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
        RenderHelper.setupFor3DItems();
        entityrenderermanager.setRenderShadow(true);
        entity.yBodyRot = f2;
        entity.yRot = f3;
        entity.xRot = f4;
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;
        renderStack.popPose();
    }

    public static void renderContainerBorder(final AbstractGui gui, final MatrixStack matrixStack, final Rectangle screenBounds, final int u, final int v, final int lw, final int rw, final int th, final int bh, final int contentColor) {
        final int width = screenBounds.width;
        final int height = screenBounds.height;
        renderContainerBorder(gui, matrixStack, screenBounds.x, screenBounds.y, width, height, u, v, lw, rw, th, bh, contentColor);
    }

    public static void renderContainerBorder(final AbstractGui gui, final MatrixStack matrixStack, final int x, final int y, final int width, final int height, final int u, final int v, final int lw, final int rw, final int th, final int bh, final int contentColor) {
        final int horizontalGap = width - lw - rw;
        final int verticalGap = height - th - bh;
        if (contentColor != 0) {
            AbstractGui.fill(matrixStack, x + lw, y + th, x + lw + horizontalGap, y + th + verticalGap, contentColor);
        }
        gui.blit(matrixStack, x, y, u, v, lw, th);
        gui.blit(matrixStack, x + lw + horizontalGap, y, u + lw + 3, v, rw, th);
        gui.blit(matrixStack, x, y + th + verticalGap, u, v + th + 3, lw, bh);
        gui.blit(matrixStack, x + lw + horizontalGap, y + th + verticalGap, u + lw + 3, v + th + 3, rw, bh);
        matrixStack.pushPose();
        matrixStack.translate(x + lw, y, 0.0);
        matrixStack.scale((float) horizontalGap, 1.0f, 1.0f);
        gui.blit(matrixStack, 0, 0, u + lw + 1, v, 1, th);
        matrixStack.translate(0.0, th + verticalGap, 0.0);
        gui.blit(matrixStack, 0, 0, u + lw + 1, v + th + 3, 1, bh);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate(x, y + th, 0.0);
        matrixStack.scale(1.0f, (float) verticalGap, 1.0f);
        gui.blit(matrixStack, 0, 0, u, v + th + 1, lw, 1);
        matrixStack.translate(lw + horizontalGap, 0.0, 0.0);
        gui.blit(matrixStack, 0, 0, u + lw + 3, v + th + 1, rw, 1);
        matrixStack.popPose();
    }

    public static void renderLabelAtRight(final AbstractGui gui, final MatrixStack matrixStack, final String text, final int x, final int y) {
        final Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(UIHelper.UI_RESOURCE);
        final FontRenderer fontRenderer = minecraft.font;
        final int textWidth = fontRenderer.width(text);
        matrixStack.pushPose();
        matrixStack.translate(x, y, 0.0);
        final float scale = 0.75f;
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-9.0, 0.0, 0.0);
        gui.blit(matrixStack, 0, 0, 143, 36, 9, 24);
        final int gap = 5;
        int remainingWidth = textWidth + 2 * gap;
        matrixStack.translate(-remainingWidth, 0.0, 0.0);
        while (remainingWidth > 0) {
            gui.blit(matrixStack, 0, 0, 136, 36, 6, 24);
            remainingWidth -= 6;
            matrixStack.translate(Math.min(6, remainingWidth), 0.0, 0.0);
        }
        matrixStack.translate(-textWidth - 2 * gap - 6, 0.0, 0.0);
        gui.blit(matrixStack, 0, 0, 121, 36, 14, 24);
        fontRenderer.draw(matrixStack, text, (float) (14 + gap), 9.0f, -12305893);
        matrixStack.popPose();
    }

    public static int renderCenteredWrappedText(final MatrixStack matrixStack, final ITextComponent text, final int maxWidth, final int padding) {
        final Minecraft minecraft = Minecraft.getInstance();
        final FontRenderer fontRenderer = minecraft.font;
        final List<ITextProperties> lines = getLines(TextComponentUtils.mergeStyles(text.copy(), text.getStyle()), maxWidth - 3 * padding);
        final int length = lines.stream().mapToInt(fontRenderer::width).max().orElse(0);
        final List<IReorderingProcessor> processors = LanguageMap.getInstance().getVisualOrder(lines);
        matrixStack.pushPose();
        matrixStack.translate(-length / 2.0f, 0.0, 0.0);
        for (int i = 0; i < processors.size(); ++i) {
            fontRenderer.draw(matrixStack, processors.get(i), (float) padding, (float) (10 * i + padding), -15130590);
        }
        matrixStack.popPose();
        return processors.size();
    }

    public static int renderWrappedText(final MatrixStack matrixStack, final ITextComponent text, final int maxWidth, final int padding) {
        return renderWrappedText(matrixStack, text, maxWidth, padding, -15130590);
    }

    public static int renderWrappedText(final MatrixStack matrixStack, final ITextComponent text, final int maxWidth, final int padding, final int color) {
        final Minecraft minecraft = Minecraft.getInstance();
        final FontRenderer fontRenderer = minecraft.font;
        final List<ITextProperties> lines = getLines(TextComponentUtils.mergeStyles(text.copy(), text.getStyle()), maxWidth - 3 * padding);
        final List<IReorderingProcessor> processors = LanguageMap.getInstance().getVisualOrder(lines);
        for (int i = 0; i < processors.size(); ++i) {
            fontRenderer.draw(matrixStack, processors.get(i), (float) padding, (float) (10 * i + padding), color);
        }
        return processors.size();
    }

    private static List<ITextProperties> getLines(final ITextComponent component, final int maxWidth) {
        final Minecraft minecraft = Minecraft.getInstance();
        final CharacterManager charactermanager = minecraft.font.getSplitter();
        List<ITextProperties> list = null;
        float f = Float.MAX_VALUE;
        for (final int i : UIHelper.LINE_BREAK_VALUES) {
            final List<ITextProperties> list2 = charactermanager.splitLines(component, maxWidth - i, Style.EMPTY);
            final float f2 = Math.abs(getTextWidth(charactermanager, list2) - maxWidth);
            if (f2 <= 10.0f) {
                return list2;
            }
            if (f2 < f) {
                f = f2;
                list = list2;
            }
        }
        return list;
    }

    private static float getTextWidth(final CharacterManager manager, final List<ITextProperties> text) {
        return (float) text.stream().mapToDouble(manager::stringWidth).max().orElse(0.0);
    }

    public static String formatTimeString(final int remainingTicks) {
        final long seconds = remainingTicks / 20 % 60;
        final long minutes = remainingTicks / 20 / 60 % 60;
        final long hours = remainingTicks / 20 / 60 / 60;
        return (hours > 0L) ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    static {
        UI_RESOURCE = new ResourceLocation("the_vault", "textures/gui/ability-tree.png");
        LINE_BREAK_VALUES = new int[]{0, 10, -10, 25, -25};
    }
}
