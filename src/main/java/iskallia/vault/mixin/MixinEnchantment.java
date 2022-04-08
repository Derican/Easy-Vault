package iskallia.vault.mixin;

import iskallia.vault.item.gear.VaultGear;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Enchantment.class})
public abstract class MixinEnchantment {
    @Inject(method = {"canApply"}, at = {@At("HEAD")}, cancellable = true)
    private void canApply(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
        if (stack.getItem() instanceof VaultGear && !((VaultGear) stack.getItem()).canApply(stack, (Enchantment) this))
            ci.setReturnValue(Boolean.valueOf(false));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */