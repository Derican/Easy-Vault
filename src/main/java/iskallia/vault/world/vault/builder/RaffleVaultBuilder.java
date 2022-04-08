package iskallia.vault.world.vault.builder;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.util.NameProviderPublic;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayerType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class RaffleVaultBuilder
        extends VaultRaidBuilder {
    private static final RaffleVaultBuilder INSTANCE = new RaffleVaultBuilder();


    public static RaffleVaultBuilder getInstance() {
        return INSTANCE;
    }


    public VaultRaid.Builder initializeBuilder(ServerWorld world, ServerPlayerEntity player, CrystalData crystal) {
        VaultRaid.Builder builder = getDefaultBuilder(crystal, world, player).set(VaultRaid.IS_RAFFLE, Boolean.valueOf(true));

        String playerBossName = crystal.getPlayerBossName();
        builder.set(VaultRaid.PLAYER_BOSS_NAME, playerBossName.isEmpty() ? NameProviderPublic.getRandomName() : playerBossName);

        builder.addPlayer(VaultPlayerType.RUNNER, player);
        builder.set(VaultRaid.HOST, player.getUUID());
        return builder;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\builder\RaffleVaultBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */