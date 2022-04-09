// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.tab;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.component.AbilityDialog;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.client.gui.widget.AbilityWidget;
import iskallia.vault.client.gui.widget.connect.ConnectorWidget;
import iskallia.vault.container.SkillTreeContainer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.AbilityRegistry;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.effect.AbilityEffect;
import iskallia.vault.util.MiscUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AbilitiesTab extends SkillTab
{
    private final Map<String, AbilityWidget> abilityWidgets;
    private final List<ConnectorWidget> abilityConnectors;
    private final AbilityDialog abilityDialog;
    private AbilityWidget selectedWidget;
    
    public AbilitiesTab(final AbilityDialog abilityDialog, final SkillTreeScreen parentScreen) {
        super(parentScreen, (ITextComponent)new StringTextComponent("Abilities Tab"));
        this.abilityWidgets = new HashMap<String, AbilityWidget>();
        this.abilityConnectors = new LinkedList<ConnectorWidget>();
        this.abilityDialog = abilityDialog;
    }
    
    @Override
    public void refresh() {
        this.abilityWidgets.clear();
        final AbilityTree abilityTree = ((SkillTreeContainer)this.parentScreen.getMenu()).getAbilityTree();
        ModConfigs.ABILITIES_GUI.getStyles().forEach((abilityName, style) -> this.abilityWidgets.put(abilityName, new AbilityWidget(abilityName, abilityTree, style)));
        ModConfigs.ABILITIES_GUI.getStyles().forEach((abilityName, style) -> {
            final AbilityEffect<?> ability = AbilityRegistry.getAbility(abilityName);
            if (!abilityName.equals(ability.getAbilityGroupName())) {
                final AbilityWidget abilityGroup = this.abilityWidgets.get(ability.getAbilityGroupName());
                final AbilityWidget thisAbility = this.abilityWidgets.get(abilityName);
                if (abilityGroup != null && thisAbility != null) {
                    final ConnectorWidget widget = new ConnectorWidget(abilityGroup, thisAbility, ConnectorWidget.ConnectorType.LINE);
                    if (abilityName.equals(abilityTree.getNodeOf(ability).getSpecialization())) {
                        widget.setColor(new Color(13021470));
                    }
                    else {
                        widget.setColor(new Color(5592405));
                    }
                    this.abilityConnectors.add(widget);
                }
            }
        });
    }
    
    @Override
    public String getTabName() {
        return "Abilities";
    }
    
    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        final boolean mouseClicked = super.mouseClicked(mouseX, mouseY, button);
        final Point2D.Float midpoint = MiscUtils.getMidpoint(this.parentScreen.getContainerBounds());
        final int containerMouseX = (int)((mouseX - midpoint.x) / this.viewportScale - this.viewportTranslation.x);
        final int containerMouseY = (int)((mouseY - midpoint.y) / this.viewportScale - this.viewportTranslation.y);
        for (final AbilityWidget abilityWidget : this.abilityWidgets.values()) {
            if (abilityWidget.isMouseOver(containerMouseX, containerMouseY) && abilityWidget.mouseClicked(containerMouseX, containerMouseY, button)) {
                if (this.selectedWidget != null) {
                    this.selectedWidget.deselect();
                }
                (this.selectedWidget = abilityWidget).select();
                this.abilityDialog.setAbilityWidget(this.selectedWidget.getAbilityName());
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
        renderStack.pushPose();
        renderStack.translate(0.0, 10.0, 0.0);
        renderStack.scale(1.6f, 1.6f, 1.6f);
        UIHelper.drawFacingPlayer(renderStack, containerMouseX, containerMouseY);
        renderStack.popPose();
        for (final ConnectorWidget researchConnector : this.abilityConnectors) {
            researchConnector.renderConnection(renderStack, containerMouseX, containerMouseY, pTicks, this.viewportScale);
        }
        for (final AbilityWidget abilityWidget : this.abilityWidgets.values()) {
            abilityWidget.renderWidget(renderStack, containerMouseX, containerMouseY, pTicks, postContainerRender);
        }
        renderStack.popPose();
    }
}
