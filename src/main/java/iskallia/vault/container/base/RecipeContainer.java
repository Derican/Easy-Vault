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


public abstract class RecipeContainer
        extends Container {
    protected RecipeInventory internalInventory;
    protected PlayerInventory playerInventory;

    protected RecipeContainer(@Nullable ContainerType<?> containerType, int windowId, RecipeInventory internalInventory, PlayerEntity player) {
        super(containerType, windowId);
        this.internalInventory = internalInventory;
        this.playerInventory = player.inventory;

        addInternalInventorySlots();
        addPlayerInventorySlots();
    }


    protected void addPlayerInventorySlots() {
        for (int row = 0; row < 3; row++) {
            for (int i = 0; i < 9; i++) {
                addSlot(new Slot((IInventory) this.playerInventory, i + row * 9 + 9, 8 + i * 18, 84 + row * 18));
            }
        }


        for (int col = 0; col < 9; col++) {
            addSlot(new Slot((IInventory) this.playerInventory, col, 8 + col * 18, 142));
        }
    }


    public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        ItemStack result = super.clicked(slotId, dragType, clickTypeIn, player);
        this.internalInventory.updateResult();
        return result;
    }


    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        Slot slot = this.slots.get(index);

        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stackOnSlot = slot.getItem();
        ItemStack copiedStack = stackOnSlot.copy();

        int inventoryFirstIndex = this.internalInventory.getContainerSize();
        int inventoryLastIndex = 36 + inventoryFirstIndex;


        if (index == this.internalInventory.outputSlotIndex()) {
            if (moveItemStackTo(stackOnSlot, inventoryFirstIndex, inventoryLastIndex, false)) {
                this.internalInventory.consumeIngredients();
                onResultPicked(player, index);
                return copiedStack;
            }
            return ItemStack.EMPTY;
        }


        if (this.internalInventory.isIngredientIndex(index)) {
            if (moveItemStackTo(stackOnSlot, inventoryFirstIndex, inventoryLastIndex, false)) {
                this.internalInventory.updateResult();
                return copiedStack;
            }
            return ItemStack.EMPTY;
        }


        if (!moveItemStackTo(stackOnSlot, 0, this.internalInventory.getContainerSize() - 1, false)) {
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


    public void removed(PlayerEntity player) {
        super.removed(player);

        this.internalInventory.forEachInput(index -> {
            ItemStack ingredientStack = this.internalInventory.getItem(index.intValue());
            if (!ingredientStack.isEmpty())
                EntityHelper.giveItem(player, ingredientStack);
        });
    }

    public void onResultPicked(PlayerEntity player, int index) {
    }

    protected abstract void addInternalInventorySlots();
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\base\RecipeContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */