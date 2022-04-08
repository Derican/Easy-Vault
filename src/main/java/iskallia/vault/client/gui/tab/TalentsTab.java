package iskallia.vault.client.gui.tab;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.component.TalentDialog;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.client.gui.widget.TalentWidget;
import iskallia.vault.client.gui.widget.connect.ConnectorWidget;
import iskallia.vault.container.SkillTreeContainer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.MiscUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TalentsTab extends SkillTab {
    private final Map<String, TalentWidget> talentWidgets = new HashMap<>();
    private final List<ConnectorWidget> talentConnectors = new LinkedList<>();
    private final TalentDialog talentDialog;
    private TalentWidget selectedWidget;

    public TalentsTab(TalentDialog talentDialog, SkillTreeScreen parentScreen) {
        super(parentScreen, (ITextComponent) new StringTextComponent("Talents Tab"));
        this.talentDialog = talentDialog;
    }

    public void refresh() {
        this.talentWidgets.clear();

        TalentTree talentTree = ((SkillTreeContainer) this.parentScreen.getMenu()).getTalentTree();
        ModConfigs.TALENTS_GUI.getStyles().forEach((talentName, style) -> this.talentWidgets.put(talentName, new TalentWidget(ModConfigs.TALENTS.getByName(talentName), talentTree, style)));


        ModConfigs.TALENTS_GUI.getStyles().forEach((researchName, style) -> {
            TalentWidget target = this.talentWidgets.get(researchName);
            if (target == null) {
                return;
            }
            ModConfigs.SKILL_GATES.getGates().getDependencyTalents(researchName).forEach((talentGroup -> {
            }));
            ModConfigs.SKILL_GATES.getGates().getLockedByTalents(researchName).forEach((talentGroup -> {
            }));
        });
    }


    public String getTabName() {
        return "Talents";
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean mouseClicked = super.mouseClicked(mouseX, mouseY, button);

        Point2D.Float midpoint = MiscUtils.getMidpoint(this.parentScreen.getContainerBounds());
        int containerMouseX = (int) ((mouseX - midpoint.x) / this.viewportScale - this.viewportTranslation.x);
        int containerMouseY = (int) ((mouseY - midpoint.y) / this.viewportScale - this.viewportTranslation.y);
        for (TalentWidget abilityWidget : this.talentWidgets.values()) {
            if (abilityWidget.isMouseOver(containerMouseX, containerMouseY) && abilityWidget
                    .mouseClicked(containerMouseX, containerMouseY, button)) {
                if (this.selectedWidget != null) this.selectedWidget.deselect();
                this.selectedWidget = abilityWidget;
                this.selectedWidget.select();
                this.talentDialog.setTalentGroup(this.selectedWidget.getTalentGroup());

                break;
            }
        }
        return mouseClicked;
    }


    public void renderTabForeground(MatrixStack renderStack, int mouseX, int mouseY, float pTicks, List<Runnable> postContainerRender) {
        RenderSystem.enableBlend();

        Point2D.Float midpoint = MiscUtils.getMidpoint(this.parentScreen.getContainerBounds());

        renderStack.pushPose();
        renderStack.translate(midpoint.x, midpoint.y, 0.0D);
        renderStack.scale(this.viewportScale, this.viewportScale, 1.0F);
        renderStack.translate(this.viewportTranslation.x, this.viewportTranslation.y, 0.0D);

        int containerMouseX = (int) ((mouseX - midpoint.x) / this.viewportScale - this.viewportTranslation.x);
        int containerMouseY = (int) ((mouseY - midpoint.y) / this.viewportScale - this.viewportTranslation.y);

        for (ConnectorWidget talentConnector : this.talentConnectors) {
            talentConnector.renderConnection(renderStack, containerMouseX, containerMouseY, pTicks, this.viewportScale);
        }

        for (TalentWidget abilityWidget : this.talentWidgets.values()) {
            abilityWidget.renderWidget(renderStack, containerMouseX, containerMouseY, pTicks, postContainerRender);
        }

        renderStack.popPose();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\tab\TalentsTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */