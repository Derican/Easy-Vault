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

public class AbilityDialog extends ComponentDialog {
    private final AbilityTree abilityTree;
    private String selectedAbility = null;
    private AbilityWidget selectedAbilityWidget = null;

    public AbilityDialog(AbilityTree abilityTree, SkillTreeScreen skillTreeScreen) {
        super(skillTreeScreen);
        this.abilityTree = abilityTree;
    }


    public Point getIconUV() {
        return new Point(32, 60);
    }


    public int getHeaderHeight() {
        return (this.selectedAbilityWidget.getClickableBounds()).height;
    }


    public SkillTab createTab() {
        return (SkillTab) new AbilitiesTab(this, getSkillTreeScreen());
    }

    public void refreshWidgets() {
        if (this.selectedAbility != null) {
            Button.IPressable pressAction;
            String buttonText;
            boolean activeState;
            SkillStyle abilityStyle = (SkillStyle) ModConfigs.ABILITIES_GUI.getStyles().get(this.selectedAbility);
            this.selectedAbilityWidget = new AbilityWidget(this.selectedAbility, this.abilityTree, abilityStyle);
            this.selectedAbilityWidget.setHoverable(false);
            this.selectedAbilityWidget.setRenderPips(false);

            AbilityNode<?, ?> existingNode = this.abilityTree.getNodeOf(AbilityRegistry.getAbility(this.selectedAbility));

            AbilityNode<?, ?> targetAbilityNode = this.selectedAbilityWidget.makeAbilityNode();
            AbilityGroup<?, ?> targetAbilityGroup = targetAbilityNode.getGroup();


            if (targetAbilityNode.getSpecialization() != null) {
                buttonText = "Select Specialization";
                pressAction = (button -> selectSpecialization());


                activeState = (existingNode.getSpecialization() == null && existingNode.isLearned() && VaultBarOverlay.vaultLevel >= targetAbilityNode.getAbilityConfig().getLevelRequirement());
            } else {
                if (!targetAbilityNode.isLearned()) {
                    buttonText = "Learn (" + targetAbilityGroup.learningCost() + ")";
                } else {

                    buttonText = (existingNode.getLevel() >= targetAbilityGroup.getMaxLevel()) ? "Fully Learned" : ("Upgrade (" + targetAbilityGroup.levelUpCost(targetAbilityNode.getSpecialization(), targetAbilityNode.getLevel()) + ")");
                }
                pressAction = (button -> upgradeAbility());
                AbilityConfig ability = targetAbilityNode.getAbilityConfig();
                int cost = (ability == null) ? targetAbilityGroup.learningCost() : targetAbilityGroup.levelUpCost(targetAbilityNode.getSpecialization(), targetAbilityNode.getLevel());


                activeState = (cost <= VaultBarOverlay.unspentSkillPoints && existingNode.getLevel() < targetAbilityGroup.getMaxLevel() && targetAbilityNode.getLevel() < targetAbilityGroup.getMaxLevel() + 1 && VaultBarOverlay.vaultLevel >= targetAbilityNode.getAbilityConfig().getLevelRequirement());
            }

            this.descriptionComponent = new ScrollableContainer(this::renderDescriptions);
            this.selectButton = new Button(10, this.bounds.height - 40, this.bounds.width - 30, 20, (ITextComponent) new StringTextComponent(buttonText), pressAction, (button, matrixStack, x, y) -> {

            });
            this.selectButton.active = activeState;
        }
    }

    public void setAbilityWidget(String abilityName) {
        this.selectedAbility = abilityName;
        refreshWidgets();
    }

    private void upgradeAbility() {
        AbilityNode<?, ?> abilityNode = this.abilityTree.getNodeByName(AbilityRegistry.getAbility(this.selectedAbility).getAbilityGroupName());

        if (abilityNode.getLevel() >= abilityNode.getGroup().getMaxLevel()) {
            return;
        }
        (Minecraft.getInstance()).player.playSound(
                abilityNode.isLearned() ? ModSounds.SKILL_TREE_UPGRADE_SFX : ModSounds.SKILL_TREE_LEARN_SFX, 1.0F, 1.0F);


        this.abilityTree.upgradeAbility(null, abilityNode);
        refreshWidgets();

        ModNetwork.CHANNEL.sendToServer(new AbilityUpgradeMessage(abilityNode.getGroup().getParentName()));
    }

    private void selectSpecialization() {
        AbilityNode<?, ?> targetNode = this.selectedAbilityWidget.makeAbilityNode();
        AbilityNode<?, ?> existingNode = this.abilityTree.getNodeByName(AbilityRegistry.getAbility(this.selectedAbility).getAbilityGroupName());
        String toSelect = targetNode.getSpecialization();
        String abilityName = existingNode.getGroup().getParentName();

        if (existingNode.getSpecialization() != null && targetNode.getGroup().equals(existingNode.getGroup())) {
            return;
        }
        if (VaultBarOverlay.vaultLevel < targetNode.getAbilityConfig().getLevelRequirement()) {
            return;
        }

        (Minecraft.getInstance()).player.playSound(ModSounds.SKILL_TREE_UPGRADE_SFX, 1.0F, 1.0F);

        this.abilityTree.selectSpecialization(abilityName, toSelect);
        getSkillTreeScreen().refreshWidgets();

        ModNetwork.CHANNEL.sendToServer(new AbilitySelectSpecializationMessage(abilityName, toSelect));
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack, mouseX, mouseY, partialTicks);

        if (this.selectedAbility == null || this.selectedAbilityWidget == null) {
            return;
        }

        matrixStack.pushPose();
        matrixStack.translate((this.bounds.x + 5), (this.bounds.y + 5), 0.0D);

        renderHeading(matrixStack, mouseX, mouseY, partialTicks);

        this.descriptionComponent.setBounds(getDescriptionsBounds());
        this.descriptionComponent.render(matrixStack, mouseX, mouseY, partialTicks);

        renderFooter(matrixStack, mouseX, mouseY, partialTicks);

        matrixStack.popPose();
    }

    private void renderHeading(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        boolean learned;
        String abilityName, subText;
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);

        FontRenderer fontRenderer = (Minecraft.getInstance()).font;
        SkillStyle abilityStyle = (SkillStyle) ModConfigs.ABILITIES_GUI.getStyles().get(this.selectedAbility);


        AbilityNode<?, ?> targetAbilityNode = this.selectedAbilityWidget.makeAbilityNode();


        AbilityNode<?, ?> currentAbilityNode = this.abilityTree.getNodeOf(targetAbilityNode.getGroup());
        if (targetAbilityNode.getSpecialization() != null) {
            learned = targetAbilityNode.getSpecialization().equals(currentAbilityNode.getSpecialization());
            abilityName = targetAbilityNode.getGroup().getParentName() + ": " + targetAbilityNode.getSpecializationName();
            subText = learned ? "Selected" : "Not selected";
        } else {
            learned = targetAbilityNode.isLearned();
            abilityName = targetAbilityNode.getGroup().getParentName();
            if (!learned) {
                subText = "Not learned yet";
            } else {
                subText = "Level: " + currentAbilityNode.getLevel() + "/" + targetAbilityNode.getGroup().getMaxLevel();
            }
        }

        Rectangle abilityBounds = this.selectedAbilityWidget.getClickableBounds();
        UIHelper.renderContainerBorder(this, matrixStack,
                getHeadingBounds(), 14, 44, 2, 2, 2, 2, -7631989);


        int gap = 5;

        int contentWidth = abilityBounds.width + gap + Math.max(fontRenderer.width(abilityName), fontRenderer.width(subText));

        matrixStack.pushPose();
        matrixStack.translate(10.0D, 0.0D, 0.0D);
        FontHelper.drawStringWithBorder(matrixStack, abilityName, (abilityBounds.width + gap), 13.0F, !learned ? -1 : -1849, !learned ? -16777216 : -12897536);


        FontHelper.drawStringWithBorder(matrixStack, subText, (abilityBounds.width + gap), 23.0F, !learned ? -1 : -1849, !learned ? -16777216 : -12897536);


        matrixStack.translate(-abilityStyle.x, -abilityStyle.y, 0.0D);
        matrixStack.translate(abilityBounds.getWidth() / 2.0D, 0.0D, 0.0D);
        matrixStack.translate(0.0D, 23.0D, 0.0D);
        this.selectedAbilityWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.popPose();
    }

    private void renderDescriptions(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        AbilityNode<?, ?> targetAbilityNode = this.selectedAbilityWidget.makeAbilityNode();

        Rectangle renderableBounds = this.descriptionComponent.getRenderableBounds();

        StringTextComponent text = new StringTextComponent("");
        text.append((ITextComponent) ModConfigs.SKILL_DESCRIPTIONS.getDescriptionFor(this.selectedAbilityWidget.getAbilityName()));
        if (targetAbilityNode.getSpecialization() == null) {
            text.append("\n\n").append(getAdditionalDescription(targetAbilityNode));
        }

        int renderedLineCount = UIHelper.renderWrappedText(matrixStack, (ITextComponent) text, renderableBounds.width, 10);
        this.descriptionComponent.setInnerHeight(renderedLineCount * 10 + 20);
        RenderSystem.enableDepthTest();
    }

    private ITextComponent getAdditionalDescription(AbilityNode<?, ?> abilityNode) {
        String arrow = String.valueOf('▶');
        IFormattableTextComponent iFormattableTextComponent1 = (new StringTextComponent(" " + arrow + " ")).withStyle(Style.EMPTY.withColor(Color.fromRgb(4737095)));

        IFormattableTextComponent txt = (new StringTextComponent("Cost: ")).withStyle(Style.EMPTY.withColor(Color.fromRgb(4737095)));
        for (int lvl = 1; lvl <= abilityNode.getGroup().getMaxLevel(); lvl++) {
            if (lvl > 1) {
                txt.append((ITextComponent) iFormattableTextComponent1);
            }
            int cost = abilityNode.getGroup().levelUpCost(null, lvl);
            txt.append((ITextComponent) (new StringTextComponent(String.valueOf(cost))).withStyle(TextFormatting.WHITE));
        }

        boolean displayRequirements = false;
        IFormattableTextComponent lvlReq = (new StringTextComponent("\n\nLevel requirement: ")).withStyle(Style.EMPTY.withColor(Color.fromRgb(4737095)));
        for (int i = 0; i < abilityNode.getGroup().getMaxLevel(); i++) {
            if (i > 0) {
                lvlReq.append((ITextComponent) iFormattableTextComponent1);
            }
            int levelRequirement = abilityNode.getGroup().getAbilityConfig(null, i).getLevelRequirement();
            StringTextComponent lvlReqPart = new StringTextComponent(String.valueOf(levelRequirement));
            if (VaultBarOverlay.vaultLevel < levelRequirement) {
                lvlReqPart.withStyle(Style.EMPTY.withColor(Color.fromRgb(8257536)));
            } else {
                lvlReqPart.withStyle(TextFormatting.WHITE);
            }
            lvlReq.append((ITextComponent) lvlReqPart);
            if (levelRequirement > 0) {
                displayRequirements = true;
            }
        }
        if (displayRequirements) {
            txt.append((ITextComponent) lvlReq);
        } else {
            txt.append((ITextComponent) new StringTextComponent("\n\nNo Level requirements"));
        }
        return (ITextComponent) txt;
    }

    private void renderFooter(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int containerX = mouseX - this.bounds.x;
        int containerY = mouseY - this.bounds.y;

        this.selectButton.render(matrixStack, containerX, containerY, partialTicks);

        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);

        AbilityNode<?, ?> targetAbilityNode = this.selectedAbilityWidget.makeAbilityNode();
        AbilityNode<?, ?> existingNode = this.abilityTree.getNodeOf(targetAbilityNode.getGroup());

        if (targetAbilityNode.getSpecialization() == null && targetAbilityNode
                .isLearned() && existingNode
                .getLevel() < targetAbilityNode.getGroup().getMaxLevel() && targetAbilityNode
                .getLevel() < targetAbilityNode.getGroup().getMaxLevel() + 1)
            blit(matrixStack, 13, this.bounds.height - 40 - 2, 121, 0, 15, 23);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\component\AbilityDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */