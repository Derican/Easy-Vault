package iskallia.vault.world.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;

import java.util.Map;
import java.util.WeakHashMap;


public class VaultListener {
    public static final Map<VaultRaid, Void> REGISTRY = new WeakHashMap<>();

    public static void listen(VaultRaid vault) {
        REGISTRY.put(vault, null);
    }

    public static synchronized <T extends Event> void onEvent(T event) {
        if (Thread.currentThread().getThreadGroup() != SidedThreadGroups.SERVER) {
            return;
        }
        try {
            REGISTRY.keySet().removeIf(VaultRaid::isFinished);

            REGISTRY.keySet().forEach(vault -> vault.getEvents().forEach(()));


        } catch (Exception e) {
            Vault.LOGGER.error("Upsie, you know what causes this but are lazy to fix it :(");
            e.printStackTrace();
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\event\VaultListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */