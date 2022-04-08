package iskallia.vault.mixin;

import net.minecraft.network.login.server.SCustomPayloadLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({SCustomPayloadLoginPacket.class})
public class MixinSCustomPayloadLoginPacket {
    @ModifyConstant(method = {"readPacketData"}, constant = {@Constant(intValue = 1048576)}, require = 1)
    public int adjustMaxPayloadSize(int maxPayloadSize) {
        return Integer.MAX_VALUE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinSCustomPayloadLoginPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */