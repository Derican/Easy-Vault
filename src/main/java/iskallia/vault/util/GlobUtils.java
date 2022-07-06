// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobUtils
{
    public static boolean matches(final String glob, final String text) {
        final Pattern pattern = Pattern.compile("(?s)^\\Q" + glob.replace("\\E", "\\E\\\\E\\Q").replace("*", "\\E.*\\Q").replace("?", "\\E.\\Q") + "\\E$");
        final Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
