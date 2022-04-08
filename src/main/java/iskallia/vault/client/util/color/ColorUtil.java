package iskallia.vault.client.util.color;

import java.awt.*;


public class ColorUtil {
    public static double fastPerceptualColorDistanceSquared(int[] color1, int[] color2) {
        int red1 = color1[0];
        int red2 = color2[0];
        int redMean = red1 + red2 >> 1;
        int r = red1 - red2;
        int g = color1[1] - color2[1];
        int b = color1[2] - color2[2];
        return (((512 + redMean) * r * r >> 8) + 4 * g * g + ((767 - redMean) * b * b >> 8));
    }


    public static double slowPerceptualColorDistanceSquared(int[] color1, int[] color2) {
        double colorDistance = fastPerceptualColorDistanceSquared(color1, color2);

        double grey1 = ((color1[0] + color1[1] + color1[2]) / 3);
        double grey2 = ((color2[0] + color2[1] + color2[2]) / 3);
        double greyDistance1 = Math.abs(grey1 - color1[0]) + Math.abs(grey1 - color1[1]) + Math.abs(grey1 - color1[2]);
        double greyDistance2 = Math.abs(grey2 - color2[0]) + Math.abs(grey2 - color2[1]) + Math.abs(grey2 - color2[2]);
        double greyDistance = greyDistance1 - greyDistance2;

        return colorDistance + greyDistance * greyDistance / 10.0D;
    }

    public static double slowPerceptualColorDistanceSquared(Color color1, Color color2) {
        int[] colorInts1 = {color1.getRed(), color1.getGreen(), color1.getBlue()};
        int[] colorInts2 = {color2.getRed(), color2.getGreen(), color2.getBlue()};
        return slowPerceptualColorDistanceSquared(colorInts1, colorInts2);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\clien\\util\color\ColorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */