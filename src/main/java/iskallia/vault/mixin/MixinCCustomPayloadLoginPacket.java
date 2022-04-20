package iskallia.vault.mixin;

import net.minecraft.network.login.client.CCustomPayloadLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({CCustomPayloadLoginPacket.class})
public class MixinCCustomPayloadLoginPacket {
    @ModifyConstant(method = {"read"}, constant = {@Constant(intValue = 1048576)}, require = 1)
    public int adjustMaxPayloadSize(final int maxPayloadSize) {
        return Integer.MAX_VALUE;
    }
}
