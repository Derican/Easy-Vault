// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ItemStack.class })
public class MixinItemStackClient
{
    @Redirect(method = { "getTooltipLines" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasCustomHoverName()Z", ordinal = 0))
    public boolean doDisplayNameItalic(final ItemStack stack) {
        return false;
    }
}
