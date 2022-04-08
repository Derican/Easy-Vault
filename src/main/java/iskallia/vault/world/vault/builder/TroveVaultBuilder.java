package iskallia.vault.world.vault.builder;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayerType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class TroveVaultBuilder
        extends VaultRaidBuilder {
    private static final TroveVaultBuilder INSTANCE = new TroveVaultBuilder();


    public static TroveVaultBuilder getInstance() {
        return INSTANCE;
    }


    public VaultRaid.Builder initializeBuilder(ServerWorld world, ServerPlayerEntity player, CrystalData crystal) {
        VaultRaid.Builder builder = getDefaultBuilder(crystal, world, player);
        builder.addPlayer(VaultPlayerType.RUNNER, player);
        builder.set(VaultRaid.HOST, player.getUUID());
        return builder;
    }


    protected VaultRaid.Builder getDefaultBuilder(CrystalData crystal, ServerWorld world, ServerPlayerEntity player) {
        return getDefaultBuilder(crystal, 0, VaultRaid.VAULT_TROVE.get());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\builder\TroveVaultBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */