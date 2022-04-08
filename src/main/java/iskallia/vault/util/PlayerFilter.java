package iskallia.vault.util;

import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlayerFilter
        implements Predicate<UUID> {
    private final List<UUID> playerUUIDs;

    private PlayerFilter(List<UUID> playerUUIDs) {
        this.playerUUIDs = playerUUIDs;
    }

    public static PlayerFilter any() {
        return new PlayerFilter(Collections.emptyList());
    }

    public static PlayerFilter of(UUID... playerIds) {
        return new PlayerFilter(Arrays.asList(playerIds));
    }

    public static PlayerFilter of(PlayerEntity... players) {
        return new PlayerFilter((List<UUID>) Arrays.<PlayerEntity>stream(players)
                .map(Entity::getUUID)
                .collect(Collectors.toList()));
    }

    public static PlayerFilter of(VaultPlayer... players) {
        return new PlayerFilter((List<UUID>) Arrays.<VaultPlayer>stream(players)
                .map(VaultPlayer::getPlayerId)
                .collect(Collectors.toList()));
    }


    public boolean test(UUID uuid) {
        return (this.playerUUIDs.isEmpty() || this.playerUUIDs.contains(uuid));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\PlayerFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */