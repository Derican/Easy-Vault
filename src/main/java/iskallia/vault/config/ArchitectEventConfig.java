package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.RangeEntry;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.logic.objective.architect.modifier.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArchitectEventConfig extends Config {
    public static final String EVENT_KEY = "architect_event";
    @Expose
    private boolean enabled;
    @Expose
    private List<BlockPlacementModifier> BLOCK_PLACEMENT;
    @Expose
    private List<FloatingItemPlacementModifier> FLOATING_ITEM_PLACEMENT;
    @Expose
    private List<TimeModifyModifier> TIME_MODIFIER;
    @Expose
    private List<MobSpawnModifier> ADDITIONAL_MOB_SPAWNS;
    @Expose
    private List<PieceSelectionModifier> ROOM_SELECTION;
    @Expose
    private List<RandomVoteModifier> RANDOM;
    @Expose
    private BossExitModifier BOSS;
    @Expose
    private RangeEntry requiredPolls;
    @Expose
    private WeightedList<String> rolls;

    public List<VoteModifier> getAll() {
        Stream<List<VoteModifier>> stream = Stream.of(new List[]{this.BLOCK_PLACEMENT, this.FLOATING_ITEM_PLACEMENT, this.TIME_MODIFIER, this.ADDITIONAL_MOB_SPAWNS, this.ROOM_SELECTION, this.RANDOM});
        Stream<VoteModifier> stream1 = stream.flatMap(Collection::stream);
        List<VoteModifier> modifiers = stream1.collect(Collectors.toList());
//        final List<VoteModifier> modifiers = (List<VoteModifier>) Stream.of(new List[] { this.BLOCK_PLACEMENT, this.FLOATING_ITEM_PLACEMENT, this.TIME_MODIFIER, this.ADDITIONAL_MOB_SPAWNS, this.ROOM_SELECTION, this.RANDOM }).flatMap(Collection::stream).collect(Collectors.toList());
        modifiers.add(this.BOSS);
        return modifiers;
    }

    @Nullable
    public VoteModifier getModifier(final String modifierName) {
        if (modifierName == null) {
            return null;
        }
        return this.getAll().stream().filter(modifier -> modifierName.equalsIgnoreCase(modifier.getName())).findFirst().orElse(null);
    }

    public VoteModifier getBossModifier() {
        return this.BOSS;
    }

    @Nullable
    public VoteModifier generateRandomModifier() {
        return this.getModifier(this.rolls.getRandom(ArchitectEventConfig.rand));
    }

    public int getRandomTotalRequiredPolls() {
        return this.requiredPolls.getRandom();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getName() {
        return "architect_event";
    }

    @Override
    protected void reset() {
        this.BLOCK_PLACEMENT = new ArrayList<>();
        this.BLOCK_PLACEMENT.add(new BlockPlacementModifier("gilded", "Gilded Chests", 15, ModBlocks.VAULT_BONUS_CHEST, 6000));
        this.BLOCK_PLACEMENT.add(new BlockPlacementModifier("gilded2", "Gilded Chests x2", 30, ModBlocks.VAULT_BONUS_CHEST, 4600));
        this.BLOCK_PLACEMENT.add(new BlockPlacementModifier("gilded3", "Gilded Chests x4", 45, ModBlocks.VAULT_BONUS_CHEST, 1600));
        this.BLOCK_PLACEMENT.add(new BlockPlacementModifier("treasure", "Treasure Chest", 80, ModBlocks.VAULT_TREASURE_CHEST, 80000));
        this.BLOCK_PLACEMENT.add(new BlockPlacementModifier("treasure2", "Treasure Chest x2", 140, ModBlocks.VAULT_TREASURE_CHEST, 40000));
        this.FLOATING_ITEM_PLACEMENT = new ArrayList<>();
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Gems", "Gems", 15, 3200, FloatingItemPlacementModifier.defaultGemList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Gems2", "Gems+", 30, 1600, FloatingItemPlacementModifier.defaultGemList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("PerfectGems", "Perfect Gems", 60, 4200, FloatingItemPlacementModifier.perfectGemList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Pog", "POG Hunt", 60, 45000, FloatingItemPlacementModifier.defaultPogList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Wutax", "Wutax Shards", 15, 8000, FloatingItemPlacementModifier.defaultWutaxList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Wutax2", "Wutax Shards+", 30, 5000, FloatingItemPlacementModifier.defaultWutaxList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Wutax3", "Wutax Shards++", 45, 2400, FloatingItemPlacementModifier.defaultWutaxList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Essence", "Essence", 30, 30000, FloatingItemPlacementModifier.defaultEssenceList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Essence2", "Essence+", 45, 20000, FloatingItemPlacementModifier.defaultEssenceList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Essence3", "Essence++", 60, 10000, FloatingItemPlacementModifier.defaultEssenceList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("XP", "XP", 10, 8000, FloatingItemPlacementModifier.defaultXpList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("XP2", "XP+", 20, 12000, FloatingItemPlacementModifier.defaultXpList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("XP3", "XP++", 30, 16000, FloatingItemPlacementModifier.defaultXpList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Bronze", "Vault Gold", 5, 4000, FloatingItemPlacementModifier.defaultBronzeList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Bronze2", "Vault Gold+", 15, 4000, FloatingItemPlacementModifier.defaultBronzeList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Bronze3", "Vault Gold++", 30, 3000, FloatingItemPlacementModifier.defaultBronzeList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Keys", "Treasure Key Hunt", 120, 90000, FloatingItemPlacementModifier.defaultKeysList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("gamba", "Goblin's Gamble", 240, 6000, FloatingItemPlacementModifier.defaultGambaList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("gamba2", "Goblin's Gamble", 240, 4000, FloatingItemPlacementModifier.defaultGambaList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Repaircores", "Repair Cores", 90, 20000, FloatingItemPlacementModifier.defaultRepairCoreList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Voidorb", "Void Orbs", 60, 16000, FloatingItemPlacementModifier.defaultVoidOrbList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Knowledge", "Knowledge", 30, 14000, FloatingItemPlacementModifier.defaultKnowledgeList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Knowledge2", "Knowledge+", 45, 10000, FloatingItemPlacementModifier.defaultKnowledgeList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Knowledge2", "Knowledge++", 60, 6000, FloatingItemPlacementModifier.defaultKnowledgeList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Skill", "Skillpoints", 45, 14000, FloatingItemPlacementModifier.defaultSkillList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Skill2", "Skillpoints+", 75, 8000, FloatingItemPlacementModifier.defaultSkillList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("Skill3", "Skillpoints++", 90, 6000, FloatingItemPlacementModifier.defaultSkillList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("prismatic", "Prismatic", 10, 32000, FloatingItemPlacementModifier.defaultPrismaticList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("prismatic2", "Prismatic x2", 30, 12000, FloatingItemPlacementModifier.defaultPrismaticList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("prismatic3", "Prismatic x4", 60, 8000, FloatingItemPlacementModifier.defaultPrismaticList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("vaultgear", "Vault Gear", 15, 14000, FloatingItemPlacementModifier.defaultVaultGearList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("vaultgear2", "Vault Gear x2", 20, 8000, FloatingItemPlacementModifier.defaultVaultGearList()));
        this.FLOATING_ITEM_PLACEMENT.add(new FloatingItemPlacementModifier("vaultgear3", "Vault Gear x4", 30, 4000, FloatingItemPlacementModifier.defaultVaultGearList()));
        this.TIME_MODIFIER = new ArrayList<>();
        this.TIME_MODIFIER.add(new TimeModifyModifier("moretime3", "+3 Minutes", 60, 3600));
        this.TIME_MODIFIER.add(new TimeModifyModifier("moretime2", "+1 Minute", 30, 1200));
        this.TIME_MODIFIER.add(new TimeModifyModifier("MoreTime2", "+2 Minutes", 60, 2400));
        this.TIME_MODIFIER.add(new TimeModifyModifier("moretime1", "+30 Seconds", 5, 600));
        this.TIME_MODIFIER.add(new TimeModifyModifier("lesstime3", "-3 Minutes", -120, -3600));
        this.TIME_MODIFIER.add(new TimeModifyModifier("LessTime2", "-2 Minutes", -90, -2400));
        this.TIME_MODIFIER.add(new TimeModifyModifier("lesstime2", "-1 Minute", -45, -1200));
        this.TIME_MODIFIER.add(new TimeModifyModifier("lesstime1", "-30 Seconds", -15, -600));
        this.ADDITIONAL_MOB_SPAWNS = new ArrayList<>();
        this.ADDITIONAL_MOB_SPAWNS.add(new MobSpawnModifier("Peaceful", "Peaceful", -5, 200000));
        this.ADDITIONAL_MOB_SPAWNS.add(new MobSpawnModifier("Peaceful2", "Peaceful+", -5, 200000));
        this.ADDITIONAL_MOB_SPAWNS.add(new MobSpawnModifier("Peaceful3", "Peaceful++", -10, 200000));
        this.ADDITIONAL_MOB_SPAWNS.add(new MobSpawnModifier("crowded", "Crowded", -10, 6000));
        this.ADDITIONAL_MOB_SPAWNS.add(new MobSpawnModifier("crowded2", "Crowded x2", -20, 3000));
        this.ADDITIONAL_MOB_SPAWNS.add(new MobSpawnModifier("crowded3", "Crowded x4", -60, 1200));
        this.ADDITIONAL_MOB_SPAWNS.add(new MobSpawnModifier("Mobs4", "Chaotic", -50, 1000));
        this.ADDITIONAL_MOB_SPAWNS.add(new MobSpawnModifier("Mobs5", "Chaotic+", -60, 800));
        this.ADDITIONAL_MOB_SPAWNS.add(new MobSpawnModifier("Mobs6", "Chaotic++", -60, 600));
        this.ROOM_SELECTION = new ArrayList<>();
        this.ROOM_SELECTION.add(new PieceSelectionModifier("omegaroom", "Random Omega Room", 30, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/mineshaft/", "the_vault:vault/enigma/rooms/x_spot/", "the_vault:vault/enigma/rooms/digsite/", "the_vault:vault/enigma/rooms/viewer/", "the_vault:vault/enigma/rooms/puzzle_cube/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("OmegaRooms2", "25% Omega Room", 15, 0.25f, Arrays.asList("the_vault:vault/enigma/rooms/mineshaft/", "the_vault:vault/enigma/rooms/x_spot/", "the_vault:vault/enigma/rooms/digsite/", "the_vault:vault/enigma/rooms/viewer/", "the_vault:vault/enigma/rooms/puzzle_cube/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("mine", "Mine", 90, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/mineshaft/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("digsite", "Digsite", 60, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/digsite/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("xmark", "X-Mark Room", 15, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/x_spot/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("viewer", "Viewer Room", 30, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/viewer/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("puzzle", "Puzzle Room", 30, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/puzzle_cube/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("vendor", "Vendor Room", 60, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/vendor/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("crystalcaves", "Crystal Caves", 15, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/crystal_caves/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("graves", "Graveyard", 5, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/graves/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("wildwest", "Wild West", 0, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/wildwest/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("mushroomforest", "Mushroom Forest", -5, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/mushroom_forest/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("caverns", "Caverns", -5, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/lava/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("biomes", "Biomes", -5, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/forest/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("piratecove", "Pirate Coves", -10, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/pirate_cove/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("rainbowforest", "Rainbow Forest", -5, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/rainbow_forest/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("lakes", "Lakes", -5, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/lakes/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("village", "Village", 10, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/village/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("dragon", "Dragon Lair", 15, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/contest_dragon/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("pixel", "Pixel Art", 60, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/contest_pixel/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("fishtank", "Aquarium", 10, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/contest_fishtank/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("alien", "End World", 10, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/contest_alien/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("web", "Spider Nest", 0, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/contest_web/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("tree", "Mega Tree", 5, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/contest_tree/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("oriental", "Cherry Blossoms", -5, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/contest_oriental/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("mustard", "Yellow Brick Road", -5, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/contest_mustard/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("birdcage", "Ancient Temple", 0, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/contest_birdcage/")));
        this.ROOM_SELECTION.add(new PieceSelectionModifier("city", "City Streets", 0, 1.0f, Arrays.asList("the_vault:vault/enigma/rooms/contest_city/")));
        this.RANDOM = new ArrayList<>();
        this.RANDOM.add(new RandomVoteModifier("random", "Random", -10));
        this.RANDOM.add(new RandomVoteModifier("random2", "Random", 10));
        this.RANDOM.add(new RandomVoteModifier("Random3", "Random", 5));
        this.RANDOM.add(new RandomVoteModifier("Random4", "Random", -30));
        this.BOSS = new BossExitModifier("BossExit", "Exit Vault", 0, 1.0f);
        this.requiredPolls = new RangeEntry(24, 38);
        this.enabled = false;
        this.rolls = new WeightedList<>();
        this.rolls.add("gilded", 8);
        this.rolls.add("gilded2", 4);
        this.rolls.add("gilded3", 2);
        this.rolls.add("crowded", 16);
        this.rolls.add("crowded2", 12);
        this.rolls.add("crowded3", 8);
        this.rolls.add("moretime1", 12);
        this.rolls.add("moretime2", 6);
        this.rolls.add("moretime3", 2);
        this.rolls.add("lesstime1", 16);
        this.rolls.add("lesstime2", 6);
        this.rolls.add("lesstime1", 2);
        this.rolls.add("treasure", 2);
        this.rolls.add("treasure2", 1);
        this.rolls.add("prismatic", 8);
        this.rolls.add("prismatic2", 4);
        this.rolls.add("prismatic3", 2);
        this.rolls.add("vaultgear", 8);
        this.rolls.add("vaultgear2", 4);
        this.rolls.add("mine", 1);
        this.rolls.add("digsite", 2);
        this.rolls.add("viewer", 6);
        this.rolls.add("puzzle", 4);
        this.rolls.add("xmark", 6);
        this.rolls.add("omegaroom", 7);
        this.rolls.add("lakes", 20);
        this.rolls.add("wildwest", 12);
        this.rolls.add("crystalcaves", 10);
        this.rolls.add("village", 14);
        this.rolls.add("mushroomforest", 20);
        this.rolls.add("random", 18);
        this.rolls.add("random2", 18);
        this.rolls.add("caverns", 20);
        this.rolls.add("rainbowforest", 20);
        this.rolls.add("piratecove", 20);
        this.rolls.add("biomes", 20);
        this.rolls.add("tree", 12);
        this.rolls.add("pixel", 6);
        this.rolls.add("dragon", 10);
        this.rolls.add("vendor", 3);
        this.rolls.add("graves", 10);
        this.rolls.add("fishtank", 10);
        this.rolls.add("alien", 12);
        this.rolls.add("web", 14);
        this.rolls.add("oriental", 16);
        this.rolls.add("city", 10);
        this.rolls.add("birdcage", 10);
        this.rolls.add("mustard", 16);
    }
}
