package iskallia.vault.client;

import iskallia.vault.entity.eternal.ActiveEternalData;
import iskallia.vault.network.message.ActiveEternalMessage;

import java.util.*;

public class ClientActiveEternalData {
    private static final Set<ActiveEternalData.ActiveEternal> activeEternals;

    public static Set<ActiveEternalData.ActiveEternal> getActiveEternals() {
        return Collections.unmodifiableSet((Set<? extends ActiveEternalData.ActiveEternal>) ClientActiveEternalData.activeEternals);
    }

    public static void receive(final ActiveEternalMessage message) {
        final Set<ActiveEternalData.ActiveEternal> updatedEternals = message.getActiveEternals();
        final Set<ActiveEternalData.ActiveEternal> processed = new HashSet<ActiveEternalData.ActiveEternal>();
        ClientActiveEternalData.activeEternals.removeIf(activeEternal -> {
            ActiveEternalData.ActiveEternal updated = null;

            final Iterator iterator = updatedEternals.iterator();
            while (iterator.hasNext()) {
                final ActiveEternalData.ActiveEternal eternal = (ActiveEternalData.ActiveEternal) iterator.next();
                if (eternal.equals(activeEternal)) {
                    updated = eternal;
                    break;
                }
            }
            if (updated == null) {
                return true;
            } else {
                activeEternal.updateFrom(updated);
                processed.add(updated);
                return false;
            }
        });
        updatedEternals.removeIf(processed::contains);
        ClientActiveEternalData.activeEternals.addAll(updatedEternals);
    }

    public static void clearClientCache() {
        ClientActiveEternalData.activeEternals.clear();
    }

    static {
        activeEternals = new LinkedHashSet<ActiveEternalData.ActiveEternal>();
    }
}
