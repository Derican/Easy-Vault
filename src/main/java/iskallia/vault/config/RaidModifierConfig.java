package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.logic.objective.raid.modifier.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RaidModifierConfig extends Config {
    @Expose
    private List<DamageTakenModifier> DAMAGE_TAKEN_MODIFIERS;
    @Expose
    private List<MonsterAmountModifier> MONSTER_AMOUNT_MODIFIERS;
    @Expose
    private List<MonsterDamageModifier> MONSTER_DAMAGE_MODIFIERS;
    @Expose
    private List<MonsterHealthModifier> MONSTER_HEALTH_MODIFIERS;
    @Expose
    private List<MonsterSpeedModifier> MONSTER_SPEED_MODIFIERS;
    @Expose
    private List<MonsterLevelModifier> MONSTER_LEVEL_MODIFIERS;
    @Expose
    private List<BlockPlacementModifier> BLOCK_PLACEMENT_MODIFIERS;
    @Expose
    private List<FloatingItemModifier> ITEM_PLACEMENT_MODIFIERS;
    @Expose
    private List<ArtifactFragmentModifier> ARTIFACT_FRAGMENT_MODIFIERS;
    @Expose
    private List<ModifierDoublingModifier> MODIFIER_DOUBLING_MODIFIERS;
    @Expose
    private List<Level> positiveLevels;
    @Expose
    private List<Level> negativeLevels;

    public RaidModifierConfig() {
        this.DAMAGE_TAKEN_MODIFIERS = new ArrayList<DamageTakenModifier>();
        this.MONSTER_AMOUNT_MODIFIERS = new ArrayList<MonsterAmountModifier>();
        this.MONSTER_DAMAGE_MODIFIERS = new ArrayList<MonsterDamageModifier>();
        this.MONSTER_HEALTH_MODIFIERS = new ArrayList<MonsterHealthModifier>();
        this.MONSTER_SPEED_MODIFIERS = new ArrayList<MonsterSpeedModifier>();
        this.MONSTER_LEVEL_MODIFIERS = new ArrayList<MonsterLevelModifier>();
        this.BLOCK_PLACEMENT_MODIFIERS = new ArrayList<BlockPlacementModifier>();
        this.ITEM_PLACEMENT_MODIFIERS = new ArrayList<FloatingItemModifier>();
        this.ARTIFACT_FRAGMENT_MODIFIERS = new ArrayList<ArtifactFragmentModifier>();
        this.MODIFIER_DOUBLING_MODIFIERS = new ArrayList<ModifierDoublingModifier>();
        this.positiveLevels = new ArrayList<Level>();
        this.negativeLevels = new ArrayList<Level>();
    }

    public List<RaidModifier> getAll() {
        Stream<List<RaidModifier>> stream = Stream.<List<RaidModifier>>of(new List[]{this.DAMAGE_TAKEN_MODIFIERS, this.MONSTER_AMOUNT_MODIFIERS, this.MONSTER_DAMAGE_MODIFIERS, this.MONSTER_HEALTH_MODIFIERS, this.MONSTER_LEVEL_MODIFIERS, this.BLOCK_PLACEMENT_MODIFIERS, this.MONSTER_SPEED_MODIFIERS, this.ITEM_PLACEMENT_MODIFIERS, this.ARTIFACT_FRAGMENT_MODIFIERS, this.MODIFIER_DOUBLING_MODIFIERS
        });
        Stream<RaidModifier> stream1 = stream.flatMap(Collection::stream);
        List<RaidModifier> list = stream1.collect(Collectors.toList());
        return list;
//        return (List<RaidModifier>) Stream.of((List[])new List[] { this.DAMAGE_TAKEN_MODIFIERS, this.MONSTER_AMOUNT_MODIFIERS, this.MONSTER_DAMAGE_MODIFIERS, this.MONSTER_HEALTH_MODIFIERS, this.MONSTER_LEVEL_MODIFIERS, this.BLOCK_PLACEMENT_MODIFIERS, this.MONSTER_SPEED_MODIFIERS, this.ITEM_PLACEMENT_MODIFIERS, this.ARTIFACT_FRAGMENT_MODIFIERS, this.MODIFIER_DOUBLING_MODIFIERS }).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Nullable
    public RaidModifier getByName(final String name) {
        return this.getAll().stream().filter(modifier -> modifier.getName().equals(name)).findFirst().orElse(null);
    }

    public Optional<RollableModifier> getRandomModifier(final int level, final boolean positive, final boolean preventArtifacts) {
        final List<Level> levels = positive ? this.positiveLevels : this.negativeLevels;
        return this.getForLevel(levels, level).map(modifierLevel -> {
            WeightedList<RollableModifier> modifierList = modifierLevel.modifiers;
            if (preventArtifacts) {
                modifierList = modifierList.copyFiltered(modifierName -> {
                    final RaidModifier modifier = this.getByName(modifierName.modifier);
                    return !(modifier instanceof ArtifactFragmentModifier);
                });
            }
            return (RollableModifier) modifierList.getRandom(RaidModifierConfig.rand);
        });
    }

    @Override
    public String getName() {
        return "raid_modifiers";
    }

    @Override
    protected void reset() {
        (this.DAMAGE_TAKEN_MODIFIERS = new ArrayList<DamageTakenModifier>()).add(new DamageTakenModifier("damageTaken"));
        (this.MONSTER_AMOUNT_MODIFIERS = new ArrayList<MonsterAmountModifier>()).add(new MonsterAmountModifier("monsterAmount"));
        (this.MONSTER_DAMAGE_MODIFIERS = new ArrayList<MonsterDamageModifier>()).add(new MonsterDamageModifier("monsterDamage"));
        (this.MONSTER_HEALTH_MODIFIERS = new ArrayList<MonsterHealthModifier>()).add(new MonsterHealthModifier("monsterHealth"));
        (this.MONSTER_SPEED_MODIFIERS = new ArrayList<MonsterSpeedModifier>()).add(new MonsterSpeedModifier("monsterSpeed"));
        (this.MONSTER_LEVEL_MODIFIERS = new ArrayList<MonsterLevelModifier>()).add(new MonsterLevelModifier("monsterLevel"));
        (this.BLOCK_PLACEMENT_MODIFIERS = new ArrayList<BlockPlacementModifier>()).add(new BlockPlacementModifier("gildedChests", ModBlocks.VAULT_BONUS_CHEST, 5, "Gilded Chests"));
        (this.ITEM_PLACEMENT_MODIFIERS = new ArrayList<FloatingItemModifier>()).add(new FloatingItemModifier("vaultGems", 10, FloatingItemModifier.defaultGemList(), "Vault Gems"));
        (this.ARTIFACT_FRAGMENT_MODIFIERS = new ArrayList<ArtifactFragmentModifier>()).add(new ArtifactFragmentModifier("artifactFragment"));
        (this.MODIFIER_DOUBLING_MODIFIERS = new ArrayList<ModifierDoublingModifier>()).add(new ModifierDoublingModifier("modifierDoubling"));
        (this.positiveLevels = new ArrayList<Level>()).add(new Level(0, new WeightedList<RollableModifier>().add(new RollableModifier("gildedChests", 1.0f, 1.0f), 1).add(new RollableModifier("vaultGems", 1.0f, 1.0f), 1).add(new RollableModifier("artifactFragment", 0.01f, 0.05f), 1)));
        (this.negativeLevels = new ArrayList<Level>()).add(new Level(0, new WeightedList<RollableModifier>().add(new RollableModifier("monsterAmount", 0.15f, 0.25f), 1).add(new RollableModifier("monsterLevel", 10.0f, 25.0f), 1)));
    }

    private Optional<Level> getForLevel(final List<Level> levels, final int level) {
        int i = 0;
        while (i < levels.size()) {
            if (level < levels.get(i).level) {
                if (i == 0) {
                    break;
                }
                return Optional.of(levels.get(i - 1));
            } else {
                if (i == levels.size() - 1) {
                    return Optional.of(levels.get(i));
                }
                ++i;
            }
        }
        return Optional.empty();
    }

    public static class Level {
        @Expose
        private final int level;
        @Expose
        private final WeightedList<RollableModifier> modifiers;

        public Level(final int level, final WeightedList<RollableModifier> modifiers) {
            this.level = level;
            this.modifiers = modifiers;
        }
    }

    public static class RollableModifier {
        @Expose
        private String modifier;
        @Expose
        private float minValue;
        @Expose
        private float maxValue;

        public RollableModifier(final String modifier, final float minValue, final float maxValue) {
            this.modifier = modifier;
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        @Nullable
        public RaidModifier getModifier() {
            return ModConfigs.RAID_MODIFIER_CONFIG.getByName(this.modifier);
        }

        public float getRandomValue() {
            final RaidModifier modifier = this.getModifier();
            if (modifier == null) {
                return 0.0f;
            }
            final float value = MathUtilities.randomFloat(this.minValue, this.maxValue);
            if (modifier.isPercentage()) {
                return Math.round(value * 100.0f) / 100.0f;
            }
            return (float) Math.round(value);
        }
    }
}
