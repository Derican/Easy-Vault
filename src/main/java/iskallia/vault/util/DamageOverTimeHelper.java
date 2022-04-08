package iskallia.vault.util;

import iskallia.vault.event.ActiveFlags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageOverTimeHelper {
    private static final Map<RegistryKey<World>, List<DamageOverTimeEntry>> worldEntries = new HashMap<>();

    public static void applyDamageOverTime(LivingEntity target, DamageSource damageSource, float totalDamage, int seconds) {
        ServerScheduler.INSTANCE.schedule(1, () -> {
            DamageOverTimeEntry entry = new DamageOverTimeEntry(seconds * 20, damageSource, target.getId(), totalDamage / seconds);
            ((List<DamageOverTimeEntry>) worldEntries.computeIfAbsent(target.getCommandSenderWorld().dimension(), ())).add(entry);
        });
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            return;
        }
        World world = event.world;
        if (world.isClientSide()) {
            return;
        }

        List<DamageOverTimeEntry> entries = worldEntries.computeIfAbsent(world.dimension(), key -> new ArrayList());
        entries.forEach(rec$ -> ((DamageOverTimeEntry) rec$).decrement());
        ActiveFlags.IS_DOT_ATTACKING.runIfNotSet(() -> entries.forEach(()));


        entries.removeIf(entry -> !entry.valid);
    }


    private static class DamageOverTimeEntry {
        private int ticks;
        private final DamageSource source;
        private final int entityId;
        private final float damagePerSecond;
        private boolean valid = true;

        public DamageOverTimeEntry(int ticks, DamageSource source, int entityId, float damagePerSecond) {
            this.ticks = ticks;
            this.source = source;
            this.entityId = entityId;
            this.damagePerSecond = damagePerSecond;
        }

        private void decrement() {
            this.ticks--;
            this.valid = (this.valid && this.ticks > 0);
        }

        private void invalidate() {
            this.valid = false;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\DamageOverTimeHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */