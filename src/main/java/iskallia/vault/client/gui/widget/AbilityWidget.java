// 
// Decompiled by Procyon v0.6.0
// 

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
import iskallia.vault.skill.ability.config.AbilityConfig;
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

public class AbilityWidget extends Widget implements ConnectableWidget, ComponentWidget
{
    private static final int PIP_SIZE = 8;
    private static final int GAP_SIZE = 2;
    private static final int ICON_SIZE = 30;
    private static final int MAX_PIPs_INLINE = 4;
    private static final ResourceLocation SKILL_WIDGET_RESOURCE;
    private static final ResourceLocation ABILITIES_RESOURCE;
    private final String abilityName;
    private final AbilityTree abilityTree;
    private final SkillStyle style;
    private boolean selected;
    private boolean hoverable;
    private boolean renderPips;
    
    public AbilityWidget(final String abilityName, final AbilityTree abilityTree, final SkillStyle style) {
        super(style.x, style.y, 48, pipRowCount(abilityTree.getNodeOf(AbilityRegistry.getAbility(abilityName)).getLevel()) * 10 - 2, (ITextComponent)new StringTextComponent("the_vault.widgets.ability"));
        this.selected = false;
        this.hoverable = true;
        this.renderPips = true;
        this.abilityName = abilityName;
        this.abilityTree = abilityTree;
        this.style = style;
    }
    
    public AbilityNode<?, ?> makeAbilityNode() {
        final AbilityGroup<?, ?> group = this.getAbilityGroup();
        final AbilityNode<?, ?> node = this.abilityTree.getNodeOf(group);
        int level = node.getLevel();
        if (node.isLearned() && !this.isSpecialization()) {
            level = Math.min(level + 1, group.getMaxLevel());
        }
        return new AbilityNode<>(this.getAbility().getAbilityGroupName(), level, this.isSpecialization() ? this.abilityName : null);
    }
    
    public AbilityGroup<?, ?> getAbilityGroup() {
        return ModConfigs.ABILITIES.getAbilityGroupByName(this.getAbility().getAbilityGroupName());
    }
    
    private AbilityEffect<?> getAbility() {
        return AbilityRegistry.getAbility(this.abilityName);
    }
    
    public String getAbilityName() {
        return this.abilityName;
    }
    
    public boolean isSpecialization() {
        return !this.getAbility().getAbilityGroupName().equals(this.abilityName);
    }
    
    public AbilityTree getAbilityTree() {
        return this.abilityTree;
    }
    
    public boolean isLocked() {
        if (this.isSpecialization()) {
            final AbilityNode<?, ?> existing = this.abilityTree.getNodeOf(this.getAbility());
            if (!existing.isLearned() || (existing.getSpecialization() != null && !existing.getSpecialization().equals(this.abilityName))) {
                return true;
            }
        }
        return VaultBarOverlay.vaultLevel < ((AbilityConfig)this.makeAbilityNode().getAbilityConfig()).getLevelRequirement();
    }
    
    public Point2D.Double getRenderPosition() {
        return new Point2D.Double(this.x - this.getRenderWidth() / 2.0, this.y - this.getRenderHeight() / 2.0);
    }
    
    public double getRenderWidth() {
        return 15.0;
    }
    
    public double getRenderHeight() {
        return 15.0;
    }
    
    public int getClickableWidth() {
        final int onlyIconWidth = 34;
        final int pipLineWidth = Math.min(this.getAbilityGroup().getMaxLevel(), 4) * 10;
        return this.hasPips() ? Math.max(pipLineWidth, onlyIconWidth) : onlyIconWidth;
    }
    
    public int getClickableHeight() {
        int height = 34;
        if (this.hasPips()) {
            final int lines = pipRowCount(this.getAbilityGroup().getMaxLevel());
            height += 2;
            height += lines * 8 + (lines - 1) * 2;
        }
        return height;
    }
    
    public Rectangle getClickableBounds() {
        return new Rectangle(this.x - this.getClickableWidth() / 2, this.y - 15 - 2, this.getClickableWidth(), this.getClickableHeight());
    }
    
    public boolean hasPips() {
        return this.renderPips && !this.isSpecialization() && this.getAbilityGroup().getMaxLevel() > 1;
    }
    
    public void setHoverable(final boolean hoverable) {
        this.hoverable = hoverable;
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
    
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderIcon(matrixStack, mouseX, mouseY, partialTicks);
        if (this.hasPips()) {
            this.renderPips(matrixStack, mouseX, mouseY, partialTicks);
        }
    }
    
    private void renderHover(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        if (!this.hoverable || !this.getClickableBounds().contains(mouseX, mouseY)) {
            return;
        }
        final AbilityNode<?, ?> node = this.makeAbilityNode();
        final AbilityNode<?, ?> existing = this.abilityTree.getNodeOf(this.getAbility());
        final List<ITextComponent> tTip = new ArrayList<ITextComponent>();
        tTip.add((ITextComponent)new StringTextComponent(node.getGroup().getParentName()));
        if (this.isSpecialization()) {
            tTip.add((ITextComponent)new StringTextComponent(node.getSpecializationName()).withStyle(TextFormatting.ITALIC).withStyle(TextFormatting.GOLD));
        }
        if (this.isLocked() && this.isSpecialization() && existing.getSpecialization() != null && !existing.getSpecialization().equals(node.getSpecialization())) {
            tTip.add((ITextComponent)new StringTextComponent("Specialization already in use:").withStyle(TextFormatting.RED));
            tTip.add((ITextComponent)new StringTextComponent(existing.getSpecializationName()).withStyle(TextFormatting.RED));
        }
        final int levelRequirement = ((AbilityConfig)node.getGroup().getAbilityConfig(this.abilityName, Math.max(existing.getLevel() - 1, 0))).getLevelRequirement();
        if (levelRequirement > 0) {
            TextFormatting color;
            if (VaultBarOverlay.vaultLevel < levelRequirement) {
                color = TextFormatting.RED;
            }
            else {
                color = TextFormatting.GREEN;
            }
            tTip.add((ITextComponent)new StringTextComponent("Requires level: " + levelRequirement).withStyle(color));
        }
        matrixStack.pushPose();
        matrixStack.translate((double)this.x, (double)(this.y - 15), 0.0);
        GuiUtils.drawHoveringText(matrixStack, (List)tTip, 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, -1, Minecraft.getInstance().font);
        matrixStack.popPose();
        RenderSystem.enableBlend();
    }
    
    public void renderIcon(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final ResourceBoundary resourceBoundary = this.style.frameType.getResourceBoundary();
        matrixStack.pushPose();
        matrixStack.translate(-15.0, -15.0, 0.0);
        Minecraft.getInstance().textureManager.bind(resourceBoundary.getResource());
        final AbilityNode<?, ?> abilityNode = this.abilityTree.getNodeOf(this.getAbility());
        final boolean locked = this.isLocked();
        int vOffset = 0;
        if (this.isSpecialization() && abilityNode.isLearned() && this.abilityName.equals(abilityNode.getSpecialization())) {
            vOffset = 31;
        }
        else if (locked && (this.isSpecialization() || !abilityNode.isLearned())) {
            vOffset = 62;
        }
        else if (this.selected || this.getClickableBounds().contains(mouseX, mouseY)) {
            vOffset = -31;
        }
        else if (this.isSpecialization()) {
            if (this.abilityName.equals(abilityNode.getSpecialization())) {
                vOffset = 31;
            }
        }
        else if (abilityNode.getLevel() >= 1) {
            vOffset = 31;
        }
        this.blit(matrixStack, this.x, this.y, resourceBoundary.getU(), resourceBoundary.getV() + vOffset, resourceBoundary.getW(), resourceBoundary.getH());
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate(-8.0, -8.0, 0.0);
        Minecraft.getInstance().textureManager.bind(AbilityWidget.ABILITIES_RESOURCE);
        this.blit(matrixStack, this.x, this.y, this.style.u, this.style.v, 16, 16);
        matrixStack.popPose();
    }
    
    public void renderPips(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        Minecraft.getInstance().textureManager.bind(AbilityWidget.SKILL_WIDGET_RESOURCE);
        final AbilityGroup<?, ?> group = this.getAbilityGroup();
        final int rowCount = pipRowCount(group.getMaxLevel());
        int remainingPips = group.getMaxLevel();
        int remainingFilledPips = this.abilityTree.getNodeOf(group).getLevel();
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
        matrixStack.translate((double)x, (double)y, 0.0);
        matrixStack.translate((double)(-lineWidth / 2.0f), -4.0, 0.0);
        for (int i = 0; i < count; ++i) {
            if (remainingFilled > 0) {
                this.blit(matrixStack, 0, 0, 1, 133, 8, 8);
                --remainingFilled;
            }
            else {
                this.blit(matrixStack, 0, 0, 1, 124, 8, 8);
            }
            matrixStack.translate(10.0, 0.0, 0.0);
        }
        matrixStack.popPose();
    }
    
    public static int pipRowCount(final int level) {
        return (int)Math.ceil(level / 4.0f);
    }
    
    static {
        SKILL_WIDGET_RESOURCE = new ResourceLocation("the_vault", "textures/gui/skill-widget.png");
        ABILITIES_RESOURCE = new ResourceLocation("the_vault", "textures/gui/abilities.png");
    }
}
