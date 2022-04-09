// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.util.color;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

public class ColorThief
{
    private static final int DEFAULT_QUALITY = 2;
    private static final boolean DEFAULT_IGNORE_WHITE = false;
    
    @Nullable
    public static int[] getColor(final BufferedImage sourceImage) {
        final int[][] palette = getPalette(sourceImage, 5);
        if (palette == null) {
            return null;
        }
        final int[] dominantColor = palette[0];
        return dominantColor;
    }
    
    @Nullable
    public static int[] getColor(final BufferedImage sourceImage, final int quality, final boolean ignoreWhite) {
        final int[][] palette = getPalette(sourceImage, 5, quality, ignoreWhite);
        if (palette == null) {
            return null;
        }
        final int[] dominantColor = palette[0];
        return dominantColor;
    }
    
    @Nullable
    public static int[][] getPalette(final BufferedImage sourceImage, final int colorCount) {
        final MMCQ.CMap cmap = getColorMap(sourceImage, colorCount);
        if (cmap == null) {
            return null;
        }
        return cmap.palette();
    }
    
    @Nullable
    public static int[][] getPalette(final BufferedImage sourceImage, final int colorCount, final int quality, final boolean ignoreWhite) {
        final MMCQ.CMap cmap = getColorMap(sourceImage, colorCount, quality, ignoreWhite);
        if (cmap == null) {
            return null;
        }
        return cmap.palette();
    }
    
    @Nullable
    public static MMCQ.CMap getColorMap(final BufferedImage sourceImage, final int colorCount) {
        return getColorMap(sourceImage, colorCount, 2, false);
    }
    
    @Nullable
    public static MMCQ.CMap getColorMap(final BufferedImage sourceImage, final int colorCount, final int quality, final boolean ignoreWhite) {
        int[][] pixelArray = null;
        switch (sourceImage.getType()) {
            case 5:
            case 6: {
                pixelArray = getPixelsFast(sourceImage, quality, ignoreWhite);
                break;
            }
            default: {
                pixelArray = getPixelsSlow(sourceImage, quality, ignoreWhite);
                break;
            }
        }
        final MMCQ.CMap cmap = MMCQ.quantize(pixelArray, colorCount);
        return cmap;
    }
    
    private static int[][] getPixelsFast(final BufferedImage sourceImage, final int quality, final boolean ignoreWhite) {
        final DataBufferByte imageData = (DataBufferByte)sourceImage.getRaster().getDataBuffer();
        final byte[] pixels = imageData.getData();
        final int pixelCount = sourceImage.getWidth() * sourceImage.getHeight();
        final int type = sourceImage.getType();
        int colorDepth = 0;
        switch (type) {
            case 5: {
                colorDepth = 3;
                break;
            }
            case 6: {
                colorDepth = 4;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unhandled type: " + type);
            }
        }
        final int expectedDataLength = pixelCount * colorDepth;
        if (expectedDataLength != pixels.length) {
            throw new IllegalArgumentException("(expectedDataLength = " + expectedDataLength + ") != (pixels.length = " + pixels.length + ")");
        }
        final int numRegardedPixels = (pixelCount + quality - 1) / quality;
        int numUsedPixels = 0;
        final int[][] pixelArray = new int[numRegardedPixels][];
        switch (type) {
            case 5: {
                for (int i = 0; i < pixelCount; i += quality) {
                    final int offset = i * 3;
                    final int b = pixels[offset] & 0xFF;
                    final int g = pixels[offset + 1] & 0xFF;
                    final int r = pixels[offset + 2] & 0xFF;
                    if (!ignoreWhite || r <= 250 || g <= 250 || b <= 250) {
                        pixelArray[numUsedPixels] = new int[] { r, g, b };
                        ++numUsedPixels;
                    }
                }
                break;
            }
            case 6: {
                for (int i = 0; i < pixelCount; i += quality) {
                    final int offset = i * 4;
                    final int a = pixels[offset] & 0xFF;
                    final int b = pixels[offset + 1] & 0xFF;
                    final int g = pixels[offset + 2] & 0xFF;
                    final int r = pixels[offset + 3] & 0xFF;
                    if (a >= 125 && (!ignoreWhite || r <= 250 || g <= 250 || b <= 250)) {
                        pixelArray[numUsedPixels] = new int[] { r, g, b };
                        ++numUsedPixels;
                    }
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Unhandled type: " + type);
            }
        }
        return Arrays.copyOfRange(pixelArray, 0, numUsedPixels);
    }
    
    private static int[][] getPixelsSlow(final BufferedImage sourceImage, final int quality, final boolean ignoreWhite) {
        final int width = sourceImage.getWidth();
        final int height = sourceImage.getHeight();
        final int pixelCount = width * height;
        final int numRegardedPixels = (pixelCount + quality - 1) / quality;
        int numUsedPixels = 0;
        final int[][] res = new int[numRegardedPixels][];
        for (int i = 0; i < pixelCount; i += quality) {
            final int row = i / width;
            final int col = i % width;
            final int rgb = sourceImage.getRGB(col, row);
            final int r = rgb >> 16 & 0xFF;
            final int g = rgb >> 8 & 0xFF;
            final int b = rgb & 0xFF;
            if (!ignoreWhite || r <= 250 || r <= 250 || r <= 250) {
                res[numUsedPixels] = new int[] { r, g, b };
                ++numUsedPixels;
            }
        }
        return Arrays.copyOfRange(res, 0, numUsedPixels);
    }
}
