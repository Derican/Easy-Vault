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

public class Product
        implements INBTSerializable {
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

    public Product(Item item, int amount, CompoundNBT nbt) {
        this.itemCache = item;
        if (this.itemCache != null)
            this.id = item.getRegistryName().toString();
        this.nbtCache = nbt;
        if (this.nbtCache != null)
            this.nbt = this.nbtCache.toString();
        this.amount = amount;
    }

    public boolean equals(Object obj) {
        boolean similarNBT;
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (getClass() != obj.getClass()) {
            return false;
        }
        Product product = (Product) obj;


        if (getNBT() != null && product.getNBT() != null) {
            similarNBT = getNBT().equals(product.getNBT());
        } else {
            similarNBT = true;
        }

        return (product.getItem() == getItem() && similarNBT);
    }

    public int getAmount() {
        return this.amount;
    }

    public Item getItem() {
        if (this.itemCache != null)
            return this.itemCache;
        this.itemCache = (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.id));
        if (this.itemCache == null)
            System.out.println("Unknown item " + this.id + ".");
        return this.itemCache;
    }

    public String getId() {
        return this.id;
    }

    public CompoundNBT getNBT() {
        if (this.nbt == null)
            return null;
        try {
            if (this.nbtCache == null)
                this.nbtCache = JsonToNBT.parseTag(this.nbt);
        } catch (Exception e) {
            this.nbtCache = null;
            System.out.println("Unknown NBT for item " + this.id + ".");
        }
        return this.nbtCache;
    }

    public boolean isValid() {
        if (getAmount() <= 0)
            return false;
        if (getItem() == null)
            return false;
        if (getItem() == Items.AIR)
            return false;
        if (getAmount() > getItem().getMaxStackSize())
            return false;
        if (this.nbt != null && getNBT() == null)
            return false;
        return true;
    }

    public ItemStack toStack() {
        ItemStack stack = new ItemStack((IItemProvider) getItem(), getAmount());
        stack.setTag(getNBT());
        return stack;
    }


    public String toString() {
        return "{ id='" + this.id + '\'' + ", nbt='" + this.nbt + '\'' + ", amount=" + this.amount + '}';
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\vending\Product.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */