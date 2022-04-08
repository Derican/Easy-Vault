package iskallia.vault.world.vault.logic.task;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.world.server.ServerWorld;


@FunctionalInterface
public interface IVaultTask {
    default void executeForAllPlayers(VaultRaid vault, ServerWorld world) {
        vault.getPlayers().forEach(vPlayer -> execute(vault, vPlayer, world));
    }


    default IVaultTask then(IVaultTask other) {
        return (vault, player, world) -> {
            execute(vault, player, world);
            other.execute(vault, player, world);
        };
    }

    void execute(VaultRaid paramVaultRaid, VaultPlayer paramVaultPlayer, ServerWorld paramServerWorld);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\task\IVaultTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */