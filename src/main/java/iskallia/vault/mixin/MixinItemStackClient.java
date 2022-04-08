package iskallia.vault.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ItemStack.class})
public class MixinItemStackClient {
    @Redirect(method = {"getTooltip"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasDisplayName()Z", ordinal = 0))
    public boolean doDisplayNameItalic(ItemStack stack) {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinItemStackClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */