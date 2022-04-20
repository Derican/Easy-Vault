package iskallia.vault.container.inventory;

import com.google.common.collect.Sets;
import iskallia.vault.container.slot.ConditionalReadSlot;
import iskallia.vault.init.ModContainers;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.ItemShardPouch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class ShardPouchContainer extends Container {
    private final int pouchSlot;
    private final PlayerInventory inventory;
    private int dragMode;
    private int dragEvent;
    private final Set<Slot> dragSlots;

    public ShardPouchContainer(final int id, final PlayerInventory inventory, final int pouchSlot) {
        super((ContainerType) ModContainers.SHARD_POUCH_CONTAINER, id);
        this.dragMode = -1;
        this.dragSlots = Sets.newHashSet();
        this.inventory = inventory;
        this.pouchSlot = pouchSlot;
        if (this.hasPouch()) {
            inventory.player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(playerInvHandler -> {
                final ItemStack pouch = this.inventory.getItem(this.pouchSlot);
                pouch.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(pouchHandler -> this.initSlots(playerInvHandler, pouchHandler));
            });
        }
    }

    private void initSlots(final IItemHandler playerInvHandler, final IItemHandler pouchHandler) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot((Slot) new ConditionalReadSlot(playerInvHandler, column + row * 9 + 9, 8 + column * 18, 55 + row * 18, this::canAccess));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot((Slot) new ConditionalReadSlot(playerInvHandler, hotbarSlot, 8 + hotbarSlot * 18, 113, this::canAccess));
        }
        this.addSlot((Slot) new ConditionalReadSlot(pouchHandler, 0, 80, 16, (slot, stack) -> this.canAccess(slot, stack) && stack.getItem() == ModItems.SOUL_SHARD) {
            public int getMaxStackSize(@Nonnull final ItemStack stack) {
                return pouchHandler.getSlotLimit(0);
            }

            public void setChanged() {
                ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(this.getSlotIndex(), this.getItem());
            }
        });
    }

    public boolean stillValid(final PlayerEntity player) {
        return this.hasPouch();
    }

    public boolean canAccess(final int slot, final ItemStack slotStack) {
        return this.hasPouch() && !(slotStack.getItem() instanceof ItemShardPouch);
    }

    public boolean hasPouch() {
        final ItemStack pouchStack = this.inventory.getItem(this.pouchSlot);
        return !pouchStack.isEmpty() && pouchStack.getItem() instanceof ItemShardPouch;
    }

    public ItemStack quickMoveStack(final PlayerEntity playerIn, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            final ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            if (index >= 0 && index < 36 && this.moveItemStackTo(slotStack, 36, 37, false)) {
                return itemstack;
            }
            if (index >= 0 && index < 27) {
                if (!this.moveItemStackTo(slotStack, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 27 && index < 36) {
                if (!this.moveItemStackTo(slotStack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, slotStack);
        }
        return itemstack;
    }

    @Nonnull
    public ItemStack clicked(final int slotId, final int dragType, final ClickType clickTypeIn, final PlayerEntity player) {
        ItemStack returnStack = ItemStack.EMPTY;
        final PlayerInventory PlayerInventory = player.inventory;
        if (clickTypeIn == ClickType.QUICK_CRAFT) {
            final int j1 = this.dragEvent;
            this.dragEvent = getQuickcraftHeader(dragType);
            if ((j1 != 1 || this.dragEvent != 2) && j1 != this.dragEvent) {
                this.resetQuickCraft();
            } else if (PlayerInventory.getCarried().isEmpty()) {
                this.resetQuickCraft();
            } else if (this.dragEvent == 0) {
                this.dragMode = getQuickcraftType(dragType);
                if (isValidQuickcraftType(this.dragMode, player)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    this.resetQuickCraft();
                }
            } else if (this.dragEvent == 1) {
                final Slot slot = this.slots.get(slotId);
                final ItemStack mouseStack = PlayerInventory.getCarried();
                if (slot != null && canAddItemToSlot(slot, mouseStack, true) && slot.mayPlace(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() > this.dragSlots.size()) && this.canDragTo(slot)) {
                    this.dragSlots.add(slot);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    final ItemStack mouseStackCopy = PlayerInventory.getCarried().copy();
                    int k1 = PlayerInventory.getCarried().getCount();
                    for (final Slot dragSlot : this.dragSlots) {
                        final ItemStack mouseStack2 = PlayerInventory.getCarried();
                        if (dragSlot != null && canAddItemToSlot(dragSlot, mouseStack2, true) && dragSlot.mayPlace(mouseStack2) && (this.dragMode == 2 || mouseStack2.getCount() >= this.dragSlots.size()) && this.canDragTo(dragSlot)) {
                            final ItemStack itemstack14 = mouseStackCopy.copy();
                            final int j2 = dragSlot.hasItem() ? dragSlot.getItem().getCount() : 0;
                            getQuickCraftSlotCount((Set) this.dragSlots, this.dragMode, itemstack14, j2);
                            final int k2 = dragSlot.getMaxStackSize(itemstack14);
                            if (itemstack14.getCount() > k2) {
                                itemstack14.setCount(k2);
                            }
                            k1 -= itemstack14.getCount() - j2;
                            dragSlot.set(itemstack14);
                        }
                    }
                    mouseStackCopy.setCount(k1);
                    PlayerInventory.setCarried(mouseStackCopy);
                }
                this.resetQuickCraft();
            } else {
                this.resetQuickCraft();
            }
        } else if (this.dragEvent != 0) {
            this.resetQuickCraft();
        } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (!PlayerInventory.getCarried().isEmpty()) {
                    if (dragType == 0) {
                        player.drop(PlayerInventory.getCarried(), true);
                        PlayerInventory.setCarried(ItemStack.EMPTY);
                    }
                    if (dragType == 1) {
                        player.drop(PlayerInventory.getCarried().split(1), true);
                    }
                }
            } else if (clickTypeIn == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }
                final Slot slot2 = this.slots.get(slotId);
                if (slot2 == null || !slot2.mayPickup(player)) {
                    return ItemStack.EMPTY;
                }
                for (ItemStack itemstack15 = this.quickMoveStack(player, slotId); !itemstack15.isEmpty() && ItemStack.isSame(slot2.getItem(), itemstack15); itemstack15 = this.quickMoveStack(player, slotId)) {
                    returnStack = itemstack15.copy();
                }
            } else {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }
                final Slot slot2 = this.slots.get(slotId);
                if (slot2 != null) {
                    ItemStack slotStack = slot2.getItem();
                    final ItemStack mouseStack = PlayerInventory.getCarried();
                    if (!slotStack.isEmpty()) {
                        returnStack = slotStack.copy();
                    }
                    if (slotStack.isEmpty()) {
                        if (!mouseStack.isEmpty() && slot2.mayPlace(mouseStack)) {
                            int i3 = (dragType == 0) ? mouseStack.getCount() : 1;
                            if (i3 > slot2.getMaxStackSize(mouseStack)) {
                                i3 = slot2.getMaxStackSize(mouseStack);
                            }
                            slot2.set(mouseStack.split(i3));
                        }
                    } else if (slot2.mayPickup(player)) {
                        if (mouseStack.isEmpty()) {
                            if (slotStack.isEmpty()) {
                                slot2.set(ItemStack.EMPTY);
                                PlayerInventory.setCarried(ItemStack.EMPTY);
                            } else {
                                final int toMove = (dragType == 0) ? slotStack.getCount() : ((slotStack.getCount() + 1) / 2);
                                PlayerInventory.setCarried(slot2.remove(toMove));
                                if (slotStack.isEmpty()) {
                                    slot2.set(ItemStack.EMPTY);
                                }
                                slot2.onTake(player, PlayerInventory.getCarried());
                            }
                        } else if (slot2.mayPlace(mouseStack)) {
                            if (slotStack.getItem() == mouseStack.getItem() && ItemStack.tagMatches(slotStack, mouseStack)) {
                                int k3 = (dragType == 0) ? mouseStack.getCount() : 1;
                                if (k3 > slot2.getMaxStackSize(mouseStack) - slotStack.getCount()) {
                                    k3 = slot2.getMaxStackSize(mouseStack) - slotStack.getCount();
                                }
                                mouseStack.shrink(k3);
                                slotStack.grow(k3);
                            } else if (mouseStack.getCount() <= slot2.getMaxStackSize(mouseStack) && slotStack.getCount() <= slotStack.getMaxStackSize()) {
                                slot2.set(mouseStack);
                                PlayerInventory.setCarried(slotStack);
                            }
                        } else if (slotStack.getItem() == mouseStack.getItem() && mouseStack.getMaxStackSize() > 1 && ItemStack.tagMatches(slotStack, mouseStack) && !slotStack.isEmpty()) {
                            final int j3 = slotStack.getCount();
                            if (j3 + mouseStack.getCount() <= mouseStack.getMaxStackSize()) {
                                mouseStack.grow(j3);
                                slotStack = slot2.remove(j3);
                                if (slotStack.isEmpty()) {
                                    slot2.set(ItemStack.EMPTY);
                                }
                                slot2.onTake(player, PlayerInventory.getCarried());
                            }
                        }
                    }
                    slot2.setChanged();
                }
            }
        } else if (clickTypeIn != ClickType.SWAP || dragType < 0 || dragType >= 9) {
            if (clickTypeIn == ClickType.CLONE && player.abilities.instabuild && PlayerInventory.getCarried().isEmpty() && slotId >= 0) {
                final Slot slot3 = this.slots.get(slotId);
                if (slot3 != null && slot3.hasItem()) {
                    final ItemStack itemstack16 = slot3.getItem().copy();
                    itemstack16.setCount(itemstack16.getMaxStackSize());
                    PlayerInventory.setCarried(itemstack16);
                }
            } else if (clickTypeIn == ClickType.THROW && PlayerInventory.getCarried().isEmpty() && slotId >= 0) {
                final Slot slot2 = this.slots.get(slotId);
                if (slot2 != null && slot2.hasItem() && slot2.mayPickup(player)) {
                    final ItemStack itemstack17 = slot2.remove((dragType == 0) ? 1 : slot2.getItem().getCount());
                    slot2.onTake(player, itemstack17);
                    player.drop(itemstack17, true);
                }
            } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
                final Slot slot2 = this.slots.get(slotId);
                final ItemStack mouseStack3 = PlayerInventory.getCarried();
                if (!mouseStack3.isEmpty() && (slot2 == null || !slot2.hasItem() || !slot2.mayPickup(player))) {
                    final int l = (dragType == 0) ? 0 : (this.slots.size() - 1);
                    final int m = (dragType == 0) ? 1 : -1;
                    for (int k4 = 0; k4 < 2; ++k4) {
                        for (int l2 = l; l2 >= 0 && l2 < this.slots.size() && mouseStack3.getCount() < mouseStack3.getMaxStackSize(); l2 += m) {
                            final Slot slot4 = this.slots.get(l2);
                            if (slot4.hasItem() && canAddItemToSlot(slot4, mouseStack3, true) && slot4.mayPickup(player) && this.canTakeItemForPickAll(mouseStack3, slot4)) {
                                final ItemStack itemstack18 = slot4.getItem();
                                if (k4 != 0 || itemstack18.getCount() < slot4.getMaxStackSize(itemstack18)) {
                                    final int i4 = Math.min(mouseStack3.getMaxStackSize() - mouseStack3.getCount(), itemstack18.getCount());
                                    final ItemStack itemstack19 = slot4.remove(i4);
                                    mouseStack3.grow(i4);
                                    if (itemstack19.isEmpty()) {
                                        slot4.set(ItemStack.EMPTY);
                                    }
                                    slot4.onTake(player, itemstack19);
                                }
                            }
                        }
                    }
                }
                this.broadcastChanges();
            }
        }
        if (returnStack.getCount() > 64) {
            returnStack = returnStack.copy();
            returnStack.setCount(64);
        }
        return returnStack;
    }

    protected void resetQuickCraft() {
        this.dragEvent = 0;
        this.dragSlots.clear();
    }

    public void broadcastChanges() {
//        for (int i = 0; i < this.slots.size(); ++i) {
//            final ItemStack itemstack = this.slots.get(i).getItem();
//            final ItemStack itemstack2 = (ItemStack)this.lastSlots.get(i);
//            if (!ItemStack.matches(itemstack2, itemstack)) {
//                final boolean clientStackChanged = !itemstack2.equals(itemstack, true);
//                final ItemStack itemstack3 = itemstack.copy();
//                this.lastSlots.set(i, itemstack3);
//                if (clientStackChanged) {
//                    for (final IContainerListener icontainerlistener : this.containerListeners) {
//                        if (icontainerlistener instanceof ServerPlayerEntity && i == 36) {
//                            final ServerPlayerEntity playerEntity = (ServerPlayerEntity)icontainerlistener;
//                            ModNetwork.CHANNEL.sendTo(new SyncOversizedStackMessage(this.containerId, i, itemstack2), playerEntity.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
//                        }
//                        else {
//                            icontainerlistener.slotChanged(this, i, itemstack3);
//                        }
//                    }
//                }
//            }
//        }
//        for (int j = 0; j < this.dataSlots.size(); ++j) {
//            final IntReferenceHolder intreferenceholder = this.dataSlots.get(j);
//            if (intreferenceholder.checkAndClearUpdateFlag()) {
//                for (final IContainerListener icontainerlistener2 : this.containerListeners) {
//                    icontainerlistener2.setContainerData(this, j, intreferenceholder.get());
//                }
//            }
//        }
        super.broadcastChanges();
    }

    public void addSlotListener(final IContainerListener listener) {
//        if (!this.containerListeners.contains(listener)) {
//            this.containerListeners.add(listener);
//            if (listener instanceof ServerPlayerEntity) {
//                final ServerPlayerEntity player = (ServerPlayerEntity)listener;
//                for (int i = 0; i < this.slots.size(); ++i) {
//                    final ItemStack stack = this.slots.get(i).getItem();
//                    final int slotIndex = i;
//                    ServerScheduler.INSTANCE.schedule(0, () -> ModNetwork.CHANNEL.sendTo(new SyncOversizedStackMessage(this.containerId, slotIndex, stack), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT));
//                }
//                player.connection.send((IPacket)new SSetSlotPacket(-1, -1, player.inventory.getCarried()));
//            }
//            else {
//                listener.refreshContainer(this, this.getItems());
//            }
//            this.broadcastChanges();
//        }
        super.addSlotListener(listener);
    }

    protected boolean moveItemStackTo(final ItemStack stack, final int startIndex, final int endIndex, final boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }
        while (!stack.isEmpty()) {
            if (reverseDirection) {
                if (i < startIndex) {
                    break;
                }
            } else if (i >= endIndex) {
                break;
            }
            final Slot slot = this.slots.get(i);
            final ItemStack slotStack = slot.getItem();
            if (!slotStack.isEmpty() && slotStack.getItem() == stack.getItem() && ItemStack.tagMatches(stack, slotStack)) {
                final int j = slotStack.getCount() + stack.getCount();
                final int maxSize = slot.getMaxStackSize(slotStack);
                if (j <= maxSize) {
                    stack.setCount(0);
                    slotStack.setCount(j);
                    slot.setChanged();
                    flag = true;
                } else if (slotStack.getCount() < maxSize) {
                    stack.shrink(maxSize - slotStack.getCount());
                    slotStack.setCount(maxSize);
                    slot.setChanged();
                    flag = true;
                }
            }
            i += (reverseDirection ? -1 : 1);
        }
        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }
            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }
                final Slot slot2 = this.slots.get(i);
                final ItemStack itemstack1 = slot2.getItem();
                if (itemstack1.isEmpty() && slot2.mayPlace(stack)) {
                    if (stack.getCount() > slot2.getMaxStackSize(stack)) {
                        slot2.set(stack.split(slot2.getMaxStackSize(stack)));
                    } else {
                        slot2.set(stack.split(stack.getCount()));
                    }
                    slot2.setChanged();
                    flag = true;
                    break;
                }
                i += (reverseDirection ? -1 : 1);
            }
        }
        return flag;
    }

    public static boolean canAddItemToSlot(@Nullable final Slot slot, @Nonnull final ItemStack stack, final boolean stackSizeMatters) {
        final boolean flag = slot == null || !slot.hasItem();
        if (slot != null) {
            final ItemStack slotStack = slot.getItem();
            if (!flag && stack.sameItem(slotStack) && ItemStack.tagMatches(slotStack, stack)) {
                return slotStack.getCount() + (stackSizeMatters ? 0 : stack.getCount()) <= slot.getMaxStackSize(slotStack);
            }
        }
        return flag;
    }
}
