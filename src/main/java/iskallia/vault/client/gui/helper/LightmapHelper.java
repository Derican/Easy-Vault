// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.helper;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public class LightmapHelper
{
    public static int getPackedFullbrightCoords() {
        return 15728880;
    }
    
    public static int getPackedLightCoords(final int lightValue) {
        return getPackedLightCoords(lightValue, lightValue);
    }
    
    public static int getPackedLightCoords(final int skyLight, final int blockLight) {
        return skyLight << 20 | blockLight << 4;
    }
    
    public static int getPackedLightCoords(final IBlockDisplayReader world, final BlockPos at) {
        return WorldRenderer.getLightColor(world, at);
    }
    
    public static int getUnpackedSkyCoords(final int packed) {
        return packed >> 20 & 0xF;
    }
    
    public static int getUnpackedBlockCoords(final int packed) {
        return packed >> 4 & 0xF;
    }
    
    public static int getUnpackedBrightestCoords(final int packed) {
        return Math.max(getUnpackedSkyCoords(packed), getUnpackedBlockCoords(packed));
    }
}
