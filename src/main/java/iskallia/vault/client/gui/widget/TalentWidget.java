package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.widget.connect.ConnectableWidget;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.talent.ArchetypeTalentGroup;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.PlayerTalent;
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

public class TalentWidget extends Widget implements ConnectableWidget, ComponentWidget {
    private static final int PIP_SIZE = 8;
    private static final int GAP_SIZE = 2;
    private static final int ICON_SIZE = 30;
    private static final int MAX_PIPs_INLINE = 4;
    private static final ResourceLocation SKILL_WIDGET_RESOURCE;
    private static final ResourceLocation TALENTS_RESOURCE;
    TalentGroup<?> talentGroup;
    TalentTree talentTree;
    boolean locked;
    SkillStyle style;
    boolean selected;
    private boolean renderPips;

    public TalentWidget(final TalentGroup<?> talentGroup, final TalentTree talentTree, final SkillStyle style) {
        super(style.x, style.y, 48, pipRowCount(talentTree.getNodeOf(talentGroup).getLevel()) * 10 - 2, new StringTextComponent("the_vault.widgets.talent"));
        this.renderPips = true;
        this.style = style;
        this.talentGroup = talentGroup;
        this.talentTree = talentTree;
        final TalentNode<?> existingNode = talentTree.getNodeOf(talentGroup);
        this.locked = (ModConfigs.SKILL_GATES.getGates().isLocked(talentGroup, talentTree) || VaultBarOverlay.vaultLevel < talentGroup.getTalent(existingNode.getLevel() + 1).getLevelRequirement());
        this.selected = false;
    }

    public TalentGroup<?> getTalentGroup() {
        return this.talentGroup;
    }

    public TalentTree getTalentTree() {
        return this.talentTree;
    }

    public int getClickableWidth() {
        final int onlyIconWidth = 34;
        final int pipLineWidth = Math.min(this.talentGroup.getMaxLevel(), 4) * 10;
        return this.hasPips() ? Math.max(pipLineWidth, onlyIconWidth) : onlyIconWidth;
    }

    public int getClickableHeight() {
        int height = 34;
        if (this.hasPips()) {
            final int lines = pipRowCount(this.talentGroup.getMaxLevel());
            height += 2;
            height += lines * 8 + (lines - 1) * 2;
        }
        return height;
    }

    public Point2D.Double getRenderPosition() {
        return new Point2D.Double(this.x - this.getRenderWidth() / 2.0, this.y - this.getRenderHeight() / 2.0);
    }

    public double getRenderWidth() {
        return 22.0;
    }

    public double getRenderHeight() {
        return 22.0;
    }

    public Rectangle getClickableBounds() {
        return new Rectangle(this.x - this.getClickableWidth() / 2, this.y - 15 - 2, this.getClickableWidth(), this.getClickableHeight());
    }

    public boolean hasPips() {
        return this.renderPips && this.talentGroup.getMaxLevel() > 1;
    }

    public void setRenderPips(final boolean renderPips) {
        this.renderPips = renderPips;
    }

