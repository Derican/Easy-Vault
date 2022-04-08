package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class UnidentifiedTreasureKeyConfig
        extends Config {
    @Expose
    private WeightedList<ProductEntry> treasureKeys;

    public String getName() {
        return "unidentified_treasure_key";
    }

    public ItemStack getRandomKey(Random random) {
        ProductEntry product = (ProductEntry) this.treasureKeys.getRandom(random);
        if (product == null) return ItemStack.EMPTY;
        return product.generateItemStack();
    }


    protected void reset() {
        this.treasureKeys = new WeightedList();
        this.treasureKeys.add(new ProductEntry((Item) ModItems.ISKALLIUM_KEY, 1, null), 1);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\UnidentifiedTreasureKeyConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */