// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import net.minecraft.network.play.client.CUpdateStructureBlockPacket;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ CUpdateStructureBlockPacket.class })
public class MixinCUpdateStructureBlockPacket
{
    @Redirect(method = { "read" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
    private int readPacketData(final int num, final int min, final int max) {
        return MathHelper.clamp(num, min * 11, max * 11);
    }
}
