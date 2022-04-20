package iskallia.vault.network.message;

import iskallia.vault.client.ClientEternalData;
import iskallia.vault.entity.eternal.EternalDataSnapshot;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.*;
import java.util.function.Supplier;

public class EternalSyncMessage {
    private final Map<UUID, List<EternalDataSnapshot>> eternalData;

    public EternalSyncMessage(final Map<UUID, List<EternalDataSnapshot>> eternalData) {
        this.eternalData = eternalData;
    }

    public Map<UUID, List<EternalDataSnapshot>> getEternalData() {
        return this.eternalData;
    }

    public static void encode(final EternalSyncMessage pkt, final PacketBuffer buffer) {
        buffer.writeInt(pkt.eternalData.size());
        pkt.eternalData.forEach((playerUUID, playerEternals) -> {
            buffer.writeUUID(playerUUID);
            buffer.writeInt(playerEternals.size());
            playerEternals.forEach(eternalData -> eternalData.serialize(buffer));
        });
    }

    public static EternalSyncMessage decode(final PacketBuffer buffer) {
        final Map<UUID, List<EternalDataSnapshot>> eternalData = new HashMap<UUID, List<EternalDataSnapshot>>();
        for (int playerEternals = buffer.readInt(), i = 0; i < playerEternals; ++i) {
            final UUID playerUUID = buffer.readUUID();
            final List<EternalDataSnapshot> snapshots = new ArrayList<EternalDataSnapshot>();
            for (int eternals = buffer.readInt(), j = 0; j < eternals; ++j) {
                snapshots.add(EternalDataSnapshot.deserialize(buffer));
            }
            eternalData.put(playerUUID, snapshots);
        }
        return new EternalSyncMessage(eternalData);
    }

    public static void handle(final EternalSyncMessage pkt, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientEternalData.receiveUpdate(pkt));
        context.setPacketHandled(true);
    }
}
