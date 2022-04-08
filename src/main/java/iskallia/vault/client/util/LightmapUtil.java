package iskallia.vault.client.util;

import iskallia.vault.client.gui.helper.LightmapHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class LightmapUtil {
    public static float getLightmapBrightness(int packedLight) {
        DynamicTexture lightTex = ((Minecraft.getInstance()).gameRenderer.lightTexture()).lightTexture;
        if (lightTex.getPixels() == null) {
            return 1.0F;
        }

        int block = LightmapHelper.getUnpackedBlockCoords(packedLight);
        int sky = LightmapHelper.getUnpackedSkyCoords(packedLight);
        int lightPx = lightTex.getPixels().getPixelRGBA(block, sky);
        return (lightPx & 0xFF) / 255.0F;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\clien\\util\LightmapUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */