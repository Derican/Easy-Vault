package iskallia.vault.init;

import iskallia.vault.util.scheduler.DailyScheduler;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.data.SoulShardTraderData;
import net.minecraft.server.MinecraftServer;

public class ModTasks {
    public static void initTasks(final DailyScheduler scheduler, final MinecraftServer server) {
        scheduler.scheduleServer(3, () -> SoulShardTraderData.get(server).resetDailyTrades());
        scheduler.scheduleServer(15, () -> SoulShardTraderData.get(server).resetDailyTrades());
        scheduler.scheduleServer(0, () -> PlayerVaultStatsData.get(server).generateRecordRewards(server.overworld()));
    }
}
