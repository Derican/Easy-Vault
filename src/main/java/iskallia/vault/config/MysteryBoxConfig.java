package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;

public class MysteryBoxConfig extends Config {
    @Expose
    public WeightedList<ProductEntry> POOL;

    public MysteryBoxConfig() {
        this.POOL = new WeightedList<ProductEntry>();
    }

    @Override
    public String getName() {
        return "mystery_box";
    }

    @Override
    protected void reset() {
        this.POOL.add(new ProductEntry(Items.ENCHANTED_BOOK, 1, 1, "{StoredEnchantments:[{id:\\\"mending\\\",lvl:1s}]}"), 4);
        this.POOL.add(new ProductEntry(Items.ENCHANTED_BOOK, 1, 1, "{StoredEnchantments:[{id:\\\"sharpness\\\",lvl:5s}]}"), 4);
        this.POOL.add(new ProductEntry(Items.ENCHANTED_BOOK, 1, 1, "{StoredEnchantments:[{id:\\\"protection\\\",lvl:4s}]}"), 4);
        this.POOL.add(new ProductEntry(Items.ENCHANTED_BOOK, 1, 1, "{StoredEnchantments:[{id:\\\"efficiency\\\",lvl:5s}]}"), 4);
        this.POOL.add(new ProductEntry(Items.ENCHANTED_BOOK, 1, 1, "{StoredEnchantments:[{id:\\\"unbreaking\\\",lvl:3s}]}"), 4);
        this.POOL.add(new ProductEntry(Items.ENCHANTED_BOOK, 1, 1, "{StoredEnchantments:[{id:\\\"fortune\\\",lvl:3s}]}"), 2);
        this.POOL.add(new ProductEntry(Items.ENCHANTED_BOOK, 1, 1, "{StoredEnchantments:[{id:\\\"silk_touch\\\",lvl:1s}]}"), 4);
        this.POOL.add(new ProductEntry(Items.ENCHANTED_BOOK, 1, 1, "{StoredEnchantments:[{id:\\\"feather_falling\\\",lvl:4s}]}"), 4);
        this.POOL.add(new ProductEntry(Items.ENCHANTED_BOOK, 1, 1, "{StoredEnchantments:[{id:\\\"depth_strider\\\",lvl:3s}]}"), 4);
        this.POOL.add(new ProductEntry(Items.ENCHANTED_BOOK, 1, 1, "{StoredEnchantments:[{id:\\\"sweeping\\\",lvl:3s}]}"), 4);
        this.POOL.add(new ProductEntry(Items.GOLDEN_CARROT, 6, 16, (CompoundNBT) null), 24);
        this.POOL.add(new ProductEntry(Items.OBSIDIAN, 8, 16, (CompoundNBT) null), 12);
        this.POOL.add(new ProductEntry(Items.OAK_LOG, 16, 32, (CompoundNBT) null), 36);
        this.POOL.add(new ProductEntry(Items.SAND, 48, 64, (CompoundNBT) null), 36);
        this.POOL.add(new ProductEntry(Items.GRAVEL, 48, 64, (CompoundNBT) null), 36);
        this.POOL.add(new ProductEntry(Items.WHITE_WOOL, 8, 16, (CompoundNBT) null), 24);
        this.POOL.add(new ProductEntry(Items.STRING, 16, 34, (CompoundNBT) null), 24);
        this.POOL.add(new ProductEntry(Items.ENDER_PEARL, 4, 8, (CompoundNBT) null), 8);
        this.POOL.add(new ProductEntry(Items.EXPERIENCE_BOTTLE, 24, 36, (CompoundNBT) null), 6);
        this.POOL.add(new ProductEntry(Items.BONE, 8, 64, (CompoundNBT) null), 24);
        this.POOL.add(new ProductEntry(Items.REDSTONE, 24, 32, (CompoundNBT) null), 16);
        this.POOL.add(new ProductEntry(Items.IRON_ORE, 12, 18, (CompoundNBT) null), 32);
        this.POOL.add(new ProductEntry(Items.COAL, 24, 32, (CompoundNBT) null), 24);
        this.POOL.add(new ProductEntry(Items.LAPIS_LAZULI, 12, 24, (CompoundNBT) null), 12);
        this.POOL.add(new ProductEntry(Items.DIAMOND, 4, 12, (CompoundNBT) null), 12);
        this.POOL.add(new ProductEntry(Items.EMERALD, 16, 32, (CompoundNBT) null), 32);
        this.POOL.add(new ProductEntry(Items.GOLD_ORE, 12, 20, (CompoundNBT) null), 16);
        this.POOL.add(new ProductEntry(Items.GLASS, 32, 64, (CompoundNBT) null), 24);
        this.POOL.add(new ProductEntry(Items.GOLDEN_APPLE, 1, 1, (CompoundNBT) null), 6);
        this.POOL.add(new ProductEntry(ModItems.JADE_APPLE, 1, 1, (CompoundNBT) null), 4);
        this.POOL.add(new ProductEntry(ModItems.PIXIE_APPLE, 1, 1, (CompoundNBT) null), 4);
        this.POOL.add(new ProductEntry(ModItems.COBALT_APPLE, 1, 1, (CompoundNBT) null), 4);
        this.POOL.add(new ProductEntry(ModItems.CANDY_BAR, 1, 1, (CompoundNBT) null), 4);
        this.POOL.add(new ProductEntry(ModItems.POWER_BAR, 1, 1, (CompoundNBT) null), 4);
        this.POOL.add(new ProductEntry(Items.DIAMOND_HELMET, 1, 1, "{Enchantments:[{id:\\\"protection\\\",lvl:4s},{id:\\\"unbreaking\\\",lvl:3s}]}"), 1);
        this.POOL.add(new ProductEntry(Items.DIAMOND_BOOTS, 1, 1, "{Enchantments:[{id:\\\"protection\\\",lvl:4s},{id:\\\"unbreaking\\\",lvl:3s},{id:\\\"depth_strider\\\",lvl:3s},{id:\\\"feather_falling\\\",lvl:4s}]}"), 1);
        this.POOL.add(new ProductEntry(Items.DIAMOND_LEGGINGS, 1, 1, "{Enchantments:[{id:\\\"protection\\\",lvl:4s},{id:\\\"unbreaking\\\",lvl:3s}]}"), 1);
        this.POOL.add(new ProductEntry(Items.DIAMOND_CHESTPLATE, 1, 1, "{Enchantments:[{id:\\\"protection\\\",lvl:4s},{id:\\\"unbreaking\\\",lvl:3s}]}"), 1);
        this.POOL.add(new ProductEntry(Items.DIAMOND_SWORD, 1, 1, "{Enchantments:[{id:\\\"sharpness\\\",lvl:5s},{id:\\\"unbreaking\\\",lvl:3s}]}"), 1);
        this.POOL.add(new ProductEntry(Items.DIAMOND_PICKAXE, 1, 1, "{Enchantments:[{id:\\\"efficiency\\\",lvl:5s},{id:\\\"unbreaking\\\",lvl:3s}]}"), 1);
        this.POOL.add(new ProductEntry(Items.DIAMOND_AXE, 1, 1, "{Enchantments:[{id:\\\"efficiency\\\",lvl:5s},{id:\\\"unbreaking\\\",lvl:3s}]}"), 1);
        this.POOL.add(new ProductEntry(Items.DIAMOND_SHOVEL, 1, 1, "{Enchantments:[{id:\\\"efficiency\\\",lvl:5s},{id:\\\"unbreaking\\\",lvl:3s}]}"), 1);
        this.POOL.add(new ProductEntry(Items.ANCIENT_DEBRIS, 1, 1, (CompoundNBT) null), 2);
        this.POOL.add(new ProductEntry(ModItems.STAR_ESSENCE, 1, 3, (CompoundNBT) null), 3);
        this.POOL.add(new ProductEntry(ModItems.BURGER_BUN, 1, 1, (CompoundNBT) null), 12);
        this.POOL.add(new ProductEntry(ModItems.BURGER_PATTY, 1, 1, (CompoundNBT) null), 12);
        this.POOL.add(new ProductEntry(ModItems.MYSTIC_TOMATO, 1, 1, (CompoundNBT) null), 12);
        this.POOL.add(new ProductEntry(ModItems.MATURE_CHEDDAR, 1, 1, (CompoundNBT) null), 12);
        this.POOL.add(new ProductEntry(ModItems.VAULT_BRONZE, 2, 4, (CompoundNBT) null), 3);
        this.POOL.add(new ProductEntry(ModItems.SKILL_ESSENCE, 1, 2, (CompoundNBT) null), 1);
        this.POOL.add(new ProductEntry(Items.ELYTRA, 1, 1, (CompoundNBT) null), 1);
    }
}
