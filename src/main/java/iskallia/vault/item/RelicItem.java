package iskallia.vault.item;

import iskallia.vault.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public class RelicItem extends Item {
    public RelicItem(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties())
                .tab(group)
                .stacksTo(64));

        setRegistryName(id);
    }

    public static ItemStack withCustomModelData(int customModelData) {
        ItemStack itemStack = new ItemStack((IItemProvider) ModItems.VAULT_RELIC);

        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("CustomModelData", customModelData);
        itemStack.setTag(nbt);

        return itemStack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\RelicItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */