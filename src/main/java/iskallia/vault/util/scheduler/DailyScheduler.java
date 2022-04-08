package iskallia.vault.util.scheduler;

import iskallia.vault.Vault;
import iskallia.vault.init.ModTasks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class DailyScheduler {
    private static DailyScheduler scheduler;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);


    public static void start(FMLServerStartingEvent event) {
        scheduler = new DailyScheduler();
        ModTasks.initTasks(scheduler, event.getServer());
    }

    public void scheduleServer(int hour, Runnable task) {
        scheduleServer(hour, 0, 0, task);
    }

    public void scheduleServer(int hour, int minute, int second, Runnable task) {
        if (scheduler == null) {
            throw new IllegalStateException("Startup not finished, Scheduler not running!");
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime nextRun = now.withHour(hour).withMinute(minute).withSecond(second);
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1L);
        }

        scheduler.executorService.scheduleAtFixedRate(() -> {
            MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);

            srv.execute(task);
        }Duration.between(now, nextRun).getSeconds(), TimeUnit.DAYS
                .toSeconds(1L), TimeUnit.SECONDS);
    }


    public static void stop(FMLServerStoppingEvent event) {
        scheduler.executorService.shutdown();
        try {
            scheduler.executorService.awaitTermination(1L, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Vault.LOGGER.error(ex);
        }
        scheduler = null;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\scheduler\DailyScheduler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */