package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class SingleItemEntry {
    @Expose
    public final String ITEM;

    public SingleItemEntry(String item, String nbt) {
        this.ITEM = item;
        this.NBT = nbt;
    }

    @Expose
    public final String NBT;

    public SingleItemEntry(ResourceLocation key, CompoundNBT nbt) {
        this(key.toString(), nbt.toString());
    }

    public SingleItemEntry(IItemProvider item) {
        this(item.asItem().getRegistryName(), new CompoundNBT());
    }

    public SingleItemEntry(ItemStack itemStack) {
        this(itemStack.getItem().getRegistryName(), itemStack.getOrCreateTag());
    }

    public ItemStack createItemStack() {
        return Registry.ITEM.getOptional(new ResourceLocation(this.ITEM)).map(item -> {
            ItemStack stack = new ItemStack((IItemProvider) item);
            try {
                if (this.NBT != null) {
                    CompoundNBT tag = JsonToNBT.parseTag(this.NBT);
                    if (!tag.isEmpty()) {
                        stack.setTag(tag);
                    }
                }
            } catch (CommandSyntaxException commandSyntaxException) {
            }
            return stack;
        }).orElse(ItemStack.EMPTY);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\SingleItemEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */