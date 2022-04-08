package iskallia.vault.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.attribute.FloatAttribute;
import iskallia.vault.attribute.IntegerAttribute;
import iskallia.vault.init.ModAttributes;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


public abstract class MixinItemRenderer {
    private void render(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text, CallbackInfo ci) {
        if (!ModAttributes.GEAR_MAX_LEVEL.exists(stack)) {
            return;
        }

        RenderSystem.disableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        float progress = ((Integer) ((IntegerAttribute) ModAttributes.GEAR_MAX_LEVEL.getOrDefault(stack, Integer.valueOf(1))).getValue(stack)).intValue();
        progress = (progress - ((Float) ((FloatAttribute) ModAttributes.GEAR_LEVEL.getOrDefault(stack, Float.valueOf(0.0F))).getValue(stack)).floatValue()) / progress;
        progress = MathHelper.clamp(progress, 0.0F, 1.0F);

        if (progress != 0.0F && progress != 1.0F) {
            int i = Math.round(13.0F - progress * 13.0F);

            int j = MathHelper.hsvToRgb(Math.max(0.0F, 1.0F - progress) / 3.0F, 1.0F, 1.0F);
        }


        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinItemRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */