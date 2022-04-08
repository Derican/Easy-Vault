package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.widget.connect.ConnectableWidget;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.type.Research;
import iskallia.vault.util.ResourceBoundary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ResearchWidget
        extends Widget
        implements ConnectableWidget, ComponentWidget {
    private static final int ICON_SIZE = 30;
    private static final ResourceLocation SKILL_WIDGET_RESOURCE = new ResourceLocation("the_vault", "textures/gui/skill-widget.png");
    public static final ResourceLocation RESEARCHES_RESOURCE = new ResourceLocation("the_vault", "textures/gui/researches.png");

    private final String researchName;

    private final ResearchTree researchTree;
    private final boolean locked;
    private final SkillStyle style;
    private boolean selected = false;
    private boolean hoverable = true;

    public ResearchWidget(String researchName, ResearchTree researchTree, SkillStyle style) {
        super(style.x, style.y, 30, 30, (ITextComponent) new StringTextComponent("the_vault.widgets.research"));


        this.style = style;
        this.locked = ModConfigs.SKILL_GATES.getGates().isLocked(researchName, researchTree);
        this.researchName = researchName;
        this.researchTree = researchTree;
    }

    public ResearchTree getResearchTree() {
        return this.researchTree;
    }

    public String getResearchName() {
        return this.researchName;
    }


    public Rectangle getClickableBounds() {
        return new Rectangle(this.x, this.y, 30, 30);
    }


    public Point2D.Double getRenderPosition() {
        return new Point2D.Double(this.x + 2.5D, this.y + 2.5D);
    }


    public double getRenderWidth() {
        return 25.0D;
    }


    public double getRenderHeight() {
        return 25.0D;
    }

    public void setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
    }


    public boolean isMouseOver(double mouseX, double mouseY) {
        if (!this.hoverable) {
            return false;
        }
        return getClickableBounds().contains(mouseX, mouseY);
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.selected) return false;

        playDownSound(Minecraft.getInstance().getSoundManager());
        return true;
    }

    public void select() {
        this.selected = true;
    }

    public void deselect() {
        this.selected = false;
    }

    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, List<Runnable> postContainerRender) {
        render(matrixStack, mouseX, mouseY, partialTicks);

        Matrix4f current = matrixStack.last().pose().copy();
        postContainerRender.add(() -> {
            RenderSystem.pushMatrix();
            RenderSystem.multMatrix(current);
            renderHover(matrixStack, mouseX, mouseY, partialTicks);
            RenderSystem.popMatrix();
        });
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderIcon(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void renderHover(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (!isMouseOver(mouseX, mouseY)) {
            return;
        }

        List<ITextComponent> tTip = new ArrayList<>();
        tTip.add(new StringTextComponent(this.researchName));

        if (this.locked) {
            List<Research> preconditions = ModConfigs.SKILL_GATES.getGates().getDependencyResearches(this.researchName);
            if (!preconditions.isEmpty()) {
                tTip.add((new StringTextComponent("Requires:")).withStyle(TextFormatting.RED));
                preconditions.forEach(research -> tTip.add((new StringTextComponent("- " + research.getName())).withStyle(TextFormatting.RED)));
            }
        }


        List<Research> conflicts = ModConfigs.SKILL_GATES.getGates().getLockedByResearches(this.researchName);
        if (!conflicts.isEmpty()) {
            tTip.add((new StringTextComponent("Cannot be unlocked alongside:")).withStyle(TextFormatting.RED));
            conflicts.forEach(research -> tTip.add((new StringTextComponent("- " + research.getName())).withStyle(TextFormatting.RED)));
        }


        GuiUtils.drawHoveringText(matrixStack, tTip, this.x + 15, this.y + 15, 2147483647, 2147483647, -1,


                (Minecraft.getInstance()).font);

        RenderSystem.enableBlend();
    }

    private void renderIcon(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        ResourceBoundary resourceBoundary = this.style.frameType.getResourceBoundary();

        matrixStack.pushPose();
        (Minecraft.getInstance()).textureManager.bind(resourceBoundary.getResource());


        int vOffset = this.locked ? 62 : ((this.selected || isMouseOver(mouseX, mouseY)) ? -31 : (this.researchTree.getResearchesDone().contains(this.researchName) ? 31 : 0));
        blit(matrixStack, this.x, this.y, resourceBoundary
                .getU(), resourceBoundary
                .getV() + vOffset, resourceBoundary
                .getW(), resourceBoundary
                .getH());
        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(-8.0D, -8.0D, 0.0D);
        (Minecraft.getInstance()).textureManager.bind(RESEARCHES_RESOURCE);
        blit(matrixStack, this.x + 15, this.y + 15, this.style.u, this.style.v, 16, 16);


        matrixStack.popPose();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\widget\ResearchWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */