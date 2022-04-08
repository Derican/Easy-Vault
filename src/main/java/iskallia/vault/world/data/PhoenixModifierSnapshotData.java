package iskallia.vault.world.data;

import iskallia.vault.attribute.BooleanAttribute;
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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PhoenixModifierSnapshotData extends InventorySnapshotData {
    private static final String RESTORE_FLAG = "the_vault_restore_inventory";
    protected static final String DATA_NAME = "the_vault_PhoenixModifier";

    public PhoenixModifierSnapshotData() {
        super("the_vault_PhoenixModifier");
    }


    protected boolean shouldSnapshotItem(PlayerEntity player, ItemStack stack) {
        return (!stack.isEmpty() && !((Boolean) ((BooleanAttribute) ModAttributes.SOULBOUND.getOrDefault(stack, Boolean.valueOf(false))).getValue(stack)).booleanValue());
    }


    protected InventorySnapshotData.Builder makeSnapshotBuilder(PlayerEntity player) {
        return (new InventorySnapshotData.Builder(player)).setStackFilter(this::shouldSnapshotItem);
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (!player.isAlive() || !(player.level instanceof ServerWorld)) {
            return;
        }
        if (!player.getTags().contains("the_vault_restore_inventory")) {
            return;
        }
        ServerWorld world = (ServerWorld) event.player.level;
        PhoenixModifierSnapshotData data = get(world);
        if (data.hasSnapshot(player)) {
            data.restoreSnapshot(player);
        }
        player.removeTag("the_vault_restore_inventory");
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity) ||
                !((event.getEntity()).level instanceof ServerWorld)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        ServerWorld world = (ServerWorld) player.level;

        VaultRaid currentRaid = VaultRaidData.get(world).getActiveFor(player);
        if (currentRaid != null && !currentRaid.getActiveModifiersFor(PlayerFilter.of(new PlayerEntity[]{(PlayerEntity) player}, ), InventoryRestoreModifier.class).isEmpty()) {
            PhoenixModifierSnapshotData data = get(world);
            if (data.hasSnapshot((PlayerEntity) player)) {
                player.addTag("the_vault_restore_inventory");
            }
        }
    }

    public static PhoenixModifierSnapshotData get(ServerWorld world) {
        return (PhoenixModifierSnapshotData) world.getServer().overworld().getDataStorage().computeIfAbsent(PhoenixModifierSnapshotData::new, "the_vault_PhoenixModifier");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\PhoenixModifierSnapshotData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */