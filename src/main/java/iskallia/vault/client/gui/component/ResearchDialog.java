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
import iskallia.vault.client.gui.tab.ResearchesTab;
import iskallia.vault.client.gui.tab.SkillTab;
import iskallia.vault.client.gui.widget.ResearchWidget;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.init.ModSounds;
import iskallia.vault.network.message.ResearchMessage;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.type.Research;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;

public class ResearchDialog extends ComponentDialog
{
    private final ResearchTree researchTree;
    private String researchName;
    private ResearchWidget researchWidget;
    
    public ResearchDialog(final ResearchTree researchTree, final SkillTreeScreen skillTreeScreen) {
        super(skillTreeScreen);
        this.researchName = null;
        this.researchWidget = null;
        this.researchTree = researchTree;
    }
    
    @Override
    public Point getIconUV() {
        return new Point(0, 60);
    }
    
    @Override
    public int getHeaderHeight() {
        return this.researchWidget.getClickableBounds().height;
    }
    
    @Override
    public SkillTab createTab() {
        return new ResearchesTab(this, this.getSkillTreeScreen());
    }
    
    @Override
    public void refreshWidgets() {
        if (this.researchName != null) {
            final SkillStyle researchStyle = ModConfigs.RESEARCHES_GUI.getStyles().get(this.researchName);
            (this.researchWidget = new ResearchWidget(this.researchName, this.researchTree, researchStyle)).setHoverable(false);
            final Research research = ModConfigs.RESEARCHES.getByName(this.researchName);
            final int researchCost = this.researchTree.getResearchCost(research);
            final String buttonText = this.researchTree.isResearched(this.researchName) ? "Researched" : ("Research (" + researchCost + ")");
            this.selectButton = new Button(10, this.bounds.height - 40, this.bounds.width - 30, 20, (ITextComponent)new StringTextComponent(buttonText), button -> this.research(), (button, matrixStack, x, y) -> {});
            this.descriptionComponent = new ScrollableContainer(this::renderDescriptions);
            final boolean isLocked = ModConfigs.SKILL_GATES.getGates().isLocked(this.researchName, this.researchTree);
            this.selectButton.active = (!this.researchTree.isResearched(this.researchName) && !isLocked && (research.usesKnowledge() ? (VaultBarOverlay.unspentKnowledgePoints >= researchCost) : (VaultBarOverlay.unspentSkillPoints >= researchCost)));
        }
    }
    
    public void setResearchName(final String researchName) {
        this.researchName = researchName;
        this.refreshWidgets();
    }
    
    public void research() {
        final Research research = ModConfigs.RESEARCHES.getByName(this.researchName);
        final int cost = this.researchTree.getResearchCost(research);
        final int unspentPoints = research.usesKnowledge() ? VaultBarOverlay.unspentKnowledgePoints : VaultBarOverlay.unspentSkillPoints;
        if (cost > unspentPoints) {
            return;
        }
        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            minecraft.player.playSound(ModSounds.SKILL_TREE_LEARN_SFX, 1.0f, 1.0f);
        }
        this.researchTree.research(this.researchName);
        this.refreshWidgets();
        ModNetwork.CHANNEL.sendToServer(new ResearchMessage(this.researchName));
    }
    
    @Override
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        matrixStack.pushPose();
        this.renderBackground(matrixStack, mouseX, mouseY, partialTicks);
        if (this.researchName == null) {
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
        final SkillStyle abilityStyle = ModConfigs.RESEARCHES_GUI.getStyles().get(this.researchName);
        final Rectangle abilityBounds = this.researchWidget.getClickableBounds();
        UIHelper.renderContainerBorder(this, matrixStack, this.getHeadingBounds(), 14, 44, 2, 2, 2, 2, -7631989);
        final boolean researched = this.researchTree.getResearchesDone().contains(this.researchName);
        final String subText = researched ? "Researched" : "Not Researched";
        final int gap = 5;
        final int contentWidth = abilityBounds.width + gap + Math.max(fontRenderer.width(this.researchName), fontRenderer.width(subText));
        matrixStack.pushPose();
        matrixStack.translate(10.0, 0.0, 0.0);
        FontHelper.drawStringWithBorder(matrixStack, this.researchName, (float)(abilityBounds.width + gap), 13.0f, researched ? -1849 : -1, researched ? -12897536 : -16777216);
        FontHelper.drawStringWithBorder(matrixStack, subText, (float)(abilityBounds.width + gap), 23.0f, researched ? -1849 : -1, researched ? -12897536 : -16777216);
        matrixStack.translate((double)(-abilityStyle.x), (double)(-abilityStyle.y), 0.0);
        matrixStack.translate(abilityBounds.getWidth() / 2.0, 0.0, 0.0);
        matrixStack.translate(-this.researchWidget.getRenderWidth() / 2.0, -this.researchWidget.getRenderHeight() / 2.0, 0.0);
        matrixStack.translate(-3.0, 20.0, 0.0);
        this.researchWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.popPose();
    }
    
    private void renderDescriptions(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final Rectangle renderableBounds = this.descriptionComponent.getRenderableBounds();
        final IFormattableTextComponent description = ModConfigs.SKILL_DESCRIPTIONS.getDescriptionFor(this.researchName);
        final int renderedLineCount = UIHelper.renderWrappedText(matrixStack, (ITextComponent)description, renderableBounds.width, 10);
        this.descriptionComponent.setInnerHeight(renderedLineCount * 10 + 20);
        RenderSystem.enableDepthTest();
    }
    
    private void renderFooter(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final int containerX = mouseX - this.bounds.x;
        final int containerY = mouseY - this.bounds.y;
        this.selectButton.render(matrixStack, containerX, containerY, partialTicks);
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);
        final Research research = ModConfigs.RESEARCHES.getByName(this.researchName);
        final boolean researched = this.researchTree.getResearchesDone().contains(this.researchName);
        if (!researched) {
            this.blit(matrixStack, 13, this.bounds.height - 40 - 2, 121 + (research.usesKnowledge() ? 15 : 30), 0, 15, 23);
        }
    }
}
