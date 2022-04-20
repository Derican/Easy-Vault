package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.screen.StatueCauldronScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class StatueWidget extends Widget {
    public static final int BUTTON_WIDTH = 88;
    public static final int BUTTON_HEIGHT = 27;
    protected StatueCauldronScreen parentScreen;
    protected String name;
    protected int count;

    public StatueWidget(final int x, final int y, final String name, final int count, final StatueCauldronScreen parentScreen) {
        super(x, y, 0, 0, (ITextComponent) new StringTextComponent(""));
        this.parentScreen = parentScreen;
        this.name = name;
        this.count = count;
    }

    public boolean isHovered(final int mouseX, final int mouseY) {
        return this.x <= mouseX && mouseX <= this.x + 88 && this.y <= mouseY && mouseY <= this.y + 27;
    }

    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(StatueCauldronScreen.HUD_RESOURCE);
        final boolean isSelected = this.parentScreen.getSelected().getLatestNickname().equalsIgnoreCase(this.name);
        final boolean isHovered = this.isHovered(mouseX, mouseY);
        blit(matrixStack, this.x, this.y, 225.0f, (isHovered || isSelected) ? 68.0f : 40.0f, 88, 27, 512, 256);
        RenderSystem.disableDepthTest();
        final StringTextComponent nameText = new StringTextComponent(this.name);
        final float startXname = 44.0f - minecraft.font.width(nameText.getString()) / 2.0f;
        minecraft.font.draw(matrixStack, (ITextComponent) nameText, startXname, (float) (this.y + 4), -1);
        final StringTextComponent countText = new StringTextComponent("(" + this.count + ")");
        final float startXcount = 44.0f - minecraft.font.width(countText.getString()) / 2.0f;
        minecraft.font.draw(matrixStack, (ITextComponent) countText, startXcount, (float) (this.y + 14), -1);
        RenderSystem.enableDepthTest();
    }

    public String getName() {
        return this.name;
    }
}
