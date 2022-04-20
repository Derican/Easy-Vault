package iskallia.vault.mixin;

import iskallia.vault.Vault;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin({FireBlock.class})
public class MixinFireBlock {
    @Inject(method = {"tick"}, at = {@At("HEAD")}, cancellable = true)
    public void onFireTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random rand, final CallbackInfo ci) {
        if (world.dimension() == Vault.VAULT_KEY) {
            ci.cancel();
        }
    }
}
