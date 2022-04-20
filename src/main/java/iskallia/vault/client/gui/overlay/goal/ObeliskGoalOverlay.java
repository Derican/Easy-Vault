package iskallia.vault.client.gui.overlay.goal;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.ClientVaultRaidData;
import iskallia.vault.client.gui.helper.LightmapHelper;
import iskallia.vault.client.vault.goal.VaultGoalData;
import iskallia.vault.client.vault.goal.VaultObeliskData;
import iskallia.vault.network.message.VaultOverlayMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ObeliskGoalOverlay {
    public static final ResourceLocation VAULT_HUD_RESOURCE;

    @SubscribeEvent
    public static void onObeliskRender(final RenderGameOverlayEvent.Post event) {
        final VaultOverlayMessage.OverlayType type = ClientVaultRaidData.getOverlayType();
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR || type != VaultOverlayMessage.OverlayType.VAULT) {
            return;
        }
        final VaultGoalData data = VaultGoalData.CURRENT_DATA;
        if (data == null) {
            return;
        }
        if (data instanceof VaultObeliskData) {
            final MatrixStack renderStack = event.getMatrixStack();
            final VaultObeliskData displayData = (VaultObeliskData) data;
            renderObeliskMessage(renderStack, displayData);
            renderObeliskIndicator(renderStack, displayData);
        }
        Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    private static void renderObeliskMessage(final MatrixStack matrixStack, final VaultObeliskData data) {
        final Minecraft mc = Minecraft.getInstance();
        final FontRenderer fr = mc.font;
        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        final int bottom = mc.getWindow().getGuiScaledHeight();
        final IReorderingProcessor bidiText = data.getMessage().getVisualOrderText();
        matrixStack.pushPose();
        matrixStack.translate(15.0, (double) (bottom - 34), 0.0);
        fr.drawInBatch(bidiText, 0.0f, 0.0f, -1, true, matrixStack.last().pose(), (IRenderTypeBuffer) buffer, false, 0, LightmapHelper.getPackedFullbrightCoords());
        buffer.endBatch();
        matrixStack.popPose();
    }

    private static void renderObeliskIndicator(final MatrixStack matrixStack, final VaultObeliskData data) {
        final int maxObelisks = data.getMaxObelisks();
        final int touchedObelisks = data.getCurrentObelisks();
        if (maxObelisks <= 0) {
            return;
        }
        final Minecraft mc = Minecraft.getInstance();
        final int untouchedObelisks = maxObelisks - touchedObelisks;
        final int bottom = mc.getWindow().getGuiScaledHeight();
        final float scale = 0.6f;
        final int gap = 2;
        final int margin = 2;
        mc.getTextureManager().bind(ObeliskGoalOverlay.VAULT_HUD_RESOURCE);
        final int iconWidth = 12;
        final int iconHeight = 22;
        matrixStack.pushPose();
        matrixStack.translate(15.0, (double) (bottom - 34), 0.0);
        matrixStack.translate(0.0, (double) (-margin), 0.0);
        matrixStack.translate(0.0, (double) (-scale * iconHeight), 0.0);
        matrixStack.scale(scale, scale, scale);
        for (int i = 0; i < touchedObelisks; ++i) {
            final int u = 77;
            final int v = 84;
            AbstractGui.blit(matrixStack, 0, 0, (float) u, (float) v, iconWidth, iconHeight, 256, 256);
            matrixStack.translate((double) (scale * gap + iconWidth), 0.0, 0.0);
        }
        for (int i = 0; i < untouchedObelisks; ++i) {
            final int u = 64;
            final int v = 84;
            AbstractGui.blit(matrixStack, 0, 0, (float) u, (float) v, iconWidth, iconHeight, 256, 256);
            matrixStack.translate((double) (scale * gap + iconWidth), 0.0, 0.0);
        }
        matrixStack.popPose();
    }

    static {
        VAULT_HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
    }
}
