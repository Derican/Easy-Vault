package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.client.ClientShardTradeData;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.container.inventory.ShardTradeContainer;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.ItemShardPouch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

public class ShardTradeScreen extends ContainerScreen<ShardTradeContainer> {
    private static final ResourceLocation TEXTURE = Vault.id("textures/gui/shard_trade.png");

    public ShardTradeScreen(ShardTradeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 184;

        this.inventoryLabelY = 90;
    }


    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int offsetX = (this.width - this.imageWidth) / 2;
        int offsetY = (this.height - this.imageHeight) / 2;
        blit(matrixStack, offsetX, offsetY, getBlitOffset(), 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 512);

        for (int tradeIndex = 0; tradeIndex < 3; tradeIndex++) {
            int xx = offsetX + 83;
            int yy = offsetY + 5 + tradeIndex * 28;
            int slotXX = offsetX + 145;
            int slotYY = offsetY + 9 + tradeIndex * 28;

            int vOffset = 1;
            if (ClientShardTradeData.getTradeInfo(tradeIndex) == null) {
                vOffset = 57;
            } else {
                Rectangle tradeBox = new Rectangle(xx, yy, 88, 27);
                if (tradeBox.contains(x, y)) {
                    vOffset = 29;
                }
            }

            blit(matrixStack, xx, yy, getBlitOffset(), 177.0F, vOffset, 88, 27, 256, 512);
            blit(matrixStack, slotXX, slotYY, getBlitOffset(), 177.0F, 85.0F, 18, 18, 256, 512);
        }
    }

    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        matrixStack.pushPose();
        matrixStack.translate(this.titleLabelX, this.titleLabelY, 0.0D);
        matrixStack.scale(0.75F, 0.75F, 1.0F);
        this.font.draw(matrixStack, this.title, 0.0F, 0.0F, 4210752);
        matrixStack.popPose();
        this.font.draw(matrixStack, this.inventory.getDisplayName(), this.inventoryLabelX, this.inventoryLabelY, 4210752);

        int shardCount = ItemShardPouch.getShardCount((Minecraft.getInstance()).player.inventory);
        ItemStack stack = new ItemStack((IItemProvider) ModItems.SOUL_SHARD);

        for (int tradeIndex = 0; tradeIndex < 3; tradeIndex++) {
            Tuple<ItemStack, Integer> trade = ClientShardTradeData.getTradeInfo(tradeIndex);
            if (trade != null) {
                int i = 94;
                int j = 10 + tradeIndex * 28;

                this.itemRenderer.renderGuiItem(stack, i, j);

                String str = String.valueOf(trade.getB());
                int k = this.font.width(str);
                int m = 16777215;
                if (shardCount < ((Integer) trade.getB()).intValue()) {
                    m = 8257536;
                }

                matrixStack.pushPose();
                matrixStack.translate(0.0D, 0.0D, 400.0D);
                FontHelper.drawStringWithBorder(matrixStack, str, (i + 8) - k / 2.0F, (j + 8), m, 0);
                matrixStack.popPose();
            }
        }

        int xx = 34;
        int yy = 56;
        this.itemRenderer.renderGuiItem(stack, xx, yy);

        String text = String.valueOf(ClientShardTradeData.getRandomTradeCost());
        int width = this.font.width(text);
        int color = 16777215;
        if (shardCount < ClientShardTradeData.getRandomTradeCost()) {
            color = 8257536;
        }
        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.0D, 400.0D);
        FontHelper.drawStringWithBorder(matrixStack, text, (xx + 9) - width / 2.0F, (yy + 8), color, 0);
        matrixStack.popPose();
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);
    }


    public boolean mouseReleased(double mouseX, double mouseY, int button) {
//        this.doubleclick = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }


    public boolean isPauseScreen() {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\screen\ShardTradeScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */