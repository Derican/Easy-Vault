package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.gui.component.StatueOptionSlot;
import iskallia.vault.container.OmegaStatueContainer;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.OmegaStatueUIMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
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

public class OmegaStatueScreen extends ContainerScreen<OmegaStatueContainer> {
    public static final ResourceLocation TEXTURE;
    private final List<StatueOptionSlot> slots;
    List<ItemStack> items;
    BlockPos statuePos;

    public OmegaStatueScreen(final OmegaStatueContainer screenContainer, final PlayerInventory inv, final ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.slots = new ArrayList<StatueOptionSlot>();
        this.items = new ArrayList<ItemStack>();
        this.font = Minecraft.getInstance().font;
        this.imageWidth = 176;
        this.imageHeight = 84;
        this.titleLabelX = 88;
        this.titleLabelY = 7;
        final ListNBT itemList = screenContainer.getItemsCompound();
        for (final INBT nbt : itemList) {
            final CompoundNBT itemNbt = (CompoundNBT) nbt;
            this.items.add(ItemStack.of(itemNbt));
        }
        this.statuePos = NBTUtil.readBlockPos(screenContainer.getBlockPos());
        int x = 0;
        final int y = 29;
        for (int i = 0; i < 5; ++i) {
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
        this.initFields();
    }

    protected void initFields() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        final int i = (this.width - this.imageWidth) / 2;
        final int j = (this.height - this.imageHeight) / 2;
    }

    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if ((keyCode == 256 || keyCode == 69) && this.minecraft != null && this.minecraft.player != null) {
            ModNetwork.CHANNEL.sendToServer(OmegaStatueUIMessage.selectItem(this.items.get(0), this.statuePos));
            this.minecraft.player.closeContainer();
            return true;
        }
        return false;
    }

    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        final float midX = this.width / 2.0f;
        final float midY = this.height / 2.0f;
        this.minecraft.getTextureManager().bind(OmegaStatueScreen.TEXTURE);
        blit(matrixStack, (int) (midX - this.imageWidth / 2), (int) (midY - this.imageHeight / 2), 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
        this.renderTitle(matrixStack);
        this.renderText(matrixStack);
        final int startX = this.width / 2 - this.imageWidth / 2;
        final int startY = this.height / 2 - this.imageHeight / 2;
        for (final StatueOptionSlot slot : this.slots) {
            this.renderItem(slot.getStack(), startX + slot.getPosX(), startY + slot.getPosY());
        }
        for (final StatueOptionSlot slot : this.slots) {
            if (slot.contains(mouseX - startX, mouseY - startY) && !slot.getStack().isEmpty()) {
                this.renderTooltip(matrixStack, slot.getStack(), startX + slot.getPosX(), startY + slot.getPosY());
                break;
            }
        }
    }

    private void renderItem(final ItemStack stack, final int x, final int y) {
        this.itemRenderer.renderGuiItem(stack, x, y);
    }

    protected void renderBg(final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
    }

    private void renderTitle(final MatrixStack matrixStack) {
        final int i = (this.width - this.imageWidth) / 2;
        final int j = (this.height - this.imageHeight) / 2;
        final float startX = i + this.titleLabelX - this.font.width(this.title.getString()) / 2.0f;
        final float startY = j + (float) this.titleLabelY;
        this.font.draw(matrixStack, this.title, startX, startY, 4210752);
    }

    private void renderText(final MatrixStack matrixStack) {
        final int i = (this.width - this.imageWidth) / 2;
        final int j = (this.height - this.imageHeight) / 2;
        final StringTextComponent text = new StringTextComponent("Select an option for");
        final StringTextComponent text2 = new StringTextComponent("the statue to generate.");
        final float startTextX = i + this.imageWidth / 2.0f - this.font.width(text.getString()) / 2.0f;
        final float startTextY = j + 59.0f;
        this.font.draw(matrixStack, text, startTextX, startTextY, 4210752);
        final float startText1X = i + this.titleLabelX - this.font.width(text2.getString()) / 2.0f;
        final float startText1Y = j + 56.0f + 13.0f;
        this.font.draw(matrixStack, text2, startText1X, startText1Y, 4210752);
    }

    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        final int translatedX = (int) Math.max(0.0, mouseX - this.getGuiLeft());
        final int translatedY = (int) Math.max(0.0, mouseY - this.getGuiTop());
        final StatueOptionSlot slot = this.getClickedSlot(translatedX, translatedY);
        if (slot != null) {
            ModNetwork.CHANNEL.sendToServer(OmegaStatueUIMessage.selectItem(slot.getStack(), this.statuePos));
            this.onClose();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private StatueOptionSlot getClickedSlot(final int x, final int y) {
        if (y < 29 || y > 45) {
            return null;
        }
        for (final StatueOptionSlot slot : this.slots) {
            if (x >= slot.getPosX() && x <= slot.getPosX() + 16) {
                return slot;
            }
        }
        return null;
    }

    static {
        TEXTURE = new ResourceLocation("the_vault", "textures/gui/omega_statue_options.png");
    }
}
