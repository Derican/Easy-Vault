// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({ SCustomPayloadPlayPacket.class })
public class MixinSCustomPayloadPlayPacket
{
    @ModifyConstant(method = { "read" }, constant = { @Constant(intValue = 1048576) }, require = 1)
    public int adjustMaxPayloadSize(final int maxPayloadSize) {
        return Integer.MAX_VALUE;
    }
    
    @ModifyConstant(method = { "<init>(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/network/PacketBuffer;)V" }, constant = { @Constant(intValue = 1048576) }, require = 1)
    public int adjustCtorMaxPayloadSize(final int maxPayloadSize) {
        return Integer.MAX_VALUE;
    }
}
