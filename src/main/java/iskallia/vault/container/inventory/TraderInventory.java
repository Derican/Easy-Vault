package iskallia.vault.container.inventory;

import iskallia.vault.vending.Product;
import iskallia.vault.vending.Trade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class TraderInventory
        implements IInventory {
    public static final int BUY_SLOT = 0;
    public static final int EXTRA_SLOT = 1;
    public static final int SELL_SLOT = 2;
    private final NonNullList<ItemStack> slots = NonNullList.withSize(3, ItemStack.EMPTY);
    private Trade selectedTrade;

    public void updateTrade(Trade core) {
        this.selectedTrade = core;
    }

    public Trade getSelectedTrade() {
        return this.selectedTrade;
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

        if (index == 2 && !itemStack.isEmpty()) {
            ItemStack andSplit = ItemStackHelper.removeItem((List) this.slots, index, itemStack.getCount());
            removeItem(0, this.selectedTrade.getBuy().getAmount());
            this.selectedTrade.onTraded();
            updateRecipe();
            return andSplit;
        }

        ItemStack splitStack = ItemStackHelper.removeItem((List) this.slots, index, count);
        updateRecipe();
        return splitStack;
    }


    public ItemStack removeItemNoUpdate(int index) {
        ItemStack andRemove = ItemStackHelper.takeItem((List) this.slots, index);
        updateRecipe();
        return andRemove;
    }


    public void setItem(int index, ItemStack stack) {
        this.slots.set(index, stack);
        updateRecipe();
    }


    public void setChanged() {
    }


    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    public void updateRecipe() {
        if (this.selectedTrade == null)
            return;
        Trade trade = this.selectedTrade;
        Product buy = trade.getBuy();
        Product sell = trade.getSell();

        if (((ItemStack) this.slots.get(0)).getItem() != buy.getItem()) {
            this.slots.set(2, ItemStack.EMPTY);
        } else if (((ItemStack) this.slots.get(0)).getCount() < buy.getAmount()) {
            this.slots.set(2, ItemStack.EMPTY);
        } else {
            this.slots.set(2, sell.toStack());
        }

        if (trade.getTradesLeft() == 0) {
            this.slots.set(2, ItemStack.EMPTY);
        }
    }


    public void clearContent() {
        this.slots.clear();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\inventory\TraderInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */