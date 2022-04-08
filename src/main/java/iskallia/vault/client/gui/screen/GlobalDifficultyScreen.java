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
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class GlobalDifficultyScreen
        extends ContainerScreen<GlobalDifficultyContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("the_vault", "textures/gui/global_difficulty_screen.png");

    private DifficultyButton vaultDifficultyButton;

    private DifficultyButton crystalCostButton;
    private Button confirmButton;
    private final int buttonWidth = 168;
    private final int buttonHeight = 20;
    private final int buttonPadding = 5;
    private int buttonStartX;
    private int buttonStartY;

    public GlobalDifficultyScreen(GlobalDifficultyContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.font = (Minecraft.getInstance()).font;

        this.imageWidth = 190;
        this.imageHeight = 256;

        this.titleLabelX = this.imageWidth / 2;
        this.titleLabelY = 7;
    }


    protected void init() {
        super.init();

        int centerX = this.leftPos + this.imageWidth / 2;

        this.buttonStartX = centerX - 84;
        int guiBottom = this.topPos + this.imageHeight;
        this.buttonStartY = guiBottom - 75 - 8;

        initializeFields();
    }


    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);

        Minecraft.getInstance().getTextureManager().bind(TEXTURE);

        float midX = this.width / 2.0F;
        float midY = this.height / 2.0F;
        blit(matrixStack, (int) (midX - (this.imageWidth / 2)), (int) (midY - (this.imageHeight / 2)), 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);


        this.vaultDifficultyButton.render(matrixStack, mouseX, mouseY, partialTicks);
        this.crystalCostButton.render(matrixStack, mouseX, mouseY, partialTicks);
        this.confirmButton.render(matrixStack, mouseX, mouseY, partialTicks);

        matrixStack.pushPose();
        matrixStack.translate((this.leftPos + 5), (this.topPos + 27), 0.0D);
        UIHelper.renderWrappedText(matrixStack, (ITextComponent) ModConfigs.DIFFICULTY_DESCRIPTION.getDescription(), this.imageWidth - 10, 5);
        matrixStack.popPose();
        renderTitle(matrixStack);
    }


    protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
    }


    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.buttons.forEach(button -> {
            if (button.isHovered()) {
                button.renderToolTip(matrixStack, mouseX, mouseY);
            }
        });
    }


    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }


    public boolean isPauseScreen() {
        return true;
    }


    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, mouseButton)) {
            return false;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void initializeFields() {
        GlobalDifficultyData.Difficulty vaultDifficulty = GlobalDifficultyData.Difficulty.values()[((GlobalDifficultyContainer) this.menu).getData().getInt("VaultDifficulty")];
        this


                .vaultDifficultyButton = new DifficultyButton("Vault Mob Difficulty", "VaultDifficulty", this.buttonStartX, this.buttonStartY, 168, 20, (ITextComponent) new StringTextComponent("Vault Mob Difficulty: " + vaultDifficulty.toString()), this::buttonPressed);


        GlobalDifficultyData.Difficulty crystalCost = GlobalDifficultyData.Difficulty.values()[((GlobalDifficultyContainer) this.menu).getData().getInt("CrystalCost")];
        this


                .crystalCostButton = new DifficultyButton("Crystal Cost Multiplier", "CrystalCost", this.buttonStartX, this.vaultDifficultyButton.y + 20 + 5, 168, 20, (ITextComponent) new StringTextComponent("Crystal Cost Multiplier: " + crystalCost.toString()), this::buttonPressed);


        this.confirmButton = new Button(this.buttonStartX, this.crystalCostButton.y + 20 + 5, 168, 20, (ITextComponent) new StringTextComponent("Confirm"), this::buttonPressed);


        addButton((Widget) this.vaultDifficultyButton);
        addButton((Widget) this.crystalCostButton);
        addButton((Widget) this.confirmButton);
    }

    private void buttonPressed(Button button) {
        if (button instanceof DifficultyButton) {
            DifficultyButton difficultyButton = (DifficultyButton) button;
            difficultyButton.getNextOption();
            selectDifficulty(difficultyButton.getKey(), difficultyButton.getCurrentOption());
        } else {
            ModNetwork.CHANNEL.sendToServer(GlobalDifficultyMessage.setGlobalDifficultyOptions(((GlobalDifficultyContainer) this.menu).getData()));
            onClose();
        }
    }

    public void selectDifficulty(String key, GlobalDifficultyData.Difficulty selected) {
        ((GlobalDifficultyContainer) this.menu).getData().putInt(key, selected.ordinal());
    }

    private void renderTitle(MatrixStack matrixStack) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        float startX = (i + this.titleLabelX) - this.font.width(this.title.getString()) / 2.0F;
        float startY = j + this.titleLabelY;
        this.font.draw(matrixStack, this.title, startX, startY, 4210752);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\screen\GlobalDifficultyScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */