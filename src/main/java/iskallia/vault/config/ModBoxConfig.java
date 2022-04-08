package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ModBoxConfig
        extends Config {
    @Expose
    public Map<String, WeightedList<ProductEntry>> POOL = new HashMap<>();


    public String getName() {
        return "mod_box";
    }


    protected void reset() {
        WeightedList<ProductEntry> none = new WeightedList();
        none.add(new ProductEntry(Items.APPLE, 8, null), 3);
        none.add(new ProductEntry(Items.GOLDEN_APPLE, 1, null), 1);
        this.POOL.put("None", none);

        WeightedList<ProductEntry> decorator = new WeightedList();
        decorator.add(new ProductEntry(Items.APPLE, 8, null), 3);
        decorator.add(new ProductEntry(Items.GOLDEN_APPLE, 1, null), 1);
        this.POOL.put("Decorator", decorator);

        WeightedList<ProductEntry> refinedStorage = new WeightedList();
        refinedStorage.add(new ProductEntry(Items.APPLE, 8, null), 3);
        refinedStorage.add(new ProductEntry(Items.GOLDEN_APPLE, 1, null), 1);
        this.POOL.put("Storage Refined", refinedStorage);

        WeightedList<ProductEntry> oneWithEnder = new WeightedList();
        oneWithEnder.add(new ProductEntry(Items.APPLE, 8, null), 3);
        oneWithEnder.add(new ProductEntry(Items.GOLDEN_APPLE, 1, null), 1);
        this.POOL.put("One with Ender", oneWithEnder);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\ModBoxConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */