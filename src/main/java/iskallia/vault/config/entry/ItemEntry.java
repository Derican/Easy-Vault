package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public class ItemEntry extends SingleItemEntry {
    @Expose
    public int AMOUNT;

    public ItemEntry(final String item, final int amount, final String nbt) {
        super(item, nbt);
        this.AMOUNT = amount;
    }

    public ItemEntry(final ResourceLocation key, final int amount, final CompoundNBT nbt) {
        this(key.toString(), amount, nbt.toString());
    }

    public ItemEntry(final IItemProvider item, final int amount) {
        this(item.asItem().getRegistryName(), amount, new CompoundNBT());
    }

    public ItemEntry(final ItemStack itemStack) {
        this(itemStack.getItem().getRegistryName(), itemStack.getCount(), itemStack.getOrCreateTag());
    }

    @Override
    public ItemStack createItemStack() {
        final ItemStack stack = super.createItemStack();
        stack.setCount(this.AMOUNT);
        return stack;
    }
}
