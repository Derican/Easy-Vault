package iskallia.vault.network.message;

import iskallia.vault.client.ClientActiveEternalData;
import iskallia.vault.entity.eternal.ActiveEternalData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;


public class ActiveEternalMessage {
    private final Set<ActiveEternalData.ActiveEternal> activeEternals;

    public ActiveEternalMessage(Set<ActiveEternalData.ActiveEternal> activeEternals) {
        this.activeEternals = activeEternals;
    }

    public Set<ActiveEternalData.ActiveEternal> getActiveEternals() {
        return this.activeEternals;
    }

    public static void encode(ActiveEternalMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.activeEternals.size());
        message.activeEternals.forEach(activeEternal -> activeEternal.write(buffer));
    }

    public static ActiveEternalMessage decode(PacketBuffer buffer) {
        int eternalCount = buffer.readInt();
        Set<ActiveEternalData.ActiveEternal> activeEternals = new LinkedHashSet<>();
        for (int i = 0; i < eternalCount; i++) {
            activeEternals.add(ActiveEternalData.ActiveEternal.read(buffer));
        }
        return new ActiveEternalMessage(activeEternals);
    }

    public static void handle(ActiveEternalMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientActiveEternalData.receive(message));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\ActiveEternalMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */