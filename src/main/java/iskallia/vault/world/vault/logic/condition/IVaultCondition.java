// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.condition;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;

@FunctionalInterface
public interface IVaultCondition
{
    boolean test(final VaultRaid p0, final VaultPlayer p1, final ServerWorld p2);
    
    default IVaultCondition negate() {
        return (vault, player, world) -> !this.test(vault, player, world);
    }
    
    default IVaultCondition and(final IVaultCondition other) {
        Objects.requireNonNull(other);
        return (vault, player, world) -> this.test(vault, player, world) && other.test(vault, player, world);
    }
    
    default IVaultCondition or(final IVaultCondition other) {
        Objects.requireNonNull(other);
        return (vault, player, world) -> this.test(vault, player, world) || other.test(vault, player, world);
    }
    
    default IVaultCondition xor(final IVaultCondition other) {
        Objects.requireNonNull(other);
        return (vault, player, world) -> {
            final boolean a = this.test(vault, player, world);
            final boolean b = other.test(vault, player, world);
            return (!a && b) || (a && !b);
        };
    }
}
