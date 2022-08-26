package iskallia.vault.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.widget.DifficultyButton;
import iskallia.vault.container.GlobalDifficultyContainer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.GlobalDifficultyMessage;
import iskallia.vault.world.data.GlobalDifficultyData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class GlobalDifficultyScreen extends ContainerScreen<GlobalDifficultyContainer> {
    private static final ResourceLocation TEXTURE;
    private DifficultyButton vaultDifficultyButton;
    private DifficultyButton crystalCostButton;
    private Button confirmButton;
    private final int buttonWidth = 168;
    private final int buttonHeight = 20;
    private final int buttonPadding = 5;
    private int buttonStartX;
    private int buttonStartY;

    public GlobalDifficultyScreen(final GlobalDifficultyContainer container, final PlayerInventory inventory, final ITextComponent title) {
        super(container, inventory, title);
        this.font = Minecraft.getInstance().font;
        this.imageWidth = 190;
        this.imageHeight = 256;
        this.titleLabelX = this.imageWidth / 2;
        this.titleLabelY = 7;
    }

    protected void init() {
        super.init();
        final int centerX = this.leftPos + this.imageWidth / 2;
        this.buttonStartX = centerX - 84;
        final int guiBottom = this.topPos + this.imageHeight;
        this.buttonStartY = guiBottom - 75 - 8;
        this.initializeFields();
    }

    public void render(@Nonnull final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        Minecraft.getInstance().getTextureManager().bind(GlobalDifficultyScreen.TEXTURE);
        final float midX = this.width / 2.0f;
        final float midY = this.height / 2.0f;
        blit(matrixStack, (int) (midX - this.imageWidth / 2), (int) (midY - this.imageHeight / 2), 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
        this.vaultDifficultyButton.render(matrixStack, mouseX, mouseY, partialTicks);
        this.crystalCostButton.render(matrixStack, mouseX, mouseY, partialTicks);
        this.confirmButton.render(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.pushPose();
        matrixStack.translate(this.leftPos + 5, this.topPos + 27, 0.0);
        UIHelper.renderWrappedText(matrixStack, ModConfigs.DIFFICULTY_DESCRIPTION.getDescription(), this.imageWidth - 10, 5);
        matrixStack.popPose();
        this.renderTitle(matrixStack);
    }

    protected void renderBg(@Nonnull final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
    }

    protected void renderLabels(final MatrixStack matrixStack, final int mouseX, final int mouseY) {
        this.buttons.forEach(button -> {
            if (button.isHovered()) {
                button.renderToolTip(matrixStack, mouseX, mouseY);
            }
        });
    }

    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        return false;
    }

    public boolean isPauseScreen() {
        return true;
    }

    public boolean mouseClicked(final double mouseX, final double mouseY, final int mouseButton) {
        return !this.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, mouseButton) && super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void initializeFields() {
        final GlobalDifficultyData.Difficulty vaultDifficulty = GlobalDifficultyData.Difficulty.values()[this.menu.getData().getInt("VaultDifficulty")];
        this.vaultDifficultyButton = new DifficultyButton("Vault Mob Difficulty", "VaultDifficulty", this.buttonStartX, this.buttonStartY, 168, 20, new StringTextComponent("Vault Mob Difficulty: " + vaultDifficulty.toString()), this::buttonPressed);
        final GlobalDifficultyData.Difficulty crystalCost = GlobalDifficultyData.Difficulty.values()[this.menu.getData().getInt("CrystalCost")];
        this.crystalCostButton = new DifficultyButton("Crystal Cost Multiplier", "CrystalCost", this.buttonStartX, this.vaultDifficultyButton.y + 20 + 5, 168, 20, new StringTextComponent("Crystal Cost Multiplier: " + crystalCost.toString()), this::buttonPressed);
        this.confirmButton = new Button(this.buttonStartX, this.crystalCostButton.y + 20 + 5, 168, 20, new StringTextComponent("Confirm"), this::buttonPressed);
        this.addButton((Widget) this.vaultDifficultyButton);
        this.addButton((Widget) this.crystalCostButton);
        this.addButton((Widget) this.confirmButton);
    }

    private void buttonPressed(final Button button) {
        if (button instanceof DifficultyButton) {
            final DifficultyButton difficultyButton = (DifficultyButton) button;
            difficultyButton.getNextOption();
            this.selectDifficulty(difficultyButton.getKey(), difficultyButton.getCurrentOption());
        } else {
            ModNetwork.CHANNEL.sendToServer(GlobalDifficultyMessage.setGlobalDifficultyOptions(this.menu.getData()));
            this.onClose();
        }
    }

    public void selectDifficulty(final String key, final GlobalDifficultyData.Difficulty selected) {
        this.menu.getData().putInt(key, selected.ordinal());
    }

    private void renderTitle(final MatrixStack matrixStack) {
        final int i = (this.width - this.imageWidth) / 2;
        final int j = (this.height - this.imageHeight) / 2;
        final float startX = i + this.titleLabelX - this.font.width(this.title.getString()) / 2.0f;
        final float startY = j + (float) this.titleLabelY;
        this.font.draw(matrixStack, this.title, startX, startY, 4210752);
    }

    static {
        TEXTURE = new ResourceLocation("the_vault", "textures/gui/global_difficulty_screen.png");
    }
}
