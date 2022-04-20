package iskallia.vault.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class ItemVaultRune extends Item {
    public ItemVaultRune(final ItemGroup group, final ResourceLocation id) {
        super(new Item.Properties().tab(group).fireResistant().stacksTo(1));
        this.setRegistryName(id);
    }
}
