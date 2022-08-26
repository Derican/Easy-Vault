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
import java.time.chrono.ChronoZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DailyScheduler {
    private static DailyScheduler scheduler;
    private final ScheduledExecutorService executorService;

    private DailyScheduler() {
        this.executorService = Executors.newScheduledThreadPool(1);
    }

    public static void start(final FMLServerStartingEvent event) {
        ModTasks.initTasks(DailyScheduler.scheduler = new DailyScheduler(), event.getServer());
    }

    public void scheduleServer(final int hour, final Runnable task) {
        this.scheduleServer(hour, 0, 0, task);
    }

    public void scheduleServer(final int hour, final int minute, final int second, final Runnable task) {
        if (DailyScheduler.scheduler == null) {
            throw new IllegalStateException("Startup not finished, Scheduler not running!");
        }
        final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime nextRun = now.withHour(hour).withMinute(minute).withSecond(second);
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1L);
        }
        DailyScheduler.scheduler.executorService.scheduleAtFixedRate(() -> {
            final MinecraftServer srv = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
            srv.execute(task);
        }, Duration.between(now, nextRun).getSeconds(), TimeUnit.DAYS.toSeconds(1L), TimeUnit.SECONDS);
    }

    public static void stop(final FMLServerStoppingEvent event) {
        DailyScheduler.scheduler.executorService.shutdown();
        try {
            DailyScheduler.scheduler.executorService.awaitTermination(1L, TimeUnit.SECONDS);
        } catch (final InterruptedException ex) {
            Vault.LOGGER.error(ex);
        }
        DailyScheduler.scheduler = null;
    }
}
