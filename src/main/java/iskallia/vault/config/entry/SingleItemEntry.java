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
    @Expose
    public final String NBT;

    public SingleItemEntry(final String item, final String nbt) {
        this.ITEM = item;
        this.NBT = nbt;
    }

    public SingleItemEntry(final ResourceLocation key, final CompoundNBT nbt) {
        this(key.toString(), nbt.toString());
    }

    public SingleItemEntry(final IItemProvider item) {
        this(item.asItem().getRegistryName(), new CompoundNBT());
    }

    public SingleItemEntry(final ItemStack itemStack) {
        this(itemStack.getItem().getRegistryName(), itemStack.getOrCreateTag());
    }

    public ItemStack createItemStack() {
        return Registry.ITEM.getOptional(new ResourceLocation(this.ITEM)).map(item -> {
            final ItemStack stack = new ItemStack((IItemProvider) item);
            try {
                if (this.NBT != null) {
                    final CompoundNBT tag = JsonToNBT.parseTag(this.NBT);
                    if (!tag.isEmpty()) {
                        stack.setTag(tag);
                    }
                }
            } catch (final CommandSyntaxException ex) {
            }
            return stack;
        }).orElse(ItemStack.EMPTY);
    }
}
