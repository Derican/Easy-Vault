package iskallia.vault.client.gui.overlay.goal;

import net.minecraft.client.renderer.BufferBuilder;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.client.gui.FontRenderer;
import iskallia.vault.client.gui.helper.LightmapHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.Minecraft;
import iskallia.vault.client.vault.goal.FinalArchitectGoalData;
import iskallia.vault.client.vault.goal.VaultObeliskData;
import iskallia.vault.client.vault.goal.VaultGoalData;
import iskallia.vault.network.message.VaultOverlayMessage;
import iskallia.vault.client.ClientVaultRaidData;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ObeliskGoalOverlay {
    public static final ResourceLocation VAULT_HUD_RESOURCE;
    private static final ResourceLocation ARCHITECT_HUD;

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
            renderObeliskMessage(renderStack, displayData.getMessage());
            renderObeliskIndicator(renderStack, displayData.getCurrentObelisks(), displayData.getMaxObelisks());
        }
        if (data instanceof FinalArchitectGoalData) {
            final MatrixStack renderStack = event.getMatrixStack();
            final FinalArchitectGoalData displayData2 = (FinalArchitectGoalData) data;
            renderStack.pushPose();
            renderStack.translate(-12.0, -24.0, 0.0);
            renderObeliskMessage(renderStack, displayData2.getMessage());
            renderStack.translate(0.0, 24.0, 0.0);
            renderObeliskIndicator(renderStack, displayData2.getKilledBosses(), displayData2.getTotalKilledBossesNeeded());
            renderKnowledgeGatherIndicator(renderStack, (float) displayData2.getKnowledge(), (float) displayData2.getTotalKnowledgeNeeded());
            renderStack.popPose();
        }
        Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    private static void renderObeliskMessage(final MatrixStack matrixStack, final ITextComponent message) {
        final Minecraft mc = Minecraft.getInstance();
        final FontRenderer fr = mc.font;
        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        final int bottom = mc.getWindow().getGuiScaledHeight();
        final IReorderingProcessor bidiText = message.getVisualOrderText();
        matrixStack.pushPose();
        matrixStack.translate(15.0, (double) (bottom - 34), 0.0);
        fr.drawInBatch(bidiText, 0.0f, 0.0f, -1, true, matrixStack.last().pose(), (IRenderTypeBuffer) buffer, false, 0, LightmapHelper.getPackedFullbrightCoords());
        buffer.endBatch();
        matrixStack.popPose();
    }

    private static void renderKnowledgeGatherIndicator(final MatrixStack renderStack, final float knowledge, final float totalKnowledgeNeeded) {
        final Minecraft mc = Minecraft.getInstance();
        final int bottom = mc.getWindow().getGuiScaledHeight();
        final int offsetY = 139;
        final int width = 80;
        final int height = 10;
        final float perc = width * (knowledge / totalKnowledgeNeeded);
        mc.getTextureManager().bind(ObeliskGoalOverlay.ARCHITECT_HUD);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        ScreenDrawHelper.drawQuad(buf -> {
            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(15.0f, (float) (bottom - 34)).dim(perc, (float) height).texVanilla(0.0f, (float) (offsetY + height), perc, (float) height).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(15.0f, (float) (bottom - 34)).dim((float) width, (float) height).texVanilla(0.0f, (float) offsetY, (float) width, (float) height).draw();
        });
    }

    private static void renderObeliskIndicator(final MatrixStack matrixStack, final int currentObelisks, final int maxObelisks) {
        if (maxObelisks <= 0) {
            return;
        }
        final Minecraft mc = Minecraft.getInstance();
        final int untouchedObelisks = maxObelisks - currentObelisks;
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
        for (int i = 0; i < currentObelisks; ++i) {
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
        ARCHITECT_HUD = new ResourceLocation("the_vault", "textures/gui/architect_event_bar.png");
    }
}
