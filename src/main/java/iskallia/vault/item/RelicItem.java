// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item;

import iskallia.vault.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public class RelicItem extends Item
{
    public RelicItem(final ItemGroup group, final ResourceLocation id) {
        super(new Item.Properties().tab(group).stacksTo(64));
        this.setRegistryName(id);
    }
    
    public static ItemStack withCustomModelData(final int customModelData) {
        final ItemStack itemStack = new ItemStack((IItemProvider)ModItems.VAULT_RELIC);
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("CustomModelData", customModelData);
        itemStack.setTag(nbt);
        return itemStack;
    }
}
