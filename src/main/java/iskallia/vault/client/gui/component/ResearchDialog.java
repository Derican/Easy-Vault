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

public class ResearchDialog
        extends ComponentDialog {
    private final ResearchTree researchTree;
    private String researchName = null;
    private ResearchWidget researchWidget = null;

    public ResearchDialog(ResearchTree researchTree, SkillTreeScreen skillTreeScreen) {
        super(skillTreeScreen);
        this.researchTree = researchTree;
    }


    public Point getIconUV() {
        return new Point(0, 60);
    }


    public int getHeaderHeight() {
        return (this.researchWidget.getClickableBounds()).height;
    }


    public SkillTab createTab() {
        return (SkillTab) new ResearchesTab(this, getSkillTreeScreen());
    }

    public void refreshWidgets() {
        if (this.researchName != null) {

            SkillStyle researchStyle = (SkillStyle) ModConfigs.RESEARCHES_GUI.getStyles().get(this.researchName);
            this.researchWidget = new ResearchWidget(this.researchName, this.researchTree, researchStyle);
            this.researchWidget.setHoverable(false);

            Research research = ModConfigs.RESEARCHES.getByName(this.researchName);
            int researchCost = this.researchTree.getResearchCost(research);

            String buttonText = this.researchTree.isResearched(this.researchName) ? "Researched" : ("Research (" + researchCost + ")");


            this.selectButton = new Button(10, this.bounds.height - 40, this.bounds.width - 30, 20, (ITextComponent) new StringTextComponent(buttonText), button -> research(), (button, matrixStack, x, y) -> {

            });


            this.descriptionComponent = new ScrollableContainer(this::renderDescriptions);

            boolean isLocked = ModConfigs.SKILL_GATES.getGates().isLocked(this.researchName, this.researchTree);
            this.selectButton

                    .active = (!this.researchTree.isResearched(this.researchName) && !isLocked && (research.usesKnowledge() ? (VaultBarOverlay.unspentKnowledgePoints >= researchCost) : (VaultBarOverlay.unspentSkillPoints >= researchCost)));
        }
    }


    public void setResearchName(String researchName) {
        this.researchName = researchName;
        refreshWidgets();
    }

    public void research() {
        Research research = ModConfigs.RESEARCHES.getByName(this.researchName);

        int cost = this.researchTree.getResearchCost(research);
        int unspentPoints = research.usesKnowledge() ? VaultBarOverlay.unspentKnowledgePoints : VaultBarOverlay.unspentSkillPoints;


        if (cost > unspentPoints) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player != null) {
            minecraft.player.playSound(ModSounds.SKILL_TREE_LEARN_SFX, 1.0F, 1.0F);
        }


        this.researchTree.research(this.researchName);
        refreshWidgets();

        ModNetwork.CHANNEL.sendToServer(new ResearchMessage(this.researchName));
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        matrixStack.pushPose();

        renderBackground(matrixStack, mouseX, mouseY, partialTicks);

        if (this.researchName == null)
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

        SkillStyle abilityStyle = (SkillStyle) ModConfigs.RESEARCHES_GUI.getStyles().get(this.researchName);

        Rectangle abilityBounds = this.researchWidget.getClickableBounds();

        UIHelper.renderContainerBorder(this, matrixStack,
                getHeadingBounds(), 14, 44, 2, 2, 2, 2, -7631989);


        boolean researched = this.researchTree.getResearchesDone().contains(this.researchName);

        String subText = !researched ? "Not Researched" : "Researched";

        int gap = 5;

        int contentWidth = abilityBounds.width + gap + Math.max(fontRenderer.width(this.researchName), fontRenderer.width(subText));

        matrixStack.pushPose();
        matrixStack.translate(10.0D, 0.0D, 0.0D);
        FontHelper.drawStringWithBorder(matrixStack, this.researchName, (abilityBounds.width + gap), 13.0F, !researched ? -1 : -1849, !researched ? -16777216 : -12897536);


        FontHelper.drawStringWithBorder(matrixStack, subText, (abilityBounds.width + gap), 23.0F, !researched ? -1 : -1849, !researched ? -16777216 : -12897536);


        matrixStack.translate(-abilityStyle.x, -abilityStyle.y, 0.0D);
        matrixStack.translate(abilityBounds.getWidth() / 2.0D, 0.0D, 0.0D);
        matrixStack.translate(-this.researchWidget.getRenderWidth() / 2.0D, -this.researchWidget.getRenderHeight() / 2.0D, 0.0D);
        matrixStack.translate(-3.0D, 20.0D, 0.0D);
        this.researchWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.popPose();
    }

    private void renderDescriptions(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Rectangle renderableBounds = this.descriptionComponent.getRenderableBounds();

        IFormattableTextComponent description = ModConfigs.SKILL_DESCRIPTIONS.getDescriptionFor(this.researchName);

        int renderedLineCount = UIHelper.renderWrappedText(matrixStack, (ITextComponent) description, renderableBounds.width, 10);


        this.descriptionComponent.setInnerHeight(renderedLineCount * 10 + 20);

        RenderSystem.enableDepthTest();
    }

    private void renderFooter(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int containerX = mouseX - this.bounds.x;
        int containerY = mouseY - this.bounds.y;

        this.selectButton.render(matrixStack, containerX, containerY, partialTicks);

        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);

        Research research = ModConfigs.RESEARCHES.getByName(this.researchName);
        boolean researched = this.researchTree.getResearchesDone().contains(this.researchName);

        if (!researched)
            blit(matrixStack, 13, this.bounds.height - 40 - 2, 121 + (

                    research.usesKnowledge() ? 15 : 30), 0, 15, 23);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\component\ResearchDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */