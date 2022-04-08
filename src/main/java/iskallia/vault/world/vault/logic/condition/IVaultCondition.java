package iskallia.vault.world.vault.logic.condition;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;


@FunctionalInterface
public interface IVaultCondition {
    default IVaultCondition negate() {
        return (vault, player, world) -> !test(vault, player, world);
    }

    default IVaultCondition and(IVaultCondition other) {
        Objects.requireNonNull(other);
        return (vault, player, world) -> (test(vault, player, world) && other.test(vault, player, world));
    }

    default IVaultCondition or(IVaultCondition other) {
        Objects.requireNonNull(other);
        return (vault, player, world) -> (test(vault, player, world) || other.test(vault, player, world));
    }

    default IVaultCondition xor(IVaultCondition other) {
        Objects.requireNonNull(other);
        return (vault, player, world) -> {
            boolean a = test(vault, player, world);
            boolean b = other.test(vault, player, world);
            return ((!a && b) || (a && !b));
        };
    }

    boolean test(VaultRaid paramVaultRaid, VaultPlayer paramVaultPlayer, ServerWorld paramServerWorld);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\condition\IVaultCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */