package iskallia.vault.world.vault.builder;

import iskallia.vault.world.data.PlayerVaultStatsData;

import java.util.Optional;

import iskallia.vault.world.vault.player.VaultPlayerType;

import java.util.Collection;

import iskallia.vault.util.MiscUtils;

import java.util.UUID;

import iskallia.vault.world.data.VaultPartyData;
import iskallia.vault.world.vault.logic.objective.ancient.AncientObjective;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.item.crystal.CrystalData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class CoopVaultBuilder extends VaultRaidBuilder {
    private static final CoopVaultBuilder INSTANCE;

    private CoopVaultBuilder() {
    }

    public static CoopVaultBuilder getInstance() {
        return CoopVaultBuilder.INSTANCE;
    }

    @Override
    public VaultRaid.Builder initializeBuilder(final ServerWorld world, final ServerPlayerEntity player, final CrystalData crystal) {
        final VaultRaid.Builder builder = this.getDefaultBuilder(crystal, world, player);
        final boolean isAncientVault = VaultRaid.ANCIENTS.get().getId().equals(crystal.getSelectedObjective());
        if (player != null) {
            final Optional<VaultPartyData.Party> partyOpt = VaultPartyData.get(world).getParty(player.getUUID());
            if (partyOpt.isPresent() && partyOpt.get().getMembers().size() > 1 && !isAncientVault && !crystal.isChallenge()) {
                final VaultPartyData.Party party = partyOpt.get();
                final UUID leader = (party.getLeader() != null) ? party.getLeader() : MiscUtils.getRandomEntry(party.getMembers(), world.getRandom());
                builder.set(VaultRaid.HOST, leader);
                party.getMembers().forEach(uuid -> {
                    final ServerPlayerEntity partyPlayer = world.getServer().getPlayerList().getPlayer(uuid);
                    if (partyPlayer != null) {
                        builder.addPlayer(VaultPlayerType.RUNNER, partyPlayer);
                    }
                    return;
                });
            } else {
                builder.addPlayer(VaultPlayerType.RUNNER, player);
                builder.set(VaultRaid.HOST, player.getUUID());
            }
        }
        builder.setLevelInitializer(VaultRaid.INIT_LEVEL_COOP);
        return builder;
    }

    @Override
    protected int getVaultLevelForObjective(final ServerWorld world, final ServerPlayerEntity player) {
        return (player == null) ? 0 : VaultPartyData.get(world).getParty(player.getUUID()).map(party -> {
            final UUID leader = (party.getLeader() != null) ? party.getLeader() : MiscUtils.getRandomEntry(party.getMembers(), world.getRandom());
            return PlayerVaultStatsData.get(world).getVaultStats(leader).getVaultLevel();
        }).orElse(super.getVaultLevelForObjective(world, player));
    }

    static {
        INSTANCE = new CoopVaultBuilder();
    }
}
