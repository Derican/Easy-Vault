package iskallia.vault.client.gui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.client.gui.tab.SkillTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;

import java.awt.*;

public abstract class ComponentDialog
        extends AbstractGui {
    private final SkillTreeScreen skillTreeScreen;
    protected Rectangle bounds;
    protected ScrollableContainer descriptionComponent;
    protected Button selectButton;

    protected ComponentDialog(SkillTreeScreen skillTreeScreen) {
        this.skillTreeScreen = skillTreeScreen;
    }

    public abstract void refreshWidgets();

    public abstract int getHeaderHeight();

    public abstract SkillTab createTab();

    public abstract Point getIconUV();

    protected final SkillTreeScreen getSkillTreeScreen() {
        return this.skillTreeScreen;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void mouseMoved(double screenX, double screenY) {
        if (this.bounds == null) {
            return;
        }

        double containerX = screenX - this.bounds.x;
        double containerY = screenY - this.bounds.y;

        if (this.selectButton != null) {
            this.selectButton.mouseMoved(containerX, containerY);
        }
    }

    public void mouseClicked(double screenX, double screenY, int button) {
        if (this.bounds == null) {
            return;
        }

        double containerX = screenX - this.bounds.x;
        double containerY = screenY - this.bounds.y;

        if (this.selectButton != null) {
            this.selectButton.mouseClicked(containerX, containerY, button);
        }
    }

    public void mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.bounds == null || this.descriptionComponent == null || this.descriptionComponent.bounds == null ||


                !this.descriptionComponent.bounds.contains((int) mouseX - this.bounds.x, (int) mouseY - this.bounds.y)) {
            return;
        }
        this.descriptionComponent.mouseScrolled(mouseX, mouseY, delta);
    }

    public Rectangle getHeadingBounds() {
        int widgetHeight = getHeaderHeight();
        return new Rectangle(5, 5, this.bounds.width - 20, widgetHeight + 5);
    }

    public Rectangle getDescriptionsBounds() {
        Rectangle headingBounds = getHeadingBounds();

        int topOffset = headingBounds.y + headingBounds.height + 10;
        int descriptionHeight = this.bounds.height - 50 - topOffset;
        return new Rectangle(headingBounds.x, topOffset, headingBounds.width, descriptionHeight);
    }


    public abstract void render(MatrixStack paramMatrixStack, int paramInt1, int paramInt2, float paramFloat);

    protected void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);
        fill(matrixStack, this.bounds.x + 5, this.bounds.y + 5, this.bounds.x + this.bounds.width - 5, this.bounds.y + this.bounds.height - 5, -3750202);


        blit(matrixStack, this.bounds.x, this.bounds.y, 0, 44, 5, 5);


        blit(matrixStack, this.bounds.x + this.bounds.width - 5, this.bounds.y, 8, 44, 5, 5);


        blit(matrixStack, this.bounds.x, this.bounds.y + this.bounds.height - 5, 0, 52, 5, 5);


        blit(matrixStack, this.bounds.x + this.bounds.width - 5, this.bounds.y + this.bounds.height - 5, 8, 52, 5, 5);


        matrixStack.pushPose();
        matrixStack.translate((this.bounds.x + 5), this.bounds.y, 0.0D);
        matrixStack.scale((this.bounds.width - 10), 1.0F, 1.0F);
        blit(matrixStack, 0, 0, 6, 44, 1, 5);

        matrixStack.translate(0.0D, this.bounds.getHeight() - 5.0D, 0.0D);
        blit(matrixStack, 0, 0, 6, 52, 1, 5);

        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(this.bounds.x, (this.bounds.y + 5), 0.0D);
        matrixStack.scale(1.0F, (this.bounds.height - 10), 1.0F);
        blit(matrixStack, 0, 0, 0, 50, 5, 1);

        matrixStack.translate(this.bounds.getWidth() - 5.0D, 0.0D, 0.0D);
        blit(matrixStack, 0, 0, 8, 50, 5, 1);

        matrixStack.popPose();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\component\ComponentDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */