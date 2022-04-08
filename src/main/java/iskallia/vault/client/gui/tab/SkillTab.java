package iskallia.vault.client.gui.tab;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SkillTab
        extends Screen {
    protected SkillTreeScreen parentScreen;
    protected static Map<Class<? extends SkillTab>, Vector2f> persistedTranslations = new HashMap<>();
    protected static Map<Class<? extends SkillTab>, Float> persistedScales = new HashMap<>();

    private boolean scrollable = true;

    protected Vector2f viewportTranslation;
    protected float viewportScale;
    protected boolean dragging;
    protected Vector2f grabbedPos;

    protected SkillTab(SkillTreeScreen parentScreen, ITextComponent title) {
        super(title);
        this.parentScreen = parentScreen;
        this.viewportTranslation = persistedTranslations.computeIfAbsent(getClass(), clazz -> new Vector2f(0.0F, 0.0F));
        this.viewportScale = ((Float) persistedScales.computeIfAbsent(getClass(), clazz -> Float.valueOf(1.0F))).floatValue();
        this.dragging = false;
        this.grabbedPos = new Vector2f(0.0F, 0.0F);
    }

    protected void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.scrollable) {
            this.dragging = true;
            this.grabbedPos = new Vector2f((float) mouseX, (float) mouseY);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }


    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.scrollable) {
            this.dragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }


    public void mouseMoved(double mouseX, double mouseY) {
        if (this.scrollable && this.dragging) {
            float dx = (float) (mouseX - this.grabbedPos.x) / this.viewportScale;
            float dy = (float) (mouseY - this.grabbedPos.y) / this.viewportScale;
            this.viewportTranslation = new Vector2f(this.viewportTranslation.x + dx, this.viewportTranslation.y + dy);


            this.grabbedPos = new Vector2f((float) mouseX, (float) mouseY);
        }
    }


    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        boolean mouseScrolled = super.mouseScrolled(mouseX, mouseY, delta);
        if (!this.scrollable) {
            return mouseScrolled;
        }
        Point2D.Float midpoint = MiscUtils.getMidpoint(this.parentScreen.getContainerBounds());

        double zoomingX = (mouseX - midpoint.x) / this.viewportScale + this.viewportTranslation.x;
        double zoomingY = (mouseY - midpoint.y) / this.viewportScale + this.viewportTranslation.y;

        int wheel = (delta < 0.0D) ? -1 : 1;

        double zoomTargetX = (zoomingX - this.viewportTranslation.x) / this.viewportScale;
        double zoomTargetY = (zoomingY - this.viewportTranslation.y) / this.viewportScale;

        this.viewportScale = (float) (this.viewportScale + 0.25D * wheel * this.viewportScale);
        this.viewportScale = (float) MathHelper.clamp(this.viewportScale, 0.5D, 5.0D);

        this.viewportTranslation = new Vector2f((float) (-zoomTargetX * this.viewportScale + zoomingX), (float) (-zoomTargetY * this.viewportScale + zoomingY));


        return mouseScrolled;
    }


    public void removed() {
        persistedTranslations.put(getClass(), this.viewportTranslation);
        persistedScales.put(getClass(), Float.valueOf(this.viewportScale));
    }

    public List<Runnable> renderTab(Rectangle containerBounds, MatrixStack renderStack, int mouseX, int mouseY, float pTicks) {
        List<Runnable> postRender = new ArrayList<>();
        UIHelper.renderOverflowHidden(renderStack, ms -> renderTabBackground(ms, containerBounds), ms -> renderTabForeground(ms, mouseX, mouseY, pTicks, postRender));


        return postRender;
    }

    public void renderTabForeground(MatrixStack renderStack, int mouseX, int mouseY, float pTicks, List<Runnable> postContainerRender) {
        render(renderStack, mouseX, mouseY, pTicks);
    }

    public void renderTabBackground(MatrixStack matrixStack, Rectangle containerBounds) {
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.BACKGROUNDS_RESOURCE);

        ScreenDrawHelper.draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, buf -> {
            float textureSize = 16.0F * this.viewportScale;
            float currentX = containerBounds.x;
            float currentY = containerBounds.y;
            float uncoveredWidth = containerBounds.width;
            float uncoveredHeight = containerBounds.height;
            while (uncoveredWidth > 0.0F) {
                while (uncoveredHeight > 0.0F) {
                    float pWidth = Math.min(textureSize, uncoveredWidth) / textureSize;
                    float pHeight = Math.min(textureSize, uncoveredHeight) / textureSize;
                    ScreenDrawHelper.rect((IVertexBuilder) buf, matrixStack, currentX, currentY, 0.0F, pWidth * textureSize, pHeight * textureSize).tex(0.31254F, 0.0F, 0.999F * pWidth / 16.0F, 0.999F * pHeight / 16.0F).draw();
                    uncoveredHeight -= textureSize;
                    currentY += textureSize;
                }
                uncoveredWidth -= textureSize;
                currentX += textureSize;
                uncoveredHeight = containerBounds.height;
                currentY = containerBounds.y;
            }
        });
    }

    public abstract void refresh();

    public abstract String getTabName();
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\tab\SkillTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */