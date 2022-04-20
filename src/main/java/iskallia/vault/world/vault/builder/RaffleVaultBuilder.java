package iskallia.vault.world.vault.builder;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.util.NameProviderPublic;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayerType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class RaffleVaultBuilder extends VaultRaidBuilder {
    private static final RaffleVaultBuilder INSTANCE;

    private RaffleVaultBuilder() {
    }

    public static RaffleVaultBuilder getInstance() {
        return RaffleVaultBuilder.INSTANCE;
    }

    @Override
    public VaultRaid.Builder initializeBuilder(final ServerWorld world, final ServerPlayerEntity player, final CrystalData crystal) {
        final VaultRaid.Builder builder = this.getDefaultBuilder(crystal, world, player).set(VaultRaid.IS_RAFFLE, true);
        final String playerBossName = crystal.getPlayerBossName();
        builder.set(VaultRaid.PLAYER_BOSS_NAME, playerBossName.isEmpty() ? NameProviderPublic.getRandomName() : playerBossName);
        builder.addPlayer(VaultPlayerType.RUNNER, player);
        builder.set(VaultRaid.HOST, player.getUUID());
        return builder;
    }

    static {
        INSTANCE = new RaffleVaultBuilder();
    }
}
