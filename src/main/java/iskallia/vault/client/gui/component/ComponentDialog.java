package iskallia.vault.client.gui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.client.gui.tab.SkillTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;

import java.awt.*;

public abstract class ComponentDialog extends AbstractGui {
    private final SkillTreeScreen skillTreeScreen;
    protected Rectangle bounds;
    protected ScrollableContainer descriptionComponent;
    protected Button selectButton;

    protected ComponentDialog(final SkillTreeScreen skillTreeScreen) {
        this.skillTreeScreen = skillTreeScreen;
    }

    public abstract void refreshWidgets();

    public abstract int getHeaderHeight();

    public abstract SkillTab createTab();

    public abstract Point getIconUV();

    protected final SkillTreeScreen getSkillTreeScreen() {
        return this.skillTreeScreen;
    }

    public void setBounds(final Rectangle bounds) {
        this.bounds = bounds;
    }

    public void mouseMoved(final double screenX, final double screenY) {
        if (this.bounds == null) {
            return;
        }
        final double containerX = screenX - this.bounds.x;
        final double containerY = screenY - this.bounds.y;
        if (this.selectButton != null) {
            this.selectButton.mouseMoved(containerX, containerY);
        }
    }

    public void mouseClicked(final double screenX, final double screenY, final int button) {
        if (this.bounds == null) {
            return;
        }
        final double containerX = screenX - this.bounds.x;
        final double containerY = screenY - this.bounds.y;
        if (this.selectButton != null) {
            this.selectButton.mouseClicked(containerX, containerY, button);
        }
    }

    public void mouseScrolled(final double mouseX, final double mouseY, final double delta) {
        if (this.bounds == null || this.descriptionComponent == null || this.descriptionComponent.bounds == null || !this.descriptionComponent.bounds.contains((int) mouseX - this.bounds.x, (int) mouseY - this.bounds.y)) {
            return;
        }
        this.descriptionComponent.mouseScrolled(mouseX, mouseY, delta);
    }

    public Rectangle getHeadingBounds() {
        final int widgetHeight = this.getHeaderHeight();
        return new Rectangle(5, 5, this.bounds.width - 20, widgetHeight + 5);
    }

    public Rectangle getDescriptionsBounds() {
        final Rectangle headingBounds = this.getHeadingBounds();
        final int topOffset = headingBounds.y + headingBounds.height + 10;
        final int descriptionHeight = this.bounds.height - 50 - topOffset;
        return new Rectangle(headingBounds.x, topOffset, headingBounds.width, descriptionHeight);
    }

    public abstract void render(final MatrixStack p0, final int p1, final int p2, final float p3);

    protected void renderBackground(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);
        fill(matrixStack, this.bounds.x + 5, this.bounds.y + 5, this.bounds.x + this.bounds.width - 5, this.bounds.y + this.bounds.height - 5, -3750202);
        this.blit(matrixStack, this.bounds.x, this.bounds.y, 0, 44, 5, 5);
        this.blit(matrixStack, this.bounds.x + this.bounds.width - 5, this.bounds.y, 8, 44, 5, 5);
        this.blit(matrixStack, this.bounds.x, this.bounds.y + this.bounds.height - 5, 0, 52, 5, 5);
        this.blit(matrixStack, this.bounds.x + this.bounds.width - 5, this.bounds.y + this.bounds.height - 5, 8, 52, 5, 5);
        matrixStack.pushPose();
        matrixStack.translate(this.bounds.x + 5, this.bounds.y, 0.0);
        matrixStack.scale((float) (this.bounds.width - 10), 1.0f, 1.0f);
        this.blit(matrixStack, 0, 0, 6, 44, 1, 5);
        matrixStack.translate(0.0, this.bounds.getHeight() - 5.0, 0.0);
        this.blit(matrixStack, 0, 0, 6, 52, 1, 5);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate(this.bounds.x, this.bounds.y + 5, 0.0);
        matrixStack.scale(1.0f, (float) (this.bounds.height - 10), 1.0f);
        this.blit(matrixStack, 0, 0, 0, 50, 5, 1);
        matrixStack.translate(this.bounds.getWidth() - 5.0, 0.0, 0.0);
        this.blit(matrixStack, 0, 0, 8, 50, 5, 1);
        matrixStack.popPose();
    }
}
