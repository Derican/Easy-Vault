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

public class MysteryEggConfig extends Config {
    @Expose
    public WeightedList<ProductEntry> POOL;

    public MysteryEggConfig() {
        this.POOL = new WeightedList<ProductEntry>();
    }

    @Override
    public String getName() {
        return "mystery_egg";
    }

    @Override
    protected void reset() {
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.COW)), 4);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.PIG)), 1);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.CHICKEN)), 4);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.MOOSHROOM)), 1);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.POLAR_BEAR)), 1);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.RABBIT)), 2);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.SHEEP)), 4);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.SQUID)), 4);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.TURTLE)), 1);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.STRIDER)), 2);
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
