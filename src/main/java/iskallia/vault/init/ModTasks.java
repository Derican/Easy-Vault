package iskallia.vault.init;

import iskallia.vault.util.scheduler.DailyScheduler;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.data.SoulShardTraderData;
import net.minecraft.server.MinecraftServer;


public class ModTasks {
    public static void initTasks(DailyScheduler scheduler, MinecraftServer server) {
        scheduler.scheduleServer(3, () -> SoulShardTraderData.get(server).resetDailyTrades());
        scheduler.scheduleServer(15, () -> SoulShardTraderData.get(server).resetDailyTrades());


        scheduler.scheduleServer(0, () -> PlayerVaultStatsData.get(server).generateRecordRewards(server.overworld()));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModTasks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */