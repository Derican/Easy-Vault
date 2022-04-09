// 
// Decompiled by Procyon v0.6.0
// 

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

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TalentsTab extends SkillTab
{
    private final Map<String, TalentWidget> talentWidgets;
    private final List<ConnectorWidget> talentConnectors;
    private final TalentDialog talentDialog;
    private TalentWidget selectedWidget;
    
    public TalentsTab(final TalentDialog talentDialog, final SkillTreeScreen parentScreen) {
        super(parentScreen, (ITextComponent)new StringTextComponent("Talents Tab"));
        this.talentWidgets = new HashMap<String, TalentWidget>();
        this.talentConnectors = new LinkedList<ConnectorWidget>();
        this.talentDialog = talentDialog;
    }
    
    @Override
    public void refresh() {
        this.talentWidgets.clear();
        final TalentTree talentTree = ((SkillTreeContainer)this.parentScreen.getMenu()).getTalentTree();
        ModConfigs.TALENTS_GUI.getStyles().forEach((talentName, style) -> this.talentWidgets.put(talentName, new TalentWidget(ModConfigs.TALENTS.getByName(talentName), talentTree, style)));
        ModConfigs.TALENTS_GUI.getStyles().forEach((researchName, style) -> {
            final TalentWidget target = this.talentWidgets.get(researchName);
            if (target != null) {
                ModConfigs.SKILL_GATES.getGates().getDependencyTalents(researchName).forEach(dependentOn -> {
                    final TalentWidget source = this.talentWidgets.get(dependentOn.getParentName());
                    if (source == null) {
                        return;
                    }
                    else {
                        this.talentConnectors.add(new ConnectorWidget(source, target, ConnectorWidget.ConnectorType.ARROW));
                        return;
                    }
                });
                ModConfigs.SKILL_GATES.getGates().getLockedByTalents(researchName).forEach(dependentOn -> {
                    final TalentWidget source2 = this.talentWidgets.get(dependentOn.getParentName());
                    if (source2 != null) {
                        final ConnectorWidget widget = new ConnectorWidget(source2, target, ConnectorWidget.ConnectorType.DOUBLE_ARROW);
                        widget.setColor(new Color(11272192));
                        this.talentConnectors.add(widget);
                    }
                });
            }
        });
    }
    
    @Override
    public String getTabName() {
        return "Talents";
    }
    
    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        final boolean mouseClicked = super.mouseClicked(mouseX, mouseY, button);
        final Point2D.Float midpoint = MiscUtils.getMidpoint(this.parentScreen.getContainerBounds());
        final int containerMouseX = (int)((mouseX - midpoint.x) / this.viewportScale - this.viewportTranslation.x);
        final int containerMouseY = (int)((mouseY - midpoint.y) / this.viewportScale - this.viewportTranslation.y);
        for (final TalentWidget abilityWidget : this.talentWidgets.values()) {
            if (abilityWidget.isMouseOver(containerMouseX, containerMouseY) && abilityWidget.mouseClicked(containerMouseX, containerMouseY, button)) {
                if (this.selectedWidget != null) {
                    this.selectedWidget.deselect();
                }
                (this.selectedWidget = abilityWidget).select();
                this.talentDialog.setTalentGroup(this.selectedWidget.getTalentGroup());
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
        for (final ConnectorWidget talentConnector : this.talentConnectors) {
            talentConnector.renderConnection(renderStack, containerMouseX, containerMouseY, pTicks, this.viewportScale);
        }
        for (final TalentWidget abilityWidget : this.talentWidgets.values()) {
            abilityWidget.renderWidget(renderStack, containerMouseX, containerMouseY, pTicks, postContainerRender);
        }
        renderStack.popPose();
    }
}
