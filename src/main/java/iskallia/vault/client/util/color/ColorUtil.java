// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.util.color;

import java.awt.*;

public class ColorUtil
{
    private ColorUtil() {
    }
    
    public static double fastPerceptualColorDistanceSquared(final int[] color1, final int[] color2) {
        final int red1 = color1[0];
        final int red2 = color2[0];
        final int redMean = red1 + red2 >> 1;
        final int r = red1 - red2;
        final int g = color1[1] - color2[1];
        final int b = color1[2] - color2[2];
        return ((512 + redMean) * r * r >> 8) + 4 * g * g + ((767 - redMean) * b * b >> 8);
    }
    
    public static double slowPerceptualColorDistanceSquared(final int[] color1, final int[] color2) {
        final double colorDistance = fastPerceptualColorDistanceSquared(color1, color2);
        final double grey1 = (color1[0] + color1[1] + color1[2]) / 3;
        final double grey2 = (color2[0] + color2[1] + color2[2]) / 3;
        final double greyDistance1 = Math.abs(grey1 - color1[0]) + Math.abs(grey1 - color1[1]) + Math.abs(grey1 - color1[2]);
        final double greyDistance2 = Math.abs(grey2 - color2[0]) + Math.abs(grey2 - color2[1]) + Math.abs(grey2 - color2[2]);
        final double greyDistance3 = greyDistance1 - greyDistance2;
        return colorDistance + greyDistance3 * greyDistance3 / 10.0;
    }
    
    public static double slowPerceptualColorDistanceSquared(final Color color1, final Color color2) {
        final int[] colorInts1 = { color1.getRed(), color1.getGreen(), color1.getBlue() };
        final int[] colorInts2 = { color2.getRed(), color2.getGreen(), color2.getBlue() };
        return slowPerceptualColorDistanceSquared(colorInts1, colorInts2);
    }
}
