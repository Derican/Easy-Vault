package iskallia.vault.container.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;
import java.util.function.Consumer;

public abstract class RecipeInventory implements IInventory {
    protected final NonNullList<ItemStack> slots;

    public RecipeInventory(final int inputCount) {
        this.slots = (NonNullList<ItemStack>) NonNullList.withSize(inputCount + 1, ItemStack.EMPTY);
    }

    public int getContainerSize() {
        return this.slots.size();
    }

    public boolean isEmpty() {
        return this.slots.isEmpty();
    }

    public ItemStack getItem(final int index) {
        return (ItemStack) this.slots.get(index);
    }

    public ItemStack removeItem(final int index, final int count) {
        final ItemStack itemStack = (ItemStack) this.slots.get(index);
        if (index == this.outputSlotIndex() && !itemStack.isEmpty()) {
            final ItemStack andSplit = ItemStackHelper.removeItem((List) this.slots, index, itemStack.getCount());
            this.consumeIngredients();
            this.updateResult();
            return andSplit;
        }
        final ItemStack splitStack = ItemStackHelper.removeItem((List) this.slots, index, count);
        this.updateResult();
        return splitStack;
    }

    public ItemStack removeItemNoUpdate(final int index) {
        final ItemStack andRemove = ItemStackHelper.takeItem((List) this.slots, index);
        this.updateResult();
        return andRemove;
    }

    public void setItem(final int index, final ItemStack stack) {
        this.slots.set(index, stack);
        this.updateResult();
    }

    public void setChanged() {
    }

    public boolean stillValid(final PlayerEntity playerEntity) {
        return true;
    }

    public void clearContent() {
        this.slots.clear();
    }

    public final void updateResult() {
        final ItemStack outputItemStack = this.getItem(this.outputSlotIndex());
        if (this.recipeFulfilled()) {
            this.slots.set(this.outputSlotIndex(), this.resultingItemStack());
        } else if (!outputItemStack.isEmpty()) {
            this.slots.set(this.outputSlotIndex(), ItemStack.EMPTY);
        }
    }

    public void consumeIngredients() {
        this.forEachInput(inputIndex -> this.removeItem(inputIndex, 1));
    }

    public abstract boolean recipeFulfilled();

    public abstract ItemStack resultingItemStack();

    public boolean isIngredientSlotsFilled() {
        for (int i = 0; i < this.slots.size() - 1; ++i) {
            final ItemStack ingredientStack = this.getItem(i);
            if (ingredientStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void forEachInput(final Consumer<Integer> inputConsumer) {
        for (int i = 0; i < this.slots.size() - 1; ++i) {
            inputConsumer.accept(i);
        }
    }

    public int outputSlotIndex() {
        return this.slots.size() - 1;
    }

    public boolean isIngredientIndex(final int index) {
        return index < this.outputSlotIndex();
    }
}
