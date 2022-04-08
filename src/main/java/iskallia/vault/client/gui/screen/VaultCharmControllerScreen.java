package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.container.VaultCharmControllerContainer;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.VaultCharmControllerScrollMessage;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class VaultCharmControllerScreen extends ContainerScreen<VaultCharmControllerContainer> {
    private static final ResourceLocation TEXTURE = Vault.id("textures/gui/vault_charm_controller.png");

    private float scrollDelta;
    private float currentScroll;
    private boolean isScrolling;

    public VaultCharmControllerScreen(VaultCharmControllerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 195;
        this.imageHeight = 222;
    }


    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int offsetX = (this.width - this.imageWidth) / 2;
        int offsetY = (this.height - this.imageHeight) / 2;
        blit(matrixStack, offsetX, offsetY, 0, 0, this.imageWidth, this.imageHeight);
        int drawnSlots = 0;
        for (Slot slot : ((VaultCharmControllerContainer) this.menu).slots) {
            if (slot.index > 35) {
                blit(matrixStack, offsetX + slot.x - 1, offsetY + slot.y - 1, 195, 0, 18, 18);
                if (drawnSlots++ == 54)
                    return;
            }
        }
    }

    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        String title = "Charm Inscription - " + ((VaultCharmControllerContainer) this.menu).getCurrentAmountWhitelisted() + "/" + ((VaultCharmControllerContainer) this.menu).getInventorySize() + " slots";
        this.font.draw(matrixStack, (ITextComponent) new StringTextComponent(title), 5.0F, 5.0F, 4210752);
        if (needsScrollBars()) {
            this.minecraft.getTextureManager().bind(TEXTURE);
            blit(matrixStack, 175, 18 + (int) (95.0F * this.currentScroll), 195 + (needsScrollBars() ? 0 : 12), 19, 12, 15);
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

    private boolean needsScrollBars() {
        return ((VaultCharmControllerContainer) this.menu).canScroll();
    }

    private boolean scrollBarClicked(double mouseX, double mouseY) {
        int scrollLeft = this.leftPos + 175;
        int scrollTop = this.topPos + 18;
        int scrollRight = scrollLeft + 12;
        int scrollBottom = scrollTop + 110;
        return (mouseX >= scrollLeft && mouseY >= scrollTop && mouseX < scrollRight && mouseY < scrollBottom);
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (scrollBarClicked(mouseX, mouseY)) {
            this.isScrolling = true;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }


    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isScrolling = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }


    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!needsScrollBars()) {
            return false;
        }
        int i = ((VaultCharmControllerContainer) this.menu).getInventorySize() / 9 - 6;
        this.currentScroll = (float) (this.currentScroll - delta / i);
        this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
        ModNetwork.CHANNEL.sendToServer(new VaultCharmControllerScrollMessage(this.currentScroll));
        ((VaultCharmControllerContainer) this.menu).scrollTo(this.currentScroll);
        return true;
    }


    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.isScrolling) {
            int top = this.topPos + 18;
            int bottom = top + 110;


            this.currentScroll = ((float) mouseY - top - 7.5F) / ((bottom - top) - 15.0F);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);


            int intervals = ((VaultCharmControllerContainer) this.menu).getInventorySize() / 9 - 6;
            float scroll = Math.round(this.currentScroll * intervals) / intervals;

            if (scroll != this.scrollDelta) {
                ModNetwork.CHANNEL.sendToServer(new VaultCharmControllerScrollMessage(scroll));
                ((VaultCharmControllerContainer) this.menu).scrollTo(scroll);

                this.scrollDelta = scroll;
            }

            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\screen\VaultCharmControllerScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */