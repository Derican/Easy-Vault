// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class TextUtil
{
    static final TextFormatting[] rainbow;
    
    public static StringTextComponent applyRainbowTo(final String text) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); ++i) {
            final char c = text.charAt(i);
            sb.append(getNextColor(i));
            sb.append(c);
        }
        return new StringTextComponent(sb.toString());
    }
    
    private static TextFormatting getNextColor(final int index) {
        return TextUtil.rainbow[index % TextUtil.rainbow.length];
    }
    
    static {
        rainbow = new TextFormatting[] { TextFormatting.RED, TextFormatting.GOLD, TextFormatting.YELLOW, TextFormatting.GREEN, TextFormatting.BLUE, TextFormatting.LIGHT_PURPLE, TextFormatting.DARK_PURPLE };
    }
}
