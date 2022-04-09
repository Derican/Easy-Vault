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

public class TalentDialog extends ComponentDialog
{
    private final TalentTree talentTree;
    private TalentGroup<?> talentGroup;
    private TalentWidget talentWidget;
    
    public TalentDialog(final TalentTree talentTree, final SkillTreeScreen skillTreeScreen) {
        super(skillTreeScreen);
        this.talentGroup = null;
        this.talentWidget = null;
        this.talentTree = talentTree;
    }
    
    @Override
    public Point getIconUV() {
        return new Point(16, 60);
    }
    
    @Override
    public int getHeaderHeight() {
        return this.talentWidget.getClickableBounds().height;
    }
    
    @Override
    public SkillTab createTab() {
        return new TalentsTab(this, this.getSkillTreeScreen());
    }
    
    @Override
    public void refreshWidgets() {
        if (this.talentGroup != null) {
            final SkillStyle abilityStyle = ModConfigs.TALENTS_GUI.getStyles().get(this.talentGroup.getParentName());
            (this.talentWidget = new TalentWidget(this.talentGroup, this.talentTree, abilityStyle)).setRenderPips(false);
            final TalentNode<?> talentNode = this.talentTree.getNodeOf(this.talentGroup);
            final String buttonText = talentNode.isLearned() ? ((talentNode.getLevel() >= this.talentGroup.getMaxLevel()) ? "Fully Learned" : ("Upgrade (" + this.talentGroup.cost(talentNode.getLevel() + 1) + ")")) : ("Learn (" + this.talentGroup.learningCost() + ")");
            this.selectButton = new Button(10, this.bounds.height - 40, this.bounds.width - 30, 20, (ITextComponent)new StringTextComponent(buttonText), button -> this.upgradeAbility(), (button, matrixStack, x, y) -> {});
            this.descriptionComponent = new ScrollableContainer(this::renderDescriptions);
            final boolean isLocked = ModConfigs.SKILL_GATES.getGates().isLocked(this.talentGroup, this.talentTree);
            final boolean fulfillsLevelRequirement = talentNode.getLevel() >= this.talentGroup.getMaxLevel() || VaultBarOverlay.vaultLevel >= ((PlayerTalent)talentNode.getGroup().getTalent(talentNode.getLevel() + 1)).getLevelRequirement();
            final PlayerTalent ability = (PlayerTalent)talentNode.getTalent();
            final int cost = (ability == null) ? this.talentGroup.learningCost() : this.talentGroup.cost(talentNode.getLevel() + 1);
            this.selectButton.active = (cost <= VaultBarOverlay.unspentSkillPoints && fulfillsLevelRequirement && !isLocked && talentNode.getLevel() < this.talentGroup.getMaxLevel());
        }
    }
    
    public void setTalentGroup(final TalentGroup<?> talentGroup) {
        this.talentGroup = talentGroup;
        this.refreshWidgets();
    }
    
    public void upgradeAbility() {
        final TalentNode<?> talentNode = this.talentTree.getNodeOf(this.talentGroup);
        if (talentNode.getLevel() >= this.talentGroup.getMaxLevel()) {
            return;
        }
        if (VaultBarOverlay.vaultLevel < ((PlayerTalent)talentNode.getGroup().getTalent(talentNode.getLevel() + 1)).getLevelRequirement()) {
            return;
        }
        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            minecraft.player.playSound(talentNode.isLearned() ? ModSounds.SKILL_TREE_UPGRADE_SFX : ModSounds.SKILL_TREE_LEARN_SFX, 1.0f, 1.0f);
        }
        this.talentTree.upgradeTalent(null, talentNode);
        this.refreshWidgets();
        ModNetwork.CHANNEL.sendToServer(new TalentUpgradeMessage(this.talentGroup.getParentName()));
    }
    
    @Override
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        matrixStack.pushPose();
        this.renderBackground(matrixStack, mouseX, mouseY, partialTicks);
        if (this.talentGroup == null) {
            return;
        }
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
        final SkillStyle abilityStyle = ModConfigs.TALENTS_GUI.getStyles().get(this.talentGroup.getParentName());
        final TalentNode<?> talentNode = this.talentTree.getNodeByName(this.talentGroup.getParentName());
        final Rectangle abilityBounds = this.talentWidget.getClickableBounds();
        UIHelper.renderContainerBorder(this, matrixStack, this.getHeadingBounds(), 14, 44, 2, 2, 2, 2, -7631989);
        final String abilityName = talentNode.getGroup().getParentName();
        final String subText = (talentNode.getLevel() == 0) ? "Not Learned Yet" : ("Level: " + talentNode.getLevel() + "/" + talentNode.getGroup().getMaxLevel());
        final int gap = 5;
        final int contentWidth = abilityBounds.width + gap + Math.max(fontRenderer.width(abilityName), fontRenderer.width(subText));
        matrixStack.pushPose();
        matrixStack.translate(10.0, 0.0, 0.0);
        FontHelper.drawStringWithBorder(matrixStack, abilityName, (float)(abilityBounds.width + gap), 13.0f, (talentNode.getLevel() == 0) ? -1 : -1849, (talentNode.getLevel() == 0) ? -16777216 : -12897536);
        FontHelper.drawStringWithBorder(matrixStack, subText, (float)(abilityBounds.width + gap), 23.0f, (talentNode.getLevel() == 0) ? -1 : -1849, (talentNode.getLevel() == 0) ? -16777216 : -12897536);
        matrixStack.translate((double)(-abilityStyle.x), (double)(-abilityStyle.y), 0.0);
        matrixStack.translate(abilityBounds.getWidth() / 2.0, 0.0, 0.0);
        matrixStack.translate(0.0, 23.0, 0.0);
        this.talentWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.popPose();
    }
    
    private void renderDescriptions(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final Rectangle renderableBounds = this.descriptionComponent.getRenderableBounds();
        final StringTextComponent text = new StringTextComponent("");
        text.append((ITextComponent)ModConfigs.SKILL_DESCRIPTIONS.getDescriptionFor(this.talentGroup.getParentName()));
        text.append("\n\n").append(this.getAdditionalDescription(this.talentGroup));
        final int renderedLineCount = UIHelper.renderWrappedText(matrixStack, (ITextComponent)text, renderableBounds.width, 10);
        this.descriptionComponent.setInnerHeight(renderedLineCount * 10 + 20);
        RenderSystem.enableDepthTest();
    }
    
    private ITextComponent getAdditionalDescription(final TalentGroup<?> talentGroup) {
        final String arrow = String.valueOf('\u25b6');
        final ITextComponent costArrowTxt = (ITextComponent)new StringTextComponent(" " + arrow + " ").withStyle(Style.EMPTY.withColor(Color.fromRgb(4737095)));
        final ITextComponent lvlReqArrowTxt = (ITextComponent)new StringTextComponent(" " + arrow + " ").withStyle(Style.EMPTY.withColor(Color.fromRgb(4737095)));
        final IFormattableTextComponent txt = new StringTextComponent("Cost: ").withStyle(Style.EMPTY.withColor(Color.fromRgb(4737095)));
        for (int lvl = 1; lvl <= talentGroup.getMaxLevel(); ++lvl) {
            if (lvl > 1) {
                txt.append(costArrowTxt);
            }
            final int cost = ((PlayerTalent)talentGroup.getTalent(lvl)).getCost();
            txt.append((ITextComponent)new StringTextComponent(String.valueOf(cost)).withStyle(TextFormatting.WHITE));
        }
        boolean displayRequirements = false;
        final StringTextComponent lvlReq = new StringTextComponent("\n\nLevel requirement: ");
        for (int lvl2 = 1; lvl2 <= talentGroup.getMaxLevel(); ++lvl2) {
            if (lvl2 > 1) {
                lvlReq.append(lvlReqArrowTxt);
            }
            final int levelRequirement = ((PlayerTalent)talentGroup.getTalent(lvl2)).getLevelRequirement();
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
        final TalentNode<?> talentNode = this.talentTree.getNodeOf(this.talentGroup);
        if (talentNode.isLearned() && talentNode.getLevel() < this.talentGroup.getMaxLevel()) {
            this.blit(matrixStack, 13, this.bounds.height - 40 - 2, 121, 0, 15, 23);
        }
    }
}
