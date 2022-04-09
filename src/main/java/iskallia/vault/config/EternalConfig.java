// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.RangeEntry;
import iskallia.vault.init.ModItems;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class EternalConfig extends Config
{
    @Expose
    private int expPerLevel;
    @Expose
    private final Map<String, RangeEntry> foodExpRanges;
    
    public EternalConfig() {
        this.foodExpRanges = new HashMap<String, RangeEntry>();
    }
    
    @Override
    public String getName() {
        return "eternal";
    }
    
    public int getExpForLevel(final int nextLevel) {
        return this.expPerLevel * nextLevel;
    }
    
    public Optional<Integer> getFoodExp(final Item foodItem) {
        return Optional.ofNullable(this.foodExpRanges.get(foodItem.getRegistryName().toString())).map((Function<? super RangeEntry, ? extends Integer>)RangeEntry::getRandom);
    }
    
    @Override
    protected void reset() {
        this.expPerLevel += 150;
        this.foodExpRanges.clear();
        this.foodExpRanges.put(ModItems.CRYSTAL_BURGER.getRegistryName().toString(), new RangeEntry(80, 125));
        this.foodExpRanges.put(ModItems.FULL_PIZZA.getRegistryName().toString(), new RangeEntry(40, 70));
    }
}
