package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.widget.connect.ConnectableWidget;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
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

public class TalentWidget
        extends Widget
        implements ConnectableWidget, ComponentWidget {
    private static final int PIP_SIZE = 8;
    private static final int GAP_SIZE = 2;
    private static final int ICON_SIZE = 30;
    private static final int MAX_PIPs_INLINE = 4;
    private static final ResourceLocation SKILL_WIDGET_RESOURCE = new ResourceLocation("the_vault", "textures/gui/skill-widget.png");
    private static final ResourceLocation TALENTS_RESOURCE = new ResourceLocation("the_vault", "textures/gui/talents.png");

    TalentGroup<?> talentGroup;

    TalentTree talentTree;
    boolean locked;
    SkillStyle style;
    boolean selected;
    private boolean renderPips = true;

    public TalentWidget(TalentGroup<?> talentGroup, TalentTree talentTree, SkillStyle style) {
        super(style.x, style.y, 48,

                pipRowCount(talentTree.getNodeOf(talentGroup).getLevel()) * 10 - 2, (ITextComponent) new StringTextComponent("the_vault.widgets.talent"));

        this.style = style;
        this.talentGroup = talentGroup;
        this.talentTree = talentTree;
        TalentNode<?> existingNode = talentTree.getNodeOf(talentGroup);
        this
                .locked = (ModConfigs.SKILL_GATES.getGates().isLocked(talentGroup, talentTree) || VaultBarOverlay.vaultLevel < talentGroup.getTalent(existingNode.getLevel() + 1).getLevelRequirement());
        this.selected = false;
    }

    public TalentGroup<?> getTalentGroup() {
        return this.talentGroup;
    }

    public TalentTree getTalentTree() {
        return this.talentTree;
    }

    public int getClickableWidth() {
        int onlyIconWidth = 34;
        int pipLineWidth = Math.min(this.talentGroup.getMaxLevel(), 4) * 10;
        return hasPips() ?
                Math.max(pipLineWidth, onlyIconWidth) : onlyIconWidth;
    }


    public int getClickableHeight() {
        int height = 34;
        if (hasPips()) {
            int lines = pipRowCount(this.talentGroup.getMaxLevel());
            height += 2;
            height += lines * 8 + (lines - 1) * 2;
        }
        return height;
    }


    public Point2D.Double getRenderPosition() {
        return new Point2D.Double(this.x - getRenderWidth() / 2.0D, this.y - getRenderHeight() / 2.0D);
    }


    public double getRenderWidth() {
        return 22.0D;
    }


    public double getRenderHeight() {
        return 22.0D;
    }


    public Rectangle getClickableBounds() {
        return new Rectangle(this.x - getClickableWidth() / 2, this.y - 15 - 2,
                getClickableWidth(), getClickableHeight());
    }

    public boolean hasPips() {
        return (this.renderPips && this.talentGroup.getMaxLevel() > 1);
    }

    public void setRenderPips(boolean renderPips) {
        this.renderPips = renderPips;
    }


    public boolean isMouseOver(double mouseX, double mouseY) {
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

    private void renderHover(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (!isMouseOver(mouseX, mouseY)) {
            return;
        }
        TalentNode<?> node = this.talentTree.getNodeOf(this.talentGroup);
        if (node == null) {
            return;
        }

        List<ITextComponent> tTip = new ArrayList<>();
        tTip.add(new StringTextComponent(node.getGroup().getParentName()));

        if (this.locked) {
            List<TalentGroup<?>> preconditions = ModConfigs.SKILL_GATES.getGates().getDependencyTalents(this.talentGroup.getParentName());
            if (!preconditions.isEmpty()) {
                tTip.add((new StringTextComponent("Requires:")).withStyle(TextFormatting.RED));
                preconditions.forEach(talent -> tTip.add((new StringTextComponent("- " + talent.getParentName())).withStyle(TextFormatting.RED)));
            }
        }


        List<TalentGroup<?>> conflicts = ModConfigs.SKILL_GATES.getGates().getLockedByTalents(this.talentGroup.getParentName());
        if (!conflicts.isEmpty()) {
            tTip.add((new StringTextComponent("Cannot be unlocked alongside:")).withStyle(TextFormatting.RED));
            conflicts.forEach(talent -> tTip.add((new StringTextComponent("- " + talent.getParentName())).withStyle(TextFormatting.RED)));
        }


        if (!node.isLearned() && this.talentGroup instanceof iskallia.vault.skill.talent.ArchetypeTalentGroup) {
            tTip.add((new StringTextComponent("Cannot be unlocked alongside")).withStyle(TextFormatting.RED));
            tTip.add((new StringTextComponent("other archetype talents.")).withStyle(TextFormatting.RED));
        }
        if (node.getLevel() < node.getGroup().getMaxLevel()) {
            int levelRequirement = node.getGroup().getTalent(node.getLevel() + 1).getLevelRequirement();
            if (VaultBarOverlay.vaultLevel < levelRequirement) {
                tTip.add((new StringTextComponent("Requires level: " + levelRequirement)).withStyle(TextFormatting.RED));
            }
        }

        matrixStack.pushPose();
        matrixStack.translate(this.x, (this.y - 15), 0.0D);
        GuiUtils.drawHoveringText(matrixStack, tTip, 0, 0, 2147483647, 2147483647, -1,


                (Minecraft.getInstance()).font);
        matrixStack.popPose();

        RenderSystem.enableBlend();
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderIcon(matrixStack, mouseX, mouseY, partialTicks);
        if (hasPips()) {
            renderPips(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    public void renderIcon(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        ResourceBoundary resourceBoundary = this.style.frameType.getResourceBoundary();

        matrixStack.pushPose();
        matrixStack.translate(-15.0D, -15.0D, 0.0D);
        (Minecraft.getInstance()).textureManager.bind(resourceBoundary.getResource());


        int vOffset = this.locked ? 62 : ((this.selected || isMouseOver(mouseX, mouseY)) ? -31 : ((this.talentTree.getNodeOf(this.talentGroup).getLevel() >= 1) ? 31 : 0));
        blit(matrixStack, this.x, this.y, resourceBoundary
                .getU(), resourceBoundary
                .getV() + vOffset, resourceBoundary
                .getW(), resourceBoundary
                .getH());
        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(-8.0D, -8.0D, 0.0D);
        (Minecraft.getInstance()).textureManager.bind(TALENTS_RESOURCE);
        blit(matrixStack, this.x, this.y, this.style.u, this.style.v, 16, 16);


        matrixStack.popPose();
    }

    public void renderPips(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        (Minecraft.getInstance()).textureManager.bind(SKILL_WIDGET_RESOURCE);

        int rowCount = pipRowCount(this.talentGroup.getMaxLevel());
        int remainingPips = this.talentGroup.getMaxLevel();
        int remainingFilledPips = this.talentTree.getNodeOf(this.talentGroup).getLevel();
        for (int r = 0; r < rowCount; r++) {
            renderPipLine(matrixStack, this.x, this.y + 15 + 4 + r * 10,


                    Math.min(4, remainingPips),
                    Math.min(4, remainingFilledPips));

            remainingPips -= 4;
            remainingFilledPips -= 4;
        }
    }

    public void renderPipLine(MatrixStack matrixStack, int x, int y, int count, int filledCount) {
        int lineWidth = count * 8 + (count - 1) * 2;
        int remainingFilled = filledCount;

        matrixStack.pushPose();
        matrixStack.translate(x, y, 0.0D);
        matrixStack.translate((-lineWidth / 2.0F), -4.0D, 0.0D);

        for (int i = 0; i < count; i++) {
            if (remainingFilled > 0) {
                blit(matrixStack, 0, 0, 1, 133, 8, 8);

                remainingFilled--;
            } else {

                blit(matrixStack, 0, 0, 1, 124, 8, 8);
            }

            matrixStack.translate(10.0D, 0.0D, 0.0D);
        }

        matrixStack.popPose();
    }

    public static int pipRowCount(int level) {
        return (int) Math.ceil((level / 4.0F));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\widget\TalentWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */