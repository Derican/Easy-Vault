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


@Mixin({LavaFluid.class})
public class MixinLavaFluid {
    @Inject(method = {"randomTick"}, at = {@At("HEAD")}, cancellable = true)
    public void onRandomTick(World world, BlockPos pos, FluidState state, Random random, CallbackInfo ci) {
        if (world.dimension() == Vault.VAULT_KEY)
            ci.cancel();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinLavaFluid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */