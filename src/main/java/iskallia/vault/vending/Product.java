package iskallia.vault.vending;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.nbt.INBTSerializable;
import iskallia.vault.util.nbt.NBTSerialize;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class Product implements INBTSerializable {
    protected Item itemCache;
    protected CompoundNBT nbtCache;
    @Expose
    @NBTSerialize
    protected String id;
    @Expose
    @NBTSerialize
    protected String nbt;
    @Expose
    @NBTSerialize
    protected int amount;

    public Product() {
    }

    public Product(final Item item, final int amount, final CompoundNBT nbt) {
        this.itemCache = item;
        if (this.itemCache != null) {
            this.id = item.getRegistryName().toString();
        }
        this.nbtCache = nbt;
        if (this.nbtCache != null) {
            this.nbt = this.nbtCache.toString();
        }
        this.amount = amount;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Product product = (Product) obj;
        final boolean similarNBT = this.getNBT() == null || product.getNBT() == null || this.getNBT().equals(product.getNBT());
        return product.getItem() == this.getItem() && similarNBT;
    }

    public int getAmount() {
        return this.amount;
    }

    public Item getItem() {
        if (this.itemCache != null) {
            return this.itemCache;
        }
        this.itemCache = (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.id));
        if (this.itemCache == null) {
            System.out.println("Unknown item " + this.id + ".");
        }
        return this.itemCache;
    }

    public String getId() {
        return this.id;
    }

    public CompoundNBT getNBT() {
        if (this.nbt == null) {
            return null;
        }
        try {
            if (this.nbtCache == null) {
                this.nbtCache = JsonToNBT.parseTag(this.nbt);
            }
        } catch (final Exception e) {
            this.nbtCache = null;
            System.out.println("Unknown NBT for item " + this.id + ".");
        }
        return this.nbtCache;
    }

    public boolean isValid() {
        return this.getAmount() > 0 && this.getItem() != null && this.getItem() != Items.AIR && this.getAmount() <= this.getItem().getMaxStackSize() && (this.nbt == null || this.getNBT() != null);
    }

    public ItemStack toStack() {
        final ItemStack stack = new ItemStack((IItemProvider) this.getItem(), this.getAmount());
        stack.setTag(this.getNBT());
        return stack;
    }

    @Override
    public String toString() {
        return "{ id='" + this.id + '\'' + ", nbt='" + this.nbt + '\'' + ", amount=" + this.amount + '}';
    }
}
