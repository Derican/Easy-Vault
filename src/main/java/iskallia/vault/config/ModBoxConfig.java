// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ModBoxConfig extends Config
{
    @Expose
    public Map<String, WeightedList<ProductEntry>> POOL;
    
    public ModBoxConfig() {
        this.POOL = new HashMap<String, WeightedList<ProductEntry>>();
    }
    
    @Override
    public String getName() {
        return "mod_box";
    }
    
    @Override
    protected void reset() {
        final WeightedList<ProductEntry> none = new WeightedList<ProductEntry>();
        none.add(new ProductEntry(Items.APPLE, 8, null), 3);
        none.add(new ProductEntry(Items.GOLDEN_APPLE, 1, null), 1);
        this.POOL.put("None", none);
        final WeightedList<ProductEntry> decorator = new WeightedList<ProductEntry>();
        decorator.add(new ProductEntry(Items.APPLE, 8, null), 3);
        decorator.add(new ProductEntry(Items.GOLDEN_APPLE, 1, null), 1);
        this.POOL.put("Decorator", decorator);
        final WeightedList<ProductEntry> refinedStorage = new WeightedList<ProductEntry>();
        refinedStorage.add(new ProductEntry(Items.APPLE, 8, null), 3);
        refinedStorage.add(new ProductEntry(Items.GOLDEN_APPLE, 1, null), 1);
        this.POOL.put("Storage Refined", refinedStorage);
        final WeightedList<ProductEntry> oneWithEnder = new WeightedList<ProductEntry>();
        oneWithEnder.add(new ProductEntry(Items.APPLE, 8, null), 3);
        oneWithEnder.add(new ProductEntry(Items.GOLDEN_APPLE, 1, null), 1);
        this.POOL.put("One with Ender", oneWithEnder);
    }
}
