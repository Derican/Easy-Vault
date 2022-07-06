// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.builder;

import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.gen.VaultGenerator;
import java.util.function.Supplier;
import iskallia.vault.world.vault.player.VaultPlayerType;
import java.util.function.Function;
import java.util.Optional;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.item.crystal.CrystalData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class FinalBossBuilder extends VaultRaidBuilder
{
    private static final FinalBossBuilder INSTANCE;
    
    private FinalBossBuilder() {
    }
    
    public static FinalBossBuilder getInstance() {
        return FinalBossBuilder.INSTANCE;
    }
    
    @Override
    public VaultRaid.Builder initializeBuilder(final ServerWorld world, final ServerPlayerEntity player, final CrystalData crystal) {
        final VaultRaid.Builder builder = this.getDefaultBuilder(crystal, world, player);
        final VaultRaid vault = VaultRaidData.get(world).getActiveFor(player);
        if (vault == null) {
            return null;
        }
        vault.getPlayers().stream().map(p -> p.getServerPlayer(world.getServer())).filter(Optional::isPresent).map(Optional::get).forEach(sPlayer -> builder.addPlayer(VaultPlayerType.RUNNER, sPlayer));
        builder.set(VaultRaid.HOST, player.getUUID());
        builder.setGenerator(VaultRaid.FINAL_BOSS);
        return builder;
    }
    
    static {
        INSTANCE = new FinalBossBuilder();
    }
}