    public boolean isMouseOver(final double mouseX, final double mouseY) {
        return this.getClickableBounds().contains(mouseX, mouseY);
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

    private void renderHover(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        if (!this.isMouseOver(mouseX, mouseY)) {
            return;
        }
        final TalentNode<?> node = this.talentTree.getNodeOf(this.talentGroup);
        if (node == null) {
            return;
        }
        final List<ITextComponent> tTip = new ArrayList<ITextComponent>();
        tTip.add(new StringTextComponent(node.getGroup().getParentName()));
        if (this.locked) {
            final List<TalentGroup<?>> preconditions = ModConfigs.SKILL_GATES.getGates().getDependencyTalents(this.talentGroup.getParentName());
            if (!preconditions.isEmpty()) {
                tTip.add(new StringTextComponent("Requires:").withStyle(TextFormatting.RED));
                preconditions.forEach(talent -> {

                    final StringTextComponent stringTextComponent = new StringTextComponent("- " + talent.getParentName());
                    tTip.add(stringTextComponent.withStyle(TextFormatting.RED));
                    return;
                });
            }
        }
        final List<TalentGroup<?>> conflicts = ModConfigs.SKILL_GATES.getGates().getLockedByTalents(this.talentGroup.getParentName());
        if (!conflicts.isEmpty()) {
            tTip.add(new StringTextComponent("Cannot be unlocked alongside:").withStyle(TextFormatting.RED));
            conflicts.forEach(talent -> {

                final StringTextComponent stringTextComponent2 = new StringTextComponent("- " + talent.getParentName());
                tTip.add(stringTextComponent2.withStyle(TextFormatting.RED));
                return;
            });
        }
        if (!node.isLearned() && this.talentGroup instanceof ArchetypeTalentGroup) {
            tTip.add(new StringTextComponent("Cannot be unlocked alongside").withStyle(TextFormatting.RED));
            tTip.add(new StringTextComponent("other archetype talents.").withStyle(TextFormatting.RED));
        }
        if (node.getLevel() < node.getGroup().getMaxLevel()) {
            final int levelRequirement = node.getGroup().getTalent(node.getLevel() + 1).getLevelRequirement();
            if (VaultBarOverlay.vaultLevel < levelRequirement) {
                tTip.add(new StringTextComponent("Requires level: " + levelRequirement).withStyle(TextFormatting.RED));
            }
        }
        matrixStack.pushPose();
        matrixStack.translate(this.x, this.y - 15, 0.0);
        GuiUtils.drawHoveringText(matrixStack, tTip, 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, -1, Minecraft.getInstance().font);
        matrixStack.popPose();
        RenderSystem.enableBlend();
    }

    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderIcon(matrixStack, mouseX, mouseY, partialTicks);
        if (this.hasPips()) {
            this.renderPips(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    public void renderIcon(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final ResourceBoundary resourceBoundary = this.style.frameType.getResourceBoundary();
        matrixStack.pushPose();
        matrixStack.translate(-15.0, -15.0, 0.0);
        Minecraft.getInstance().textureManager.bind(resourceBoundary.getResource());
        final int vOffset = this.locked ? 62 : ((this.selected || this.isMouseOver(mouseX, mouseY)) ? -31 : ((this.talentTree.getNodeOf(this.talentGroup).getLevel() >= 1) ? 31 : 0));
        this.blit(matrixStack, this.x, this.y, resourceBoundary.getU(), resourceBoundary.getV() + vOffset, resourceBoundary.getW(), resourceBoundary.getH());
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate(-8.0, -8.0, 0.0);
        Minecraft.getInstance().textureManager.bind(TalentWidget.TALENTS_RESOURCE);
        this.blit(matrixStack, this.x, this.y, this.style.u, this.style.v, 16, 16);
        matrixStack.popPose();
    }

    public void renderPips(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        Minecraft.getInstance().textureManager.bind(TalentWidget.SKILL_WIDGET_RESOURCE);
        final int rowCount = pipRowCount(this.talentGroup.getMaxLevel());
        int remainingPips = this.talentGroup.getMaxLevel();
        int remainingFilledPips = this.talentTree.getNodeOf(this.talentGroup).getLevel();
        for (int r = 0; r < rowCount; ++r) {
            this.renderPipLine(matrixStack, this.x, this.y + 15 + 4 + r * 10, Math.min(4, remainingPips), Math.min(4, remainingFilledPips));
            remainingPips -= 4;
            remainingFilledPips -= 4;
        }
    }

    public void renderPipLine(final MatrixStack matrixStack, final int x, final int y, final int count, final int filledCount) {
        final int lineWidth = count * 8 + (count - 1) * 2;
        int remainingFilled = filledCount;
        matrixStack.pushPose();
        matrixStack.translate(x, y, 0.0);
        matrixStack.translate(-lineWidth / 2.0f, -4.0, 0.0);
        for (int i = 0; i < count; ++i) {
            if (remainingFilled > 0) {
                this.blit(matrixStack, 0, 0, 1, 133, 8, 8);
                --remainingFilled;
            } else {
                this.blit(matrixStack, 0, 0, 1, 124, 8, 8);
            }
            matrixStack.translate(10.0, 0.0, 0.0);
        }
        matrixStack.popPose();
    }

    public static int pipRowCount(final int level) {
        return (int) Math.ceil(level / 4.0f);
    }

    static {
        SKILL_WIDGET_RESOURCE = new ResourceLocation("the_vault", "textures/gui/skill-widget.png");
        TALENTS_RESOURCE = new ResourceLocation("the_vault", "textures/gui/talents.png");
    }
}
