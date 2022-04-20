package iskallia.vault.world.data;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.set.PlayerSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PhoenixSetSnapshotData extends InventorySnapshotData {
    private static final String RESTORE_FLAG = "the_vault_restore_phoenixset";
    protected static final String DATA_NAME = "the_vault_PhoenixSet";

    public PhoenixSetSnapshotData() {
        super("the_vault_PhoenixSet");
    }

    @Override
    protected boolean shouldSnapshotItem(final PlayerEntity player, final ItemStack stack) {
        return !stack.isEmpty() && !ModAttributes.SOULBOUND.getOrDefault(stack, false).getValue(stack);
    }

    @Override
    protected Builder makeSnapshotBuilder(final PlayerEntity player) {
        return new Builder(player).setStackFilter(this::shouldSnapshotItem);
    }

    @SubscribeEvent
    public static void onTick(final TickEvent.PlayerTickEvent event) {
        final PlayerEntity player = event.player;
        if (!player.isAlive() || !(player.level instanceof ServerWorld)) {
            return;
        }
        if (!player.getTags().contains("the_vault_restore_phoenixset")) {
            return;
        }
        final ServerWorld world = (ServerWorld) event.player.level;
        final PhoenixSetSnapshotData data = get(world);
        if (data.hasSnapshot(player)) {
            data.restoreSnapshot(player);
        }
        player.removeTag("the_vault_restore_phoenixset");
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDeath(final LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity) || !(event.getEntity().level instanceof ServerWorld)) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        final ServerWorld world = (ServerWorld) player.level;
        if (PlayerSet.isActive(VaultGear.Set.PHOENIX, (LivingEntity) player)) {
            final PhoenixSetSnapshotData data = get(world);
            if (data.hasSnapshot((PlayerEntity) player)) {
                player.addTag("the_vault_restore_phoenixset");
            }
        }
    }

    public static PhoenixSetSnapshotData get(final ServerWorld world) {
        return get(world.getServer());
    }

    public static PhoenixSetSnapshotData get(final MinecraftServer srv) {
        return (PhoenixSetSnapshotData) srv.overworld().getDataStorage().computeIfAbsent((Supplier) PhoenixSetSnapshotData::new, "the_vault_PhoenixSet");
    }
}
