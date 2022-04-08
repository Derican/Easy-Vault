package iskallia.vault.client.gui.overlay.goal;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.ClientVaultRaidData;
import iskallia.vault.client.gui.helper.LightmapHelper;
import iskallia.vault.client.vault.goal.AncientGoalData;
import iskallia.vault.client.vault.goal.VaultGoalData;
import iskallia.vault.network.message.VaultOverlayMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;


@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = {Dist.CLIENT}, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AncientGoalOverlay {
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

        if (data instanceof AncientGoalData) {
            MatrixStack renderStack = event.getMatrixStack();
            AncientGoalData displayData = (AncientGoalData) data;

            renderAncientsMessage(renderStack, displayData);
            renderAncientIndicator(renderStack, displayData);
        }

        Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    private static void renderAncientsMessage(MatrixStack matrixStack, AncientGoalData data) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.font;
        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());

        int bottom = mc.getWindow().getGuiScaledHeight();
        int offsetY = 54;

        List<IReorderingProcessor> msg = new ArrayList<>();
        if (data.getTotalAncients() <= 0) {
            msg.add((new StringTextComponent("Hunt and escape")).withStyle(TextFormatting.DARK_AQUA).withStyle(TextFormatting.BOLD).getVisualOrderText());
            msg.add((new StringTextComponent("the Vault!")).withStyle(TextFormatting.DARK_AQUA).withStyle(TextFormatting.BOLD).getVisualOrderText());
            offsetY = 24;
        } else {
            String eternalPart = (data.getTotalAncients() > 1) ? "eternals" : "eternal";
            msg.add((new StringTextComponent("Find your " + eternalPart)).withStyle(TextFormatting.DARK_AQUA).withStyle(TextFormatting.BOLD).getVisualOrderText());
            msg.add((new StringTextComponent("and escape the Vault!")).withStyle(TextFormatting.DARK_AQUA).withStyle(TextFormatting.BOLD).getVisualOrderText());
        }

        matrixStack.pushPose();
        matrixStack.translate(12.0D, (bottom - offsetY - msg.size() * 10), 0.0D);

        for (int i = 0; i < msg.size(); i++) {
            IReorderingProcessor txt = msg.get(i);
            fr.drawInBatch(txt, 0.0F, (i * 10), -1, true, matrixStack.last().pose(), (IRenderTypeBuffer) buffer, false, 0,
                    LightmapHelper.getPackedFullbrightCoords());
        }

        buffer.endBatch();
        matrixStack.popPose();
    }

    private static void renderAncientIndicator(MatrixStack matrixStack, AncientGoalData data) {
        int totalAncients = data.getTotalAncients();
        int foundAncients = data.getFoundAncients();
        if (totalAncients <= 0) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        int untouchedObelisks = totalAncients - foundAncients;
        int bottom = mc.getWindow().getGuiScaledHeight();

        float scale = 1.0F;
        int gap = 2;
        int margin = 2;

        mc.getTextureManager().bind(VAULT_HUD_RESOURCE);

        int iconWidth = 15;
        int iconHeight = 27;

        matrixStack.pushPose();
        matrixStack.translate(12.0D, (bottom - 24), 0.0D);
        matrixStack.translate(0.0D, -margin, 0.0D);
        matrixStack.translate(0.0D, (-scale * iconHeight), 0.0D);
        matrixStack.scale(scale, scale, scale);
        int i;
        for (i = 0; i < foundAncients; i++) {
            int u = 81, v = 109;
            AbstractGui.blit(matrixStack, 0, 0, u, v, iconWidth, iconHeight, 256, 256);


            matrixStack.translate((scale * gap + iconWidth), 0.0D, 0.0D);
        }

        for (i = 0; i < untouchedObelisks; i++) {
            int u = 64, v = 109;
            AbstractGui.blit(matrixStack, 0, 0, u, v, iconWidth, iconHeight, 256, 256);


            matrixStack.translate((scale * gap + iconWidth), 0.0D, 0.0D);
        }

        matrixStack.popPose();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\goal\AncientGoalOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */