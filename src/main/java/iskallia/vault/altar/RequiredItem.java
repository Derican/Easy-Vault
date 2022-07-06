package iskallia.vault.altar;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RequiredItem {
    private ItemStack item;
    private int currentAmount;
    private int amountRequired;

    public RequiredItem(final ItemStack stack, final int currentAmount, final int amountRequired) {
        this.item = stack;
        this.currentAmount = currentAmount;
        this.amountRequired = amountRequired;
    }

    public RequiredItem(final Item item, final int currentAmount, final int amountRequired) {
        this(new ItemStack((IItemProvider) item), currentAmount, amountRequired);
    }

    public static CompoundNBT serializeNBT(final RequiredItem requiredItem) {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.put("item", (INBT) requiredItem.getItem().serializeNBT());
        nbt.putInt("currentAmount", requiredItem.getCurrentAmount());
        nbt.putInt("amountRequired", requiredItem.getAmountRequired());
        return nbt;
    }

    public static RequiredItem deserializeNBT(final CompoundNBT nbt) {
        if (!nbt.contains("item")) {
            return null;
        }
        return new RequiredItem(ItemStack.of(nbt.getCompound("item")), nbt.getInt("currentAmount"), nbt.getInt("amountRequired"));
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(final ItemStack item) {
        this.item = item;
    }

    public int getCurrentAmount() {
        return this.currentAmount;
    }

    public void setCurrentAmount(final int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public void addAmount(final int amount) {
        this.currentAmount += amount;
    }

    public int getAmountRequired() {
        return this.amountRequired;
    }

    public void setAmountRequired(final int amountRequired) {
        this.amountRequired = amountRequired;
    }

    public boolean reachedAmountRequired() {
        return this.getCurrentAmount() >= this.getAmountRequired();
    }

    public int getRemainder(final int amount) {
        return Math.max(this.getCurrentAmount() + amount - this.getAmountRequired(), 0);
    }

    public boolean isItemEqual(final ItemStack stack) {
        return ItemStack.isSameIgnoreDurability(this.getItem(), stack);
    }

    public RequiredItem copy() {
        return new RequiredItem(this.item.copy(), this.currentAmount, this.amountRequired);
    }
}
