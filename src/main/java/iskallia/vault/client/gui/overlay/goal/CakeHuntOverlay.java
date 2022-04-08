package iskallia.vault.client.gui.overlay.goal;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.Vault;
import iskallia.vault.client.ClientVaultRaidData;
import iskallia.vault.client.gui.helper.LightmapHelper;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import iskallia.vault.client.vault.goal.CakeHuntData;
import iskallia.vault.client.vault.goal.VaultGoalData;
import iskallia.vault.network.message.VaultOverlayMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = {Dist.CLIENT}, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CakeHuntOverlay {
    private static final ResourceLocation ARCHITECT_HUD = Vault.id("textures/gui/architect_event_bar.png");

    @SubscribeEvent
    public static void onArchitectBuild(RenderGameOverlayEvent.Post event) {
        VaultOverlayMessage.OverlayType type = ClientVaultRaidData.getOverlayType();
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR || type != VaultOverlayMessage.OverlayType.VAULT) {
            return;
        }


        Minecraft mc = Minecraft.getInstance();
        VaultGoalData data = VaultGoalData.CURRENT_DATA;
        if (data instanceof CakeHuntData) {
            CakeHuntData displayData = (CakeHuntData) data;
            MatrixStack renderStack = event.getMatrixStack();

            IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
            FontRenderer fr = mc.font;
            int bottom = mc.getWindow().getGuiScaledHeight();
            float part = displayData.getCompletePercent();

            IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("Find the cakes!")).withStyle(TextFormatting.AQUA).withStyle(TextFormatting.BOLD);
            fr.drawInBatch(iFormattableTextComponent.getVisualOrderText(), 8.0F, (bottom - 54), -1, true, renderStack.last().pose(), (IRenderTypeBuffer) buffer, false, 0,
                    LightmapHelper.getPackedFullbrightCoords());
            iFormattableTextComponent = (new StringTextComponent(displayData.getFoundCakes() + " / " + displayData.getTotalCakes())).withStyle(TextFormatting.AQUA).withStyle(TextFormatting.BOLD);
            fr.drawInBatch(iFormattableTextComponent.getVisualOrderText(), 12.0F, (bottom - 44), -1, true, renderStack.last().pose(), (IRenderTypeBuffer) buffer, false, 0,
                    LightmapHelper.getPackedFullbrightCoords());
            buffer.endBatch();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            mc.getTextureManager().bind(ARCHITECT_HUD);
            ScreenDrawHelper.drawQuad(buf -> {
                ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(15.0F, (bottom - 31)).dim(54.0F, 7.0F).texVanilla(0.0F, 105.0F, 54.0F, 7.0F).draw();


                ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(16.0F, (bottom - 30)).dim(52.0F * part, 5.0F).texVanilla(0.0F, 113.0F, 52.0F * part, 5.0F).draw();
            });
        }


        mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\goal\CakeHuntOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */