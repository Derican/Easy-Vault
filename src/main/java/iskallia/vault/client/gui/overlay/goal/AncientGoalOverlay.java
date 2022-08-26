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

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = {Dist.CLIENT}, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AncientGoalOverlay {
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
        if (data instanceof AncientGoalData) {
            final MatrixStack renderStack = event.getMatrixStack();
            final AncientGoalData displayData = (AncientGoalData) data;
            renderAncientsMessage(renderStack, displayData);
            renderAncientIndicator(renderStack, displayData);
        }
        Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    private static void renderAncientsMessage(final MatrixStack matrixStack, final AncientGoalData data) {
        final Minecraft mc = Minecraft.getInstance();
        final FontRenderer fr = mc.font;
        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        final int bottom = mc.getWindow().getGuiScaledHeight();
        int offsetY = 54;
        final List<IReorderingProcessor> msg = new ArrayList<IReorderingProcessor>();
        if (data.getTotalAncients() <= 0) {
            msg.add(new StringTextComponent("Hunt and escape").withStyle(TextFormatting.DARK_AQUA).withStyle(TextFormatting.BOLD).getVisualOrderText());
            msg.add(new StringTextComponent("the Vault!").withStyle(TextFormatting.DARK_AQUA).withStyle(TextFormatting.BOLD).getVisualOrderText());
            offsetY = 24;
        } else {
            final String eternalPart = (data.getTotalAncients() > 1) ? "eternals" : "eternal";
            msg.add(new StringTextComponent("Find your " + eternalPart).withStyle(TextFormatting.DARK_AQUA).withStyle(TextFormatting.BOLD).getVisualOrderText());
            msg.add(new StringTextComponent("and escape the Vault!").withStyle(TextFormatting.DARK_AQUA).withStyle(TextFormatting.BOLD).getVisualOrderText());
        }
        matrixStack.pushPose();
        matrixStack.translate(12.0, bottom - offsetY - msg.size() * 10, 0.0);
        for (int i = 0; i < msg.size(); ++i) {
            final IReorderingProcessor txt = msg.get(i);
            fr.drawInBatch(txt, 0.0f, (float) (i * 10), -1, true, matrixStack.last().pose(), buffer, false, 0, LightmapHelper.getPackedFullbrightCoords());
        }
        buffer.endBatch();
        matrixStack.popPose();
    }

    private static void renderAncientIndicator(final MatrixStack matrixStack, final AncientGoalData data) {
        final int totalAncients = data.getTotalAncients();
        final int foundAncients = data.getFoundAncients();
        if (totalAncients <= 0) {
            return;
        }
        final Minecraft mc = Minecraft.getInstance();
        final int untouchedObelisks = totalAncients - foundAncients;
        final int bottom = mc.getWindow().getGuiScaledHeight();
        final float scale = 1.0f;
        final int gap = 2;
        final int margin = 2;
        mc.getTextureManager().bind(AncientGoalOverlay.VAULT_HUD_RESOURCE);
        final int iconWidth = 15;
        final int iconHeight = 27;
        matrixStack.pushPose();
        matrixStack.translate(12.0, bottom - 24, 0.0);
        matrixStack.translate(0.0, -margin, 0.0);
        matrixStack.translate(0.0, -scale * iconHeight, 0.0);
        matrixStack.scale(scale, scale, scale);
        for (int i = 0; i < foundAncients; ++i) {
            final int u = 81;
            final int v = 109;
            AbstractGui.blit(matrixStack, 0, 0, (float) u, (float) v, iconWidth, iconHeight, 256, 256);
            matrixStack.translate(scale * gap + iconWidth, 0.0, 0.0);
        }
        for (int i = 0; i < untouchedObelisks; ++i) {
            final int u = 64;
            final int v = 109;
            AbstractGui.blit(matrixStack, 0, 0, (float) u, (float) v, iconWidth, iconHeight, 256, 256);
            matrixStack.translate(scale * gap + iconWidth, 0.0, 0.0);
        }
        matrixStack.popPose();
    }

    static {
        VAULT_HUD_RESOURCE = new ResourceLocation("the_vault", "textures/gui/vault-hud.png");
    }
}
