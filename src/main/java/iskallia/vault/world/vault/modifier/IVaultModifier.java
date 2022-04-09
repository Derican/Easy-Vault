// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.modifier;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public interface IVaultModifier
{
    void apply(final VaultRaid p0, final VaultPlayer p1, final ServerWorld p2, final Random p3);
    
    void remove(final VaultRaid p0, final VaultPlayer p1, final ServerWorld p2, final Random p3);
    
    void tick(final VaultRaid p0, final VaultPlayer p1, final ServerWorld p2);
}
