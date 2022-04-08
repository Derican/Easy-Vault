package iskallia.vault.config.entry.vending;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.vending.Product;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

public class ProductEntry {
    @Expose
    protected String id;
    @Expose
    protected String nbt;
    @Expose
    protected int amountMin;
    @Expose
    protected int amountMax;

    public ProductEntry() {
    }

    public ProductEntry(Item item) {
        this(item, 1, null);
    }

    public ProductEntry(ItemStack stack) {
        this(stack.getItem(), stack.getCount(), stack.getTag());
    }

    public ProductEntry(Item item, int amount, @Nullable CompoundNBT nbt) {
        this(item, amount, amount, nbt);
    }

    public ProductEntry(Item item, int amountMin, int amountMax, @Nullable CompoundNBT nbt) {
        this.id = ((ResourceLocation) Objects.<ResourceLocation>requireNonNull(item.getRegistryName())).toString();
        this.nbt = (nbt == null) ? null : nbt.toString();
        this.amountMin = amountMin;
        this.amountMax = amountMax;
    }

    public Item getItem() {
        return (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.id));
    }

    public int generateAmount() {
        return MathUtilities.getRandomInt(this.amountMin, this.amountMax);
    }

    public CompoundNBT getNBT() {
        if (this.nbt == null) return null;
        try {
            return JsonToNBT.parseTag(this.nbt);
        } catch (Exception e) {
            return null;
        }
    }

    public Product toProduct() {
        return new Product(getItem(), generateAmount(), getNBT());
    }

    public ItemStack generateItemStack() {
        ItemStack itemStack = new ItemStack((IItemProvider) getItem(), generateAmount());
        itemStack.setTag(getNBT());
        return itemStack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\vending\ProductEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */