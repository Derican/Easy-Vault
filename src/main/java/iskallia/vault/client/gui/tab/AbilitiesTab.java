package iskallia.vault.client.gui.tab;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.component.AbilityDialog;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.client.gui.widget.AbilityWidget;
import iskallia.vault.client.gui.widget.connect.ConnectableWidget;
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

public class AbilitiesTab extends SkillTab {
    private final Map<String, AbilityWidget> abilityWidgets = new HashMap<>();
    private final List<ConnectorWidget> abilityConnectors = new LinkedList<>();
    private final AbilityDialog abilityDialog;
    private AbilityWidget selectedWidget;

    public AbilitiesTab(AbilityDialog abilityDialog, SkillTreeScreen parentScreen) {
        super(parentScreen, (ITextComponent) new StringTextComponent("Abilities Tab"));
        this.abilityDialog = abilityDialog;
    }

    public void refresh() {
        this.abilityWidgets.clear();

        AbilityTree abilityTree = ((SkillTreeContainer) this.parentScreen.getMenu()).getAbilityTree();
        ModConfigs.ABILITIES_GUI.getStyles().forEach((abilityName, style) -> this.abilityWidgets.put(abilityName, new AbilityWidget(abilityName, abilityTree, style)));


        ModConfigs.ABILITIES_GUI.getStyles().forEach((abilityName, style) -> {
            AbilityEffect<?> ability = AbilityRegistry.getAbility(abilityName);
            if (!abilityName.equals(ability.getAbilityGroupName())) {
                AbilityWidget abilityGroup = this.abilityWidgets.get(ability.getAbilityGroupName());
                AbilityWidget thisAbility = this.abilityWidgets.get(abilityName);
                if (abilityGroup != null && thisAbility != null) {
                    ConnectorWidget widget = new ConnectorWidget((ConnectableWidget) abilityGroup, (ConnectableWidget) thisAbility, ConnectorWidget.ConnectorType.LINE);
                    if (abilityName.equals(abilityTree.getNodeOf(ability).getSpecialization())) {
                        widget.setColor(new Color(13021470));
                    } else {
                        widget.setColor(new Color(5592405));
                    }
                    this.abilityConnectors.add(widget);
                }
            }
        });
    }


    public String getTabName() {
        return "Abilities";
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean mouseClicked = super.mouseClicked(mouseX, mouseY, button);

        Point2D.Float midpoint = MiscUtils.getMidpoint(this.parentScreen.getContainerBounds());
        int containerMouseX = (int) ((mouseX - midpoint.x) / this.viewportScale - this.viewportTranslation.x);
        int containerMouseY = (int) ((mouseY - midpoint.y) / this.viewportScale - this.viewportTranslation.y);

        for (AbilityWidget abilityWidget : this.abilityWidgets.values()) {
            if (abilityWidget.isMouseOver(containerMouseX, containerMouseY) && abilityWidget.mouseClicked(containerMouseX, containerMouseY, button)) {
                if (this.selectedWidget != null) {
                    this.selectedWidget.deselect();
                }
                this.selectedWidget = abilityWidget;
                this.selectedWidget.select();
                this.abilityDialog.setAbilityWidget(this.selectedWidget.getAbilityName());

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

        renderStack.pushPose();
        renderStack.translate(0.0D, 10.0D, 0.0D);
        renderStack.scale(1.6F, 1.6F, 1.6F);
        UIHelper.drawFacingPlayer(renderStack, containerMouseX, containerMouseY);
        renderStack.popPose();

        for (ConnectorWidget researchConnector : this.abilityConnectors) {
            researchConnector.renderConnection(renderStack, containerMouseX, containerMouseY, pTicks, this.viewportScale);
        }

        for (AbilityWidget abilityWidget : this.abilityWidgets.values()) {
            abilityWidget.renderWidget(renderStack, containerMouseX, containerMouseY, pTicks, postContainerRender);
        }

        renderStack.popPose();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\tab\AbilitiesTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */