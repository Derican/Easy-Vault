package iskallia.vault.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class ItemVaultRune
        extends Item {
    public ItemVaultRune(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties())
                .tab(group)
                .fireResistant()
                .stacksTo(1));

        setRegistryName(id);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemVaultRune.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */