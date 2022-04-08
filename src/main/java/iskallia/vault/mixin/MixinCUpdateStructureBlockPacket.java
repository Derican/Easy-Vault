package iskallia.vault.mixin;

import net.minecraft.network.play.client.CUpdateStructureBlockPacket;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({CUpdateStructureBlockPacket.class})
public class MixinCUpdateStructureBlockPacket {
    @Redirect(method = {"readPacketData"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
    private int readPacketData(int num, int min, int max) {
        return MathHelper.clamp(num, min * 11, max * 11);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinCUpdateStructureBlockPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */