package iskallia.vault.world.data;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.modifier.InventoryRestoreModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PhoenixModifierSnapshotData extends InventorySnapshotData {
    private static final String RESTORE_FLAG = "the_vault_restore_inventory";
    protected static final String DATA_NAME = "the_vault_PhoenixModifier";

    public PhoenixModifierSnapshotData() {
        super("the_vault_PhoenixModifier");
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
        if (!player.getTags().contains("the_vault_restore_inventory")) {
            return;
        }
        final ServerWorld world = (ServerWorld) event.player.level;
        final PhoenixModifierSnapshotData data = get(world);
        if (data.hasSnapshot(player)) {
            data.restoreSnapshot(player);
        }
        player.removeTag("the_vault_restore_inventory");
    }

    @SubscribeEvent
    public static void onDeath(final LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity) || !(event.getEntity().level instanceof ServerWorld)) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        final ServerWorld world = (ServerWorld) player.level;
        final VaultRaid currentRaid = VaultRaidData.get(world).getActiveFor(player);
        if (currentRaid != null && !currentRaid.getActiveModifiersFor(PlayerFilter.of((PlayerEntity) player), InventoryRestoreModifier.class).isEmpty()) {
            final PhoenixModifierSnapshotData data = get(world);
            if (data.hasSnapshot((PlayerEntity) player)) {
                player.addTag("the_vault_restore_inventory");
            }
        }
    }

    public static PhoenixModifierSnapshotData get(final ServerWorld world) {
        return (PhoenixModifierSnapshotData) world.getServer().overworld().getDataStorage().computeIfAbsent((Supplier) PhoenixModifierSnapshotData::new, "the_vault_PhoenixModifier");
    }
}
