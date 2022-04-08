package iskallia.vault.client.gui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.client.gui.tab.SkillTab;
import iskallia.vault.client.gui.tab.TalentsTab;
import iskallia.vault.client.gui.widget.TalentWidget;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.init.ModSounds;
import iskallia.vault.network.message.TalentUpgradeMessage;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.PlayerTalent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.*;

import java.awt.*;

public class TalentDialog extends ComponentDialog {
    private TalentGroup<?> talentGroup = null;
    private final TalentTree talentTree;
    private TalentWidget talentWidget = null;

    public TalentDialog(TalentTree talentTree, SkillTreeScreen skillTreeScreen) {
        super(skillTreeScreen);
        this.talentTree = talentTree;
    }


    public Point getIconUV() {
        return new Point(16, 60);
    }


    public int getHeaderHeight() {
        return (this.talentWidget.getClickableBounds()).height;
    }


    public SkillTab createTab() {
        return (SkillTab) new TalentsTab(this, getSkillTreeScreen());
    }

    public void refreshWidgets() {
        if (this.talentGroup != null) {

            SkillStyle abilityStyle = (SkillStyle) ModConfigs.TALENTS_GUI.getStyles().get(this.talentGroup.getParentName());
            this.talentWidget = new TalentWidget(this.talentGroup, this.talentTree, abilityStyle);
            this.talentWidget.setRenderPips(false);

            TalentNode<?> talentNode = this.talentTree.getNodeOf(this.talentGroup);


            String buttonText = !talentNode.isLearned() ? ("Learn (" + this.talentGroup.learningCost() + ")") : ((talentNode.getLevel() >= this.talentGroup.getMaxLevel()) ? "Fully Learned" : ("Upgrade (" + this.talentGroup.cost(talentNode.getLevel() + 1) + ")"));

            this.selectButton = new Button(10, this.bounds.height - 40, this.bounds.width - 30, 20, (ITextComponent) new StringTextComponent(buttonText), button -> upgradeAbility(), (button, matrixStack, x, y) -> {

            });


            this.descriptionComponent = new ScrollableContainer(this::renderDescriptions);

            boolean isLocked = ModConfigs.SKILL_GATES.getGates().isLocked(this.talentGroup, this.talentTree);

            boolean fulfillsLevelRequirement = (talentNode.getLevel() >= this.talentGroup.getMaxLevel() || VaultBarOverlay.vaultLevel >= talentNode.getGroup().getTalent(talentNode.getLevel() + 1).getLevelRequirement());
            PlayerTalent ability = talentNode.getTalent();
            int cost = (ability == null) ? this.talentGroup.learningCost() : this.talentGroup.cost(talentNode.getLevel() + 1);
            this.selectButton


                    .active = (cost <= VaultBarOverlay.unspentSkillPoints && fulfillsLevelRequirement && !isLocked && talentNode.getLevel() < this.talentGroup.getMaxLevel());
        }
    }

    public void setTalentGroup(TalentGroup<?> talentGroup) {
        this.talentGroup = talentGroup;
        refreshWidgets();
    }

    public void upgradeAbility() {
        TalentNode<?> talentNode = this.talentTree.getNodeOf(this.talentGroup);

        if (talentNode.getLevel() >= this.talentGroup.getMaxLevel()) {
            return;
        }
        if (VaultBarOverlay.vaultLevel < talentNode.getGroup().getTalent(talentNode.getLevel() + 1).getLevelRequirement()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player != null) {
            minecraft.player.playSound(
                    talentNode.isLearned() ? ModSounds.SKILL_TREE_UPGRADE_SFX : ModSounds.SKILL_TREE_LEARN_SFX, 1.0F, 1.0F);
        }


        this.talentTree.upgradeTalent(null, talentNode);
        refreshWidgets();

        ModNetwork.CHANNEL.sendToServer(new TalentUpgradeMessage(this.talentGroup.getParentName()));
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        matrixStack.pushPose();

        renderBackground(matrixStack, mouseX, mouseY, partialTicks);

        if (this.talentGroup == null)
            return;
        matrixStack.translate((this.bounds.x + 5), (this.bounds.y + 5), 0.0D);

        renderHeading(matrixStack, mouseX, mouseY, partialTicks);

        this.descriptionComponent.setBounds(getDescriptionsBounds());
        this.descriptionComponent.render(matrixStack, mouseX, mouseY, partialTicks);

        renderFooter(matrixStack, mouseX, mouseY, partialTicks);

        matrixStack.popPose();
    }

    private void renderHeading(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);

        FontRenderer fontRenderer = (Minecraft.getInstance()).font;

        SkillStyle abilityStyle = (SkillStyle) ModConfigs.TALENTS_GUI.getStyles().get(this.talentGroup.getParentName());

        TalentNode<?> talentNode = this.talentTree.getNodeByName(this.talentGroup.getParentName());

        Rectangle abilityBounds = this.talentWidget.getClickableBounds();

        UIHelper.renderContainerBorder(this, matrixStack,
                getHeadingBounds(), 14, 44, 2, 2, 2, 2, -7631989);


        String abilityName = talentNode.getGroup().getParentName();


        String subText = (talentNode.getLevel() == 0) ? "Not Learned Yet" : ("Level: " + talentNode.getLevel() + "/" + talentNode.getGroup().getMaxLevel());

        int gap = 5;

        int contentWidth = abilityBounds.width + gap + Math.max(fontRenderer.width(abilityName), fontRenderer.width(subText));

        matrixStack.pushPose();
        matrixStack.translate(10.0D, 0.0D, 0.0D);
        FontHelper.drawStringWithBorder(matrixStack, abilityName, (abilityBounds.width + gap), 13.0F,


                (talentNode.getLevel() == 0) ? -1 : -1849,
                (talentNode.getLevel() == 0) ? -16777216 : -12897536);

        FontHelper.drawStringWithBorder(matrixStack, subText, (abilityBounds.width + gap), 23.0F,


                (talentNode.getLevel() == 0) ? -1 : -1849,
                (talentNode.getLevel() == 0) ? -16777216 : -12897536);


        matrixStack.translate(-abilityStyle.x, -abilityStyle.y, 0.0D);
        matrixStack.translate(abilityBounds.getWidth() / 2.0D, 0.0D, 0.0D);
        matrixStack.translate(0.0D, 23.0D, 0.0D);
        this.talentWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.popPose();
    }

    private void renderDescriptions(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Rectangle renderableBounds = this.descriptionComponent.getRenderableBounds();

        StringTextComponent text = new StringTextComponent("");
        text.append((ITextComponent) ModConfigs.SKILL_DESCRIPTIONS.getDescriptionFor(this.talentGroup.getParentName()));
        text.append("\n\n").append(getAdditionalDescription(this.talentGroup));

        int renderedLineCount = UIHelper.renderWrappedText(matrixStack, (ITextComponent) text, renderableBounds.width, 10);
        this.descriptionComponent.setInnerHeight(renderedLineCount * 10 + 20);

        RenderSystem.enableDepthTest();
    }

    private ITextComponent getAdditionalDescription(TalentGroup<?> talentGroup) {
        String arrow = String.valueOf('▶');
        IFormattableTextComponent iFormattableTextComponent1 = (new StringTextComponent(" " + arrow + " ")).withStyle(Style.EMPTY.withColor(Color.fromRgb(4737095)));
        IFormattableTextComponent iFormattableTextComponent2 = (new StringTextComponent(" " + arrow + " ")).withStyle(Style.EMPTY.withColor(Color.fromRgb(4737095)));

        IFormattableTextComponent txt = (new StringTextComponent("Cost: ")).withStyle(Style.EMPTY.withColor(Color.fromRgb(4737095)));
        for (int lvl = 1; lvl <= talentGroup.getMaxLevel(); lvl++) {
            if (lvl > 1) {
                txt.append((ITextComponent) iFormattableTextComponent1);
            }
            int cost = talentGroup.getTalent(lvl).getCost();
            txt.append((ITextComponent) (new StringTextComponent(String.valueOf(cost))).withStyle(TextFormatting.WHITE));
        }

        boolean displayRequirements = false;
        StringTextComponent lvlReq = new StringTextComponent("\n\nLevel requirement: ");
        for (int i = 1; i <= talentGroup.getMaxLevel(); i++) {
            if (i > 1) {
                lvlReq.append((ITextComponent) iFormattableTextComponent2);
            }
            int levelRequirement = talentGroup.getTalent(i).getLevelRequirement();
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

        TalentNode<?> talentNode = this.talentTree.getNodeOf(this.talentGroup);

        if (talentNode.isLearned() && talentNode.getLevel() < this.talentGroup.getMaxLevel())
            blit(matrixStack, 13, this.bounds.height - 40 - 2, 121, 0, 15, 23);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\component\TalentDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */