package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.ClientAbilityData;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class AbilitiesOverlay {
    public static final ResourceLocation HUD_RESOURCE;
    private static final ResourceLocation ABILITIES_RESOURCE;

    @SubscribeEvent
    public static void onPostRender(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) {
            return;
        }
        final List<AbilityNode<?, ?>> abilities = ClientAbilityData.getLearnedAbilityNodes();
        if (abilities.isEmpty()) {
            return;
        }
        final AbilityGroup<?, ?> selectedAbilityGroup = ClientAbilityData.getSelectedAbility();
        if (selectedAbilityGroup == null) {
            return;
        }
        final AbilityNode<?, ?> selectAbilityNode = ClientAbilityData.getLearnedAbilityNode(selectedAbilityGroup);
        if (selectAbilityNode == null) {
            return;
        }
        final int selectedAbilityIndex = ClientAbilityData.getIndexOf(selectedAbilityGroup);
        if (selectedAbilityIndex == -1) {
            return;
        }
        int previousIndex = selectedAbilityIndex - 1;
        if (previousIndex < 0) {
            previousIndex += abilities.size();
        }
        final AbilityNode<?, ?> previousAbility = abilities.get(previousIndex);
        int nextIndex = selectedAbilityIndex + 1;
        if (nextIndex >= abilities.size()) {
            nextIndex -= abilities.size();
        }
        final AbilityNode<?, ?> nextAbility = abilities.get(nextIndex);
        final MatrixStack matrixStack = event.getMatrixStack();
        final Minecraft minecraft = Minecraft.getInstance();
        final int bottom = minecraft.getWindow().getGuiScaledHeight();
        final int barWidth = 62;
        final int barHeight = 22;
        minecraft.getProfiler().push("abilityBar");
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        matrixStack.translate(10.0, bottom - barHeight, 0.0);
        minecraft.getTextureManager().bind(AbilitiesOverlay.HUD_RESOURCE);
        minecraft.gui.blit(matrixStack, 0, 0, 1, 13, barWidth, barHeight);
        minecraft.getTextureManager().bind(AbilitiesOverlay.ABILITIES_RESOURCE);
        final int selectedCooldown = ClientAbilityData.getCooldown(selectedAbilityGroup);
        final int selectedMaxCooldown = ClientAbilityData.getMaxCooldown(selectedAbilityGroup);
        final String styleKey = (selectAbilityNode.getSpecialization() != null) ? selectAbilityNode.getSpecialization() : selectAbilityNode.getGroup().getParentName();
        final SkillStyle focusedStyle = ModConfigs.ABILITIES_GUI.getStyles().get(styleKey);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, (selectedCooldown > 0) ? 0.4f : 1.0f);
        minecraft.gui.blit(matrixStack, 23, 3, focusedStyle.u, focusedStyle.v, 16, 16);
        if (selectedCooldown > 0) {
            final float cooldownPercent = selectedCooldown / (float) Math.max(1, selectedMaxCooldown);
            final int cooldownHeight = (int) (16.0f * cooldownPercent);
            AbstractGui.fill(matrixStack, 23, 3 + (16 - cooldownHeight), 39, 19, -1711276033);
            RenderSystem.enableBlend();
        }
        final int previousCooldown = ClientAbilityData.getCooldown(previousAbility.getGroup());
        final int previousMaxCooldown = ClientAbilityData.getMaxCooldown(previousAbility.getGroup());
        RenderSystem.color4f(0.7f, 0.7f, 0.7f, 0.5f);
        if (previousCooldown > 0) {
            final float cooldownPercent2 = previousCooldown / (float) Math.max(1, previousMaxCooldown);
            final int cooldownHeight2 = (int) (16.0f * cooldownPercent2);
            AbstractGui.fill(matrixStack, 43, 3 + (16 - cooldownHeight2), 59, 19, -1711276033);
            RenderSystem.enableBlend();
        }
        final String prevStyleKey = (previousAbility.getSpecialization() != null) ? previousAbility.getSpecialization() : previousAbility.getGroup().getParentName();
        final SkillStyle previousStyle = ModConfigs.ABILITIES_GUI.getStyles().get(prevStyleKey);
        minecraft.gui.blit(matrixStack, 43, 3, previousStyle.u, previousStyle.v, 16, 16);
        final int nextCooldown = ClientAbilityData.getCooldown(nextAbility.getGroup());
        final int nextMaxCooldown = ClientAbilityData.getMaxCooldown(nextAbility.getGroup());
        if (nextCooldown > 0) {
            final float cooldownPercent3 = nextCooldown / (float) Math.max(1, nextMaxCooldown);
            final int cooldownHeight3 = (int) (16.0f * cooldownPercent3);
            AbstractGui.fill(matrixStack, 3, 3 + (16 - cooldownHeight3), 19, 19, -1711276033);
            RenderSystem.enableBlend();
        }
        final String nextStyleKey = (nextAbility.getSpecialization() != null) ? nextAbility.getSpecialization() : nextAbility.getGroup().getParentName();
        final SkillStyle nextStyle = ModConfigs.ABILITIES_GUI.getStyles().get(nextStyleKey);
        minecraft.gui.blit(matrixStack, 3, 3, nextStyle.u, nextStyle.v, 16, 16);
        minecraft.getTextureManager().bind(AbilitiesOverlay.HUD_RESOURCE);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        minecraft.gui.blit(matrixStack, 19, -1, 64 + ((selectedCooldown > 0) ? 50 : (ClientAbilityData.isActive() ? 25 : 0)), 13, 24, 24);
        matrixStack.popPose();
        minecraft.getProfiler().pop();
    }

    static {
        HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
        ABILITIES_RESOURCE = new ResourceLocation("the_vault", "textures/gui/abilities.png");
    }
}
