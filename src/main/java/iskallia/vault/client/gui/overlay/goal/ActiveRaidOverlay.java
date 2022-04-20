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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class ActiveRaidOverlay extends BossBarOverlay {
    public static final ResourceLocation VAULT_HUD_RESOURCE;
    private final ActiveRaidGoalData data;

    public ActiveRaidOverlay(final ActiveRaidGoalData data) {
        this.data = data;
    }

    @SubscribeEvent
    public static void onDrawPlayerlist(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.PLAYER_LIST) {
            return;
        }
        final VaultGoalData data = VaultGoalData.CURRENT_DATA;
        if (!(data instanceof ActiveRaidGoalData)) {
            return;
        }
        event.setCanceled(true);
    }

    @Override
    public boolean shouldDisplay() {
        return true;
    }

    @Override
    public int drawOverlay(final MatrixStack renderStack, final float pTicks) {
        int offsetY = 5;
        offsetY = this.drawWaveDisplay(renderStack, pTicks, offsetY);
        offsetY = this.drawMobBar(renderStack, pTicks, offsetY);
        offsetY = this.drawModifierDisplay(renderStack, pTicks, offsetY);
        return offsetY;
    }

    private int drawWaveDisplay(final MatrixStack renderStack, final float pTicks, final int offsetY) {
        if (this.data.getTotalWaves() <= 0) {
            return offsetY;
        }
        String fullDisplay;
        final String waveDisplay = fullDisplay = String.format("%s / %s", this.data.getWave() + 1, this.data.getTotalWaves());
        if (this.data.getTickWaveDelay() > 0) {
            fullDisplay = fullDisplay + " - " + UIHelper.formatTimeString(this.data.getTickWaveDelay());
        }
        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        final Minecraft mc = Minecraft.getInstance();
        final FontRenderer fr = mc.font;
        final int width = fr.width(waveDisplay);
        final float midX = mc.getWindow().getGuiScaledWidth() / 2.0f;
        renderStack.pushPose();
        renderStack.translate((double) (midX - width / 2.0f), (double) offsetY, 0.0);
        renderStack.scale(1.25f, 1.25f, 1.0f);
        FontHelper.drawStringWithBorder(renderStack, fullDisplay, 0.0f, 0.0f, 16777215, 0);
        buffer.endBatch();
        renderStack.popPose();
        return offsetY + 13;
    }

    private int drawMobBar(final MatrixStack renderStack, final float pTicks, final int offsetY) {
        if (this.data.getTotalWaves() <= 0) {
            return offsetY;
        }
        final Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bind(ActiveRaidOverlay.VAULT_HUD_RESOURCE);
        final float killedPerc = this.data.getAliveMobs() / (float) this.data.getTotalMobs();
        final float midX = mc.getWindow().getGuiScaledWidth() / 2.0f;
        final int width = 182;
        final int mobWidth = (int) (width * killedPerc);
        final int totalWidth = width - mobWidth;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        ScreenDrawHelper.drawQuad(buf -> {
            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(midX - width / 2.0f, (float) offsetY).dim((float) mobWidth, 5.0f).texVanilla(0.0f, 168.0f, (float) mobWidth, 5.0f).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(midX - width / 2.0f, (float) offsetY).dim((float) mobWidth, 5.0f).texVanilla(0.0f, 178.0f, (float) mobWidth, 5.0f).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(midX - width / 2.0f + mobWidth, (float) offsetY).dim((float) totalWidth, 5.0f).texVanilla((float) mobWidth, 163.0f, (float) totalWidth, 5.0f).draw();
            ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack).at(midX - width / 2.0f + mobWidth, (float) offsetY).dim((float) totalWidth, 5.0f).texVanilla((float) mobWidth, 173.0f, (float) totalWidth, 5.0f).draw();
            return;
        });
        RenderSystem.disableBlend();
        mc.getTextureManager().bind(PlayerContainer.BLOCK_ATLAS);
        return offsetY + 8;
    }

    private int drawModifierDisplay(final MatrixStack renderStack, final float pTicks, int offsetY) {
        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        final Minecraft mc = Minecraft.getInstance();
        final FontRenderer fr = mc.font;
        final int guiScale = mc.options.guiScale;
        boolean drawAdditionalInfo = false;
        List<ITextComponent> positives = this.data.getPositives();
        List<ITextComponent> negatives = this.data.getNegatives();
        if (!mc.options.keyPlayerList.isDown()) {
            drawAdditionalInfo = (positives.size() > 2 || negatives.size() > 2);
            positives = positives.subList(0, Math.min(positives.size(), 2));
            negatives = negatives.subList(0, Math.min(negatives.size(), 2));
        }
        final float midX = mc.getWindow().getGuiScaledWidth() / 2.0f;
        final float scale = (guiScale >= 4 || guiScale == 0) ? 0.7f : 1.0f;
        final float height = 10.0f * scale;
        float maxHeight = Math.max(positives.size(), negatives.size()) * height;
        if (this.data.getRaidsCompleted() > 0) {
            renderStack.pushPose();
            renderStack.translate((double) midX, (double) offsetY, 0.0);
            renderStack.scale(scale, scale, 1.0f);
            final String raid = (this.data.getRaidsCompleted() > 1) ? " Raids" : " Raid";
            final ITextComponent info = (ITextComponent) new StringTextComponent(this.data.getRaidsCompleted() + raid + " Completed").withStyle(TextFormatting.GOLD);
            final int width = fr.width((ITextProperties) info);
            fr.drawInBatch(info, (float) (-width / 2), 0.0f, -1, false, renderStack.last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
            renderStack.popPose();
            offsetY += (int) (height + 1.0f);
        }
        renderStack.pushPose();
        renderStack.translate((double) (midX - 5.0f), (double) offsetY, 0.0);
        renderStack.scale(scale, scale, 1.0f);
        for (final ITextComponent positive : positives) {
            final int width = fr.width((ITextProperties) positive);
            fr.drawInBatch(positive, (float) (-width), 0.0f, -1, false, renderStack.last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
            renderStack.translate(0.0, 10.0, 0.0);
        }
        renderStack.popPose();
        renderStack.pushPose();
        renderStack.translate((double) (midX + 5.0f), (double) offsetY, 0.0);
        renderStack.scale(scale, scale, 1.0f);
        for (final ITextComponent negative : negatives) {
            fr.drawInBatch(negative, 0.0f, 0.0f, -1, false, renderStack.last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
            renderStack.translate(0.0, 10.0, 0.0);
        }
        renderStack.popPose();
        if (drawAdditionalInfo) {
            renderStack.pushPose();
            renderStack.translate((double) midX, (double) (offsetY + maxHeight), 0.0);
            renderStack.scale(scale, scale, 1.0f);
            final KeyBinding listSetting = mc.options.keyPlayerList;
            final ITextComponent info = (ITextComponent) new StringTextComponent("Hold ").withStyle(TextFormatting.DARK_GRAY).append(listSetting.getTranslatedKeyMessage());
            final int width = fr.width((ITextProperties) info);
            fr.drawInBatch(info, (float) (-width / 2), 0.0f, -1, false, renderStack.last().pose(), (IRenderTypeBuffer) buffer, true, 0, LightmapHelper.getPackedFullbrightCoords());
            renderStack.popPose();
            maxHeight += height;
        }
        buffer.endBatch();
        return MathHelper.ceil(offsetY + maxHeight);
    }

    static {
        VAULT_HUD_RESOURCE = Vault.id("textures/gui/vault-hud.png");
    }
}
