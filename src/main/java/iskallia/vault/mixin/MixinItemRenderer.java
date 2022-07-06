// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.MathHelper;
import iskallia.vault.attribute.FloatAttribute;
import iskallia.vault.attribute.IntegerAttribute;
import net.minecraft.client.renderer.Tessellator;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.init.ModAttributes;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.FontRenderer;

public abstract class MixinItemRenderer
{
    private void render(final FontRenderer fr, final ItemStack stack, final int xPosition, final int yPosition, final String text, final CallbackInfo ci) {
        if (!ModAttributes.GEAR_MAX_LEVEL.exists(stack)) {
            return;
        }
        RenderSystem.disableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuilder();
        float progress = ModAttributes.GEAR_MAX_LEVEL.getOrDefault(stack, 1).getValue(stack);
        progress = (progress - ModAttributes.GEAR_LEVEL.getOrDefault(stack, 0.0f).getValue(stack)) / progress;
        progress = MathHelper.clamp(progress, 0.0f, 1.0f);
        if (progress != 0.0f && progress != 1.0f) {
            final int i = Math.round(13.0f - progress * 13.0f);
            MathHelper.hsvToRgb(Math.max(0.0f, 1.0f - progress) / 3.0f, 1.0f, 1.0f);
        }
        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
    }
}
