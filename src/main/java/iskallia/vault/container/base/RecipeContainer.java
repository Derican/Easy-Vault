package iskallia.vault.container.base;

import iskallia.vault.util.EntityHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public abstract class RecipeContainer extends Container {
    protected RecipeInventory internalInventory;
    protected PlayerInventory playerInventory;

    protected RecipeContainer(@Nullable final ContainerType<?> containerType, final int windowId, final RecipeInventory internalInventory, final PlayerEntity player) {
        super(containerType, windowId);
        this.internalInventory = internalInventory;
        this.playerInventory = player.inventory;
        this.addInternalInventorySlots();
        this.addPlayerInventorySlots();
    }

    protected abstract void addInternalInventorySlots();

    protected void addPlayerInventorySlots() {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(this.playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col2 = 0; col2 < 9; ++col2) {
            this.addSlot(new Slot(this.playerInventory, col2, 8 + col2 * 18, 142));
        }
    }

    public ItemStack clicked(final int slotId, final int dragType, final ClickType clickTypeIn, final PlayerEntity player) {
        final ItemStack result = super.clicked(slotId, dragType, clickTypeIn, player);
        this.internalInventory.updateResult();
        return result;
    }

    public ItemStack quickMoveStack(final PlayerEntity player, final int index) {
        final Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        final ItemStack stackOnSlot = slot.getItem();
        final ItemStack copiedStack = stackOnSlot.copy();
        final int inventoryFirstIndex = this.internalInventory.getContainerSize();
        final int inventoryLastIndex = 36 + inventoryFirstIndex;
        if (index == this.internalInventory.outputSlotIndex()) {
            if (this.moveItemStackTo(stackOnSlot, inventoryFirstIndex, inventoryLastIndex, false)) {
                this.internalInventory.consumeIngredients();
                this.onResultPicked(player, index);
                return copiedStack;
            }
            return ItemStack.EMPTY;
        } else if (this.internalInventory.isIngredientIndex(index)) {
            if (this.moveItemStackTo(stackOnSlot, inventoryFirstIndex, inventoryLastIndex, false)) {
                this.internalInventory.updateResult();
                return copiedStack;
            }
            return ItemStack.EMPTY;
        } else {
            if (!this.moveItemStackTo(stackOnSlot, 0, this.internalInventory.getContainerSize() - 1, false)) {
                return ItemStack.EMPTY;
            }
            if (stackOnSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stackOnSlot.getCount() == copiedStack.getCount()) {
                return ItemStack.EMPTY;
            }
            return copiedStack;
        }
    }

    public void removed(final PlayerEntity player) {
        super.removed(player);
        this.internalInventory.forEachInput(index -> {
            final ItemStack ingredientStack = this.internalInventory.getItem(index);
            if (!ingredientStack.isEmpty()) {
                EntityHelper.giveItem(player, ingredientStack);
            }
        });
    }

    public void onResultPicked(final PlayerEntity player, final int index) {
    }
}
