// 
// Decompiled by Procyon v0.6.0
// 

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

public class ShardTradeScreen extends ContainerScreen<ShardTradeContainer>
{
    private static final ResourceLocation TEXTURE;
    
    public ShardTradeScreen(final ShardTradeContainer screenContainer, final PlayerInventory inv, final ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 184;
        this.inventoryLabelY = 90;
    }
    
    protected void renderBg(final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(ShardTradeScreen.TEXTURE);
        final int offsetX = (this.width - this.imageWidth) / 2;
        final int offsetY = (this.height - this.imageHeight) / 2;
        blit(matrixStack, offsetX, offsetY, this.getBlitOffset(), 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 512);
        for (int tradeIndex = 0; tradeIndex < 3; ++tradeIndex) {
            final int xx = offsetX + 83;
            final int yy = offsetY + 5 + tradeIndex * 28;
            final int slotXX = offsetX + 145;
            final int slotYY = offsetY + 9 + tradeIndex * 28;
            int vOffset = 1;
            if (ClientShardTradeData.getTradeInfo(tradeIndex) == null) {
                vOffset = 57;
            }
            else {
                final Rectangle tradeBox = new Rectangle(xx, yy, 88, 27);
                if (tradeBox.contains(x, y)) {
                    vOffset = 29;
                }
            }
            blit(matrixStack, xx, yy, this.getBlitOffset(), 177.0f, (float)vOffset, 88, 27, 256, 512);
            blit(matrixStack, slotXX, slotYY, this.getBlitOffset(), 177.0f, 85.0f, 18, 18, 256, 512);
        }
    }
    
    protected void renderLabels(final MatrixStack matrixStack, final int x, final int y) {
        matrixStack.pushPose();
        matrixStack.translate((double)this.titleLabelX, (double)this.titleLabelY, 0.0);
        matrixStack.scale(0.75f, 0.75f, 1.0f);
        this.font.draw(matrixStack, this.title, 0.0f, 0.0f, 4210752);
        matrixStack.popPose();
        this.font.draw(matrixStack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
        final int shardCount = ItemShardPouch.getShardCount(Minecraft.getInstance().player.inventory);
        final ItemStack stack = new ItemStack((IItemProvider)ModItems.SOUL_SHARD);
        for (int tradeIndex = 0; tradeIndex < 3; ++tradeIndex) {
            final Tuple<ItemStack, Integer> trade = ClientShardTradeData.getTradeInfo(tradeIndex);
            if (trade != null) {
                final int xx = 94;
                final int yy = 10 + tradeIndex * 28;
                this.itemRenderer.renderGuiItem(stack, xx, yy);
                final String text = String.valueOf(trade.getB());
                final int width = this.font.width(text);
                int color = 16777215;
                if (shardCount < (int)trade.getB()) {
                    color = 8257536;
                }
                matrixStack.pushPose();
                matrixStack.translate(0.0, 0.0, 400.0);
                FontHelper.drawStringWithBorder(matrixStack, text, xx + 8 - width / 2.0f, (float)(yy + 8), color, 0);
                matrixStack.popPose();
            }
        }
        final int xx2 = 34;
        final int yy2 = 56;
        this.itemRenderer.renderGuiItem(stack, xx2, yy2);
        final String text2 = String.valueOf(ClientShardTradeData.getRandomTradeCost());
        final int width2 = this.font.width(text2);
        int color2 = 16777215;
        if (shardCount < ClientShardTradeData.getRandomTradeCost()) {
            color2 = 8257536;
        }
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.0, 400.0);
        FontHelper.drawStringWithBorder(matrixStack, text2, xx2 + 9 - width2 / 2.0f, (float)(yy2 + 8), color2, 0);
        matrixStack.popPose();
    }
    
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    public boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        this.doubleclick = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    public boolean isPauseScreen() {
        return false;
    }
    
    static {
        TEXTURE = Vault.id("textures/gui/shard_trade.png");
    }
}
