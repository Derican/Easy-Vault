package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.config.*;


public class ModConfigs {
    public static AbilitiesConfig ABILITIES;
    public static AbilitiesGUIConfig ABILITIES_GUI;
    public static TalentsConfig TALENTS;
    public static TalentsGUIConfig TALENTS_GUI;
    public static ResearchConfig RESEARCHES;
    public static ResearchesGUIConfig RESEARCHES_GUI;
    public static ResearchGroupConfig RESEARCH_GROUPS;
    public static ResearchGroupStyleConfig RESEARCH_GROUP_STYLES;
    public static SkillDescriptionsConfig SKILL_DESCRIPTIONS;
    public static SkillGatesConfig SKILL_GATES;

    public static void register() {
        ABILITIES = (AbilitiesConfig) (new AbilitiesConfig()).readConfig();
        ABILITIES_GUI = (AbilitiesGUIConfig) (new AbilitiesGUIConfig()).readConfig();
        TALENTS = (TalentsConfig) (new TalentsConfig()).readConfig();
        TALENTS_GUI = (TalentsGUIConfig) (new TalentsGUIConfig()).readConfig();
        RESEARCHES = (ResearchConfig) (new ResearchConfig()).readConfig();
        RESEARCHES_GUI = (ResearchesGUIConfig) (new ResearchesGUIConfig()).readConfig();
        RESEARCH_GROUPS = (ResearchGroupConfig) (new ResearchGroupConfig()).readConfig();
        RESEARCH_GROUP_STYLES = (ResearchGroupStyleConfig) (new ResearchGroupStyleConfig()).readConfig();
        SKILL_DESCRIPTIONS = (SkillDescriptionsConfig) (new SkillDescriptionsConfig()).readConfig();
        SKILL_GATES = (SkillGatesConfig) (new SkillGatesConfig()).readConfig();
        LEVELS_META = (VaultLevelsConfig) (new VaultLevelsConfig()).readConfig();
        VAULT_RELICS = (VaultRelicsConfig) (new VaultRelicsConfig()).readConfig();
        VAULT_MOBS = (VaultMobsConfig) (new VaultMobsConfig()).readConfig();
        VAULT_ITEMS = (VaultItemsConfig) (new VaultItemsConfig()).readConfig();
        VAULT_ALTAR = (VaultAltarConfig) (new VaultAltarConfig()).readConfig();
        VAULT_GENERAL = (VaultGeneralConfig) (new VaultGeneralConfig()).readConfig();
        VAULT_CRYSTAL = (VaultCrystalConfig) (new VaultCrystalConfig()).readConfig();
        VAULT_PORTAL = (VaultPortalConfig) (new VaultPortalConfig()).readConfig();
        LEGENDARY_TREASURE_NORMAL = (LegendaryTreasureNormalConfig) (new LegendaryTreasureNormalConfig()).readConfig();
        LEGENDARY_TREASURE_RARE = (LegendaryTreasureRareConfig) (new LegendaryTreasureRareConfig()).readConfig();
        LEGENDARY_TREASURE_EPIC = (LegendaryTreasureEpicConfig) (new LegendaryTreasureEpicConfig()).readConfig();
        LEGENDARY_TREASURE_OMEGA = (LegendaryTreasureOmegaConfig) (new LegendaryTreasureOmegaConfig()).readConfig();
        STATUE_LOOT = (StatueLootConfig) (new StatueLootConfig()).readConfig();
        CRYO_CHAMBER = (CryoChamberConfig) (new CryoChamberConfig()).readConfig();
        KEY_PRESS = (KeyPressRecipesConfig) (new KeyPressRecipesConfig()).readConfig();
        OVERLEVEL_ENCHANT = (OverLevelEnchantConfig) (new OverLevelEnchantConfig()).readConfig();
        VAULT_STEW = (VaultStewConfig) (new VaultStewConfig()).readConfig();
        MYSTERY_BOX = (MysteryBoxConfig) (new MysteryBoxConfig()).readConfig();
        VAULT_MODIFIERS = (VaultModifiersConfig) (new VaultModifiersConfig()).readConfig();
        TRADER_CORE_COMMON = (TraderCoreConfig.TraderCoreCommonConfig) (new TraderCoreConfig.TraderCoreCommonConfig()).readConfig();
        PANDORAS_BOX = (PandorasBoxConfig) (new PandorasBoxConfig()).readConfig();
        ETERNAL = (EternalConfig) (new EternalConfig()).readConfig();
        VAULT_GEAR_SCRAPPY = (VaultGearConfig) (new VaultGearConfig.Scrappy()).readConfig();
        VAULT_GEAR_COMMON = (VaultGearConfig) (new VaultGearConfig.Common()).readConfig();
        VAULT_GEAR_RARE = (VaultGearConfig) (new VaultGearConfig.Rare()).readConfig();
        VAULT_GEAR_EPIC = (VaultGearConfig) (new VaultGearConfig.Epic()).readConfig();
        VAULT_GEAR_OMEGA = (VaultGearConfig) (new VaultGearConfig.Omega()).readConfig();
        VAULT_GEAR = (VaultGearConfig.General) (new VaultGearConfig.General()).readConfig();
        SETS = (SetsConfig) (new SetsConfig()).readConfig();
        GLOBAL_TRADER = (GlobalTraderConfig) (new GlobalTraderConfig()).readConfig();
        VAULT_LOOTABLES = (VaultLootablesConfig) (new VaultLootablesConfig()).readConfig();
        VAULT_CHEST = (VaultChestConfig) (new VaultChestConfig("vault_chest")).readConfig();
        VAULT_TREASURE_CHEST = (VaultChestConfig) (new VaultChestConfig("vault_treasure_chest")).readConfig();
        VAULT_ALTAR_CHEST = (VaultChestConfig) (new VaultChestConfig("vault_altar_chest")).readConfig();
        VAULT_COOP_CHEST = (VaultChestConfig) (new VaultChestConfig("vault_coop_chest")).readConfig();
        VAULT_BONUS_CHEST = (VaultChestConfig) (new VaultChestConfig("vault_bonus_chest")).readConfig();
        VAULT_CHEST_META = (VaultMetaChestConfig) (new VaultMetaChestConfig()).readConfig();
        STATUE_RECYCLING = (StatueRecyclingConfig) (new StatueRecyclingConfig()).readConfig();
        UNKNOWN_EGG = (UnknownEggConfig) (new UnknownEggConfig()).readConfig();
        CONSUMABLES = (ConsumablesConfig) (new ConsumablesConfig()).readConfig();
        LOOT_TABLES = (LootTablesConfig) (new LootTablesConfig()).readConfig();
        VAULT_UTILITIES = (VaultUtilitiesConfig) (new VaultUtilitiesConfig()).readConfig();
        VAULT_CRYSTAL_CATALYST = (VaultCrystalCatalystConfig) (new VaultCrystalCatalystConfig()).readConfig();
        PLAYER_SCALING = (PlayerScalingConfig) (new PlayerScalingConfig()).readConfig();
        PAXEL_ENHANCEMENTS = (PaxelEnhancementConfig) (new PaxelEnhancementConfig()).readConfig();
        SCAVENGER_HUNT = (ScavengerHuntConfig) (new ScavengerHuntConfig()).readConfig();
        DURBILITY = (DurabilityConfig) (new DurabilityConfig()).readConfig();
        MOD_BOX = (ModBoxConfig) (new ModBoxConfig()).readConfig();
        ARCHITECT_EVENT = (ArchitectEventConfig) (new ArchitectEventConfig()).readConfig();
        UNIDENTIFIED_TREASURE_KEY = (UnidentifiedTreasureKeyConfig) (new UnidentifiedTreasureKeyConfig()).readConfig();
        VAULT_GEAR_UPGRADE = (VaultGearUpgradeConfig) (new VaultGearUpgradeConfig()).readConfig();
        VAULT_GEAR_SCALING = (VaultGearScalingConfig) (new VaultGearScalingConfig()).readConfig();
        VAULT_SIZE = (VaultSizeConfig) (new VaultSizeConfig()).readConfig();
        SOUL_SHARD = (SoulShardConfig) (new SoulShardConfig()).readConfig();
        ETERNAL_ATTRIBUTES = (EternalAttributeConfig) (new EternalAttributeConfig()).readConfig();
        ETERNAL_AURAS = (EternalAuraConfig) (new EternalAuraConfig()).readConfig();
        VAULT_GEAR_UTILITIES = (VaultGearUtilitiesConfig) (new VaultGearUtilitiesConfig()).readConfig();
        VAULT_GEAR_CRAFTING_SCALING = (VaultGearCraftingScalingConfig) (new VaultGearCraftingScalingConfig()).readConfig();
        DIFFICULTY_DESCRIPTION = (DifficultyDescriptionConfig) (new DifficultyDescriptionConfig()).readConfig();
        SCALING_CHEST_REWARDS = (VaultScalingChestConfig) (new VaultScalingChestConfig()).readConfig();
        VAULT_INHIBITOR = (VaultInhibitorConfig) (new VaultInhibitorConfig()).readConfig();
        FLAWED_RUBY = (FlawedRubyConfig) (new FlawedRubyConfig()).readConfig();
        ETCHING = (EtchingConfig) (new EtchingConfig()).readConfig();
        RAID_CONFIG = (RaidConfig) (new RaidConfig()).readConfig();
        RAID_MODIFIER_CONFIG = (RaidModifierConfig) (new RaidModifierConfig()).readConfig();
        RAID_EVENT_CONFIG = (RaidEventConfig) (new RaidEventConfig()).readConfig();
        VAULT_CHARM = (VaultCharmConfig) (new VaultCharmConfig()).readConfig();
        MYSTERY_EGG = (MysteryEggConfig) (new MysteryEggConfig()).readConfig();
        MYSTERY_HOSTILE_EGG = (MysteryHostileEggConfig) (new MysteryHostileEggConfig()).readConfig();
        VAULT_RUNE = (VaultRuneConfig) (new VaultRuneConfig()).readConfig();
        OTHER_SIDE = (OtherSideConfig) (new OtherSideConfig()).readConfig();
        TOOLTIP = (TooltipConfig) (new TooltipConfig()).readConfig();

        Vault.LOGGER.info("Vault Configs are loaded successfully!");
    }

    public static VaultLevelsConfig LEVELS_META;
    public static VaultRelicsConfig VAULT_RELICS;
    public static VaultMobsConfig VAULT_MOBS;
    public static VaultItemsConfig VAULT_ITEMS;
    public static VaultAltarConfig VAULT_ALTAR;
    public static VaultGeneralConfig VAULT_GENERAL;
    public static VaultCrystalConfig VAULT_CRYSTAL;
    public static VaultPortalConfig VAULT_PORTAL;
    public static LegendaryTreasureNormalConfig LEGENDARY_TREASURE_NORMAL;
    public static LegendaryTreasureRareConfig LEGENDARY_TREASURE_RARE;
    public static LegendaryTreasureEpicConfig LEGENDARY_TREASURE_EPIC;
    public static LegendaryTreasureOmegaConfig LEGENDARY_TREASURE_OMEGA;
    public static StatueLootConfig STATUE_LOOT;
    public static CryoChamberConfig CRYO_CHAMBER;
    public static KeyPressRecipesConfig KEY_PRESS;
    public static OverLevelEnchantConfig OVERLEVEL_ENCHANT;
    public static VaultStewConfig VAULT_STEW;
    public static MysteryBoxConfig MYSTERY_BOX;
    public static VaultModifiersConfig VAULT_MODIFIERS;
    public static TraderCoreConfig.TraderCoreCommonConfig TRADER_CORE_COMMON;
    public static PandorasBoxConfig PANDORAS_BOX;
    public static EternalConfig ETERNAL;
    public static VaultGearConfig VAULT_GEAR_SCRAPPY;
    public static VaultGearConfig VAULT_GEAR_COMMON;
    public static VaultGearConfig VAULT_GEAR_RARE;
    public static VaultGearConfig VAULT_GEAR_EPIC;
    public static VaultGearConfig VAULT_GEAR_OMEGA;
    public static VaultGearConfig.General VAULT_GEAR;
    public static SetsConfig SETS;
    public static GlobalTraderConfig GLOBAL_TRADER;
    public static VaultLootablesConfig VAULT_LOOTABLES;
    public static VaultChestConfig VAULT_CHEST;
    public static VaultChestConfig VAULT_TREASURE_CHEST;
    public static VaultChestConfig VAULT_ALTAR_CHEST;
    public static VaultChestConfig VAULT_COOP_CHEST;
    public static VaultChestConfig VAULT_BONUS_CHEST;
    public static VaultMetaChestConfig VAULT_CHEST_META;
    public static StatueRecyclingConfig STATUE_RECYCLING;
    public static UnknownEggConfig UNKNOWN_EGG;
    public static ConsumablesConfig CONSUMABLES;
    public static LootTablesConfig LOOT_TABLES;
    public static VaultUtilitiesConfig VAULT_UTILITIES;
    public static VaultCrystalCatalystConfig VAULT_CRYSTAL_CATALYST;
    public static PlayerScalingConfig PLAYER_SCALING;
    public static PaxelEnhancementConfig PAXEL_ENHANCEMENTS;
    public static ScavengerHuntConfig SCAVENGER_HUNT;
    public static DurabilityConfig DURBILITY;
    public static ModBoxConfig MOD_BOX;
    public static ArchitectEventConfig ARCHITECT_EVENT;
    public static UnidentifiedTreasureKeyConfig UNIDENTIFIED_TREASURE_KEY;
    public static VaultGearUpgradeConfig VAULT_GEAR_UPGRADE;
    public static VaultGearScalingConfig VAULT_GEAR_SCALING;
    public static VaultSizeConfig VAULT_SIZE;
    public static SoulShardConfig SOUL_SHARD;
    public static EternalAttributeConfig ETERNAL_ATTRIBUTES;
    public static EternalAuraConfig ETERNAL_AURAS;
    public static VaultGearUtilitiesConfig VAULT_GEAR_UTILITIES;
    public static VaultGearCraftingScalingConfig VAULT_GEAR_CRAFTING_SCALING;
    public static DifficultyDescriptionConfig DIFFICULTY_DESCRIPTION;
    public static VaultScalingChestConfig SCALING_CHEST_REWARDS;
    public static VaultInhibitorConfig VAULT_INHIBITOR;
    public static FlawedRubyConfig FLAWED_RUBY;
    public static EtchingConfig ETCHING;
    public static RaidConfig RAID_CONFIG;
    public static RaidModifierConfig RAID_MODIFIER_CONFIG;
    public static RaidEventConfig RAID_EVENT_CONFIG;
    public static VaultCharmConfig VAULT_CHARM;
    public static MysteryEggConfig MYSTERY_EGG;
    public static MysteryHostileEggConfig MYSTERY_HOSTILE_EGG;
    public static VaultRuneConfig VAULT_RUNE;
    public static OtherSideConfig OTHER_SIDE;
    public static TooltipConfig TOOLTIP;

    public static void registerCompressionConfigs() {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModConfigs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */