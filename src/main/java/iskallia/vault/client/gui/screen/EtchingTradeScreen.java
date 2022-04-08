package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.block.entity.EtchingVendorControllerTileEntity;
import iskallia.vault.container.inventory.EtchingTradeContainer;
import iskallia.vault.entity.EtchingVendorEntity;
import iskallia.vault.init.ModItems;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;


public class EtchingTradeScreen
        extends ContainerScreen<EtchingTradeContainer> {
    private static final ResourceLocation TEXTURE = Vault.id("textures/gui/etching_trade.png");

    public EtchingTradeScreen(EtchingTradeContainer screenContainer, PlayerInventory inv, ITextComponent title) {
        super( screenContainer, inv, StringTextComponent.EMPTY);
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

        EtchingTradeContainer container = (EtchingTradeContainer) getMenu();
        EtchingVendorEntity vendor = container.getVendor();
        if (vendor == null) {
            return;
        }
        EtchingVendorControllerTileEntity controllerTile = vendor.getControllerTile();
        if (controllerTile == null) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            int xx = offsetX + 44;
            int yy = offsetY + 5 + i * 28;
            int slotInXX = offsetX + 52;
            int slotInYY = offsetY + 9 + i * 28;
            int slotOutXX = offsetX + 106;
            int slotOutYY = offsetY + 9 + i * 28;

            int vOffset = 1;
            EtchingVendorControllerTileEntity.EtchingTrade trade = controllerTile.getTrade(i);
            if (trade == null || trade.isSold()) {
                vOffset = 57;
            } else {
                Rectangle tradeBox = new Rectangle(xx, yy, 88, 27);
                if (tradeBox.contains(x, y)) {
                    vOffset = 29;
                }
            }

            blit(matrixStack, xx, yy, getBlitOffset(), 177.0F, vOffset, 88, 27, 256, 512);
            blit(matrixStack, slotInXX, slotInYY, getBlitOffset(), 177.0F, 85.0F, 18, 18, 256, 512);
            blit(matrixStack, slotOutXX, slotOutYY, getBlitOffset(), 177.0F, 85.0F, 18, 18, 256, 512);
        }
    }


    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        super.renderLabels(matrixStack, x, y);

        EtchingTradeContainer container = (EtchingTradeContainer) getMenu();
        EtchingVendorEntity vendor = container.getVendor();
        if (vendor == null) {
            return;
        }
        EtchingVendorControllerTileEntity controllerTile = vendor.getControllerTile();
        if (controllerTile == null) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            EtchingVendorControllerTileEntity.EtchingTrade trade = controllerTile.getTrade(i);
            if (trade != null && !trade.isSold()) {


                int xx = 71;
                int yy = 10 + i * 28;

                ItemStack stack = new ItemStack((IItemProvider) ModItems.VAULT_PLATINUM, trade.getRequiredPlatinum());
                this.itemRenderer.renderGuiItem(stack, xx, yy);
                this.itemRenderer.renderGuiItemDecorations(this.font, stack, xx, yy, null);
            }
        }
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);
    }


    public boolean isPauseScreen() {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\screen\EtchingTradeScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */