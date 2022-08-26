package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.ClientAbilityData;
import iskallia.vault.client.gui.widget.AbilitySelectionWidget;
import iskallia.vault.init.ModKeybinds;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.AbilityKeyMessage;
import iskallia.vault.skill.ability.AbilityNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.LinkedList;
import java.util.List;

public class AbilitySelectionScreen extends Screen {
    public static final ResourceLocation HUD_RESOURCE;
    private static final ResourceLocation ABILITIES_RESOURCE;

    public AbilitySelectionScreen() {
        super(new StringTextComponent(""));
    }

    public List<AbilitySelectionWidget> getAbilitiesAsWidgets() {
        final List<AbilitySelectionWidget> abilityWidgets = new LinkedList<AbilitySelectionWidget>();
        final Minecraft minecraft = Minecraft.getInstance();
        final float midX = minecraft.getWindow().getGuiScaledWidth() / 2.0f;
        final float midY = minecraft.getWindow().getGuiScaledHeight() / 2.0f;
        final float radius = 60.0f;
        final List<AbilityNode<?, ?>> learnedAbilities = ClientAbilityData.getLearnedAbilityNodes();
        final double clickableAngle = 6.283185307179586 / learnedAbilities.size();
        for (int i = 0; i < learnedAbilities.size(); ++i) {
            final AbilityNode<?, ?> ability = learnedAbilities.get(i);
            final double angle = i * (6.283185307179586 / learnedAbilities.size()) - 1.5707963267948966;
            final double x = radius * Math.cos(angle) + midX;
            final double y = radius * Math.sin(angle) + midY;
            final AbilitySelectionWidget widget = new AbilitySelectionWidget((int) x, (int) y, ability, clickableAngle / 2.0);
            abilityWidgets.add(widget);
        }
        return abilityWidgets;
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    public boolean isPauseScreen() {
        return false;
    }

    public boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        for (final AbilitySelectionWidget widget : this.getAbilitiesAsWidgets()) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                this.requestSwap(widget.getAbilityNode());
                this.onClose();
                return true;
            }
        }
        this.onClose();
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean keyReleased(final int keyCode, final int scanCode, final int modifiers) {
        if (keyCode == ModKeybinds.abilityWheelKey.getKey().getValue()) {
            final Minecraft minecraft = Minecraft.getInstance();
            final double guiScaleFactor = minecraft.getWindow().getGuiScale();
            final double mouseX = minecraft.mouseHandler.xpos() / guiScaleFactor;
            final double mouseY = minecraft.mouseHandler.ypos() / guiScaleFactor;
            for (final AbilitySelectionWidget widget : this.getAbilitiesAsWidgets()) {
                if (widget.isMouseOver(mouseX, mouseY)) {
                    this.requestSwap(widget.getAbilityNode());
                    break;
                }
            }
            this.onClose();
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    public void requestSwap(final AbilityNode<?, ?> abilityNode) {
        if (!abilityNode.getGroup().equals(ClientAbilityData.getSelectedAbility())) {
            ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(abilityNode.getGroup()));
        }
    }

    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        final Minecraft minecraft = Minecraft.getInstance();
        final float midX = minecraft.getWindow().getGuiScaledWidth() / 2.0f;
        final float midY = minecraft.getWindow().getGuiScaledHeight() / 2.0f;
        final float radius = 60.0f;
        final List<AbilitySelectionWidget> abilitiesAsWidgets = this.getAbilitiesAsWidgets();
        boolean focusRendered = false;
        for (final AbilitySelectionWidget widget : abilitiesAsWidgets) {
            widget.render(matrixStack, mouseX, mouseY, partialTicks);
            if (!focusRendered && widget.isMouseOver(mouseX, mouseY)) {
                int yOffset = 35;
                if (widget.getAbilityNode().getSpecialization() != null) {
                    yOffset += 10;
                }
                final String abilityName = widget.getAbilityNode().getName();
                final int abilityNameWidth = minecraft.font.width(abilityName);
                minecraft.font.drawShadow(matrixStack, abilityName, midX - abilityNameWidth / 2.0f, midY - (radius + yOffset), 16777215);
                if (widget.getAbilityNode().getSpecialization() != null) {
                    final String specName = widget.getAbilityNode().getSpecializationName();
                    final int specNameWidth = minecraft.font.width(specName);
                    minecraft.font.drawShadow(matrixStack, specName, midX - specNameWidth / 2.0f, midY - (radius + yOffset - 10.0f), TextFormatting.GOLD.getColor());
                }
                if (widget.getAbilityNode().getGroup().equals(ClientAbilityData.getSelectedAbility())) {
                    final String text = "Currently Focused Ability";
                    final int textWidth = minecraft.font.width(text);
                    minecraft.font.drawShadow(matrixStack, text, midX - textWidth / 2.0f, midY + (radius + 15.0f), 11266750);
                }
                focusRendered = true;
            }
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    static {
        HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
        ABILITIES_RESOURCE = new ResourceLocation("the_vault", "textures/gui/abilities.png");
    }
}
