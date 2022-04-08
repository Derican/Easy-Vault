package iskallia.vault.client.gui.overlay.goal;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.Vault;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.client.gui.helper.LightmapHelper;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.vault.goal.ActiveRaidGoalData;
import iskallia.vault.client.vault.goal.VaultGoalData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class ActiveRaidOverlay extends BossBarOverlay {
    public static final ResourceLocation VAULT_HUD_RESOURCE = Vault.id("textures/gui/vault-hud.png");

    private final ActiveRaidGoalData data;

    public ActiveRaidOverlay(ActiveRaidGoalData data) {
        this.data = data;
    }

    @SubscribeEvent
    public static void onDrawPlayerlist(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.PLAYER_LIST) {
            return;
        }
        VaultGoalData data = VaultGoalData.CURRENT_DATA;
        if (!(data instanceof ActiveRaidGoalData)) {
            return;
        }
        event.setCanceled(true);
    }


    public boolean shouldDisplay() {
        return true;
    }


    public int drawOverlay(MatrixStack renderStack, float pTicks) {
        int offsetY = 5;
        offsetY = drawWaveDisplay(renderStack, pTicks, offsetY);
        offsetY = drawMobBar(renderStack, pTicks, offsetY);
        offsetY = drawModifierDisplay(renderStack, pTicks, offsetY);
        return offsetY;
    }

    private int drawWaveDisplay(MatrixStack renderStack, float pTicks, int offsetY) {
        if (this.data.getTotalWaves() <= 0) {
            return offsetY;
        }
        String waveDisplay = String.format("%s / %s", new Object[]{Integer.valueOf(this.data.getWave() + 1), Integer.valueOf(this.data.getTotalWaves())});
        String fullDisplay = waveDisplay;
        if (this.data.getTickWaveDelay() > 0) {
            fullDisplay = fullDisplay + " - " + UIHelper.formatTimeString(this.data.getTickWaveDelay());
        }

        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.font;
        int width = fr.width(waveDisplay);
        float midX = mc.getWindow().getGuiScaledWidth() / 2.0F;

        renderStack.pushPose();
        renderStack.translate((midX - width / 2.0F), offsetY, 0.0D);
        renderStack.scale(1.25F, 1.25F, 1.0F);
        FontHelper.drawStringWithBorder(renderStack, fullDisplay, 0.0F, 0.0F, 16777215, 0);
        buffer.endBatch();
        renderStack.popPose();

        return offsetY + 13;
    }

    private int drawMobBar(MatrixStack renderStack, float pTicks, int offsetY) {
        if (this.data.getTotalWaves() <= 0) {
            return offsetY;
        }
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bind(VAULT_HUD_RESOURCE);

        float killedPerc = this.data.getAliveMobs() / this.data.getTotalMobs();

        float midX = mc.getWindow().getGuiScaledWidth() / 2.0F;
        int width = 182;
        int mobWidth = (int) (width * killedPerc);
        int totalWidth = width - mobWidth;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        ScreenDrawHelper.drawQuad(buf -> {
            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(midX - width / 2.0F, offsetY).dim(mobWidth, 5.0F).texVanilla(0.0F, 168.0F, mobWidth, 5.0F).draw();


            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(midX - width / 2.0F, offsetY).dim(mobWidth, 5.0F).texVanilla(0.0F, 178.0F, mobWidth, 5.0F).draw();


            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(midX - width / 2.0F + mobWidth, offsetY).dim(totalWidth, 5.0F).texVanilla(mobWidth, 163.0F, totalWidth, 5.0F).draw();


            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(midX - width / 2.0F + mobWidth, offsetY).dim(totalWidth, 5.0F).texVanilla(mobWidth, 173.0F, totalWidth, 5.0F).draw();
        });


        RenderSystem.disableBlend();

        mc.getTextureManager().bind(PlayerContainer.BLOCK_ATLAS);
        return offsetY + 8;
    }

    private int drawModifierDisplay(MatrixStack renderStack, float pTicks, int offsetY) {
        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.font;
        int guiScale = mc.options.guiScale;

        boolean drawAdditionalInfo = false;
        List<ITextComponent> positives = this.data.getPositives();
        List<ITextComponent> negatives = this.data.getNegatives();
        if (!mc.options.keyPlayerList.isDown()) {
            drawAdditionalInfo = (positives.size() > 2 || negatives.size() > 2);
            positives = positives.subList(0, Math.min(positives.size(), 2));
            negatives = negatives.subList(0, Math.min(negatives.size(), 2));
        }

        float midX = mc.getWindow().getGuiScaledWidth() / 2.0F;
        float scale = (guiScale >= 4 || guiScale == 0) ? 0.7F : 1.0F;
        float height = 10.0F * scale;
        float maxHeight = Math.max(positives.size(), negatives.size()) * height;

        if (this.data.getRaidsCompleted() > 0) {
            renderStack.pushPose();
            renderStack.translate(midX, offsetY, 0.0D);
            renderStack.scale(scale, scale, 1.0F);

            String raid = (this.data.getRaidsCompleted() > 1) ? " Raids" : " Raid";

            IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent(this.data.getRaidsCompleted() + raid + " Completed")).withStyle(TextFormatting.GOLD);
            int width = fr.width((ITextProperties) iFormattableTextComponent);
            fr.drawInBatch((ITextComponent) iFormattableTextComponent, (-width / 2), 0.0F, -1, false, renderStack
                    .last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
            renderStack.popPose();

            offsetY = (int) (offsetY + height + 1.0F);
        }

        renderStack.pushPose();
        renderStack.translate((midX - 5.0F), offsetY, 0.0D);
        renderStack.scale(scale, scale, 1.0F);
        for (ITextComponent positive : positives) {
            int width = fr.width((ITextProperties) positive);
            fr.drawInBatch(positive, -width, 0.0F, -1, false, renderStack
                    .last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
            renderStack.translate(0.0D, 10.0D, 0.0D);
        }
        renderStack.popPose();

        renderStack.pushPose();
        renderStack.translate((midX + 5.0F), offsetY, 0.0D);
        renderStack.scale(scale, scale, 1.0F);
        for (ITextComponent negative : negatives) {
            fr.drawInBatch(negative, 0.0F, 0.0F, -1, false, renderStack
                    .last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
            renderStack.translate(0.0D, 10.0D, 0.0D);
        }
        renderStack.popPose();

        if (drawAdditionalInfo) {
            renderStack.pushPose();
            renderStack.translate(midX, (offsetY + maxHeight), 0.0D);
            renderStack.scale(scale, scale, 1.0F);

            KeyBinding listSetting = mc.options.keyPlayerList;

            IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("Hold ")).withStyle(TextFormatting.DARK_GRAY).append(listSetting.getTranslatedKeyMessage());
            int width = fr.width((ITextProperties) iFormattableTextComponent);
            fr.drawInBatch((ITextComponent) iFormattableTextComponent, (-width / 2), 0.0F, -1, false, renderStack
                    .last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
            renderStack.popPose();

            maxHeight += height;
        }

        buffer.endBatch();

        return MathHelper.ceil(offsetY + maxHeight);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\goal\ActiveRaidOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */