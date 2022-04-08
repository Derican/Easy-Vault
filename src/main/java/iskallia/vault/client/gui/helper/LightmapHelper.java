package iskallia.vault.client.gui.helper;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public class LightmapHelper {
    public static int getPackedFullbrightCoords() {
        return 15728880;
    }

    public static int getPackedLightCoords(int lightValue) {
        return getPackedLightCoords(lightValue, lightValue);
    }

    public static int getPackedLightCoords(int skyLight, int blockLight) {
        return skyLight << 20 | blockLight << 4;
    }

    public static int getPackedLightCoords(IBlockDisplayReader world, BlockPos at) {
        return WorldRenderer.getLightColor(world, at);
    }

    public static int getUnpackedSkyCoords(int packed) {
        return packed >> 20 & 0xF;
    }

    public static int getUnpackedBlockCoords(int packed) {
        return packed >> 4 & 0xF;
    }

    public static int getUnpackedBrightestCoords(int packed) {
        return Math.max(getUnpackedSkyCoords(packed), getUnpackedBlockCoords(packed));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\helper\LightmapHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */