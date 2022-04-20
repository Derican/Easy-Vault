package iskallia.vault.world.vault.logic.objective.ancient;

import iskallia.vault.util.NameProviderPublic;
import iskallia.vault.world.data.EternalsData;
import net.minecraft.server.MinecraftServer;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AncientEternalArchive {
    public static List<String> getAncients(final MinecraftServer server, final UUID playerId) {
        final EternalsData.EternalGroup playerEternals = EternalsData.get(server).getEternals(playerId);
        final List<String> ancients = NameProviderPublic.getVHSMPAssociates();
        Collections.shuffle(ancients);
        ancients.removeIf(ancientRef -> playerEternals.containsOriginalEternal(ancientRef, true));
        return ancients;
    }
}
