package iskallia.vault.util;

import iskallia.vault.network.message.PlayerDamageMultiplierMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.*;
import java.util.function.Consumer;

@EventBusSubscriber
public class PlayerDamageHelper {
    private static final Map<UUID, Set<DamageMultiplier>> multipliers = new HashMap<>();

    public static DamageMultiplier applyMultiplier(ServerPlayerEntity player, float value, Operation operation) {
        return applyMultiplier(player, value, operation, true);
    }

    public static DamageMultiplier applyMultiplier(ServerPlayerEntity player, float value, Operation operation, boolean showOnClient) {
        return applyMultiplier(player, value, operation, showOnClient, 2147483647);
    }

    public static DamageMultiplier applyMultiplier(ServerPlayerEntity player, float value, Operation operation, boolean showOnClient, int tickDuration) {
        return applyMultiplier(player, value, operation, showOnClient, tickDuration, sPlayer -> {

        });
    }

    public static DamageMultiplier applyMultiplier(ServerPlayerEntity player, float value, Operation operation, boolean showOnClient, int tickDuration, Consumer<ServerPlayerEntity> onTimeout) {
        return applyMultiplier(player.getServer(), player.getUUID(), new DamageMultiplier(player.getUUID(), value, operation, showOnClient, tickDuration, onTimeout));
    }

    public static DamageMultiplier applyMultiplier(int id, ServerPlayerEntity player, float value, Operation operation) {
        return applyMultiplier(id, player, value, operation, true);
    }

    public static DamageMultiplier applyMultiplier(int id, ServerPlayerEntity player, float value, Operation operation, boolean showOnClient) {
        return applyMultiplier(id, player, value, operation, showOnClient, 2147483647);
    }

    public static DamageMultiplier applyMultiplier(int id, ServerPlayerEntity player, float value, Operation operation, boolean showOnClient, int tickDuration) {
        return applyMultiplier(id, player, value, operation, showOnClient, tickDuration, sPlayer -> {

        });
    }

    public static DamageMultiplier applyMultiplier(int id, ServerPlayerEntity player, float value, Operation operation, boolean showOnClient, int tickDuration, Consumer<ServerPlayerEntity> onTimeout) {
        return applyMultiplier(player.getServer(), player.getUUID(), new DamageMultiplier(id, player.getUUID(), value, operation, showOnClient, tickDuration, onTimeout));
    }

    private static DamageMultiplier applyMultiplier(MinecraftServer srv, UUID playerId, DamageMultiplier multiplier) {
        ((Set<DamageMultiplier>) multipliers.computeIfAbsent(playerId, id -> new HashSet())).add(multiplier);
        multiplier.removed = false;
        sync(srv, playerId);
        return multiplier;
    }

    public static Optional<DamageMultiplier> getMultiplier(ServerPlayerEntity player, int id) {
        return ((Set<DamageMultiplier>) multipliers.getOrDefault(player.getUUID(), new HashSet<>())).stream().filter(m -> (m.id == id)).findFirst();
    }

    public static boolean removeMultiplier(ServerPlayerEntity player, DamageMultiplier multiplier) {
        return removeMultiplier(player.getServer(), player.getUUID(), multiplier);
    }

    public static boolean removeMultiplier(ServerPlayerEntity player, int id) {
        return getMultiplier(player, id).filter(damageMultiplier -> removeMultiplier(player, damageMultiplier)).isPresent();
    }

    public static boolean removeMultiplier(MinecraftServer srv, UUID playerId, DamageMultiplier multiplier) {
        boolean removed = ((Set) multipliers.getOrDefault(playerId, new HashSet<>())).remove(multiplier);
        if (removed) {
            multiplier.removed = true;
            sync(srv, playerId);
        }
        return removed;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }

        MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        multipliers.forEach((playerId, multipliers) -> {
            ServerPlayerEntity sPlayer = srv.getPlayerList().getPlayer(playerId);
            if (sPlayer == null) {
                return;
            }
            boolean didRemoveAny = multipliers.removeIf(());
            if (didRemoveAny) {
                sync(srv, playerId);
            }
        });
    }


    @SubscribeEvent
    public static void onPlayerDamage(LivingHurtEvent event) {
        Entity source = event.getSource().getEntity();
        if (source instanceof ServerPlayerEntity) {
            event.setAmount(event.getAmount() * getDamageMultiplier((PlayerEntity) source, true));
        }
    }

    public static float getDamageMultiplier(PlayerEntity player, boolean ignoreClientFlag) {
        return getDamageMultiplier(player.getUUID(), ignoreClientFlag);
    }

    private static float getDamageMultiplier(UUID playerId, boolean ignoreClientFlag) {
        Set<DamageMultiplier> damageMultipliers = multipliers.getOrDefault(playerId, new HashSet<>());
        float multiplier = 1.0F;
        for (DamageMultiplier mult : damageMultipliers) {
            if ((ignoreClientFlag || mult.showOnClient) && mult.operation == Operation.ADDITIVE_MULTIPLY) {
                multiplier += mult.value;
            }
        }
        for (DamageMultiplier mult : damageMultipliers) {
            if ((ignoreClientFlag || mult.showOnClient) && mult.operation == Operation.STACKING_MULTIPLY) {
                multiplier *= mult.value;
            }
        }
        return Math.max(multiplier, 0.0F);
    }

    public static void syncAll(MinecraftServer srv) {
        multipliers.keySet().forEach(playerId -> sync(srv, playerId));
    }


    public static void sync(MinecraftServer srv, UUID playerId) {
        ServerPlayerEntity sPlayer = srv.getPlayerList().getPlayer(playerId);
        if (sPlayer != null) {
            sync(sPlayer);
        }
    }

    public static void sync(ServerPlayerEntity sPlayer) {
        float multiplier = getDamageMultiplier((PlayerEntity) sPlayer, false);

        ModNetwork.CHANNEL.sendTo(new PlayerDamageMultiplierMessage(multiplier), sPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static class DamageMultiplier {
        private static int counter = 0;

        private final int id;

        private final UUID playerId;
        private final float value;
        private final PlayerDamageHelper.Operation operation;
        private final boolean showOnClient;
        private final int originalTimeout;
        private int tickTimeout;
        private final Consumer<ServerPlayerEntity> onTimeout;
        private boolean removed = false;

        private DamageMultiplier(UUID playerId, float value, PlayerDamageHelper.Operation operation, boolean showOnClient, int tickTimeout, Consumer<ServerPlayerEntity> onTimeout) {
            this(counter++ & Integer.MAX_VALUE, playerId, value, operation, showOnClient, tickTimeout, onTimeout);
        }

        private DamageMultiplier(int id, UUID playerId, float value, PlayerDamageHelper.Operation operation, boolean showOnClient, int tickTimeout, Consumer<ServerPlayerEntity> onTimeout) {
            this.id = id;
            this.playerId = playerId;
            this.value = value;
            this.operation = operation;
            this.showOnClient = showOnClient;
            this.originalTimeout = tickTimeout;
            this.tickTimeout = tickTimeout;
            this.onTimeout = onTimeout;
        }

        public float getMultiplier() {
            return this.value;
        }

        private boolean shouldRemove() {
            return (this.tickTimeout < 0);
        }

        public void refreshDuration(MinecraftServer srv) {
            if (this.removed) {
                PlayerDamageHelper.applyMultiplier(srv, this.playerId, this);
            }
            this.tickTimeout = this.originalTimeout;
        }

        private void tick() {
            if (this.tickTimeout != Integer.MAX_VALUE) {
                this.tickTimeout--;
            }
        }


        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DamageMultiplier that = (DamageMultiplier) o;
            return (this.id == that.id);
        }


        public int hashCode() {
            return this.id;
        }
    }

    public enum Operation {
        ADDITIVE_MULTIPLY,
        STACKING_MULTIPLY;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\PlayerDamageHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */