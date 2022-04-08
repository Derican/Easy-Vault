package iskallia.vault.world.data;

import iskallia.vault.integration.IntegrationCurios;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.nbt.VMapNBT;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraftforge.common.extensions.IForgeItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.ModList;

import java.util.*;
import java.util.function.BiPredicate;

public abstract class InventorySnapshotData extends WorldSavedData {
    public VMapNBT<UUID, InventorySnapshot> snapshotData = VMapNBT.ofUUID(() -> new InventorySnapshot());

    protected InventorySnapshotData(String name) {
        super(name);
    }


    protected Builder makeSnapshotBuilder(PlayerEntity player) {
        return (new Builder(player)).setStackFilter(this::shouldSnapshotItem).removeSnapshotItems();
    }

    public boolean hasSnapshot(PlayerEntity player) {
        return hasSnapshot(player.getUUID());
    }

    public boolean hasSnapshot(UUID playerUUID) {
        return this.snapshotData.containsKey(playerUUID);
    }

    public void createSnapshot(PlayerEntity player) {
        if (this.snapshotData.containsKey(player.getUUID())) {
            restoreSnapshot(player);
        }
        this.snapshotData.put(player.getUUID(), makeSnapshotBuilder(player).createSnapshot());
        setDirty();
    }

    public boolean removeSnapshot(PlayerEntity player) {
        return removeSnapshot(player.getUUID());
    }

    public boolean removeSnapshot(UUID playerUUID) {
        if (this.snapshotData.remove(playerUUID) != null) {
            setDirty();
            return true;
        }
        return false;
    }

    public boolean restoreSnapshot(PlayerEntity player) {
        InventorySnapshot snapshot = (InventorySnapshot) this.snapshotData.remove(player.getUUID());
        if (snapshot != null) {
            setDirty();
            return snapshot.apply(player);
        }
        return false;
    }


    public void load(CompoundNBT nbt) {
        this.snapshotData.deserializeNBT(nbt.getList("Players", 10));
    }


    public CompoundNBT save(CompoundNBT compound) {
        compound.put("Players", (INBT) this.snapshotData.serializeNBT());
        return compound;
    }

    protected abstract boolean shouldSnapshotItem(PlayerEntity paramPlayerEntity, ItemStack paramItemStack);

    public static interface InventoryAccessor {
        int getSize();
    }

    public static class InventorySnapshot
            implements INBTSerializable<CompoundNBT> {
        private final VListNBT<Integer, IntNBT> invIds = new VListNBT(IntNBT::valueOf, IntNBT::getAsInt);
        private final VListNBT<ItemStack, CompoundNBT> items = new VListNBT(IForgeItemStack::serializeNBT, ItemStack::of);
        private final Map<String, CompoundNBT> customInventoryData = new HashMap<>();

        private boolean removeSnapshotItems = false;

        private boolean replaceExisting = false;


        private InventorySnapshot(boolean removeSnapshotItems, boolean replaceExisting) {
            this.removeSnapshotItems = removeSnapshotItems;
            this.replaceExisting = replaceExisting;
        }

        private void createSnapshot(PlayerEntity player, BiPredicate<PlayerEntity, ItemStack> stackFilter) {
            for (int slot = 0; slot < ((InventorySnapshotData.InventoryAccessor) player.inventory).getSize(); slot++) {
                ItemStack stack = player.inventory.getItem(slot);
                if (stackFilter.test(player, stack)) {


                    addItemStack(slot, stack);
                    if (this.removeSnapshotItems) {
                        player.inventory.setItem(slot, ItemStack.EMPTY);
                    }
                }
            }
            if (ModList.get().isLoaded("curios")) {
                CompoundNBT curiosData = IntegrationCurios.getMappedSerializedCuriosItemStacks(player, stackFilter, this.removeSnapshotItems);
                this.customInventoryData.put("curios", curiosData);
            }
        }

        private void addItemStack(int slot, ItemStack stack) {
            this.invIds.add(Integer.valueOf(slot));
            this.items.add(stack.copy());
        }

        private boolean apply(PlayerEntity player) {
            if (!player.isAlive()) {
                return false;
            }

            List<ItemStack> addLater = new ArrayList<>();
            for (int index = 0; index < this.items.size(); index++) {
                ItemStack toAdd = ((ItemStack) this.items.get(index)).copy();
                int slot = ((Integer) this.invIds.get(index)).intValue();


                if (this.replaceExisting || player.inventory.getItem(slot).isEmpty()) {
                    player.inventory.setItem(slot, toAdd);
                } else {
                    addLater.add(toAdd);
                }
            }

            if (ModList.get().isLoaded("curios") && this.customInventoryData.containsKey("curios")) {
                CompoundNBT curiosData = this.customInventoryData.get("curios");
                addLater.addAll(IntegrationCurios.applyMappedSerializedCuriosItemStacks(player, curiosData, this.replaceExisting));
            }

            for (ItemStack stack : addLater) {
                if (!player.addItem(stack)) {
                    ItemEntity itementity = player.drop(stack, false);
                    if (itementity != null) {
                        itementity.setNoPickUpDelay();
                        itementity.setOwner(player.getUUID());
                    }
                }
            }
            return true;
        }


        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("InvIds", (INBT) this.invIds.serializeNBT());
            nbt.put("Items", (INBT) this.items.serializeNBT());

            CompoundNBT customData = new CompoundNBT();
            this.customInventoryData.forEach(customData::put);
            nbt.put("customData", (INBT) customData);

            nbt.putBoolean("removeSnapshotItems", this.removeSnapshotItems);
            nbt.putBoolean("replaceExisting", this.replaceExisting);
            return nbt;
        }


        public void deserializeNBT(CompoundNBT nbt) {
            this.invIds.deserializeNBT(nbt.getList("InvIds", 3));
            this.items.deserializeNBT(nbt.getList("Items", 10));

            this.customInventoryData.clear();
            if (nbt.contains("customData", 10)) {
                CompoundNBT customData = nbt.getCompound("customData");
                for (String key : customData.getAllKeys()) {
                    this.customInventoryData.put(key, customData.getCompound(key));
                }
            }

            this.removeSnapshotItems = nbt.getBoolean("removeSnapshotItems");
            this.replaceExisting = nbt.getBoolean("replaceExisting");
        }

        private InventorySnapshot() {
        }
    }

    public static class Builder {
        private final PlayerEntity player;
        private boolean removeSnapshotItems = false;
        private boolean replaceExisting = false;
        private BiPredicate<PlayerEntity, ItemStack> stackFilter = (player, stack) -> true;

        public Builder(PlayerEntity player) {
            this.player = player;
        }

        public Builder removeSnapshotItems() {
            this.removeSnapshotItems = true;
            return this;
        }

        public Builder replaceExisting() {
            this.replaceExisting = true;
            return this;
        }

        public Builder setStackFilter(BiPredicate<PlayerEntity, ItemStack> stackFilter) {
            this.stackFilter = stackFilter;
            return this;
        }

        public InventorySnapshotData.InventorySnapshot createSnapshot() {
            InventorySnapshotData.InventorySnapshot snapshot = new InventorySnapshotData.InventorySnapshot(this.removeSnapshotItems, this.replaceExisting);
            snapshot.createSnapshot(this.player, this.stackFilter);
            return snapshot;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\InventorySnapshotData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */