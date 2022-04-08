package iskallia.vault.mixin;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.OverlevelEnchantHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Item.class})
public class MixinItem {
    @Inject(method = {"getDisplayName"}, cancellable = true, at = {@At("RETURN")})
    public void appendOverlevelPrefix(ItemStack stack, CallbackInfoReturnable<ITextComponent> info) {
        if (stack.getItem() == Items.ENCHANTED_BOOK) {
            int overLevels = OverlevelEnchantHelper.getOverlevels(stack);
            if (overLevels != -1) {
                IFormattableTextComponent formatted = ModConfigs.OVERLEVEL_ENCHANT.format((ITextComponent) info.getReturnValue(), overLevels);
                if (formatted != null) {
                    info.setReturnValue(formatted);
                    info.cancel();
                }
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */