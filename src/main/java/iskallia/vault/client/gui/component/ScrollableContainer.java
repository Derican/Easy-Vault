// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.gui.helper.Renderable;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ScrollableContainer extends AbstractGui
{
    public static final ResourceLocation UI_RESOURCE;
    public static final int SCROLL_WIDTH = 8;
    protected Rectangle bounds;
    protected Renderable renderer;
    protected int innerHeight;
    protected int yOffset;
    protected boolean scrolling;
    protected double scrollingStartY;
    protected int scrollingOffsetY;
    
    public ScrollableContainer(final Renderable renderer) {
        this.renderer = renderer;
    }
    
    public int getyOffset() {
        return this.yOffset;
    }
    
    public float scrollPercentage() {
        final Rectangle scrollBounds = this.getScrollBounds();
        return this.yOffset / (float)(this.innerHeight - scrollBounds.height);
    }
    
    public void setInnerHeight(final int innerHeight) {
        this.innerHeight = innerHeight;
    }
    
    public void setBounds(final Rectangle bounds) {
        this.bounds = bounds;
    }
    
    public Rectangle getRenderableBounds() {
        return new Rectangle(this.bounds.x, this.bounds.y, this.bounds.width - 8 + 2, this.bounds.height);
    }
    
    public Rectangle getScrollBounds() {
        return new Rectangle(this.bounds.x + this.bounds.width - 8, this.bounds.y, 8, this.bounds.height);
    }
    
    public void mouseMoved(final double mouseX, final double mouseY) {
        if (this.scrolling) {
            final double deltaY = mouseY - this.scrollingStartY;
            final Rectangle renderableBounds = this.getRenderableBounds();
            final Rectangle scrollBounds = this.getScrollBounds();
            final double deltaOffset = deltaY * this.innerHeight / scrollBounds.getHeight();
            this.yOffset = MathHelper.clamp(this.scrollingOffsetY + (int)(deltaOffset * this.innerHeight / scrollBounds.height), 0, this.innerHeight - renderableBounds.height + 2);
        }
    }
    
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        final Rectangle renderableBounds = this.getRenderableBounds();
        final Rectangle scrollBounds = this.getScrollBounds();
        final float viewportRatio = (float)renderableBounds.getHeight() / this.innerHeight;
        if (viewportRatio < 1.0f && scrollBounds.contains((int)mouseX, (int)mouseY)) {
            this.scrolling = true;
            this.scrollingStartY = mouseY;
            this.scrollingOffsetY = this.yOffset;
        }
    }
    
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
        this.scrolling = false;
    }
    
    public void mouseScrolled(final double mouseX, final double mouseY, final double delta) {
        final Rectangle renderableBounds = this.getRenderableBounds();
        final float viewportRatio = (float)renderableBounds.getHeight() / this.innerHeight;
        if (viewportRatio < 1.0f) {
            this.yOffset = MathHelper.clamp(this.yOffset + (int)(-delta * 5.0), 0, this.innerHeight - renderableBounds.height + 2);
        }
    }
    
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        final Rectangle renderBounds = this.getRenderableBounds();
        final Rectangle scrollBounds = this.getScrollBounds();
        textureManager.bind(SkillTreeScreen.UI_RESOURCE);
        UIHelper.renderContainerBorder(this, matrixStack, renderBounds, 14, 44, 2, 2, 2, 2, -7631989);
        UIHelper.renderOverflowHidden(matrixStack, ms -> fill(ms, renderBounds.x + 1, renderBounds.y + 1, renderBounds.x + renderBounds.width - 1, renderBounds.y + renderBounds.height - 1, -7631989), ms -> {
            ms.pushPose();
            ms.translate((double)(renderBounds.x + 1), (double)(renderBounds.y - this.yOffset + 1), 0.0);
            this.renderer.render(matrixStack, mouseX, mouseY, partialTicks);
            ms.popPose();
            return;
        });
        textureManager.bind(SkillTreeScreen.UI_RESOURCE);
        matrixStack.pushPose();
        matrixStack.translate((double)(scrollBounds.x + 2), (double)scrollBounds.y, 0.0);
        matrixStack.scale(1.0f, (float)scrollBounds.height, 1.0f);
        this.blit(matrixStack, 0, 0, 1, 146, 6, 1);
        matrixStack.popPose();
        this.blit(matrixStack, scrollBounds.x + 2, scrollBounds.y, 1, 145, 6, 1);
        this.blit(matrixStack, scrollBounds.x + 2, scrollBounds.y + scrollBounds.height - 1, 1, 251, 6, 1);
        final float scrollPercentage = this.scrollPercentage();
        final float viewportRatio = (float)renderBounds.getHeight() / this.innerHeight;
        final int scrollHeight = (int)(renderBounds.getHeight() * viewportRatio);
        if (viewportRatio <= 1.0f) {
            final int scrollU = this.scrolling ? 28 : (scrollBounds.contains(mouseX, mouseY) ? 18 : 8);
            matrixStack.pushPose();
            matrixStack.translate(0.0, (scrollBounds.getHeight() - scrollHeight) * scrollPercentage, 0.0);
            this.blit(matrixStack, scrollBounds.x + 1, scrollBounds.y, scrollU, 104, 8, scrollHeight);
            this.blit(matrixStack, scrollBounds.x + 1, scrollBounds.y - 2, scrollU, 101, 8, 2);
            this.blit(matrixStack, scrollBounds.x + 1, scrollBounds.y + scrollHeight, scrollU, 253, 8, 2);
            matrixStack.popPose();
        }
    }
    
    static {
        UI_RESOURCE = new ResourceLocation("the_vault", "textures/gui/ability-tree.png");
    }
}
