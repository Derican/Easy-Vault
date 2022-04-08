package iskallia.vault.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class ItemBit
        extends Item {
    protected int value;

    public ItemBit(ItemGroup group, ResourceLocation id, int value) {
        super((new Item.Properties())
                .tab(group)
                .stacksTo(64));

        setRegistryName(id);

        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemBit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */