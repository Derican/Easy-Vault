// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.builder;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayerType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class TroveVaultBuilder extends VaultRaidBuilder
{
    private static final TroveVaultBuilder INSTANCE;
    
    private TroveVaultBuilder() {
    }
    
    public static TroveVaultBuilder getInstance() {
        return TroveVaultBuilder.INSTANCE;
    }
    
    @Override
    public VaultRaid.Builder initializeBuilder(final ServerWorld world, final ServerPlayerEntity player, final CrystalData crystal) {
        final VaultRaid.Builder builder = this.getDefaultBuilder(crystal, world, player);
        builder.addPlayer(VaultPlayerType.RUNNER, player);
        builder.set(VaultRaid.HOST, player.getUUID());
        return builder;
    }
    
    @Override
    protected VaultRaid.Builder getDefaultBuilder(final CrystalData crystal, final ServerWorld world, final ServerPlayerEntity player) {
        return super.getDefaultBuilder(crystal, 0, VaultRaid.VAULT_TROVE.get());
    }
    
    static {
        INSTANCE = new TroveVaultBuilder();
    }
}
