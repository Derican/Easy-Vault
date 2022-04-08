package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScavengerHuntConfig extends Config {
    @Expose
    private ItemPool requiredItems;
    @Expose
    private final Map<String, ItemPool> mobDropItems = new HashMap<>();
    @Expose
    private ItemPool chestItems;
    @Expose
    private ItemPool treasureItems;

    @Nullable
    public ItemEntry getRandomRequiredItem(Predicate<Item> excludedItems) {
        return (ItemEntry) this.requiredItems.pool.copyFiltered(entry -> !excludedItems.test(entry.getItem())).getRandom(rand);
    }

    public int getTotalRequiredItems() {
        return this.requiredItems.getRandomAmount();
    }

    public List<ItemEntry> generateChestLoot(Predicate<ItemEntry> dropFilter) {
        return this.chestItems.getRandomEntries(dropFilter);
    }

    public List<ItemEntry> generateTreasureLoot(Predicate<ItemEntry> dropFilter) {
        return this.treasureItems.getRandomEntries(dropFilter);
    }

    public List<ItemEntry> generateMobDropLoot(Predicate<ItemEntry> dropFilter, EntityType<?> mobType) {
        return ((ItemPool) this.mobDropItems.getOrDefault(mobType.getRegistryName().toString(), ItemPool.EMPTY)).getRandomEntries(dropFilter);
    }

    public SourceType getRequirementSource(ItemStack stack) {
        Item requiredItem = stack.getItem();
        for (WeightedList.Entry<ItemEntry> entry : (Iterable<WeightedList.Entry<ItemEntry>>) this.chestItems.pool) {
            if (requiredItem.equals(((ItemEntry) entry.value).getItem())) {
                return SourceType.CHEST;
            }
        }
        for (WeightedList.Entry<ItemEntry> entry : (Iterable<WeightedList.Entry<ItemEntry>>) this.treasureItems.pool) {
            if (requiredItem.equals(((ItemEntry) entry.value).getItem())) {
                return SourceType.TREASURE;
            }
        }
        return SourceType.MOB;
    }


    public String getName() {
        return "scavenger_hunt";
    }


    protected void reset() {
        this.requiredItems = new ItemPool(10, 15);

        this.chestItems = new ItemPool(1, 3);
        this.treasureItems = new ItemPool(1, 3);
        this.mobDropItems.clear();

        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_CREEPER_EYE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_CREEPER_FOOT);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_CREEPER_FUSE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_CREEPER_TNT);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_CREEPER_VIAL);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_CREEPER_CHARM);

        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_DROWNED_BARNACLE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_DROWNED_EYE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_DROWNED_HIDE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_DROWNED_VIAL);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_DROWNED_CHARM);

        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SKELETON_SHARD);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SKELETON_EYE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SKELETON_RIBCAGE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SKELETON_SKULL);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SKELETON_WISHBONE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SKELETON_VIAL);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SKELETON_CHARM);

        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SPIDER_FANGS);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SPIDER_LEG);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SPIDER_WEBBING);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SPIDER_CURSED_CHARM);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SPIDER_VIAL);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SPIDER_CHARM);

        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_ZOMBIE_BRAIN);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_ZOMBIE_ARM);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_ZOMBIE_EAR);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_ZOMBIE_EYE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_ZOMBIE_HIDE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_ZOMBIE_NOSE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_ZOMBIE_VIAL);

        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_TREASURE_BANGLE_BLUE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_TREASURE_BANGLE_PINK);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_TREASURE_BANGLE_GREEN);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_TREASURE_EARRINGS);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_TREASURE_GOBLET);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_TREASURE_SACK);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_TREASURE_SCROLL_RED);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_TREASURE_SCROLL_BLUE);

        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SCRAP_BROKEN_POTTERY);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SCRAP_CRACKED_PEARL);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SCRAP_CRACKED_SCRIPT);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SCRAP_EMPTY_JAR);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SCRAP_OLD_BOOK);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SCRAP_POTTERY_SHARD);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SCRAP_POULTICE_JAR);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SCRAP_PRESERVES_JAR);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SCRAP_RIPPED_PAGE);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SCRAP_SADDLE_BAG);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SCRAP_SPICE_JAR);
        addRequiredItem(this.requiredItems, (Item) ModItems.SCAVENGER_SCRAP_WIZARD_WAND);

        addDropItem(this.chestItems, (Item) ModItems.SCAVENGER_SCRAP_BROKEN_POTTERY);
        addDropItem(this.chestItems, (Item) ModItems.SCAVENGER_SCRAP_CRACKED_PEARL);
        addDropItem(this.chestItems, (Item) ModItems.SCAVENGER_SCRAP_CRACKED_SCRIPT);
        addDropItem(this.chestItems, (Item) ModItems.SCAVENGER_SCRAP_EMPTY_JAR);
        addDropItem(this.chestItems, (Item) ModItems.SCAVENGER_SCRAP_OLD_BOOK);
        addDropItem(this.chestItems, (Item) ModItems.SCAVENGER_SCRAP_POTTERY_SHARD);
        addDropItem(this.chestItems, (Item) ModItems.SCAVENGER_SCRAP_POULTICE_JAR);
        addDropItem(this.chestItems, (Item) ModItems.SCAVENGER_SCRAP_PRESERVES_JAR);
        addDropItem(this.chestItems, (Item) ModItems.SCAVENGER_SCRAP_RIPPED_PAGE);
        addDropItem(this.chestItems, (Item) ModItems.SCAVENGER_SCRAP_SADDLE_BAG);
        addDropItem(this.chestItems, (Item) ModItems.SCAVENGER_SCRAP_SPICE_JAR);
        addDropItem(this.chestItems, (Item) ModItems.SCAVENGER_SCRAP_WIZARD_WAND);

        addDropItem(this.treasureItems, (Item) ModItems.SCAVENGER_TREASURE_BANGLE_BLUE);
        addDropItem(this.treasureItems, (Item) ModItems.SCAVENGER_TREASURE_BANGLE_PINK);
        addDropItem(this.treasureItems, (Item) ModItems.SCAVENGER_TREASURE_BANGLE_GREEN);
        addDropItem(this.treasureItems, (Item) ModItems.SCAVENGER_TREASURE_EARRINGS);
        addDropItem(this.treasureItems, (Item) ModItems.SCAVENGER_TREASURE_GOBLET);
        addDropItem(this.treasureItems, (Item) ModItems.SCAVENGER_TREASURE_SACK);
        addDropItem(this.treasureItems, (Item) ModItems.SCAVENGER_TREASURE_SCROLL_RED);
        addDropItem(this.treasureItems, (Item) ModItems.SCAVENGER_TREASURE_SCROLL_BLUE);


        ItemPool creeperPool = new ItemPool(1, 3);
        addDropItem(creeperPool, (Item) ModItems.SCAVENGER_CREEPER_EYE);
        addDropItem(creeperPool, (Item) ModItems.SCAVENGER_CREEPER_FOOT);
        addDropItem(creeperPool, (Item) ModItems.SCAVENGER_CREEPER_FUSE);
        addDropItem(creeperPool, (Item) ModItems.SCAVENGER_CREEPER_TNT);
        addDropItem(creeperPool, (Item) ModItems.SCAVENGER_CREEPER_VIAL);
        addDropItem(creeperPool, (Item) ModItems.SCAVENGER_CREEPER_CHARM);
        this.mobDropItems.put(EntityType.CREEPER.getRegistryName().toString(), creeperPool);

        ItemPool zombiePool = new ItemPool(1, 3);
        addDropItem(zombiePool, (Item) ModItems.SCAVENGER_ZOMBIE_BRAIN);
        addDropItem(zombiePool, (Item) ModItems.SCAVENGER_ZOMBIE_ARM);
        addDropItem(zombiePool, (Item) ModItems.SCAVENGER_ZOMBIE_EAR);
        addDropItem(zombiePool, (Item) ModItems.SCAVENGER_ZOMBIE_EYE);
        addDropItem(zombiePool, (Item) ModItems.SCAVENGER_ZOMBIE_HIDE);
        addDropItem(zombiePool, (Item) ModItems.SCAVENGER_ZOMBIE_NOSE);
        addDropItem(zombiePool, (Item) ModItems.SCAVENGER_ZOMBIE_VIAL);
        this.mobDropItems.put(EntityType.ZOMBIE.getRegistryName().toString(), zombiePool);
        this.mobDropItems.put(EntityType.HUSK.getRegistryName().toString(), zombiePool);

        ItemPool drownedPool = new ItemPool(1, 3);
        drownedPool.pool.addAll((Collection) zombiePool.pool.copy());
        addDropItem(drownedPool, (Item) ModItems.SCAVENGER_DROWNED_BARNACLE);
        addDropItem(drownedPool, (Item) ModItems.SCAVENGER_DROWNED_EYE);
        addDropItem(drownedPool, (Item) ModItems.SCAVENGER_DROWNED_HIDE);
        addDropItem(drownedPool, (Item) ModItems.SCAVENGER_DROWNED_VIAL);
        addDropItem(drownedPool, (Item) ModItems.SCAVENGER_DROWNED_CHARM);
        this.mobDropItems.put(EntityType.DROWNED.getRegistryName().toString(), drownedPool);

        ItemPool spiderPool = new ItemPool(1, 3);
        addDropItem(spiderPool, (Item) ModItems.SCAVENGER_SPIDER_FANGS);
        addDropItem(spiderPool, (Item) ModItems.SCAVENGER_SPIDER_LEG);
        addDropItem(spiderPool, (Item) ModItems.SCAVENGER_SPIDER_WEBBING);
        addDropItem(spiderPool, (Item) ModItems.SCAVENGER_SPIDER_CURSED_CHARM);
        addDropItem(spiderPool, (Item) ModItems.SCAVENGER_SPIDER_VIAL);
        addDropItem(spiderPool, (Item) ModItems.SCAVENGER_SPIDER_CHARM);
        this.mobDropItems.put(EntityType.SPIDER.getRegistryName().toString(), spiderPool);
        this.mobDropItems.put(EntityType.CAVE_SPIDER.getRegistryName().toString(), spiderPool);

        ItemPool skeletonPool = new ItemPool(1, 3);
        addDropItem(skeletonPool, (Item) ModItems.SCAVENGER_SKELETON_SHARD);
        addDropItem(skeletonPool, (Item) ModItems.SCAVENGER_SKELETON_EYE);
        addDropItem(skeletonPool, (Item) ModItems.SCAVENGER_SKELETON_RIBCAGE);
        addDropItem(skeletonPool, (Item) ModItems.SCAVENGER_SKELETON_SKULL);
        addDropItem(skeletonPool, (Item) ModItems.SCAVENGER_SKELETON_WISHBONE);
        addDropItem(skeletonPool, (Item) ModItems.SCAVENGER_SKELETON_VIAL);
        addDropItem(skeletonPool, (Item) ModItems.SCAVENGER_SKELETON_CHARM);
        this.mobDropItems.put(EntityType.SKELETON.getRegistryName().toString(), skeletonPool);
        this.mobDropItems.put(EntityType.WITHER_SKELETON.getRegistryName().toString(), skeletonPool);
    }

    private void addRequiredItem(ItemPool out, Item i) {
        out.pool.add(new ItemEntry(i, 10, 15), 1);
    }

    private void addDropItem(ItemPool out, Item i) {
        out.pool.add(new ItemEntry(i, 1, 1), 1);
    }

    public static class ItemPool {
        private static final ItemPool EMPTY = new ItemPool(0, 0);
        @Expose
        private final WeightedList<ScavengerHuntConfig.ItemEntry> pool = new WeightedList();
        @Expose
        private final int min;
        @Expose
        private final int max;

        public ItemPool(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public ScavengerHuntConfig.ItemEntry getRandomEntry(Predicate<ScavengerHuntConfig.ItemEntry> dropFilter) {
            return (ScavengerHuntConfig.ItemEntry) this.pool.copyFiltered(dropFilter).getRandom(Config.rand);
        }

        public int getRandomAmount() {
            return MathUtilities.getRandomInt(this.min, this.max + 1);
        }

        public List<ScavengerHuntConfig.ItemEntry> getRandomEntries(Predicate<ScavengerHuntConfig.ItemEntry> dropFilter) {
            return (List<ScavengerHuntConfig.ItemEntry>) IntStream.range(0, getRandomAmount())
                    .mapToObj(index -> getRandomEntry(dropFilter))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    public static class ItemEntry {
        @Expose
        private final String item;
        @Expose
        private final int min;
        @Expose
        private final int max;

        public ItemEntry(Item item, int min, int max) {
            this(item.getRegistryName().toString(), min, max);
        }

        public ItemEntry(String item, int min, int max) {
            this.item = item;
            this.min = min;
            this.max = max;
        }

        public Item getItem() {
            return (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.item));
        }

        public int getRandomAmount() {
            return MathUtilities.getRandomInt(this.min, this.max + 1);
        }

        public ItemStack createItemStack() {
            return new ItemStack((IItemProvider) getItem(), getRandomAmount());
        }
    }


    public enum SourceType {
        MOB( Vault.id("textures/gui/overlay/scav_mob.png"), TextFormatting.RED),
        CHEST( Vault.id("textures/gui/overlay/scav_chest.png"), TextFormatting.GREEN),
        TREASURE( Vault.id("textures/gui/overlay/scav_treasure.png"), TextFormatting.GOLD);

        private final ResourceLocation iconPath;
        private final TextFormatting requirementColor;

        SourceType(ResourceLocation iconPath, TextFormatting requirementColor) {
            this.iconPath = iconPath;
            this.requirementColor = requirementColor;
        }

        public ResourceLocation getIconPath() {
            return this.iconPath;
        }

        public TextFormatting getRequirementColor() {
            return this.requirementColor;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\ScavengerHuntConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */