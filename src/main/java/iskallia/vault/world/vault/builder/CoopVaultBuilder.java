package iskallia.vault.world.vault.builder;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.data.VaultPartyData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayerType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.UUID;


public class CoopVaultBuilder
        extends VaultRaidBuilder {
    private static final CoopVaultBuilder INSTANCE = new CoopVaultBuilder();


    public static CoopVaultBuilder getInstance() {
        return INSTANCE;
    }


    public VaultRaid.Builder initializeBuilder(ServerWorld world, ServerPlayerEntity player, CrystalData crystal) {
        VaultRaid.Builder builder = getDefaultBuilder(crystal, world, player);

        Optional<VaultPartyData.Party> partyOpt = VaultPartyData.get(world).getParty(player.getUUID());

        if (partyOpt.isPresent() && ((VaultPartyData.Party) partyOpt.get()).getMembers().size() > 1) {
            VaultPartyData.Party party = partyOpt.get();

            UUID leader = (party.getLeader() != null) ? party.getLeader() : (UUID) MiscUtils.getRandomEntry(party.getMembers(), world.getRandom());
            builder.set(VaultRaid.HOST, leader);

            party.getMembers().forEach(uuid -> {
                ServerPlayerEntity partyPlayer = world.getServer().getPlayerList().getPlayer(uuid);
                if (partyPlayer != null) {
                    builder.addPlayer(VaultPlayerType.RUNNER, partyPlayer);
                }
            });
        } else {
            builder.addPlayer(VaultPlayerType.RUNNER, player);
            builder.set(VaultRaid.HOST, player.getUUID());
        }
        builder.setLevelInitializer(VaultRaid.INIT_LEVEL_COOP);
        return builder;
    }


    protected int getVaultLevelForObjective(ServerWorld world, ServerPlayerEntity player) {
        return ((Integer) VaultPartyData.get(world).getParty(player.getUUID()).map(party -> {
            UUID leader = (party.getLeader() != null) ? party.getLeader() : (UUID) MiscUtils.getRandomEntry(party.getMembers(), world.getRandom());
            return Integer.valueOf(PlayerVaultStatsData.get(world).getVaultStats(leader).getVaultLevel());
        }).orElse(Integer.valueOf(super.getVaultLevelForObjective(world, player)))).intValue();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\builder\CoopVaultBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */