package iskallia.vault.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber
public class PlayerActiveFlags {
    private static final Map<UUID, List<FlagTimeout>> timeouts = new HashMap<>();

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            return;
        }

        timeouts.forEach((playerId, flagTimeouts) -> {
//            flagTimeouts.forEach(());
//            flagTimeouts.removeIf(());
        });
    }

    public static void set(PlayerEntity player, Flag flag, int timeout) {
        set(player.getUUID(), flag, timeout);
    }

    public static void set(UUID playerId, Flag flag, int timeout) {
        List<FlagTimeout> flags = timeouts.computeIfAbsent(playerId, id -> new ArrayList());
        for (FlagTimeout flagTimeout : flags) {
            if (flagTimeout.flag == flag) {
                flagTimeout.tickTimeout = timeout;
                return;
            }
        }
        flags.add(new FlagTimeout(flag, timeout));
    }

    public static boolean isSet(PlayerEntity player, Flag flag) {
        return isSet(player.getUUID(), flag);
    }

    public static boolean isSet(UUID playerId, Flag flag) {
        List<FlagTimeout> flags = timeouts.getOrDefault(playerId, Collections.emptyList());
        for (FlagTimeout timeout : flags) {
            if (timeout.flag == flag && !timeout.isFinished()) {
                return true;
            }
        }
        return false;
    }

    private static class FlagTimeout {
        private final PlayerActiveFlags.Flag flag;
        private int tickTimeout;

        private FlagTimeout(PlayerActiveFlags.Flag flag, int tickTimeout) {
            this.flag = flag;
            this.tickTimeout = tickTimeout;
        }

        private void tick() {
            this.tickTimeout--;
        }

        private boolean isFinished() {
            return (this.tickTimeout <= 0);
        }
    }

    public enum Flag {
        ATTACK_AOE,
        CHAINING_AOE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\event\PlayerActiveFlags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */