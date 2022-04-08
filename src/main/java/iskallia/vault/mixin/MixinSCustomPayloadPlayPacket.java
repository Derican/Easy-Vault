package iskallia.vault.mixin;

import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({SCustomPayloadPlayPacket.class})
public class MixinSCustomPayloadPlayPacket {
    @ModifyConstant(method = {"readPacketData"}, constant = {@Constant(intValue = 1048576)}, require = 1)
    public int adjustMaxPayloadSize(int maxPayloadSize) {
        return Integer.MAX_VALUE;
    }

    @ModifyConstant(method = {"<init>(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/network/PacketBuffer;)V"}, constant = {@Constant(intValue = 1048576)}, require = 1)
    public int adjustCtorMaxPayloadSize(int maxPayloadSize) {
        return Integer.MAX_VALUE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinSCustomPayloadPlayPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */