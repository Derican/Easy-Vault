// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.client.ClientDamageData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerDamageMultiplierMessage
{
    private final float multiplier;
    
    public PlayerDamageMultiplierMessage(final float multiplier) {
        this.multiplier = multiplier;
    }
    
    public float getMultiplier() {
        return this.multiplier;
    }
    
    public static void encode(final PlayerDamageMultiplierMessage message, final PacketBuffer buffer) {
        buffer.writeFloat(message.multiplier);
    }
    
    public static PlayerDamageMultiplierMessage decode(final PacketBuffer buffer) {
        return new PlayerDamageMultiplierMessage(buffer.readFloat());
    }
    
    public static void handle(final PlayerDamageMultiplierMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientDamageData.receiveUpdate(message));
        context.setPacketHandled(true);
    }
}
