package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.ClientAbilityData;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.util.MathUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;

public class AbilitySelectionWidget
        extends Widget {
    public static final ResourceLocation HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
    private static final ResourceLocation ABILITIES_RESOURCE = new ResourceLocation("the_vault", "textures/gui/abilities.png");

    protected AbilityNode<?, ?> abilityNode;
    protected double angleBoundary;

    public AbilitySelectionWidget(int x, int y, AbilityNode<?, ?> abilityNode, double angleBoundary) {
        super(x, y, 24, 24, (ITextComponent) new StringTextComponent(abilityNode.getName()));
        this.abilityNode = abilityNode;
        this.angleBoundary = angleBoundary;
    }

    public AbilityNode<?, ?> getAbilityNode() {
        return this.abilityNode;
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x - 12, this.y - 12, this.width, this.height);
    }


    public boolean isMouseOver(double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();

        float midX = minecraft.getWindow().getGuiScaledWidth() / 2.0F;
        float midY = minecraft.getWindow().getGuiScaledHeight() / 2.0F;

        Vector2f towardsWidget = new Vector2f(this.x - midX, this.y - midY);
        Vector2f towardsMouse = new Vector2f((float) mouseX - midX, (float) (mouseY - midY));

        double dot = (towardsWidget.x * towardsMouse.x + towardsWidget.y * towardsMouse.y);
        double angleBetween = Math.acos(dot / MathUtilities.length(towardsWidget) * MathUtilities.length(towardsMouse));

        return (angleBetween < this.angleBoundary);
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Rectangle bounds = getBounds();
        Minecraft minecraft = Minecraft.getInstance();


        String styleKey = (this.abilityNode.getSpecialization() != null) ? this.abilityNode.getSpecialization() : this.abilityNode.getGroup().getParentName();
        SkillStyle abilityStyle = (SkillStyle) ModConfigs.ABILITIES_GUI.getStyles().get(styleKey);

        AbilityGroup<?, ?> thisAbility = this.abilityNode.getGroup();
        int cooldown = ClientAbilityData.getCooldown(thisAbility);
        int maxCooldown = ClientAbilityData.getMaxCooldown(thisAbility);

        if (thisAbility.equals(ClientAbilityData.getSelectedAbility())) {
            RenderSystem.color4f(0.7F, 0.7F, 0.7F, 0.3F);
        } else {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        RenderSystem.enableBlend();

        minecraft.getTextureManager().bind(HUD_RESOURCE);
        blit(matrixStack, bounds.x + 1, bounds.y + 1, 28, 36, 22, 22);


        minecraft.getTextureManager().bind(ABILITIES_RESOURCE);
        blit(matrixStack, bounds.x + 4, bounds.y + 4, abilityStyle.u, abilityStyle.v, 16, 16);


        if (cooldown > 0) {
            RenderSystem.color4f(0.7F, 0.7F, 0.7F, 0.5F);
            float cooldownPercent = cooldown / maxCooldown;
            int cooldownHeight = (int) (16.0F * cooldownPercent);
            AbstractGui.fill(matrixStack, bounds.x + 4, bounds.y + 4 + 16 - cooldownHeight, bounds.x + 4 + 16, bounds.y + 4 + 16, -1711276033);


            RenderSystem.enableBlend();
        }

        if (thisAbility.equals(ClientAbilityData.getSelectedAbility())) {
            minecraft.getTextureManager().bind(HUD_RESOURCE);
            blit(matrixStack, bounds.x, bounds.y, 89, 13, 24, 24);


        } else if (isMouseOver(mouseX, mouseY)) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            minecraft.getTextureManager().bind(HUD_RESOURCE);
            blit(matrixStack, bounds.x, bounds.y, 64 + ((cooldown > 0) ? 50 : 0), 13, 24, 24);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\widget\AbilitySelectionWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */