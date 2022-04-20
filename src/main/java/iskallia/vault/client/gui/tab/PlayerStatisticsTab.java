package iskallia.vault.client.gui.tab;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.client.ClientStatisticsData;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerStatisticsTab extends SkillTab {
    public PlayerStatisticsTab(final SkillTreeScreen parentScreen) {
        super(parentScreen, (ITextComponent) new StringTextComponent("Statistics Tab"));
        this.setScrollable(false);
    }

    @Override
    public String getTabName() {
        return "Statistics";
    }

    @Override
    public void refresh() {
    }

    @Override
    public List<Runnable> renderTab(final Rectangle containerBounds, final MatrixStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        this.renderTabBackground(renderStack, containerBounds);
        this.renderBookBackground(containerBounds, renderStack);
        final int pxOffsetX = 38;
        final int pxOffsetY = 16;
        final float pxWidth = containerBounds.width / 192.0f;
        final float pxHeight = containerBounds.height / 192.0f;
        final int offsetX = containerBounds.x + Math.round(pxWidth * pxOffsetX);
        final int offsetY = containerBounds.y + Math.round(pxHeight * pxOffsetY);
        final Rectangle bookCt = new Rectangle(offsetX, offsetY, Math.round(108.0f * pxWidth), Math.round(104.0f * pxWidth));
        renderStack.pushPose();
        renderStack.translate((double) (offsetX + 5), (double) (offsetY + 5), 0.0);
        this.renderPlayerAttributes(bookCt, renderStack, mouseX, mouseY, pTicks);
        renderStack.popPose();
        return Collections.emptyList();
    }

    private void renderPlayerAttributes(final Rectangle containerBounds, final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final FontRenderer fr = Minecraft.getInstance().font;
        final CompoundNBT vaultStats = ClientStatisticsData.getSerializedVaultStats();
        final List<Tuple<ITextComponent, Tuple<ITextComponent, Integer>>> statDisplay = new ArrayList<Tuple<ITextComponent, Tuple<ITextComponent, Integer>>>();
        final int numberOffset = this.buildVaultStatisticsDisplay(vaultStats, statDisplay);
        int maxLength = 0;
        final StringTextComponent text = new StringTextComponent("");
        for (final Tuple<ITextComponent, Tuple<ITextComponent, Integer>> statTpl : statDisplay) {
            text.append((ITextComponent) statTpl.getA()).append("\n");
            final int length = fr.width((ITextProperties) statTpl.getA());
            if (length > maxLength) {
                maxLength = length;
            }
        }
        maxLength += 5;
        maxLength += numberOffset;
        matrixStack.pushPose();
        final int yOffset = this.renderFastestVaultDisplay(matrixStack, vaultStats, maxLength);
        matrixStack.translate(0.0, (double) yOffset, 0.0);
        UIHelper.renderWrappedText(matrixStack, (ITextComponent) text, containerBounds.width, 0);
        matrixStack.translate((double) maxLength, 0.0, 0.0);
        for (final Tuple<ITextComponent, Tuple<ITextComponent, Integer>> statTpl2 : statDisplay) {
            final Tuple<ITextComponent, Integer> valueDisplayTpl = (Tuple<ITextComponent, Integer>) statTpl2.getB();
            matrixStack.pushPose();
            matrixStack.translate((double) (-(int) valueDisplayTpl.getB()), 0.0, 0.0);
            fr.draw(matrixStack, (ITextComponent) valueDisplayTpl.getA(), 0.0f, 0.0f, -15130590);
            matrixStack.popPose();
            matrixStack.translate(0.0, 10.0, 0.0);
        }
        matrixStack.popPose();
        RenderSystem.enableDepthTest();
    }

    private int renderFastestVaultDisplay(final MatrixStack matrixStack, final CompoundNBT vaultStats, final int rightShift) {
        final PlayerVaultStatsData.PlayerRecordEntry entry = PlayerVaultStatsData.PlayerRecordEntry.deserialize(vaultStats.getCompound("fastestVault"));
        final String displayName = StringUtils.isNullOrEmpty(entry.getPlayerName()) ? "Unclaimed" : entry.getPlayerName();
        final FontRenderer fr = Minecraft.getInstance().font;
        final ITextComponent display = (ITextComponent) new TranslationTextComponent("stat.the_vault.fastestVault").append(":");
        fr.draw(matrixStack, display.getVisualOrderText(), 0.0f, 0.0f, -15130590);
        fr.draw(matrixStack, new StringTextComponent(displayName).getVisualOrderText(), 0.0f, 10.0f, -15130590);
        final ITextComponent timeString = (ITextComponent) new StringTextComponent(UIHelper.formatTimeString(entry.getTickCount()));
        final int xOffset = rightShift - fr.width((ITextProperties) timeString);
        fr.draw(matrixStack, timeString.getVisualOrderText(), (float) xOffset, 10.0f, -15130590);
        return 25;
    }

    private int buildVaultStatisticsDisplay(final CompoundNBT vaultStats, final List<Tuple<ITextComponent, Tuple<ITextComponent, Integer>>> out) {
        final DecimalFormat decFormat = new DecimalFormat("0.##");
        int numberOffset = 0;
        numberOffset = this.addVaultStat(out, "powerLevel", String.valueOf(vaultStats.getInt("powerLevel")), numberOffset);
        numberOffset = this.addVaultStat(out, "knowledgeLevel", String.valueOf(vaultStats.getInt("knowledgeLevel")), numberOffset);
        numberOffset = this.addVaultStat(out, "crystalsCrafted", String.valueOf(vaultStats.getInt("crystalsCrafted")), numberOffset);
        numberOffset = this.addVaultStat(out, "vaultArtifacts", String.valueOf(vaultStats.getInt("vaultArtifacts")), numberOffset);
        numberOffset = this.addVaultStat(out, "vaultTotal", String.valueOf(vaultStats.getInt("vaultTotal")), numberOffset);
        numberOffset = this.addVaultStat(out, "vaultDeaths", String.valueOf(vaultStats.getInt("vaultDeaths")), numberOffset);
        numberOffset = this.addVaultStat(out, "vaultBails", String.valueOf(vaultStats.getInt("vaultBails")), numberOffset);
        numberOffset = this.addVaultStat(out, "vaultBossKills", String.valueOf(vaultStats.getInt("vaultBossKills")), numberOffset);
        if (vaultStats.contains("vaultRaids", 3)) {
            numberOffset = this.addVaultStat(out, "vaultRaids", String.valueOf(vaultStats.getInt("vaultRaids")), numberOffset);
        }
        return numberOffset;
    }

    private int addVaultStat(final List<Tuple<ITextComponent, Tuple<ITextComponent, Integer>>> out, final String key, final String value, final int currentMaxOffset) {
        return this.addVaultStat(out, key, value, value, currentMaxOffset);
    }

    private int addVaultStat(final List<Tuple<ITextComponent, Tuple<ITextComponent, Integer>>> out, final String key, final String value, final String valueLengthStr, int currentMaxOffset) {
        final FontRenderer fr = Minecraft.getInstance().font;
        final int valueStrLength = fr.width(valueLengthStr);
        if (valueStrLength > currentMaxOffset) {
            currentMaxOffset = valueStrLength;
        }
        final Tuple<ITextComponent, Integer> valueDisplayTpl = (Tuple<ITextComponent, Integer>) new Tuple(new StringTextComponent(value), valueStrLength);
        out.add((Tuple<ITextComponent, Tuple<ITextComponent, Integer>>) new Tuple(new TranslationTextComponent("stat.the_vault." + key), valueDisplayTpl));
        return currentMaxOffset;
    }

    private void renderBookBackground(final Rectangle containerBounds, final MatrixStack renderStack) {
        Minecraft.getInstance().getTextureManager().bind(ReadBookScreen.BOOK_LOCATION);
        ScreenDrawHelper.draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, (BufferBuilder buf) -> ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack, (float) containerBounds.width, (float) containerBounds.height).at((float) containerBounds.x, (float) containerBounds.y).texVanilla(0.0f, 0.0f, 192.0f, 192.0f).draw());
    }
}
