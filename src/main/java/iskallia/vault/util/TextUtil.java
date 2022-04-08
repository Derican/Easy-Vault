package iskallia.vault.util;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class TextUtil {
    static final TextFormatting[] rainbow = new TextFormatting[]{TextFormatting.RED, TextFormatting.GOLD, TextFormatting.YELLOW, TextFormatting.GREEN, TextFormatting.BLUE, TextFormatting.LIGHT_PURPLE, TextFormatting.DARK_PURPLE};


    public static StringTextComponent applyRainbowTo(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            sb.append(getNextColor(i));
            sb.append(c);
        }
        return new StringTextComponent(sb.toString());
    }

    private static TextFormatting getNextColor(int index) {
        return rainbow[index % rainbow.length];
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\TextUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */