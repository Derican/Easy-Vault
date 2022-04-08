package iskallia.vault.util;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;


public class NetcodeUtils {
    public static void runIfPresent(@Nullable MinecraftServer server, @Nonnull UUID uuid, Consumer<ServerPlayerEntity> action) {
        runIfPresent(server, uuid, sPlayer -> {
            action.accept(sPlayer);
            return null;
        });
    }

    public static <T> Optional<T> runIfPresent(@Nullable MinecraftServer server, @Nonnull UUID uuid, Function<ServerPlayerEntity, T> action) {
        if (server == null) {
            return Optional.empty();
        }

        ServerPlayerEntity player = server.getPlayerList().getPlayer(uuid);
        if (player == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(action.apply(player));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\NetcodeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */