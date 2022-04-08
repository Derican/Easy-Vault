package iskallia.vault.network.message;

import iskallia.vault.client.ClientEternalData;
import iskallia.vault.entity.eternal.EternalDataSnapshot;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.*;
import java.util.function.Supplier;

public class EternalSyncMessage {
    public EternalSyncMessage(Map<UUID, List<EternalDataSnapshot>> eternalData) {
        this.eternalData = eternalData;
    }

    private final Map<UUID, List<EternalDataSnapshot>> eternalData;

    public Map<UUID, List<EternalDataSnapshot>> getEternalData() {
        return this.eternalData;
    }

    public static void encode(EternalSyncMessage pkt, PacketBuffer buffer) {
        buffer.writeInt(pkt.eternalData.size());
        pkt.eternalData.forEach((playerUUID, playerEternals) -> {
            buffer.writeUUID(playerUUID);
            buffer.writeInt(playerEternals.size());
            playerEternals.forEach(());
        });
    }


    public static EternalSyncMessage decode(PacketBuffer buffer) {
        Map<UUID, List<EternalDataSnapshot>> eternalData = new HashMap<>();
        int playerEternals = buffer.readInt();
        for (int i = 0; i < playerEternals; i++) {
            UUID playerUUID = buffer.readUUID();
            List<EternalDataSnapshot> snapshots = new ArrayList<>();
            int eternals = buffer.readInt();
            for (int j = 0; j < eternals; j++) {
                snapshots.add(EternalDataSnapshot.deserialize(buffer));
            }

            eternalData.put(playerUUID, snapshots);
        }

        return new EternalSyncMessage(eternalData);
    }

    public static void handle(EternalSyncMessage pkt, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientEternalData.receiveUpdate(pkt));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\EternalSyncMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */