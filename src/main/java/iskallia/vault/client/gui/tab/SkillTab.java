// 
// Decompiled by Procyon v0.6.0
// 

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

public abstract class SkillTab extends Screen
{
    protected SkillTreeScreen parentScreen;
    protected static Map<Class<? extends SkillTab>, Vector2f> persistedTranslations;
    protected static Map<Class<? extends SkillTab>, Float> persistedScales;
    private boolean scrollable;
    protected Vector2f viewportTranslation;
    protected float viewportScale;
    protected boolean dragging;
    protected Vector2f grabbedPos;
    
    protected SkillTab(final SkillTreeScreen parentScreen, final ITextComponent title) {
        super(title);
        this.scrollable = true;
        this.parentScreen = parentScreen;
        this.viewportTranslation = SkillTab.persistedTranslations.computeIfAbsent(this.getClass(), clazz -> new Vector2f(0.0f, 0.0f));
        this.viewportScale = SkillTab.persistedScales.computeIfAbsent(this.getClass(), clazz -> 1.0f);
        this.dragging = false;
        this.grabbedPos = new Vector2f(0.0f, 0.0f);
    }
    
    protected void setScrollable(final boolean scrollable) {
        this.scrollable = scrollable;
    }
    
    public abstract void refresh();
    
    public abstract String getTabName();
    
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.scrollable) {
            this.dragging = true;
            this.grabbedPos = new Vector2f((float)mouseX, (float)mouseY);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    public boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        if (this.scrollable) {
            this.dragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    public void mouseMoved(final double mouseX, final double mouseY) {
        if (this.scrollable && this.dragging) {
            final float dx = (float)(mouseX - this.grabbedPos.x) / this.viewportScale;
            final float dy = (float)(mouseY - this.grabbedPos.y) / this.viewportScale;
            this.viewportTranslation = new Vector2f(this.viewportTranslation.x + dx, this.viewportTranslation.y + dy);
            this.grabbedPos = new Vector2f((float)mouseX, (float)mouseY);
        }
    }
    
    public boolean mouseScrolled(final double mouseX, final double mouseY, final double delta) {
        final boolean mouseScrolled = super.mouseScrolled(mouseX, mouseY, delta);
        if (!this.scrollable) {
            return mouseScrolled;
        }
        final Point2D.Float midpoint = MiscUtils.getMidpoint(this.parentScreen.getContainerBounds());
        final double zoomingX = (mouseX - midpoint.x) / this.viewportScale + this.viewportTranslation.x;
        final double zoomingY = (mouseY - midpoint.y) / this.viewportScale + this.viewportTranslation.y;
        final int wheel = (delta < 0.0) ? -1 : 1;
        final double zoomTargetX = (zoomingX - this.viewportTranslation.x) / this.viewportScale;
        final double zoomTargetY = (zoomingY - this.viewportTranslation.y) / this.viewportScale;
        this.viewportScale += (float)(0.25 * wheel * this.viewportScale);
        this.viewportScale = (float)MathHelper.clamp((double)this.viewportScale, 0.5, 5.0);
        this.viewportTranslation = new Vector2f((float)(-zoomTargetX * this.viewportScale + zoomingX), (float)(-zoomTargetY * this.viewportScale + zoomingY));
        return mouseScrolled;
    }
    
    public void removed() {
        SkillTab.persistedTranslations.put(this.getClass(), this.viewportTranslation);
        SkillTab.persistedScales.put(this.getClass(), this.viewportScale);
    }
    
    public List<Runnable> renderTab(final Rectangle containerBounds, final MatrixStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        final List<Runnable> postRender = new ArrayList<Runnable>();
        UIHelper.renderOverflowHidden(renderStack, ms -> this.renderTabBackground(ms, containerBounds), ms -> this.renderTabForeground(ms, mouseX, mouseY, pTicks, postRender));
        return postRender;
    }
    
    public void renderTabForeground(final MatrixStack renderStack, final int mouseX, final int mouseY, final float pTicks, final List<Runnable> postContainerRender) {
        this.render(renderStack, mouseX, mouseY, pTicks);
    }
    
    public void renderTabBackground(final MatrixStack matrixStack, final Rectangle containerBounds) {
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.BACKGROUNDS_RESOURCE);
        ScreenDrawHelper.draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, buf -> {
            final float textureSize = 16.0f * this.viewportScale;
            float currentX = (float)containerBounds.x;
            float currentY = (float)containerBounds.y;
            float uncoveredWidth = (float)containerBounds.width;
            float uncoveredHeight = (float)containerBounds.height;
            while (uncoveredWidth > 0.0f) {
                while (uncoveredHeight > 0.0f) {
                    final float pWidth = Math.min(textureSize, uncoveredWidth) / textureSize;
                    final float pHeight = Math.min(textureSize, uncoveredHeight) / textureSize;
                    ScreenDrawHelper.rect((IVertexBuilder)buf, matrixStack, currentX, currentY, 0.0f, pWidth * textureSize, pHeight * textureSize).tex(0.31254f, 0.0f, 0.999f * pWidth / 16.0f, 0.999f * pHeight / 16.0f).draw();
                    uncoveredHeight -= textureSize;
                    currentY += textureSize;
                }
                uncoveredWidth -= textureSize;
                currentX += textureSize;
                uncoveredHeight = (float)containerBounds.height;
                currentY = (float)containerBounds.y;
            }
        });
    }
    
    static {
        SkillTab.persistedTranslations = new HashMap<Class<? extends SkillTab>, Vector2f>();
        SkillTab.persistedScales = new HashMap<Class<? extends SkillTab>, Float>();
    }
}
