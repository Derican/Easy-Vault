package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.RangeEntry;
import iskallia.vault.init.ModItems;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EternalConfig
        extends Config {
    @Expose
    private int expPerLevel;
    @Expose
    private final Map<String, RangeEntry> foodExpRanges = new HashMap<>();


    public String getName() {
        return "eternal";
    }

    public int getExpForLevel(int nextLevel) {
        return this.expPerLevel * nextLevel;
    }

    public Optional<Integer> getFoodExp(Item foodItem) {
        return Optional.ofNullable(this.foodExpRanges.get(foodItem.getRegistryName().toString())).map(RangeEntry::getRandom);
    }


    protected void reset() {
        this.expPerLevel += 150;

        this.foodExpRanges.clear();
        this.foodExpRanges.put(ModItems.CRYSTAL_BURGER.getRegistryName().toString(), new RangeEntry(80, 125));
        this.foodExpRanges.put(ModItems.FULL_PIZZA.getRegistryName().toString(), new RangeEntry(40, 70));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\EternalConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */