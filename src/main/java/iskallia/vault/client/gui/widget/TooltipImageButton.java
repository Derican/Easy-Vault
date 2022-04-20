package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TooltipImageButton extends Button {
    private final ResourceLocation resourceLocation;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffText;
    private final int textureWidth;
    private final int textureHeight;

    public TooltipImageButton(final int xIn, final int yIn, final int widthIn, final int heightIn, final int xTexStartIn, final int yTexStartIn, final int yDiffTextIn, final ResourceLocation texture, final Button.IPressable onPressIn) {
        this(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, texture, 256, 256, onPressIn);
    }

    public TooltipImageButton(final int xIn, final int yIn, final int widthIn, final int heightIn, final int xTexStartIn, final int yTexStartIn, final int yDiffTextIn, final ResourceLocation texture, final int textureWidth, final int textureHeight, final Button.IPressable onPressIn) {
        this(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, texture, textureWidth, textureHeight, onPressIn, StringTextComponent.EMPTY);
    }

    public TooltipImageButton(final int x, final int y, final int width, final int height, final int xTexStart, final int yTexStart, final int yDiffText, final ResourceLocation texture, final int textureWidth, final int textureHeight, final Button.IPressable onPress, final ITextComponent title) {
        this(x, y, width, height, xTexStart, yTexStart, yDiffText, texture, textureWidth, textureHeight, onPress, TooltipImageButton.NO_TOOLTIP, title);
    }

    public TooltipImageButton(final int x, final int y, final int width, final int height, final int xTexStart, final int yTexStart, final int yDiffText, final ResourceLocation texture, final int textureWidth, final int textureHeight, final Button.IPressable onPress, final Button.ITooltip onTooltip, final ITextComponent title) {
        super(x, y, width, height, title, onPress, onTooltip);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yDiffText = yDiffText;
        this.resourceLocation = texture;
    }

    public void setPosition(final int xIn, final int yIn) {
        this.x = xIn;
        this.y = yIn;
    }

    public void renderButton(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }
        final Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(this.resourceLocation);
        int v = this.yTexStart;
        if (this.isHovered()) {
            v += this.yDiffText;
        }
        RenderSystem.enableDepthTest();
        blit(matrixStack, this.x, this.y, (float) this.xTexStart, (float) v, this.width, this.height, this.textureWidth, this.textureHeight);
    }
}
