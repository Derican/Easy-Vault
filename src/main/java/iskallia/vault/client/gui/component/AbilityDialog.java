// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.client.gui.tab.AbilitiesTab;
import iskallia.vault.client.gui.tab.SkillTab;
import iskallia.vault.client.gui.widget.AbilityWidget;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.init.ModSounds;
import iskallia.vault.network.message.AbilitySelectSpecializationMessage;
import iskallia.vault.network.message.AbilityUpgradeMessage;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityRegistry;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.AbilityConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.*;

import java.awt.*;

public class AbilityDialog extends ComponentDialog
{
    private final AbilityTree abilityTree;
    private String selectedAbility;
    private AbilityWidget selectedAbilityWidget;
    
    public AbilityDialog(final AbilityTree abilityTree, final SkillTreeScreen skillTreeScreen) {
        super(skillTreeScreen);
        this.selectedAbility = null;
        this.selectedAbilityWidget = null;
        this.abilityTree = abilityTree;
    }
    
    @Override
    public Point getIconUV() {
        return new Point(32, 60);
    }
    
    @Override
    public int getHeaderHeight() {
        return this.selectedAbilityWidget.getClickableBounds().height;
    }
    
    @Override
    public SkillTab createTab() {
        return new AbilitiesTab(this, this.getSkillTreeScreen());
    }
    
    @Override
    public void refreshWidgets() {
        if (this.selectedAbility != null) {
            final SkillStyle abilityStyle = ModConfigs.ABILITIES_GUI.getStyles().get(this.selectedAbility);
            (this.selectedAbilityWidget = new AbilityWidget(this.selectedAbility, this.abilityTree, abilityStyle)).setHoverable(false);
            this.selectedAbilityWidget.setRenderPips(false);
            final AbilityNode<?, ?> existingNode = this.abilityTree.getNodeOf(AbilityRegistry.getAbility(this.selectedAbility));
            final AbilityNode<?, ?> targetAbilityNode = this.selectedAbilityWidget.makeAbilityNode();
            final AbilityGroup<?, ?> targetAbilityGroup = targetAbilityNode.getGroup();
            String buttonText;
            Button.IPressable pressAction;
            boolean activeState;
            if (targetAbilityNode.getSpecialization() != null) {
                buttonText = "Select Specialization";
                pressAction = (button -> this.selectSpecialization());
                activeState = (existingNode.getSpecialization() == null && existingNode.isLearned() && VaultBarOverlay.vaultLevel >= ((AbilityConfig)targetAbilityNode.getAbilityConfig()).getLevelRequirement());
            }
            else {
                if (!targetAbilityNode.isLearned()) {
                    buttonText = "Learn (" + targetAbilityGroup.learningCost() + ")";
                }
                else {
                    buttonText = ((existingNode.getLevel() >= targetAbilityGroup.getMaxLevel()) ? "Fully Learned" : ("Upgrade (" + targetAbilityGroup.levelUpCost(targetAbilityNode.getSpecialization(), targetAbilityNode.getLevel()) + ")"));
                }
                pressAction = (button -> this.upgradeAbility());
                final AbilityConfig ability = (AbilityConfig)targetAbilityNode.getAbilityConfig();
                final int cost = (ability == null) ? targetAbilityGroup.learningCost() : targetAbilityGroup.levelUpCost(targetAbilityNode.getSpecialization(), targetAbilityNode.getLevel());
                activeState = (cost <= VaultBarOverlay.unspentSkillPoints && existingNode.getLevel() < targetAbilityGroup.getMaxLevel() && targetAbilityNode.getLevel() < targetAbilityGroup.getMaxLevel() + 1 && VaultBarOverlay.vaultLevel >= ((AbilityConfig)targetAbilityNode.getAbilityConfig()).getLevelRequirement());
            }
            this.descriptionComponent = new ScrollableContainer(this::renderDescriptions);
            this.selectButton = new Button(10, this.bounds.height - 40, this.bounds.width - 30, 20, (ITextComponent)new StringTextComponent(buttonText), pressAction, (button, matrixStack, x, y) -> {});
            this.selectButton.active = activeState;
        }
    }
    
    public void setAbilityWidget(final String abilityName) {
        this.selectedAbility = abilityName;
        this.refreshWidgets();
    }
    
    private void upgradeAbility() {
        final AbilityNode<?, ?> abilityNode = this.abilityTree.getNodeByName(AbilityRegistry.getAbility(this.selectedAbility).getAbilityGroupName());
        if (abilityNode.getLevel() >= abilityNode.getGroup().getMaxLevel()) {
            return;
        }
        Minecraft.getInstance().player.playSound(abilityNode.isLearned() ? ModSounds.SKILL_TREE_UPGRADE_SFX : ModSounds.SKILL_TREE_LEARN_SFX, 1.0f, 1.0f);
        this.abilityTree.upgradeAbility(null, abilityNode);
        this.refreshWidgets();
        ModNetwork.CHANNEL.sendToServer(new AbilityUpgradeMessage(abilityNode.getGroup().getParentName()));
    }
    
    private void selectSpecialization() {
        final AbilityNode<?, ?> targetNode = this.selectedAbilityWidget.makeAbilityNode();
        final AbilityNode<?, ?> existingNode = this.abilityTree.getNodeByName(AbilityRegistry.getAbility(this.selectedAbility).getAbilityGroupName());
        final String toSelect = targetNode.getSpecialization();
        final String abilityName = existingNode.getGroup().getParentName();
        if (existingNode.getSpecialization() != null && targetNode.getGroup().equals(existingNode.getGroup())) {
            return;
        }
        if (VaultBarOverlay.vaultLevel < ((AbilityConfig)targetNode.getAbilityConfig()).getLevelRequirement()) {
            return;
        }
        Minecraft.getInstance().player.playSound(ModSounds.SKILL_TREE_UPGRADE_SFX, 1.0f, 1.0f);
        this.abilityTree.selectSpecialization(abilityName, toSelect);
        this.getSkillTreeScreen().refreshWidgets();
        ModNetwork.CHANNEL.sendToServer(new AbilitySelectSpecializationMessage(abilityName, toSelect));
    }
    
