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

public class AdvancedTradeWidget
        extends Widget {
    public static final int BUTTON_WIDTH = 88;
    public static final int BUTTON_HEIGHT = 27;
    protected AdvancedVendingMachineScreen parentScreen;
    protected TraderCore traderCode;

    public AdvancedTradeWidget(int x, int y, TraderCore traderCode, AdvancedVendingMachineScreen parentScreen) {
        super(x, y, 0, 0, (ITextComponent) new StringTextComponent(""));
        this.parentScreen = parentScreen;
        this.traderCode = traderCode;
    }

    public TraderCore getTraderCode() {
        return this.traderCode;
    }


    public void mouseMoved(double mouseX, double mouseY) {
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (this.x <= mouseX && mouseX <= this.x + 88 && this.y <= mouseY && mouseY <= this.y + 27);
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();

        minecraft.getTextureManager().bind(AdvancedVendingMachineScreen.HUD_RESOURCE);

        Trade trade = this.traderCode.getTrade();
        ItemStack buy = trade.getBuy().toStack();
        ItemStack sell = trade.getSell().toStack();

        ItemRenderer itemRenderer = minecraft.getItemRenderer();

        Rectangle tradeBoundaries = this.parentScreen.getTradeBoundaries();
        int yOFfset = this.parentScreen.tradesContainer.getyOffset();

        if (trade.getTradesLeft() == 0) {
            blit(matrixStack, this.x, this.y, 277.0F, 96.0F, 88, 27, 512, 256);

            RenderSystem.disableDepthTest();
            itemRenderer.renderGuiItem(buy, 5 + this.x + tradeBoundaries.x, 6 + this.y + tradeBoundaries.y - yOFfset);


            itemRenderer.renderGuiItem(sell, 55 + this.x + tradeBoundaries.x, 6 + this.y + tradeBoundaries.y - yOFfset);


            return;
        }

        boolean isHovered = isHovered(mouseX, mouseY);

        boolean isSelected = (((AdvancedVendingContainer) this.parentScreen.getMenu()).getSelectedTrade() == this.traderCode);

        blit(matrixStack, this.x, this.y, 277.0F, (isHovered || isSelected) ? 68.0F : 40.0F, 88, 27, 512, 256);


        RenderSystem.disableDepthTest();
        itemRenderer.renderGuiItem(buy, 5 + this.x + tradeBoundaries.x, 6 + this.y + tradeBoundaries.y - yOFfset);


        itemRenderer.renderGuiItem(sell, 55 + this.x + tradeBoundaries.x, 6 + this.y + tradeBoundaries.y - yOFfset);


        minecraft.font.draw(matrixStack, buy
                .getCount() + "", (this.x + 23), (this.y + 10), -1);
        minecraft.font.draw(matrixStack, sell
                .getCount() + "", (this.x + 73), (this.y + 10), -1);
        RenderSystem.enableDepthTest();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\widget\AdvancedTradeWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */