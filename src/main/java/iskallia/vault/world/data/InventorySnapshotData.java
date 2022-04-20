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
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.extensions.IForgeItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.ModList;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public abstract class InventorySnapshotData extends WorldSavedData {
    public VMapNBT<UUID, InventorySnapshot> snapshotData;

    protected InventorySnapshotData(final String name) {
        super(name);
        this.snapshotData = VMapNBT.ofUUID(() -> new InventorySnapshot());
    }

    protected abstract boolean shouldSnapshotItem(final PlayerEntity p0, final ItemStack p1);

    protected Builder makeSnapshotBuilder(final PlayerEntity player) {
        return new Builder(player).setStackFilter(this::shouldSnapshotItem).removeSnapshotItems();
    }

    public boolean hasSnapshot(final PlayerEntity player) {
        return this.hasSnapshot(player.getUUID());
    }

    public boolean hasSnapshot(final UUID playerUUID) {
        return this.snapshotData.containsKey(playerUUID);
    }

    public void createSnapshot(final PlayerEntity player) {
        if (this.snapshotData.containsKey(player.getUUID())) {
            this.restoreSnapshot(player);
        }
        this.snapshotData.put(player.getUUID(), this.makeSnapshotBuilder(player).createSnapshot());
        this.setDirty();
    }

    public boolean removeSnapshot(final PlayerEntity player) {
        return this.removeSnapshot(player.getUUID());
    }

    public boolean removeSnapshot(final UUID playerUUID) {
        if (this.snapshotData.remove(playerUUID) != null) {
            this.setDirty();
            return true;
        }
        return false;
    }

    public boolean restoreSnapshot(final PlayerEntity player) {
        final InventorySnapshot snapshot = this.snapshotData.remove(player.getUUID());
        if (snapshot != null) {
            this.setDirty();
            return snapshot.apply(player);
        }
        return false;
    }

    public void load(final CompoundNBT nbt) {
        this.snapshotData.deserializeNBT(nbt.getList("Players", 10));
    }

    public CompoundNBT save(final CompoundNBT compound) {
        compound.put("Players", (INBT) this.snapshotData.serializeNBT());
        return compound;
    }

    public static class InventorySnapshot implements INBTSerializable<CompoundNBT> {
        private final VListNBT<Integer, IntNBT> invIds;
        private final VListNBT<ItemStack, CompoundNBT> items;
        private final Map<String, CompoundNBT> customInventoryData;
        private boolean removeSnapshotItems;
        private boolean replaceExisting;

        private InventorySnapshot() {
            this.invIds = new VListNBT<Integer, IntNBT>(IntNBT::valueOf, IntNBT::getAsInt);
            this.items = new VListNBT<ItemStack, CompoundNBT>(IForgeItemStack::serializeNBT, ItemStack::of);
            this.customInventoryData = new HashMap<String, CompoundNBT>();
            this.removeSnapshotItems = false;
            this.replaceExisting = false;
        }

        private InventorySnapshot(final boolean removeSnapshotItems, final boolean replaceExisting) {
            this.invIds = new VListNBT<Integer, IntNBT>(IntNBT::valueOf, IntNBT::getAsInt);
            this.items = new VListNBT<ItemStack, CompoundNBT>(IForgeItemStack::serializeNBT, ItemStack::of);
            this.customInventoryData = new HashMap<String, CompoundNBT>();
            this.removeSnapshotItems = false;
            this.replaceExisting = false;
            this.removeSnapshotItems = removeSnapshotItems;
            this.replaceExisting = replaceExisting;
        }

        private void createSnapshot(final PlayerEntity player, final BiPredicate<PlayerEntity, ItemStack> stackFilter) {
            for (int slot = 0; slot < ((InventoryAccessor) player.inventory).getSize(); ++slot) {
                final ItemStack stack = player.inventory.getItem(slot);
                if (stackFilter.test(player, stack)) {
                    this.addItemStack(slot, stack);
                    if (this.removeSnapshotItems) {
                        player.inventory.setItem(slot, ItemStack.EMPTY);
                    }
                }
            }
            if (ModList.get().isLoaded("curios")) {
                final CompoundNBT curiosData = IntegrationCurios.getMappedSerializedCuriosItemStacks(player, stackFilter, this.removeSnapshotItems);
                this.customInventoryData.put("curios", curiosData);
            }
        }

        private void addItemStack(final int slot, final ItemStack stack) {
            this.invIds.add(slot);
            this.items.add(stack.copy());
        }

        private boolean apply(final PlayerEntity player) {
            if (!player.isAlive()) {
                return false;
            }
            final List<ItemStack> addLater = new ArrayList<ItemStack>();
            for (int index = 0; index < this.items.size(); ++index) {
                final ItemStack toAdd = this.items.get(index).copy();
                final int slot = this.invIds.get(index);
                if (this.replaceExisting || player.inventory.getItem(slot).isEmpty()) {
                    player.inventory.setItem(slot, toAdd);
                } else {
                    addLater.add(toAdd);
                }
            }
            if (ModList.get().isLoaded("curios") && this.customInventoryData.containsKey("curios")) {
                final CompoundNBT curiosData = this.customInventoryData.get("curios");
                addLater.addAll(IntegrationCurios.applyMappedSerializedCuriosItemStacks(player, curiosData, this.replaceExisting));
            }
            for (final ItemStack stack : addLater) {
                if (!player.addItem(stack)) {
                    final ItemEntity itementity = player.drop(stack, false);
                    if (itementity == null) {
                        continue;
                    }
                    itementity.setNoPickUpDelay();
                    itementity.setOwner(player.getUUID());
                }
            }
            return true;
        }

        public CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.put("InvIds", (INBT) this.invIds.serializeNBT());
            nbt.put("Items", (INBT) this.items.serializeNBT());
            final CompoundNBT customData = new CompoundNBT();
            this.customInventoryData.forEach((BiConsumer<? super String, ? super CompoundNBT>) customData::put);
            nbt.put("customData", (INBT) customData);
            nbt.putBoolean("removeSnapshotItems", this.removeSnapshotItems);
            nbt.putBoolean("replaceExisting", this.replaceExisting);
            return nbt;
        }

        public void deserializeNBT(final CompoundNBT nbt) {
            this.invIds.deserializeNBT(nbt.getList("InvIds", 3));
            this.items.deserializeNBT(nbt.getList("Items", 10));
            this.customInventoryData.clear();
            if (nbt.contains("customData", 10)) {
                final CompoundNBT customData = nbt.getCompound("customData");
                for (final String key : customData.getAllKeys()) {
                    this.customInventoryData.put(key, customData.getCompound(key));
                }
            }
            this.removeSnapshotItems = nbt.getBoolean("removeSnapshotItems");
            this.replaceExisting = nbt.getBoolean("replaceExisting");
        }
    }

    public static class Builder {
        private final PlayerEntity player;
        private boolean removeSnapshotItems;
        private boolean replaceExisting;
        private BiPredicate<PlayerEntity, ItemStack> stackFilter;

        public Builder(final PlayerEntity player) {
            this.removeSnapshotItems = false;
            this.replaceExisting = false;
            this.stackFilter = ((player1, stack) -> true);
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

        public Builder setStackFilter(final BiPredicate<PlayerEntity, ItemStack> stackFilter) {
            this.stackFilter = stackFilter;
            return this;
        }

        public InventorySnapshot createSnapshot() {
            final InventorySnapshot snapshot = new InventorySnapshot(this.removeSnapshotItems, this.replaceExisting);
            snapshot.createSnapshot(this.player, this.stackFilter);
            return snapshot;
        }
    }

    public interface InventoryAccessor {
        int getSize();
    }
}
