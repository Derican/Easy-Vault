package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.screen.StatueCauldronScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class StatueWidget
        extends Widget {
    public static final int BUTTON_WIDTH = 88;
    public static final int BUTTON_HEIGHT = 27;
    protected StatueCauldronScreen parentScreen;
    protected String name;
    protected int count;

    public StatueWidget(int x, int y, String name, int count, StatueCauldronScreen parentScreen) {
        super(x, y, 0, 0, (ITextComponent) new StringTextComponent(""));

        this.parentScreen = parentScreen;
        this.name = name;
        this.count = count;
    }


    public boolean isHovered(int mouseX, int mouseY) {
        return (this.x <= mouseX && mouseX <= this.x + 88 && this.y <= mouseY && mouseY <= this.y + 27);
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();

        minecraft.getTextureManager().bind(StatueCauldronScreen.HUD_RESOURCE);

        boolean isSelected = this.parentScreen.getSelected().getLatestNickname().equalsIgnoreCase(this.name);
        boolean isHovered = isHovered(mouseX, mouseY);

        blit(matrixStack, this.x, this.y, 225.0F, (isHovered || isSelected) ? 68.0F : 40.0F, 88, 27, 512, 256);


        RenderSystem.disableDepthTest();
        StringTextComponent nameText = new StringTextComponent(this.name);
        float startXname = 44.0F - minecraft.font.width(nameText.getString()) / 2.0F;
        minecraft.font.draw(matrixStack, (ITextComponent) nameText, startXname, (this.y + 4), -1);

        StringTextComponent countText = new StringTextComponent("(" + this.count + ")");
        float startXcount = 44.0F - minecraft.font.width(countText.getString()) / 2.0F;
        minecraft.font.draw(matrixStack, (ITextComponent) countText, startXcount, (this.y + 14), -1);
        RenderSystem.enableDepthTest();
    }


    public String getName() {
        return this.name;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\widget\StatueWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */