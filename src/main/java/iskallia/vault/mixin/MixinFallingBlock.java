package iskallia.vault.mixin;

import iskallia.vault.world.gen.structure.JigsawPiecePlacer;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ITickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({FallingBlock.class})
public class MixinFallingBlock {
    @Redirect(method = {"onBlockAdded"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ITickList;scheduleTick(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"))
    public <T> void interceptBlockAddedTick(ITickList<T> iTickList, BlockPos pos, T itemIn, int scheduledTime) {
        if (!JigsawPiecePlacer.isPlacingRoom()) {
            iTickList.scheduleTick(pos, itemIn, scheduledTime);
        }
    }

    @Redirect(method = {"updatePostPlacement"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ITickList;scheduleTick(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"))
    public <T> void interceptPostPlacementTick(ITickList<T> iTickList, BlockPos pos, T itemIn, int scheduledTime) {
        if (!JigsawPiecePlacer.isPlacingRoom())
            iTickList.scheduleTick(pos, itemIn, scheduledTime);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinFallingBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */