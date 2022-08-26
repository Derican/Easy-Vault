// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.builder;

import iskallia.vault.item.crystal.FrameData;
import java.util.Iterator;
import iskallia.vault.world.vault.gen.VaultGenerator;
import java.util.function.Supplier;
import iskallia.vault.world.vault.player.VaultPlayerType;
import java.util.Collection;
import java.util.ArrayList;
import iskallia.vault.world.data.VaultPartyData;
import net.minecraft.world.GameRules;
import iskallia.vault.init.ModGameRules;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import iskallia.vault.init.ModBlocks;
import java.util.UUID;
import java.util.Set;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.item.crystal.CrystalData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class FinalLobbyBuilder extends VaultRaidBuilder
{
    private static final FinalLobbyBuilder INSTANCE;
    
    private FinalLobbyBuilder() {
    }
    
    public static FinalLobbyBuilder getInstance() {
        return FinalLobbyBuilder.INSTANCE;
    }
    
    @Override
    public VaultRaid.Builder initializeBuilder(final ServerWorld world, final ServerPlayerEntity player, final CrystalData crystal) {
        final VaultRaid.Builder builder = this.getDefaultBuilder(crystal, world, player);
        final Set<UUID> players = crystal.getFrameData().tiles.stream().filter(tile -> tile.block == ModBlocks.FINAL_VAULT_FRAME).filter(tile -> tile.data.contains("OwnerUUID", 8)).map(tile -> UUID.fromString(tile.data.getString("OwnerUUID"))).collect(Collectors.toSet());
        if (!players.contains(player.getUUID())) {
            return null;
        }
        for (final UUID uuid : players) {
            final ServerPlayerEntity vaultPlayer = world.getServer().getPlayerList().getPlayer(uuid);
            if (vaultPlayer == null) {
                return null;
            }
        }
        if (world.getGameRules().getBoolean(ModGameRules.FINAL_VAULT_ALLOW_PARTY)) {
            final VaultPartyData data = VaultPartyData.get(world);
            for (final UUID uuid2 : new ArrayList<>(players)) {
                data.getParty(uuid2).ifPresent(party -> players.addAll(party.getMembers()));
            }
        }
        for (final UUID uuid : players) {
            final ServerPlayerEntity partyPlayer = world.getServer().getPlayerList().getPlayer(uuid);
            if (partyPlayer != null) {
                builder.addPlayer(VaultPlayerType.RUNNER, partyPlayer);
            }
        }
        builder.set(VaultRaid.HOST, player.getUUID());
        builder.setGenerator(VaultRaid.FINAL_LOBBY);
        return builder;
    }
    
    static {
        INSTANCE = new FinalLobbyBuilder();
    }
}
