package iskallia.vault.world.vault.logic.objective.ancient;

import iskallia.vault.util.NameProviderPublic;
import iskallia.vault.world.data.EternalsData;
import net.minecraft.server.MinecraftServer;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AncientEternalArchive {
    public static List<String> getAncients(MinecraftServer server, UUID playerId) {
        EternalsData.EternalGroup playerEternals = EternalsData.get(server).getEternals(playerId);

        List<String> ancients = NameProviderPublic.getVHSMPAssociates();
        Collections.shuffle(ancients);
        ancients.removeIf(ancientRef -> playerEternals.containsOriginalEternal(ancientRef, true));
        return ancients;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\ancient\AncientEternalArchive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */