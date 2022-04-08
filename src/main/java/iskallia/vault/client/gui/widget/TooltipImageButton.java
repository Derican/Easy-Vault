package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TooltipImageButton
        extends Button {
    private final ResourceLocation resourceLocation;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffText;
    private final int textureWidth;
    private final int textureHeight;

    public TooltipImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation texture, Button.IPressable onPressIn) {
        this(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, texture, 256, 256, onPressIn);
    }

    public TooltipImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation texture, int textureWidth, int textureHeight, Button.IPressable onPressIn) {
        this(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, texture, textureWidth, textureHeight, onPressIn, StringTextComponent.EMPTY);
    }

    public TooltipImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffText, ResourceLocation texture, int textureWidth, int textureHeight, Button.IPressable onPress, ITextComponent title) {
        this(x, y, width, height, xTexStart, yTexStart, yDiffText, texture, textureWidth, textureHeight, onPress, NO_TOOLTIP, title);
    }

    public TooltipImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffText, ResourceLocation texture, int textureWidth, int textureHeight, Button.IPressable onPress, Button.ITooltip onTooltip, ITextComponent title) {
        super(x, y, width, height, title, onPress, onTooltip);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yDiffText = yDiffText;
        this.resourceLocation = texture;
    }

    public void setPosition(int xIn, int yIn) {
        this.x = xIn;
        this.y = yIn;
    }

    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (isHovered()) {
            renderToolTip(matrixStack, mouseX, mouseY);
        }

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(this.resourceLocation);
        int v = this.yTexStart;
        if (isHovered()) {
            v += this.yDiffText;
        }

        RenderSystem.enableDepthTest();
        blit(matrixStack, this.x, this.y, this.xTexStart, v, this.width, this.height, this.textureWidth, this.textureHeight);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\widget\TooltipImageButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */