package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.widget.connect.ConnectableWidget;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityRegistry;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.effect.AbilityEffect;
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

public class AbilityWidget
        extends Widget
        implements ConnectableWidget, ComponentWidget {
    private static final int PIP_SIZE = 8;
    private static final int GAP_SIZE = 2;
    private static final int ICON_SIZE = 30;
    private static final int MAX_PIPs_INLINE = 4;
    private static final ResourceLocation SKILL_WIDGET_RESOURCE = new ResourceLocation("the_vault", "textures/gui/skill-widget.png");
    private static final ResourceLocation ABILITIES_RESOURCE = new ResourceLocation("the_vault", "textures/gui/abilities.png");

    private final String abilityName;

    private final AbilityTree abilityTree;
    private final SkillStyle style;
    private boolean selected = false;
    private boolean hoverable = true;
    private boolean renderPips = true;

    public AbilityWidget(String abilityName, AbilityTree abilityTree, SkillStyle style) {
        super(style.x, style.y, 48,

                pipRowCount(abilityTree.getNodeOf(AbilityRegistry.getAbility(abilityName)).getLevel()) * 10 - 2, (ITextComponent) new StringTextComponent("the_vault.widgets.ability"));

        this.abilityName = abilityName;
        this.abilityTree = abilityTree;
        this.style = style;
    }

    public AbilityNode<?, ?> makeAbilityNode() {
        AbilityGroup<?, ?> group = getAbilityGroup();
        AbilityNode<?, ?> node = this.abilityTree.getNodeOf(group);
        int level = node.getLevel();
        if (node.isLearned() && !isSpecialization()) {
            level = Math.min(level + 1, group.getMaxLevel());
        }
        return new AbilityNode(getAbility().getAbilityGroupName(), level, isSpecialization() ? this.abilityName : null);
    }

    public AbilityGroup<?, ?> getAbilityGroup() {
        return ModConfigs.ABILITIES.getAbilityGroupByName(getAbility().getAbilityGroupName());
    }

    private AbilityEffect<?> getAbility() {
        return AbilityRegistry.getAbility(this.abilityName);
    }

    public String getAbilityName() {
        return this.abilityName;
    }


    public boolean isSpecialization() {
        return !getAbility().getAbilityGroupName().equals(this.abilityName);
    }

    public AbilityTree getAbilityTree() {
        return this.abilityTree;
    }

    public boolean isLocked() {
        if (isSpecialization()) {
            AbilityNode<?, ?> existing = this.abilityTree.getNodeOf(getAbility());
            if (!existing.isLearned() || (existing.getSpecialization() != null && !existing.getSpecialization().equals(this.abilityName))) {
                return true;
            }
        }
        return (VaultBarOverlay.vaultLevel < makeAbilityNode().getAbilityConfig().getLevelRequirement());
    }


    public Point2D.Double getRenderPosition() {
        return new Point2D.Double(this.x - getRenderWidth() / 2.0D, this.y - getRenderHeight() / 2.0D);
    }


    public double getRenderWidth() {
        return 15.0D;
    }


    public double getRenderHeight() {
        return 15.0D;
    }

    public int getClickableWidth() {
        int onlyIconWidth = 34;
        int pipLineWidth = Math.min(getAbilityGroup().getMaxLevel(), 4) * 10;
        return hasPips() ?
                Math.max(pipLineWidth, onlyIconWidth) : onlyIconWidth;
    }


    public int getClickableHeight() {
        int height = 34;
        if (hasPips()) {
            int lines = pipRowCount(getAbilityGroup().getMaxLevel());
            height += 2;
            height += lines * 8 + (lines - 1) * 2;
        }
        return height;
    }


    public Rectangle getClickableBounds() {
        return new Rectangle(this.x - getClickableWidth() / 2, this.y - 15 - 2,
                getClickableWidth(), getClickableHeight());
    }

    public boolean hasPips() {
        return (this.renderPips && !isSpecialization() && getAbilityGroup().getMaxLevel() > 1);
    }

    public void setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
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


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderIcon(matrixStack, mouseX, mouseY, partialTicks);
        if (hasPips()) {
            renderPips(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    private void renderHover(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (!this.hoverable || !getClickableBounds().contains(mouseX, mouseY)) {
            return;
        }

        AbilityNode<?, ?> node = makeAbilityNode();
        AbilityNode<?, ?> existing = this.abilityTree.getNodeOf(getAbility());
        List<ITextComponent> tTip = new ArrayList<>();
        tTip.add(new StringTextComponent(node.getGroup().getParentName()));
        if (isSpecialization()) {
            tTip.add((new StringTextComponent(node.getSpecializationName())).withStyle(TextFormatting.ITALIC).withStyle(TextFormatting.GOLD));
        }
        if (isLocked() &&
                isSpecialization() &&
                existing.getSpecialization() != null && !existing.getSpecialization().equals(node.getSpecialization())) {
            tTip.add((new StringTextComponent("Specialization already in use:")).withStyle(TextFormatting.RED));
            tTip.add((new StringTextComponent(existing.getSpecializationName())).withStyle(TextFormatting.RED));
        }


        int levelRequirement = node.getGroup().getAbilityConfig(this.abilityName, Math.max(existing.getLevel() - 1, 0)).getLevelRequirement();
        if (levelRequirement > 0) {
            TextFormatting color;
            if (VaultBarOverlay.vaultLevel < levelRequirement) {
                color = TextFormatting.RED;
            } else {
                color = TextFormatting.GREEN;
            }
            tTip.add((new StringTextComponent("Requires level: " + levelRequirement)).withStyle(color));
        }

        matrixStack.pushPose();
        matrixStack.translate(this.x, (this.y - 15), 0.0D);
        GuiUtils.drawHoveringText(matrixStack, tTip, 0, 0, 2147483647, 2147483647, -1,


                (Minecraft.getInstance()).font);
        matrixStack.popPose();
        RenderSystem.enableBlend();
    }

    public void renderIcon(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        ResourceBoundary resourceBoundary = this.style.frameType.getResourceBoundary();

        matrixStack.pushPose();
        matrixStack.translate(-15.0D, -15.0D, 0.0D);
        (Minecraft.getInstance()).textureManager.bind(resourceBoundary.getResource());
        AbilityNode<?, ?> abilityNode = this.abilityTree.getNodeOf(getAbility());

        boolean locked = isLocked();
        int vOffset = 0;
        if (isSpecialization() && abilityNode.isLearned() && this.abilityName.equals(abilityNode.getSpecialization())) {
            vOffset = 31;
        } else if (locked && (isSpecialization() || !abilityNode.isLearned())) {
            vOffset = 62;
        } else if (this.selected || getClickableBounds().contains(mouseX, mouseY)) {
            vOffset = -31;
        } else if (isSpecialization()) {
            if (this.abilityName.equals(abilityNode.getSpecialization())) {
                vOffset = 31;
            }
        } else if (abilityNode.getLevel() >= 1) {
            vOffset = 31;
        }
        blit(matrixStack, this.x, this.y, resourceBoundary
                .getU(), resourceBoundary
                .getV() + vOffset, resourceBoundary
                .getW(), resourceBoundary
                .getH());
        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(-8.0D, -8.0D, 0.0D);
        (Minecraft.getInstance()).textureManager.bind(ABILITIES_RESOURCE);
        blit(matrixStack, this.x, this.y, this.style.u, this.style.v, 16, 16);


        matrixStack.popPose();
    }

    public void renderPips(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        (Minecraft.getInstance()).textureManager.bind(SKILL_WIDGET_RESOURCE);

        AbilityGroup<?, ?> group = getAbilityGroup();
        int rowCount = pipRowCount(group.getMaxLevel());
        int remainingPips = group.getMaxLevel();
        int remainingFilledPips = this.abilityTree.getNodeOf(group).getLevel();
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\widget\AbilityWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */