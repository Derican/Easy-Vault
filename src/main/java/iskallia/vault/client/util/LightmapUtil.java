package iskallia.vault.client.util;

public class LightmapUtil {
    public static float getLightmapBrightness(final int packedLight) {
//        final DynamicTexture lightTex = Minecraft.getInstance().gameRenderer.lightTexture().lightTexture;
//        if (lightTex.getPixels() == null) {
//            return 1.0f;
//        }
//        final int block = LightmapHelper.getUnpackedBlockCoords(packedLight);
//        final int sky = LightmapHelper.getUnpackedSkyCoords(packedLight);
//        final int lightPx = lightTex.getPixels().getPixelRGBA(block, sky);
//        return (lightPx & 0xFF) / 255.0f;
        return 1.0f;
    }
}
