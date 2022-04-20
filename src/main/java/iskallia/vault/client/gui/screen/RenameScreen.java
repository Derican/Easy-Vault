package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.container.RenamingContainer;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.network.message.RenameUIMessage;
import iskallia.vault.util.RenameType;
import iskallia.vault.util.nbt.NBTSerializer;
import iskallia.vault.vending.TraderCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class RenameScreen extends ContainerScreen<RenamingContainer> {
    public static final ResourceLocation TEXTURE;
    private String name;
    private CompoundNBT data;
    private RenameType renameType;
    private Button renameButton;
    private ItemStack itemStack;
    private TraderCore core;
    private BlockPos chamberPos;
    private TextFieldWidget nameField;

    public RenameScreen(final RenamingContainer screenContainer, final PlayerInventory inv, final ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.font = Minecraft.getInstance().font;
        this.imageWidth = 172;
        this.imageHeight = 66;
        this.titleLabelX = this.imageWidth / 2;
        this.titleLabelY = 7;
        this.renameType = screenContainer.getRenameType();
        this.data = screenContainer.getNbt();
        switch (this.renameType) {
            case PLAYER_STATUE: {
                this.name = this.data.getString("PlayerNickname");
                break;
            }
            case TRADER_CORE: {
                this.itemStack = ItemStack.of(this.data);
                final CompoundNBT stackNbt = this.itemStack.getOrCreateTag();
                try {
                    this.core = NBTSerializer.deserialize(TraderCore.class, stackNbt.getCompound("core"));
                    this.name = this.core.getName();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case CRYO_CHAMBER: {
                this.chamberPos = NBTUtil.readBlockPos(this.data.getCompound("BlockPos"));
                this.name = this.data.getString("EternalName");
                break;
            }
            case VAULT_CRYSTAL: {
                this.itemStack = ItemStack.of(this.data);
                this.name = VaultCrystalItem.getData(this.itemStack).getPlayerBossName();
                break;
            }
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
        (this.nameField = new TextFieldWidget(this.font, i + 34, j + 26, 103, 12, (ITextComponent) new StringTextComponent(this.name))).setCanLoseFocus(false);
        this.nameField.setTextColor(-1);
        this.nameField.setTextColorUneditable(-1);
        this.nameField.setBordered(false);
        this.nameField.setMaxLength(16);
        this.nameField.setResponder(this::rename);
        this.children.add(this.nameField);
        this.setInitialFocus((IGuiEventListener) this.nameField);
        this.nameField.setValue(this.name);
        this.addButton((Widget) (this.renameButton = new Button(i + 31, j + 40, 110, 20, (ITextComponent) new StringTextComponent("Confirm"), this::confirmPressed)));
    }

    private void confirmPressed(final Button button) {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("RenameType", this.renameType.ordinal());
        switch (this.renameType) {
            case PLAYER_STATUE: {
                this.data.putString("PlayerNickname", this.name);
                nbt.put("Data", (INBT) this.data);
                break;
            }
            case TRADER_CORE: {
                try {
                    final CompoundNBT stackNbt = this.itemStack.getOrCreateTag();
                    this.core.setName(this.name);
                    final CompoundNBT coreNbt = NBTSerializer.serialize(this.core);
                    stackNbt.put("core", (INBT) coreNbt);
                    this.itemStack.setTag(stackNbt);
                    nbt.put("Data", (INBT) this.itemStack.serializeNBT());
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case CRYO_CHAMBER: {
                final CompoundNBT data = new CompoundNBT();
                data.put("BlockPos", (INBT) NBTUtil.writeBlockPos(this.chamberPos));
                data.putString("EternalName", this.name);
                nbt.put("Data", (INBT) data);
                break;
            }
            case VAULT_CRYSTAL: {
                VaultCrystalItem.getData(this.itemStack).setPlayerBossName(this.name);
                nbt.put("Data", (INBT) this.itemStack.serializeNBT());
                break;
            }
        }
        if (this.renameType != RenameType.PLAYER_STATUE) {
            if (this.renameType != RenameType.TRADER_CORE) {
                if (this.renameType == RenameType.CRYO_CHAMBER) {
                }
            }
        }
        ModNetwork.CHANNEL.sendToServer(RenameUIMessage.updateName(this.renameType, nbt));
        this.onClose();
    }

    private void rename(final String name) {
        if (!name.isEmpty()) {
            this.name = name;
        }
    }

    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (keyCode == 256) {
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.closeContainer();
            }
        } else if (keyCode == 257) {
            Minecraft.getInstance().getSoundManager().play((ISound) SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            this.confirmPressed(this.renameButton);
        } else if (keyCode == 69) {
            return true;
        }
        return this.nameField.keyPressed(keyCode, scanCode, modifiers) || this.nameField.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        final float midX = this.width / 2.0f;
        final float midY = this.height / 2.0f;
        this.minecraft.getTextureManager().bind(RenameScreen.TEXTURE);
        blit(matrixStack, (int) (midX - this.imageWidth / 2), (int) (midY - this.imageHeight / 2), 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
        this.renderTitle(matrixStack);
        this.renderNameField(matrixStack, mouseX, mouseY, partialTicks);
        this.renameButton.render(matrixStack, mouseX, mouseY, partialTicks);
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

    public void renderNameField(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.nameField.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    static {
        TEXTURE = new ResourceLocation("the_vault", "textures/gui/rename_screen.png");
    }
}
