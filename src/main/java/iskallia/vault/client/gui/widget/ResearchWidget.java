// 
// Decompiled by Procyon v0.6.0
// 

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

public class ResearchWidget extends Widget implements ConnectableWidget, ComponentWidget {
    private static final int ICON_SIZE = 30;
    private static final ResourceLocation SKILL_WIDGET_RESOURCE;
    public static final ResourceLocation RESEARCHES_RESOURCE;
    private final String researchName;
    private final ResearchTree researchTree;
    private final boolean locked;
    private final SkillStyle style;
    private boolean selected;
    private boolean hoverable;

    public ResearchWidget(final String researchName, final ResearchTree researchTree, final SkillStyle style) {
        super(style.x, style.y, 30, 30, (ITextComponent) new StringTextComponent("the_vault.widgets.research"));
        this.selected = false;
        this.hoverable = true;
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
        return new Point2D.Double(this.x + 2.5, this.y + 2.5);
    }

    public double getRenderWidth() {
        return 25.0;
    }

    public double getRenderHeight() {
        return 25.0;
    }

    public void setHoverable(final boolean hoverable) {
        this.hoverable = hoverable;
    }

    public boolean isMouseOver(final double mouseX, final double mouseY) {
        return this.hoverable && this.getClickableBounds().contains(mouseX, mouseY);
    }

    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.selected) {
            return false;
        }
        this.playDownSound(Minecraft.getInstance().getSoundManager());
        return true;
    }

    public void select() {
        this.selected = true;
    }

    public void deselect() {
        this.selected = false;
    }

    public void renderWidget(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks, final List<Runnable> postContainerRender) {
        this.render(matrixStack, mouseX, mouseY, partialTicks);
        final Matrix4f current = matrixStack.last().pose().copy();
        postContainerRender.add(() -> {
            RenderSystem.pushMatrix();
            RenderSystem.multMatrix(current);
            this.renderHover(matrixStack, mouseX, mouseY, partialTicks);
            RenderSystem.popMatrix();
        });
    }

    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderIcon(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void renderHover(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        if (!this.isMouseOver(mouseX, mouseY)) {
            return;
        }
        final List<ITextComponent> tTip = new ArrayList<ITextComponent>();
        tTip.add((ITextComponent) new StringTextComponent(this.researchName));
        if (this.locked) {
            final List<Research> preconditions = ModConfigs.SKILL_GATES.getGates().getDependencyResearches(this.researchName);
            if (!preconditions.isEmpty()) {
                tTip.add((ITextComponent) new StringTextComponent("Requires:").withStyle(TextFormatting.RED));
                preconditions.forEach(research -> {
                    final StringTextComponent stringTextComponent = new StringTextComponent("- " + research.getName());
                    tTip.add(stringTextComponent.withStyle(TextFormatting.RED));
                    return;
                });
            }
        }
        final List<Research> conflicts = ModConfigs.SKILL_GATES.getGates().getLockedByResearches(this.researchName);
        if (!conflicts.isEmpty()) {
            tTip.add((ITextComponent) new StringTextComponent("Cannot be unlocked alongside:").withStyle(TextFormatting.RED));
            conflicts.forEach(research -> {
                final StringTextComponent stringTextComponent2 = new StringTextComponent("- " + research.getName());
                tTip.add(stringTextComponent2.withStyle(TextFormatting.RED));
                return;
            });
        }
        GuiUtils.drawHoveringText(matrixStack, (List) tTip, this.x + 15, this.y + 15, Integer.MAX_VALUE, Integer.MAX_VALUE, -1, Minecraft.getInstance().font);
        RenderSystem.enableBlend();
    }

    private void renderIcon(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final ResourceBoundary resourceBoundary = this.style.frameType.getResourceBoundary();
        matrixStack.pushPose();
        Minecraft.getInstance().textureManager.bind(resourceBoundary.getResource());
        final int vOffset = this.locked ? 62 : ((this.selected || this.isMouseOver(mouseX, mouseY)) ? -31 : (this.researchTree.getResearchesDone().contains(this.researchName) ? 31 : 0));
        this.blit(matrixStack, this.x, this.y, resourceBoundary.getU(), resourceBoundary.getV() + vOffset, resourceBoundary.getW(), resourceBoundary.getH());
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate(-8.0, -8.0, 0.0);
        Minecraft.getInstance().textureManager.bind(ResearchWidget.RESEARCHES_RESOURCE);
        this.blit(matrixStack, this.x + 15, this.y + 15, this.style.u, this.style.v, 16, 16);
        matrixStack.popPose();
    }

    static {
        SKILL_WIDGET_RESOURCE = new ResourceLocation("the_vault", "textures/gui/skill-widget.png");
        RESEARCHES_RESOURCE = new ResourceLocation("the_vault", "textures/gui/researches.png");
    }
}
