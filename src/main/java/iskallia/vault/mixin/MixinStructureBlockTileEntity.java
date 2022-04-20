package iskallia.vault.mixin;

import net.minecraft.tileentity.StructureBlockTileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({StructureBlockTileEntity.class})
public abstract class MixinStructureBlockTileEntity {
    @Redirect(method = {"load"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
    private int read(final int num, final int min, final int max) {
        return MathHelper.clamp(num, min * 11, max * 11);
    }

    /**
     * @author
     * @reason
     */
    @OnlyIn(Dist.CLIENT)
    @Overwrite
    public double getViewDistance() {
        return 816.0;
    }
}
