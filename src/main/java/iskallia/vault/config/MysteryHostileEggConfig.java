package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;

import java.util.NoSuchElementException;
import java.util.stream.StreamSupport;

public class MysteryHostileEggConfig extends Config {
    @Expose
    public WeightedList<ProductEntry> POOL;

    public MysteryHostileEggConfig() {
        this.POOL = new WeightedList<ProductEntry>();
    }

    @Override
    public String getName() {
        return "mystery_hostile_egg";
    }

    @Override
    protected void reset() {
        this.POOL.add(new ProductEntry(this.getEgg((EntityType<?>) EntityType.ZOMBIE)), 3);
        this.POOL.add(new ProductEntry(this.getEgg((EntityType<?>) EntityType.SKELETON)), 1);
    }

    private Item getEgg(final EntityType<?> type) {
        Item item = Items.AIR;
        try {
            item = StreamSupport.stream(SpawnEggItem.eggs().spliterator(), false).filter(eggItem -> type.equals(eggItem.getType((CompoundNBT) null))).findAny().get();

        } catch (NoSuchElementException e) {

        }
        return item;
    }
}
