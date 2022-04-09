// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;

public class FontHelper
{
    public static float drawStringWithBorder(final MatrixStack matrixStack, final String text, final float x, final float y, final int color, final int borderColor) {
        return drawStringWithBorder(matrixStack, (ITextComponent)new StringTextComponent(text), x, y, color, borderColor);
    }
    
    public static float drawStringWithBorder(final MatrixStack matrixStack, final ITextComponent text, final float x, final float y, final int color, final int borderColor) {
        final Minecraft minecraft = Minecraft.getInstance();
        minecraft.font.draw(matrixStack, text, x - 1.0f, y, borderColor);
        minecraft.font.draw(matrixStack, text, x + 1.0f, y, borderColor);
        minecraft.font.draw(matrixStack, text, x, y - 1.0f, borderColor);
        minecraft.font.draw(matrixStack, text, x, y + 1.0f, borderColor);
        return (float)(minecraft.font.draw(matrixStack, text, x, y, color) + 1);
    }
    
    public static int drawTextComponent(final MatrixStack matrixStack, final ITextComponent component, final boolean rightAligned) {
        final FontRenderer fontRenderer = Minecraft.getInstance().font;
        final int width = fontRenderer.width((ITextProperties)component);
        fontRenderer.drawShadow(matrixStack, component, rightAligned ? ((float)(-width)) : 0.0f, 0.0f, -1052689);
        return width;
    }
}
