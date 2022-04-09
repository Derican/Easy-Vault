// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class NetcodeUtils
{
    public static void runIfPresent(@Nullable final MinecraftServer server, @Nonnull final UUID uuid, final Consumer<ServerPlayerEntity> action) {
        runIfPresent(server, uuid, sPlayer -> {
            action.accept(sPlayer);
            return null;
        });
    }
    
    public static <T> Optional<T> runIfPresent(@Nullable final MinecraftServer server, @Nonnull final UUID uuid, final Function<ServerPlayerEntity, T> action) {
        if (server == null) {
            return Optional.empty();
        }
        final ServerPlayerEntity player = server.getPlayerList().getPlayer(uuid);
        if (player == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(action.apply(player));
    }
}
