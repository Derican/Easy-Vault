package iskallia.vault.altar;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class RequiredItem {
    private ItemStack item;
    private int currentAmount;
    private int amountRequired;

    public RequiredItem(ItemStack stack, int currentAmount, int amountRequired) {
        this.item = stack;
        this.currentAmount = currentAmount;
        this.amountRequired = amountRequired;
    }

    public static CompoundNBT serializeNBT(RequiredItem requiredItem) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("item", (INBT) requiredItem.getItem().serializeNBT());
        nbt.putInt("currentAmount", requiredItem.getCurrentAmount());
        nbt.putInt("amountRequired", requiredItem.getAmountRequired());
        return nbt;
    }

    public static RequiredItem deserializeNBT(CompoundNBT nbt) {
        if (!nbt.contains("item"))
            return null;
        return new RequiredItem(ItemStack.of(nbt.getCompound("item")), nbt.getInt("currentAmount"), nbt.getInt("amountRequired"));
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getCurrentAmount() {
        return this.currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public void addAmount(int amount) {
        this.currentAmount += amount;
    }

    public int getAmountRequired() {
        return this.amountRequired;
    }

    public void setAmountRequired(int amountRequired) {
        this.amountRequired = amountRequired;
    }

    public boolean reachedAmountRequired() {
        return (getCurrentAmount() >= getAmountRequired());
    }

    public int getRemainder(int amount) {
        return Math.max(getCurrentAmount() + amount - getAmountRequired(), 0);
    }

    public boolean isItemEqual(ItemStack stack) {
        return ItemStack.isSameIgnoreDurability(getItem(), stack);
    }

    public RequiredItem copy() {
        return new RequiredItem(this.item.copy(), this.currentAmount, this.amountRequired);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\altar\RequiredItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */