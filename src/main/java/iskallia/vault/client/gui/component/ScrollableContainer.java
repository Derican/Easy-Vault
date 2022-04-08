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


public class ScrollableContainer
        extends AbstractGui {
    public static final ResourceLocation UI_RESOURCE = new ResourceLocation("the_vault", "textures/gui/ability-tree.png");

    public static final int SCROLL_WIDTH = 8;

    protected Rectangle bounds;

    protected Renderable renderer;

    protected int innerHeight;
    protected int yOffset;
    protected boolean scrolling;
    protected double scrollingStartY;
    protected int scrollingOffsetY;

    public ScrollableContainer(Renderable renderer) {
        this.renderer = renderer;
    }

    public int getyOffset() {
        return this.yOffset;
    }

    public float scrollPercentage() {
        Rectangle scrollBounds = getScrollBounds();
        return this.yOffset / (this.innerHeight - scrollBounds.height);
    }

    public void setInnerHeight(int innerHeight) {
        this.innerHeight = innerHeight;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Rectangle getRenderableBounds() {
        return new Rectangle(this.bounds.x, this.bounds.y, this.bounds.width - 8 + 2, this.bounds.height);
    }

    public Rectangle getScrollBounds() {
        return new Rectangle(this.bounds.x + this.bounds.width - 8, this.bounds.y, 8, this.bounds.height);
    }

    public void mouseMoved(double mouseX, double mouseY) {
        if (this.scrolling) {
            double deltaY = mouseY - this.scrollingStartY;
            Rectangle renderableBounds = getRenderableBounds();
            Rectangle scrollBounds = getScrollBounds();
            double deltaOffset = deltaY * this.innerHeight / scrollBounds.getHeight();

            this.yOffset = MathHelper.clamp(this.scrollingOffsetY + (int) (deltaOffset * this.innerHeight / scrollBounds.height), 0, this.innerHeight - renderableBounds.height + 2);
        }
    }


    public void mouseClicked(double mouseX, double mouseY, int button) {
        Rectangle renderableBounds = getRenderableBounds();
        Rectangle scrollBounds = getScrollBounds();

        float viewportRatio = (float) renderableBounds.getHeight() / this.innerHeight;

        if (viewportRatio < 1.0F && scrollBounds.contains((int) mouseX, (int) mouseY)) {
            this.scrolling = true;
            this.scrollingStartY = mouseY;
            this.scrollingOffsetY = this.yOffset;
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.scrolling = false;
    }

    public void mouseScrolled(double mouseX, double mouseY, double delta) {
        Rectangle renderableBounds = getRenderableBounds();
        float viewportRatio = (float) renderableBounds.getHeight() / this.innerHeight;

        if (viewportRatio < 1.0F) {
            this.yOffset = MathHelper.clamp(this.yOffset + (int) (-delta * 5.0D), 0, this.innerHeight - renderableBounds.height + 2);
        }
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();

        Rectangle renderBounds = getRenderableBounds();
        Rectangle scrollBounds = getScrollBounds();

        textureManager.bind(SkillTreeScreen.UI_RESOURCE);
        UIHelper.renderContainerBorder(this, matrixStack, renderBounds, 14, 44, 2, 2, 2, 2, -7631989);


        UIHelper.renderOverflowHidden(matrixStack, ms -> fill(ms, renderBounds.x + 1, renderBounds.y + 1, renderBounds.x + renderBounds.width - 1, renderBounds.y + renderBounds.height - 1, -7631989), ms -> {
            ms.pushPose();

            ms.translate((renderBounds.x + 1), (renderBounds.y - this.yOffset + 1), 0.0D);

            this.renderer.render(matrixStack, mouseX, mouseY, partialTicks);

            ms.popPose();
        });

        textureManager.bind(SkillTreeScreen.UI_RESOURCE);
        matrixStack.pushPose();
        matrixStack.translate((scrollBounds.x + 2), scrollBounds.y, 0.0D);
        matrixStack.scale(1.0F, scrollBounds.height, 1.0F);
        blit(matrixStack, 0, 0, 1, 146, 6, 1);
        matrixStack.popPose();
        blit(matrixStack, scrollBounds.x + 2, scrollBounds.y, 1, 145, 6, 1);
        blit(matrixStack, scrollBounds.x + 2, scrollBounds.y + scrollBounds.height - 1, 1, 251, 6, 1);

        float scrollPercentage = scrollPercentage();
        float viewportRatio = (float) renderBounds.getHeight() / this.innerHeight;
        int scrollHeight = (int) (renderBounds.getHeight() * viewportRatio);

        if (viewportRatio <= 1.0F) {
            int scrollU = this.scrolling ? 28 : (scrollBounds.contains(mouseX, mouseY) ? 18 : 8);

            matrixStack.pushPose();
            matrixStack.translate(0.0D, (scrollBounds.getHeight() - scrollHeight) * scrollPercentage, 0.0D);
            blit(matrixStack, scrollBounds.x + 1, scrollBounds.y, scrollU, 104, 8, scrollHeight);


            blit(matrixStack, scrollBounds.x + 1, scrollBounds.y - 2, scrollU, 101, 8, 2);


            blit(matrixStack, scrollBounds.x + 1, scrollBounds.y + scrollHeight, scrollU, 253, 8, 2);


            matrixStack.popPose();
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\component\ScrollableContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */