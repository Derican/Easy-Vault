package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.ClientActiveEternalData;
import iskallia.vault.client.ClientPartyData;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.client.util.ShaderUtil;
import iskallia.vault.config.EternalAuraConfig;
import iskallia.vault.entity.eternal.ActiveEternalData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.VaultPartyData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.ARBShaderObjects;

import java.util.Set;
import java.util.UUID;


@OnlyIn(Dist.CLIENT)
public class VaultPartyOverlay {
    public static final ResourceLocation VAULT_HUD_SPRITE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");

    @SubscribeEvent
    public static void renderSidebarHUD(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null) {
            return;
        }

        MatrixStack matrixStack = event.getMatrixStack();
        int bottom = mc.getWindow().getGuiScaledHeight();
        int right = mc.getWindow().getGuiScaledWidth();

        float offsetY = Math.max(bottom / 3.0F, 45.0F);

        offsetY += renderPartyHUD(matrixStack, offsetY, right);
        offsetY += renderEternalHUD(matrixStack, offsetY, right);

        mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    private static int renderEternalHUD(MatrixStack matrixStack, float offsetY, int right) {
        Set<ActiveEternalData.ActiveEternal> eternals = ClientActiveEternalData.getActiveEternals();
        if (eternals.isEmpty()) {
            return 0;
        }
        int height = 0;
        matrixStack.pushPose();
        matrixStack.translate((right - 5), offsetY, 0.0D);

        matrixStack.pushPose();
        matrixStack.scale(0.8F, 0.8F, 1.0F);
        IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("Eternals")).withStyle(TextFormatting.GOLD);
        FontHelper.drawTextComponent(matrixStack, (ITextComponent) iFormattableTextComponent, true);
        matrixStack.popPose();

        height += 8;
        matrixStack.translate(0.0D, 8.0D, -50.0D);
        matrixStack.scale(0.7F, 0.7F, 1.0F);

        for (ActiveEternalData.ActiveEternal eternal : eternals) {
            int eternalHeight = renderEternalSection(matrixStack, eternal) + 4;
            matrixStack.translate(0.0D, eternalHeight, 0.0D);
            height = (int) (height + eternalHeight * 0.7F);
        }

        matrixStack.popPose();
        return height + 6;
    }

    private static int renderEternalSection(MatrixStack matrixStack, ActiveEternalData.ActiveEternal eternal) {
        int textSize = 8;
        int headSize = 16;
        int gap = 2;

        boolean dead = (eternal.getHealth() <= 0.0F);
        ResourceLocation skin = eternal.getSkin().getLocationSkin();
        StringTextComponent stringTextComponent = new StringTextComponent("");

        matrixStack.pushPose();

        matrixStack.translate(-headSize, 0.0D, 0.0D);
        render2DHead(matrixStack, skin, headSize, dead);
        matrixStack.translate(-gap, ((headSize - textSize) / 2.0F), 0.0D);

        if (dead) {
            stringTextComponent.append((ITextComponent) (new StringTextComponent("Unalived")).withStyle(TextFormatting.RED));
        } else {
            int heartSize = 9;
            int heartU = 86, heartV = 2;
            matrixStack.translate(-heartSize, 0.0D, 0.0D);
            Minecraft.getInstance().getTextureManager().bind(VAULT_HUD_SPRITE);
            AbstractGui.blit(matrixStack, 0, 0, heartU, heartV, heartSize, heartSize, 256, 256);
            matrixStack.translate(-gap, 0.0D, 0.0D);

            stringTextComponent.append((ITextComponent) (new StringTextComponent((int) eternal.getHealth() + "x")).withStyle(TextFormatting.WHITE));
        }
        int width = FontHelper.drawTextComponent(matrixStack, (ITextComponent) stringTextComponent, true);
        if (eternal.getAbilityName() != null) {
            EternalAuraConfig.AuraConfig cfg = ModConfigs.ETERNAL_AURAS.getByName(eternal.getAbilityName());
            if (cfg != null) {
                matrixStack.translate(-(width + 18), -4.0D, 0.0D);
                Minecraft.getInstance().getTextureManager().bind(new ResourceLocation(cfg.getIconPath()));
                AbstractGui.blit(matrixStack, 0, 0, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        matrixStack.popPose();
        return headSize;
    }

    private static int renderPartyHUD(MatrixStack matrixStack, float offsetY, int right) {
        ClientPlayerEntity player = (Minecraft.getInstance()).player;
        VaultPartyData.Party thisParty = ClientPartyData.getParty(player.getUUID());
        if (thisParty == null) {
            return 0;
        }
        int height = 0;

        matrixStack.pushPose();
        matrixStack.translate((right - 5), offsetY, 0.0D);

        matrixStack.pushPose();
        matrixStack.scale(0.8F, 0.8F, 1.0F);
        IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("Vault Party")).withStyle(TextFormatting.GREEN);
        FontHelper.drawTextComponent(matrixStack, (ITextComponent) iFormattableTextComponent, true);
        matrixStack.popPose();

        height += 8;
        matrixStack.translate(0.0D, 8.0D, -50.0D);
        matrixStack.scale(0.7F, 0.7F, 1.0F);

        ClientPlayNetHandler netHandler = Minecraft.getInstance().getConnection();
        if (netHandler != null) {
            for (UUID uuid : thisParty.getMembers()) {
                NetworkPlayerInfo info = netHandler.getPlayerInfo(uuid);
                int playerHeight = renderPartyPlayerSection(matrixStack, thisParty, uuid, info) + 4;
                matrixStack.translate(0.0D, playerHeight, 0.0D);
                height = (int) (height + playerHeight * 0.7F);
            }
        }
        matrixStack.popPose();

        return height + 6;
    }

    private static int renderPartyPlayerSection(MatrixStack matrixStack, VaultPartyData.Party party, UUID playerUUID, NetworkPlayerInfo info) {
        int textSize = 8;
        int headSize = 16;
        int gap = 2;

        boolean offline = (info == null);
        ClientPartyData.PartyMember member = offline ? null : ClientPartyData.getCachedMember(info.getProfile().getId());
        ResourceLocation skin = offline ? DefaultPlayerSkin.getDefaultSkin() : info.getSkinLocation();

        String prefix = playerUUID.equals(party.getLeader()) ? "⭐ " : "";
        IFormattableTextComponent txt = (new StringTextComponent(prefix)).withStyle(TextFormatting.GOLD);

        matrixStack.pushPose();

        matrixStack.translate(-headSize, 0.0D, 0.0D);
        render2DHead(matrixStack, skin, headSize, offline);
        matrixStack.translate(-gap, ((headSize - textSize) / 2.0F), 0.0D);

        if (offline) {
            txt.append((ITextComponent) (new StringTextComponent(prefix + "OFFLINE")).withStyle(TextFormatting.GRAY));
        } else {
            int heartSize = 9;
            int heartU = 86, heartV = 2;
            matrixStack.translate(-heartSize, 0.0D, 0.0D);
            Minecraft.getInstance().getTextureManager().bind(VAULT_HUD_SPRITE);
            ClientPartyData.PartyMember.Status status = (member == null) ? ClientPartyData.PartyMember.Status.NORMAL : member.status;
            AbstractGui.blit(matrixStack, 0, 0, (heartU +
                    getPartyPlayerStatusOffset(status)), heartV, heartSize, heartSize, 256, 256);


            matrixStack.translate(-gap, 0.0D, 0.0D);
            txt.append((ITextComponent) (new StringTextComponent((member == null) ? "-" : ((int) member.healthPts + "x"))).withStyle(TextFormatting.WHITE));
        }
        FontHelper.drawTextComponent(matrixStack, (ITextComponent) txt, true);

        matrixStack.popPose();
        return headSize;
    }

    private static int getPartyPlayerStatusOffset(ClientPartyData.PartyMember.Status status) {
        switch (status) {
            case POISONED:
                return 10;
            case WITHERED:
                return 20;
            case DEAD:
                return 30;
        }
        return 0;
    }


    public static void render2DHead(MatrixStack matrixStack, ResourceLocation skin, int size, boolean grayscaled) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(skin);
        int u1 = 8, v1 = 8;
        int u2 = 40, v2 = 8;
        int w = 8, h = 8;
        if (grayscaled) {
            ShaderUtil.useShader(ShaderUtil.GRAYSCALE_SHADER, () -> {
                int grayScaleFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "grayFactor");
                ARBShaderObjects.glUniform1fARB(grayScaleFactor, 0.0F);
            });
        }
        AbstractGui.blit(matrixStack, 0, 0, size, size, u1, v1, w, h, 64, 64);

        AbstractGui.blit(matrixStack, 0, 0, size, size, u2, v2, w, h, 64, 64);

        if (grayscaled)
            ShaderUtil.releaseShader();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\VaultPartyOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */