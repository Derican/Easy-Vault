// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.client.ClientActiveEternalData;
import iskallia.vault.entity.eternal.ActiveEternalData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ActiveEternalMessage
{
    private final Set<ActiveEternalData.ActiveEternal> activeEternals;
    
    public ActiveEternalMessage(final Set<ActiveEternalData.ActiveEternal> activeEternals) {
        this.activeEternals = activeEternals;
    }
    
    public Set<ActiveEternalData.ActiveEternal> getActiveEternals() {
        return this.activeEternals;
    }
    
    public static void encode(final ActiveEternalMessage message, final PacketBuffer buffer) {
        buffer.writeInt(message.activeEternals.size());
        message.activeEternals.forEach(activeEternal -> activeEternal.write(buffer));
    }
    
    public static ActiveEternalMessage decode(final PacketBuffer buffer) {
        final int eternalCount = buffer.readInt();
        final Set<ActiveEternalData.ActiveEternal> activeEternals = new LinkedHashSet<ActiveEternalData.ActiveEternal>();
        for (int i = 0; i < eternalCount; ++i) {
            activeEternals.add(ActiveEternalData.ActiveEternal.read(buffer));
        }
        return new ActiveEternalMessage(activeEternals);
    }
    
    public static void handle(final ActiveEternalMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientActiveEternalData.receive(message));
        context.setPacketHandled(true);
    }
}
