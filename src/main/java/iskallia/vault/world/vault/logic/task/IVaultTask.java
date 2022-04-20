package iskallia.vault.world.vault.logic.task;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.world.server.ServerWorld;

@FunctionalInterface
public interface IVaultTask {
    void execute(final VaultRaid p0, final VaultPlayer p1, final ServerWorld p2);

    default void executeForAllPlayers(final VaultRaid vault, final ServerWorld world) {
        vault.getPlayers().forEach(vPlayer -> this.execute(vault, vPlayer, world));
    }

    default IVaultTask then(final IVaultTask other) {
        return (vault, player, world) -> {
            this.execute(vault, player, world);
            other.execute(vault, player, world);
        };
    }
}
