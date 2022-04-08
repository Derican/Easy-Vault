package iskallia.vault.world.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.*;

@EventBusSubscriber
public class ScheduledItemDropData
        extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_ScheduledItemDrops";
    private final Map<UUID, List<ItemStack>> scheduledItems = new HashMap<>();

    public ScheduledItemDropData() {
        super("the_vault_ScheduledItemDrops");
    }

    public void addDrop(PlayerEntity player, ItemStack toDrop) {
        addDrop(player.getUUID(), toDrop);
    }

    public void addDrop(UUID playerUUID, ItemStack toDrop) {
        ((List<ItemStack>) this.scheduledItems.computeIfAbsent(playerUUID, key -> new ArrayList()))
                .add(toDrop.copy());
        setDirty();
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            ScheduledItemDropData data = get(player.getLevel());
            if (data.scheduledItems.isEmpty()) {
                return;
            }

            if (data.scheduledItems.containsKey(player.getUUID())) {
                List<ItemStack> drops = data.scheduledItems.get(player.getUUID());
                while (!drops.isEmpty() && player.inventory.getFreeSlot() != -1) {
                    ItemStack drop = drops.get(0);
                    if (player.inventory.add(drop)) {
                        drops.remove(0);
                        data.setDirty();
                    }
                }


                if (drops.isEmpty()) {
                    data.scheduledItems.remove(player.getUUID());
                    data.setDirty();
                }
            }
        }
    }


    public void load(CompoundNBT tag) {
        this.scheduledItems.clear();
        CompoundNBT savTag = tag.getCompound("drops");
        for (String key : savTag.getAllKeys()) {
            UUID playerUUID;
            try {
                playerUUID = UUID.fromString(key);
            } catch (IllegalArgumentException exc) {
                continue;
            }
            List<ItemStack> drops = new ArrayList<>();
            ListNBT dropsList = savTag.getList(key, 10);
            for (int i = 0; i < dropsList.size(); i++) {
                drops.add(ItemStack.of(dropsList.getCompound(i)));
            }
            this.scheduledItems.put(playerUUID, drops);
        }
    }


    public CompoundNBT save(CompoundNBT tag) {
        CompoundNBT savTag = new CompoundNBT();
        this.scheduledItems.forEach((uuid, drops) -> {
            ListNBT dropsList = new ListNBT();
            drops.forEach(());
            savTag.put(uuid.toString(), (INBT) dropsList);
        });
        tag.put("drops", (INBT) savTag);
        return tag;
    }

    public static ScheduledItemDropData get(ServerWorld world) {
        return get(world.getServer());
    }

    public static ScheduledItemDropData get(MinecraftServer srv) {
        return (ScheduledItemDropData) srv.overworld().getDataStorage().computeIfAbsent(ScheduledItemDropData::new, "the_vault_ScheduledItemDrops");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\ScheduledItemDropData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */