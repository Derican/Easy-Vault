package iskallia.vault.world.data;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.RelicPartItem;
import iskallia.vault.util.RelicSet;
import iskallia.vault.util.nbt.NBTHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultSetsData extends WorldSavedData {
    private Map<UUID, Set<String>> playerData = new HashMap<>();
    protected static final String DATA_NAME = "the_vault_VaultSets";

    public VaultSetsData() {
        super("the_vault_VaultSets");
    }

    public VaultSetsData(String name) {
        super(name);
    }

    public Set<String> getCraftedSets(UUID playerId) {
        return this.playerData.computeIfAbsent(playerId, uuid -> new HashSet());
    }

    public int getExtraTime(UUID playerId) {
        return getCraftedSets(playerId).size() * ModConfigs.VAULT_RELICS.getExtraTickPerSet();
    }

    public boolean markSetAsCrafted(UUID playerId, RelicSet relicSet) {
        Set<String> craftedSets = getCraftedSets(playerId);
        setDirty();
        return craftedSets.add(relicSet.getId().toString());
    }

    @SubscribeEvent
    public static void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        PlayerEntity player = event.getPlayer();

        if (player.level.isClientSide)
            return;
        IInventory craftingMatrix = event.getInventory();
        ItemStack craftedItemstack = event.getCrafting();

        if (craftedItemstack.getItem() != ModBlocks.RELIC_STATUE_BLOCK_ITEM) {
            return;
        }
        for (int i = 0; i < craftingMatrix.getContainerSize(); i++) {
            ItemStack stackInSlot = craftingMatrix.getItem(i);
            if (stackInSlot != ItemStack.EMPTY) {
                Item item = stackInSlot.getItem();
                if (item instanceof RelicPartItem) {
                    RelicPartItem relicPart = (RelicPartItem) item;
                    VaultSetsData vaultSetsData = get((ServerWorld) player.level);
                    vaultSetsData.markSetAsCrafted(player.getUUID(), relicPart.getRelicSet());
                    break;
                }
            }
        }
    }

    public void load(CompoundNBT nbt) {
        this.playerData = NBTHelper.readMap(nbt, "Sets", ListNBT.class, list -> (Set) IntStream.range(0, list.size()).mapToObj(list::getString).collect(Collectors.toSet()));
    }


    public CompoundNBT save(CompoundNBT compound) {
        NBTHelper.writeMap(compound, "Sets", this.playerData, ListNBT.class, strings -> {
            ListNBT list = new ListNBT();

            strings.forEach(());
            return list;
        });
        return compound;
    }

    public static VaultSetsData get(ServerWorld world) {
        return (VaultSetsData) world.getServer().overworld()
                .getDataStorage().computeIfAbsent(VaultSetsData::new, "the_vault_VaultSets");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\VaultSetsData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */