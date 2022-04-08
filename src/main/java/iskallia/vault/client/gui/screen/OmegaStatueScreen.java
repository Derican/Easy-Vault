package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.gui.component.StatueOptionSlot;
import iskallia.vault.container.OmegaStatueContainer;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.OmegaStatueUIMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;


public class OmegaStatueScreen
        extends ContainerScreen<OmegaStatueContainer> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("the_vault", "textures/gui/omega_statue_options.png");
    private List<StatueOptionSlot> slots = new ArrayList<>();

    List<ItemStack> items = new ArrayList<>();

    BlockPos statuePos;

    public OmegaStatueScreen(OmegaStatueContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        this.font = (Minecraft.getInstance()).font;
        this.imageWidth = 176;
        this.imageHeight = 84;

        this.titleLabelX = 88;
        this.titleLabelY = 7;

        ListNBT itemList = screenContainer.getItemsCompound();
        for (INBT nbt : itemList) {
            CompoundNBT itemNbt = (CompoundNBT) nbt;
            this.items.add(ItemStack.of(itemNbt));
        }
        this.statuePos = NBTUtil.readBlockPos(screenContainer.getBlockPos());

        int x = 0;
        int y = 29;
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                x += 44;
            } else {
                x += 18;
            }
            this.slots.add(new StatueOptionSlot(x, y, 16, 16, this.items.get(i)));
        }
    }


    protected void init() {
        super.init();
        initFields();
    }

    protected void initFields() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
    }


    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if ((keyCode == 256 || keyCode == 69) &&
                this.minecraft != null && this.minecraft.player != null) {

            ModNetwork.CHANNEL.sendToServer(OmegaStatueUIMessage.selectItem(this.items.get(0), this.statuePos));
            this.minecraft.player.closeContainer();
            return true;
        }

        return false;
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);

        float midX = this.width / 2.0F;
        float midY = this.height / 2.0F;
        this.minecraft.getTextureManager().bind(TEXTURE);
        blit(matrixStack, (int) (midX - (this.imageWidth / 2)), (int) (midY - (this.imageHeight / 2)), 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);


        renderTitle(matrixStack);
        renderText(matrixStack);

        int startX = this.width / 2 - this.imageWidth / 2;
        int startY = this.height / 2 - this.imageHeight / 2;
        for (StatueOptionSlot slot : this.slots) {
            renderItem(slot.getStack(), startX + slot.getPosX(), startY + slot.getPosY());
        }

        for (StatueOptionSlot slot : this.slots) {
            if (slot.contains((mouseX - startX), (mouseY - startY)) && !slot.getStack().isEmpty()) {
                renderTooltip(matrixStack, slot.getStack(), startX + slot.getPosX(), startY + slot.getPosY());
                break;
            }
        }
    }

    private void renderItem(ItemStack stack, int x, int y) {
        this.itemRenderer.renderGuiItem(stack, x, y);
    }


    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
    }


    private void renderTitle(MatrixStack matrixStack) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        float startX = (i + this.titleLabelX) - this.font.width(this.title.getString()) / 2.0F;
        float startY = j + this.titleLabelY;
        this.font.draw(matrixStack, this.title, startX, startY, 4210752);
    }

    private void renderText(MatrixStack matrixStack) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        StringTextComponent text = new StringTextComponent("Select an option for");
        StringTextComponent text1 = new StringTextComponent("the statue to generate.");
        float startTextX = i + this.imageWidth / 2.0F - this.font.width(text.getString()) / 2.0F;
        float startTextY = j + 59.0F;
        this.font.draw(matrixStack, (ITextComponent) text, startTextX, startTextY, 4210752);

        float startText1X = (i + this.titleLabelX) - this.font.width(text1.getString()) / 2.0F;
        float startText1Y = j + 56.0F + 13.0F;
        this.font.draw(matrixStack, (ITextComponent) text1, startText1X, startText1Y, 4210752);
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int translatedX = (int) Math.max(0.0D, mouseX - getGuiLeft());
        int translatedY = (int) Math.max(0.0D, mouseY - getGuiTop());
        StatueOptionSlot slot = getClickedSlot(translatedX, translatedY);
        if (slot != null) {
            ModNetwork.CHANNEL.sendToServer(OmegaStatueUIMessage.selectItem(slot.getStack(), this.statuePos));
            onClose();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private StatueOptionSlot getClickedSlot(int x, int y) {
        if (y < 29 || y > 45) return null;
        for (StatueOptionSlot slot : this.slots) {
            if (x >= slot.getPosX() && x <= slot.getPosX() + 16) return slot;
        }
        return null;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\screen\OmegaStatueScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */