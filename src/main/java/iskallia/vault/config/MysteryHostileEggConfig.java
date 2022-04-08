package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;

import java.util.stream.StreamSupport;

public class MysteryHostileEggConfig
        extends Config {
    @Expose
    public WeightedList<ProductEntry> POOL = new WeightedList();


    public String getName() {
        return "mystery_hostile_egg";
    }


    protected void reset() {
        this.POOL.add(new ProductEntry(getEgg(EntityType.ZOMBIE)), 3);
        this.POOL.add(new ProductEntry(getEgg(EntityType.SKELETON)), 1);
    }

    private Item getEgg(EntityType<?> type) {
        return StreamSupport.stream(SpawnEggItem.eggs().spliterator(), false)
                .filter(eggItem -> type.equals(eggItem.getType(null)))
                .findAny()
                .map(eggItem -> eggItem)
                .orElse((SpawnEggItem) Items.AIR);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\MysteryHostileEggConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */