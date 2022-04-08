package iskallia.vault.client.gui.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.*;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class UIHelper {
    public static final ResourceLocation UI_RESOURCE = new ResourceLocation("the_vault", "textures/gui/ability-tree.png");

    public static void renderOverflowHidden(MatrixStack matrixStack, Consumer<MatrixStack> backgroundRenderer, Consumer<MatrixStack> innerRenderer) {
        matrixStack.pushPose();
        RenderSystem.enableDepthTest();

        matrixStack.translate(0.0D, 0.0D, 950.0D);
        RenderSystem.colorMask(false, false, false, false);
        AbstractGui.fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        matrixStack.translate(0.0D, 0.0D, -950.0D);

        RenderSystem.depthFunc(518);
        backgroundRenderer.accept(matrixStack);
        RenderSystem.depthFunc(515);
        innerRenderer.accept(matrixStack);
        RenderSystem.depthFunc(518);

        matrixStack.translate(0.0D, 0.0D, -950.0D);
        RenderSystem.colorMask(false, false, false, false);
        AbstractGui.fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        matrixStack.translate(0.0D, 0.0D, 950.0D);
        RenderSystem.depthFunc(515);

        RenderSystem.disableDepthTest();
        matrixStack.popPose();
    }

    public static void drawFacingPlayer(MatrixStack renderStack, int containerMouseX, int containerMouseY) {
        ClientPlayerEntity clientPlayerEntity = (Minecraft.getInstance()).player;
        if (clientPlayerEntity != null) {
            drawFacingEntity((LivingEntity) clientPlayerEntity, renderStack, containerMouseX, containerMouseY);
        }
    }

    public static void drawFacingEntity(LivingEntity entity, MatrixStack renderStack, int containerMouseX, int containerMouseY) {
        float xYaw = (float) -Math.atan((containerMouseX / 40.0F));
        float yPitch = (float) -Math.atan(((containerMouseY + 50) / 40.0F));

        renderStack.pushPose();
        renderStack.scale(1.0F, 1.0F, -1.0F);
        renderStack.translate(0.0D, 0.0D, -500.0D);
        renderStack.scale(30.0F, 30.0F, 30.0F);

        Quaternion rotation = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion viewRotation = Vector3f.XP.rotationDegrees(yPitch * 20.0F);
        rotation.mul(viewRotation);
        renderStack.mulPose(rotation);

        float f2 = entity.yBodyRot;
        float f3 = entity.yRot;
        float f4 = entity.xRot;
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;
        entity.yBodyRot = 180.0F + xYaw * 20.0F;
        entity.yRot = 180.0F + xYaw * 40.0F;
        entity.xRot = -yPitch * 20.0F;
        entity.yHeadRot = entity.yRot;
        entity.yHeadRotO = entity.yRot;

        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        viewRotation.conj();
        entityrenderermanager.overrideCameraOrientation(viewRotation);
        entityrenderermanager.setRenderShadow(false);
        RenderHelper.setupForFlatItems();

        IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        entityrenderermanager.render((Entity) entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, renderStack, (IRenderTypeBuffer) buffers, LightmapHelper.getPackedFullbrightCoords());
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


    public static void renderContainerBorder(AbstractGui gui, MatrixStack matrixStack, Rectangle screenBounds, int u, int v, int lw, int rw, int th, int bh, int contentColor) {
        int width = screenBounds.width;
        int height = screenBounds.height;
        renderContainerBorder(gui, matrixStack, screenBounds.x, screenBounds.y, width, height, u, v, lw, rw, th, bh, contentColor);
    }


    public static void renderContainerBorder(AbstractGui gui, MatrixStack matrixStack, int x, int y, int width, int height, int u, int v, int lw, int rw, int th, int bh, int contentColor) {
        int horizontalGap = width - lw - rw;
        int verticalGap = height - th - bh;

        if (contentColor != 0) {
            AbstractGui.fill(matrixStack, x + lw, y + th, x + lw + horizontalGap, y + th + verticalGap, contentColor);
        }


        gui.blit(matrixStack, x, y, u, v, lw, th);


        gui.blit(matrixStack, x + lw + horizontalGap, y, u + lw + 3, v, rw, th);


        gui.blit(matrixStack, x, y + th + verticalGap, u, v + th + 3, lw, bh);


        gui.blit(matrixStack, x + lw + horizontalGap, y + th + verticalGap, u + lw + 3, v + th + 3, rw, bh);


        matrixStack.pushPose();
        matrixStack.translate((x + lw), y, 0.0D);
        matrixStack.scale(horizontalGap, 1.0F, 1.0F);
        gui.blit(matrixStack, 0, 0, u + lw + 1, v, 1, th);


        matrixStack.translate(0.0D, (th + verticalGap), 0.0D);
        gui.blit(matrixStack, 0, 0, u + lw + 1, v + th + 3, 1, bh);


        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(x, (y + th), 0.0D);
        matrixStack.scale(1.0F, verticalGap, 1.0F);
        gui.blit(matrixStack, 0, 0, u, v + th + 1, lw, 1);


        matrixStack.translate((lw + horizontalGap), 0.0D, 0.0D);
        gui.blit(matrixStack, 0, 0, u + lw + 3, v + th + 1, rw, 1);


        matrixStack.popPose();
    }

    public static void renderLabelAtRight(AbstractGui gui, MatrixStack matrixStack, String text, int x, int y) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(UI_RESOURCE);

        FontRenderer fontRenderer = minecraft.font;
        int textWidth = fontRenderer.width(text);

        matrixStack.pushPose();
        matrixStack.translate(x, y, 0.0D);

        float scale = 0.75F;
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-9.0D, 0.0D, 0.0D);
        gui.blit(matrixStack, 0, 0, 143, 36, 9, 24);

        int gap = 5;
        int remainingWidth = textWidth + 2 * gap;
        matrixStack.translate(-remainingWidth, 0.0D, 0.0D);
        while (remainingWidth > 0) {
            gui.blit(matrixStack, 0, 0, 136, 36, 6, 24);
            remainingWidth -= 6;
            matrixStack.translate(Math.min(6, remainingWidth), 0.0D, 0.0D);
        }

        matrixStack.translate((-textWidth - 2 * gap - 6), 0.0D, 0.0D);
        gui.blit(matrixStack, 0, 0, 121, 36, 14, 24);

        fontRenderer.draw(matrixStack, text, (14 + gap), 9.0F, -12305893);


        matrixStack.popPose();
    }

    public static int renderCenteredWrappedText(MatrixStack matrixStack, ITextComponent text, int maxWidth, int padding) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontRenderer = minecraft.font;

        List<ITextProperties> lines = getLines(
                (ITextComponent) TextComponentUtils.mergeStyles(text.copy(), text.getStyle()), maxWidth - 3 * padding);


        int length = lines.stream().mapToInt(fontRenderer::width).max().orElse(0);
        List<IReorderingProcessor> processors = LanguageMap.getInstance().getVisualOrder(lines);

        matrixStack.pushPose();
        matrixStack.translate((-length / 2.0F), 0.0D, 0.0D);
        for (int i = 0; i < processors.size(); i++) {
            fontRenderer.draw(matrixStack, processors.get(i), padding, (10 * i + padding), -15130590);
        }

        matrixStack.popPose();

        return processors.size();
    }

    public static int renderWrappedText(MatrixStack matrixStack, ITextComponent text, int maxWidth, int padding) {
        return renderWrappedText(matrixStack, text, maxWidth, padding, -15130590);
    }

    public static int renderWrappedText(MatrixStack matrixStack, ITextComponent text, int maxWidth, int padding, int color) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontRenderer = minecraft.font;

        List<ITextProperties> lines = getLines(
                (ITextComponent) TextComponentUtils.mergeStyles(text.copy(), text.getStyle()), maxWidth - 3 * padding);


        List<IReorderingProcessor> processors = LanguageMap.getInstance().getVisualOrder(lines);

        for (int i = 0; i < processors.size(); i++) {
            fontRenderer.draw(matrixStack, processors.get(i), padding, (10 * i + padding), color);
        }


        return processors.size();
    }

    private static final int[] LINE_BREAK_VALUES = new int[]{0, 10, -10, 25, -25};

    private static List<ITextProperties> getLines(ITextComponent component, int maxWidth) {
        Minecraft minecraft = Minecraft.getInstance();

        CharacterManager charactermanager = minecraft.font.getSplitter();
        List<ITextProperties> list = null;
        float f = Float.MAX_VALUE;

        for (int i : LINE_BREAK_VALUES) {
            List<ITextProperties> list1 = charactermanager.splitLines((ITextProperties) component, maxWidth - i, Style.EMPTY);
            float f1 = Math.abs(getTextWidth(charactermanager, list1) - maxWidth);
            if (f1 <= 10.0F) {
                return list1;
            }

            if (f1 < f) {
                f = f1;
                list = list1;
            }
        }

        return list;
    }

    private static float getTextWidth(CharacterManager manager, List<ITextProperties> text) {
        return (float) text.stream().mapToDouble(manager::stringWidth).max().orElse(0.0D);
    }

    public static String formatTimeString(int remainingTicks) {
        long seconds = (remainingTicks / 20 % 60);
        long minutes = (remainingTicks / 20 / 60 % 60);
        long hours = (remainingTicks / 20 / 60 / 60);
        return (hours > 0L) ?
                String.format("%02d:%02d:%02d", new Object[]{Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)
                }) : String.format("%02d:%02d", new Object[]{Long.valueOf(minutes), Long.valueOf(seconds)});
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\helper\UIHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */