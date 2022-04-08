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

public class AbilitySelectionScreen
        extends Screen {
    public static final ResourceLocation HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
    private static final ResourceLocation ABILITIES_RESOURCE = new ResourceLocation("the_vault", "textures/gui/abilities.png");

    public AbilitySelectionScreen() {
        super((ITextComponent) new StringTextComponent(""));
    }

    public List<AbilitySelectionWidget> getAbilitiesAsWidgets() {
        List<AbilitySelectionWidget> abilityWidgets = new LinkedList<>();

        Minecraft minecraft = Minecraft.getInstance();

        float midX = minecraft.getWindow().getGuiScaledWidth() / 2.0F;
        float midY = minecraft.getWindow().getGuiScaledHeight() / 2.0F;
        float radius = 60.0F;

        List<AbilityNode<?, ?>> learnedAbilities = ClientAbilityData.getLearnedAbilityNodes();
        double clickableAngle = 6.283185307179586D / learnedAbilities.size();
        for (int i = 0; i < learnedAbilities.size(); i++) {
            AbilityNode<?, ?> ability = learnedAbilities.get(i);
            double angle = i * 6.283185307179586D / learnedAbilities.size() - 1.5707963267948966D;
            double x = radius * Math.cos(angle) + midX;
            double y = radius * Math.sin(angle) + midY;

            AbilitySelectionWidget widget = new AbilitySelectionWidget((int) x, (int) y, ability, clickableAngle / 2.0D);
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


    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (AbilitySelectionWidget widget : getAbilitiesAsWidgets()) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                requestSwap(widget.getAbilityNode());
                onClose();
                return true;
            }
        }

        onClose();
        return super.mouseReleased(mouseX, mouseY, button);
    }


    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == ModKeybinds.abilityWheelKey.getKey().getValue()) {
            Minecraft minecraft = Minecraft.getInstance();

            double guiScaleFactor = minecraft.getWindow().getGuiScale();
            double mouseX = minecraft.mouseHandler.xpos() / guiScaleFactor;
            double mouseY = minecraft.mouseHandler.ypos() / guiScaleFactor;

            for (AbilitySelectionWidget widget : getAbilitiesAsWidgets()) {
                if (widget.isMouseOver(mouseX, mouseY)) {
                    requestSwap(widget.getAbilityNode());

                    break;
                }
            }
            onClose();
            return true;
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    public void requestSwap(AbilityNode<?, ?> abilityNode) {
        if (!abilityNode.getGroup().equals(ClientAbilityData.getSelectedAbility())) {
            ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(abilityNode.getGroup()));
        }
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);

        Minecraft minecraft = Minecraft.getInstance();

        float midX = minecraft.getWindow().getGuiScaledWidth() / 2.0F;
        float midY = minecraft.getWindow().getGuiScaledHeight() / 2.0F;
        float radius = 60.0F;

        List<AbilitySelectionWidget> abilitiesAsWidgets = getAbilitiesAsWidgets();
        boolean focusRendered = false;
        for (AbilitySelectionWidget widget : abilitiesAsWidgets) {
            widget.render(matrixStack, mouseX, mouseY, partialTicks);

            if (!focusRendered && widget.isMouseOver(mouseX, mouseY)) {
                int yOffset = 35;
                if (widget.getAbilityNode().getSpecialization() != null) {
                    yOffset += 10;
                }

                String abilityName = widget.getAbilityNode().getName();
                int abilityNameWidth = minecraft.font.width(abilityName);
                minecraft.font.drawShadow(matrixStack, abilityName, midX - abilityNameWidth / 2.0F, midY - radius + yOffset, 16777215);


                if (widget.getAbilityNode().getSpecialization() != null) {
                    String specName = widget.getAbilityNode().getSpecializationName();
                    int specNameWidth = minecraft.font.width(specName);
                    minecraft.font.drawShadow(matrixStack, specName, midX - specNameWidth / 2.0F, midY - radius + yOffset - 10.0F, TextFormatting.GOLD

                            .getColor().intValue());
                }


                if (widget.getAbilityNode().getGroup().equals(ClientAbilityData.getSelectedAbility())) {
                    String text = "Currently Focused Ability";
                    int textWidth = minecraft.font.width(text);
                    minecraft.font.drawShadow(matrixStack, text, midX - textWidth / 2.0F, midY + radius + 15.0F, 11266750);
                }


                focusRendered = true;
            }
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\screen\AbilitySelectionScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */