// 
// Decompiled by Procyon v0.6.0
// 

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
import java.util.stream.Collector;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class VaultBarOverlay
{
    public static final ResourceLocation VAULT_HUD_SPRITE;
    public static int vaultLevel;
    public static int vaultExp;
    public static int tnl;
    public static int unspentSkillPoints;
    public static int unspentKnowledgePoints;
    public static AnimationTwoPhased expGainedAnimation;
    public static long previousTick;
    
    @SubscribeEvent
    public static void onPostRender(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) {
            return;
        }
        final long now = System.currentTimeMillis();
        final Minecraft minecraft = Minecraft.getInstance();
        final ClientPlayerEntity player = minecraft.player;
        if (player == null) {
            return;
        }
        final MatrixStack matrixStack = event.getMatrixStack();
        final int midX = minecraft.getWindow().getGuiScaledWidth() / 2;
        final int bottom = minecraft.getWindow().getGuiScaledHeight();
        final int right = minecraft.getWindow().getGuiScaledWidth();
        final String text = String.valueOf(VaultBarOverlay.vaultLevel);
        final int textX = midX + 50 - minecraft.font.width(text) / 2;
        final int textY = bottom - 54;
        final int barWidth = 85;
        final float expPercentage = VaultBarOverlay.vaultExp / (float)VaultBarOverlay.tnl;
        final int potionOffsetY = potionOffsetY(player);
        final int gap = 5;
        matrixStack.pushPose();
        if (potionOffsetY > 0) {
            matrixStack.translate(0.0, (double)potionOffsetY, 0.0);
        }
        if (VaultBarOverlay.unspentSkillPoints > 0) {
            minecraft.getTextureManager().bind(VaultBarOverlay.VAULT_HUD_SPRITE);
            final String unspentText = (VaultBarOverlay.unspentSkillPoints == 1) ? " unspent skill point" : " unspent skill points";
            final String unspentPointsText = VaultBarOverlay.unspentSkillPoints + "";
            final int unspentPointsWidth = minecraft.font.width(unspentPointsText);
            final int unspentWidth = minecraft.font.width(unspentText);
            minecraft.font.drawShadow(matrixStack, VaultBarOverlay.unspentSkillPoints + "", (float)(right - unspentWidth - unspentPointsWidth - gap), 18.0f, -10240);
            minecraft.font.drawShadow(matrixStack, unspentText, (float)(right - unspentWidth - gap), 18.0f, -1);
            minecraft.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
        }
        if (VaultBarOverlay.unspentKnowledgePoints > 0) {
            minecraft.getTextureManager().bind(VaultBarOverlay.VAULT_HUD_SPRITE);
            final String unspentText = (VaultBarOverlay.unspentKnowledgePoints == 1) ? " unspent knowledge point" : " unspent knowledge points";
            final String unspentPointsText = VaultBarOverlay.unspentKnowledgePoints + "";
            final int unspentPointsWidth = minecraft.font.width(unspentPointsText);
            final int unspentWidth = minecraft.font.width(unspentText);
            matrixStack.pushPose();
            if (VaultBarOverlay.unspentSkillPoints > 0) {
                matrixStack.translate(0.0, 12.0, 0.0);
            }
            minecraft.font.drawShadow(matrixStack, VaultBarOverlay.unspentKnowledgePoints + "", (float)(right - unspentWidth - unspentPointsWidth - gap), 18.0f, -12527695);
            minecraft.font.drawShadow(matrixStack, unspentText, (float)(right - unspentWidth - gap), 18.0f, -1);
            matrixStack.popPose();
            minecraft.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
        }
        matrixStack.popPose();
        VaultBarOverlay.expGainedAnimation.tick((int)(now - VaultBarOverlay.previousTick));
        VaultBarOverlay.previousTick = now;
        if (minecraft.gameMode == null || !minecraft.gameMode.hasExperience()) {
            return;
        }
        minecraft.getProfiler().push("vaultBar");
        minecraft.getTextureManager().bind(VaultBarOverlay.VAULT_HUD_SPRITE);
        RenderSystem.enableBlend();
        minecraft.gui.blit(matrixStack, midX + 9, bottom - 48, 1, 1, barWidth, 5);
        if (VaultBarOverlay.expGainedAnimation.getValue() != 0.0f) {
            GlStateManager._color4f(1.0f, 1.0f, 1.0f, VaultBarOverlay.expGainedAnimation.getValue());
            minecraft.gui.blit(matrixStack, midX + 8, bottom - 49, 62, 41, 84, 7);
            GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        minecraft.gui.blit(matrixStack, midX + 9, bottom - 48, 1, 7, (int)(barWidth * expPercentage), 5);
        if (VaultBarOverlay.expGainedAnimation.getValue() != 0.0f) {
            GlStateManager._color4f(1.0f, 1.0f, 1.0f, VaultBarOverlay.expGainedAnimation.getValue());
            minecraft.gui.blit(matrixStack, midX + 8, bottom - 49, 62, 49, (int)(barWidth * expPercentage), 7);
            GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        FontHelper.drawStringWithBorder(matrixStack, text, (float)textX, (float)textY, -6601, 3945472);
        minecraft.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
        minecraft.getProfiler().pop();
    }
    
    private static int potionOffsetY(final ClientPlayerEntity player) {
        final List<EffectInstance> effectInstances = player.getActiveEffects().stream().filter(EffectInstance::showIcon).collect(Collectors.toList());
        if (effectInstances.size() == 0) {
            return 0;
        }
        for (final EffectInstance effectInstance : effectInstances) {
            if (effectInstance.getEffect().getCategory() == EffectType.HARMFUL) {
                return 36;
            }
        }
        return 18;
    }
    
    static {
        VAULT_HUD_SPRITE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
        VaultBarOverlay.expGainedAnimation = new AnimationTwoPhased(0.0f, 1.0f, 0.0f, 500);
        VaultBarOverlay.previousTick = System.currentTimeMillis();
    }
}
