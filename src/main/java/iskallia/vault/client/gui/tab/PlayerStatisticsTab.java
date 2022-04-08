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
import net.minecraft.util.text.*;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerStatisticsTab
        extends SkillTab {
    public PlayerStatisticsTab(SkillTreeScreen parentScreen) {
        super(parentScreen, (ITextComponent) new StringTextComponent("Statistics Tab"));
        setScrollable(false);
    }


    public String getTabName() {
        return "Statistics";
    }


    public void refresh() {
    }


    public List<Runnable> renderTab(Rectangle containerBounds, MatrixStack renderStack, int mouseX, int mouseY, float pTicks) {
        renderTabBackground(renderStack, containerBounds);
        renderBookBackground(containerBounds, renderStack);

        int pxOffsetX = 38;
        int pxOffsetY = 16;

        float pxWidth = containerBounds.width / 192.0F;
        float pxHeight = containerBounds.height / 192.0F;
        int offsetX = containerBounds.x + Math.round(pxWidth * pxOffsetX);
        int offsetY = containerBounds.y + Math.round(pxHeight * pxOffsetY);
        Rectangle bookCt = new Rectangle(offsetX, offsetY, Math.round(108.0F * pxWidth), Math.round(104.0F * pxWidth));

        renderStack.pushPose();
        renderStack.translate((offsetX + 5), (offsetY + 5), 0.0D);
        renderPlayerAttributes(bookCt, renderStack, mouseX, mouseY, pTicks);
        renderStack.popPose();

        return Collections.emptyList();
    }


    private void renderPlayerAttributes(Rectangle containerBounds, MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        FontRenderer fr = (Minecraft.getInstance()).font;
        CompoundNBT vaultStats = ClientStatisticsData.getSerializedVaultStats();

        List<Tuple<ITextComponent, Tuple<ITextComponent, Integer>>> statDisplay = new ArrayList<>();
        int numberOffset = buildVaultStatisticsDisplay(vaultStats, statDisplay);

        int maxLength = 0;

        StringTextComponent text = new StringTextComponent("");
        for (Tuple<ITextComponent, Tuple<ITextComponent, Integer>> statTpl : statDisplay) {
            text.append((ITextComponent) statTpl.getA()).append("\n");

            int length = fr.width((ITextProperties) statTpl.getA());
            if (length > maxLength) {
                maxLength = length;
            }
        }

        maxLength += 5;
        maxLength += numberOffset;

        matrixStack.pushPose();
        int yOffset = renderFastestVaultDisplay(matrixStack, vaultStats, maxLength);
        matrixStack.translate(0.0D, yOffset, 0.0D);
        UIHelper.renderWrappedText(matrixStack, (ITextComponent) text, containerBounds.width, 0);

        matrixStack.translate(maxLength, 0.0D, 0.0D);
        for (Tuple<ITextComponent, Tuple<ITextComponent, Integer>> statTpl : statDisplay) {
            Tuple<ITextComponent, Integer> valueDisplayTpl = (Tuple<ITextComponent, Integer>) statTpl.getB();

            matrixStack.pushPose();
            matrixStack.translate(-((Integer) valueDisplayTpl.getB()).intValue(), 0.0D, 0.0D);
            fr.draw(matrixStack, (ITextComponent) valueDisplayTpl.getA(), 0.0F, 0.0F, -15130590);

            matrixStack.popPose();

            matrixStack.translate(0.0D, 10.0D, 0.0D);
        }
        matrixStack.popPose();

        RenderSystem.enableDepthTest();
    }

    private int renderFastestVaultDisplay(MatrixStack matrixStack, CompoundNBT vaultStats, int rightShift) {
        PlayerVaultStatsData.PlayerRecordEntry entry = PlayerVaultStatsData.PlayerRecordEntry.deserialize(vaultStats.getCompound("fastestVault"));
        String displayName = StringUtils.isNullOrEmpty(entry.getPlayerName()) ? "Unclaimed" : entry.getPlayerName();
        FontRenderer fr = (Minecraft.getInstance()).font;

        IFormattableTextComponent iFormattableTextComponent = (new TranslationTextComponent("stat.the_vault.fastestVault")).append(":");
        fr.draw(matrixStack, iFormattableTextComponent.getVisualOrderText(), 0.0F, 0.0F, -15130590);
        fr.draw(matrixStack, (new StringTextComponent(displayName)).getVisualOrderText(), 0.0F, 10.0F, -15130590);

        StringTextComponent stringTextComponent = new StringTextComponent(UIHelper.formatTimeString(entry.getTickCount()));
        int xOffset = rightShift - fr.width((ITextProperties) stringTextComponent);
        fr.draw(matrixStack, stringTextComponent.getVisualOrderText(), xOffset, 10.0F, -15130590);

        return 25;
    }

    private int buildVaultStatisticsDisplay(CompoundNBT vaultStats, List<Tuple<ITextComponent, Tuple<ITextComponent, Integer>>> out) {
        DecimalFormat decFormat = new DecimalFormat("0.##");
        int numberOffset = 0;

        numberOffset = addVaultStat(out, "powerLevel", String.valueOf(vaultStats.getInt("powerLevel")), numberOffset);
        numberOffset = addVaultStat(out, "knowledgeLevel", String.valueOf(vaultStats.getInt("knowledgeLevel")), numberOffset);
        numberOffset = addVaultStat(out, "crystalsCrafted", String.valueOf(vaultStats.getInt("crystalsCrafted")), numberOffset);
        numberOffset = addVaultStat(out, "vaultArtifacts", String.valueOf(vaultStats.getInt("vaultArtifacts")), numberOffset);
        numberOffset = addVaultStat(out, "vaultTotal", String.valueOf(vaultStats.getInt("vaultTotal")), numberOffset);
        numberOffset = addVaultStat(out, "vaultDeaths", String.valueOf(vaultStats.getInt("vaultDeaths")), numberOffset);
        numberOffset = addVaultStat(out, "vaultBails", String.valueOf(vaultStats.getInt("vaultBails")), numberOffset);
        numberOffset = addVaultStat(out, "vaultBossKills", String.valueOf(vaultStats.getInt("vaultBossKills")), numberOffset);
        if (vaultStats.contains("vaultRaids", 3)) {
            numberOffset = addVaultStat(out, "vaultRaids", String.valueOf(vaultStats.getInt("vaultRaids")), numberOffset);
        }


        return numberOffset;
    }


    private int addVaultStat(List<Tuple<ITextComponent, Tuple<ITextComponent, Integer>>> out, String key, String value, int currentMaxOffset) {
        return addVaultStat(out, key, value, value, currentMaxOffset);
    }


    private int addVaultStat(List<Tuple<ITextComponent, Tuple<ITextComponent, Integer>>> out, String key, String value, String valueLengthStr, int currentMaxOffset) {
        FontRenderer fr = (Minecraft.getInstance()).font;
        int valueStrLength = fr.width(valueLengthStr);
        if (valueStrLength > currentMaxOffset) {
            currentMaxOffset = valueStrLength;
        }
        Tuple<ITextComponent, Integer> valueDisplayTpl = new Tuple(new StringTextComponent(value), Integer.valueOf(valueStrLength));
        out.add(new Tuple(new TranslationTextComponent("stat.the_vault." + key), valueDisplayTpl));
        return currentMaxOffset;
    }

    private void renderBookBackground(Rectangle containerBounds, MatrixStack renderStack) {
        Minecraft.getInstance().getTextureManager().bind(ReadBookScreen.BOOK_LOCATION);
        ScreenDrawHelper.draw(7, DefaultVertexFormats.POSITION_COLOR_TEX, (BufferBuilder buf) -> ScreenDrawHelper.rect((IVertexBuilder) buf, renderStack, containerBounds.width, containerBounds.height).at(containerBounds.x, containerBounds.y).texVanilla(0.0F, 0.0F, 192.0F, 192.0F).draw());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\tab\PlayerStatisticsTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */