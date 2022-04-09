// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PlayerFilter implements Predicate<UUID>
{
    private final List<UUID> playerUUIDs;
    
    private PlayerFilter(final List<UUID> playerUUIDs) {
        this.playerUUIDs = playerUUIDs;
    }
    
    public static PlayerFilter any() {
        return new PlayerFilter(Collections.emptyList());
    }
    
    public static PlayerFilter of(final UUID... playerIds) {
        return new PlayerFilter(Arrays.asList(playerIds));
    }
    
    public static PlayerFilter of(final PlayerEntity... players) {
        return new PlayerFilter((List<UUID>)Arrays.stream(players).map(Entity::getUUID).collect(Collectors.toList()));
    }
    
    public static PlayerFilter of(final VaultPlayer... players) {
        return new PlayerFilter((List<UUID>)Arrays.stream(players).map(VaultPlayer::getPlayerId).collect(Collectors.toList()));
    }
    
    @Override
    public boolean test(final UUID uuid) {
        return this.playerUUIDs.isEmpty() || this.playerUUIDs.contains(uuid);
    }
}
