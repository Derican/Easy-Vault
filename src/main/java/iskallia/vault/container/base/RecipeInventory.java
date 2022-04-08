package iskallia.vault.container.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;
import java.util.function.Consumer;

public abstract class RecipeInventory
        implements IInventory {
    protected final NonNullList<ItemStack> slots;

    public RecipeInventory(int inputCount) {
        this.slots = NonNullList.withSize(inputCount + 1, ItemStack.EMPTY);
    }


    public int getContainerSize() {
        return this.slots.size();
    }


    public boolean isEmpty() {
        return this.slots.isEmpty();
    }


    public ItemStack getItem(int index) {
        return (ItemStack) this.slots.get(index);
    }


    public ItemStack removeItem(int index, int count) {
        ItemStack itemStack = (ItemStack) this.slots.get(index);

        if (index == outputSlotIndex() && !itemStack.isEmpty()) {
            ItemStack andSplit = ItemStackHelper.removeItem((List) this.slots, index, itemStack.getCount());
            consumeIngredients();
            updateResult();
            return andSplit;
        }

        ItemStack splitStack = ItemStackHelper.removeItem((List) this.slots, index, count);
        updateResult();
        return splitStack;
    }


    public ItemStack removeItemNoUpdate(int index) {
        ItemStack andRemove = ItemStackHelper.takeItem((List) this.slots, index);
        updateResult();
        return andRemove;
    }


    public void setItem(int index, ItemStack stack) {
        this.slots.set(index, stack);
        updateResult();
    }


    public void setChanged() {
    }


    public boolean stillValid(PlayerEntity playerEntity) {
        return true;
    }


    public void clearContent() {
        this.slots.clear();
    }

    public final void updateResult() {
        ItemStack outputItemStack = getItem(outputSlotIndex());

        if (recipeFulfilled()) {
            this.slots.set(outputSlotIndex(), resultingItemStack());
        } else if (!outputItemStack.isEmpty()) {
            this.slots.set(outputSlotIndex(), ItemStack.EMPTY);
        }
    }


    public void consumeIngredients() {
        forEachInput(inputIndex -> removeItem(inputIndex.intValue(), 1));
    }


    public boolean isIngredientSlotsFilled() {
        for (int i = 0; i < this.slots.size() - 1; i++) {
            ItemStack ingredientStack = getItem(i);
            if (ingredientStack.isEmpty()) return false;
        }
        return true;
    }

    public void forEachInput(Consumer<Integer> inputConsumer) {
        for (int i = 0; i < this.slots.size() - 1; i++) {
            inputConsumer.accept(Integer.valueOf(i));
        }
    }

    public int outputSlotIndex() {
        return this.slots.size() - 1;
    }

    public boolean isIngredientIndex(int index) {
        return (index < outputSlotIndex());
    }

    public abstract boolean recipeFulfilled();

    public abstract ItemStack resultingItemStack();
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\base\RecipeInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */