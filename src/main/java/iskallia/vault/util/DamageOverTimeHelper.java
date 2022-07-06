package iskallia.vault.util;

import iskallia.vault.event.ActiveFlags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageOverTimeHelper {
    private static final Map<RegistryKey<World>, List<DamageOverTimeEntry>> worldEntries;

    public static void applyDamageOverTime(final LivingEntity target, final DamageSource damageSource, final float totalDamage, final int seconds) {
        ServerScheduler.INSTANCE.schedule(1, () -> {
            final DamageOverTimeEntry entry = new DamageOverTimeEntry(seconds * 20, damageSource, target.getId(), totalDamage / seconds);
            DamageOverTimeHelper.worldEntries.computeIfAbsent((RegistryKey<World>) target.getCommandSenderWorld().dimension(), key -> new ArrayList()).add(entry);
        });
    }

    public static void invalidateAll(final LivingEntity target) {
        getDotEntries((Entity) target).forEach(rec$ -> ((DamageOverTimeEntry) rec$).invalidate());
    }

    public static List<DamageOverTimeEntry> getDotEntries(final Entity entity) {
        final World entityWorld = entity.getCommandSenderWorld();
        final List<DamageOverTimeEntry> allEntries = DamageOverTimeHelper.worldEntries.get(entityWorld.dimension());
        final List<DamageOverTimeEntry> entries = new LinkedList<DamageOverTimeEntry>();
        if (allEntries == null) {
            return entries;
        }
        for (final DamageOverTimeEntry entry : allEntries) {
            if (entry.entityId == entity.getId()) {
                entries.add(entry);
            }
        }
        return entries;
    }

    @SubscribeEvent
    public static void onWorldTick(final TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            return;
        }
        final World world = event.world;
        if (world.isClientSide()) {
            return;
        }
        final List<DamageOverTimeEntry> entries = DamageOverTimeHelper.worldEntries.computeIfAbsent((RegistryKey<World>) world.dimension(), key -> new ArrayList());
        entries.forEach(rec$ -> ((DamageOverTimeEntry) rec$).decrement());
        ActiveFlags.IS_DOT_ATTACKING.runIfNotSet(() -> entries.forEach(entry -> {
            if (entry.valid && entry.ticks % 20 == 0) {
                final Entity e = world.getEntity(entry.entityId);
                if (e instanceof LivingEntity && e.isAlive()) {
                    DamageUtil.shotgunAttack(e, entity -> entity.hurt(entry.source, entry.damagePerSecond));
                } else {
                    entry.invalidate();
                }
            }
        }));
        entries.removeIf(entry -> !entry.valid);
    }

    static {
        worldEntries = new HashMap<RegistryKey<World>, List<DamageOverTimeEntry>>();
    }

    private static class DamageOverTimeEntry {
        private int ticks;
        private final DamageSource source;
        private final int entityId;
        private final float damagePerSecond;
        private boolean valid;

        public DamageOverTimeEntry(final int ticks, final DamageSource source, final int entityId, final float damagePerSecond) {
            this.valid = true;
            this.ticks = ticks;
            this.source = source;
            this.entityId = entityId;
            this.damagePerSecond = damagePerSecond;
        }

        private void decrement() {
            --this.ticks;
            this.valid = (this.valid && this.ticks > 0);
        }

        private void invalidate() {
            this.valid = false;
        }
    }
}