    @Override
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack, mouseX, mouseY, partialTicks);
        if (this.selectedAbility == null || this.selectedAbilityWidget == null) {
            return;
        }
        matrixStack.pushPose();
        matrixStack.translate((double)(this.bounds.x + 5), (double)(this.bounds.y + 5), 0.0);
        this.renderHeading(matrixStack, mouseX, mouseY, partialTicks);
        this.descriptionComponent.setBounds(this.getDescriptionsBounds());
        this.descriptionComponent.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderFooter(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.popPose();
    }
    
    private void renderHeading(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);
        final FontRenderer fontRenderer = Minecraft.getInstance().font;
        final SkillStyle abilityStyle = ModConfigs.ABILITIES_GUI.getStyles().get(this.selectedAbility);
        final AbilityNode<?, ?> targetAbilityNode = this.selectedAbilityWidget.makeAbilityNode();
        final AbilityNode<?, ?> currentAbilityNode = this.abilityTree.getNodeOf(targetAbilityNode.getGroup());
        boolean learned;
        String abilityName;
        String subText;
        if (targetAbilityNode.getSpecialization() != null) {
            learned = targetAbilityNode.getSpecialization().equals(currentAbilityNode.getSpecialization());
            abilityName = targetAbilityNode.getGroup().getParentName() + ": " + targetAbilityNode.getSpecializationName();
            subText = (learned ? "Selected" : "Not selected");
        }
        else {
            learned = targetAbilityNode.isLearned();
            abilityName = targetAbilityNode.getGroup().getParentName();
            if (!learned) {
                subText = "Not learned yet";
            }
            else {
                subText = "Level: " + currentAbilityNode.getLevel() + "/" + targetAbilityNode.getGroup().getMaxLevel();
            }
        }
        final Rectangle abilityBounds = this.selectedAbilityWidget.getClickableBounds();
        UIHelper.renderContainerBorder(this, matrixStack, this.getHeadingBounds(), 14, 44, 2, 2, 2, 2, -7631989);
        final int gap = 5;
        final int contentWidth = abilityBounds.width + gap + Math.max(fontRenderer.width(abilityName), fontRenderer.width(subText));
        matrixStack.pushPose();
        matrixStack.translate(10.0, 0.0, 0.0);
        FontHelper.drawStringWithBorder(matrixStack, abilityName, (float)(abilityBounds.width + gap), 13.0f, learned ? -1849 : -1, learned ? -12897536 : -16777216);
        FontHelper.drawStringWithBorder(matrixStack, subText, (float)(abilityBounds.width + gap), 23.0f, learned ? -1849 : -1, learned ? -12897536 : -16777216);
        matrixStack.translate((double)(-abilityStyle.x), (double)(-abilityStyle.y), 0.0);
        matrixStack.translate(abilityBounds.getWidth() / 2.0, 0.0, 0.0);
        matrixStack.translate(0.0, 23.0, 0.0);
        this.selectedAbilityWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.popPose();
    }
    
    private void renderDescriptions(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final AbilityNode<?, ?> targetAbilityNode = this.selectedAbilityWidget.makeAbilityNode();
        final Rectangle renderableBounds = this.descriptionComponent.getRenderableBounds();
        final StringTextComponent text = new StringTextComponent("");
        text.append((ITextComponent)ModConfigs.SKILL_DESCRIPTIONS.getDescriptionFor(this.selectedAbilityWidget.getAbilityName()));
        if (targetAbilityNode.getSpecialization() == null) {
            text.append("\n\n").append(this.getAdditionalDescription(targetAbilityNode));
        }
        final int renderedLineCount = UIHelper.renderWrappedText(matrixStack, (ITextComponent)text, renderableBounds.width, 10);
        this.descriptionComponent.setInnerHeight(renderedLineCount * 10 + 20);
        RenderSystem.enableDepthTest();
    }
    
    private ITextComponent getAdditionalDescription(final AbilityNode<?, ?> abilityNode) {
        final String arrow = String.valueOf('\u25b6');
        final ITextComponent arrowTxt = (ITextComponent)new StringTextComponent(" " + arrow + " ").withStyle(Style.EMPTY.withColor(Color.fromRgb(4737095)));
        final IFormattableTextComponent txt = new StringTextComponent("Cost: ").withStyle(Style.EMPTY.withColor(Color.fromRgb(4737095)));
        for (int lvl = 1; lvl <= abilityNode.getGroup().getMaxLevel(); ++lvl) {
            if (lvl > 1) {
                txt.append(arrowTxt);
            }
            final int cost = abilityNode.getGroup().levelUpCost(null, lvl);
            txt.append((ITextComponent)new StringTextComponent(String.valueOf(cost)).withStyle(TextFormatting.WHITE));
        }
        boolean displayRequirements = false;
        final IFormattableTextComponent lvlReq = new StringTextComponent("\n\nLevel requirement: ").withStyle(Style.EMPTY.withColor(Color.fromRgb(4737095)));
        for (int lvl2 = 0; lvl2 < abilityNode.getGroup().getMaxLevel(); ++lvl2) {
            if (lvl2 > 0) {
                lvlReq.append(arrowTxt);
            }
            final int levelRequirement = ((AbilityConfig)abilityNode.getGroup().getAbilityConfig(null, lvl2)).getLevelRequirement();
            final StringTextComponent lvlReqPart = new StringTextComponent(String.valueOf(levelRequirement));
            if (VaultBarOverlay.vaultLevel < levelRequirement) {
                lvlReqPart.withStyle(Style.EMPTY.withColor(Color.fromRgb(8257536)));
            }
            else {
                lvlReqPart.withStyle(TextFormatting.WHITE);
            }
            lvlReq.append((ITextComponent)lvlReqPart);
            if (levelRequirement > 0) {
                displayRequirements = true;
            }
        }
        if (displayRequirements) {
            txt.append((ITextComponent)lvlReq);
        }
        else {
            txt.append((ITextComponent)new StringTextComponent("\n\nNo Level requirements"));
        }
        return (ITextComponent)txt;
    }
    
    private void renderFooter(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final int containerX = mouseX - this.bounds.x;
        final int containerY = mouseY - this.bounds.y;
        this.selectButton.render(matrixStack, containerX, containerY, partialTicks);
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);
        final AbilityNode<?, ?> targetAbilityNode = this.selectedAbilityWidget.makeAbilityNode();
        final AbilityNode<?, ?> existingNode = this.abilityTree.getNodeOf(targetAbilityNode.getGroup());
        if (targetAbilityNode.getSpecialization() == null && targetAbilityNode.isLearned() && existingNode.getLevel() < targetAbilityNode.getGroup().getMaxLevel() && targetAbilityNode.getLevel() < targetAbilityNode.getGroup().getMaxLevel() + 1) {
            this.blit(matrixStack, 13, this.bounds.height - 40 - 2, 121, 0, 15, 23);
        }
    }
}
