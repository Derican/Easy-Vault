// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import iskallia.vault.world.gen.structure.JigsawPiecePlacer;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ITickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ FallingBlock.class })
public class MixinFallingBlock
{
    @Redirect(method = { "onPlace" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ITickList;scheduleTick(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"))
    public <T> void interceptBlockAddedTick(final ITickList<T> iTickList, final BlockPos pos, final T itemIn, final int scheduledTime) {
        if (!JigsawPiecePlacer.isPlacingRoom()) {
            iTickList.scheduleTick(pos, itemIn, scheduledTime);
        }
    }
    
    @Redirect(method = { "updateShape" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ITickList;scheduleTick(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"))
    public <T> void interceptPostPlacementTick(final ITickList<T> iTickList, final BlockPos pos, final T itemIn, final int scheduledTime) {
        if (!JigsawPiecePlacer.isPlacingRoom()) {
            iTickList.scheduleTick(pos, itemIn, scheduledTime);
        }
    }
}
