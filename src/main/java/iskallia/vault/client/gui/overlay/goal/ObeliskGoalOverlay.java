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
    public static final ResourceLocation VAULT_HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");

    @SubscribeEvent
    public static void onObeliskRender(RenderGameOverlayEvent.Post event) {
        VaultOverlayMessage.OverlayType type = ClientVaultRaidData.getOverlayType();
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR || type != VaultOverlayMessage.OverlayType.VAULT) {
            return;
        }


        VaultGoalData data = VaultGoalData.CURRENT_DATA;
        if (data == null) {
            return;
        }

        if (data instanceof VaultObeliskData) {
            MatrixStack renderStack = event.getMatrixStack();
            VaultObeliskData displayData = (VaultObeliskData) data;

            renderObeliskMessage(renderStack, displayData);
            renderObeliskIndicator(renderStack, displayData);
        }

        Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    private static void renderObeliskMessage(MatrixStack matrixStack, VaultObeliskData data) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.font;
        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());

        int bottom = mc.getWindow().getGuiScaledHeight();
        IReorderingProcessor bidiText = data.getMessage().getVisualOrderText();

        matrixStack.pushPose();
        matrixStack.translate(15.0D, (bottom - 34), 0.0D);

        fr.drawInBatch(bidiText, 0.0F, 0.0F, -1, true, matrixStack.last().pose(), (IRenderTypeBuffer) buffer, false, 0,
                LightmapHelper.getPackedFullbrightCoords());
        buffer.endBatch();
        matrixStack.popPose();
    }

    private static void renderObeliskIndicator(MatrixStack matrixStack, VaultObeliskData data) {
        int maxObelisks = data.getMaxObelisks();
        int touchedObelisks = data.getCurrentObelisks();
        if (maxObelisks <= 0) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        int untouchedObelisks = maxObelisks - touchedObelisks;
        int bottom = mc.getWindow().getGuiScaledHeight();

        float scale = 0.6F;
        int gap = 2;
        int margin = 2;

        mc.getTextureManager().bind(VAULT_HUD_RESOURCE);

        int iconWidth = 12;
        int iconHeight = 22;

        matrixStack.pushPose();
        matrixStack.translate(15.0D, (bottom - 34), 0.0D);
        matrixStack.translate(0.0D, -margin, 0.0D);
        matrixStack.translate(0.0D, (-scale * iconHeight), 0.0D);
        matrixStack.scale(scale, scale, scale);
        int i;
        for (i = 0; i < touchedObelisks; i++) {
            int u = 77, v = 84;
            AbstractGui.blit(matrixStack, 0, 0, u, v, iconWidth, iconHeight, 256, 256);


            matrixStack.translate((scale * gap + iconWidth), 0.0D, 0.0D);
        }

        for (i = 0; i < untouchedObelisks; i++) {
            int u = 64, v = 84;
            AbstractGui.blit(matrixStack, 0, 0, u, v, iconWidth, iconHeight, 256, 256);


            matrixStack.translate((scale * gap + iconWidth), 0.0D, 0.0D);
        }

        matrixStack.popPose();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\goal\ObeliskGoalOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */