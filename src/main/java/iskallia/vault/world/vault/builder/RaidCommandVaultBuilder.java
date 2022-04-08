package iskallia.vault.world.vault.builder;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.VaultLogic;
import iskallia.vault.world.vault.player.VaultPlayerType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;


public class RaidCommandVaultBuilder
        extends VaultRaidBuilder {
    public static RaidCommandVaultBuilder get() {
        return new RaidCommandVaultBuilder();
    }


    public VaultRaid.Builder initializeBuilder(ServerWorld world, ServerPlayerEntity player, CrystalData crystal) {
        return VaultRaid.builder(VaultLogic.CLASSIC, 0, VaultRaid.SUMMON_AND_KILL_BOSS.get())
                .setInitializer(getDefaultInitializer())
                .addEvents(getDefaultEvents())
                .addPlayer(VaultPlayerType.RUNNER, player);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\builder\RaidCommandVaultBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */