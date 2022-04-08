package iskallia.vault.client.gui.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;

public class FontHelper {
    public static float drawStringWithBorder(MatrixStack matrixStack, String text, float x, float y, int color, int borderColor) {
        return drawStringWithBorder(matrixStack, (ITextComponent) new StringTextComponent(text), x, y, color, borderColor);
    }

    public static float drawStringWithBorder(MatrixStack matrixStack, ITextComponent text, float x, float y, int color, int borderColor) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.font.draw(matrixStack, text, x - 1.0F, y, borderColor);
        minecraft.font.draw(matrixStack, text, x + 1.0F, y, borderColor);
        minecraft.font.draw(matrixStack, text, x, y - 1.0F, borderColor);
        minecraft.font.draw(matrixStack, text, x, y + 1.0F, borderColor);
        return (minecraft.font.draw(matrixStack, text, x, y, color) + 1);
    }

    public static int drawTextComponent(MatrixStack matrixStack, ITextComponent component, boolean rightAligned) {
        FontRenderer fontRenderer = (Minecraft.getInstance()).font;
        int width = fontRenderer.width((ITextProperties) component);
        fontRenderer.drawShadow(matrixStack, component, rightAligned ? -width : 0.0F, 0.0F, -1052689);
        return width;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\helper\FontHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */