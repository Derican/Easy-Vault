package iskallia.vault.client;

import iskallia.vault.entity.eternal.EternalDataSnapshot;
import iskallia.vault.network.message.EternalSyncMessage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ClientEternalData {
    private static Map<UUID, List<EternalDataSnapshot>> eternalSnapshots = new HashMap<>();

    @Nullable
    public static EternalDataSnapshot getSnapshot(UUID eternalId) {
        for (UUID playerId : eternalSnapshots.keySet()) {
            List<EternalDataSnapshot> snapshots = eternalSnapshots.get(playerId);
            for (EternalDataSnapshot snapshot : snapshots) {
                if (snapshot.getId().equals(eternalId)) {
                    return snapshot;
                }
            }
        }
        return null;
    }

    public static List<EternalDataSnapshot> getPlayerEternals(UUID playerId) {
        return eternalSnapshots.getOrDefault(playerId, new ArrayList<>());
    }

    public static void receiveUpdate(EternalSyncMessage pkt) {
        eternalSnapshots = pkt.getEternalData();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\ClientEternalData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */