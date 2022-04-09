// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import iskallia.vault.Vault;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin({ LavaFluid.class })
public class MixinLavaFluid
{
    @Inject(method = { "randomTick" }, at = { @At("HEAD") }, cancellable = true)
    public void onRandomTick(final World world, final BlockPos pos, final FluidState state, final Random random, final CallbackInfo ci) {
        if (world.dimension() == Vault.VAULT_KEY) {
            ci.cancel();
        }
    }
}
