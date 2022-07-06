// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.client.ClientSandEventData;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;

public class SandEventUpdateMessage
{
    private final float percentFilled;
    private final int sandSpawned;
    private final int sandCollected;
    
    public SandEventUpdateMessage(final float percentFilled, final int sandSpawned, final int sandCollected) {
        this.percentFilled = percentFilled;
        this.sandSpawned = sandSpawned;
        this.sandCollected = sandCollected;
    }
    
    public float getPercentFilled() {
        return this.percentFilled;
    }
    
    public int getSandSpawned() {
        return this.sandSpawned;
    }
    
    public int getSandCollected() {
        return this.sandCollected;
    }
    
    public static void encode(final SandEventUpdateMessage message, final PacketBuffer buffer) {
        buffer.writeFloat(message.percentFilled);
        buffer.writeInt(message.sandSpawned);
        buffer.writeInt(message.sandCollected);
    }
    
    public static SandEventUpdateMessage decode(final PacketBuffer buffer) {
        return new SandEventUpdateMessage(buffer.readFloat(), buffer.readInt(), buffer.readInt());
    }
    
    public static void handle(final SandEventUpdateMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientSandEventData.getInstance().receive(message));
        context.setPacketHandled(true);
    }
}
