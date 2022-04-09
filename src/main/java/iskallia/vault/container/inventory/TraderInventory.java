// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.container.inventory;

import iskallia.vault.vending.Product;
import iskallia.vault.vending.Trade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class TraderInventory implements IInventory
{
    public static final int BUY_SLOT = 0;
    public static final int EXTRA_SLOT = 1;
    public static final int SELL_SLOT = 2;
    private final NonNullList<ItemStack> slots;
    private Trade selectedTrade;
    
    public TraderInventory() {
        this.slots = (NonNullList<ItemStack>)NonNullList.withSize(3, ItemStack.EMPTY);
    }
    
    public void updateTrade(final Trade core) {
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
    
    public ItemStack getItem(final int index) {
        return (ItemStack)this.slots.get(index);
    }
    
    public ItemStack removeItem(final int index, final int count) {
        final ItemStack itemStack = (ItemStack)this.slots.get(index);
        if (index == 2 && !itemStack.isEmpty()) {
            final ItemStack andSplit = ItemStackHelper.removeItem((List)this.slots, index, itemStack.getCount());
            this.removeItem(0, this.selectedTrade.getBuy().getAmount());
            this.selectedTrade.onTraded();
            this.updateRecipe();
            return andSplit;
        }
        final ItemStack splitStack = ItemStackHelper.removeItem((List)this.slots, index, count);
        this.updateRecipe();
        return splitStack;
    }
    
    public ItemStack removeItemNoUpdate(final int index) {
        final ItemStack andRemove = ItemStackHelper.takeItem((List)this.slots, index);
        this.updateRecipe();
        return andRemove;
    }
    
    public void setItem(final int index, final ItemStack stack) {
        this.slots.set(index, stack);
        this.updateRecipe();
    }
    
    public void setChanged() {
    }
    
    public boolean stillValid(final PlayerEntity player) {
        return true;
    }
    
    public void updateRecipe() {
        if (this.selectedTrade == null) {
            return;
        }
        final Trade trade = this.selectedTrade;
        final Product buy = trade.getBuy();
        final Product sell = trade.getSell();
        if (((ItemStack)this.slots.get(0)).getItem() != buy.getItem()) {
            this.slots.set(2, ItemStack.EMPTY);
        }
        else if (((ItemStack)this.slots.get(0)).getCount() < buy.getAmount()) {
            this.slots.set(2, ItemStack.EMPTY);
        }
        else {
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
