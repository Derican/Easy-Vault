package iskallia.vault.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber
public class PlayerActiveFlags {
    private static final Map<UUID, List<FlagTimeout>> timeouts;

    @SubscribeEvent
    public static void onTick(final TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            return;
        }
        PlayerActiveFlags.timeouts.forEach((playerId, flagTimeouts) -> {
            flagTimeouts.forEach(rec$ -> ((FlagTimeout) rec$).tick());
            flagTimeouts.removeIf(rec$ -> ((FlagTimeout) rec$).isFinished());
        });
    }

    public static void set(final PlayerEntity player, final Flag flag, final int timeout) {
        set(player.getUUID(), flag, timeout);
    }

    public static void set(final UUID playerId, final Flag flag, final int timeout) {
        final List<FlagTimeout> flags = PlayerActiveFlags.timeouts.computeIfAbsent(playerId, id -> new ArrayList());
        for (final FlagTimeout flagTimeout : flags) {
            if (flagTimeout.flag == flag) {
                flagTimeout.tickTimeout = timeout;
                return;
            }
        }
        flags.add(new FlagTimeout(flag, timeout));
    }

    public static boolean isSet(final PlayerEntity player, final Flag flag) {
        return isSet(player.getUUID(), flag);
    }

    public static boolean isSet(final UUID playerId, final Flag flag) {
        final List<FlagTimeout> flags = PlayerActiveFlags.timeouts.getOrDefault(playerId, Collections.emptyList());
        for (final FlagTimeout timeout : flags) {
            if (timeout.flag == flag && !timeout.isFinished()) {
                return true;
            }
        }
        return false;
    }

    static {
        timeouts = new HashMap<UUID, List<FlagTimeout>>();
    }

    private static class FlagTimeout {
        private final Flag flag;
        private int tickTimeout;

        private FlagTimeout(final Flag flag, final int tickTimeout) {
            this.flag = flag;
            this.tickTimeout = tickTimeout;
        }

        private void tick() {
            --this.tickTimeout;
        }

        private boolean isFinished() {
            return this.tickTimeout <= 0;
        }
    }

    public enum Flag {
        ATTACK_AOE,
        CHAINING_AOE;
    }
}
