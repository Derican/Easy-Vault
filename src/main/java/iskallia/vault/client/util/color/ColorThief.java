package iskallia.vault.client.util.color;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;


public class ColorThief {
    private static final int DEFAULT_QUALITY = 2;
    private static final boolean DEFAULT_IGNORE_WHITE = false;

    @Nullable
    public static int[] getColor(BufferedImage sourceImage) {
        int[][] palette = getPalette(sourceImage, 5);
        if (palette == null) {
            return null;
        }
        int[] dominantColor = palette[0];
        return dominantColor;
    }


    @Nullable
    public static int[] getColor(BufferedImage sourceImage, int quality, boolean ignoreWhite) {
        int[][] palette = getPalette(sourceImage, 5, quality, ignoreWhite);
        if (palette == null) {
            return null;
        }
        int[] dominantColor = palette[0];
        return dominantColor;
    }


    @Nullable
    public static int[][] getPalette(BufferedImage sourceImage, int colorCount) {
        MMCQ.CMap cmap = getColorMap(sourceImage, colorCount);
        if (cmap == null) {
            return (int[][]) null;
        }
        return cmap.palette();
    }


    @Nullable
    public static int[][] getPalette(BufferedImage sourceImage, int colorCount, int quality, boolean ignoreWhite) {
        MMCQ.CMap cmap = getColorMap(sourceImage, colorCount, quality, ignoreWhite);
        if (cmap == null) {
            return (int[][]) null;
        }
        return cmap.palette();
    }


    @Nullable
    public static MMCQ.CMap getColorMap(BufferedImage sourceImage, int colorCount) {
        return getColorMap(sourceImage, colorCount, 2, false);
    }


    @Nullable
    public static MMCQ.CMap getColorMap(BufferedImage sourceImage, int colorCount, int quality, boolean ignoreWhite) {
        switch (sourceImage.getType()) {
            case 5:
            case 6:
                pixelArray = getPixelsFast(sourceImage, quality, ignoreWhite);


                cmap = MMCQ.quantize(pixelArray, colorCount);
                return cmap;
        }
        int[][] pixelArray = getPixelsSlow(sourceImage, quality, ignoreWhite);
        MMCQ.CMap cmap = MMCQ.quantize(pixelArray, colorCount);
        return cmap;
    }


    private static int[][] getPixelsFast(BufferedImage sourceImage, int quality, boolean ignoreWhite) {
        int colorDepth, i;
        DataBufferByte imageData = (DataBufferByte) sourceImage.getRaster().getDataBuffer();
        byte[] pixels = imageData.getData();
        int pixelCount = sourceImage.getWidth() * sourceImage.getHeight();


        int type = sourceImage.getType();
        switch (type) {
            case 5:
                colorDepth = 3;
                break;

            case 6:
                colorDepth = 4;
                break;

            default:
                throw new IllegalArgumentException("Unhandled type: " + type);
        }

        int expectedDataLength = pixelCount * colorDepth;
        if (expectedDataLength != pixels.length) {
            throw new IllegalArgumentException("(expectedDataLength = " + expectedDataLength + ") != (pixels.length = " + pixels.length + ")");
        }


        int numRegardedPixels = (pixelCount + quality - 1) / quality;

        int numUsedPixels = 0;
        int[][] pixelArray = new int[numRegardedPixels][];


        switch (type) {
            case 5:
                for (i = 0; i < pixelCount; i += quality) {
                    int offset = i * 3;
                    int b = pixels[offset] & 0xFF;
                    int g = pixels[offset + 1] & 0xFF;
                    int r = pixels[offset + 2] & 0xFF;


                    if (!ignoreWhite || r <= 250 || g <= 250 || b <= 250) {
                        (new int[3])[0] = r;
                        (new int[3])[1] = g;
                        (new int[3])[2] = b;
                        pixelArray[numUsedPixels] = new int[3];
                        numUsedPixels++;
                    }
                }


                return Arrays.<int[]>copyOfRange(pixelArray, 0, numUsedPixels);
            case 6:
                for (i = 0; i < pixelCount; i += quality) {
                    int offset = i * 4;
                    int a = pixels[offset] & 0xFF;
                    int b = pixels[offset + 1] & 0xFF;
                    int g = pixels[offset + 2] & 0xFF;
                    int r = pixels[offset + 3] & 0xFF;
                    if (a >= 125 && (!ignoreWhite || r <= 250 || g <= 250 || b <= 250)) {
                        (new int[3])[0] = r;
                        (new int[3])[1] = g;
                        (new int[3])[2] = b;
                        pixelArray[numUsedPixels] = new int[3];
                        numUsedPixels++;
                    }
                }
                return Arrays.<int[]>copyOfRange(pixelArray, 0, numUsedPixels);
        }
        throw new IllegalArgumentException("Unhandled type: " + type);
    }


    private static int[][] getPixelsSlow(BufferedImage sourceImage, int quality, boolean ignoreWhite) {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();

        int pixelCount = width * height;


        int numRegardedPixels = (pixelCount + quality - 1) / quality;

        int numUsedPixels = 0;

        int[][] res = new int[numRegardedPixels][];

        int i;
        for (i = 0; i < pixelCount; i += quality) {
            int row = i / width;
            int col = i % width;
            int rgb = sourceImage.getRGB(col, row);

            int r = rgb >> 16 & 0xFF;
            int g = rgb >> 8 & 0xFF;
            int b = rgb & 0xFF;
            if (!ignoreWhite || r <= 250 || r <= 250 || r <= 250) {
                (new int[3])[0] = r;
                (new int[3])[1] = g;
                (new int[3])[2] = b;
                res[numUsedPixels] = new int[3];
                numUsedPixels++;
            }
        }

        return Arrays.<int[]>copyOfRange(res, 0, numUsedPixels);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\clien\\util\color\ColorThief.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */