// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.container.base;

import iskallia.vault.container.slot.PlayerSensitiveSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.Set;

public interface PlayerSensitiveContainer
{
    void setDragMode(final int p0);
    
    int getDragMode();
    
    void setDragEvent(final int p0);
    
    int getDragEvent();
    
    Set<Slot> getDragSlots();
    
    default void resetDrag() {
        this.setDragEvent(0);
        this.getDragSlots().clear();
    }
    
    default ItemStack playerSensitiveSlotClick(final Container thisContainer, final int slotId, final int dragType, final ClickType clickType, final PlayerEntity player) {
        ItemStack itemstack = ItemStack.EMPTY;
        final PlayerInventory playerinventory = player.inventory;
        if (clickType == ClickType.QUICK_CRAFT) {
            final int currentDragEvent = this.getDragEvent();
            this.setDragEvent(Container.getQuickcraftHeader(dragType));
            if ((currentDragEvent != 1 || this.getDragEvent() != 2) && currentDragEvent != this.getDragEvent()) {
                this.resetDrag();
            }
            else if (playerinventory.getCarried().isEmpty()) {
                this.resetDrag();
            }
            else if (this.getDragEvent() == 0) {
                this.setDragEvent(Container.getQuickcraftType(dragType));
                if (Container.isValidQuickcraftType(this.getDragMode(), player)) {
                    this.setDragEvent(1);
                    this.getDragSlots().clear();
                }
                else {
                    this.resetDrag();
                }
            }
            else if (this.getDragEvent() == 1) {
                final Slot slot7 = thisContainer.slots.get(slotId);
                final ItemStack itemstack2 = playerinventory.getCarried();
                if (slot7 != null && canMergeSlotItemStack(player, slot7, itemstack2, true) && slot7.mayPlace(itemstack2) && (this.getDragMode() == 2 || itemstack2.getCount() > this.getDragSlots().size()) && thisContainer.canDragTo(slot7)) {
                    this.getDragSlots().add(slot7);
                }
            }
            else if (this.getDragEvent() == 2) {
                if (!this.getDragSlots().isEmpty()) {
                    final ItemStack itemstack3 = playerinventory.getCarried().copy();
                    int k1 = playerinventory.getCarried().getCount();
                    for (final Slot slot8 : this.getDragSlots()) {
                        final ItemStack itemstack4 = playerinventory.getCarried();
                        if (slot8 != null && canMergeSlotItemStack(player, slot8, itemstack4, true) && slot8.mayPlace(itemstack4) && (this.getDragMode() == 2 || itemstack4.getCount() >= this.getDragSlots().size()) && thisContainer.canDragTo(slot8)) {
                            final ItemStack itemstack5 = itemstack3.copy();
                            final int j3 = slot8.hasItem() ? slot8.getItem().getCount() : 0;
                            Container.getQuickCraftSlotCount((Set)this.getDragSlots(), this.getDragMode(), itemstack5, j3);
                            final int k2 = Math.min(itemstack5.getMaxStackSize(), slot8.getMaxStackSize(itemstack5));
                            if (itemstack5.getCount() > k2) {
                                itemstack5.setCount(k2);
                            }
                            k1 -= itemstack5.getCount() - j3;
                            slot8.set(itemstack5);
                        }
                    }
                    itemstack3.setCount(k1);
                    playerinventory.setCarried(itemstack3);
                }
                this.resetDrag();
            }
            else {
                this.resetDrag();
            }
        }
        else if (this.getDragEvent() != 0) {
            this.resetDrag();
        }
        else if ((clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (!playerinventory.getCarried().isEmpty()) {
                    if (dragType == 0) {
                        player.drop(playerinventory.getCarried(), true);
                        playerinventory.setCarried(ItemStack.EMPTY);
                    }
                    if (dragType == 1) {
                        player.drop(playerinventory.getCarried().split(1), true);
                    }
                }
            }
            else if (clickType == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }
                final Slot slot9 = thisContainer.slots.get(slotId);
                if (slot9 == null || !slot9.mayPickup(player)) {
                    return ItemStack.EMPTY;
                }
                for (ItemStack itemstack6 = thisContainer.quickMoveStack(player, slotId); !itemstack6.isEmpty() && ItemStack.isSame(slot9.getItem(), itemstack6); itemstack6 = thisContainer.quickMoveStack(player, slotId)) {
                    itemstack = itemstack6.copy();
                }
            }
            else {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }
                final Slot slot10 = thisContainer.slots.get(slotId);
                if (slot10 != null) {
                    ItemStack slotStack = slot10.getItem();
                    final ItemStack playerMouseStack = playerinventory.getCarried();
                    if (!slotStack.isEmpty()) {
                        itemstack = slotStack.copy();
                    }
                    if (slotStack.isEmpty()) {
                        if (!playerMouseStack.isEmpty() && slot10.mayPlace(playerMouseStack)) {
                            int j4 = (dragType == 0) ? playerMouseStack.getCount() : 1;
                            if (j4 > slot10.getMaxStackSize(playerMouseStack)) {
                                j4 = slot10.getMaxStackSize(playerMouseStack);
                            }
                            slot10.set(playerMouseStack.split(j4));
                        }
                    }
                    else if (slot10.mayPickup(player)) {
                        if (playerMouseStack.isEmpty()) {
                            if (slotStack.isEmpty()) {
                                slot10.set(ItemStack.EMPTY);
                                playerinventory.setCarried(ItemStack.EMPTY);
                            }
                            else {
                                final int k3 = (dragType == 0) ? slotStack.getCount() : ((slotStack.getCount() + 1) / 2);
                                ItemStack pickedStack = slot10.remove(k3);
                                if (slot10 instanceof PlayerSensitiveSlot) {
                                    pickedStack = ((PlayerSensitiveSlot)slot10).modifyTakenStack(player, pickedStack, false);
                                }
                                playerinventory.setCarried(pickedStack);
                                if (slotStack.isEmpty()) {
                                    slot10.set(ItemStack.EMPTY);
                                }
                                slot10.onTake(player, playerinventory.getCarried());
                            }
                        }
                        else if (slot10.mayPlace(playerMouseStack)) {
                            if (Container.consideredTheSameItem(slotStack, playerMouseStack)) {
                                int l2 = (dragType == 0) ? playerMouseStack.getCount() : 1;
                                if (l2 > slot10.getMaxStackSize(playerMouseStack) - slotStack.getCount()) {
                                    l2 = slot10.getMaxStackSize(playerMouseStack) - slotStack.getCount();
                                }
                                if (l2 > playerMouseStack.getMaxStackSize() - slotStack.getCount()) {
                                    l2 = playerMouseStack.getMaxStackSize() - slotStack.getCount();
                                }
                                playerMouseStack.shrink(l2);
                                slotStack.grow(l2);
                            }
                            else if (playerMouseStack.getCount() <= slot10.getMaxStackSize(playerMouseStack)) {
                                slot10.set(playerMouseStack);
                                playerinventory.setCarried(slotStack);
                            }
                        }
                        else if (playerMouseStack.getMaxStackSize() > 1 && Container.consideredTheSameItem(slotStack, playerMouseStack) && !slotStack.isEmpty()) {
                            final int i3 = slotStack.getCount();
                            if (i3 + playerMouseStack.getCount() <= playerMouseStack.getMaxStackSize()) {
                                playerMouseStack.grow(i3);
                                slotStack = slot10.remove(i3);
                                if (slot10 instanceof PlayerSensitiveSlot) {
                                    slotStack = ((PlayerSensitiveSlot)slot10).modifyTakenStack(player, slotStack, false);
                                }
                                if (slotStack.isEmpty()) {
                                    slot10.set(ItemStack.EMPTY);
                                }
                                slot10.onTake(player, playerinventory.getCarried());
                            }
                        }
                    }
                    slot10.setChanged();
                }
            }
        }
        else if (clickType == ClickType.SWAP) {
            final Slot slot10 = thisContainer.slots.get(slotId);
            final ItemStack plInventoryDragStack = playerinventory.getItem(dragType);
            ItemStack slotStack2 = slot10.getItem();
            if (!plInventoryDragStack.isEmpty() || !slotStack2.isEmpty()) {
                if (plInventoryDragStack.isEmpty()) {
                    if (slot10.mayPickup(player)) {
                        if (slot10 instanceof PlayerSensitiveSlot) {
                            slotStack2 = ((PlayerSensitiveSlot)slot10).modifyTakenStack(player, slotStack2, false);
                        }
                        playerinventory.setItem(dragType, slotStack2);
                        slot10.set(ItemStack.EMPTY);
                        slot10.onTake(player, slotStack2);
                    }
                }
                else if (slotStack2.isEmpty()) {
                    if (slot10.mayPlace(plInventoryDragStack)) {
                        final int m = slot10.getMaxStackSize(plInventoryDragStack);
                        if (plInventoryDragStack.getCount() > m) {
                            slot10.set(plInventoryDragStack.split(m));
                        }
                        else {
                            slot10.set(plInventoryDragStack);
                            playerinventory.setItem(dragType, ItemStack.EMPTY);
                        }
                    }
                }
                else if (slot10.mayPickup(player) && slot10.mayPlace(plInventoryDragStack)) {
                    final int l3 = slot10.getMaxStackSize(plInventoryDragStack);
                    if (plInventoryDragStack.getCount() > l3) {
                        slot10.set(plInventoryDragStack.split(l3));
                        if (slot10 instanceof PlayerSensitiveSlot) {
                            slotStack2 = ((PlayerSensitiveSlot)slot10).modifyTakenStack(player, slotStack2, false);
                        }
                        slot10.onTake(player, slotStack2);
                        if (!playerinventory.add(slotStack2)) {
                            player.drop(slotStack2, true);
                        }
                    }
                    else {
                        slot10.set(plInventoryDragStack);
                        if (slot10 instanceof PlayerSensitiveSlot) {
                            slotStack2 = ((PlayerSensitiveSlot)slot10).modifyTakenStack(player, slotStack2, false);
                        }
                        playerinventory.setItem(dragType, slotStack2);
                        slot10.onTake(player, slotStack2);
                    }
                }
            }
        }
        else if (clickType == ClickType.CLONE && player.abilities.instabuild && playerinventory.getCarried().isEmpty() && slotId >= 0) {
            final Slot slot11 = thisContainer.slots.get(slotId);
            if (slot11 != null && slot11.hasItem()) {
                final ItemStack itemstack7 = slot11.getItem().copy();
                itemstack7.setCount(itemstack7.getMaxStackSize());
                playerinventory.setCarried(itemstack7);
            }
        }
        else if (clickType == ClickType.THROW && playerinventory.getCarried().isEmpty() && slotId >= 0) {
            final Slot slot12 = thisContainer.slots.get(slotId);
            if (slot12 != null && slot12.hasItem() && slot12.mayPickup(player)) {
                ItemStack slotStack = slot12.remove((dragType == 0) ? 1 : slot12.getItem().getCount());
                if (slot12 instanceof PlayerSensitiveSlot) {
                    slotStack = ((PlayerSensitiveSlot)slot12).modifyTakenStack(player, slotStack, false);
                }
                slot12.onTake(player, slotStack);
                player.drop(slotStack, true);
            }
        }
        else if (clickType == ClickType.PICKUP_ALL && slotId >= 0) {
            final Slot slot10 = thisContainer.slots.get(slotId);
            final ItemStack playerMouseStack2 = playerinventory.getCarried();
            if (!playerMouseStack2.isEmpty() && (slot10 == null || !slot10.hasItem() || !slot10.mayPickup(player))) {
                final int j5 = (dragType == 0) ? 0 : (thisContainer.slots.size() - 1);
                final int i4 = (dragType == 0) ? 1 : -1;
                for (int j6 = 0; j6 < 2; ++j6) {
                    for (int k4 = j5; k4 >= 0 && k4 < thisContainer.slots.size() && playerMouseStack2.getCount() < playerMouseStack2.getMaxStackSize(); k4 += i4) {
                        final Slot slot13 = thisContainer.slots.get(k4);
                        if (slot13.hasItem() && canMergeSlotItemStack(player, slot13, playerMouseStack2, true) && slot13.mayPickup(player) && thisContainer.canTakeItemForPickAll(playerMouseStack2, slot13)) {
                            final ItemStack itemstack8 = slot13.getItem();
                            if (j6 != 0 || itemstack8.getCount() != itemstack8.getMaxStackSize()) {
                                final int l4 = Math.min(playerMouseStack2.getMaxStackSize() - playerMouseStack2.getCount(), itemstack8.getCount());
                                ItemStack slotStack3 = slot13.remove(l4);
                                if (slot13 instanceof PlayerSensitiveSlot) {
                                    slotStack3 = ((PlayerSensitiveSlot)slot13).modifyTakenStack(player, slotStack3, false);
                                }
                                playerMouseStack2.grow(l4);
                                if (slotStack3.isEmpty()) {
                                    slot13.set(ItemStack.EMPTY);
                                }
                                slot13.onTake(player, slotStack3);
                            }
                        }
                    }
                }
            }
            thisContainer.broadcastChanges();
        }
        return itemstack;
    }
    
    default boolean canMergeSlotItemStack(final PlayerEntity player, final Slot slot, final ItemStack toMergeOn, final boolean stackSizeMatters) {
        if (slot == null || !slot.hasItem()) {
            return true;
        }
        ItemStack slotStack = slot.getItem();
        if (slot instanceof PlayerSensitiveSlot) {
            slotStack = ((PlayerSensitiveSlot)slot).modifyTakenStack(player, slotStack, true);
        }
        return toMergeOn.sameItem(slotStack) && ItemStack.tagMatches(slotStack, toMergeOn) && slotStack.getCount() + (stackSizeMatters ? 0 : toMergeOn.getCount()) <= toMergeOn.getMaxStackSize();
    }
}
