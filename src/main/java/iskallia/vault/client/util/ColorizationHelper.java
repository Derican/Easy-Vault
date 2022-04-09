// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.util;

import iskallia.vault.Vault;
import iskallia.vault.client.util.color.ColorThief;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ColorizationHelper
{
    private static final Random rand;
    private static final Map<Item, Optional<Color>> itemColors;
    
    private ColorizationHelper() {
    }
    
    @Nonnull
    public static Optional<Color> getColor(final ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }
        final Optional<Color> override = getCustomColorOverride(stack);
        if (override.isPresent()) {
            return override;
        }
        final Item i = stack.getItem();
        if (!ColorizationHelper.itemColors.containsKey(i)) {
            final TextureAtlasSprite tas = getParticleTexture(stack);
            if (tas != null) {
                ColorizationHelper.itemColors.put(i, getDominantColor(tas));
            }
            else {
                ColorizationHelper.itemColors.put(i, Optional.empty());
            }
        }
        return ColorizationHelper.itemColors.get(i).map(c -> MiscUtils.overlayColor(c, new Color(MiscUtils.getOverlayColor(stack))));
    }
    
    public static Optional<Color> getCustomColorOverride(final ItemStack stack) {
        final Item i = stack.getItem();
        if (i == ModItems.VAULT_PLATINUM) {
            return Optional.of(new Color(16705664));
        }
        if (i == ModItems.BANISHED_SOUL) {
            return Optional.of(new Color(9972223));
        }
        if (i instanceof VaultGear) {
            return Optional.of(Color.getHSBColor(ColorizationHelper.rand.nextFloat(), 1.0f, 1.0f));
        }
        return Optional.empty();
    }
    
    @Nullable
    private static TextureAtlasSprite getParticleTexture(final ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        final ItemModelMesher imm = Minecraft.getInstance().getItemRenderer().getItemModelShaper();
        final IBakedModel mdl = imm.getItemModel(stack);
        if (mdl.equals(imm.getModelManager().getMissingModel())) {
            return null;
        }
        return mdl.getParticleTexture((IModelData)EmptyModelData.INSTANCE);
    }
    
    private static Optional<Color> getDominantColor(final TextureAtlasSprite tas) {
        if (tas == null) {
            return Optional.empty();
        }
        try {
            final BufferedImage extractedImage = extractImage(tas);
            final int[] dominantColor = ColorThief.getColor(extractedImage);
            final int color = (dominantColor[0] & 0xFF) << 16 | (dominantColor[1] & 0xFF) << 8 | (dominantColor[2] & 0xFF);
            return Optional.of(new Color(color));
        }
        catch (final Exception exc) {
            Vault.LOGGER.error("Item Colorization Helper: Ignoring non-resolvable image " + tas.getName().toString());
            exc.printStackTrace();
            return Optional.empty();
        }
    }
    
    @Nullable
    private static BufferedImage extractImage(final TextureAtlasSprite tas) {
        final int w = tas.getWidth();
        final int h = tas.getHeight();
        final int count = tas.getFrameCount();
        if (w <= 0 || h <= 0 || count <= 0) {
            return null;
        }
        final BufferedImage bufferedImage = new BufferedImage(w, h * count, 6);
        for (int i = 0; i < count; ++i) {
            final int[] pxArray = new int[tas.getWidth() * tas.getHeight()];
            for (int xx = 0; xx < tas.getWidth(); ++xx) {
                for (int zz = 0; zz < tas.getHeight(); ++zz) {
                    final int argb = tas.getPixelRGBA(0, xx, zz + i * tas.getHeight());
                    pxArray[zz * tas.getWidth() + xx] = ((argb & 0xFF00FF00) | (argb & 0xFF0000) >> 16 | (argb & 0xFF) << 16);
                }
            }
            bufferedImage.setRGB(0, i * h, w, h, pxArray, 0, w);
        }
        return bufferedImage;
    }
    
    static {
        rand = new Random();
        itemColors = new HashMap<Item, Optional<Color>>();
    }
}
