package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import iskallia.vault.container.TransmogTableContainer;
import iskallia.vault.container.inventory.TransmogTableInventory;
import iskallia.vault.item.gear.VaultArmorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TransmogTableScreen extends ContainerScreen<TransmogTableContainer> {
    private static final ResourceLocation GUI_RESOURCE;

    public TransmogTableScreen(final TransmogTableContainer screenContainer, final PlayerInventory inv, final ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    protected void renderBg(final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
    }

    protected void renderLabels(final MatrixStack matrixStack, final int x, final int y) {
        this.font.draw(matrixStack, new StringTextComponent(""), (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        final float midX = this.width / 2.0f;
        final float midY = this.height / 2.0f;
        final Minecraft minecraft = this.getMinecraft();
        final int containerWidth = 176;
        final int containerHeight = 166;
        minecraft.getTextureManager().bind(TransmogTableScreen.GUI_RESOURCE);
        this.blit(matrixStack, (int) (midX - containerWidth / 2), (int) (midY - containerHeight / 2), 0, 0, containerWidth, containerHeight);
        final TransmogTableInventory transmogInventory = this.menu.getInternalInventory();
        final ItemStack armorStack = transmogInventory.getItem(0);
        if (transmogInventory.isIngredientSlotsFilled() && !transmogInventory.recipeFulfilled()) {
            this.blit(matrixStack, (int) (midX + 15.0f), (int) (midY - 33.0f), 176, 0, 28, 21);
        }
        final FontRenderer fontRenderer = minecraft.font;
        final String title = "Transmogrification";
        fontRenderer.draw(matrixStack, title, midX - 35.0f, midY - 70.0f, 4144959);
        final String inventoryTitle = "Inventory";
        fontRenderer.draw(matrixStack, inventoryTitle, midX - 80.0f, midY - 11.0f, 4144959);
        if (!armorStack.isEmpty() && armorStack.getItem() instanceof VaultArmorItem) {
            final int requiredVaultBronze = transmogInventory.requiredVaultBronze();
            if (requiredVaultBronze != -1) {
                fontRenderer.draw(matrixStack, "x", midX - 9.0f, midY - 45.0f, 9145227);
                fontRenderer.draw(matrixStack, String.valueOf(requiredVaultBronze), midX - 2.0f, midY - 45.0f, 9145227);
            }
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    static {
        GUI_RESOURCE = Vault.id("textures/gui/transmog-table.png");
    }
}
