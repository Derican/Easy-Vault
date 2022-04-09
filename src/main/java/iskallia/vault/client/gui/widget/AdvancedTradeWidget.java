// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.screen.AdvancedVendingMachineScreen;
import iskallia.vault.container.AdvancedVendingContainer;
import iskallia.vault.vending.Trade;
import iskallia.vault.vending.TraderCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;

public class AdvancedTradeWidget extends Widget
{
    public static final int BUTTON_WIDTH = 88;
    public static final int BUTTON_HEIGHT = 27;
    protected AdvancedVendingMachineScreen parentScreen;
    protected TraderCore traderCode;
    
    public AdvancedTradeWidget(final int x, final int y, final TraderCore traderCode, final AdvancedVendingMachineScreen parentScreen) {
        super(x, y, 0, 0, (ITextComponent)new StringTextComponent(""));
        this.parentScreen = parentScreen;
        this.traderCode = traderCode;
    }
    
    public TraderCore getTraderCode() {
        return this.traderCode;
    }
    
    public void mouseMoved(final double mouseX, final double mouseY) {
    }
    
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        return this.x <= mouseX && mouseX <= this.x + 88 && this.y <= mouseY && mouseY <= this.y + 27;
    }
    
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(AdvancedVendingMachineScreen.HUD_RESOURCE);
        final Trade trade = this.traderCode.getTrade();
        final ItemStack buy = trade.getBuy().toStack();
        final ItemStack sell = trade.getSell().toStack();
        final ItemRenderer itemRenderer = minecraft.getItemRenderer();
        final Rectangle tradeBoundaries = this.parentScreen.getTradeBoundaries();
        final int yOFfset = this.parentScreen.tradesContainer.getyOffset();
        if (trade.getTradesLeft() == 0) {
            blit(matrixStack, this.x, this.y, 277.0f, 96.0f, 88, 27, 512, 256);
            RenderSystem.disableDepthTest();
            itemRenderer.renderGuiItem(buy, 5 + this.x + tradeBoundaries.x, 6 + this.y + tradeBoundaries.y - yOFfset);
            itemRenderer.renderGuiItem(sell, 55 + this.x + tradeBoundaries.x, 6 + this.y + tradeBoundaries.y - yOFfset);
            return;
        }
        final boolean isHovered = this.isHovered(mouseX, mouseY);
        final boolean isSelected = ((AdvancedVendingContainer)this.parentScreen.getMenu()).getSelectedTrade() == this.traderCode;
        blit(matrixStack, this.x, this.y, 277.0f, (isHovered || isSelected) ? 68.0f : 40.0f, 88, 27, 512, 256);
        RenderSystem.disableDepthTest();
        itemRenderer.renderGuiItem(buy, 5 + this.x + tradeBoundaries.x, 6 + this.y + tradeBoundaries.y - yOFfset);
        itemRenderer.renderGuiItem(sell, 55 + this.x + tradeBoundaries.x, 6 + this.y + tradeBoundaries.y - yOFfset);
        minecraft.font.draw(matrixStack, buy.getCount() + "", (float)(this.x + 23), (float)(this.y + 10), -1);
        minecraft.font.draw(matrixStack, sell.getCount() + "", (float)(this.x + 73), (float)(this.y + 10), -1);
        RenderSystem.enableDepthTest();
    }
}
