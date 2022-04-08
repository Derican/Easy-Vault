package iskallia.vault.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin({EnchantmentHelper.class})
public class MixinEnchantmentHelper {
    @Inject(method = {"getDepthStriderModifier"}, at = {@At("RETURN")}, cancellable = true)
    private static void modifyBossDepthStrider(LivingEntity entityIn, CallbackInfoReturnable<Integer> cir) {
        if (entityIn instanceof iskallia.vault.entity.VaultBoss)
            cir.setReturnValue(Integer.valueOf(3));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinEnchantmentHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */