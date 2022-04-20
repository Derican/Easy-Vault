package iskallia.vault.world.vault.builder;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayerType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class ClassicVaultBuilder extends VaultRaidBuilder {
    private static final ClassicVaultBuilder INSTANCE;

    private ClassicVaultBuilder() {
    }

    public static ClassicVaultBuilder getInstance() {
        return ClassicVaultBuilder.INSTANCE;
    }

    @Override
    public VaultRaid.Builder initializeBuilder(final ServerWorld world, final ServerPlayerEntity player, final CrystalData crystal) {
        final VaultRaid.Builder builder = this.getDefaultBuilder(crystal, world, player);
        builder.addPlayer(VaultPlayerType.RUNNER, player);
        builder.set(VaultRaid.HOST, player.getUUID());
        return builder;
    }

    static {
        INSTANCE = new ClassicVaultBuilder();
    }
}
