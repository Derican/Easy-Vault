package iskallia.vault.client.gui.tab;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.component.ResearchDialog;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.client.gui.widget.ResearchGroupWidget;
import iskallia.vault.client.gui.widget.ResearchWidget;
import iskallia.vault.client.gui.widget.connect.ConnectorWidget;
import iskallia.vault.config.entry.ResearchGroupStyle;
import iskallia.vault.container.SkillTreeContainer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.type.Research;
import iskallia.vault.util.MiscUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResearchesTab extends SkillTab {
    private final List<ResearchGroupWidget> researchGroups = new LinkedList<>();
    private final Map<String, ResearchWidget> researchWidgets = new HashMap<>();
    private final List<ConnectorWidget> researchConnectors = new LinkedList<>();
    private final ResearchDialog researchDialog;
    private ResearchWidget selectedWidget;

    public ResearchesTab(ResearchDialog researchDialog, SkillTreeScreen parentScreen) {
        super(parentScreen, (ITextComponent) new StringTextComponent("Researches Tab"));
        this.researchDialog = researchDialog;
    }


    public void refresh() {
        this.researchGroups.clear();
        this.researchWidgets.clear();
        this.researchConnectors.clear();

        ResearchTree researchTree = ((SkillTreeContainer) this.parentScreen.getMenu()).getResearchTree();

        ModConfigs.RESEARCH_GROUPS.getGroups().forEach((groupId, group) -> {
            ResearchGroupStyle style = ModConfigs.RESEARCH_GROUP_STYLES.getStyle(groupId);
            if (style != null) {
                this.researchGroups.add(new ResearchGroupWidget(style, researchTree, () -> {
                    return null;
                }));
            }
        });
        ModConfigs.RESEARCHES_GUI.getStyles().forEach((researchName, style) -> this.researchWidgets.put(researchName, new ResearchWidget(researchName, researchTree, style)));


        ModConfigs.RESEARCHES_GUI.getStyles().forEach((researchName, style) -> {
            ResearchWidget target = this.researchWidgets.get(researchName);
            if (target == null) {
                return;
            }
            ModConfigs.SKILL_GATES.getGates().getDependencyResearches(researchName).forEach((Research research) -> {
            });
            ModConfigs.SKILL_GATES.getGates().getLockedByResearches(researchName).forEach((Research research) -> {
            });
        });
    }


    public String getTabName() {
        return "Researches";
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean mouseClicked = super.mouseClicked(mouseX, mouseY, button);

        Point2D.Float midpoint = MiscUtils.getMidpoint(this.parentScreen.getContainerBounds());
        int containerMouseX = (int) ((mouseX - midpoint.x) / this.viewportScale - this.viewportTranslation.x);
        int containerMouseY = (int) ((mouseY - midpoint.y) / this.viewportScale - this.viewportTranslation.y);

        for (ResearchWidget researchWidget : this.researchWidgets.values()) {
            if (researchWidget.isMouseOver(containerMouseX, containerMouseY) && researchWidget
                    .mouseClicked(containerMouseX, containerMouseY, button)) {
                if (this.selectedWidget != null) this.selectedWidget.deselect();
                this.selectedWidget = researchWidget;
                this.selectedWidget.select();
                this.researchDialog.setResearchName(this.selectedWidget.getResearchName());

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

        for (ResearchGroupWidget researchGroupWidget : this.researchGroups) {
            researchGroupWidget.render(renderStack, containerMouseX, containerMouseY, pTicks);
        }

        for (ConnectorWidget researchConnector : this.researchConnectors) {
            researchConnector.renderConnection(renderStack, containerMouseX, containerMouseY, pTicks, this.viewportScale);
        }

        for (ResearchWidget researchWidget : this.researchWidgets.values()) {
            researchWidget.renderWidget(renderStack, containerMouseX, containerMouseY, pTicks, postContainerRender);
        }

        renderStack.popPose();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\tab\ResearchesTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */