// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import iskallia.vault.init.ModNetwork;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.*;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class PlayerDamageHelper
{
    private static final Map<UUID, Set<DamageMultiplier>> multipliers;
    
    public static DamageMultiplier applyMultiplier(final ServerPlayerEntity player, final float value, final Operation operation) {
        return applyMultiplier(player, value, operation, true);
    }
    
    public static DamageMultiplier applyMultiplier(final ServerPlayerEntity player, final float value, final Operation operation, final boolean showOnClient) {
        return applyMultiplier(player, value, operation, showOnClient, Integer.MAX_VALUE);
    }
    
    public static DamageMultiplier applyMultiplier(final ServerPlayerEntity player, final float value, final Operation operation, final boolean showOnClient, final int tickDuration) {
        return applyMultiplier(player, value, operation, showOnClient, tickDuration, sPlayer -> {});
    }
    
    public static DamageMultiplier applyMultiplier(final ServerPlayerEntity player, final float value, final Operation operation, final boolean showOnClient, final int tickDuration, final Consumer<ServerPlayerEntity> onTimeout) {
        return applyMultiplier(player.getServer(), player.getUUID(), new DamageMultiplier(player.getUUID(), value, operation, showOnClient, tickDuration, onTimeout));
    }
    
    public static DamageMultiplier applyMultiplier(final int id, final ServerPlayerEntity player, final float value, final Operation operation) {
        return applyMultiplier(id, player, value, operation, true);
    }
    
    public static DamageMultiplier applyMultiplier(final int id, final ServerPlayerEntity player, final float value, final Operation operation, final boolean showOnClient) {
        return applyMultiplier(id, player, value, operation, showOnClient, Integer.MAX_VALUE);
    }
    
    public static DamageMultiplier applyMultiplier(final int id, final ServerPlayerEntity player, final float value, final Operation operation, final boolean showOnClient, final int tickDuration) {
        return applyMultiplier(id, player, value, operation, showOnClient, tickDuration, sPlayer -> {});
    }
    
    public static DamageMultiplier applyMultiplier(final int id, final ServerPlayerEntity player, final float value, final Operation operation, final boolean showOnClient, final int tickDuration, final Consumer<ServerPlayerEntity> onTimeout) {
        return applyMultiplier(player.getServer(), player.getUUID(), new DamageMultiplier(id, player.getUUID(), value, operation, showOnClient, tickDuration, onTimeout));
    }
    
    private static DamageMultiplier applyMultiplier(final MinecraftServer srv, final UUID playerId, final DamageMultiplier multiplier) {
        PlayerDamageHelper.multipliers.computeIfAbsent(playerId, id -> new HashSet()).add(multiplier);
        multiplier.removed = false;
        sync(srv, playerId);
        return multiplier;
    }
    
    public static Optional<DamageMultiplier> getMultiplier(final ServerPlayerEntity player, final int id) {
        return PlayerDamageHelper.multipliers.getOrDefault(player.getUUID(), new HashSet<DamageMultiplier>()).stream().filter(m -> m.id == id).findFirst();
    }
    
    public static boolean removeMultiplier(final ServerPlayerEntity player, final DamageMultiplier multiplier) {
        return removeMultiplier(player.getServer(), player.getUUID(), multiplier);
    }
    
    public static boolean removeMultiplier(final ServerPlayerEntity player, final int id) {
        return getMultiplier(player, id).filter(damageMultiplier -> removeMultiplier(player, damageMultiplier)).isPresent();
    }
    
    public static boolean removeMultiplier(final MinecraftServer srv, final UUID playerId, final DamageMultiplier multiplier) {
        final boolean removed = PlayerDamageHelper.multipliers.getOrDefault(playerId, new HashSet<DamageMultiplier>()).remove(multiplier);
        if (removed) {
            multiplier.removed = true;
            sync(srv, playerId);
        }
        return removed;
    }
    
    @SubscribeEvent
    public static void onServerTick(final TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        final MinecraftServer srv = (MinecraftServer)LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        PlayerDamageHelper.multipliers.forEach((playerId, multipliers) -> {
            final ServerPlayerEntity sPlayer = srv.getPlayerList().getPlayer(playerId);
            if (sPlayer != null) {
                final boolean didRemoveAny = multipliers.removeIf(multiplier -> {
                    multiplier.tick();
                    if (multiplier.shouldRemove()) {
                        multiplier.onTimeout.accept(sPlayer);
                        multiplier.removed = true;
                        return true;
                    }
                    else {
                        return false;
                    }
                });
                if (didRemoveAny) {
                    sync(srv, playerId);
                }
            }
        });
    }
    
    @SubscribeEvent
    public static void onPlayerDamage(final LivingHurtEvent event) {
        final Entity source = event.getSource().getEntity();
        if (source instanceof ServerPlayerEntity) {
            event.setAmount(event.getAmount() * getDamageMultiplier((PlayerEntity)source, true));
        }
    }
    
    public static float getDamageMultiplier(final PlayerEntity player, final boolean ignoreClientFlag) {
        return getDamageMultiplier(player.getUUID(), ignoreClientFlag);
    }
    
    private static float getDamageMultiplier(final UUID playerId, final boolean ignoreClientFlag) {
        final Set<DamageMultiplier> damageMultipliers = PlayerDamageHelper.multipliers.getOrDefault(playerId, new HashSet<DamageMultiplier>());
        float multiplier = 1.0f;
        for (final DamageMultiplier mult : damageMultipliers) {
            if ((ignoreClientFlag || mult.showOnClient) && mult.operation == Operation.ADDITIVE_MULTIPLY) {
                multiplier += mult.value;
            }
        }
        for (final DamageMultiplier mult : damageMultipliers) {
            if ((ignoreClientFlag || mult.showOnClient) && mult.operation == Operation.STACKING_MULTIPLY) {
                multiplier *= mult.value;
            }
        }
        return Math.max(multiplier, 0.0f);
    }
    
    public static void syncAll(final MinecraftServer srv) {
        PlayerDamageHelper.multipliers.keySet().forEach(playerId -> sync(srv, playerId));
    }
    
    public static void sync(final MinecraftServer srv, final UUID playerId) {
        final ServerPlayerEntity sPlayer = srv.getPlayerList().getPlayer(playerId);
        if (sPlayer != null) {
            sync(sPlayer);
        }
    }
    
    public static void sync(final ServerPlayerEntity sPlayer) {
        final float multiplier = getDamageMultiplier((PlayerEntity)sPlayer, false);
        ModNetwork.CHANNEL.sendTo(new PlayerDamageMultiplierMessage(multiplier), sPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    
    static {
        multipliers = new HashMap<UUID, Set<DamageMultiplier>>();
    }
    
    public static class DamageMultiplier
    {
        private static int counter;
        private final int id;
        private final UUID playerId;
        private final float value;
        private final Operation operation;
        private final boolean showOnClient;
        private final int originalTimeout;
        private int tickTimeout;
        private final Consumer<ServerPlayerEntity> onTimeout;
        private boolean removed;
        
        private DamageMultiplier(final UUID playerId, final float value, final Operation operation, final boolean showOnClient, final int tickTimeout, final Consumer<ServerPlayerEntity> onTimeout) {
            this(DamageMultiplier.counter++ & Integer.MAX_VALUE, playerId, value, operation, showOnClient, tickTimeout, onTimeout);
        }
        
        private DamageMultiplier(final int id, final UUID playerId, final float value, final Operation operation, final boolean showOnClient, final int tickTimeout, final Consumer<ServerPlayerEntity> onTimeout) {
            this.removed = false;
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
            return this.tickTimeout < 0;
        }
        
        public void refreshDuration(final MinecraftServer srv) {
            if (this.removed) {
                applyMultiplier(srv, this.playerId, this);
            }
            this.tickTimeout = this.originalTimeout;
        }
        
        private void tick() {
            if (this.tickTimeout != Integer.MAX_VALUE) {
                --this.tickTimeout;
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final DamageMultiplier that = (DamageMultiplier)o;
            return this.id == that.id;
        }
        
        @Override
        public int hashCode() {
            return this.id;
        }
        
        static {
            DamageMultiplier.counter = 0;
        }
    }
    
    public enum Operation
    {
        ADDITIVE_MULTIPLY, 
        STACKING_MULTIPLY;
    }
}
