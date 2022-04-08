package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.helper.AnimationTwoPhased;
import iskallia.vault.client.gui.helper.FontHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.stream.Collectors;


@OnlyIn(Dist.CLIENT)
public class VaultBarOverlay {
    public static final ResourceLocation VAULT_HUD_SPRITE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");

    public static int vaultLevel;
    public static int vaultExp;
    public static int tnl;
    public static int unspentSkillPoints;
    public static int unspentKnowledgePoints;
    public static AnimationTwoPhased expGainedAnimation = new AnimationTwoPhased(0.0F, 1.0F, 0.0F, 500);
    public static long previousTick = System.currentTimeMillis();

    @SubscribeEvent
    public static void onPostRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) {
            return;
        }
        long now = System.currentTimeMillis();

        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerEntity player = minecraft.player;
        if (player == null) {
            return;
        }

        MatrixStack matrixStack = event.getMatrixStack();
        int midX = minecraft.getWindow().getGuiScaledWidth() / 2;
        int bottom = minecraft.getWindow().getGuiScaledHeight();
        int right = minecraft.getWindow().getGuiScaledWidth();

        String text = String.valueOf(vaultLevel);
        int textX = midX + 50 - minecraft.font.width(text) / 2;
        int textY = bottom - 54;
        int barWidth = 85;
        float expPercentage = vaultExp / tnl;

        int potionOffsetY = potionOffsetY(player);
        int gap = 5;

        matrixStack.pushPose();

        if (potionOffsetY > 0) {
            matrixStack.translate(0.0D, potionOffsetY, 0.0D);
        }

        if (unspentSkillPoints > 0) {
            minecraft.getTextureManager().bind(VAULT_HUD_SPRITE);
            String unspentText = (unspentSkillPoints == 1) ? " unspent skill point" : " unspent skill points";


            String unspentPointsText = unspentSkillPoints + "";
            int unspentPointsWidth = minecraft.font.width(unspentPointsText);
            int unspentWidth = minecraft.font.width(unspentText);
            minecraft.font.drawShadow(matrixStack, unspentSkillPoints + "", (right - unspentWidth - unspentPointsWidth - gap), 18.0F, -10240);


            minecraft.font.drawShadow(matrixStack, unspentText, (right - unspentWidth - gap), 18.0F, -1);


            minecraft.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
        }

        if (unspentKnowledgePoints > 0) {
            minecraft.getTextureManager().bind(VAULT_HUD_SPRITE);
            String unspentText = (unspentKnowledgePoints == 1) ? " unspent knowledge point" : " unspent knowledge points";


            String unspentPointsText = unspentKnowledgePoints + "";
            int unspentPointsWidth = minecraft.font.width(unspentPointsText);
            int unspentWidth = minecraft.font.width(unspentText);
            matrixStack.pushPose();
            if (unspentSkillPoints > 0) {
                matrixStack.translate(0.0D, 12.0D, 0.0D);
            }
            minecraft.font.drawShadow(matrixStack, unspentKnowledgePoints + "", (right - unspentWidth - unspentPointsWidth - gap), 18.0F, -12527695);


            minecraft.font.drawShadow(matrixStack, unspentText, (right - unspentWidth - gap), 18.0F, -1);


            matrixStack.popPose();

            minecraft.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
        }

        matrixStack.popPose();

        expGainedAnimation.tick((int) (now - previousTick));
        previousTick = now;

        if (minecraft.gameMode == null || !minecraft.gameMode.hasExperience()) {
            return;
        }
        minecraft.getProfiler().push("vaultBar");
        minecraft.getTextureManager().bind(VAULT_HUD_SPRITE);
        RenderSystem.enableBlend();
        minecraft.gui.blit(matrixStack, midX + 9, bottom - 48, 1, 1, barWidth, 5);


        if (expGainedAnimation.getValue() != 0.0F) {
            GlStateManager._color4f(1.0F, 1.0F, 1.0F, expGainedAnimation.getValue());
            minecraft.gui.blit(matrixStack, midX + 8, bottom - 49, 62, 41, 84, 7);


            GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        minecraft.gui.blit(matrixStack, midX + 9, bottom - 48, 1, 7, (int) (barWidth * expPercentage), 5);


        if (expGainedAnimation.getValue() != 0.0F) {
            GlStateManager._color4f(1.0F, 1.0F, 1.0F, expGainedAnimation.getValue());
            minecraft.gui.blit(matrixStack, midX + 8, bottom - 49, 62, 49, (int) (barWidth * expPercentage), 7);


            GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        FontHelper.drawStringWithBorder(matrixStack, text, textX, textY, -6601, 3945472);


        minecraft.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
        minecraft.getProfiler().pop();
    }


    private static int potionOffsetY(ClientPlayerEntity player) {
        List<EffectInstance> effectInstances = (List<EffectInstance>) player.getActiveEffects().stream().filter(EffectInstance::showIcon).collect(Collectors.toList());

        if (effectInstances.size() == 0) return 0;

        for (EffectInstance effectInstance : effectInstances) {
            if (effectInstance.getEffect().getCategory() == EffectType.HARMFUL) return 36;

        }
        return 18;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\VaultBarOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */