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
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;

public class EtchingTradeScreen extends ContainerScreen<EtchingTradeContainer> {
    private static final ResourceLocation TEXTURE;

    public EtchingTradeScreen(final EtchingTradeContainer screenContainer, final PlayerInventory inv, final ITextComponent title) {
        super(screenContainer, inv, StringTextComponent.EMPTY);
        this.imageWidth = 176;
        this.imageHeight = 184;
        this.inventoryLabelY = 90;
    }

    protected void renderBg(final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(EtchingTradeScreen.TEXTURE);
        final int offsetX = (this.width - this.imageWidth) / 2;
        final int offsetY = (this.height - this.imageHeight) / 2;
        blit(matrixStack, offsetX, offsetY, this.getBlitOffset(), 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 512);
        final EtchingTradeContainer container = (EtchingTradeContainer) this.getMenu();
        final EtchingVendorEntity vendor = container.getVendor();
        if (vendor == null) {
            return;
        }
        final EtchingVendorControllerTileEntity controllerTile = vendor.getControllerTile();
        if (controllerTile == null) {
            return;
        }
        for (int i = 0; i < 3; ++i) {
            final int xx = offsetX + 44;
            final int yy = offsetY + 5 + i * 28;
            final int slotInXX = offsetX + 52;
            final int slotInYY = offsetY + 9 + i * 28;
            final int slotOutXX = offsetX + 106;
            final int slotOutYY = offsetY + 9 + i * 28;
            int vOffset = 1;
            final EtchingVendorControllerTileEntity.EtchingTrade trade = controllerTile.getTrade(i);
            if (trade == null || trade.isSold()) {
                vOffset = 57;
            } else {
                final Rectangle tradeBox = new Rectangle(xx, yy, 88, 27);
                if (tradeBox.contains(x, y)) {
                    vOffset = 29;
                }
            }
            blit(matrixStack, xx, yy, this.getBlitOffset(), 177.0f, (float) vOffset, 88, 27, 256, 512);
            blit(matrixStack, slotInXX, slotInYY, this.getBlitOffset(), 177.0f, 85.0f, 18, 18, 256, 512);
            blit(matrixStack, slotOutXX, slotOutYY, this.getBlitOffset(), 177.0f, 85.0f, 18, 18, 256, 512);
        }
    }

    protected void renderLabels(final MatrixStack matrixStack, final int x, final int y) {
        super.renderLabels(matrixStack, x, y);
        final EtchingTradeContainer container = (EtchingTradeContainer) this.getMenu();
        final EtchingVendorEntity vendor = container.getVendor();
        if (vendor == null) {
            return;
        }
        final EtchingVendorControllerTileEntity controllerTile = vendor.getControllerTile();
        if (controllerTile == null) {
            return;
        }
        for (int i = 0; i < 3; ++i) {
            final EtchingVendorControllerTileEntity.EtchingTrade trade = controllerTile.getTrade(i);
            if (trade != null) {
                if (!trade.isSold()) {
                    final int xx = 71;
                    final int yy = 10 + i * 28;
                    final ItemStack stack = new ItemStack((IItemProvider) ModItems.VAULT_PLATINUM, trade.getRequiredPlatinum());
                    this.itemRenderer.renderGuiItem(stack, xx, yy);
                    this.itemRenderer.renderGuiItemDecorations(this.font, stack, xx, yy, (String) null);
                }
            }
        }
    }

    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    public boolean isPauseScreen() {
        return false;
    }

    static {
        TEXTURE = Vault.id("textures/gui/etching_trade.png");
    }
}
