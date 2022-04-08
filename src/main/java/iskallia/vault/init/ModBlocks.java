package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.block.*;
import iskallia.vault.block.entity.*;
import iskallia.vault.block.item.*;
import iskallia.vault.client.render.VaultISTER;
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
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModBlocks {
    public static final VaultPortalBlock VAULT_PORTAL = new VaultPortalBlock();
    public static final FinalVaultPortalBlock FINAL_VAULT_PORTAL = new FinalVaultPortalBlock();
    public static final OtherSidePortalBlock OTHER_SIDE_PORTAL = new OtherSidePortalBlock();
    public static final VaultAltarBlock VAULT_ALTAR = new VaultAltarBlock();
    public static final VaultOreBlock ALEXANDRITE_ORE = new VaultOreBlock(ModItems.ALEXANDRITE_GEM);
    public static final VaultOreBlock BENITOITE_ORE = new VaultOreBlock(ModItems.BENITOITE_GEM);
    public static final VaultOreBlock LARIMAR_ORE = new VaultOreBlock(ModItems.LARIMAR_GEM);
    public static final VaultOreBlock BLACK_OPAL_ORE = new VaultOreBlock(ModItems.BLACK_OPAL_GEM);
    public static final VaultOreBlock PAINITE_ORE = new VaultOreBlock(ModItems.PAINITE_GEM);
    public static final VaultOreBlock ISKALLIUM_ORE = new VaultOreBlock(ModItems.ISKALLIUM_GEM);
    public static final VaultOreBlock GORGINITE_ORE = new VaultOreBlock(ModItems.GORGINITE_GEM);
    public static final VaultOreBlock SPARKLETINE_ORE = new VaultOreBlock(ModItems.SPARKLETINE_GEM);
    public static final VaultOreBlock WUTODIE_ORE = new VaultOreBlock(ModItems.WUTODIE_GEM);
    public static final VaultOreBlock ASHIUM_ORE = new VaultOreBlock(ModItems.ASHIUM_GEM);
    public static final VaultOreBlock BOMIGNITE_ORE = new VaultOreBlock(ModItems.BOMIGNITE_GEM);
    public static final VaultOreBlock FUNSOIDE_ORE = new VaultOreBlock(ModItems.FUNSOIDE_GEM);
    public static final VaultOreBlock TUBIUM_ORE = new VaultOreBlock(ModItems.TUBIUM_GEM);
    public static final VaultOreBlock UPALINE_ORE = new VaultOreBlock(ModItems.UPALINE_GEM);
    public static final VaultOreBlock PUFFIUM_ORE = new VaultOreBlock(ModItems.PUFFIUM_GEM);
    public static final VaultOreBlock ECHO_ORE = new VaultOreBlock(ModItems.ECHO_GEM);
    public static final Block UNKNOWN_ORE = (Block) new VaultLootableBlock(VaultLootableBlock.Type.ORE);
    public static final VaultRockBlock VAULT_ROCK_ORE = new VaultRockBlock();
    public static final DoorBlock ISKALLIUM_DOOR = (DoorBlock) new VaultDoorBlock((Item) ModItems.ISKALLIUM_KEY);
    public static final DoorBlock GORGINITE_DOOR = (DoorBlock) new VaultDoorBlock((Item) ModItems.GORGINITE_KEY);
    public static final DoorBlock SPARKLETINE_DOOR = (DoorBlock) new VaultDoorBlock((Item) ModItems.SPARKLETINE_KEY);
    public static final DoorBlock ASHIUM_DOOR = (DoorBlock) new VaultDoorBlock((Item) ModItems.ASHIUM_KEY);
    public static final DoorBlock BOMIGNITE_DOOR = (DoorBlock) new VaultDoorBlock((Item) ModItems.BOMIGNITE_KEY);
    public static final DoorBlock FUNSOIDE_DOOR = (DoorBlock) new VaultDoorBlock((Item) ModItems.FUNSOIDE_KEY);
    public static final DoorBlock TUBIUM_DOOR = (DoorBlock) new VaultDoorBlock((Item) ModItems.TUBIUM_KEY);
    public static final DoorBlock UPALINE_DOOR = (DoorBlock) new VaultDoorBlock((Item) ModItems.UPALINE_KEY);
    public static final DoorBlock PUFFIUM_DOOR = (DoorBlock) new VaultDoorBlock((Item) ModItems.PUFFIUM_KEY);
    public static final DoorBlock UNKNOWN_DOOR = (DoorBlock) new UnknownVaultDoorBlock();
    public static final VaultRuneBlock VAULT_RUNE_BLOCK = new VaultRuneBlock();
    public static final VaultArtifactBlock VAULT_ARTIFACT = new VaultArtifactBlock();
    public static final VaultCrateBlock VAULT_CRATE = new VaultCrateBlock();
    public static final VaultCrateBlock VAULT_CRATE_CAKE = new VaultCrateBlock();
    public static final VaultCrateBlock VAULT_CRATE_ARENA = new VaultCrateBlock();
    public static final VaultCrateBlock VAULT_CRATE_SCAVENGER = new VaultCrateBlock();
    public static final ObeliskBlock OBELISK = new ObeliskBlock();
    public static final VendingMachineBlock VENDING_MACHINE = new VendingMachineBlock();
    public static final AdvancedVendingBlock ADVANCED_VENDING_MACHINE = new AdvancedVendingBlock();
    public static final VaultBedrockBlock VAULT_BEDROCK = new VaultBedrockBlock();
    public static final Block VAULT_STONE = new Block(AbstractBlock.Properties.of(Material.STONE).strength(1.5F, 6.0F).isValidSpawn((a, b, c, d) -> false));
    public static final GlassBlock VAULT_GLASS = new GlassBlock(AbstractBlock.Properties.copy((AbstractBlock) Blocks.GLASS).strength(-1.0F, 3600000.0F));
    public static final RelicStatueBlock RELIC_STATUE = new RelicStatueBlock();
    public static final LootStatueBlock GIFT_NORMAL_STATUE = new LootStatueBlock(StatueType.GIFT_NORMAL);
    public static final LootStatueBlock GIFT_MEGA_STATUE = new LootStatueBlock(StatueType.GIFT_MEGA);
    public static final BowHatBlock BOW_HAT = new BowHatBlock();
    public static final StatueDragonHeadBlock STATUE_DRAGON_HEAD = new StatueDragonHeadBlock();
    public static final LootStatueBlock VAULT_PLAYER_LOOT_STATUE = new LootStatueBlock(StatueType.VAULT_BOSS);
    public static final CryoChamberBlock CRYO_CHAMBER = new CryoChamberBlock();
    public static final KeyPressBlock KEY_PRESS = new KeyPressBlock();
    public static final Block VAULT_DIAMOND_BLOCK = new Block(AbstractBlock.Properties.copy((AbstractBlock) Blocks.DIAMOND_BLOCK));
    public static final MazeBlock MAZE_BLOCK = new MazeBlock();
    public static final PuzzleRuneBlock PUZZLE_RUNE_BLOCK = new PuzzleRuneBlock();
    public static final Block YELLOW_PUZZLE_CONCRETE = new Block(AbstractBlock.Properties.copy((AbstractBlock) Blocks.BLACK_CONCRETE));
    public static final Block PINK_PUZZLE_CONCRETE = new Block(AbstractBlock.Properties.copy((AbstractBlock) Blocks.BLACK_CONCRETE));
    public static final Block GREEN_PUZZLE_CONCRETE = new Block(AbstractBlock.Properties.copy((AbstractBlock) Blocks.BLACK_CONCRETE));
    public static final Block BLUE_PUZZLE_CONCRETE = new Block(AbstractBlock.Properties.copy((AbstractBlock) Blocks.BLACK_CONCRETE));
    public static final OmegaStatueBlock OMEGA_STATUE = new OmegaStatueBlock();
    public static final OmegaVariantStatueBlock OMEGA_STATUE_VARIANT = new OmegaVariantStatueBlock();
    public static final TrophyBlock TROPHY_STATUE = new TrophyBlock();
    public static final TransmogTableBlock TRANSMOG_TABLE = new TransmogTableBlock();
    public static final Block VAULT_LOOT_RICHITY = (Block) new VaultLootableBlock(VaultLootableBlock.Type.RICHITY);
    public static final Block VAULT_LOOT_RESOURCE = (Block) new VaultLootableBlock(VaultLootableBlock.Type.RESOURCE);
    public static final Block VAULT_LOOT_MISC = (Block) new VaultLootableBlock(VaultLootableBlock.Type.MISC);
    public static final Block UNKNOWN_VAULT_CHEST = (Block) new VaultLootableBlock(VaultLootableBlock.Type.VAULT_CHEST);
    public static final Block UNKNOWN_TREASURE_CHEST = (Block) new VaultLootableBlock(VaultLootableBlock.Type.VAULT_TREASURE);
    public static final Block UNKNOWN_VAULT_OBJECTIVE = (Block) new VaultLootableBlock(VaultLootableBlock.Type.VAULT_OBJECTIVE);
    public static final Block VAULT_CHEST = (Block) new VaultChestBlock(AbstractBlock.Properties.copy((AbstractBlock) Blocks.CHEST).strength(40.0F, 5.0F));
    public static final Block VAULT_TREASURE_CHEST = (Block) new VaultTreasureChestBlock(AbstractBlock.Properties.copy((AbstractBlock) Blocks.CHEST).strength(-1.0F, 3600000.0F));
    public static final Block VAULT_ALTAR_CHEST = (Block) new VaultChestBlock(AbstractBlock.Properties.copy((AbstractBlock) Blocks.CHEST).strength(-1.0F, 3600000.0F));
    public static final Block VAULT_COOP_CHEST = (Block) new VaultChestBlock(AbstractBlock.Properties.of(Material.STONE).strength(2.0F, 3600000.0F).sound(SoundType.STONE));
    public static final Block VAULT_BONUS_CHEST = (Block) new VaultChestBlock(AbstractBlock.Properties.of(Material.STONE).strength(2.0F, 3600000.0F).sound(SoundType.STONE));
    public static final XPAltarBlock XP_ALTAR = new XPAltarBlock();
    public static final BloodAltarBlock BLOOD_ALTAR = new BloodAltarBlock();
    public static final TimeAltarBlock TIME_ALTAR = new TimeAltarBlock();
    public static final SoulAltarBlock SOUL_ALTAR = new SoulAltarBlock();
    public static final StatueCauldronBlock STATUE_CAULDRON = new StatueCauldronBlock();
    public static final ScavengerChestBlock SCAVENGER_CHEST = new ScavengerChestBlock(AbstractBlock.Properties.copy((AbstractBlock) Blocks.CHEST).strength(-1.0F, 3600000.0F));
    public static final ScavengerTreasureBlock SCAVENGER_TREASURE = new ScavengerTreasureBlock();
    public static final StabilizerBlock STABILIZER = new StabilizerBlock();
    public static final CatalystDecryptionTableBlock CATALYST_DECRYPTION_TABLE = new CatalystDecryptionTableBlock();
    public static final EtchingVendorControllerBlock ETCHING_CONTROLLER_BLOCK = new EtchingVendorControllerBlock();
    public static final VaultCharmControllerBlock VAULT_CHARM_CONTROLLER_BLOCK = new VaultCharmControllerBlock();
    public static final VaultRaidControllerBlock RAID_CONTROLLER_BLOCK = new VaultRaidControllerBlock();


    public static final FlowingFluidBlock VOID_LIQUID_BLOCK = (FlowingFluidBlock) new VoidFluidBlock((Supplier) ModFluids.VOID_LIQUID, AbstractBlock.Properties.of(Material.WATER, MaterialColor.COLOR_BLACK).noCollission().randomTicks().strength(100.0F).lightLevel(state -> 15).noDrops());


    public static final RelicStatueBlockItem RELIC_STATUE_BLOCK_ITEM = new RelicStatueBlockItem();
    public static final LootStatueBlockItem GIFT_NORMAL_STATUE_BLOCK_ITEM = new LootStatueBlockItem((Block) GIFT_NORMAL_STATUE, StatueType.GIFT_NORMAL);
    public static final LootStatueBlockItem GIFT_MEGA_STATUE_BLOCK_ITEM = new LootStatueBlockItem((Block) GIFT_MEGA_STATUE, StatueType.GIFT_MEGA);
    public static final LootStatueBlockItem VAULT_PLAYER_LOOT_STATUE_BLOCK_ITEM = new LootStatueBlockItem((Block) VAULT_PLAYER_LOOT_STATUE, StatueType.VAULT_BOSS);
    public static final LootStatueBlockItem OMEGA_STATUE_BLOCK_ITEM = new LootStatueBlockItem((Block) OMEGA_STATUE, StatueType.OMEGA);
    public static final LootStatueBlockItem OMEGA_STATUE_VARIANT_BLOCK_ITEM = new LootStatueBlockItem((Block) OMEGA_STATUE_VARIANT, StatueType.OMEGA_VARIANT);
    public static final TrophyStatueBlockItem TROPHY_STATUE_BLOCK_ITEM = new TrophyStatueBlockItem((Block) TROPHY_STATUE);
    public static final VendingMachineBlockItem VENDING_MACHINE_BLOCK_ITEM = new VendingMachineBlockItem((Block) VENDING_MACHINE);
    public static final AdvancedVendingMachineBlockItem ADVANCED_VENDING_BLOCK_ITEM = new AdvancedVendingMachineBlockItem((Block) ADVANCED_VENDING_MACHINE);
    public static final PuzzleRuneBlock.Item PUZZLE_RUNE_BLOCK_ITEM = new PuzzleRuneBlock.Item((Block) PUZZLE_RUNE_BLOCK, (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).stacksTo(1));
    public static final BlockItem VAULT_CHEST_ITEM = new BlockItem(VAULT_CHEST, (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).setISTER(() -> VaultStackRendererProvider.INSTANCE));
    public static final BlockItem VAULT_TREASURE_CHEST_ITEM = new BlockItem(VAULT_TREASURE_CHEST, (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).setISTER(() -> VaultStackRendererProvider.INSTANCE));
    public static final BlockItem VAULT_ALTAR_CHEST_ITEM = new BlockItem(VAULT_ALTAR_CHEST, (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).setISTER(() -> VaultStackRendererProvider.INSTANCE));
    public static final BlockItem VAULT_COOP_CHEST_ITEM = new BlockItem(VAULT_COOP_CHEST, (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).setISTER(() -> VaultStackRendererProvider.INSTANCE));
    public static final BlockItem VAULT_BONUS_CHEST_ITEM = new BlockItem(VAULT_BONUS_CHEST, (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).setISTER(() -> VaultStackRendererProvider.INSTANCE));
    public static final BlockItem SCAVENGER_CHEST_ITEM = new BlockItem((Block) SCAVENGER_CHEST, (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).setISTER(() -> VaultStackRendererProvider.INSTANCE));


    public static final TileEntityType<VaultAltarTileEntity> VAULT_ALTAR_TILE_ENTITY = TileEntityType.Builder.of(VaultAltarTileEntity::new, new Block[]{(Block) VAULT_ALTAR}).build(null);

    public static final TileEntityType<VaultRuneTileEntity> VAULT_RUNE_TILE_ENTITY = TileEntityType.Builder.of(VaultRuneTileEntity::new, new Block[]{(Block) VAULT_RUNE_BLOCK}).build(null);

    public static final TileEntityType<VaultCrateTileEntity> VAULT_CRATE_TILE_ENTITY = TileEntityType.Builder.of(VaultCrateTileEntity::new, new Block[]{(Block) VAULT_CRATE, (Block) VAULT_CRATE_CAKE, (Block) VAULT_CRATE_ARENA, (Block) VAULT_CRATE_SCAVENGER}).build(null);

    public static final TileEntityType<VaultPortalTileEntity> VAULT_PORTAL_TILE_ENTITY = TileEntityType.Builder.of(VaultPortalTileEntity::new, new Block[]{(Block) VAULT_PORTAL}).build(null);

    public static final TileEntityType<OtherSidePortalTileEntity> OTHER_SIDE_PORTAL_TILE_ENTITY = TileEntityType.Builder.of(OtherSidePortalTileEntity::new, new Block[]{(Block) OTHER_SIDE_PORTAL}).build(null);

    public static final TileEntityType<VendingMachineTileEntity> VENDING_MACHINE_TILE_ENTITY = TileEntityType.Builder.of(VendingMachineTileEntity::new, new Block[]{(Block) VENDING_MACHINE}).build(null);

    public static final TileEntityType<AdvancedVendingTileEntity> ADVANCED_VENDING_MACHINE_TILE_ENTITY = TileEntityType.Builder.of(AdvancedVendingTileEntity::new, new Block[]{(Block) ADVANCED_VENDING_MACHINE}).build(null);

    public static final TileEntityType<RelicStatueTileEntity> RELIC_STATUE_TILE_ENTITY = TileEntityType.Builder.of(RelicStatueTileEntity::new, new Block[]{(Block) RELIC_STATUE}).build(null);

    public static final TileEntityType<LootStatueTileEntity> LOOT_STATUE_TILE_ENTITY = TileEntityType.Builder.of(LootStatueTileEntity::new, new Block[]{(Block) GIFT_NORMAL_STATUE, (Block) GIFT_MEGA_STATUE, (Block) VAULT_PLAYER_LOOT_STATUE, (Block) OMEGA_STATUE, (Block) OMEGA_STATUE_VARIANT}).build(null);

    public static final TileEntityType<TrophyStatueTileEntity> TROPHY_STATUE_TILE_ENTITY = TileEntityType.Builder.of(TrophyStatueTileEntity::new, new Block[]{(Block) TROPHY_STATUE}).build(null);

    public static final TileEntityType<CryoChamberTileEntity> CRYO_CHAMBER_TILE_ENTITY = TileEntityType.Builder.of(CryoChamberTileEntity::new, new Block[]{(Block) CRYO_CHAMBER}).build(null);

    public static final TileEntityType<AncientCryoChamberTileEntity> ANCIENT_CRYO_CHAMBER_TILE_ENTITY = TileEntityType.Builder.of(AncientCryoChamberTileEntity::new, new Block[]{(Block) CRYO_CHAMBER}).build(null);

    public static final TileEntityType<VaultDoorTileEntity> VAULT_DOOR_TILE_ENTITY = TileEntityType.Builder.of(VaultDoorTileEntity::new, new Block[]{(Block) ISKALLIUM_DOOR, (Block) GORGINITE_DOOR, (Block) SPARKLETINE_DOOR, (Block) ASHIUM_DOOR, (Block) BOMIGNITE_DOOR, (Block) FUNSOIDE_DOOR, (Block) TUBIUM_DOOR, (Block) UPALINE_DOOR, (Block) PUFFIUM_DOOR, (Block) UNKNOWN_DOOR
    }).build(null);

    public static final TileEntityType<VaultLootableTileEntity> VAULT_LOOTABLE_TILE_ENTITY = TileEntityType.Builder.of(VaultLootableTileEntity::new, new Block[]{UNKNOWN_ORE, VAULT_LOOT_RICHITY, VAULT_LOOT_RESOURCE, VAULT_LOOT_MISC, UNKNOWN_VAULT_CHEST, UNKNOWN_TREASURE_CHEST, UNKNOWN_VAULT_OBJECTIVE
    }).build(null);

    public static final TileEntityType<VaultChestTileEntity> VAULT_CHEST_TILE_ENTITY = TileEntityType.Builder.of(VaultChestTileEntity::new, new Block[]{VAULT_CHEST, VAULT_ALTAR_CHEST, VAULT_COOP_CHEST, VAULT_BONUS_CHEST}).build(null);

    public static final TileEntityType<VaultTreasureChestTileEntity> VAULT_TREASURE_CHEST_TILE_ENTITY = TileEntityType.Builder.of(VaultTreasureChestTileEntity::new, new Block[]{VAULT_TREASURE_CHEST}).build(null);

    public static final TileEntityType<XpAltarTileEntity> XP_ALTAR_TILE_ENTITY = TileEntityType.Builder.of(XpAltarTileEntity::new, new Block[]{(Block) XP_ALTAR}).build(null);

    public static final TileEntityType<BloodAltarTileEntity> BLOOD_ALTAR_TILE_ENTITY = TileEntityType.Builder.of(BloodAltarTileEntity::new, new Block[]{(Block) BLOOD_ALTAR}).build(null);

    public static final TileEntityType<TimeAltarTileEntity> TIME_ALTAR_TILE_ENTITY = TileEntityType.Builder.of(TimeAltarTileEntity::new, new Block[]{(Block) TIME_ALTAR}).build(null);

    public static final TileEntityType<SoulAltarTileEntity> SOUL_ALTAR_TILE_ENTITY = TileEntityType.Builder.of(SoulAltarTileEntity::new, new Block[]{(Block) SOUL_ALTAR}).build(null);

    public static final TileEntityType<StatueCauldronTileEntity> STATUE_CAULDRON_TILE_ENTITY = TileEntityType.Builder.of(StatueCauldronTileEntity::new, new Block[]{(Block) STATUE_CAULDRON}).build(null);

    public static final TileEntityType<ObeliskTileEntity> OBELISK_TILE_ENTITY = TileEntityType.Builder.of(ObeliskTileEntity::new, new Block[]{(Block) OBELISK}).build(null);

    public static final TileEntityType<ScavengerChestTileEntity> SCAVENGER_CHEST_TILE_ENTITY = TileEntityType.Builder.of(ScavengerChestTileEntity::new, new Block[]{(Block) SCAVENGER_CHEST}).build(null);

    public static final TileEntityType<ScavengerTreasureTileEntity> SCAVENGER_TREASURE_TILE_ENTITY = TileEntityType.Builder.of(ScavengerTreasureTileEntity::new, new Block[]{(Block) SCAVENGER_TREASURE}).build(null);

    public static final TileEntityType<StabilizerTileEntity> STABILIZER_TILE_ENTITY = TileEntityType.Builder.of(StabilizerTileEntity::new, new Block[]{(Block) STABILIZER}).build(null);

    public static final TileEntityType<CatalystDecryptionTableTileEntity> CATALYST_DECRYPTION_TABLE_TILE_ENTITY = TileEntityType.Builder.of(CatalystDecryptionTableTileEntity::new, new Block[]{(Block) CATALYST_DECRYPTION_TABLE}).build(null);

    public static final TileEntityType<EtchingVendorControllerTileEntity> ETCHING_CONTROLLER_TILE_ENTITY = TileEntityType.Builder.of(EtchingVendorControllerTileEntity::new, new Block[]{(Block) ETCHING_CONTROLLER_BLOCK}).build(null);

    public static final TileEntityType<VaultCharmControllerTileEntity> VAULT_CHARM_CONTROLLER_TILE_ENTITY = TileEntityType.Builder.of(VaultCharmControllerTileEntity::new, new Block[]{(Block) VAULT_CHARM_CONTROLLER_BLOCK}).build(null);

    public static final TileEntityType<VaultRaidControllerTileEntity> RAID_CONTROLLER_TILE_ENTITY = TileEntityType.Builder.of(VaultRaidControllerTileEntity::new, new Block[]{(Block) RAID_CONTROLLER_BLOCK}).build(null);

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        registerBlock(event, (Block) VAULT_PORTAL, Vault.id("vault_portal"));
        registerBlock(event, (Block) FINAL_VAULT_PORTAL, Vault.id("final_vault_portal"));
        registerBlock(event, (Block) OTHER_SIDE_PORTAL, Vault.id("other_side_portal"));
        registerBlock(event, (Block) VAULT_ALTAR, Vault.id("vault_altar"));
        registerBlock(event, (Block) ALEXANDRITE_ORE, Vault.id("ore_alexandrite"));
        registerBlock(event, (Block) BENITOITE_ORE, Vault.id("ore_benitoite"));
        registerBlock(event, (Block) LARIMAR_ORE, Vault.id("ore_larimar"));
        registerBlock(event, (Block) BLACK_OPAL_ORE, Vault.id("ore_black_opal"));
        registerBlock(event, (Block) PAINITE_ORE, Vault.id("ore_painite"));
        registerBlock(event, (Block) ISKALLIUM_ORE, Vault.id("ore_iskallium"));
        registerBlock(event, (Block) GORGINITE_ORE, Vault.id("ore_gorginite"));
        registerBlock(event, (Block) SPARKLETINE_ORE, Vault.id("ore_sparkletine"));
        registerBlock(event, (Block) WUTODIE_ORE, Vault.id("ore_wutodie"));
        registerBlock(event, (Block) ASHIUM_ORE, Vault.id("ore_ashium"));
        registerBlock(event, (Block) BOMIGNITE_ORE, Vault.id("ore_bomignite"));
        registerBlock(event, (Block) FUNSOIDE_ORE, Vault.id("ore_funsoide"));
        registerBlock(event, (Block) TUBIUM_ORE, Vault.id("ore_tubium"));
        registerBlock(event, (Block) UPALINE_ORE, Vault.id("ore_upaline"));
        registerBlock(event, (Block) PUFFIUM_ORE, Vault.id("ore_puffium"));
        registerBlock(event, (Block) ECHO_ORE, Vault.id("ore_echo"));
        registerBlock(event, UNKNOWN_ORE, Vault.id("ore_unknown"));
        registerBlock(event, (Block) VAULT_ROCK_ORE, Vault.id("ore_vault_rock"));
        registerBlock(event, (Block) ISKALLIUM_DOOR, Vault.id("door_iskallium"));
        registerBlock(event, (Block) GORGINITE_DOOR, Vault.id("door_gorginite"));
        registerBlock(event, (Block) SPARKLETINE_DOOR, Vault.id("door_sparkletine"));
        registerBlock(event, (Block) ASHIUM_DOOR, Vault.id("door_ashium"));
        registerBlock(event, (Block) BOMIGNITE_DOOR, Vault.id("door_bomignite"));
        registerBlock(event, (Block) FUNSOIDE_DOOR, Vault.id("door_funsoide"));
        registerBlock(event, (Block) TUBIUM_DOOR, Vault.id("door_tubium"));
        registerBlock(event, (Block) UPALINE_DOOR, Vault.id("door_upaline"));
        registerBlock(event, (Block) PUFFIUM_DOOR, Vault.id("door_puffium"));
        registerBlock(event, (Block) UNKNOWN_DOOR, Vault.id("door_unknown"));
        registerBlock(event, (Block) VAULT_RUNE_BLOCK, Vault.id("vault_rune_block"));
        registerBlock(event, (Block) VAULT_ARTIFACT, Vault.id("vault_artifact"));
        registerBlock(event, (Block) VAULT_CRATE, Vault.id("vault_crate"));
        registerBlock(event, (Block) VAULT_CRATE_CAKE, Vault.id("vault_crate_cake"));
        registerBlock(event, (Block) VAULT_CRATE_ARENA, Vault.id("vault_crate_arena"));
        registerBlock(event, (Block) VAULT_CRATE_SCAVENGER, Vault.id("vault_crate_scavenger"));
        registerBlock(event, (Block) OBELISK, Vault.id("obelisk"));
        registerBlock(event, (Block) VENDING_MACHINE, Vault.id("vending_machine"));
        registerBlock(event, (Block) ADVANCED_VENDING_MACHINE, Vault.id("advanced_vending_machine"));
        registerBlock(event, (Block) VAULT_BEDROCK, Vault.id("vault_bedrock"));
        registerBlock(event, VAULT_STONE, Vault.id("vault_stone"));
        registerBlock(event, (Block) VAULT_GLASS, Vault.id("vault_glass"));
        registerBlock(event, (Block) RELIC_STATUE, Vault.id("relic_statue"));
        registerBlock(event, (Block) GIFT_NORMAL_STATUE, Vault.id("gift_normal_statue"));
        registerBlock(event, (Block) GIFT_MEGA_STATUE, Vault.id("gift_mega_statue"));
        registerBlock(event, (Block) BOW_HAT, Vault.id("bow_hat"));
        registerBlock(event, (Block) STATUE_DRAGON_HEAD, Vault.id("statue_dragon"));
        registerBlock(event, (Block) VAULT_PLAYER_LOOT_STATUE, Vault.id("vault_player_loot_statue"));
        registerBlock(event, (Block) CRYO_CHAMBER, Vault.id("cryo_chamber"));
        registerBlock(event, (Block) KEY_PRESS, Vault.id("key_press"));
        registerBlock(event, VAULT_DIAMOND_BLOCK, Vault.id("vault_diamond_block"));
        registerBlock(event, (Block) MAZE_BLOCK, Vault.id("maze_block"));
        registerBlock(event, (Block) PUZZLE_RUNE_BLOCK, Vault.id("puzzle_rune_block"));
        registerBlock(event, YELLOW_PUZZLE_CONCRETE, Vault.id("yellow_puzzle_concrete"));
        registerBlock(event, PINK_PUZZLE_CONCRETE, Vault.id("pink_puzzle_concrete"));
        registerBlock(event, GREEN_PUZZLE_CONCRETE, Vault.id("green_puzzle_concrete"));
        registerBlock(event, BLUE_PUZZLE_CONCRETE, Vault.id("blue_puzzle_concrete"));
        registerBlock(event, (Block) OMEGA_STATUE, Vault.id("omega_statue"));
        registerBlock(event, (Block) OMEGA_STATUE_VARIANT, Vault.id("omega_statue_variant"));
        registerBlock(event, (Block) TROPHY_STATUE, Vault.id("trophy_statue"));
        registerBlock(event, (Block) TRANSMOG_TABLE, Vault.id("transmog_table"));
        registerBlock(event, VAULT_LOOT_RICHITY, Vault.id("vault_richity"));
        registerBlock(event, VAULT_LOOT_RESOURCE, Vault.id("vault_resource"));
        registerBlock(event, VAULT_LOOT_MISC, Vault.id("vault_misc"));
        registerBlock(event, UNKNOWN_VAULT_CHEST, Vault.id("unknown_vault_chest"));
        registerBlock(event, UNKNOWN_TREASURE_CHEST, Vault.id("unknown_treasure_chest"));
        registerBlock(event, UNKNOWN_VAULT_OBJECTIVE, Vault.id("unknown_vault_objective"));
        registerBlock(event, VAULT_CHEST, Vault.id("vault_chest"));
        registerBlock(event, VAULT_TREASURE_CHEST, Vault.id("vault_treasure_chest"));
        registerBlock(event, VAULT_ALTAR_CHEST, Vault.id("vault_altar_chest"));
        registerBlock(event, VAULT_COOP_CHEST, Vault.id("vault_coop_chest"));
        registerBlock(event, VAULT_BONUS_CHEST, Vault.id("vault_bonus_chest"));
        registerBlock(event, (Block) XP_ALTAR, Vault.id("xp_altar"));
        registerBlock(event, (Block) BLOOD_ALTAR, Vault.id("blood_altar"));
        registerBlock(event, (Block) TIME_ALTAR, Vault.id("time_altar"));
        registerBlock(event, (Block) SOUL_ALTAR, Vault.id("soul_altar"));
        registerBlock(event, (Block) STATUE_CAULDRON, Vault.id("statue_cauldron"));
        registerBlock(event, (Block) VOID_LIQUID_BLOCK, Vault.id("void_liquid"));
        registerBlock(event, (Block) SCAVENGER_CHEST, Vault.id("scavenger_chest"));
        registerBlock(event, (Block) SCAVENGER_TREASURE, Vault.id("scavenger_treasure"));
        registerBlock(event, (Block) STABILIZER, Vault.id("stabilizer"));
        registerBlock(event, (Block) CATALYST_DECRYPTION_TABLE, Vault.id("catalyst_decryption_table"));
        registerBlock(event, (Block) ETCHING_CONTROLLER_BLOCK, Vault.id("etching_vendor_controller"));
        registerBlock(event, (Block) VAULT_CHARM_CONTROLLER_BLOCK, Vault.id("vault_charm_controller"));
        registerBlock(event, (Block) RAID_CONTROLLER_BLOCK, Vault.id("raid_controller"));
    }

    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        registerTileEntity(event, VAULT_ALTAR_TILE_ENTITY, Vault.id("vault_altar_tile_entity"));
        registerTileEntity(event, VAULT_RUNE_TILE_ENTITY, Vault.id("vault_rune_tile_entity"));
        registerTileEntity(event, VAULT_CRATE_TILE_ENTITY, Vault.id("vault_crate_tile_entity"));
        registerTileEntity(event, VAULT_PORTAL_TILE_ENTITY, Vault.id("vault_portal_tile_entity"));
        registerTileEntity(event, OTHER_SIDE_PORTAL_TILE_ENTITY, Vault.id("other_side_portal_tile_entity"));
        registerTileEntity(event, VENDING_MACHINE_TILE_ENTITY, Vault.id("vending_machine_tile_entity"));
        registerTileEntity(event, ADVANCED_VENDING_MACHINE_TILE_ENTITY, Vault.id("advanced_vending_machine_tile_entity"));
        registerTileEntity(event, RELIC_STATUE_TILE_ENTITY, Vault.id("relic_statue_tile_entity"));
        registerTileEntity(event, LOOT_STATUE_TILE_ENTITY, Vault.id("loot_statue_tile_entity"));
        registerTileEntity(event, TROPHY_STATUE_TILE_ENTITY, Vault.id("trophy_statue_tile_entity"));
        registerTileEntity(event, CRYO_CHAMBER_TILE_ENTITY, Vault.id("cryo_chamber_tile_entity"));
        registerTileEntity(event, ANCIENT_CRYO_CHAMBER_TILE_ENTITY, Vault.id("ancient_cryo_chamber_tile_entity"));
        registerTileEntity(event, VAULT_DOOR_TILE_ENTITY, Vault.id("vault_door_tile_entity"));
        registerTileEntity(event, VAULT_LOOTABLE_TILE_ENTITY, Vault.id("vault_lootable_tile_entity"));
        registerTileEntity(event, VAULT_CHEST_TILE_ENTITY, Vault.id("vault_chest_tile_entity"));
        registerTileEntity(event, VAULT_TREASURE_CHEST_TILE_ENTITY, Vault.id("vault_treasure_chest_tile_entity"));
        registerTileEntity(event, XP_ALTAR_TILE_ENTITY, Vault.id("xp_altar_tile_entity"));
        registerTileEntity(event, BLOOD_ALTAR_TILE_ENTITY, Vault.id("blood_altar_tile_entity"));
        registerTileEntity(event, TIME_ALTAR_TILE_ENTITY, Vault.id("time_altar_tile_entity"));
        registerTileEntity(event, SOUL_ALTAR_TILE_ENTITY, Vault.id("soul_altar_tile_entity"));
        registerTileEntity(event, STATUE_CAULDRON_TILE_ENTITY, Vault.id("statue_cauldron_tile_entity"));
        registerTileEntity(event, OBELISK_TILE_ENTITY, Vault.id("obelisk_tile_entity"));
        registerTileEntity(event, SCAVENGER_CHEST_TILE_ENTITY, Vault.id("scavenger_chest_tile_entity"));
        registerTileEntity(event, SCAVENGER_TREASURE_TILE_ENTITY, Vault.id("scavenger_treasure_tile_entity"));
        registerTileEntity(event, STABILIZER_TILE_ENTITY, Vault.id("stabilizer_tile_entity"));
        registerTileEntity(event, CATALYST_DECRYPTION_TABLE_TILE_ENTITY, Vault.id("catalyst_decryption_table_tile_entity"));
        registerTileEntity(event, ETCHING_CONTROLLER_TILE_ENTITY, Vault.id("etching_vendor_controller_tile_entity"));
        registerTileEntity(event, VAULT_CHARM_CONTROLLER_TILE_ENTITY, Vault.id("vault_charm_controller_tile_entity"));
        registerTileEntity(event, RAID_CONTROLLER_TILE_ENTITY, Vault.id("raid_controller_tile_entity"));
    }

    public static void registerTileEntityRenderers() {
        ClientRegistry.bindTileEntityRenderer(VAULT_ALTAR_TILE_ENTITY, iskallia.vault.block.render.VaultAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(VAULT_RUNE_TILE_ENTITY, iskallia.vault.block.render.VaultRuneRenderer::new);
        ClientRegistry.bindTileEntityRenderer(VENDING_MACHINE_TILE_ENTITY, iskallia.vault.block.render.VendingMachineRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ADVANCED_VENDING_MACHINE_TILE_ENTITY, iskallia.vault.block.render.AdvancedVendingRenderer::new);
        ClientRegistry.bindTileEntityRenderer(RELIC_STATUE_TILE_ENTITY, iskallia.vault.block.render.RelicStatueRenderer::new);
        ClientRegistry.bindTileEntityRenderer(LOOT_STATUE_TILE_ENTITY, iskallia.vault.block.render.LootStatueRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TROPHY_STATUE_TILE_ENTITY, iskallia.vault.block.render.LootStatueRenderer::new);
        ClientRegistry.bindTileEntityRenderer(CRYO_CHAMBER_TILE_ENTITY, iskallia.vault.block.render.CryoChamberRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ANCIENT_CRYO_CHAMBER_TILE_ENTITY, iskallia.vault.block.render.CryoChamberRenderer::new);
        ClientRegistry.bindTileEntityRenderer(VAULT_CHEST_TILE_ENTITY, iskallia.vault.block.render.VaultChestRenderer::new);
        ClientRegistry.bindTileEntityRenderer(VAULT_TREASURE_CHEST_TILE_ENTITY, iskallia.vault.block.render.VaultChestRenderer::new);
        ClientRegistry.bindTileEntityRenderer(XP_ALTAR_TILE_ENTITY, iskallia.vault.block.render.FillableAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(BLOOD_ALTAR_TILE_ENTITY, iskallia.vault.block.render.FillableAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TIME_ALTAR_TILE_ENTITY, iskallia.vault.block.render.FillableAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(SOUL_ALTAR_TILE_ENTITY, iskallia.vault.block.render.FillableAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(STATUE_CAULDRON_TILE_ENTITY, iskallia.vault.block.render.StatueCauldronRenderer::new);
        ClientRegistry.bindTileEntityRenderer(SCAVENGER_CHEST_TILE_ENTITY, iskallia.vault.block.render.ScavengerChestRenderer::new);
        ClientRegistry.bindTileEntityRenderer(RAID_CONTROLLER_TILE_ENTITY, iskallia.vault.block.render.VaultRaidControllerRenderer::new);
    }

    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        registerBlockItem(event, (Block) VAULT_PORTAL);
        registerBlockItem(event, (Block) FINAL_VAULT_PORTAL);
        registerBlockItem(event, (Block) OTHER_SIDE_PORTAL);
        registerBlockItem(event, (Block) VAULT_ALTAR, 1);
        registerBlockItem(event, (Block) ALEXANDRITE_ORE);
        registerBlockItem(event, (Block) BENITOITE_ORE);
        registerBlockItem(event, (Block) LARIMAR_ORE);
        registerBlockItem(event, (Block) BLACK_OPAL_ORE);
        registerBlockItem(event, (Block) PAINITE_ORE);
        registerBlockItem(event, (Block) ISKALLIUM_ORE);
        registerBlockItem(event, (Block) GORGINITE_ORE);
        registerBlockItem(event, (Block) SPARKLETINE_ORE);
        registerBlockItem(event, (Block) WUTODIE_ORE);
        registerBlockItem(event, (Block) ASHIUM_ORE);
        registerBlockItem(event, (Block) BOMIGNITE_ORE);
        registerBlockItem(event, (Block) FUNSOIDE_ORE);
        registerBlockItem(event, (Block) TUBIUM_ORE);
        registerBlockItem(event, (Block) UPALINE_ORE);
        registerBlockItem(event, (Block) PUFFIUM_ORE);
        registerBlockItem(event, (Block) ECHO_ORE);
        registerBlockItem(event, UNKNOWN_ORE);
        registerBlockItem(event, (Block) VAULT_ROCK_ORE);
        registerTallBlockItem(event, (Block) ISKALLIUM_DOOR);
        registerTallBlockItem(event, (Block) GORGINITE_DOOR);
        registerTallBlockItem(event, (Block) SPARKLETINE_DOOR);
        registerTallBlockItem(event, (Block) ASHIUM_DOOR);
        registerTallBlockItem(event, (Block) BOMIGNITE_DOOR);
        registerTallBlockItem(event, (Block) FUNSOIDE_DOOR);
        registerTallBlockItem(event, (Block) TUBIUM_DOOR);
        registerTallBlockItem(event, (Block) UPALINE_DOOR);
        registerTallBlockItem(event, (Block) PUFFIUM_DOOR);
        registerTallBlockItem(event, (Block) UNKNOWN_DOOR);
        registerBlockItem(event, (Block) VAULT_RUNE_BLOCK);
        registerBlockItem(event, (Block) VAULT_ARTIFACT, 1);
        registerBlockItem(event, (Block) VAULT_CRATE, 1, Item.Properties::fireResistant);
        registerBlockItem(event, (Block) VAULT_CRATE_CAKE, 1, Item.Properties::fireResistant);
        registerBlockItem(event, (Block) VAULT_CRATE_ARENA, 1, Item.Properties::fireResistant);
        registerBlockItem(event, (Block) VAULT_CRATE_SCAVENGER, 1, Item.Properties::fireResistant);
        registerBlockItem(event, (Block) OBELISK, 1);
        registerBlockItem(event, (Block) VENDING_MACHINE, (BlockItem) VENDING_MACHINE_BLOCK_ITEM);
        registerBlockItem(event, (Block) ADVANCED_VENDING_MACHINE, (BlockItem) ADVANCED_VENDING_BLOCK_ITEM);
        registerBlockItem(event, (Block) VAULT_BEDROCK);
        registerBlockItem(event, VAULT_STONE);
        registerBlockItem(event, (Block) VAULT_GLASS);
        registerBlockItem(event, (Block) RELIC_STATUE, (BlockItem) RELIC_STATUE_BLOCK_ITEM);
        registerBlockItem(event, (Block) GIFT_NORMAL_STATUE, (BlockItem) GIFT_NORMAL_STATUE_BLOCK_ITEM);
        registerBlockItem(event, (Block) GIFT_MEGA_STATUE, (BlockItem) GIFT_MEGA_STATUE_BLOCK_ITEM);
        registerBlockItem(event, (Block) BOW_HAT, 1);
        registerBlockItem(event, (Block) STATUE_DRAGON_HEAD, 1);
        registerBlockItem(event, (Block) VAULT_PLAYER_LOOT_STATUE, (BlockItem) VAULT_PLAYER_LOOT_STATUE_BLOCK_ITEM);
        registerBlockItem(event, (Block) CRYO_CHAMBER);
        registerBlockItem(event, (Block) KEY_PRESS);
        registerBlockItem(event, VAULT_DIAMOND_BLOCK);
        registerBlockItem(event, (Block) PUZZLE_RUNE_BLOCK, (BlockItem) PUZZLE_RUNE_BLOCK_ITEM);
        registerBlockItem(event, YELLOW_PUZZLE_CONCRETE);
        registerBlockItem(event, PINK_PUZZLE_CONCRETE);
        registerBlockItem(event, GREEN_PUZZLE_CONCRETE);
        registerBlockItem(event, BLUE_PUZZLE_CONCRETE);
        registerBlockItem(event, (Block) OMEGA_STATUE, (BlockItem) OMEGA_STATUE_BLOCK_ITEM);
        registerBlockItem(event, (Block) OMEGA_STATUE_VARIANT, (BlockItem) OMEGA_STATUE_VARIANT_BLOCK_ITEM);
        registerBlockItem(event, (Block) TROPHY_STATUE, (BlockItem) TROPHY_STATUE_BLOCK_ITEM);
        registerBlockItem(event, (Block) TRANSMOG_TABLE);
        registerBlockItem(event, VAULT_LOOT_RICHITY);
        registerBlockItem(event, VAULT_LOOT_RESOURCE);
        registerBlockItem(event, VAULT_LOOT_MISC);
        registerBlockItem(event, UNKNOWN_VAULT_CHEST);
        registerBlockItem(event, UNKNOWN_TREASURE_CHEST);
        registerBlockItem(event, UNKNOWN_VAULT_OBJECTIVE);
        registerBlockItem(event, VAULT_CHEST, VAULT_CHEST_ITEM);
        registerBlockItem(event, VAULT_TREASURE_CHEST, VAULT_TREASURE_CHEST_ITEM);
        registerBlockItem(event, VAULT_ALTAR_CHEST, VAULT_ALTAR_CHEST_ITEM);
        registerBlockItem(event, VAULT_COOP_CHEST, VAULT_COOP_CHEST_ITEM);
        registerBlockItem(event, VAULT_BONUS_CHEST, VAULT_BONUS_CHEST_ITEM);
        registerBlockItem(event, (Block) XP_ALTAR);
        registerBlockItem(event, (Block) BLOOD_ALTAR);
        registerBlockItem(event, (Block) TIME_ALTAR);
        registerBlockItem(event, (Block) SOUL_ALTAR);
        registerBlockItem(event, (Block) STATUE_CAULDRON, 1);
        registerBlockItem(event, (Block) SCAVENGER_CHEST, SCAVENGER_CHEST_ITEM);
        registerBlockItem(event, (Block) SCAVENGER_TREASURE);
        registerBlockItem(event, (Block) STABILIZER);
        registerBlockItem(event, (Block) CATALYST_DECRYPTION_TABLE);
        registerBlockItem(event, (Block) ETCHING_CONTROLLER_BLOCK);
        registerBlockItem(event, (Block) VAULT_CHARM_CONTROLLER_BLOCK);
        registerBlockItem(event, (Block) RAID_CONTROLLER_BLOCK);
    }


    private static void registerBlock(RegistryEvent.Register<Block> event, Block block, ResourceLocation id) {
        block.setRegistryName(id);
        event.getRegistry().register( block);
    }

    private static <T extends net.minecraft.tileentity.TileEntity> void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event, TileEntityType<?> type, ResourceLocation id) {
        type.setRegistryName(id);
        event.getRegistry().register(type);
    }

    private static void registerBlockItemWithEffect(RegistryEvent.Register<Item> event, Block block, int maxStackSize, Consumer<Item.Properties> adjustProperties) {
        Item.Properties properties = (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).stacksTo(maxStackSize);
        adjustProperties.accept(properties);
        BlockItem blockItem = new BlockItem(block, properties) {
            public boolean isFoil(ItemStack stack) {
                return true;
            }
        };
        registerBlockItem(event, block, blockItem);
    }

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block) {
        registerBlockItem(event, block, 64);
    }

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block, int maxStackSize) {
        registerBlockItem(event, block, maxStackSize, properties -> {

        });
    }

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block, int maxStackSize, Consumer<Item.Properties> adjustProperties) {
        Item.Properties properties = (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).stacksTo(maxStackSize);
        adjustProperties.accept(properties);
        registerBlockItem(event, block, new BlockItem(block, properties));
    }

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block, BlockItem blockItem) {
        blockItem.setRegistryName(block.getRegistryName());
        event.getRegistry().register( blockItem);
    }


    private static void registerTallBlockItem(RegistryEvent.Register<Item> event, Block block) {
        TallBlockItem tallBlockItem = new TallBlockItem(block, (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).stacksTo(64));
        tallBlockItem.setRegistryName(block.getRegistryName());
        event.getRegistry().register(tallBlockItem);
    }

    @OnlyIn(Dist.CLIENT)
    private static class VaultStackRendererProvider
            implements Callable<ItemStackTileEntityRenderer> {
        public static final VaultStackRendererProvider INSTANCE = new VaultStackRendererProvider();


        public ItemStackTileEntityRenderer call() throws Exception {
            return (ItemStackTileEntityRenderer) VaultISTER.INSTANCE;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModBlocks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */