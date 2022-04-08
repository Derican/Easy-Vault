package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.Items;

public class PandorasBoxConfig extends Config {
    @Expose
    public WeightedList<ProductEntry> POOL = new WeightedList();


    public String getName() {
        return "pandoras_box";
    }


    protected void reset() {
        this.POOL.add(new ProductEntry(Items.APPLE, 8, null), 3);
        this.POOL.add(new ProductEntry(Items.GOLDEN_APPLE, 1, null), 1);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\PandorasBoxConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */