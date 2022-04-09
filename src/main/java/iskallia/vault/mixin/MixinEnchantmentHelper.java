// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import iskallia.vault.entity.VaultBoss;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ EnchantmentHelper.class })
public class MixinEnchantmentHelper
{
    @Inject(method = { "getDepthStrider" }, at = { @At("RETURN") }, cancellable = true)
    private static void modifyBossDepthStrider(final LivingEntity entityIn, final CallbackInfoReturnable<Integer> cir) {
        if (entityIn instanceof VaultBoss) {
            cir.setReturnValue(3);
        }
    }
}
