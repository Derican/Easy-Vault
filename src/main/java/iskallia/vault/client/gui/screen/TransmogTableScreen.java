package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import iskallia.vault.container.TransmogTableContainer;
import iskallia.vault.container.inventory.TransmogTableInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TransmogTableScreen
        extends ContainerScreen<TransmogTableContainer> {
    private static final ResourceLocation GUI_RESOURCE = Vault.id("textures/gui/transmog-table.png");

    public TransmogTableScreen(TransmogTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }


    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
    }


    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, (ITextComponent) new StringTextComponent(""), this.titleLabelX, this.titleLabelY, 4210752);
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);

        float midX = this.width / 2.0F;
        float midY = this.height / 2.0F;

        Minecraft minecraft = getMinecraft();

        int containerWidth = 176;
        int containerHeight = 166;

        minecraft.getTextureManager().bind(GUI_RESOURCE);
        blit(matrixStack, (int) (midX - (containerWidth / 2)), (int) (midY - (containerHeight / 2)), 0, 0, containerWidth, containerHeight);


        TransmogTableInventory transmogInventory = ((TransmogTableContainer) this.menu).getInternalInventory();
        ItemStack armorStack = transmogInventory.getItem(0);

        if (transmogInventory.isIngredientSlotsFilled() && !transmogInventory.recipeFulfilled()) {
            blit(matrixStack, (int) (midX + 15.0F), (int) (midY - 33.0F), 176, 0, 28, 21);
        }


        FontRenderer fontRenderer = minecraft.font;

        String title = "Transmogrification";
        fontRenderer.draw(matrixStack, title, midX - 35.0F, midY - 70.0F, 4144959);


        String inventoryTitle = "Inventory";
        fontRenderer.draw(matrixStack, inventoryTitle, midX - 80.0F, midY - 11.0F, 4144959);


        if (!armorStack.isEmpty() && armorStack.getItem() instanceof iskallia.vault.item.gear.VaultArmorItem) {
            int requiredVaultBronze = transmogInventory.requiredVaultBronze();
            if (requiredVaultBronze != -1) {
                fontRenderer.draw(matrixStack, "x", midX - 9.0F, midY - 45.0F, 9145227);


                fontRenderer.draw(matrixStack, String.valueOf(requiredVaultBronze), midX - 2.0F, midY - 45.0F, 9145227);
            }
        }


        super.render(matrixStack, mouseX, mouseY, partialTicks);

        renderTooltip(matrixStack, mouseX, mouseY);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\screen\TransmogTableScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */