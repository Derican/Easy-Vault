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
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.ZOMBIE)), 8);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.SKELETON)), 8);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.BLAZE)), 6);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.WITHER_SKELETON)), 6);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.ENDERMAN)), 6);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.DROWNED)), 6);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.CREEPER)), 8);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.RAVAGER)), 2);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.SLIME)), 8);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.SPIDER)), 8);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.VINDICATOR)), 3);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.WITCH)), 6);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.ZOMBIFIED_PIGLIN)), 6);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.GUARDIAN)), 6);
        this.POOL.add(new ProductEntry(this.getEgg(EntityType.MAGMA_CUBE)), 6);
    }

    private Item getEgg(final EntityType<?> type) {
        Item item = Items.AIR;
        try {
            item = StreamSupport.stream(SpawnEggItem.eggs().spliterator(), false).filter(eggItem -> type.equals(eggItem.getType(null))).findAny().get();

        } catch (NoSuchElementException e) {

        }
        return item;
    }
}
