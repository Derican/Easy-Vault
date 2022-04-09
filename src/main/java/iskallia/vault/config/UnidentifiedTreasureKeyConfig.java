// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class UnidentifiedTreasureKeyConfig extends Config
{
    @Expose
    private WeightedList<ProductEntry> treasureKeys;
    
    @Override
    public String getName() {
        return "unidentified_treasure_key";
    }
    
    public ItemStack getRandomKey(final Random random) {
        final ProductEntry product = this.treasureKeys.getRandom(random);
        if (product == null) {
            return ItemStack.EMPTY;
        }
        return product.generateItemStack();
    }
    
    @Override
    protected void reset() {
        (this.treasureKeys = new WeightedList<ProductEntry>()).add(new ProductEntry(ModItems.ISKALLIUM_KEY, 1, null), 1);
    }
}
