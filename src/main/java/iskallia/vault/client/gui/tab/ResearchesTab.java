// 
// Decompiled by Procyon v0.6.0
// 

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
import iskallia.vault.util.MiscUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResearchesTab extends SkillTab
{
    private final List<ResearchGroupWidget> researchGroups;
    private final Map<String, ResearchWidget> researchWidgets;
    private final List<ConnectorWidget> researchConnectors;
    private final ResearchDialog researchDialog;
    private ResearchWidget selectedWidget;
    
    public ResearchesTab(final ResearchDialog researchDialog, final SkillTreeScreen parentScreen) {
        super(parentScreen, (ITextComponent)new StringTextComponent("Researches Tab"));
        this.researchGroups = new LinkedList<ResearchGroupWidget>();
        this.researchWidgets = new HashMap<String, ResearchWidget>();
        this.researchConnectors = new LinkedList<ConnectorWidget>();
        this.researchDialog = researchDialog;
    }
    
    @Override
    public void refresh() {
        this.researchGroups.clear();
        this.researchWidgets.clear();
        this.researchConnectors.clear();
        final ResearchTree researchTree = ((SkillTreeContainer)this.parentScreen.getMenu()).getResearchTree();
        ModConfigs.RESEARCH_GROUPS.getGroups().forEach((groupId, group) -> {
            final ResearchGroupStyle style = ModConfigs.RESEARCH_GROUP_STYLES.getStyle(groupId);
            if (style != null) {
                this.researchGroups.add(new ResearchGroupWidget(style, researchTree, () -> this.selectedWidget));
            }
            return;
        });
        ModConfigs.RESEARCHES_GUI.getStyles().forEach((researchName, style) -> this.researchWidgets.put(researchName, new ResearchWidget(researchName, researchTree, style)));
        ModConfigs.RESEARCHES_GUI.getStyles().forEach((researchName, style) -> {
            final ResearchWidget target = this.researchWidgets.get(researchName);
            if (target != null) {
                ModConfigs.SKILL_GATES.getGates().getDependencyResearches(researchName).forEach(dependentOn -> {
                    final ResearchWidget source = this.researchWidgets.get(dependentOn.getName());
                    if (source == null) {
                        return;
                    }
                    else {
                        this.researchConnectors.add(new ConnectorWidget(source, target, ConnectorWidget.ConnectorType.ARROW));
                        return;
                    }
                });
                ModConfigs.SKILL_GATES.getGates().getLockedByResearches(researchName).forEach(dependentOn -> {
                    final ResearchWidget source2 = this.researchWidgets.get(dependentOn.getName());
                    if (source2 != null) {
                        this.researchConnectors.add(new ConnectorWidget(source2, target, ConnectorWidget.ConnectorType.DOUBLE_ARROW));
                    }
                });
            }
        });
    }
    
    @Override
    public String getTabName() {
        return "Researches";
    }
    
    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        final boolean mouseClicked = super.mouseClicked(mouseX, mouseY, button);
        final Point2D.Float midpoint = MiscUtils.getMidpoint(this.parentScreen.getContainerBounds());
        final int containerMouseX = (int)((mouseX - midpoint.x) / this.viewportScale - this.viewportTranslation.x);
        final int containerMouseY = (int)((mouseY - midpoint.y) / this.viewportScale - this.viewportTranslation.y);
        for (final ResearchWidget researchWidget : this.researchWidgets.values()) {
            if (researchWidget.isMouseOver(containerMouseX, containerMouseY) && researchWidget.mouseClicked(containerMouseX, containerMouseY, button)) {
                if (this.selectedWidget != null) {
                    this.selectedWidget.deselect();
                }
                (this.selectedWidget = researchWidget).select();
                this.researchDialog.setResearchName(this.selectedWidget.getResearchName());
                break;
            }
        }
        return mouseClicked;
    }
    
    @Override
    public void renderTabForeground(final MatrixStack renderStack, final int mouseX, final int mouseY, final float pTicks, final List<Runnable> postContainerRender) {
        RenderSystem.enableBlend();
        final Point2D.Float midpoint = MiscUtils.getMidpoint(this.parentScreen.getContainerBounds());
        renderStack.pushPose();
        renderStack.translate((double)midpoint.x, (double)midpoint.y, 0.0);
        renderStack.scale(this.viewportScale, this.viewportScale, 1.0f);
        renderStack.translate((double)this.viewportTranslation.x, (double)this.viewportTranslation.y, 0.0);
        final int containerMouseX = (int)((mouseX - midpoint.x) / this.viewportScale - this.viewportTranslation.x);
        final int containerMouseY = (int)((mouseY - midpoint.y) / this.viewportScale - this.viewportTranslation.y);
        for (final ResearchGroupWidget researchGroupWidget : this.researchGroups) {
            researchGroupWidget.render(renderStack, containerMouseX, containerMouseY, pTicks);
        }
        for (final ConnectorWidget researchConnector : this.researchConnectors) {
            researchConnector.renderConnection(renderStack, containerMouseX, containerMouseY, pTicks, this.viewportScale);
        }
        for (final ResearchWidget researchWidget : this.researchWidgets.values()) {
            researchWidget.renderWidget(renderStack, containerMouseX, containerMouseY, pTicks, postContainerRender);
        }
        renderStack.popPose();
    }
}
