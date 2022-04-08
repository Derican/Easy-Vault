package iskallia.vault.container.base;

import iskallia.vault.container.slot.PlayerSensitiveSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.Set;


public interface PlayerSensitiveContainer {
    void setDragMode(int paramInt);

    int getDragMode();

    void setDragEvent(int paramInt);

    int getDragEvent();

    Set<Slot> getDragSlots();

    default void resetDrag() {
        setDragEvent(0);
        getDragSlots().clear();
    }

    default ItemStack playerSensitiveSlotClick(Container thisContainer, int slotId, int dragType, ClickType clickType, PlayerEntity player) {
        ItemStack itemstack = ItemStack.EMPTY;
        PlayerInventory playerinventory = player.inventory;
        if (clickType == ClickType.QUICK_CRAFT) {
            int currentDragEvent = getDragEvent();
            setDragEvent(Container.getQuickcraftHeader(dragType));
            if ((currentDragEvent != 1 || getDragEvent() != 2) && currentDragEvent != getDragEvent()) {
                resetDrag();
            } else if (playerinventory.getCarried().isEmpty()) {
                resetDrag();
            } else if (getDragEvent() == 0) {
                setDragEvent(Container.getQuickcraftType(dragType));
                if (Container.isValidQuickcraftType(getDragMode(), player)) {
                    setDragEvent(1);
                    getDragSlots().clear();
                } else {
                    resetDrag();
                }
            } else if (getDragEvent() == 1) {
                Slot slot7 = thisContainer.slots.get(slotId);
                ItemStack itemstack12 = playerinventory.getCarried();
                if (slot7 != null && canMergeSlotItemStack(player, slot7, itemstack12, true) && slot7.mayPlace(itemstack12) && (getDragMode() == 2 || itemstack12.getCount() > getDragSlots().size()) && thisContainer.canDragTo(slot7)) {
                    getDragSlots().add(slot7);
                }
            } else if (getDragEvent() == 2) {
                if (!getDragSlots().isEmpty()) {
                    ItemStack itemstack10 = playerinventory.getCarried().copy();
                    int k1 = playerinventory.getCarried().getCount();

                    for (Slot slot8 : getDragSlots()) {
                        ItemStack itemstack13 = playerinventory.getCarried();
                        if (slot8 != null && canMergeSlotItemStack(player, slot8, itemstack13, true) && slot8.mayPlace(itemstack13) && (getDragMode() == 2 || itemstack13.getCount() >= getDragSlots().size()) && thisContainer.canDragTo(slot8)) {
                            ItemStack itemstack14 = itemstack10.copy();
                            int j3 = slot8.hasItem() ? slot8.getItem().getCount() : 0;
                            Container.getQuickCraftSlotCount(getDragSlots(), getDragMode(), itemstack14, j3);
                            int k3 = Math.min(itemstack14.getMaxStackSize(), slot8.getMaxStackSize(itemstack14));
                            if (itemstack14.getCount() > k3) {
                                itemstack14.setCount(k3);
                            }

                            k1 -= itemstack14.getCount() - j3;
                            slot8.set(itemstack14);
                        }
                    }

                    itemstack10.setCount(k1);
                    playerinventory.setCarried(itemstack10);
                }

                resetDrag();
            } else {
                resetDrag();
            }
        } else if (getDragEvent() != 0) {
            resetDrag();
        } else if ((clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
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
            } else if (clickType == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot5 = thisContainer.slots.get(slotId);
                if (slot5 == null || !slot5.mayPickup(player)) {
                    return ItemStack.EMPTY;
                }

                for (ItemStack itemstack8 = thisContainer.quickMoveStack(player, slotId); !itemstack8.isEmpty() && ItemStack.isSame(slot5.getItem(), itemstack8); itemstack8 = thisContainer.quickMoveStack(player, slotId)) {
                    itemstack = itemstack8.copy();
                }
            } else {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot = thisContainer.slots.get(slotId);
                if (slot != null) {
                    ItemStack slotStack = slot.getItem();
                    ItemStack playerMouseStack = playerinventory.getCarried();
                    if (!slotStack.isEmpty()) {
                        itemstack = slotStack.copy();
                    }

                    if (slotStack.isEmpty()) {
                        if (!playerMouseStack.isEmpty() && slot.mayPlace(playerMouseStack)) {
                            int j2 = (dragType == 0) ? playerMouseStack.getCount() : 1;
                            if (j2 > slot.getMaxStackSize(playerMouseStack)) {
                                j2 = slot.getMaxStackSize(playerMouseStack);
                            }

                            slot.set(playerMouseStack.split(j2));
                        }
                    } else if (slot.mayPickup(player)) {
                        if (playerMouseStack.isEmpty()) {
                            if (slotStack.isEmpty()) {
                                slot.set(ItemStack.EMPTY);
                                playerinventory.setCarried(ItemStack.EMPTY);
                            } else {
                                int k2 = (dragType == 0) ? slotStack.getCount() : ((slotStack.getCount() + 1) / 2);
                                ItemStack pickedStack = slot.remove(k2);
                                if (slot instanceof PlayerSensitiveSlot) {
                                    pickedStack = ((PlayerSensitiveSlot) slot).modifyTakenStack(player, pickedStack, false);
                                }
                                playerinventory.setCarried(pickedStack);
                                if (slotStack.isEmpty()) {
                                    slot.set(ItemStack.EMPTY);
                                }

                                slot.onTake(player, playerinventory.getCarried());
                            }
                        } else if (slot.mayPlace(playerMouseStack)) {
                            if (Container.consideredTheSameItem(slotStack, playerMouseStack)) {
                                int l2 = (dragType == 0) ? playerMouseStack.getCount() : 1;
                                if (l2 > slot.getMaxStackSize(playerMouseStack) - slotStack.getCount()) {
                                    l2 = slot.getMaxStackSize(playerMouseStack) - slotStack.getCount();
                                }

                                if (l2 > playerMouseStack.getMaxStackSize() - slotStack.getCount()) {
                                    l2 = playerMouseStack.getMaxStackSize() - slotStack.getCount();
                                }

                                playerMouseStack.shrink(l2);
                                slotStack.grow(l2);
                            } else if (playerMouseStack.getCount() <= slot.getMaxStackSize(playerMouseStack)) {
                                slot.set(playerMouseStack);
                                playerinventory.setCarried(slotStack);
                            }
                        } else if (playerMouseStack.getMaxStackSize() > 1 && Container.consideredTheSameItem(slotStack, playerMouseStack) && !slotStack.isEmpty()) {
                            int i3 = slotStack.getCount();
                            if (i3 + playerMouseStack.getCount() <= playerMouseStack.getMaxStackSize()) {
                                playerMouseStack.grow(i3);
                                slotStack = slot.remove(i3);
                                if (slot instanceof PlayerSensitiveSlot) {
                                    slotStack = ((PlayerSensitiveSlot) slot).modifyTakenStack(player, slotStack, false);
                                }
                                if (slotStack.isEmpty()) {
                                    slot.set(ItemStack.EMPTY);
                                }

                                slot.onTake(player, playerinventory.getCarried());
                            }
                        }
                    }

                    slot.setChanged();
                }
            }
        } else if (clickType == ClickType.SWAP) {
            Slot slot = thisContainer.slots.get(slotId);
            ItemStack plInventoryDragStack = playerinventory.getItem(dragType);
            ItemStack slotStack = slot.getItem();
            if (!plInventoryDragStack.isEmpty() || !slotStack.isEmpty()) {
                if (plInventoryDragStack.isEmpty()) {
                    if (slot.mayPickup(player)) {
                        if (slot instanceof PlayerSensitiveSlot) {
                            slotStack = ((PlayerSensitiveSlot) slot).modifyTakenStack(player, slotStack, false);
                        }
                        playerinventory.setItem(dragType, slotStack);

                        slot.set(ItemStack.EMPTY);
                        slot.onTake(player, slotStack);
                    }
                } else if (slotStack.isEmpty()) {
                    if (slot.mayPlace(plInventoryDragStack)) {
                        int i = slot.getMaxStackSize(plInventoryDragStack);
                        if (plInventoryDragStack.getCount() > i) {
                            slot.set(plInventoryDragStack.split(i));
                        } else {
                            slot.set(plInventoryDragStack);
                            playerinventory.setItem(dragType, ItemStack.EMPTY);
                        }
                    }
                } else if (slot.mayPickup(player) && slot.mayPlace(plInventoryDragStack)) {
                    int l1 = slot.getMaxStackSize(plInventoryDragStack);
                    if (plInventoryDragStack.getCount() > l1) {
                        slot.set(plInventoryDragStack.split(l1));
                        if (slot instanceof PlayerSensitiveSlot) {
                            slotStack = ((PlayerSensitiveSlot) slot).modifyTakenStack(player, slotStack, false);
                        }
                        slot.onTake(player, slotStack);
                        if (!playerinventory.add(slotStack)) {
                            player.drop(slotStack, true);
                        }
                    } else {
                        slot.set(plInventoryDragStack);
                        if (slot instanceof PlayerSensitiveSlot) {
                            slotStack = ((PlayerSensitiveSlot) slot).modifyTakenStack(player, slotStack, false);
                        }
                        playerinventory.setItem(dragType, slotStack);
                        slot.onTake(player, slotStack);
                    }
                }
            }
        } else if (clickType == ClickType.CLONE && player.abilities.instabuild && playerinventory.getCarried().isEmpty() && slotId >= 0) {
            Slot slot4 = thisContainer.slots.get(slotId);
            if (slot4 != null && slot4.hasItem()) {
                ItemStack itemstack7 = slot4.getItem().copy();
                itemstack7.setCount(itemstack7.getMaxStackSize());
                playerinventory.setCarried(itemstack7);
            }
        } else if (clickType == ClickType.THROW && playerinventory.getCarried().isEmpty() && slotId >= 0) {
            Slot slot3 = thisContainer.slots.get(slotId);
            if (slot3 != null && slot3.hasItem() && slot3.mayPickup(player)) {
                ItemStack slotStack = slot3.remove((dragType == 0) ? 1 : slot3.getItem().getCount());
                if (slot3 instanceof PlayerSensitiveSlot) {
                    slotStack = ((PlayerSensitiveSlot) slot3).modifyTakenStack(player, slotStack, false);
                }
                slot3.onTake(player, slotStack);
                player.drop(slotStack, true);
            }
        } else if (clickType == ClickType.PICKUP_ALL && slotId >= 0) {
            Slot slot = thisContainer.slots.get(slotId);
            ItemStack playerMouseStack = playerinventory.getCarried();
            if (!playerMouseStack.isEmpty() && (slot == null || !slot.hasItem() || !slot.mayPickup(player))) {
                int j1 = (dragType == 0) ? 0 : (thisContainer.slots.size() - 1);
                int i2 = (dragType == 0) ? 1 : -1;

                for (int j = 0; j < 2; j++) {
                    int k;
                    for (k = j1; k >= 0 && k < thisContainer.slots.size() && playerMouseStack.getCount() < playerMouseStack.getMaxStackSize(); k += i2) {
                        Slot slot1 = thisContainer.slots.get(k);
                        if (slot1.hasItem() && canMergeSlotItemStack(player, slot1, playerMouseStack, true) && slot1.mayPickup(player) && thisContainer.canTakeItemForPickAll(playerMouseStack, slot1)) {
                            ItemStack itemstack3 = slot1.getItem();
                            if (j != 0 || itemstack3.getCount() != itemstack3.getMaxStackSize()) {
                                int l = Math.min(playerMouseStack.getMaxStackSize() - playerMouseStack.getCount(), itemstack3.getCount());
                                ItemStack slotStack = slot1.remove(l);
                                if (slot1 instanceof PlayerSensitiveSlot) {
                                    slotStack = ((PlayerSensitiveSlot) slot1).modifyTakenStack(player, slotStack, false);
                                }
                                playerMouseStack.grow(l);
                                if (slotStack.isEmpty()) {
                                    slot1.set(ItemStack.EMPTY);
                                }

                                slot1.onTake(player, slotStack);
                            }
                        }
                    }
                }
            }

            thisContainer.broadcastChanges();
        }

        return itemstack;
    }

    static boolean canMergeSlotItemStack(PlayerEntity player, Slot slot, ItemStack toMergeOn, boolean stackSizeMatters) {
        if (slot == null || !slot.hasItem()) {
            return true;
        }
        ItemStack slotStack = slot.getItem();
        if (slot instanceof PlayerSensitiveSlot) {
            slotStack = ((PlayerSensitiveSlot) slot).modifyTakenStack(player, slotStack, true);
        }
        if (toMergeOn.sameItem(slotStack) && ItemStack.tagMatches(slotStack, toMergeOn)) {
            return (slotStack.getCount() + (stackSizeMatters ? 0 : toMergeOn.getCount()) <= toMergeOn.getMaxStackSize());
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\base\PlayerSensitiveContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */