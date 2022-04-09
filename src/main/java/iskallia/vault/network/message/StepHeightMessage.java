// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StepHeightMessage
{
    private final float stepHeight;
    
    public StepHeightMessage(final float stepHeight) {
        this.stepHeight = stepHeight;
    }
    
    public static void encode(final StepHeightMessage message, final PacketBuffer buffer) {
        buffer.writeFloat(message.stepHeight);
    }
    
    public static StepHeightMessage decode(final PacketBuffer buffer) {
        return new StepHeightMessage(buffer.readFloat());
    }
    
    public static void handle(final StepHeightMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.maxUpStep = message.stepHeight;
            }
            return;
        });
        context.setPacketHandled(true);
    }
}
