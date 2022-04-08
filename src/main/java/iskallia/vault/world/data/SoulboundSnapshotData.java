package iskallia.vault.world.data;

import iskallia.vault.attribute.BooleanAttribute;
import iskallia.vault.init.ModAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulboundSnapshotData
        extends InventorySnapshotData {
    public SoulboundSnapshotData() {
        super("the_vault_Soulbound");
    }

    protected static final String DATA_NAME = "the_vault_Soulbound";

    protected boolean shouldSnapshotItem(PlayerEntity player, ItemStack stack) {
        return ((Boolean) ((BooleanAttribute) ModAttributes.SOULBOUND.getOrDefault(stack, Boolean.valueOf(false))).getValue(stack)).booleanValue();
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (!player.isAlive() || !(player.level instanceof ServerWorld)) {
            return;
        }
        ServerWorld world = (ServerWorld) event.player.level;
        SoulboundSnapshotData data = get(world);
        if (data.hasSnapshot(player)) {
            data.restoreSnapshot(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity) ||
                !((event.getEntity()).level instanceof ServerWorld)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) event.getEntity();
        ServerWorld world = (ServerWorld) player.level;
        SoulboundSnapshotData data = get(world);
        if (!data.hasSnapshot(player)) {
            data.createSnapshot(player);
        }
    }

    public static SoulboundSnapshotData get(ServerWorld world) {
        return (SoulboundSnapshotData) world.getServer().overworld().getDataStorage().computeIfAbsent(SoulboundSnapshotData::new, "the_vault_Soulbound");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\SoulboundSnapshotData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */