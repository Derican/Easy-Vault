package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.config.EffectConfig;
import iskallia.vault.world.vault.modifier.*;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedList;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VaultModifiersConfig extends Config {
    @Expose
    public List<MaxMobsModifier> MAX_MOBS_MODIFIERS;
    @Expose
    public List<TimerModifier> TIMER_MODIFIERS;
    @Expose
    public List<LevelModifier> LEVEL_MODIFIERS;
    @Expose
    public List<EffectModifier> EFFECT_MODIFIERS;
    @Expose
    public List<NoExitModifier> NO_EXIT_MODIFIERS;
    @Expose
    public List<ChestModifier> ADDITIONAL_CHEST_MODIFIERS;
    @Expose
    public List<ScaleModifier> SCALE_MODIFIERS;
    @Expose
    public List<FrenzyModifier> FRENZY_MODIFIERS;
    @Expose
    public List<ChestTrapModifier> TRAPPED_CHESTS_MODIFIERS;

    public String getName() {
        return "vault_modifiers";
    }

    @Expose
    public List<ArtifactChanceModifier> ARTIFACT_MODIFIERS;
    @Expose
    public List<CatalystChanceModifier> CATALYST_MODIFIERS;
    @Expose
    public List<LootableModifier> LOOTABLE_MODIFIERS;
    @Expose
    public List<InventoryRestoreModifier> INV_RESTORE_MODIFIERS;
    @Expose
    public List<CurseOnHitModifier> CURSE_ON_HIT_MODIFIERS;
    @Expose
    public List<DurabilityDamageModifier> DURABILITY_DAMAGE_MODIFIERS;
    @Expose
    public List<StatModifier> STAT_MODIFIERS;
    @Expose
    public List<VaultFruitPreventionModifier> VAULT_FRUIT_PREVENTION_MODIFIERS;
    @Expose
    public List<Level> LEVELS;
    @Expose
    public Map<String, List<String>> MODIFIER_PREVENTIONS;

    public List<VaultModifier> getAll() {
        return (List<VaultModifier>) Stream.<List>of(new List[]{this.MAX_MOBS_MODIFIERS, this.TIMER_MODIFIERS, this.LEVEL_MODIFIERS, this.EFFECT_MODIFIERS, this.NO_EXIT_MODIFIERS, this.ADDITIONAL_CHEST_MODIFIERS, this.SCALE_MODIFIERS, this.FRENZY_MODIFIERS, this.TRAPPED_CHESTS_MODIFIERS, this.ARTIFACT_MODIFIERS, this.CATALYST_MODIFIERS, this.LOOTABLE_MODIFIERS, this.INV_RESTORE_MODIFIERS, this.CURSE_ON_HIT_MODIFIERS, this.DURABILITY_DAMAGE_MODIFIERS, this.STAT_MODIFIERS, this.VAULT_FRUIT_PREVENTION_MODIFIERS


        }).flatMap(Collection::stream).collect(Collectors.toList());
    }


    public VaultModifier getByName(String name) {
        return getAll().stream().filter(group -> group.getName().equals(name)).findFirst().orElse(null);
    }


    protected void reset() {
        this.LEVELS = new ArrayList<>();

        this.MAX_MOBS_MODIFIERS = Arrays.asList(new MaxMobsModifier[]{new MaxMobsModifier("Silent",
                Vault.id("textures/gui/modifiers/silent.png"), -2), new MaxMobsModifier("Lonely",
                Vault.id("textures/gui/modifiers/lonely.png"), -1), new MaxMobsModifier("Crowded",
                Vault.id("textures/gui/modifiers/crowded.png"), 1), new MaxMobsModifier("Chaotic",
                Vault.id("textures/gui/modifiers/chaotic.png"), 2)});

        this.TIMER_MODIFIERS = Arrays.asList(new TimerModifier[]{new TimerModifier("Fast",
                Vault.id("textures/gui/modifiers/fast.png"), -6000), new TimerModifier("Rush",
                Vault.id("textures/gui/modifiers/rush.png"), -12000)});

        this.LEVEL_MODIFIERS = Arrays.asList(new LevelModifier[]{new LevelModifier("Easy",
                Vault.id("textures/gui/modifiers/easy.png"), -5), new LevelModifier("Hard",
                Vault.id("textures/gui/modifiers/hard.png"), 5)});

        this.EFFECT_MODIFIERS = Arrays.asList(new EffectModifier[]{new EffectModifier("Treasure",
                Vault.id("textures/gui/modifiers/treasure.png"), Effects.LUCK, 1, "ADD", EffectConfig.Type.ICON_ONLY), new EffectModifier("Unlucky",
                Vault.id("textures/gui/modifiers/unlucky.png"), Effects.UNLUCK, 1, "ADD", EffectConfig.Type.ICON_ONLY)});

        this.NO_EXIT_MODIFIERS = Arrays.asList(new NoExitModifier[]{new NoExitModifier("Raffle",
                Vault.id("textures/gui/modifiers/no_exit.png")), new NoExitModifier("Locked",
                Vault.id("textures/gui/modifiers/no_exit.png"))});

        this.ADDITIONAL_CHEST_MODIFIERS = Arrays.asList(new ChestModifier[]{new ChestModifier("Gilded",
                Vault.id("textures/gui/modifiers/treasure.png"), 1), new ChestModifier("Hoard",
                Vault.id("textures/gui/modifiers/treasure.png"), 2)});

        this.SCALE_MODIFIERS = Arrays.asList(new ScaleModifier[]{new ScaleModifier("Daycare",
                Vault.id("textures/gui/modifiers/daycare.png"), 0.5F), new ScaleModifier("Me Me Big Boi",
                Vault.id("textures/gui/modifiers/daycare.png"), 1.5F)});

        this.FRENZY_MODIFIERS = Arrays.asList(new FrenzyModifier[]{new FrenzyModifier("Frenzy",
                Vault.id("textures/gui/modifiers/frenzy.png"), 4.0F, 0.1F, true)});

        this.TRAPPED_CHESTS_MODIFIERS = Arrays.asList(new ChestTrapModifier[]{new ChestTrapModifier("Safe Zone",
                Vault.id("textures/gui/modifiers/safezone.png"), 0.0D), new ChestTrapModifier("Trapped",
                Vault.id("textures/gui/modifiers/trapped.png"), 1.5D)});

        this.ARTIFACT_MODIFIERS = Arrays.asList(new ArtifactChanceModifier[]{new ArtifactChanceModifier("Exploration",
                Vault.id("textures/gui/modifiers/more-artifact1.png"), 0.25F), new ArtifactChanceModifier("Odyssey",
                Vault.id("textures/gui/modifiers/more-artifact2.png"), 0.5F)});

        this.CATALYST_MODIFIERS = Arrays.asList(new CatalystChanceModifier[]{new CatalystChanceModifier("Prismatic",
                Vault.id("textures/gui/modifiers/more-catalyst.png"), 0.25F)});

        this.LOOTABLE_MODIFIERS = Arrays.asList(new LootableModifier[]{new LootableModifier("Plentiful",
                Vault.id("textures/gui/modifiers/treasure.png"), "ORE", LootableModifier.getDefaultOreModifiers(2.0F)), new LootableModifier("Rich",
                Vault.id("textures/gui/modifiers/treasure.png"), "ORE", LootableModifier.getDefaultOreModifiers(3.0F)), new LootableModifier("Copious",
                Vault.id("textures/gui/modifiers/treasure.png"), "ORE", LootableModifier.getDefaultOreModifiers(4.0F))});

        this.INV_RESTORE_MODIFIERS = Arrays.asList(new InventoryRestoreModifier[]{new InventoryRestoreModifier("Phoenix",
                Vault.id("textures/gui/modifiers/phoenix.png"), false), new InventoryRestoreModifier("Afterlife",
                Vault.id("textures/gui/modifiers/afterlife.png"), true)});

        this.CURSE_ON_HIT_MODIFIERS = Arrays.asList(new CurseOnHitModifier[]{new CurseOnHitModifier("Poison",
                Vault.id("textures/gui/modifiers/hex_poison.png"), Effects.POISON), new CurseOnHitModifier("Wither",
                Vault.id("textures/gui/modifiers/hex_wither.png"), Effects.WITHER), new CurseOnHitModifier("Chilling",
                Vault.id("textures/gui/modifiers/hex_chilling.png"), Effects.DIG_SLOWDOWN), new CurseOnHitModifier("Slow",
                Vault.id("textures/gui/modifiers/hex_slow.png"), Effects.MOVEMENT_SLOWDOWN)});

        this.DURABILITY_DAMAGE_MODIFIERS = Arrays.asList(new DurabilityDamageModifier[]{new DurabilityDamageModifier("Resilient",
                Vault.id("textures/gui/modifiers/resilient.png"), 0.7F), new DurabilityDamageModifier("Reinforced",
                Vault.id("textures/gui/modifiers/reinforced.png"), 0.4F), new DurabilityDamageModifier("Indestructible",
                Vault.id("textures/gui/modifiers/indestructible.png"), 0.0F)});

        this.STAT_MODIFIERS = Arrays.asList(new StatModifier[]{new StatModifier("NoParry",
                Vault.id("textures/gui/modifiers/phoenix.png"), StatModifier.Statistic.PARRY, 0.0F), new StatModifier("NoResistance",
                Vault.id("textures/gui/modifiers/phoenix.png"), StatModifier.Statistic.RESISTANCE, 0.0F), new StatModifier("NoCdr",
                Vault.id("textures/gui/modifiers/phoenix.png"), StatModifier.Statistic.COOLDOWN_REDUCTION, 0.0F)});

        this.VAULT_FRUIT_PREVENTION_MODIFIERS = Arrays.asList(new VaultFruitPreventionModifier[]{new VaultFruitPreventionModifier("NoFruit",
                Vault.id("textures/gui/modifiers/phoenix.png"))});


        Level level = new Level(5);

        level.DEFAULT_POOLS.addAll(Arrays.asList(new Pool[]{(new Pool(2, 2))

                .add("Crowded", 1)
                .add("Chaos", 1)
                .add("Fast", 1)
                .add("Rush", 1)
                .add("Easy", 1)
                .add("Hard", 1)
                .add("Treasure", 1)
                .add("Unlucky", 1), (new Pool(1, 1))

                .add("Locked", 1)
                .add("Dummy", 3)}));


        level.RAFFLE_POOLS.addAll(Arrays.asList(new Pool[]{(new Pool(2, 2))

                .add("Crowded", 1)
                .add("Chaos", 1)
                .add("Fast", 1)
                .add("Rush", 1)
                .add("Easy", 1)
                .add("Hard", 1)
                .add("Treasure", 1)
                .add("Unlucky", 1), (new Pool(1, 1))

                .add("Locked", 1)
                .add("Dummy", 3)}));


        level.RAID_POOLS.addAll(Arrays.asList(new Pool[]{(new Pool(2, 2))

                .add("Crowded", 1)
                .add("Chaos", 1)
                .add("Fast", 1)
                .add("Rush", 1)
                .add("Easy", 1)
                .add("Hard", 1)
                .add("Treasure", 1)
                .add("Unlucky", 1), (new Pool(1, 1))

                .add("Locked", 1)
                .add("Dummy", 3)}));


        this.LEVELS.add(level);

        this.MODIFIER_PREVENTIONS = new HashMap<>();

        List<String> preventedModifiers = new ArrayList<>();
        preventedModifiers.add("Locked");
        this.MODIFIER_PREVENTIONS.put(Vault.id("scavenger_hunt").toString(), preventedModifiers);
    }

    public Set<VaultModifier> getRandom(Random random, int level, ModifierPoolType type, @Nullable ResourceLocation objectiveKey) {
        List<Pool> pools;
        Level override = getForLevel(level);

        switch (type) {
            case RAFFLE:
                pools = override.RAFFLE_POOLS;
                break;
            case RAID:
                pools = override.RAID_POOLS;
                break;
            default:
                pools = override.DEFAULT_POOLS;
                break;
        }
        if (pools == null) {
            return new HashSet<>();
        }
        Set<VaultModifier> modifiers = new HashSet<>();
        pools.stream().map(pool -> pool.getRandom(random)).forEach(modifiers::addAll);
        if (objectiveKey != null) {
            List<String> preventedModifiers = this.MODIFIER_PREVENTIONS.getOrDefault(objectiveKey.toString(), Collections.emptyList());
            if (!preventedModifiers.isEmpty()) {
                modifiers.removeIf(modifier -> preventedModifiers.contains(modifier.getName()));
            }
        }
        return modifiers;
    }

    public Level getForLevel(int level) {
        for (int i = 0; i < this.LEVELS.size(); i++) {
            if (level < ((Level) this.LEVELS.get(i)).MIN_LEVEL) {
                if (i == 0)
                    break;
                return this.LEVELS.get(i - 1);
            }
            if (i == this.LEVELS.size() - 1) {
                return this.LEVELS.get(i);
            }
        }

        return Level.EMPTY;
    }

    public enum ModifierPoolType {
        DEFAULT,
        RAFFLE,
        RAID;
    }

    public static class Level {
        public static Level EMPTY = new Level(0);
        @Expose
        public int MIN_LEVEL;
        @Expose
        public List<VaultModifiersConfig.Pool> DEFAULT_POOLS;
        @Expose
        public List<VaultModifiersConfig.Pool> RAFFLE_POOLS;
        @Expose
        public List<VaultModifiersConfig.Pool> RAID_POOLS;

        public Level(int minLevel) {
            this.MIN_LEVEL = minLevel;
            this.DEFAULT_POOLS = new ArrayList<>();
            this.RAFFLE_POOLS = new ArrayList<>();
            this.RAID_POOLS = new ArrayList<>();
        }
    }

    public static class Pool {
        @Expose
        public int MIN_ROLLS;
        @Expose
        public int MAX_ROLLS;
        @Expose
        public WeightedList<String> POOL;

        public Pool(int min, int max) {
            this.MIN_ROLLS = min;
            this.MAX_ROLLS = max;
            this.POOL = new WeightedList();
        }

        public Pool add(String name, int weight) {
            this.POOL.add(name, weight);
            return this;
        }

        public Set<VaultModifier> getRandom(Random random) {
            int rolls = Math.min(this.MIN_ROLLS, this.MAX_ROLLS) + random.nextInt(Math.abs(this.MIN_ROLLS - this.MAX_ROLLS) + 1);

            Set<String> res = new HashSet<>();

            while (res.size() < rolls && res.size() < this.POOL.stream().count()) {
                res.add(this.POOL.getOne(random));
            }

            return (Set<VaultModifier>) res.stream().map(s -> ModConfigs.VAULT_MODIFIERS.getByName(s)).filter(Objects::nonNull).collect(Collectors.toSet());
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultModifiersConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */