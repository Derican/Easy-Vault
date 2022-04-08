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
    public void onFireTick(BlockState state, ServerWorld world, BlockPos pos, Random rand, CallbackInfo ci) {
        if (world.dimension() == Vault.VAULT_KEY)
            ci.cancel();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinFireBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */