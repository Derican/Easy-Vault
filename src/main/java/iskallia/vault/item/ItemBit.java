// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class ItemBit extends Item
{
    protected int value;
    
    public ItemBit(final ItemGroup group, final ResourceLocation id, final int value) {
        super(new Item.Properties().tab(group).stacksTo(64));
        this.setRegistryName(id);
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
}
