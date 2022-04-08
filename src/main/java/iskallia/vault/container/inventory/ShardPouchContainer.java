package iskallia.vault.container.inventory;

import com.google.common.collect.Sets;
import iskallia.vault.container.slot.ConditionalReadSlot;
import iskallia.vault.init.ModContainers;
import iskallia.vault.init.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class ShardPouchContainer
        extends Container {
    private final int pouchSlot;
    private final PlayerInventory inventory;
    private int dragMode = -1;
    private int dragEvent;
    private final Set<Slot> dragSlots = Sets.newHashSet();

    public ShardPouchContainer(int id, PlayerInventory inventory, int pouchSlot) {
        super(ModContainers.SHARD_POUCH_CONTAINER, id);
        this.inventory = inventory;
        this.pouchSlot = pouchSlot;

        if (hasPouch()) {
            inventory.player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(playerInvHandler -> {
                ItemStack pouch = this.inventory.getItem(this.pouchSlot);
                pouch.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((net.minecraftforge.items.IItemHandler handler) -> {
                });
            });
        }
    }


    private void initSlots(IItemHandler playerInvHandler, final IItemHandler pouchHandler) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot((Slot) new ConditionalReadSlot(playerInvHandler, column + row * 9 + 9, 8 + column * 18, 55 + row * 18, this::canAccess));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            addSlot((Slot) new ConditionalReadSlot(playerInvHandler, hotbarSlot, 8 + hotbarSlot * 18, 113, this::canAccess));
        }

        addSlot((Slot) new ConditionalReadSlot(pouchHandler, 0, 80, 16, (slot, stack) ->
                (canAccess(slot.intValue(), stack) && stack.getItem() == ModItems.SOUL_SHARD)) {
            public int getMaxStackSize(@Nonnull ItemStack stack) {
                return pouchHandler.getSlotLimit(0);
            }


            public void setChanged() {
                ((IItemHandlerModifiable) getItemHandler()).setStackInSlot(getSlotIndex(), getItem());
            }
        });
    }


    public boolean stillValid(PlayerEntity player) {
        return hasPouch();
    }

    public boolean canAccess(int slot, ItemStack slotStack) {
        return (hasPouch() && !(slotStack.getItem() instanceof iskallia.vault.item.ItemShardPouch));
    }

    public boolean hasPouch() {
        ItemStack pouchStack = this.inventory.getItem(this.pouchSlot);
        return (!pouchStack.isEmpty() && pouchStack.getItem() instanceof iskallia.vault.item.ItemShardPouch);
    }


    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();

            if (index >= 0 && index < 36 &&
                    moveItemStackTo(slotStack, 36, 37, false)) {
                return itemstack;
            }

            if (index >= 0 && index < 27) {
                if (!moveItemStackTo(slotStack, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 27 && index < 36) {
                if (!moveItemStackTo(slotStack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(slotStack, 0, 36, false)) {
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
    public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        ItemStack returnStack = ItemStack.EMPTY;
        PlayerInventory playerInventory = player.inventory;

        if (clickTypeIn == ClickType.QUICK_CRAFT) {
            int j1 = this.dragEvent;
            this.dragEvent = getQuickcraftHeader(dragType);

            if ((j1 != 1 || this.dragEvent != 2) && j1 != this.dragEvent) {
                resetQuickCraft();
            } else if (playerInventory.getCarried().isEmpty()) {
                resetQuickCraft();
            } else if (this.dragEvent == 0) {
                this.dragMode = getQuickcraftType(dragType);

                if (isValidQuickcraftType(this.dragMode, player)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    resetQuickCraft();
                }
            } else if (this.dragEvent == 1) {
                Slot slot = this.slots.get(slotId);
                ItemStack mouseStack = playerInventory.getCarried();

                if (slot != null && canAddItemToSlot(slot, mouseStack, true) && slot.mayPlace(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() > this.dragSlots.size()) && canDragTo(slot)) {
                    this.dragSlots.add(slot);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    ItemStack mouseStackCopy = playerInventory.getCarried().copy();
                    int k1 = playerInventory.getCarried().getCount();

                    for (Slot dragSlot : this.dragSlots) {
                        ItemStack mouseStack = playerInventory.getCarried();

                        if (dragSlot != null && canAddItemToSlot(dragSlot, mouseStack, true) && dragSlot.mayPlace(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() >= this.dragSlots.size()) && canDragTo(dragSlot)) {
                            ItemStack itemstack14 = mouseStackCopy.copy();
                            int j3 = dragSlot.hasItem() ? dragSlot.getItem().getCount() : 0;
                            getQuickCraftSlotCount(this.dragSlots, this.dragMode, itemstack14, j3);
                            int k3 = dragSlot.getMaxStackSize(itemstack14);

                            if (itemstack14.getCount() > k3) {
                                itemstack14.setCount(k3);
                            }

                            k1 -= itemstack14.getCount() - j3;
                            dragSlot.set(itemstack14);
                        }
                    }

                    mouseStackCopy.setCount(k1);
                    playerInventory.setCarried(mouseStackCopy);
                }

                resetQuickCraft();
            } else {
                resetQuickCraft();
            }
        } else if (this.dragEvent != 0) {
            resetQuickCraft();
        } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (!playerInventory.getCarried().isEmpty()) {
                    if (dragType == 0) {
                        player.drop(playerInventory.getCarried(), true);
                        playerInventory.setCarried(ItemStack.EMPTY);
                    }

                    if (dragType == 1) {
                        player.drop(playerInventory.getCarried().split(1), true);
                    }
                }
            } else if (clickTypeIn == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot = this.slots.get(slotId);

                if (slot == null || !slot.mayPickup(player)) {
                    return ItemStack.EMPTY;
                }

                ItemStack itemstack7 = quickMoveStack(player, slotId);
                for (; !itemstack7.isEmpty() && ItemStack.isSame(slot.getItem(), itemstack7);
                     itemstack7 = quickMoveStack(player, slotId)) {
                    returnStack = itemstack7.copy();
                }
            } else {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot = this.slots.get(slotId);

                if (slot != null) {
                    ItemStack slotStack = slot.getItem();
                    ItemStack mouseStack = playerInventory.getCarried();

                    if (!slotStack.isEmpty()) {
                        returnStack = slotStack.copy();
                    }

                    if (slotStack.isEmpty()) {
                        if (!mouseStack.isEmpty() && slot.mayPlace(mouseStack)) {
                            int i3 = (dragType == 0) ? mouseStack.getCount() : 1;

                            if (i3 > slot.getMaxStackSize(mouseStack)) {
                                i3 = slot.getMaxStackSize(mouseStack);
                            }

                            slot.set(mouseStack.split(i3));
                        }
                    } else if (slot.mayPickup(player)) {
                        if (mouseStack.isEmpty()) {
                            if (slotStack.isEmpty()) {
                                slot.set(ItemStack.EMPTY);
                                playerInventory.setCarried(ItemStack.EMPTY);
                            } else {
                                int toMove = (dragType == 0) ? slotStack.getCount() : ((slotStack.getCount() + 1) / 2);
                                playerInventory.setCarried(slot.remove(toMove));

                                if (slotStack.isEmpty()) {
                                    slot.set(ItemStack.EMPTY);
                                }

                                slot.onTake(player, playerInventory.getCarried());
                            }
                        } else if (slot.mayPlace(mouseStack)) {
                            if (slotStack.getItem() == mouseStack.getItem() && ItemStack.tagMatches(slotStack, mouseStack)) {
                                int k2 = (dragType == 0) ? mouseStack.getCount() : 1;

                                if (k2 > slot.getMaxStackSize(mouseStack) - slotStack.getCount()) {
                                    k2 = slot.getMaxStackSize(mouseStack) - slotStack.getCount();
                                }

                                mouseStack.shrink(k2);
                                slotStack.grow(k2);
                            } else if (mouseStack.getCount() <= slot.getMaxStackSize(mouseStack) && slotStack.getCount() <= slotStack.getMaxStackSize()) {
                                slot.set(mouseStack);
                                playerInventory.setCarried(slotStack);
                            }
                        } else if (slotStack.getItem() == mouseStack.getItem() && mouseStack.getMaxStackSize() > 1 && ItemStack.tagMatches(slotStack, mouseStack) && !slotStack.isEmpty()) {
                            int j2 = slotStack.getCount();

                            if (j2 + mouseStack.getCount() <= mouseStack.getMaxStackSize()) {
                                mouseStack.grow(j2);
                                slotStack = slot.remove(j2);

                                if (slotStack.isEmpty()) {
                                    slot.set(ItemStack.EMPTY);
                                }

                                slot.onTake(player, playerInventory.getCarried());
                            }
                        }
                    }

                    slot.setChanged();
                }
            }
        } else if (clickTypeIn != ClickType.SWAP || dragType < 0 || dragType >= 9) {

            if (clickTypeIn == ClickType.CLONE && player.abilities.instabuild && playerInventory.getCarried().isEmpty() && slotId >= 0) {
                Slot slot3 = this.slots.get(slotId);

                if (slot3 != null && slot3.hasItem()) {
                    ItemStack itemstack5 = slot3.getItem().copy();
                    itemstack5.setCount(itemstack5.getMaxStackSize());
                    playerInventory.setCarried(itemstack5);
                }
            } else if (clickTypeIn == ClickType.THROW && playerInventory.getCarried().isEmpty() && slotId >= 0) {
                Slot slot = this.slots.get(slotId);

                if (slot != null && slot.hasItem() && slot.mayPickup(player)) {
                    ItemStack itemstack4 = slot.remove((dragType == 0) ? 1 : slot.getItem().getCount());
                    slot.onTake(player, itemstack4);
                    player.drop(itemstack4, true);
                }
            } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
                Slot slot = this.slots.get(slotId);
                ItemStack mouseStack = playerInventory.getCarried();

                if (!mouseStack.isEmpty() && (slot == null || !slot.hasItem() || !slot.mayPickup(player))) {
                    int i = (dragType == 0) ? 0 : (this.slots.size() - 1);
                    int j = (dragType == 0) ? 1 : -1;

                    for (int k = 0; k < 2; k++) {
                        int l;
                        for (l = i; l >= 0 && l < this.slots.size() && mouseStack.getCount() < mouseStack.getMaxStackSize(); l += j) {
                            Slot slot1 = this.slots.get(l);

                            if (slot1.hasItem() && canAddItemToSlot(slot1, mouseStack, true) && slot1.mayPickup(player) && canTakeItemForPickAll(mouseStack, slot1)) {
                                ItemStack itemstack2 = slot1.getItem();

                                if (k != 0 || itemstack2.getCount() < slot1.getMaxStackSize(itemstack2)) {
                                    int i1 = Math.min(mouseStack.getMaxStackSize() - mouseStack.getCount(), itemstack2.getCount());
                                    ItemStack itemstack3 = slot1.remove(i1);
                                    mouseStack.grow(i1);

                                    if (itemstack3.isEmpty()) {
                                        slot1.set(ItemStack.EMPTY);
                                    }

                                    slot1.onTake(player, itemstack3);
                                }
                            }
                        }
                    }
                }

                broadcastChanges();
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
//        for (int i = 0; i < this.slots.size(); i++) {
//            ItemStack itemstack = ((Slot) this.slots.get(i)).getItem();
//            ItemStack itemstack1 = (ItemStack) this.lastSlots.get(i);
//            if (!ItemStack.matches(itemstack1, itemstack)) {
//                boolean clientStackChanged = !itemstack1.equals(itemstack, true);
//                ItemStack itemstack2 = itemstack.copy();
//                this.lastSlots.set(i, itemstack2);
//
//                if (clientStackChanged) {
//                    for (IContainerListener icontainerlistener : this.containerListeners) {
//                        if (icontainerlistener instanceof ServerPlayerEntity && i == 36) {
//                            ServerPlayerEntity playerEntity = (ServerPlayerEntity) icontainerlistener;
//                            ModNetwork.CHANNEL.sendTo(new SyncOversizedStackMessage(this.containerId, i, itemstack1), playerEntity.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
//                            continue;
//                        }
//                        icontainerlistener.slotChanged(this, i, itemstack2);
//                    }
//                }
//            }
//        }
//
//        for (int j = 0; j < this.dataSlots.size(); j++) {
//            IntReferenceHolder intreferenceholder = this.dataSlots.get(j);
//            if (intreferenceholder.checkAndClearUpdateFlag()) {
//                for (IContainerListener icontainerlistener1 : this.containerListeners) {
//                    icontainerlistener1.setContainerData(this, j, intreferenceholder.get());
//                }
//            }
//        }
        super.broadcastChanges();
    }

    public void addSlotListener(IContainerListener listener) {
//        if (!this.containerListeners.contains(listener)) {
//            this.containerListeners.add(listener);
//            if (listener instanceof ServerPlayerEntity) {
//                ServerPlayerEntity player = (ServerPlayerEntity) listener;
//                for (int i = 0; i < this.slots.size(); i++) {
//                    ItemStack stack = ((Slot) this.slots.get(i)).getItem();
//                    int slotIndex = i;
//                    ServerScheduler.INSTANCE.schedule(0, () -> ModNetwork.CHANNEL.sendTo(new SyncOversizedStackMessage(this.containerId, slotIndex, stack), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT));
//                }
//
//
//                player.connection.send((IPacket) new SSetSlotPacket(-1, -1, player.inventory.getCarried()));
//            } else {
//                listener.refreshContainer(this, getItems());
//            }
//
//            broadcastChanges();
//        }
        super.addSlotListener(listener);
    }


    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;

        if (reverseDirection) {
            i = endIndex - 1;
        }

        while (!stack.isEmpty() && (
                reverseDirection ? (
                        i < startIndex) : (

                        i >= endIndex))) {


            Slot slot = this.slots.get(i);
            ItemStack slotStack = slot.getItem();

            if (!slotStack.isEmpty() && slotStack.getItem() == stack.getItem() && ItemStack.tagMatches(stack, slotStack)) {
                int j = slotStack.getCount() + stack.getCount();
                int maxSize = slot.getMaxStackSize(slotStack);

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

            i += reverseDirection ? -1 : 1;
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }


            while (reverseDirection ? (
                    i < startIndex) : (

                    i >= endIndex)) {


                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();

                if (itemstack1.isEmpty() && slot1.mayPlace(stack)) {
                    if (stack.getCount() > slot1.getMaxStackSize(stack)) {
                        slot1.set(stack.split(slot1.getMaxStackSize(stack)));
                    } else {
                        slot1.set(stack.split(stack.getCount()));
                    }

                    slot1.setChanged();
                    flag = true;

                    break;
                }
                i += reverseDirection ? -1 : 1;
            }
        }

        return flag;
    }

    public static boolean canAddItemToSlot(@Nullable Slot slot, @Nonnull ItemStack stack, boolean stackSizeMatters) {
        boolean flag = (slot == null || !slot.hasItem());
        if (slot != null) {
            ItemStack slotStack = slot.getItem();

            if (!flag && stack.sameItem(slotStack) && ItemStack.tagMatches(slotStack, stack)) {
                return (slotStack.getCount() + (stackSizeMatters ? 0 : stack.getCount()) <= slot.getMaxStackSize(slotStack));
            }
        }
        return flag;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\inventory\ShardPouchContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */