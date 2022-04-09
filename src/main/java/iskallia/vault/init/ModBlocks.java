// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.init;

import com.mojang.datafixers.types.Type;
import iskallia.vault.Vault;
import iskallia.vault.block.*;
import iskallia.vault.block.entity.*;
import iskallia.vault.block.item.*;
import iskallia.vault.block.render.*;
import iskallia.vault.client.render.VaultISTER;
import iskallia.vault.fluid.VoidFluid;
import iskallia.vault.fluid.block.VoidFluidBlock;
import iskallia.vault.util.StatueType;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TallBlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks
{
    public static final VaultPortalBlock VAULT_PORTAL;
    public static final FinalVaultPortalBlock FINAL_VAULT_PORTAL;
    public static final OtherSidePortalBlock OTHER_SIDE_PORTAL;
    public static final VaultAltarBlock VAULT_ALTAR;
    public static final VaultOreBlock ALEXANDRITE_ORE;
    public static final VaultOreBlock BENITOITE_ORE;
    public static final VaultOreBlock LARIMAR_ORE;
    public static final VaultOreBlock BLACK_OPAL_ORE;
    public static final VaultOreBlock PAINITE_ORE;
    public static final VaultOreBlock ISKALLIUM_ORE;
    public static final VaultOreBlock GORGINITE_ORE;
    public static final VaultOreBlock SPARKLETINE_ORE;
    public static final VaultOreBlock WUTODIE_ORE;
    public static final VaultOreBlock ASHIUM_ORE;
    public static final VaultOreBlock BOMIGNITE_ORE;
    public static final VaultOreBlock FUNSOIDE_ORE;
    public static final VaultOreBlock TUBIUM_ORE;
    public static final VaultOreBlock UPALINE_ORE;
    public static final VaultOreBlock PUFFIUM_ORE;
    public static final VaultOreBlock ECHO_ORE;
    public static final Block UNKNOWN_ORE;
    public static final VaultRockBlock VAULT_ROCK_ORE;
    public static final DoorBlock ISKALLIUM_DOOR;
    public static final DoorBlock GORGINITE_DOOR;
    public static final DoorBlock SPARKLETINE_DOOR;
    public static final DoorBlock ASHIUM_DOOR;
    public static final DoorBlock BOMIGNITE_DOOR;
    public static final DoorBlock FUNSOIDE_DOOR;
    public static final DoorBlock TUBIUM_DOOR;
    public static final DoorBlock UPALINE_DOOR;
    public static final DoorBlock PUFFIUM_DOOR;
    public static final DoorBlock UNKNOWN_DOOR;
    public static final VaultRuneBlock VAULT_RUNE_BLOCK;
    public static final VaultArtifactBlock VAULT_ARTIFACT;
    public static final VaultCrateBlock VAULT_CRATE;
    public static final VaultCrateBlock VAULT_CRATE_CAKE;
    public static final VaultCrateBlock VAULT_CRATE_ARENA;
    public static final VaultCrateBlock VAULT_CRATE_SCAVENGER;
    public static final ObeliskBlock OBELISK;
    public static final VendingMachineBlock VENDING_MACHINE;
    public static final AdvancedVendingBlock ADVANCED_VENDING_MACHINE;
    public static final VaultBedrockBlock VAULT_BEDROCK;
    public static final Block VAULT_STONE;
    public static final GlassBlock VAULT_GLASS;
    public static final RelicStatueBlock RELIC_STATUE;
    public static final LootStatueBlock GIFT_NORMAL_STATUE;
    public static final LootStatueBlock GIFT_MEGA_STATUE;
    public static final BowHatBlock BOW_HAT;
    public static final StatueDragonHeadBlock STATUE_DRAGON_HEAD;
    public static final LootStatueBlock VAULT_PLAYER_LOOT_STATUE;
    public static final CryoChamberBlock CRYO_CHAMBER;
    public static final KeyPressBlock KEY_PRESS;
    public static final Block VAULT_DIAMOND_BLOCK;
    public static final MazeBlock MAZE_BLOCK;
    public static final PuzzleRuneBlock PUZZLE_RUNE_BLOCK;
    public static final Block YELLOW_PUZZLE_CONCRETE;
    public static final Block PINK_PUZZLE_CONCRETE;
    public static final Block GREEN_PUZZLE_CONCRETE;
    public static final Block BLUE_PUZZLE_CONCRETE;
    public static final OmegaStatueBlock OMEGA_STATUE;
    public static final OmegaVariantStatueBlock OMEGA_STATUE_VARIANT;
    public static final TrophyBlock TROPHY_STATUE;
    public static final TransmogTableBlock TRANSMOG_TABLE;
    public static final Block VAULT_LOOT_RICHITY;
    public static final Block VAULT_LOOT_RESOURCE;
    public static final Block VAULT_LOOT_MISC;
    public static final Block UNKNOWN_VAULT_CHEST;
    public static final Block UNKNOWN_TREASURE_CHEST;
    public static final Block UNKNOWN_VAULT_OBJECTIVE;
    public static final Block VAULT_CHEST;
    public static final Block VAULT_TREASURE_CHEST;
    public static final Block VAULT_ALTAR_CHEST;
    public static final Block VAULT_COOP_CHEST;
    public static final Block VAULT_BONUS_CHEST;
    public static final XPAltarBlock XP_ALTAR;
    public static final BloodAltarBlock BLOOD_ALTAR;
    public static final TimeAltarBlock TIME_ALTAR;
    public static final SoulAltarBlock SOUL_ALTAR;
    public static final StatueCauldronBlock STATUE_CAULDRON;
    public static final ScavengerChestBlock SCAVENGER_CHEST;
    public static final ScavengerTreasureBlock SCAVENGER_TREASURE;
    public static final StabilizerBlock STABILIZER;
    public static final CatalystDecryptionTableBlock CATALYST_DECRYPTION_TABLE;
    public static final EtchingVendorControllerBlock ETCHING_CONTROLLER_BLOCK;
    public static final VaultCharmControllerBlock VAULT_CHARM_CONTROLLER_BLOCK;
    public static final VaultRaidControllerBlock RAID_CONTROLLER_BLOCK;
    public static final FlowingFluidBlock VOID_LIQUID_BLOCK;
    public static final RelicStatueBlockItem RELIC_STATUE_BLOCK_ITEM;
    public static final LootStatueBlockItem GIFT_NORMAL_STATUE_BLOCK_ITEM;
    public static final LootStatueBlockItem GIFT_MEGA_STATUE_BLOCK_ITEM;
    public static final LootStatueBlockItem VAULT_PLAYER_LOOT_STATUE_BLOCK_ITEM;
    public static final LootStatueBlockItem OMEGA_STATUE_BLOCK_ITEM;
    public static final LootStatueBlockItem OMEGA_STATUE_VARIANT_BLOCK_ITEM;
    public static final TrophyStatueBlockItem TROPHY_STATUE_BLOCK_ITEM;
    public static final VendingMachineBlockItem VENDING_MACHINE_BLOCK_ITEM;
    public static final AdvancedVendingMachineBlockItem ADVANCED_VENDING_BLOCK_ITEM;
    public static final PuzzleRuneBlock.Item PUZZLE_RUNE_BLOCK_ITEM;
    public static final BlockItem VAULT_CHEST_ITEM;
    public static final BlockItem VAULT_TREASURE_CHEST_ITEM;
    public static final BlockItem VAULT_ALTAR_CHEST_ITEM;
    public static final BlockItem VAULT_COOP_CHEST_ITEM;
    public static final BlockItem VAULT_BONUS_CHEST_ITEM;
    public static final BlockItem SCAVENGER_CHEST_ITEM;
    public static final TileEntityType<VaultAltarTileEntity> VAULT_ALTAR_TILE_ENTITY;
    public static final TileEntityType<VaultRuneTileEntity> VAULT_RUNE_TILE_ENTITY;
    public static final TileEntityType<VaultCrateTileEntity> VAULT_CRATE_TILE_ENTITY;
    public static final TileEntityType<VaultPortalTileEntity> VAULT_PORTAL_TILE_ENTITY;
    public static final TileEntityType<OtherSidePortalTileEntity> OTHER_SIDE_PORTAL_TILE_ENTITY;
    public static final TileEntityType<VendingMachineTileEntity> VENDING_MACHINE_TILE_ENTITY;
    public static final TileEntityType<AdvancedVendingTileEntity> ADVANCED_VENDING_MACHINE_TILE_ENTITY;
    public static final TileEntityType<RelicStatueTileEntity> RELIC_STATUE_TILE_ENTITY;
    public static final TileEntityType<LootStatueTileEntity> LOOT_STATUE_TILE_ENTITY;
    public static final TileEntityType<TrophyStatueTileEntity> TROPHY_STATUE_TILE_ENTITY;
    public static final TileEntityType<CryoChamberTileEntity> CRYO_CHAMBER_TILE_ENTITY;
    public static final TileEntityType<AncientCryoChamberTileEntity> ANCIENT_CRYO_CHAMBER_TILE_ENTITY;
    public static final TileEntityType<VaultDoorTileEntity> VAULT_DOOR_TILE_ENTITY;
    public static final TileEntityType<VaultLootableTileEntity> VAULT_LOOTABLE_TILE_ENTITY;
    public static final TileEntityType<VaultChestTileEntity> VAULT_CHEST_TILE_ENTITY;
    public static final TileEntityType<VaultTreasureChestTileEntity> VAULT_TREASURE_CHEST_TILE_ENTITY;
    public static final TileEntityType<XpAltarTileEntity> XP_ALTAR_TILE_ENTITY;
    public static final TileEntityType<BloodAltarTileEntity> BLOOD_ALTAR_TILE_ENTITY;
    public static final TileEntityType<TimeAltarTileEntity> TIME_ALTAR_TILE_ENTITY;
    public static final TileEntityType<SoulAltarTileEntity> SOUL_ALTAR_TILE_ENTITY;
    public static final TileEntityType<StatueCauldronTileEntity> STATUE_CAULDRON_TILE_ENTITY;
    public static final TileEntityType<ObeliskTileEntity> OBELISK_TILE_ENTITY;
    public static final TileEntityType<ScavengerChestTileEntity> SCAVENGER_CHEST_TILE_ENTITY;
    public static final TileEntityType<ScavengerTreasureTileEntity> SCAVENGER_TREASURE_TILE_ENTITY;
    public static final TileEntityType<StabilizerTileEntity> STABILIZER_TILE_ENTITY;
    public static final TileEntityType<CatalystDecryptionTableTileEntity> CATALYST_DECRYPTION_TABLE_TILE_ENTITY;
    public static final TileEntityType<EtchingVendorControllerTileEntity> ETCHING_CONTROLLER_TILE_ENTITY;
    public static final TileEntityType<VaultCharmControllerTileEntity> VAULT_CHARM_CONTROLLER_TILE_ENTITY;
    public static final TileEntityType<VaultRaidControllerTileEntity> RAID_CONTROLLER_TILE_ENTITY;
    
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        registerBlock(event, (Block)ModBlocks.VAULT_PORTAL, Vault.id("vault_portal"));
        registerBlock(event, (Block)ModBlocks.FINAL_VAULT_PORTAL, Vault.id("final_vault_portal"));
        registerBlock(event, (Block)ModBlocks.OTHER_SIDE_PORTAL, Vault.id("other_side_portal"));
        registerBlock(event, ModBlocks.VAULT_ALTAR, Vault.id("vault_altar"));
        registerBlock(event, (Block)ModBlocks.ALEXANDRITE_ORE, Vault.id("ore_alexandrite"));
        registerBlock(event, (Block)ModBlocks.BENITOITE_ORE, Vault.id("ore_benitoite"));
        registerBlock(event, (Block)ModBlocks.LARIMAR_ORE, Vault.id("ore_larimar"));
        registerBlock(event, (Block)ModBlocks.BLACK_OPAL_ORE, Vault.id("ore_black_opal"));
        registerBlock(event, (Block)ModBlocks.PAINITE_ORE, Vault.id("ore_painite"));
        registerBlock(event, (Block)ModBlocks.ISKALLIUM_ORE, Vault.id("ore_iskallium"));
        registerBlock(event, (Block)ModBlocks.GORGINITE_ORE, Vault.id("ore_gorginite"));
        registerBlock(event, (Block)ModBlocks.SPARKLETINE_ORE, Vault.id("ore_sparkletine"));
        registerBlock(event, (Block)ModBlocks.WUTODIE_ORE, Vault.id("ore_wutodie"));
        registerBlock(event, (Block)ModBlocks.ASHIUM_ORE, Vault.id("ore_ashium"));
        registerBlock(event, (Block)ModBlocks.BOMIGNITE_ORE, Vault.id("ore_bomignite"));
        registerBlock(event, (Block)ModBlocks.FUNSOIDE_ORE, Vault.id("ore_funsoide"));
        registerBlock(event, (Block)ModBlocks.TUBIUM_ORE, Vault.id("ore_tubium"));
        registerBlock(event, (Block)ModBlocks.UPALINE_ORE, Vault.id("ore_upaline"));
        registerBlock(event, (Block)ModBlocks.PUFFIUM_ORE, Vault.id("ore_puffium"));
        registerBlock(event, (Block)ModBlocks.ECHO_ORE, Vault.id("ore_echo"));
        registerBlock(event, ModBlocks.UNKNOWN_ORE, Vault.id("ore_unknown"));
        registerBlock(event, (Block)ModBlocks.VAULT_ROCK_ORE, Vault.id("ore_vault_rock"));
        registerBlock(event, (Block)ModBlocks.ISKALLIUM_DOOR, Vault.id("door_iskallium"));
        registerBlock(event, (Block)ModBlocks.GORGINITE_DOOR, Vault.id("door_gorginite"));
        registerBlock(event, (Block)ModBlocks.SPARKLETINE_DOOR, Vault.id("door_sparkletine"));
        registerBlock(event, (Block)ModBlocks.ASHIUM_DOOR, Vault.id("door_ashium"));
        registerBlock(event, (Block)ModBlocks.BOMIGNITE_DOOR, Vault.id("door_bomignite"));
        registerBlock(event, (Block)ModBlocks.FUNSOIDE_DOOR, Vault.id("door_funsoide"));
        registerBlock(event, (Block)ModBlocks.TUBIUM_DOOR, Vault.id("door_tubium"));
        registerBlock(event, (Block)ModBlocks.UPALINE_DOOR, Vault.id("door_upaline"));
        registerBlock(event, (Block)ModBlocks.PUFFIUM_DOOR, Vault.id("door_puffium"));
        registerBlock(event, (Block)ModBlocks.UNKNOWN_DOOR, Vault.id("door_unknown"));
        registerBlock(event, ModBlocks.VAULT_RUNE_BLOCK, Vault.id("vault_rune_block"));
        registerBlock(event, ModBlocks.VAULT_ARTIFACT, Vault.id("vault_artifact"));
        registerBlock(event, ModBlocks.VAULT_CRATE, Vault.id("vault_crate"));
        registerBlock(event, ModBlocks.VAULT_CRATE_CAKE, Vault.id("vault_crate_cake"));
        registerBlock(event, ModBlocks.VAULT_CRATE_ARENA, Vault.id("vault_crate_arena"));
        registerBlock(event, ModBlocks.VAULT_CRATE_SCAVENGER, Vault.id("vault_crate_scavenger"));
        registerBlock(event, ModBlocks.OBELISK, Vault.id("obelisk"));
        registerBlock(event, ModBlocks.VENDING_MACHINE, Vault.id("vending_machine"));
        registerBlock(event, ModBlocks.ADVANCED_VENDING_MACHINE, Vault.id("advanced_vending_machine"));
        registerBlock(event, ModBlocks.VAULT_BEDROCK, Vault.id("vault_bedrock"));
        registerBlock(event, ModBlocks.VAULT_STONE, Vault.id("vault_stone"));
        registerBlock(event, (Block)ModBlocks.VAULT_GLASS, Vault.id("vault_glass"));
        registerBlock(event, ModBlocks.RELIC_STATUE, Vault.id("relic_statue"));
        registerBlock(event, ModBlocks.GIFT_NORMAL_STATUE, Vault.id("gift_normal_statue"));
        registerBlock(event, ModBlocks.GIFT_MEGA_STATUE, Vault.id("gift_mega_statue"));
        registerBlock(event, ModBlocks.BOW_HAT, Vault.id("bow_hat"));
        registerBlock(event, ModBlocks.STATUE_DRAGON_HEAD, Vault.id("statue_dragon"));
        registerBlock(event, ModBlocks.VAULT_PLAYER_LOOT_STATUE, Vault.id("vault_player_loot_statue"));
        registerBlock(event, ModBlocks.CRYO_CHAMBER, Vault.id("cryo_chamber"));
        registerBlock(event, (Block)ModBlocks.KEY_PRESS, Vault.id("key_press"));
        registerBlock(event, ModBlocks.VAULT_DIAMOND_BLOCK, Vault.id("vault_diamond_block"));
        registerBlock(event, ModBlocks.MAZE_BLOCK, Vault.id("maze_block"));
        registerBlock(event, ModBlocks.PUZZLE_RUNE_BLOCK, Vault.id("puzzle_rune_block"));
        registerBlock(event, ModBlocks.YELLOW_PUZZLE_CONCRETE, Vault.id("yellow_puzzle_concrete"));
        registerBlock(event, ModBlocks.PINK_PUZZLE_CONCRETE, Vault.id("pink_puzzle_concrete"));
        registerBlock(event, ModBlocks.GREEN_PUZZLE_CONCRETE, Vault.id("green_puzzle_concrete"));
        registerBlock(event, ModBlocks.BLUE_PUZZLE_CONCRETE, Vault.id("blue_puzzle_concrete"));
        registerBlock(event, ModBlocks.OMEGA_STATUE, Vault.id("omega_statue"));
        registerBlock(event, ModBlocks.OMEGA_STATUE_VARIANT, Vault.id("omega_statue_variant"));
        registerBlock(event, ModBlocks.TROPHY_STATUE, Vault.id("trophy_statue"));
        registerBlock(event, ModBlocks.TRANSMOG_TABLE, Vault.id("transmog_table"));
        registerBlock(event, ModBlocks.VAULT_LOOT_RICHITY, Vault.id("vault_richity"));
        registerBlock(event, ModBlocks.VAULT_LOOT_RESOURCE, Vault.id("vault_resource"));
        registerBlock(event, ModBlocks.VAULT_LOOT_MISC, Vault.id("vault_misc"));
        registerBlock(event, ModBlocks.UNKNOWN_VAULT_CHEST, Vault.id("unknown_vault_chest"));
        registerBlock(event, ModBlocks.UNKNOWN_TREASURE_CHEST, Vault.id("unknown_treasure_chest"));
        registerBlock(event, ModBlocks.UNKNOWN_VAULT_OBJECTIVE, Vault.id("unknown_vault_objective"));
        registerBlock(event, ModBlocks.VAULT_CHEST, Vault.id("vault_chest"));
        registerBlock(event, ModBlocks.VAULT_TREASURE_CHEST, Vault.id("vault_treasure_chest"));
        registerBlock(event, ModBlocks.VAULT_ALTAR_CHEST, Vault.id("vault_altar_chest"));
        registerBlock(event, ModBlocks.VAULT_COOP_CHEST, Vault.id("vault_coop_chest"));
        registerBlock(event, ModBlocks.VAULT_BONUS_CHEST, Vault.id("vault_bonus_chest"));
        registerBlock(event, ModBlocks.XP_ALTAR, Vault.id("xp_altar"));
        registerBlock(event, ModBlocks.BLOOD_ALTAR, Vault.id("blood_altar"));
        registerBlock(event, ModBlocks.TIME_ALTAR, Vault.id("time_altar"));
        registerBlock(event, ModBlocks.SOUL_ALTAR, Vault.id("soul_altar"));
        registerBlock(event, (Block)ModBlocks.STATUE_CAULDRON, Vault.id("statue_cauldron"));
        registerBlock(event, (Block)ModBlocks.VOID_LIQUID_BLOCK, Vault.id("void_liquid"));
        registerBlock(event, (Block)ModBlocks.SCAVENGER_CHEST, Vault.id("scavenger_chest"));
        registerBlock(event, (Block)ModBlocks.SCAVENGER_TREASURE, Vault.id("scavenger_treasure"));
        registerBlock(event, ModBlocks.STABILIZER, Vault.id("stabilizer"));
        registerBlock(event, ModBlocks.CATALYST_DECRYPTION_TABLE, Vault.id("catalyst_decryption_table"));
        registerBlock(event, (Block)ModBlocks.ETCHING_CONTROLLER_BLOCK, Vault.id("etching_vendor_controller"));
        registerBlock(event, ModBlocks.VAULT_CHARM_CONTROLLER_BLOCK, Vault.id("vault_charm_controller"));
        registerBlock(event, ModBlocks.RAID_CONTROLLER_BLOCK, Vault.id("raid_controller"));
    }
    
    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
        registerTileEntity(event, ModBlocks.VAULT_ALTAR_TILE_ENTITY, Vault.id("vault_altar_tile_entity"));
        registerTileEntity(event, ModBlocks.VAULT_RUNE_TILE_ENTITY, Vault.id("vault_rune_tile_entity"));
        registerTileEntity(event, ModBlocks.VAULT_CRATE_TILE_ENTITY, Vault.id("vault_crate_tile_entity"));
        registerTileEntity(event, ModBlocks.VAULT_PORTAL_TILE_ENTITY, Vault.id("vault_portal_tile_entity"));
        registerTileEntity(event, ModBlocks.OTHER_SIDE_PORTAL_TILE_ENTITY, Vault.id("other_side_portal_tile_entity"));
        registerTileEntity(event, ModBlocks.VENDING_MACHINE_TILE_ENTITY, Vault.id("vending_machine_tile_entity"));
        registerTileEntity(event, ModBlocks.ADVANCED_VENDING_MACHINE_TILE_ENTITY, Vault.id("advanced_vending_machine_tile_entity"));
        registerTileEntity(event, ModBlocks.RELIC_STATUE_TILE_ENTITY, Vault.id("relic_statue_tile_entity"));
        registerTileEntity(event, ModBlocks.LOOT_STATUE_TILE_ENTITY, Vault.id("loot_statue_tile_entity"));
        registerTileEntity(event, ModBlocks.TROPHY_STATUE_TILE_ENTITY, Vault.id("trophy_statue_tile_entity"));
        registerTileEntity(event, ModBlocks.CRYO_CHAMBER_TILE_ENTITY, Vault.id("cryo_chamber_tile_entity"));
        registerTileEntity(event, ModBlocks.ANCIENT_CRYO_CHAMBER_TILE_ENTITY, Vault.id("ancient_cryo_chamber_tile_entity"));
        registerTileEntity(event, ModBlocks.VAULT_DOOR_TILE_ENTITY, Vault.id("vault_door_tile_entity"));
        registerTileEntity(event, ModBlocks.VAULT_LOOTABLE_TILE_ENTITY, Vault.id("vault_lootable_tile_entity"));
        registerTileEntity(event, ModBlocks.VAULT_CHEST_TILE_ENTITY, Vault.id("vault_chest_tile_entity"));
        registerTileEntity(event, ModBlocks.VAULT_TREASURE_CHEST_TILE_ENTITY, Vault.id("vault_treasure_chest_tile_entity"));
        registerTileEntity(event, ModBlocks.XP_ALTAR_TILE_ENTITY, Vault.id("xp_altar_tile_entity"));
        registerTileEntity(event, ModBlocks.BLOOD_ALTAR_TILE_ENTITY, Vault.id("blood_altar_tile_entity"));
        registerTileEntity(event, ModBlocks.TIME_ALTAR_TILE_ENTITY, Vault.id("time_altar_tile_entity"));
        registerTileEntity(event, ModBlocks.SOUL_ALTAR_TILE_ENTITY, Vault.id("soul_altar_tile_entity"));
        registerTileEntity(event, ModBlocks.STATUE_CAULDRON_TILE_ENTITY, Vault.id("statue_cauldron_tile_entity"));
        registerTileEntity(event, ModBlocks.OBELISK_TILE_ENTITY, Vault.id("obelisk_tile_entity"));
        registerTileEntity(event, ModBlocks.SCAVENGER_CHEST_TILE_ENTITY, Vault.id("scavenger_chest_tile_entity"));
        registerTileEntity(event, ModBlocks.SCAVENGER_TREASURE_TILE_ENTITY, Vault.id("scavenger_treasure_tile_entity"));
        registerTileEntity(event, ModBlocks.STABILIZER_TILE_ENTITY, Vault.id("stabilizer_tile_entity"));
        registerTileEntity(event, ModBlocks.CATALYST_DECRYPTION_TABLE_TILE_ENTITY, Vault.id("catalyst_decryption_table_tile_entity"));
        registerTileEntity(event, ModBlocks.ETCHING_CONTROLLER_TILE_ENTITY, Vault.id("etching_vendor_controller_tile_entity"));
        registerTileEntity(event, ModBlocks.VAULT_CHARM_CONTROLLER_TILE_ENTITY, Vault.id("vault_charm_controller_tile_entity"));
        registerTileEntity(event, ModBlocks.RAID_CONTROLLER_TILE_ENTITY, Vault.id("raid_controller_tile_entity"));
    }
    
    public static void registerTileEntityRenderers() {
        ClientRegistry.bindTileEntityRenderer(ModBlocks.VAULT_ALTAR_TILE_ENTITY, VaultAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.VAULT_RUNE_TILE_ENTITY, VaultRuneRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.VENDING_MACHINE_TILE_ENTITY, VendingMachineRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.ADVANCED_VENDING_MACHINE_TILE_ENTITY, AdvancedVendingRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.RELIC_STATUE_TILE_ENTITY, RelicStatueRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.LOOT_STATUE_TILE_ENTITY, LootStatueRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.TROPHY_STATUE_TILE_ENTITY, LootStatueRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.CRYO_CHAMBER_TILE_ENTITY, CryoChamberRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.ANCIENT_CRYO_CHAMBER_TILE_ENTITY, CryoChamberRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.VAULT_CHEST_TILE_ENTITY, VaultChestRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.VAULT_TREASURE_CHEST_TILE_ENTITY, VaultChestRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.XP_ALTAR_TILE_ENTITY, FillableAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.BLOOD_ALTAR_TILE_ENTITY, FillableAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.TIME_ALTAR_TILE_ENTITY, FillableAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.SOUL_ALTAR_TILE_ENTITY, FillableAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.STATUE_CAULDRON_TILE_ENTITY, StatueCauldronRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.SCAVENGER_CHEST_TILE_ENTITY, ScavengerChestRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.RAID_CONTROLLER_TILE_ENTITY, VaultRaidControllerRenderer::new);
    }
    
    public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        registerBlockItem(event, ModBlocks.VAULT_PORTAL);
        registerBlockItem(event, ModBlocks.FINAL_VAULT_PORTAL);
        registerBlockItem(event, ModBlocks.OTHER_SIDE_PORTAL);
        registerBlockItem(event, ModBlocks.VAULT_ALTAR, 1);
        registerBlockItem(event, (Block)ModBlocks.ALEXANDRITE_ORE);
        registerBlockItem(event, (Block)ModBlocks.BENITOITE_ORE);
        registerBlockItem(event, (Block)ModBlocks.LARIMAR_ORE);
        registerBlockItem(event, (Block)ModBlocks.BLACK_OPAL_ORE);
        registerBlockItem(event, (Block)ModBlocks.PAINITE_ORE);
        registerBlockItem(event, (Block)ModBlocks.ISKALLIUM_ORE);
        registerBlockItem(event, (Block)ModBlocks.GORGINITE_ORE);
        registerBlockItem(event, (Block)ModBlocks.SPARKLETINE_ORE);
        registerBlockItem(event, (Block)ModBlocks.WUTODIE_ORE);
        registerBlockItem(event, (Block)ModBlocks.ASHIUM_ORE);
        registerBlockItem(event, (Block)ModBlocks.BOMIGNITE_ORE);
        registerBlockItem(event, (Block)ModBlocks.FUNSOIDE_ORE);
        registerBlockItem(event, (Block)ModBlocks.TUBIUM_ORE);
        registerBlockItem(event, (Block)ModBlocks.UPALINE_ORE);
        registerBlockItem(event, (Block)ModBlocks.PUFFIUM_ORE);
        registerBlockItem(event, (Block)ModBlocks.ECHO_ORE);
        registerBlockItem(event, ModBlocks.UNKNOWN_ORE);
        registerBlockItem(event, (Block)ModBlocks.VAULT_ROCK_ORE);
        registerTallBlockItem(event, (Block)ModBlocks.ISKALLIUM_DOOR);
        registerTallBlockItem(event, (Block)ModBlocks.GORGINITE_DOOR);
        registerTallBlockItem(event, (Block)ModBlocks.SPARKLETINE_DOOR);
        registerTallBlockItem(event, (Block)ModBlocks.ASHIUM_DOOR);
        registerTallBlockItem(event, (Block)ModBlocks.BOMIGNITE_DOOR);
        registerTallBlockItem(event, (Block)ModBlocks.FUNSOIDE_DOOR);
        registerTallBlockItem(event, (Block)ModBlocks.TUBIUM_DOOR);
        registerTallBlockItem(event, (Block)ModBlocks.UPALINE_DOOR);
        registerTallBlockItem(event, (Block)ModBlocks.PUFFIUM_DOOR);
        registerTallBlockItem(event, (Block)ModBlocks.UNKNOWN_DOOR);
        registerBlockItem(event, ModBlocks.VAULT_RUNE_BLOCK);
        registerBlockItem(event, ModBlocks.VAULT_ARTIFACT, 1);
        registerBlockItem(event, ModBlocks.VAULT_CRATE, 1, Item.Properties::fireResistant);
        registerBlockItem(event, ModBlocks.VAULT_CRATE_CAKE, 1, Item.Properties::fireResistant);
        registerBlockItem(event, ModBlocks.VAULT_CRATE_ARENA, 1, Item.Properties::fireResistant);
        registerBlockItem(event, ModBlocks.VAULT_CRATE_SCAVENGER, 1, Item.Properties::fireResistant);
        registerBlockItem(event, ModBlocks.OBELISK, 1);
        registerBlockItem(event, ModBlocks.VENDING_MACHINE, ModBlocks.VENDING_MACHINE_BLOCK_ITEM);
        registerBlockItem(event, ModBlocks.ADVANCED_VENDING_MACHINE, ModBlocks.ADVANCED_VENDING_BLOCK_ITEM);
        registerBlockItem(event, ModBlocks.VAULT_BEDROCK);
        registerBlockItem(event, ModBlocks.VAULT_STONE);
        registerBlockItem(event, (Block)ModBlocks.VAULT_GLASS);
        registerBlockItem(event, ModBlocks.RELIC_STATUE, ModBlocks.RELIC_STATUE_BLOCK_ITEM);
        registerBlockItem(event, ModBlocks.GIFT_NORMAL_STATUE, ModBlocks.GIFT_NORMAL_STATUE_BLOCK_ITEM);
        registerBlockItem(event, ModBlocks.GIFT_MEGA_STATUE, ModBlocks.GIFT_MEGA_STATUE_BLOCK_ITEM);
        registerBlockItem(event, ModBlocks.BOW_HAT, 1);
        registerBlockItem(event, ModBlocks.STATUE_DRAGON_HEAD, 1);
        registerBlockItem(event, ModBlocks.VAULT_PLAYER_LOOT_STATUE, ModBlocks.VAULT_PLAYER_LOOT_STATUE_BLOCK_ITEM);
        registerBlockItem(event, ModBlocks.CRYO_CHAMBER);
        registerBlockItem(event, (Block)ModBlocks.KEY_PRESS);
        registerBlockItem(event, ModBlocks.VAULT_DIAMOND_BLOCK);
        registerBlockItem(event, ModBlocks.PUZZLE_RUNE_BLOCK, ModBlocks.PUZZLE_RUNE_BLOCK_ITEM);
        registerBlockItem(event, ModBlocks.YELLOW_PUZZLE_CONCRETE);
        registerBlockItem(event, ModBlocks.PINK_PUZZLE_CONCRETE);
        registerBlockItem(event, ModBlocks.GREEN_PUZZLE_CONCRETE);
        registerBlockItem(event, ModBlocks.BLUE_PUZZLE_CONCRETE);
        registerBlockItem(event, ModBlocks.OMEGA_STATUE, ModBlocks.OMEGA_STATUE_BLOCK_ITEM);
        registerBlockItem(event, ModBlocks.OMEGA_STATUE_VARIANT, ModBlocks.OMEGA_STATUE_VARIANT_BLOCK_ITEM);
        registerBlockItem(event, ModBlocks.TROPHY_STATUE, ModBlocks.TROPHY_STATUE_BLOCK_ITEM);
        registerBlockItem(event, ModBlocks.TRANSMOG_TABLE);
        registerBlockItem(event, ModBlocks.VAULT_LOOT_RICHITY);
        registerBlockItem(event, ModBlocks.VAULT_LOOT_RESOURCE);
        registerBlockItem(event, ModBlocks.VAULT_LOOT_MISC);
        registerBlockItem(event, ModBlocks.UNKNOWN_VAULT_CHEST);
        registerBlockItem(event, ModBlocks.UNKNOWN_TREASURE_CHEST);
        registerBlockItem(event, ModBlocks.UNKNOWN_VAULT_OBJECTIVE);
        registerBlockItem(event, ModBlocks.VAULT_CHEST, ModBlocks.VAULT_CHEST_ITEM);
        registerBlockItem(event, ModBlocks.VAULT_TREASURE_CHEST, ModBlocks.VAULT_TREASURE_CHEST_ITEM);
        registerBlockItem(event, ModBlocks.VAULT_ALTAR_CHEST, ModBlocks.VAULT_ALTAR_CHEST_ITEM);
        registerBlockItem(event, ModBlocks.VAULT_COOP_CHEST, ModBlocks.VAULT_COOP_CHEST_ITEM);
        registerBlockItem(event, ModBlocks.VAULT_BONUS_CHEST, ModBlocks.VAULT_BONUS_CHEST_ITEM);
        registerBlockItem(event, ModBlocks.XP_ALTAR);
        registerBlockItem(event, ModBlocks.BLOOD_ALTAR);
        registerBlockItem(event, ModBlocks.TIME_ALTAR);
        registerBlockItem(event, ModBlocks.SOUL_ALTAR);
        registerBlockItem(event, (Block)ModBlocks.STATUE_CAULDRON, 1);
        registerBlockItem(event, (Block)ModBlocks.SCAVENGER_CHEST, ModBlocks.SCAVENGER_CHEST_ITEM);
        registerBlockItem(event, (Block)ModBlocks.SCAVENGER_TREASURE);
        registerBlockItem(event, ModBlocks.STABILIZER);
        registerBlockItem(event, ModBlocks.CATALYST_DECRYPTION_TABLE);
        registerBlockItem(event, (Block)ModBlocks.ETCHING_CONTROLLER_BLOCK);
        registerBlockItem(event, ModBlocks.VAULT_CHARM_CONTROLLER_BLOCK);
        registerBlockItem(event, ModBlocks.RAID_CONTROLLER_BLOCK);
    }
    
    private static void registerBlock(final RegistryEvent.Register<Block> event, final Block block, final ResourceLocation id) {
        block.setRegistryName(id);
        event.getRegistry().register(block);
    }
    
    private static <T extends TileEntity> void registerTileEntity(final RegistryEvent.Register<TileEntityType<?>> event, final TileEntityType<?> type, final ResourceLocation id) {
        type.setRegistryName(id);
        event.getRegistry().register(type);
    }
    
    private static void registerBlockItemWithEffect(final RegistryEvent.Register<Item> event, final Block block, final int maxStackSize, final Consumer<Item.Properties> adjustProperties) {
        final Item.Properties properties = new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(maxStackSize);
        adjustProperties.accept(properties);
        final BlockItem blockItem = new BlockItem(block, properties) {
            public boolean isFoil(final ItemStack stack) {
                return true;
            }
        };
        registerBlockItem(event, block, blockItem);
    }
    
    private static void registerBlockItem(final RegistryEvent.Register<Item> event, final Block block) {
        registerBlockItem(event, block, 64);
    }
    
    private static void registerBlockItem(final RegistryEvent.Register<Item> event, final Block block, final int maxStackSize) {
        registerBlockItem(event, block, maxStackSize, properties -> {});
    }
    
    private static void registerBlockItem(final RegistryEvent.Register<Item> event, final Block block, final int maxStackSize, final Consumer<Item.Properties> adjustProperties) {
        final Item.Properties properties = new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(maxStackSize);
        adjustProperties.accept(properties);
        registerBlockItem(event, block, new BlockItem(block, properties));
    }
    
    private static void registerBlockItem(final RegistryEvent.Register<Item> event, final Block block, final BlockItem blockItem) {
        blockItem.setRegistryName(block.getRegistryName());
        event.getRegistry().register(blockItem);
    }
    
    private static void registerTallBlockItem(final RegistryEvent.Register<Item> event, final Block block) {
        final TallBlockItem tallBlockItem = new TallBlockItem(block, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(64));
        tallBlockItem.setRegistryName(block.getRegistryName());
        event.getRegistry().register(tallBlockItem);
    }
    
    static {
        VAULT_PORTAL = new VaultPortalBlock();
        FINAL_VAULT_PORTAL = new FinalVaultPortalBlock();
        OTHER_SIDE_PORTAL = new OtherSidePortalBlock();
        VAULT_ALTAR = new VaultAltarBlock();
        ALEXANDRITE_ORE = new VaultOreBlock(ModItems.ALEXANDRITE_GEM);
        BENITOITE_ORE = new VaultOreBlock(ModItems.BENITOITE_GEM);
        LARIMAR_ORE = new VaultOreBlock(ModItems.LARIMAR_GEM);
        BLACK_OPAL_ORE = new VaultOreBlock(ModItems.BLACK_OPAL_GEM);
        PAINITE_ORE = new VaultOreBlock(ModItems.PAINITE_GEM);
        ISKALLIUM_ORE = new VaultOreBlock(ModItems.ISKALLIUM_GEM);
        GORGINITE_ORE = new VaultOreBlock(ModItems.GORGINITE_GEM);
        SPARKLETINE_ORE = new VaultOreBlock(ModItems.SPARKLETINE_GEM);
        WUTODIE_ORE = new VaultOreBlock(ModItems.WUTODIE_GEM);
        ASHIUM_ORE = new VaultOreBlock(ModItems.ASHIUM_GEM);
        BOMIGNITE_ORE = new VaultOreBlock(ModItems.BOMIGNITE_GEM);
        FUNSOIDE_ORE = new VaultOreBlock(ModItems.FUNSOIDE_GEM);
        TUBIUM_ORE = new VaultOreBlock(ModItems.TUBIUM_GEM);
        UPALINE_ORE = new VaultOreBlock(ModItems.UPALINE_GEM);
        PUFFIUM_ORE = new VaultOreBlock(ModItems.PUFFIUM_GEM);
        ECHO_ORE = new VaultOreBlock(ModItems.ECHO_GEM);
        UNKNOWN_ORE = new VaultLootableBlock(VaultLootableBlock.Type.ORE);
        VAULT_ROCK_ORE = new VaultRockBlock();
        ISKALLIUM_DOOR = new VaultDoorBlock(ModItems.ISKALLIUM_KEY);
        GORGINITE_DOOR = new VaultDoorBlock(ModItems.GORGINITE_KEY);
        SPARKLETINE_DOOR = new VaultDoorBlock(ModItems.SPARKLETINE_KEY);
        ASHIUM_DOOR = new VaultDoorBlock(ModItems.ASHIUM_KEY);
        BOMIGNITE_DOOR = new VaultDoorBlock(ModItems.BOMIGNITE_KEY);
        FUNSOIDE_DOOR = new VaultDoorBlock(ModItems.FUNSOIDE_KEY);
        TUBIUM_DOOR = new VaultDoorBlock(ModItems.TUBIUM_KEY);
        UPALINE_DOOR = new VaultDoorBlock(ModItems.UPALINE_KEY);
        PUFFIUM_DOOR = new VaultDoorBlock(ModItems.PUFFIUM_KEY);
        UNKNOWN_DOOR = new UnknownVaultDoorBlock();
        VAULT_RUNE_BLOCK = new VaultRuneBlock();
        VAULT_ARTIFACT = new VaultArtifactBlock();
        VAULT_CRATE = new VaultCrateBlock();
        VAULT_CRATE_CAKE = new VaultCrateBlock();
        VAULT_CRATE_ARENA = new VaultCrateBlock();
        VAULT_CRATE_SCAVENGER = new VaultCrateBlock();
        OBELISK = new ObeliskBlock();
        VENDING_MACHINE = new VendingMachineBlock();
        ADVANCED_VENDING_MACHINE = new AdvancedVendingBlock();
        VAULT_BEDROCK = new VaultBedrockBlock();
        VAULT_STONE = new Block(AbstractBlock.Properties.of(Material.STONE).strength(1.5f, 6.0f).isValidSpawn((a, b, c, d) -> false));
        VAULT_GLASS = new GlassBlock(AbstractBlock.Properties.copy((AbstractBlock)Blocks.GLASS).strength(-1.0f, 3600000.0f));
        RELIC_STATUE = new RelicStatueBlock();
        GIFT_NORMAL_STATUE = new LootStatueBlock(StatueType.GIFT_NORMAL);
        GIFT_MEGA_STATUE = new LootStatueBlock(StatueType.GIFT_MEGA);
        BOW_HAT = new BowHatBlock();
        STATUE_DRAGON_HEAD = new StatueDragonHeadBlock();
        VAULT_PLAYER_LOOT_STATUE = new LootStatueBlock(StatueType.VAULT_BOSS);
        CRYO_CHAMBER = new CryoChamberBlock();
        KEY_PRESS = new KeyPressBlock();
        VAULT_DIAMOND_BLOCK = new Block(AbstractBlock.Properties.copy((AbstractBlock)Blocks.DIAMOND_BLOCK));
        MAZE_BLOCK = new MazeBlock();
        PUZZLE_RUNE_BLOCK = new PuzzleRuneBlock();
        YELLOW_PUZZLE_CONCRETE = new Block(AbstractBlock.Properties.copy((AbstractBlock)Blocks.BLACK_CONCRETE));
        PINK_PUZZLE_CONCRETE = new Block(AbstractBlock.Properties.copy((AbstractBlock)Blocks.BLACK_CONCRETE));
        GREEN_PUZZLE_CONCRETE = new Block(AbstractBlock.Properties.copy((AbstractBlock)Blocks.BLACK_CONCRETE));
        BLUE_PUZZLE_CONCRETE = new Block(AbstractBlock.Properties.copy((AbstractBlock)Blocks.BLACK_CONCRETE));
        OMEGA_STATUE = new OmegaStatueBlock();
        OMEGA_STATUE_VARIANT = new OmegaVariantStatueBlock();
        TROPHY_STATUE = new TrophyBlock();
        TRANSMOG_TABLE = new TransmogTableBlock();
        VAULT_LOOT_RICHITY = new VaultLootableBlock(VaultLootableBlock.Type.RICHITY);
        VAULT_LOOT_RESOURCE = new VaultLootableBlock(VaultLootableBlock.Type.RESOURCE);
        VAULT_LOOT_MISC = new VaultLootableBlock(VaultLootableBlock.Type.MISC);
        UNKNOWN_VAULT_CHEST = new VaultLootableBlock(VaultLootableBlock.Type.VAULT_CHEST);
        UNKNOWN_TREASURE_CHEST = new VaultLootableBlock(VaultLootableBlock.Type.VAULT_TREASURE);
        UNKNOWN_VAULT_OBJECTIVE = new VaultLootableBlock(VaultLootableBlock.Type.VAULT_OBJECTIVE);
        VAULT_CHEST = (Block)new VaultChestBlock(AbstractBlock.Properties.copy((AbstractBlock)Blocks.CHEST).strength(40.0f, 5.0f));
        VAULT_TREASURE_CHEST = (Block)new VaultTreasureChestBlock(AbstractBlock.Properties.copy((AbstractBlock)Blocks.CHEST).strength(-1.0f, 3600000.0f));
        VAULT_ALTAR_CHEST = (Block)new VaultChestBlock(AbstractBlock.Properties.copy((AbstractBlock)Blocks.CHEST).strength(-1.0f, 3600000.0f));
        VAULT_COOP_CHEST = (Block)new VaultChestBlock(AbstractBlock.Properties.of(Material.STONE).strength(2.0f, 3600000.0f).sound(SoundType.STONE));
        VAULT_BONUS_CHEST = (Block)new VaultChestBlock(AbstractBlock.Properties.of(Material.STONE).strength(2.0f, 3600000.0f).sound(SoundType.STONE));
        XP_ALTAR = new XPAltarBlock();
        BLOOD_ALTAR = new BloodAltarBlock();
        TIME_ALTAR = new TimeAltarBlock();
        SOUL_ALTAR = new SoulAltarBlock();
        STATUE_CAULDRON = new StatueCauldronBlock();
        SCAVENGER_CHEST = new ScavengerChestBlock(AbstractBlock.Properties.copy((AbstractBlock)Blocks.CHEST).strength(-1.0f, 3600000.0f));
        SCAVENGER_TREASURE = new ScavengerTreasureBlock();
        STABILIZER = new StabilizerBlock();
        CATALYST_DECRYPTION_TABLE = new CatalystDecryptionTableBlock();
        ETCHING_CONTROLLER_BLOCK = new EtchingVendorControllerBlock();
        VAULT_CHARM_CONTROLLER_BLOCK = new VaultCharmControllerBlock();
        RAID_CONTROLLER_BLOCK = new VaultRaidControllerBlock();
        VOID_LIQUID_BLOCK = new VoidFluidBlock((Supplier<? extends VoidFluid>)ModFluids.VOID_LIQUID, AbstractBlock.Properties.of(Material.WATER, MaterialColor.COLOR_BLACK).noCollission().randomTicks().strength(100.0f).lightLevel(state -> 15).noDrops());
        RELIC_STATUE_BLOCK_ITEM = new RelicStatueBlockItem();
        GIFT_NORMAL_STATUE_BLOCK_ITEM = new LootStatueBlockItem(ModBlocks.GIFT_NORMAL_STATUE, StatueType.GIFT_NORMAL);
        GIFT_MEGA_STATUE_BLOCK_ITEM = new LootStatueBlockItem(ModBlocks.GIFT_MEGA_STATUE, StatueType.GIFT_MEGA);
        VAULT_PLAYER_LOOT_STATUE_BLOCK_ITEM = new LootStatueBlockItem(ModBlocks.VAULT_PLAYER_LOOT_STATUE, StatueType.VAULT_BOSS);
        OMEGA_STATUE_BLOCK_ITEM = new LootStatueBlockItem(ModBlocks.OMEGA_STATUE, StatueType.OMEGA);
        OMEGA_STATUE_VARIANT_BLOCK_ITEM = new LootStatueBlockItem(ModBlocks.OMEGA_STATUE_VARIANT, StatueType.OMEGA_VARIANT);
        TROPHY_STATUE_BLOCK_ITEM = new TrophyStatueBlockItem(ModBlocks.TROPHY_STATUE);
        VENDING_MACHINE_BLOCK_ITEM = new VendingMachineBlockItem(ModBlocks.VENDING_MACHINE);
        ADVANCED_VENDING_BLOCK_ITEM = new AdvancedVendingMachineBlockItem(ModBlocks.ADVANCED_VENDING_MACHINE);
        PUZZLE_RUNE_BLOCK_ITEM = new PuzzleRuneBlock.Item(ModBlocks.PUZZLE_RUNE_BLOCK, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(1));
        VAULT_CHEST_ITEM = new BlockItem(ModBlocks.VAULT_CHEST, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).setISTER(() -> VaultStackRendererProvider.INSTANCE));
        VAULT_TREASURE_CHEST_ITEM = new BlockItem(ModBlocks.VAULT_TREASURE_CHEST, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).setISTER(() -> VaultStackRendererProvider.INSTANCE));
        VAULT_ALTAR_CHEST_ITEM = new BlockItem(ModBlocks.VAULT_ALTAR_CHEST, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).setISTER(() -> VaultStackRendererProvider.INSTANCE));
        VAULT_COOP_CHEST_ITEM = new BlockItem(ModBlocks.VAULT_COOP_CHEST, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).setISTER(() -> VaultStackRendererProvider.INSTANCE));
        VAULT_BONUS_CHEST_ITEM = new BlockItem(ModBlocks.VAULT_BONUS_CHEST, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).setISTER(() -> VaultStackRendererProvider.INSTANCE));
        SCAVENGER_CHEST_ITEM = new BlockItem((Block)ModBlocks.SCAVENGER_CHEST, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).setISTER(() -> VaultStackRendererProvider.INSTANCE));
        VAULT_ALTAR_TILE_ENTITY = TileEntityType.Builder.of((Supplier)VaultAltarTileEntity::new, new Block[] { ModBlocks.VAULT_ALTAR }).build((Type)null);
        VAULT_RUNE_TILE_ENTITY = TileEntityType.Builder.of((Supplier)VaultRuneTileEntity::new, new Block[] { ModBlocks.VAULT_RUNE_BLOCK }).build((Type)null);
        VAULT_CRATE_TILE_ENTITY = TileEntityType.Builder.of((Supplier)VaultCrateTileEntity::new, new Block[] { ModBlocks.VAULT_CRATE, ModBlocks.VAULT_CRATE_CAKE, ModBlocks.VAULT_CRATE_ARENA, ModBlocks.VAULT_CRATE_SCAVENGER }).build((Type)null);
        VAULT_PORTAL_TILE_ENTITY = TileEntityType.Builder.of((Supplier)VaultPortalTileEntity::new, new Block[] { (Block)ModBlocks.VAULT_PORTAL }).build((Type)null);
        OTHER_SIDE_PORTAL_TILE_ENTITY = TileEntityType.Builder.of((Supplier)OtherSidePortalTileEntity::new, new Block[] { (Block)ModBlocks.OTHER_SIDE_PORTAL }).build((Type)null);
        VENDING_MACHINE_TILE_ENTITY = TileEntityType.Builder.of((Supplier)VendingMachineTileEntity::new, new Block[] { ModBlocks.VENDING_MACHINE }).build((Type)null);
        ADVANCED_VENDING_MACHINE_TILE_ENTITY = TileEntityType.Builder.of((Supplier)AdvancedVendingTileEntity::new, new Block[] { ModBlocks.ADVANCED_VENDING_MACHINE }).build((Type)null);
        RELIC_STATUE_TILE_ENTITY = TileEntityType.Builder.of((Supplier)RelicStatueTileEntity::new, new Block[] { ModBlocks.RELIC_STATUE }).build((Type)null);
        LOOT_STATUE_TILE_ENTITY = TileEntityType.Builder.of((Supplier)LootStatueTileEntity::new, new Block[] { ModBlocks.GIFT_NORMAL_STATUE, ModBlocks.GIFT_MEGA_STATUE, ModBlocks.VAULT_PLAYER_LOOT_STATUE, ModBlocks.OMEGA_STATUE, ModBlocks.OMEGA_STATUE_VARIANT }).build((Type)null);
        TROPHY_STATUE_TILE_ENTITY = TileEntityType.Builder.of((Supplier)TrophyStatueTileEntity::new, new Block[] { ModBlocks.TROPHY_STATUE }).build((Type)null);
        CRYO_CHAMBER_TILE_ENTITY = TileEntityType.Builder.of((Supplier)CryoChamberTileEntity::new, new Block[] { ModBlocks.CRYO_CHAMBER }).build((Type)null);
        ANCIENT_CRYO_CHAMBER_TILE_ENTITY = TileEntityType.Builder.of((Supplier)AncientCryoChamberTileEntity::new, new Block[] { ModBlocks.CRYO_CHAMBER }).build((Type)null);
        VAULT_DOOR_TILE_ENTITY = TileEntityType.Builder.of((Supplier)VaultDoorTileEntity::new, new Block[] { (Block)ModBlocks.ISKALLIUM_DOOR, (Block)ModBlocks.GORGINITE_DOOR, (Block)ModBlocks.SPARKLETINE_DOOR, (Block)ModBlocks.ASHIUM_DOOR, (Block)ModBlocks.BOMIGNITE_DOOR, (Block)ModBlocks.FUNSOIDE_DOOR, (Block)ModBlocks.TUBIUM_DOOR, (Block)ModBlocks.UPALINE_DOOR, (Block)ModBlocks.PUFFIUM_DOOR, (Block)ModBlocks.UNKNOWN_DOOR }).build((Type)null);
        VAULT_LOOTABLE_TILE_ENTITY = TileEntityType.Builder.of((Supplier)VaultLootableTileEntity::new, new Block[] { ModBlocks.UNKNOWN_ORE, ModBlocks.VAULT_LOOT_RICHITY, ModBlocks.VAULT_LOOT_RESOURCE, ModBlocks.VAULT_LOOT_MISC, ModBlocks.UNKNOWN_VAULT_CHEST, ModBlocks.UNKNOWN_TREASURE_CHEST, ModBlocks.UNKNOWN_VAULT_OBJECTIVE }).build((Type)null);
        VAULT_CHEST_TILE_ENTITY = TileEntityType.Builder.of((Supplier)VaultChestTileEntity::new, new Block[] { ModBlocks.VAULT_CHEST, ModBlocks.VAULT_ALTAR_CHEST, ModBlocks.VAULT_COOP_CHEST, ModBlocks.VAULT_BONUS_CHEST }).build((Type)null);
        VAULT_TREASURE_CHEST_TILE_ENTITY = TileEntityType.Builder.of((Supplier)VaultTreasureChestTileEntity::new, new Block[] { ModBlocks.VAULT_TREASURE_CHEST }).build((Type)null);
        XP_ALTAR_TILE_ENTITY = TileEntityType.Builder.of((Supplier)XpAltarTileEntity::new, new Block[] { ModBlocks.XP_ALTAR }).build((Type)null);
        BLOOD_ALTAR_TILE_ENTITY = TileEntityType.Builder.of((Supplier)BloodAltarTileEntity::new, new Block[] { ModBlocks.BLOOD_ALTAR }).build((Type)null);
        TIME_ALTAR_TILE_ENTITY = TileEntityType.Builder.of((Supplier)TimeAltarTileEntity::new, new Block[] { ModBlocks.TIME_ALTAR }).build((Type)null);
        SOUL_ALTAR_TILE_ENTITY = TileEntityType.Builder.of((Supplier)SoulAltarTileEntity::new, new Block[] { ModBlocks.SOUL_ALTAR }).build((Type)null);
        STATUE_CAULDRON_TILE_ENTITY = TileEntityType.Builder.of((Supplier)StatueCauldronTileEntity::new, new Block[] { (Block)ModBlocks.STATUE_CAULDRON }).build((Type)null);
        OBELISK_TILE_ENTITY = TileEntityType.Builder.of((Supplier)ObeliskTileEntity::new, new Block[] { ModBlocks.OBELISK }).build((Type)null);
        SCAVENGER_CHEST_TILE_ENTITY = TileEntityType.Builder.of((Supplier)ScavengerChestTileEntity::new, new Block[] { (Block)ModBlocks.SCAVENGER_CHEST }).build((Type)null);
        SCAVENGER_TREASURE_TILE_ENTITY = TileEntityType.Builder.of((Supplier)ScavengerTreasureTileEntity::new, new Block[] { (Block)ModBlocks.SCAVENGER_TREASURE }).build((Type)null);
        STABILIZER_TILE_ENTITY = TileEntityType.Builder.of((Supplier)StabilizerTileEntity::new, new Block[] { ModBlocks.STABILIZER }).build((Type)null);
        CATALYST_DECRYPTION_TABLE_TILE_ENTITY = TileEntityType.Builder.of((Supplier)CatalystDecryptionTableTileEntity::new, new Block[] { ModBlocks.CATALYST_DECRYPTION_TABLE }).build((Type)null);
        ETCHING_CONTROLLER_TILE_ENTITY = TileEntityType.Builder.of((Supplier)EtchingVendorControllerTileEntity::new, new Block[] { (Block)ModBlocks.ETCHING_CONTROLLER_BLOCK }).build((Type)null);
        VAULT_CHARM_CONTROLLER_TILE_ENTITY = TileEntityType.Builder.of((Supplier)VaultCharmControllerTileEntity::new, new Block[] { ModBlocks.VAULT_CHARM_CONTROLLER_BLOCK }).build((Type)null);
        RAID_CONTROLLER_TILE_ENTITY = TileEntityType.Builder.of((Supplier)VaultRaidControllerTileEntity::new, new Block[] { ModBlocks.RAID_CONTROLLER_BLOCK }).build((Type)null);
    }
    
    @OnlyIn(Dist.CLIENT)
    private static class VaultStackRendererProvider implements Callable<ItemStackTileEntityRenderer>
    {
        public static final VaultStackRendererProvider INSTANCE;
        
        @Override
        public ItemStackTileEntityRenderer call() throws Exception {
            return VaultISTER.INSTANCE;
        }
        
        static {
            INSTANCE = new VaultStackRendererProvider();
        }
    }
}
