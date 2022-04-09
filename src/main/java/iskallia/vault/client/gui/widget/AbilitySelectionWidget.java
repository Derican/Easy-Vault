// 
// Decompiled by Procyon v0.6.0
// 

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

public class AbilitySelectionWidget extends Widget
{
    public static final ResourceLocation HUD_RESOURCE;
    private static final ResourceLocation ABILITIES_RESOURCE;
    protected AbilityNode<?, ?> abilityNode;
    protected double angleBoundary;
    
    public AbilitySelectionWidget(final int x, final int y, final AbilityNode<?, ?> abilityNode, final double angleBoundary) {
        super(x, y, 24, 24, (ITextComponent)new StringTextComponent(abilityNode.getName()));
        this.abilityNode = abilityNode;
        this.angleBoundary = angleBoundary;
    }
    
    public AbilityNode<?, ?> getAbilityNode() {
        return this.abilityNode;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(this.x - 12, this.y - 12, this.width, this.height);
    }
    
    public boolean isMouseOver(final double mouseX, final double mouseY) {
        final Minecraft minecraft = Minecraft.getInstance();
        final float midX = minecraft.getWindow().getGuiScaledWidth() / 2.0f;
        final float midY = minecraft.getWindow().getGuiScaledHeight() / 2.0f;
        final Vector2f towardsWidget = new Vector2f(this.x - midX, this.y - midY);
        final Vector2f towardsMouse = new Vector2f((float)mouseX - midX, (float)(mouseY - midY));
        final double dot = towardsWidget.x * towardsMouse.x + towardsWidget.y * towardsMouse.y;
        final double angleBetween = Math.acos(dot / (MathUtilities.length(towardsWidget) * MathUtilities.length(towardsMouse)));
        return angleBetween < this.angleBoundary;
    }
    
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final Rectangle bounds = this.getBounds();
        final Minecraft minecraft = Minecraft.getInstance();
        final String styleKey = (this.abilityNode.getSpecialization() != null) ? this.abilityNode.getSpecialization() : this.abilityNode.getGroup().getParentName();
        final SkillStyle abilityStyle = ModConfigs.ABILITIES_GUI.getStyles().get(styleKey);
        final AbilityGroup<?, ?> thisAbility = this.abilityNode.getGroup();
        final int cooldown = ClientAbilityData.getCooldown(thisAbility);
        final int maxCooldown = ClientAbilityData.getMaxCooldown(thisAbility);
        if (thisAbility.equals(ClientAbilityData.getSelectedAbility())) {
            RenderSystem.color4f(0.7f, 0.7f, 0.7f, 0.3f);
        }
        else {
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        RenderSystem.enableBlend();
        minecraft.getTextureManager().bind(AbilitySelectionWidget.HUD_RESOURCE);
        this.blit(matrixStack, bounds.x + 1, bounds.y + 1, 28, 36, 22, 22);
        minecraft.getTextureManager().bind(AbilitySelectionWidget.ABILITIES_RESOURCE);
        this.blit(matrixStack, bounds.x + 4, bounds.y + 4, abilityStyle.u, abilityStyle.v, 16, 16);
        if (cooldown > 0) {
            RenderSystem.color4f(0.7f, 0.7f, 0.7f, 0.5f);
            final float cooldownPercent = cooldown / (float)maxCooldown;
            final int cooldownHeight = (int)(16.0f * cooldownPercent);
            AbstractGui.fill(matrixStack, bounds.x + 4, bounds.y + 4 + (16 - cooldownHeight), bounds.x + 4 + 16, bounds.y + 4 + 16, -1711276033);
            RenderSystem.enableBlend();
        }
        if (thisAbility.equals(ClientAbilityData.getSelectedAbility())) {
            minecraft.getTextureManager().bind(AbilitySelectionWidget.HUD_RESOURCE);
            this.blit(matrixStack, bounds.x, bounds.y, 89, 13, 24, 24);
        }
        else if (this.isMouseOver(mouseX, mouseY)) {
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            minecraft.getTextureManager().bind(AbilitySelectionWidget.HUD_RESOURCE);
            this.blit(matrixStack, bounds.x, bounds.y, 64 + ((cooldown > 0) ? 50 : 0), 13, 24, 24);
        }
    }
    
    static {
        HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
        ABILITIES_RESOURCE = new ResourceLocation("the_vault", "textures/gui/abilities.png");
    }
}
