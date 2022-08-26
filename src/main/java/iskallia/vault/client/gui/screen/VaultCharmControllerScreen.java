package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.container.VaultCharmControllerContainer;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.VaultCharmControllerScrollMessage;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class VaultCharmControllerScreen extends ContainerScreen<VaultCharmControllerContainer> {
    private static final ResourceLocation TEXTURE;
    private float scrollDelta;
    private float currentScroll;
    private boolean isScrolling;

    public VaultCharmControllerScreen(final VaultCharmControllerContainer screenContainer, final PlayerInventory inv, final ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 195;
        this.imageHeight = 222;
    }

    protected void renderBg(final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(VaultCharmControllerScreen.TEXTURE);
        final int offsetX = (this.width - this.imageWidth) / 2;
        final int offsetY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, offsetX, offsetY, 0, 0, this.imageWidth, this.imageHeight);
        int drawnSlots = 0;
        for (final Slot slot : this.menu.slots) {
            if (slot.index > 35) {
                this.blit(matrixStack, offsetX + slot.x - 1, offsetY + slot.y - 1, 195, 0, 18, 18);
                if (drawnSlots++ == 54) {
                    return;
                }
                continue;
            }
        }
    }

    protected void renderLabels(final MatrixStack matrixStack, final int x, final int y) {
        final String title = "Charm Inscription - " + this.menu.getCurrentAmountWhitelisted() + "/" + this.menu.getInventorySize() + " slots";
        this.font.draw(matrixStack, new StringTextComponent(title), 5.0f, 5.0f, 4210752);
        if (this.needsScrollBars()) {
            this.minecraft.getTextureManager().bind(VaultCharmControllerScreen.TEXTURE);
            this.blit(matrixStack, 175, 18 + (int) (95.0f * this.currentScroll), 195 + (this.needsScrollBars() ? 0 : 12), 19, 12, 15);
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

    private boolean needsScrollBars() {
        return this.menu.canScroll();
    }

    private boolean scrollBarClicked(final double mouseX, final double mouseY) {
        final int scrollLeft = this.leftPos + 175;
        final int scrollTop = this.topPos + 18;
        final int scrollRight = scrollLeft + 12;
        final int scrollBottom = scrollTop + 110;
        return mouseX >= scrollLeft && mouseY >= scrollTop && mouseX < scrollRight && mouseY < scrollBottom;
    }

    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.scrollBarClicked(mouseX, mouseY)) {
            return this.isScrolling = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        this.isScrolling = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseScrolled(final double mouseX, final double mouseY, final double delta) {
        if (!this.needsScrollBars()) {
            return false;
        }
        final int i = this.menu.getInventorySize() / 9 - 6;
        this.currentScroll -= (float) (delta / i);
        this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0f, 1.0f);
        ModNetwork.CHANNEL.sendToServer(new VaultCharmControllerScrollMessage(this.currentScroll));
        this.menu.scrollTo(this.currentScroll);
        return true;
    }

    public boolean mouseDragged(final double mouseX, final double mouseY, final int button, final double dragX, final double dragY) {
        if (this.isScrolling) {
            final int top = this.topPos + 18;
            final int bottom = top + 110;
            this.currentScroll = ((float) mouseY - top - 7.5f) / (bottom - top - 15.0f);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0f, 1.0f);
            final int intervals = this.menu.getInventorySize() / 9 - 6;
            final float scroll = Math.round(this.currentScroll * intervals) / (float) intervals;
            if (scroll != this.scrollDelta) {
                ModNetwork.CHANNEL.sendToServer(new VaultCharmControllerScrollMessage(scroll));
                this.menu.scrollTo(scroll);
                this.scrollDelta = scroll;
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    static {
        TEXTURE = Vault.id("textures/gui/vault_charm_controller.png");
    }
}
