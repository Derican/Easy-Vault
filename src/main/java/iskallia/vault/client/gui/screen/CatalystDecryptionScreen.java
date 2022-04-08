package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.container.inventory.CatalystDecryptionContainer;
import iskallia.vault.item.VaultCatalystItem;
import iskallia.vault.item.VaultInhibitorItem;
import iskallia.vault.item.catalyst.ModifierRollResult;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CatalystDecryptionScreen
        extends ContainerScreen<CatalystDecryptionContainer> {
    private static final ResourceLocation TEXTURE = Vault.id("textures/gui/catalyst-decryption-table.png");

    public CatalystDecryptionScreen(CatalystDecryptionContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super( screenContainer, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 234;
    }


    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int offsetX = (this.width - this.imageWidth) / 2;
        int offsetY = (this.height - this.imageHeight) / 2;
        blit(matrixStack, offsetX, offsetY, 0, 0, this.imageWidth, this.imageHeight);
    }


    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title, 5.0F, 5.0F, 4210752);

        Slot crystalSlot = ((CatalystDecryptionContainer) this.menu).getSlot(((CatalystDecryptionContainer) this.menu).slots.size() - 1);
        if (!crystalSlot.hasItem()) {
            return;
        }
        ItemStack crystal = crystalSlot.getItem();
        if (crystal.isEmpty() || !(crystal.getItem() instanceof iskallia.vault.item.crystal.VaultCrystalItem)) {
            return;
        }

        for (Slot catalystSlot : ((CatalystDecryptionContainer) this.menu).getCatalystSlots()) {
            if (!catalystSlot.hasItem()) {
                continue;
            }
            ItemStack modifyingItem = catalystSlot.getItem();
            if (modifyingItem.isEmpty()) {
                continue;
            }
            if (modifyingItem.getItem() instanceof VaultCatalystItem || modifyingItem.getItem() instanceof VaultInhibitorItem) {

                boolean catalyst = modifyingItem.getItem() instanceof VaultCatalystItem;


                List<String> modifierOutcomes = catalyst ? VaultCatalystItem.getCrystalCombinationModifiers(modifyingItem, crystal) : VaultInhibitorItem.getCrystalCombinationModifiers(modifyingItem, crystal);
                if (modifierOutcomes == null || modifierOutcomes.isEmpty()) {
                    continue;
                }
                boolean isLeft = (catalystSlot.index % 2 == 0);


                List<ITextComponent> results = (List<ITextComponent>) modifierOutcomes.stream().map(ModifierRollResult::ofModifier).map(result -> result.getTooltipDescription(catalyst ? "Adds " : "Removes ", false)).flatMap(Collection::stream).collect(Collectors.toList());

                RenderSystem.pushMatrix();
                if (!isLeft) {
                    RenderSystem.translatef((catalystSlot.x + 14), catalystSlot.y, 0.0F);
                    RenderSystem.scalef(0.65F, 0.65F, 0.65F);
                    renderWrappedToolTip(matrixStack, results, 0, 0, this.font);

                } else {

                    int maxLength = results.stream().mapToInt(txt -> this.font.width(txt.getVisualOrderText())).max().orElse(0);

                    RenderSystem.translatef((catalystSlot.x - 14) - maxLength * 0.65F, catalystSlot.y, 0.0F);
                    RenderSystem.scalef(0.65F, 0.65F, 0.65F);
                    renderWrappedToolTip(matrixStack, results, 0, 0, this.font);
                }
                RenderSystem.popMatrix();
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\screen\CatalystDecryptionScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */