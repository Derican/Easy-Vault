package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public class ItemEntry extends SingleItemEntry {
    @Expose
    public int AMOUNT;

    public ItemEntry(String item, int amount, String nbt) {
        super(item, nbt);
        this.AMOUNT = amount;
    }

    public ItemEntry(ResourceLocation key, int amount, CompoundNBT nbt) {
        this(key.toString(), amount, nbt.toString());
    }

    public ItemEntry(IItemProvider item, int amount) {
        this(item.asItem().getRegistryName(), amount, new CompoundNBT());
    }

    public ItemEntry(ItemStack itemStack) {
        this(itemStack.getItem().getRegistryName(), itemStack.getCount(), itemStack.getOrCreateTag());
    }


    public ItemStack createItemStack() {
        ItemStack stack = super.createItemStack();
        stack.setCount(this.AMOUNT);
        return stack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\ItemEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */