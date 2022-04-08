package iskallia.vault.world.data;

import iskallia.vault.attribute.BooleanAttribute;
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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PhoenixSetSnapshotData
        extends InventorySnapshotData {
    private static final String RESTORE_FLAG = "the_vault_restore_phoenixset";
    protected static final String DATA_NAME = "the_vault_PhoenixSet";

    public PhoenixSetSnapshotData() {
        super("the_vault_PhoenixSet");
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
        if (!player.getTags().contains("the_vault_restore_phoenixset")) {
            return;
        }
        ServerWorld world = (ServerWorld) event.player.level;
        PhoenixSetSnapshotData data = get(world);
        if (data.hasSnapshot(player)) {
            data.restoreSnapshot(player);
        }
        player.removeTag("the_vault_restore_phoenixset");
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity) ||
                !((event.getEntity()).level instanceof ServerWorld)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        ServerWorld world = (ServerWorld) player.level;

        if (PlayerSet.isActive(VaultGear.Set.PHOENIX, (LivingEntity) player)) {
            PhoenixSetSnapshotData data = get(world);
            if (data.hasSnapshot((PlayerEntity) player)) {
                player.addTag("the_vault_restore_phoenixset");
            }
        }
    }

    public static PhoenixSetSnapshotData get(ServerWorld world) {
        return get(world.getServer());
    }

    public static PhoenixSetSnapshotData get(MinecraftServer srv) {
        return (PhoenixSetSnapshotData) srv.overworld().getDataStorage().computeIfAbsent(PhoenixSetSnapshotData::new, "the_vault_PhoenixSet");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\PhoenixSetSnapshotData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */