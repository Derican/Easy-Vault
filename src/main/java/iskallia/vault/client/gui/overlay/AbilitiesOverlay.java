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
    public static final ResourceLocation HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
    private static final ResourceLocation ABILITIES_RESOURCE = new ResourceLocation("the_vault", "textures/gui/abilities.png");

    @SubscribeEvent
    public static void onPostRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) {
            return;
        }
        List<AbilityNode<?, ?>> abilities = ClientAbilityData.getLearnedAbilityNodes();
        if (abilities.isEmpty()) {
            return;
        }
        AbilityGroup<?, ?> selectedAbilityGroup = ClientAbilityData.getSelectedAbility();
        if (selectedAbilityGroup == null) {
            return;
        }
        AbilityNode<?, ?> selectAbilityNode = ClientAbilityData.getLearnedAbilityNode(selectedAbilityGroup);
        if (selectAbilityNode == null) {
            return;
        }

        int selectedAbilityIndex = ClientAbilityData.getIndexOf(selectedAbilityGroup);
        if (selectedAbilityIndex == -1) {
            return;
        }

        int previousIndex = selectedAbilityIndex - 1;
        if (previousIndex < 0) {
            previousIndex += abilities.size();
        }
        AbilityNode<?, ?> previousAbility = abilities.get(previousIndex);

        int nextIndex = selectedAbilityIndex + 1;
        if (nextIndex >= abilities.size()) {
            nextIndex -= abilities.size();
        }
        AbilityNode<?, ?> nextAbility = abilities.get(nextIndex);

        MatrixStack matrixStack = event.getMatrixStack();
        Minecraft minecraft = Minecraft.getInstance();
        int bottom = minecraft.getWindow().getGuiScaledHeight();
        int barWidth = 62;
        int barHeight = 22;

        minecraft.getProfiler().push("abilityBar");
        matrixStack.pushPose();

        RenderSystem.enableBlend();
        matrixStack.translate(10.0D, (bottom - barHeight), 0.0D);

        minecraft.getTextureManager().bind(HUD_RESOURCE);
        minecraft.gui.blit(matrixStack, 0, 0, 1, 13, barWidth, barHeight);


        minecraft.getTextureManager().bind(ABILITIES_RESOURCE);

        int selectedCooldown = ClientAbilityData.getCooldown(selectedAbilityGroup);
        int selectedMaxCooldown = ClientAbilityData.getMaxCooldown(selectedAbilityGroup);

        String styleKey = (selectAbilityNode.getSpecialization() != null) ? selectAbilityNode.getSpecialization() : selectAbilityNode.getGroup().getParentName();
        SkillStyle focusedStyle = (SkillStyle) ModConfigs.ABILITIES_GUI.getStyles().get(styleKey);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, (selectedCooldown > 0) ? 0.4F : 1.0F);
        minecraft.gui.blit(matrixStack, 23, 3, focusedStyle.u, focusedStyle.v, 16, 16);


        if (selectedCooldown > 0) {
            float cooldownPercent = selectedCooldown / Math.max(1, selectedMaxCooldown);
            int cooldownHeight = (int) (16.0F * cooldownPercent);
            AbstractGui.fill(matrixStack, 23, 3 + 16 - cooldownHeight, 39, 19, -1711276033);


            RenderSystem.enableBlend();
        }

        int previousCooldown = ClientAbilityData.getCooldown(previousAbility.getGroup());
        int previousMaxCooldown = ClientAbilityData.getMaxCooldown(previousAbility.getGroup());
        RenderSystem.color4f(0.7F, 0.7F, 0.7F, 0.5F);
        if (previousCooldown > 0) {
            float cooldownPercent = previousCooldown / Math.max(1, previousMaxCooldown);
            int cooldownHeight = (int) (16.0F * cooldownPercent);
            AbstractGui.fill(matrixStack, 43, 3 + 16 - cooldownHeight, 59, 19, -1711276033);


            RenderSystem.enableBlend();
        }

        String prevStyleKey = (previousAbility.getSpecialization() != null) ? previousAbility.getSpecialization() : previousAbility.getGroup().getParentName();
        SkillStyle previousStyle = (SkillStyle) ModConfigs.ABILITIES_GUI.getStyles().get(prevStyleKey);
        minecraft.gui.blit(matrixStack, 43, 3, previousStyle.u, previousStyle.v, 16, 16);


        int nextCooldown = ClientAbilityData.getCooldown(nextAbility.getGroup());
        int nextMaxCooldown = ClientAbilityData.getMaxCooldown(nextAbility.getGroup());
        if (nextCooldown > 0) {
            float cooldownPercent = nextCooldown / Math.max(1, nextMaxCooldown);
            int cooldownHeight = (int) (16.0F * cooldownPercent);
            AbstractGui.fill(matrixStack, 3, 3 + 16 - cooldownHeight, 19, 19, -1711276033);


            RenderSystem.enableBlend();
        }

        String nextStyleKey = (nextAbility.getSpecialization() != null) ? nextAbility.getSpecialization() : nextAbility.getGroup().getParentName();
        SkillStyle nextStyle = (SkillStyle) ModConfigs.ABILITIES_GUI.getStyles().get(nextStyleKey);
        minecraft.gui.blit(matrixStack, 3, 3, nextStyle.u, nextStyle.v, 16, 16);


        minecraft.getTextureManager().bind(HUD_RESOURCE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.gui.blit(matrixStack, 19, -1, 64 + ((selectedCooldown > 0) ? 50 : (

                ClientAbilityData.isActive() ? 25 : 0)), 13, 24, 24);


        matrixStack.popPose();
        minecraft.getProfiler().pop();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\AbilitiesOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */