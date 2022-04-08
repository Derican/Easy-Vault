package iskallia.vault.init;

import com.google.common.collect.ImmutableList;
import iskallia.vault.Vault;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.item.*;
import iskallia.vault.item.consumable.VaultConsumableItem;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.item.gear.*;
import iskallia.vault.item.gear.applicable.VaultPlateItem;
import iskallia.vault.item.gear.applicable.VaultRepairCoreItem;
import iskallia.vault.item.paxel.VaultPaxelItem;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Random;
import java.util.function.Supplier;

public class ModItems {
    public static ItemGroup VAULT_MOD_GROUP = new ItemGroup("the_vault") {
        public ItemStack makeIcon() {
            return new ItemStack((IItemProvider) ModItems.VAULT_BURGER);
        }


        public boolean hasSearchBar() {
            return true;
        }
    };

    public static ItemGroup SCAVENGER_GROUP = new ItemGroup("the_vault.scavenger") {
        public ItemStack makeIcon() {
            return new ItemStack((IItemProvider) ModBlocks.SCAVENGER_CHEST);
        }


        public boolean hasSearchBar() {
            return true;
        }
    };

    public static VaultXPFoodItem VAULT_BURGER = (VaultXPFoodItem) new VaultXPFoodItem.Percent(Vault.id("vault_burger"), () -> Float.valueOf(ModConfigs.VAULT_ITEMS.VAULT_BURGER.minExpPercent), () -> Float.valueOf(ModConfigs.VAULT_ITEMS.VAULT_BURGER.maxExpPercent), (new Item.Properties())

            .tab(VAULT_MOD_GROUP));
    public static VaultXPFoodItem OOZING_PIZZA = (VaultXPFoodItem) new VaultXPFoodItem.Percent(Vault.id("oozing_pizza"), () -> Float.valueOf(ModConfigs.VAULT_ITEMS.VAULT_PIZZA.minExpPercent), () -> Float.valueOf(ModConfigs.VAULT_ITEMS.VAULT_PIZZA.maxExpPercent), (new Item.Properties())

            .tab(VAULT_MOD_GROUP));
    public static VaultXPFoodItem VAULT_COOKIE = (VaultXPFoodItem) new VaultXPFoodItem.Flat(Vault.id("vault_cookie"), () -> Integer.valueOf(ModConfigs.VAULT_ITEMS.VAULT_COOKIE.minExp), () -> Integer.valueOf(ModConfigs.VAULT_ITEMS.VAULT_COOKIE.maxExp), (new Item.Properties())

            .tab(VAULT_MOD_GROUP), 25);
    public static ItemSkillOrb SKILL_ORB = new ItemSkillOrb(VAULT_MOD_GROUP);
    public static ItemVaultGem VAULT_ROCK = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("vault_rock"));
    public static ItemVaultGem ALEXANDRITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_alexandrite"));
    public static ItemVaultGem BENITOITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_benitoite"));
    public static ItemVaultGem LARIMAR_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_larimar"));
    public static ItemVaultGem BLACK_OPAL_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_black_opal"));
    public static ItemVaultGem PAINITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_painite"));
    public static ItemVaultGem ISKALLIUM_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_iskallium"));
    public static ItemVaultGem GORGINITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_gorginite"));
    public static ItemVaultGem SPARKLETINE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_sparkletine"));
    public static ItemVaultGem WUTODIE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_wutodie"));
    public static ItemVaultGem ASHIUM_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_ashium"));
    public static ItemVaultGem BOMIGNITE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_bomignite"));
    public static ItemVaultGem FUNSOIDE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_funsoide"));
    public static ItemVaultGem TUBIUM_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_tubium"));
    public static ItemVaultGem UPALINE_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_upaline"));
    public static ItemVaultGem PUFFIUM_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_puffium"));
    public static ItemVaultGem ECHO_GEM = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_echo"));
    public static ItemVaultGem POG = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("gem_pog"));
    public static ItemVaultGem ECHO_POG = new ItemVaultGem(VAULT_MOD_GROUP, Vault.id("echo_pog"));
    public static VaultCrystalItem VAULT_CRYSTAL = new VaultCrystalItem(VAULT_MOD_GROUP, Vault.id("vault_crystal"));
    public static ItemVaultKey ISKALLIUM_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_iskallium"));
    public static ItemVaultKey GORGINITE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_gorginite"));
    public static ItemVaultKey SPARKLETINE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_sparkletine"));
    public static ItemVaultKey ASHIUM_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_ashium"));
    public static ItemVaultKey BOMIGNITE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_bomignite"));
    public static ItemVaultKey FUNSOIDE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_funsoide"));
    public static ItemVaultKey TUBIUM_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_tubium"));
    public static ItemVaultKey UPALINE_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_upaline"));
    public static ItemVaultKey PUFFIUM_KEY = new ItemVaultKey(VAULT_MOD_GROUP, Vault.id("key_puffium"));
    public static ItemRelicBoosterPack RELIC_BOOSTER_PACK = new ItemRelicBoosterPack(VAULT_MOD_GROUP, Vault.id("relic_booster_pack"));

    public static RelicPartItem DRAGON_HEAD_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_dragon_head"));
    public static RelicPartItem DRAGON_TAIL_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_dragon_tail"));
    public static RelicPartItem DRAGON_FOOT_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_dragon_foot"));
    public static RelicPartItem DRAGON_CHEST_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_dragon_chest"));
    public static RelicPartItem DRAGON_BREATH_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_dragon_breath"));
    public static RelicPartItem MINERS_DELIGHT_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_miners_delight"));
    public static RelicPartItem MINERS_LIGHT_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_miners_light"));
    public static RelicPartItem PICKAXE_HANDLE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_pickaxe_handle"));
    public static RelicPartItem PICKAXE_HEAD_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_pickaxe_head"));
    public static RelicPartItem PICKAXE_TOOL_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_pickaxe_tool"));
    public static RelicPartItem SWORD_BLADE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_sword_blade"));
    public static RelicPartItem SWORD_HANDLE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_sword_handle"));
    public static RelicPartItem SWORD_STICK_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_sword_stick"));
    public static RelicPartItem WARRIORS_ARMOUR_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_warriors_armour"));
    public static RelicPartItem WARRIORS_CHARM_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_warriors_charm"));
    public static RelicPartItem DIAMOND_ESSENCE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_diamond_essence"));
    public static RelicPartItem GOLD_ESSENCE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_gold_essence"));
    public static RelicPartItem MYSTIC_GEM_ESSENCE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_mystic_gem_essence"));
    public static RelicPartItem NETHERITE_ESSENCE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_netherite_essence"));
    public static RelicPartItem PLATINUM_ESSENCE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_platinum_essence"));
    public static RelicPartItem TWITCH_EMOTE1_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twitch_emote1"));
    public static RelicPartItem TWITCH_EMOTE2_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twitch_emote2"));
    public static RelicPartItem TWITCH_EMOTE3_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twitch_emote3"));
    public static RelicPartItem TWITCH_EMOTE4_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twitch_emote4"));
    public static RelicPartItem TWITCH_EMOTE5_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twitch_emote5"));
    public static RelicPartItem CUPCAKE_BLUE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_cupcake_blue"));
    public static RelicPartItem CUPCAKE_LIME_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_cupcake_lime"));
    public static RelicPartItem CUPCAKE_PINK_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_cupcake_pink"));
    public static RelicPartItem CUPCAKE_PURPLE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_cupcake_purple"));
    public static RelicPartItem CUPCAKE_RED_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_cupcake_red"));
    public static RelicPartItem AIR_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_air"));
    public static RelicPartItem SPIRIT_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_spirit"));
    public static RelicPartItem FIRE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_fire"));
    public static RelicPartItem EARTH_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_earth"));
    public static RelicPartItem WATER_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_water"));

    public static ItemVaultRune VAULT_RUNE = new ItemVaultRune(VAULT_MOD_GROUP, Vault.id("vault_rune"));
    public static ItemTraderCore TRADER_CORE = new ItemTraderCore(VAULT_MOD_GROUP, Vault.id("trader_core"));
    public static ItemUnidentifiedArtifact UNIDENTIFIED_ARTIFACT = new ItemUnidentifiedArtifact(VAULT_MOD_GROUP, Vault.id("unidentified_artifact"));
    public static ItemLegendaryTreasure LEGENDARY_TREASURE_NORMAL = new ItemLegendaryTreasure(VAULT_MOD_GROUP, Vault.id("legendary_treasure_normal"), VaultRarity.COMMON);
    public static ItemLegendaryTreasure LEGENDARY_TREASURE_RARE = new ItemLegendaryTreasure(VAULT_MOD_GROUP, Vault.id("legendary_treasure_rare"), VaultRarity.RARE);
    public static ItemLegendaryTreasure LEGENDARY_TREASURE_EPIC = new ItemLegendaryTreasure(VAULT_MOD_GROUP, Vault.id("legendary_treasure_epic"), VaultRarity.EPIC);
    public static ItemLegendaryTreasure LEGENDARY_TREASURE_OMEGA = new ItemLegendaryTreasure(VAULT_MOD_GROUP, Vault.id("legendary_treasure_omega"), VaultRarity.OMEGA);
    public static RelicItem VAULT_RELIC = new RelicItem(VAULT_MOD_GROUP, Vault.id("vault_relic"));
    public static ItemSkillOrbFrame SKILL_ORB_FRAME = new ItemSkillOrbFrame(VAULT_MOD_GROUP, Vault.id("orb_frame"));
    public static ItemSkillShard SKILL_SHARD = new ItemSkillShard(VAULT_MOD_GROUP, Vault.id("skill_shard"));
    public static ItemVaultFruit.BitterLemon BITTER_LEMON = new ItemVaultFruit.BitterLemon(VAULT_MOD_GROUP, Vault.id("bitter_lemon"), 600);
    public static ItemVaultFruit.SourOrange SOUR_ORANGE = new ItemVaultFruit.SourOrange(VAULT_MOD_GROUP, Vault.id("sour_orange"), 1200);
    public static ItemVaultFruit.MysticPear MYSTIC_PEAR = new ItemVaultFruit.MysticPear(VAULT_MOD_GROUP, Vault.id("mystic_pear"), 6000);
    public static BasicItem KEY_PIECE = new BasicItem(Vault.id("key_piece"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem KEY_MOULD = new BasicItem(Vault.id("key_mould"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem BLANK_KEY = new BasicItem(Vault.id("blank_key"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem NETHERITE_CLUSTER = new BasicItem(Vault.id("cluster_netherite"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem ISKALLIUM_CLUSTER = new BasicItem(Vault.id("cluster_iskallium"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem GORGINITE_CLUSTER = new BasicItem(Vault.id("cluster_gorginite"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem SPARKLETINE_CLUSTER = new BasicItem(Vault.id("cluster_sparkletine"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem ASHIUM_CLUSTER = new BasicItem(Vault.id("cluster_ashium"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem BOMIGNITE_CLUSTER = new BasicItem(Vault.id("cluster_bomignite"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem FUNSOIDE_CLUSTER = new BasicItem(Vault.id("cluster_funsoide"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem TUBIUM_CLUSTER = new BasicItem(Vault.id("cluster_tubium"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem UPALINE_CLUSTER = new BasicItem(Vault.id("cluster_upaline"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem PUFFIUM_CLUSTER = new BasicItem(Vault.id("cluster_puffium"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static RelicPartItem TWOLF999_HEAD_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twolf999_head"));
    public static RelicPartItem TWOLF999_COMBAT_VEST_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twolf999_combat_vest"));
    public static RelicPartItem TWOLF999_COMBAT_LEGGINGS_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twolf999_combat_leggings"));
    public static RelicPartItem TWOLF999_COMBAT_GLOVES_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twolf999_combat_gloves"));
    public static RelicPartItem TWOLF999_COMBAT_BOOTS_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_twolf999_combat_boots"));
    public static RelicPartItem ARMOR_OF_FORBEARANCE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_armor_of_forbearance"));
    public static RelicPartItem HEART_OF_THE_VOID_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_heart_of_the_void"));
    public static RelicPartItem NEMESIS_THWARTER_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_nemesis_thwarter"));
    public static RelicPartItem REVERENCE_EDGE_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_reverence_edge"));
    public static RelicPartItem WINGS_OF_EQUITY_RELIC = new RelicPartItem(VAULT_MOD_GROUP, Vault.id("relic_wings_of_equity"));

    public static VaultStewItem VAULT_STEW_MYSTERY = new VaultStewItem(Vault.id("vault_stew_mystery"), VaultStewItem.Rarity.MYSTERY, (new Item.Properties()).food(VaultStewItem.FOOD).stacksTo(1).tab(VAULT_MOD_GROUP));
    public static VaultStewItem VAULT_STEW_NORMAL = new VaultStewItem(Vault.id("vault_stew_normal"), VaultStewItem.Rarity.NORMAL, (new Item.Properties()).food(VaultStewItem.FOOD).stacksTo(1).tab(VAULT_MOD_GROUP));
    public static VaultStewItem VAULT_STEW_RARE = new VaultStewItem(Vault.id("vault_stew_rare"), VaultStewItem.Rarity.RARE, (new Item.Properties()).food(VaultStewItem.FOOD).stacksTo(1).tab(VAULT_MOD_GROUP));
    public static VaultStewItem VAULT_STEW_EPIC = new VaultStewItem(Vault.id("vault_stew_epic"), VaultStewItem.Rarity.EPIC, (new Item.Properties()).food(VaultStewItem.FOOD).stacksTo(1).tab(VAULT_MOD_GROUP));
    public static VaultStewItem VAULT_STEW_OMEGA = new VaultStewItem(Vault.id("vault_stew_omega"), VaultStewItem.Rarity.OMEGA, (new Item.Properties()).food(VaultStewItem.FOOD).stacksTo(1).tab(VAULT_MOD_GROUP));
    public static BasicItem POISONOUS_MUSHROOM = new BasicItem(Vault.id("poisonous_mushroom"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_GOLD = new BasicItem(Vault.id("vault_gold"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_DIAMOND = new BasicItem(Vault.id("vault_diamond"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem SKILL_ESSENCE = new BasicItem(Vault.id("skill_essence"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static LootableItem UNIDENTIFIED_RELIC = new LootableItem(Vault.id("unidentified_relic"), (new Item.Properties()).tab(VAULT_MOD_GROUP), () -> new ItemStack((IItemProvider) ModConfigs.VAULT_RELICS.getRandomPart()));
    public static ItemVaultFruit.SweetKiwi SWEET_KIWI = new ItemVaultFruit.SweetKiwi(VAULT_MOD_GROUP, Vault.id("sweet_kiwi"), 100);
    public static BasicItem HUNTER_EYE = new BasicItem(Vault.id("hunter_eye"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem BURGER_PATTY = new BasicItem(Vault.id("burger_patty"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem BURGER_BUN = new BasicItem(Vault.id("burger_bun"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem MATURE_CHEDDAR = new BasicItem(Vault.id("mature_cheddar"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem MYSTIC_TOMATO = new BasicItem(Vault.id("mystic_tomato"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_SCRAP = (BasicItem) new BasicTooltipItem(Vault.id("vault_scrap"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("The scrap of smelted down vault gear."))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("Can be used to craft a repair core."))
            .withStyle(TextFormatting.GRAY)});
    public static BasicItem VAULT_INGOT = new BasicItem(Vault.id("vault_ingot"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_PLATINUM = new BasicItem(Vault.id("vault_platinum"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static LootableItem MYSTERY_BOX = new LootableItem(Vault.id("mystery_box"), (new Item.Properties()).tab(VAULT_MOD_GROUP), () -> ((ProductEntry) ModConfigs.MYSTERY_BOX.POOL.getRandom(new Random())).generateItemStack());
    public static BasicItem DRILL_ARROW_PART = new BasicItem(Vault.id("drill_arrow_part"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(8));
    public static ItemDrillArrow DRILL_ARROW = new ItemDrillArrow(VAULT_MOD_GROUP, Vault.id("drill_arrow"));
    public static ItemRespecFlask RESPEC_FLASK = new ItemRespecFlask(VAULT_MOD_GROUP, Vault.id("respec_flask"));
    public static ItemResetFlask RESET_FLASK = new ItemResetFlask(VAULT_MOD_GROUP, Vault.id("reset_flask"));
    public static LootableItem MYSTERY_EGG = new LootableItem(Vault.id("mystery_egg"), (new Item.Properties()).tab(VAULT_MOD_GROUP), () -> ((ProductEntry) ModConfigs.MYSTERY_EGG.POOL.getRandom(new Random())).generateItemStack());
    public static LootableItem MYSTERY_HOSTILE_EGG = new LootableItem(Vault.id("mystery_hostile_egg"), (new Item.Properties()).tab(VAULT_MOD_GROUP), () -> ((ProductEntry) ModConfigs.MYSTERY_HOSTILE_EGG.POOL.getRandom(new Random())).generateItemStack());

    public static BasicItem ACCELERATION_CHIP = (BasicItem) new BasicTooltipItem(Vault.id("acceleration_chip"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Right click to install in an omega statue to accelerate"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("the speed of production, each chip lowers the wait time by 5 seconds"))
            .withStyle(TextFormatting.GRAY)});
    public static LootableItem PANDORAS_BOX = new LootableItem(Vault.id("pandoras_box"), (new Item.Properties()).tab(VAULT_MOD_GROUP), () -> ((ProductEntry) ModConfigs.PANDORAS_BOX.POOL.getRandom(new Random())).generateItemStack());

    public static BasicItem ISKALLIUM_CHUNK = new BasicItem(Vault.id("chunk_iskallium"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem GORGINITE_CHUNK = new BasicItem(Vault.id("chunk_gorginite"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem SPARKLETINE_CHUNK = new BasicItem(Vault.id("chunk_sparkletine"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem ASHIUM_CHUNK = new BasicItem(Vault.id("chunk_ashium"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem BOMIGNITE_CHUNK = new BasicItem(Vault.id("chunk_bomignite"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem FUNSOIDE_CHUNK = new BasicItem(Vault.id("chunk_funsoide"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem TUBIUM_CHUNK = new BasicItem(Vault.id("chunk_tubium"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem UPALINE_CHUNK = new BasicItem(Vault.id("chunk_upaline"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem PUFFIUM_CHUNK = new BasicItem(Vault.id("chunk_puffium"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem OMEGA_POG = new BasicItem(Vault.id("omega_pog"), (new Item.Properties()).tab(VAULT_MOD_GROUP));

    public static BasicItem ETERNAL_SOUL = new BasicItem(Vault.id("eternal_soul"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem SPARK = new BasicItem(Vault.id("spark"), (new Item.Properties()).tab(VAULT_MOD_GROUP));

    public static BasicItem STAR_SHARD = new BasicItem(Vault.id("star_shard"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem STAR_CORE = new BasicItem(Vault.id("star_core"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem STAR_ESSENCE = new BasicItem(Vault.id("star_essence"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static ItemKnowledgeStar KNOWLEDGE_STAR = new ItemKnowledgeStar(VAULT_MOD_GROUP);

    public static VaultSwordItem SWORD = new VaultSwordItem(Vault.id("sword"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static VaultAxeItem AXE = new VaultAxeItem(Vault.id("axe"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static VaultArmorItem HELMET = new VaultArmorItem(Vault.id("helmet"), EquipmentSlotType.HEAD, (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static VaultArmorItem CHESTPLATE = new VaultArmorItem(Vault.id("chestplate"), EquipmentSlotType.CHEST, (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static VaultArmorItem LEGGINGS = new VaultArmorItem(Vault.id("leggings"), EquipmentSlotType.LEGS, (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static VaultArmorItem BOOTS = new VaultArmorItem(Vault.id("boots"), EquipmentSlotType.FEET, (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static EtchingItem ETCHING = new EtchingItem(Vault.id("etching"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static BasicItem ETCHING_FRAGMENT = new BasicItem(Vault.id("etching_fragment"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static IdolItem IDOL_BENEVOLENT = new IdolItem(Vault.id("idol_benevolent"), PlayerFavourData.VaultGodType.BENEVOLENT, (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static IdolItem IDOL_OMNISCIENT = new IdolItem(Vault.id("idol_omniscient"), PlayerFavourData.VaultGodType.OMNISCIENT, (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static IdolItem IDOL_TIMEKEEPER = new IdolItem(Vault.id("idol_timekeeper"), PlayerFavourData.VaultGodType.TIMEKEEPER, (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static IdolItem IDOL_MALEVOLENCE = new IdolItem(Vault.id("idol_malevolence"), PlayerFavourData.VaultGodType.MALEVOLENCE, (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));

    public static PuzzleRuneItem PUZZLE_RUNE = new PuzzleRuneItem(Vault.id("puzzle_rune"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));

    public static BasicItem INFUSED_ETERNAL_SOUL = new BasicItem(Vault.id("infused_eternal_soul"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static Item UNKNOWN_EGG = (Item) new UnknownEggItem(Vault.id("unknown_egg"), (new Item.Properties()).tab(VAULT_MOD_GROUP));

    public static VaultConsumableItem CANDY_BAR = new VaultConsumableItem(Vault.id("candy_bar"));
    public static VaultConsumableItem POWER_BAR = new VaultConsumableItem(Vault.id("power_bar"));
    public static VaultConsumableItem JADE_APPLE = new VaultConsumableItem(Vault.id("jade_apple"));
    public static VaultConsumableItem COBALT_APPLE = new VaultConsumableItem(Vault.id("cobalt_apple"));
    public static VaultConsumableItem PIXIE_APPLE = new VaultConsumableItem(Vault.id("pixie_apple"));
    public static BasicItem VAULT_APPLE = new BasicItem(Vault.id("vault_apple"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static VaultConsumableItem LUCKY_APPLE = new VaultConsumableItem(Vault.id("lucky_apple"));
    public static VaultConsumableItem TREASURE_APPLE = new VaultConsumableItem(Vault.id("treasure_apple"));
    public static VaultConsumableItem POWER_APPLE = new VaultConsumableItem(Vault.id("power_apple"));
    public static VaultConsumableItem GHOST_APPLE = new VaultConsumableItem(Vault.id("ghost_apple"));
    public static VaultConsumableItem GOLEM_APPLE = new VaultConsumableItem(Vault.id("golem_apple"));
    public static VaultConsumableItem SWEET_APPLE = new VaultConsumableItem(Vault.id("sweet_apple"));
    public static VaultConsumableItem HEARTY_APPLE = new VaultConsumableItem(Vault.id("hearty_apple"));

    public static BasicItem PERFECT_ALEXANDRITE = new BasicItem(Vault.id("perfect_alexandrite"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem PERFECT_PAINITE = new BasicItem(Vault.id("perfect_painite"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem PERFECT_BENITOITE = new BasicItem(Vault.id("perfect_benitoite"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem PERFECT_LARIMAR = new BasicItem(Vault.id("perfect_larimar"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem PERFECT_BLACK_OPAL = new BasicItem(Vault.id("perfect_black_opal"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem PERFECT_ECHO_GEM = new BasicItem(Vault.id("perfect_echo_gem"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem PERFECT_WUTODIE = new BasicItem(Vault.id("perfect_wutodie"), (new Item.Properties()).tab(VAULT_MOD_GROUP));

    public static BasicItem VAULTERITE_INGOT = new BasicItem(Vault.id("vaulterite_ingot"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem RED_VAULT_ESSENCE = new BasicItem(Vault.id("red_vault_essence"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_DUST = new BasicItem(Vault.id("vault_dust"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_NUGGET = new BasicItem(Vault.id("vault_nugget"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_BRONZE = new BasicItem(Vault.id("vault_bronze"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_SILVER = new BasicItem(Vault.id("vault_silver"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem MAGNETITE = (BasicItem) new BasicTooltipItem(Vault.id("magnetite"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Can be used to repair vault magnets,"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("or craft new ones."))
            .withStyle(TextFormatting.GRAY)});
    public static BasicItem MAGNET_CORE_WEAK = new BasicItem(Vault.id("magnet_core_weak"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem MAGNET_CORE_STRONG = new BasicItem(Vault.id("magnet_core_strong"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem MAGNET_CORE_OMEGA = new BasicItem(Vault.id("magnet_core_omega"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem VAULT_ESSENCE = (BasicItem) new BasicTooltipItem(Vault.id("vault_essence"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("A rare essence of the vaults."))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("Used as an ingredient in several crafting recipes, including regeneration potions."))
            .withStyle(TextFormatting.GRAY)});
    public static BasicItem REPAIR_CORE = (BasicItem) new VaultRepairCoreItem(Vault.id("repair_core"), (new Item.Properties()).tab(VAULT_MOD_GROUP), 0, new ITextComponent[]{(ITextComponent) (new StringTextComponent("Can be used to fully repair any vault gear,"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("as long as it has repair slots remaining."))
            .withStyle(TextFormatting.GRAY)});
    public static BasicItem REPAIR_CORE_T2 = (BasicItem) new VaultRepairCoreItem(Vault.id("repair_core_t2"), (new Item.Properties()).tab(VAULT_MOD_GROUP), 1, new ITextComponent[]{(ITextComponent) (new StringTextComponent("Can be used to fully repair any vault gear,"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("as long as it has repair slots remaining."))
            .withStyle(TextFormatting.GRAY)});
    public static BasicItem REPAIR_CORE_T3 = (BasicItem) new VaultRepairCoreItem(Vault.id("repair_core_t3"), (new Item.Properties()).tab(VAULT_MOD_GROUP), 2, new ITextComponent[]{(ITextComponent) (new StringTextComponent("Can be used to fully repair any vault gear,"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("as long as it has repair slots remaining."))
            .withStyle(TextFormatting.GRAY)});
    public static BasicItem VAULT_PLATING = (BasicItem) new VaultPlateItem(Vault.id("vault_plating"), (new Item.Properties()).tab(VAULT_MOD_GROUP), 0, new ITextComponent[]{(ITextComponent) (new StringTextComponent("Permanently adds 50 max durability to any vault gear."))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("Maximum 20 plates can be attached to any one vault gear."))
            .withStyle(TextFormatting.GRAY)});
    public static BasicItem VAULT_PLATING_T2 = (BasicItem) new VaultPlateItem(Vault.id("vault_plating_t2"), (new Item.Properties()).tab(VAULT_MOD_GROUP), 1, new ITextComponent[]{(ITextComponent) (new StringTextComponent("Permanently adds 50 max durability to any vault gear."))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("Maximum 20 plates can be attached to any one vault gear."))
            .withStyle(TextFormatting.GRAY)});
    public static BasicItem VAULT_PLATING_T3 = (BasicItem) new VaultPlateItem(Vault.id("vault_plating_t3"), (new Item.Properties()).tab(VAULT_MOD_GROUP), 2, new ITextComponent[]{(ITextComponent) (new StringTextComponent("Permanently adds 50 max durability to any vault gear."))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("Maximum 20 plates can be attached to any one vault gear."))
            .withStyle(TextFormatting.GRAY)});
    public static WutaxShardItem WUTAX_SHARD = new WutaxShardItem(Vault.id("wutax_shard"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static BasicItem WUTAX_CRYSTAL = new BasicItem(Vault.id("wutax_crystal"), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static VaultCatalystItem VAULT_CATALYST = new VaultCatalystItem(VAULT_MOD_GROUP, Vault.id("vault_catalyst"));
    public static VaultInhibitorItem VAULT_INHIBITOR = new VaultInhibitorItem(VAULT_MOD_GROUP, Vault.id("vault_inhibitor"));
    public static BasicTooltipItem PAINITE_STAR = new BasicTooltipItem(Vault.id("painite_star"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(4), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Reroll the vault catalyst combination results"))
            .withStyle(TextFormatting.GRAY)});
    public static VaultRuneItem VAULT_RUNE_MINE = new VaultRuneItem(VAULT_MOD_GROUP, Vault.id("vault_rune_mineshaft"), "mineshaft");
    public static VaultRuneItem VAULT_RUNE_PUZZLE = new VaultRuneItem(VAULT_MOD_GROUP, Vault.id("vault_rune_puzzle_cube"), "puzzle_cube");
    public static VaultRuneItem VAULT_RUNE_DIGSITE = new VaultRuneItem(VAULT_MOD_GROUP, Vault.id("vault_rune_digsite"), "digsite");
    public static VaultRuneItem VAULT_RUNE_CRYSTAL = new VaultRuneItem(VAULT_MOD_GROUP, Vault.id("vault_rune_crystal_caves"), "crystal_caves");
    public static VaultRuneItem VAULT_RUNE_VIEWER = new VaultRuneItem(VAULT_MOD_GROUP, Vault.id("vault_rune_viewer"), "viewer");
    public static VaultRuneItem VAULT_RUNE_VENDOR = new VaultRuneItem(VAULT_MOD_GROUP, Vault.id("vault_rune_vendor"), "vendor");
    public static VaultRuneItem VAULT_RUNE_XMARK = new VaultRuneItem(VAULT_MOD_GROUP, Vault.id("vault_rune_xmark"), "x_spot");
    public static BasicItem VAULT_CATALYST_FRAGMENT = (BasicItem) new BasicTooltipItem(Vault.id("vault_catalyst_fragment"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Can be used to craft very powerful Catalysts,"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("which can modify a vault crystal."))
            .withStyle(TextFormatting.GRAY)});

    public static BasicItem SOUL_FLAME = new BasicItem(Vault.id("soul_flame"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(3));
    public static BasicItem VAULT_GEAR = new BasicItem(Vault.id("vault_gear"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(4));
    public static ItemVaultCrystalSeal CRYSTAL_SEAL_EXECUTIONER = new ItemVaultCrystalSeal(Vault.id("crystal_seal_executioner"), Vault.id("summon_and_kill_boss"));
    public static ItemVaultCrystalSeal CRYSTAL_SEAL_HUNTER = new ItemVaultCrystalSeal(Vault.id("crystal_seal_hunter"), Vault.id("scavenger_hunt"));
    public static ItemVaultCrystalSeal CRYSTAL_SEAL_ARCHITECT = new ItemVaultCrystalSeal(Vault.id("crystal_seal_architect"), Vault.id("architect"));
    public static ItemVaultCrystalSeal CRYSTAL_SEAL_ANCIENTS = new ItemVaultCrystalSeal(Vault.id("crystal_seal_ancients"), Vault.id("ancients"));
    public static ItemVaultCrystalSeal CRYSTAL_SEAL_RAID = new ItemVaultCrystalSeal(Vault.id("crystal_seal_raid"), Vault.id("raid_challenge"));
    public static ItemVaultRaffleSeal CRYSTAL_SEAL_RAFFLE = new ItemVaultRaffleSeal(Vault.id("crystal_seal_raffle"));
    public static BasicTooltipItem GEAR_CHARM = new BasicTooltipItem(Vault.id("gear_charm"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Combine with an unidentified vault gear in an anvil"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("to increase its tier to Tier 2."))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("Requires level: "))
            .withStyle(TextFormatting.GRAY).append((ITextComponent) (new StringTextComponent("100")).withStyle(TextFormatting.AQUA))});
    public static BasicTooltipItem GEAR_CHARM_T3 = new BasicTooltipItem(Vault.id("gear_charm_tier_3"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Combine with an unidentified vault gear in an anvil"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("to increase its tier to Tier 3."))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("Requires level: "))
            .withStyle(TextFormatting.GRAY).append((ITextComponent) (new StringTextComponent("200")).withStyle(TextFormatting.AQUA))});
    public static BasicTooltipItem IDENTIFICATION_TOME = new BasicTooltipItem(Vault.id("identification_tome"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Hold in the off hand to instantly"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("identify vault gear."))
            .withStyle(TextFormatting.GRAY)});
    public static BasicItem BANISHED_SOUL = (BasicItem) new BasicTooltipItem(Vault.id("banished_soul"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Adds 3000 Durability to a Vault idol."))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("Has a 1 in 3 chance to break the idol when applying."))
            .withStyle(TextFormatting.GRAY)});
    public static BasicItem UNKNOWN_ITEM = new BasicItem(Vault.id("unknown_item"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static BasicItem SOUL_SHARD = new BasicItem(Vault.id("soul_shard"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64));
    public static ItemShardPouch SHARD_POUCH = new ItemShardPouch(Vault.id("shard_pouch"));

    public static VaultPaxelItem VAULT_PAXEL = new VaultPaxelItem(Vault.id("vault_paxel"));
    public static BasicItem PAXEL_CHARM = (BasicItem) new BasicTooltipItem(Vault.id("paxel_charm"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Combine with a paxel in anvil to enhance it."))
            .withStyle(TextFormatting.AQUA), (ITextComponent) (new StringTextComponent("A paxel can have only one enhancement."))
            .withStyle(TextFormatting.GRAY)});
    public static InfiniteWaterBucketItem INFINITE_WATER_BUCKET = new InfiniteWaterBucketItem(Vault.id("infinite_water_bucket"));
    public static VaultPearlItem VAULT_PEARL = new VaultPearlItem(Vault.id("vault_pearl"));

    public static BasicScavengerItem SCAVENGER_CREEPER_EYE = new BasicScavengerItem("creeper_eye");
    public static BasicScavengerItem SCAVENGER_CREEPER_FOOT = new BasicScavengerItem("creeper_foot");
    public static BasicScavengerItem SCAVENGER_CREEPER_FUSE = new BasicScavengerItem("creeper_fuse");
    public static BasicScavengerItem SCAVENGER_CREEPER_TNT = new BasicScavengerItem("creeper_tnt");
    public static BasicScavengerItem SCAVENGER_CREEPER_VIAL = new BasicScavengerItem("creeper_vial");
    public static BasicScavengerItem SCAVENGER_CREEPER_CHARM = new BasicScavengerItem("creeper_soul_charm");

    public static BasicScavengerItem SCAVENGER_DROWNED_BARNACLE = new BasicScavengerItem("drowned_barnacle");
    public static BasicScavengerItem SCAVENGER_DROWNED_EYE = new BasicScavengerItem("drowned_eye");
    public static BasicScavengerItem SCAVENGER_DROWNED_HIDE = new BasicScavengerItem("drowned_hide");
    public static BasicScavengerItem SCAVENGER_DROWNED_VIAL = new BasicScavengerItem("drowned_vial");
    public static BasicScavengerItem SCAVENGER_DROWNED_CHARM = new BasicScavengerItem("drowned_soul_charm");

    public static BasicScavengerItem SCAVENGER_SKELETON_SHARD = new BasicScavengerItem("skeleton_bone_shard");
    public static BasicScavengerItem SCAVENGER_SKELETON_EYE = new BasicScavengerItem("skeleton_milky_eye");
    public static BasicScavengerItem SCAVENGER_SKELETON_RIBCAGE = new BasicScavengerItem("skeleton_ribcage");
    public static BasicScavengerItem SCAVENGER_SKELETON_SKULL = new BasicScavengerItem("skeleton_skull");
    public static BasicScavengerItem SCAVENGER_SKELETON_WISHBONE = new BasicScavengerItem("skeleton_wishbone");
    public static BasicScavengerItem SCAVENGER_SKELETON_VIAL = new BasicScavengerItem("skeleton_milky_vial");
    public static BasicScavengerItem SCAVENGER_SKELETON_CHARM = new BasicScavengerItem("skeleton_soul_charm");

    public static BasicScavengerItem SCAVENGER_SPIDER_FANGS = new BasicScavengerItem("spider_fangs");
    public static BasicScavengerItem SCAVENGER_SPIDER_LEG = new BasicScavengerItem("spider_leg");
    public static BasicScavengerItem SCAVENGER_SPIDER_WEBBING = new BasicScavengerItem("spider_webbing_spool");
    public static BasicScavengerItem SCAVENGER_SPIDER_CURSED_CHARM = new BasicScavengerItem("spider_cursed_charm");
    public static BasicScavengerItem SCAVENGER_SPIDER_VIAL = new BasicScavengerItem("spider_vial");
    public static BasicScavengerItem SCAVENGER_SPIDER_CHARM = new BasicScavengerItem("spider_soul_charm");

    public static BasicScavengerItem SCAVENGER_ZOMBIE_BRAIN = new BasicScavengerItem("zombie_brain");
    public static BasicScavengerItem SCAVENGER_ZOMBIE_ARM = new BasicScavengerItem("zombie_arm");
    public static BasicScavengerItem SCAVENGER_ZOMBIE_EAR = new BasicScavengerItem("zombie_ear");
    public static BasicScavengerItem SCAVENGER_ZOMBIE_EYE = new BasicScavengerItem("zombie_eye");
    public static BasicScavengerItem SCAVENGER_ZOMBIE_HIDE = new BasicScavengerItem("zombie_hide");
    public static BasicScavengerItem SCAVENGER_ZOMBIE_NOSE = new BasicScavengerItem("zombie_nose");
    public static BasicScavengerItem SCAVENGER_ZOMBIE_VIAL = new BasicScavengerItem("zombie_blood_vial");

    public static BasicScavengerItem SCAVENGER_TREASURE_BANGLE_BLUE = new BasicScavengerItem("blue_bangle");
    public static BasicScavengerItem SCAVENGER_TREASURE_BANGLE_PINK = new BasicScavengerItem("pink_bangle");
    public static BasicScavengerItem SCAVENGER_TREASURE_BANGLE_GREEN = new BasicScavengerItem("green_bangle");
    public static BasicScavengerItem SCAVENGER_TREASURE_EARRINGS = new BasicScavengerItem("earrings");
    public static BasicScavengerItem SCAVENGER_TREASURE_GOBLET = new BasicScavengerItem("goblet");
    public static BasicScavengerItem SCAVENGER_TREASURE_SACK = new BasicScavengerItem("sack");
    public static BasicScavengerItem SCAVENGER_TREASURE_SCROLL_RED = new BasicScavengerItem("red_scroll");
    public static BasicScavengerItem SCAVENGER_TREASURE_SCROLL_BLUE = new BasicScavengerItem("blue_scroll");

    public static BasicScavengerItem SCAVENGER_SCRAP_BROKEN_POTTERY = new BasicScavengerItem("broken_pottery");
    public static BasicScavengerItem SCAVENGER_SCRAP_CRACKED_PEARL = new BasicScavengerItem("cracked_pearl");
    public static BasicScavengerItem SCAVENGER_SCRAP_CRACKED_SCRIPT = new BasicScavengerItem("cracked_script");
    public static BasicScavengerItem SCAVENGER_SCRAP_EMPTY_JAR = new BasicScavengerItem("empty_jar");
    public static BasicScavengerItem SCAVENGER_SCRAP_OLD_BOOK = new BasicScavengerItem("old_book");
    public static BasicScavengerItem SCAVENGER_SCRAP_POTTERY_SHARD = new BasicScavengerItem("pottery_shard");
    public static BasicScavengerItem SCAVENGER_SCRAP_POULTICE_JAR = new BasicScavengerItem("poultice_jar");
    public static BasicScavengerItem SCAVENGER_SCRAP_PRESERVES_JAR = new BasicScavengerItem("preserves_jar");
    public static BasicScavengerItem SCAVENGER_SCRAP_RIPPED_PAGE = new BasicScavengerItem("ripped_page");
    public static BasicScavengerItem SCAVENGER_SCRAP_SADDLE_BAG = new BasicScavengerItem("saddle_bag");
    public static BasicScavengerItem SCAVENGER_SCRAP_SPICE_JAR = new BasicScavengerItem("spice_jar");
    public static BasicScavengerItem SCAVENGER_SCRAP_WIZARD_WAND = new BasicScavengerItem("wizard_wand");

    public static VaultMagnetItem VAULT_MAGNET_WEAK = new VaultMagnetItem(Vault.id("vault_magnet_weak"), VaultMagnetItem.MagnetType.WEAK);
    public static VaultMagnetItem VAULT_MAGNET_STRONG = new VaultMagnetItem(Vault.id("vault_magnet_strong"), VaultMagnetItem.MagnetType.STRONG);
    public static VaultMagnetItem VAULT_MAGNET_OMEGA = new VaultMagnetItem(Vault.id("vault_magnet_omega"), VaultMagnetItem.MagnetType.OMEGA);

    public static final BucketItem VOID_LIQUID_BUCKET = (BucketItem) (new BucketItem((Supplier) ModFluids.VOID_LIQUID, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(VAULT_MOD_GROUP))).setRegistryName(Vault.id("void_liquid_bucket"));

    public static GatedLootableItem MOD_BOX = new GatedLootableItem(Vault.id("mod_box"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) new StringTextComponent("Contains a random modded item from any of your unlocked mods")});


    public static LootableItem UNIDENTIFIED_TREASURE_KEY = new LootableItem(Vault.id("unidentified_treasure_key"), (new Item.Properties())
            .tab(VAULT_MOD_GROUP), () -> ModConfigs.UNIDENTIFIED_TREASURE_KEY.getRandomKey(new Random()));

    public static VoidOrbItem VOID_ORB = new VoidOrbItem(Vault.id("void_orb"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static BasicItem CRYSTAL_BURGER = (BasicItem) new BasicTooltipItem(Vault.id("crystal_burger"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Eternal Exp food"))
            .withStyle(TextFormatting.GOLD)});
    public static BasicItem FULL_PIZZA = (BasicItem) new BasicTooltipItem(Vault.id("full_pizza"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Eternal Exp food"))
            .withStyle(TextFormatting.GOLD)});
    public static BasicItem LIFE_SCROLL = (BasicItem) new BasicTooltipItem(Vault.id("life_scroll"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Revives a unalived eternal"))
            .withStyle(TextFormatting.GRAY)});
    public static BasicItem AURA_SCROLL = (BasicItem) new BasicTooltipItem(Vault.id("aura_scroll"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Rerolls an eternal's available auras"))
            .withStyle(TextFormatting.GRAY)});
    public static ArtisanScrollItem ARTISAN_SCROLL = new ArtisanScrollItem(Vault.id("artisan_scroll"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1));
    public static BasicTooltipItem FABRICATION_JEWEL = new BasicTooltipItem(Vault.id("fabrication_jewel"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Requires Talent: "))
            .withStyle(TextFormatting.GRAY).append((ITextComponent) (new StringTextComponent("Artisan")).withStyle(TextFormatting.AQUA))});

    public static ItemModArmorCrate ARMOR_CRATE_HELLCOW = new ItemModArmorCrate(Vault.id("armor_crate_hellcow"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.HELLCOW_SET));

    public static ItemModArmorCrate ARMOR_CRATE_BOTANIA = new ItemModArmorCrate(Vault.id("armor_crate_botania"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.BOTANIA_SET));

    public static ItemModArmorCrate ARMOR_CRATE_CREATE = new ItemModArmorCrate(Vault.id("armor_crate_create"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.CREATE_SET));

    public static ItemModArmorCrate ARMOR_CRATE_DANK = new ItemModArmorCrate(Vault.id("armor_crate_dank"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.DANK_SET));

    public static ItemModArmorCrate ARMOR_CRATE_FLUX = new ItemModArmorCrate(Vault.id("armor_crate_flux"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.FLUX_SET));

    public static ItemModArmorCrate ARMOR_CRATE_IMMERSIVE_ENGINEERING = new ItemModArmorCrate(Vault.id("armor_crate_ie"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.IMMERSIVE_ENGINEERING_SET));

    public static ItemModArmorCrate ARMOR_CRATE_MEKA = new ItemModArmorCrate(Vault.id("armor_crate_meka"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.MEKA_SET_DARK, ModModels.SpecialGearModel.MEKA_SET_LIGHT));

    public static ItemModArmorCrate ARMOR_CRATE_POWAH = new ItemModArmorCrate(Vault.id("armor_crate_powah"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.POWAH_SET));

    public static ItemModArmorCrate ARMOR_CRATE_THERMAL = new ItemModArmorCrate(Vault.id("armor_crate_thermal"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.THERMAL_SET));

    public static ItemModArmorCrate ARMOR_CRATE_TRASH = new ItemModArmorCrate(Vault.id("armor_crate_trash"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.TRASH_SET));

    public static ItemModArmorCrate ARMOR_CRATE_VILLAGER = new ItemModArmorCrate(Vault.id("armor_crate_villager"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.VILLAGER_SET));

    public static ItemModArmorCrate ARMOR_CRATE_AUTOMATIC = new ItemModArmorCrate(Vault.id("armor_crate_automatic"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.AUTOMATIC_SET));

    public static ItemModArmorCrate ARMOR_CRATE_FAIRY = new ItemModArmorCrate(Vault.id("armor_crate_fairy"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.FAIRY_SET));

    public static ItemModArmorCrate ARMOR_CRATE_BUILDING = new ItemModArmorCrate(Vault.id("armor_crate_building"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.BUILDING_SET));

    public static ItemModArmorCrate ARMOR_CRATE_ZOMBIE = new ItemModArmorCrate(Vault.id("armor_crate_zombie"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.ZOMBIE_SET));

    public static ItemModArmorCrate ARMOR_CRATE_XNET = new ItemModArmorCrate(Vault.id("armor_crate_xnet"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.XNET_SET));

    public static ItemModArmorCrate ARMOR_CRATE_TEST_DUMMY = new ItemModArmorCrate(Vault.id("armor_crate_test_dummy"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.TEST_DUMMY_SET));

    public static ItemModArmorCrate ARMOR_CRATE_INDUSTRIAL_FOREGOING = new ItemModArmorCrate(Vault.id("armor_crate_if"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.INDUSTRIAL_FOREGOING_SET));

    public static ItemModArmorCrate ARMOR_CRATE_CAKE = new ItemModArmorCrate(Vault.id("armor_crate_cake"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(64), () -> ImmutableList.of(ModModels.SpecialGearModel.CAKE_SET));


    public static FlawedRubyItem FLAWED_RUBY = new FlawedRubyItem(Vault.id("flawed_ruby"), (new Item.Properties()).tab(VAULT_MOD_GROUP), new ITextComponent[]{(ITextComponent) (new StringTextComponent("Combine with a gear piece in an anvil"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("to add additional modifier(s) with a"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("chance that the gear will break."))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent(" "))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("Requires Talent: "))
            .withStyle(TextFormatting.GRAY)
            .append((ITextComponent) (new StringTextComponent("Artisan ")).withStyle(TextFormatting.AQUA))
            .append((ITextComponent) (new StringTextComponent("or ")).withStyle(TextFormatting.GRAY))
            .append((ITextComponent) (new StringTextComponent("Treasure Hunter")).withStyle(TextFormatting.AQUA))});

    public static BasicItem ARTIFACT_FRAGMENT = new BasicItem(Vault.id("artifact_fragment"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(3).fireResistant());

    public static BasicTooltipItem VAULT_CHARM = new BasicTooltipItem(Vault.id("vault_charm"), (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1), new ITextComponent[]{(ITextComponent) (new StringTextComponent("When this charm is in your inventory"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("it will automatically void any item"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("whitelisted in the Vault Controller"))
            .withStyle(TextFormatting.GRAY), (ITextComponent) (new StringTextComponent("on pickup in a Vault."))
            .withStyle(TextFormatting.GRAY)});


    public static VaultCharmUpgrade CHARM_UPGRADE_TIER_1 = new VaultCharmUpgrade(Vault.id("charm_upgrade_tier_1"), VaultCharmUpgrade.Tier.ONE, (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1).fireResistant());
    public static VaultCharmUpgrade CHARM_UPGRADE_TIER_2 = new VaultCharmUpgrade(Vault.id("charm_upgrade_tier_2"), VaultCharmUpgrade.Tier.TWO, (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1).fireResistant());
    public static VaultCharmUpgrade CHARM_UPGRADE_TIER_3 = new VaultCharmUpgrade(Vault.id("charm_upgrade_tier_3"), VaultCharmUpgrade.Tier.THREE, (new Item.Properties()).tab(VAULT_MOD_GROUP).stacksTo(1).fireResistant());

    public static BurntCrystalItem BURNT_CRYSTAL = new BurntCrystalItem(VAULT_MOD_GROUP, Vault.id("burnt_crystal"));

    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.register( VAULT_BURGER);
        registry.register( OOZING_PIZZA);
        registry.register( VAULT_COOKIE);

        registry.register( SKILL_ORB);
        registry.register( ALEXANDRITE_GEM);
        registry.register( BENITOITE_GEM);
        registry.register( LARIMAR_GEM);
        registry.register( BLACK_OPAL_GEM);
        registry.register( PAINITE_GEM);
        registry.register( ISKALLIUM_GEM);
        registry.register( GORGINITE_GEM);
        registry.register( SPARKLETINE_GEM);
        registry.register( ASHIUM_GEM);
        registry.register( BOMIGNITE_GEM);
        registry.register( FUNSOIDE_GEM);
        registry.register( TUBIUM_GEM);
        registry.register( WUTODIE_GEM);
        registry.register( UPALINE_GEM);
        registry.register( PUFFIUM_GEM);
        registry.register( ECHO_GEM);
        registry.register( VAULT_ROCK);
        registry.register( POG);
        registry.register( ECHO_POG);

        registry.register( VAULT_CRYSTAL);

        registry.register( ISKALLIUM_KEY);
        registry.register( GORGINITE_KEY);
        registry.register( SPARKLETINE_KEY);
        registry.register( ASHIUM_KEY);
        registry.register( BOMIGNITE_KEY);
        registry.register( FUNSOIDE_KEY);
        registry.register( TUBIUM_KEY);
        registry.register( UPALINE_KEY);
        registry.register( PUFFIUM_KEY);

        registry.register( RELIC_BOOSTER_PACK);

        registry.register( DRAGON_HEAD_RELIC);
        registry.register( DRAGON_TAIL_RELIC);
        registry.register( DRAGON_FOOT_RELIC);
        registry.register( DRAGON_CHEST_RELIC);
        registry.register( DRAGON_BREATH_RELIC);
        registry.register( MINERS_DELIGHT_RELIC);
        registry.register( MINERS_LIGHT_RELIC);
        registry.register( PICKAXE_HANDLE_RELIC);
        registry.register( PICKAXE_HEAD_RELIC);
        registry.register( PICKAXE_TOOL_RELIC);
        registry.register( SWORD_BLADE_RELIC);
        registry.register( SWORD_HANDLE_RELIC);
        registry.register( SWORD_STICK_RELIC);
        registry.register( WARRIORS_ARMOUR_RELIC);
        registry.register( WARRIORS_CHARM_RELIC);
        registry.register( DIAMOND_ESSENCE_RELIC);
        registry.register( GOLD_ESSENCE_RELIC);
        registry.register( MYSTIC_GEM_ESSENCE_RELIC);
        registry.register( NETHERITE_ESSENCE_RELIC);
        registry.register( PLATINUM_ESSENCE_RELIC);
        registry.register( CUPCAKE_BLUE_RELIC);
        registry.register( CUPCAKE_LIME_RELIC);
        registry.register( CUPCAKE_PINK_RELIC);
        registry.register( CUPCAKE_PURPLE_RELIC);
        registry.register( CUPCAKE_RED_RELIC);
        registry.register( AIR_RELIC);
        registry.register( SPIRIT_RELIC);
        registry.register( FIRE_RELIC);
        registry.register( EARTH_RELIC);
        registry.register( WATER_RELIC);
        registry.register( TWOLF999_HEAD_RELIC);
        registry.register( TWOLF999_COMBAT_VEST_RELIC);
        registry.register( TWOLF999_COMBAT_LEGGINGS_RELIC);
        registry.register( TWOLF999_COMBAT_GLOVES_RELIC);
        registry.register( TWOLF999_COMBAT_BOOTS_RELIC);
        registry.register( ARMOR_OF_FORBEARANCE_RELIC);
        registry.register( HEART_OF_THE_VOID_RELIC);
        registry.register( NEMESIS_THWARTER_RELIC);
        registry.register( REVERENCE_EDGE_RELIC);
        registry.register( WINGS_OF_EQUITY_RELIC);
        registry.register( TWITCH_EMOTE1_RELIC);
        registry.register( TWITCH_EMOTE2_RELIC);
        registry.register( TWITCH_EMOTE3_RELIC);
        registry.register( TWITCH_EMOTE4_RELIC);
        registry.register( TWITCH_EMOTE5_RELIC);

        registry.register( VAULT_RUNE);
        registry.register( TRADER_CORE);
        registry.register( LEGENDARY_TREASURE_NORMAL);
        registry.register( LEGENDARY_TREASURE_RARE);
        registry.register( LEGENDARY_TREASURE_EPIC);
        registry.register( LEGENDARY_TREASURE_OMEGA);
        registry.register( UNIDENTIFIED_ARTIFACT);
        registry.register( VAULT_RELIC);
        registry.register( SKILL_ORB_FRAME);
        registry.register( SKILL_SHARD);
        registry.register( BITTER_LEMON);
        registry.register( SOUR_ORANGE);
        registry.register( MYSTIC_PEAR);
        registry.register( KEY_PIECE);
        registry.register( KEY_MOULD);
        registry.register( BLANK_KEY);
        registry.register( NETHERITE_CLUSTER);
        registry.register( ISKALLIUM_CLUSTER);
        registry.register( GORGINITE_CLUSTER);
        registry.register( SPARKLETINE_CLUSTER);
        registry.register( ASHIUM_CLUSTER);
        registry.register( BOMIGNITE_CLUSTER);
        registry.register( FUNSOIDE_CLUSTER);
        registry.register( TUBIUM_CLUSTER);
        registry.register( UPALINE_CLUSTER);
        registry.register( PUFFIUM_CLUSTER);

        registry.register( VAULT_STEW_MYSTERY);
        registry.register( VAULT_STEW_NORMAL);
        registry.register( VAULT_STEW_RARE);
        registry.register( VAULT_STEW_EPIC);
        registry.register( VAULT_STEW_OMEGA);
        registry.register( POISONOUS_MUSHROOM);
        registry.register( VAULT_GOLD);
        registry.register( VAULT_DIAMOND);
        registry.register( SKILL_ESSENCE);
        registry.register( UNIDENTIFIED_RELIC);
        registry.register( SWEET_KIWI);
        registry.register( HUNTER_EYE);
        registry.register( BURGER_PATTY);
        registry.register( BURGER_BUN);
        registry.register( MATURE_CHEDDAR);
        registry.register( MYSTIC_TOMATO);
        registry.register( VAULT_SCRAP);
        registry.register( VAULT_INGOT);
        registry.register( VAULT_PLATINUM);
        registry.register( MYSTERY_BOX);
        registry.register( DRILL_ARROW_PART);
        registry.register( DRILL_ARROW);
        registry.register( RESPEC_FLASK);
        registry.register( RESET_FLASK);
        registry.register( MYSTERY_EGG);
        registry.register( MYSTERY_HOSTILE_EGG);

        registry.register( ACCELERATION_CHIP);
        registry.register( PANDORAS_BOX);

        registry.register( ISKALLIUM_CHUNK);
        registry.register( GORGINITE_CHUNK);
        registry.register( SPARKLETINE_CHUNK);
        registry.register( ASHIUM_CHUNK);
        registry.register( BOMIGNITE_CHUNK);
        registry.register( FUNSOIDE_CHUNK);
        registry.register( TUBIUM_CHUNK);
        registry.register( UPALINE_CHUNK);
        registry.register( PUFFIUM_CHUNK);
        registry.register( OMEGA_POG);

        registry.register( ETERNAL_SOUL);
        registry.register( SPARK);

        registry.register( STAR_SHARD);
        registry.register( STAR_CORE);
        registry.register( STAR_ESSENCE);
        registry.register( KNOWLEDGE_STAR);

        registry.register( SWORD);
        registry.register( AXE);
        registry.register( HELMET);
        registry.register( CHESTPLATE);
        registry.register( LEGGINGS);
        registry.register( BOOTS);
        registry.register( ETCHING);
        registry.register( ETCHING_FRAGMENT);
        registry.register( IDOL_BENEVOLENT);
        registry.register( IDOL_OMNISCIENT);
        registry.register( IDOL_TIMEKEEPER);
        registry.register( IDOL_MALEVOLENCE);

        registry.register( PUZZLE_RUNE);
        registry.register( INFUSED_ETERNAL_SOUL);
        registry.register( UNKNOWN_EGG);

        registry.register( CANDY_BAR);
        registry.register( POWER_BAR);
        registry.register( JADE_APPLE);
        registry.register( COBALT_APPLE);
        registry.register( PIXIE_APPLE);
        registry.register( VAULT_APPLE);
        registry.register( LUCKY_APPLE);
        registry.register( TREASURE_APPLE);
        registry.register( POWER_APPLE);
        registry.register( GHOST_APPLE);
        registry.register( GOLEM_APPLE);
        registry.register( SWEET_APPLE);
        registry.register( HEARTY_APPLE);

        registry.register( PERFECT_ALEXANDRITE);
        registry.register( PERFECT_PAINITE);
        registry.register( PERFECT_BENITOITE);
        registry.register( PERFECT_LARIMAR);
        registry.register( PERFECT_BLACK_OPAL);
        registry.register( PERFECT_ECHO_GEM);
        registry.register( PERFECT_WUTODIE);

        registry.register( VAULT_DUST);
        registry.register( VAULT_NUGGET);
        registry.register( VAULT_BRONZE);
        registry.register( VAULT_SILVER);
        registry.register( MAGNETITE);
        registry.register( MAGNET_CORE_WEAK);
        registry.register( MAGNET_CORE_STRONG);
        registry.register( MAGNET_CORE_OMEGA);
        registry.register( VAULT_ESSENCE);
        registry.register( REPAIR_CORE);
        registry.register( REPAIR_CORE_T2);
        registry.register( REPAIR_CORE_T3);
        registry.register( VAULT_PLATING);
        registry.register( VAULT_PLATING_T2);
        registry.register( VAULT_PLATING_T3);
        registry.register( WUTAX_SHARD);
        registry.register( WUTAX_CRYSTAL);
        registry.register( VAULT_CATALYST);
        registry.register( VAULT_INHIBITOR);
        registry.register( PAINITE_STAR);
        registry.register( VAULT_RUNE_MINE);
        registry.register( VAULT_RUNE_PUZZLE);
        registry.register( VAULT_RUNE_DIGSITE);
        registry.register( VAULT_RUNE_CRYSTAL);
        registry.register( VAULT_RUNE_VIEWER);
        registry.register( VAULT_RUNE_VENDOR);
        registry.register( VAULT_RUNE_XMARK);
        registry.register( VAULT_CATALYST_FRAGMENT);
        registry.register( SOUL_FLAME);
        registry.register( VAULT_GEAR);
        registry.register( CRYSTAL_SEAL_EXECUTIONER);
        registry.register( CRYSTAL_SEAL_HUNTER);
        registry.register( CRYSTAL_SEAL_ARCHITECT);
        registry.register( CRYSTAL_SEAL_ANCIENTS);
        registry.register( CRYSTAL_SEAL_RAID);
        registry.register( CRYSTAL_SEAL_RAFFLE);
        registry.register( GEAR_CHARM);
        registry.register( GEAR_CHARM_T3);
        registry.register( IDENTIFICATION_TOME);
        registry.register( BANISHED_SOUL);
        registry.register( UNKNOWN_ITEM);
        registry.register( SOUL_SHARD);
        registry.register( SHARD_POUCH);

        registry.register( PAXEL_CHARM);
        registry.register( VAULT_PAXEL);
        registry.register( INFINITE_WATER_BUCKET);
        registry.register( VAULT_PEARL);

        registry.register( VAULT_MAGNET_WEAK);
        registry.register( VAULT_MAGNET_STRONG);
        registry.register( VAULT_MAGNET_OMEGA);

        registry.register( SCAVENGER_CREEPER_EYE);
        registry.register( SCAVENGER_CREEPER_FOOT);
        registry.register( SCAVENGER_CREEPER_FUSE);
        registry.register( SCAVENGER_CREEPER_TNT);
        registry.register( SCAVENGER_CREEPER_VIAL);
        registry.register( SCAVENGER_CREEPER_CHARM);
        registry.register( SCAVENGER_DROWNED_BARNACLE);
        registry.register( SCAVENGER_DROWNED_EYE);
        registry.register( SCAVENGER_DROWNED_HIDE);
        registry.register( SCAVENGER_DROWNED_VIAL);
        registry.register( SCAVENGER_DROWNED_CHARM);
        registry.register( SCAVENGER_SKELETON_SHARD);
        registry.register( SCAVENGER_SKELETON_EYE);
        registry.register( SCAVENGER_SKELETON_RIBCAGE);
        registry.register( SCAVENGER_SKELETON_SKULL);
        registry.register( SCAVENGER_SKELETON_WISHBONE);
        registry.register( SCAVENGER_SKELETON_VIAL);
        registry.register( SCAVENGER_SKELETON_CHARM);
        registry.register( SCAVENGER_SPIDER_FANGS);
        registry.register( SCAVENGER_SPIDER_LEG);
        registry.register( SCAVENGER_SPIDER_WEBBING);
        registry.register( SCAVENGER_SPIDER_CURSED_CHARM);
        registry.register( SCAVENGER_SPIDER_VIAL);
        registry.register( SCAVENGER_SPIDER_CHARM);
        registry.register( SCAVENGER_ZOMBIE_BRAIN);
        registry.register( SCAVENGER_ZOMBIE_ARM);
        registry.register( SCAVENGER_ZOMBIE_EAR);
        registry.register( SCAVENGER_ZOMBIE_EYE);
        registry.register( SCAVENGER_ZOMBIE_HIDE);
        registry.register( SCAVENGER_ZOMBIE_NOSE);
        registry.register( SCAVENGER_ZOMBIE_VIAL);
        registry.register( SCAVENGER_TREASURE_BANGLE_BLUE);
        registry.register( SCAVENGER_TREASURE_BANGLE_PINK);
        registry.register( SCAVENGER_TREASURE_BANGLE_GREEN);
        registry.register( SCAVENGER_TREASURE_EARRINGS);
        registry.register( SCAVENGER_TREASURE_GOBLET);
        registry.register( SCAVENGER_TREASURE_SACK);
        registry.register( SCAVENGER_TREASURE_SCROLL_RED);
        registry.register( SCAVENGER_TREASURE_SCROLL_BLUE);
        registry.register( SCAVENGER_SCRAP_BROKEN_POTTERY);
        registry.register( SCAVENGER_SCRAP_CRACKED_PEARL);
        registry.register( SCAVENGER_SCRAP_CRACKED_SCRIPT);
        registry.register( SCAVENGER_SCRAP_EMPTY_JAR);
        registry.register( SCAVENGER_SCRAP_OLD_BOOK);
        registry.register( SCAVENGER_SCRAP_POTTERY_SHARD);
        registry.register( SCAVENGER_SCRAP_POULTICE_JAR);
        registry.register( SCAVENGER_SCRAP_PRESERVES_JAR);
        registry.register( SCAVENGER_SCRAP_RIPPED_PAGE);
        registry.register( SCAVENGER_SCRAP_SADDLE_BAG);
        registry.register( SCAVENGER_SCRAP_SPICE_JAR);
        registry.register( SCAVENGER_SCRAP_WIZARD_WAND);

        registry.register( VOID_LIQUID_BUCKET);
        registry.register( MOD_BOX);

        registry.register( VAULTERITE_INGOT);
        registry.register( RED_VAULT_ESSENCE);
        registry.register( UNIDENTIFIED_TREASURE_KEY);

        registry.register( VOID_ORB);
        registry.register( CRYSTAL_BURGER);
        registry.register( FULL_PIZZA);
        registry.register( LIFE_SCROLL);
        registry.register( AURA_SCROLL);
        registry.register( ARTISAN_SCROLL);
        registry.register( FABRICATION_JEWEL);

        registry.register( FLAWED_RUBY);
        registry.register( ARMOR_CRATE_HELLCOW);
        registry.register( ARMOR_CRATE_BOTANIA);
        registry.register( ARMOR_CRATE_CREATE);
        registry.register( ARMOR_CRATE_DANK);
        registry.register( ARMOR_CRATE_FLUX);
        registry.register( ARMOR_CRATE_IMMERSIVE_ENGINEERING);
        registry.register( ARMOR_CRATE_MEKA);
        registry.register( ARMOR_CRATE_POWAH);
        registry.register( ARMOR_CRATE_THERMAL);
        registry.register( ARMOR_CRATE_TRASH);
        registry.register( ARMOR_CRATE_VILLAGER);
        registry.register( ARMOR_CRATE_AUTOMATIC);
        registry.register( ARMOR_CRATE_FAIRY);
        registry.register( ARMOR_CRATE_BUILDING);
        registry.register( ARMOR_CRATE_ZOMBIE);
        registry.register( ARMOR_CRATE_XNET);
        registry.register( ARMOR_CRATE_TEST_DUMMY);
        registry.register( ARMOR_CRATE_INDUSTRIAL_FOREGOING);
        registry.register( ARMOR_CRATE_CAKE);

        registry.register( ARTIFACT_FRAGMENT);
        registry.register( VAULT_CHARM);
        registry.register( CHARM_UPGRADE_TIER_1);
        registry.register( CHARM_UPGRADE_TIER_2);
        registry.register( CHARM_UPGRADE_TIER_3);

        registry.register( BURNT_CRYSTAL);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModItems.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */