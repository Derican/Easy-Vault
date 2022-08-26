package iskallia.vault.config;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModEntities;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.entity.EntityType;

import javax.annotation.Nullable;
import java.util.*;

public class RaidConfig extends Config {
    public static final Level DEFAULT;
    @Expose
    private final Map<String, List<Level>> mobPools;
    @Expose
    private final WeightedList<WaveSetup> raidWaves;
    @Expose
    private final List<AmountLevel> amountLevels;

    public RaidConfig() {
        this.mobPools = new HashMap<String, List<Level>>();
        this.raidWaves = new WeightedList<WaveSetup>();
        this.amountLevels = new ArrayList<AmountLevel>();
    }

    @Nullable
    public MobPool getPool(final String pool, final int level) {
        final List<Level> mobLevelPool = this.mobPools.get(pool);
        if (mobLevelPool == null) {
            return null;
        }
        return this.getForLevel(mobLevelPool, level).orElse(RaidConfig.DEFAULT).mobPool;
    }

    public float getMobCountMultiplier(final int level) {
        return this.getAmountLevel(this.amountLevels, level).map(AmountLevel::getMobAmountMultiplier).orElse(1.0f);
    }

    @Nullable
    public WaveSetup getRandomWaveSetup() {
        return this.raidWaves.getRandom(RaidConfig.rand);
    }

    @Override
    public String getName() {
        return "raid";
    }

    @Override
    protected void reset() {
        this.mobPools.clear();
        this.raidWaves.clear();
        this.mobPools.put("fgroup1", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.STRAY, 4).add(ModEntities.VAULT_FIGHTER_TYPES.get(0), 2))));
        this.mobPools.put("fgroup2", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.STRAY, 4).add(EntityType.VINDICATOR, 2))));
        this.mobPools.put("fgroup3", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.PILLAGER, 4).add(EntityType.VINDICATOR, 2))));
        this.mobPools.put("fgroup4", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.VEX, 2).add(EntityType.VINDICATOR, 4))));
        this.mobPools.put("fgroup5", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.STRAY, 2).add(EntityType.SPIDER, 4))));
        this.mobPools.put("fgroup6", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.VEX, 2).add(EntityType.SPIDER, 4))));
        this.mobPools.put("fgroup7", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.RAVAGER, 2))));
        this.mobPools.put("fgroup8", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.PILLAGER, 2).add(EntityType.SPIDER, 4))));
        this.mobPools.put("fgroup9", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.VAULT_GUARDIAN, 2))));
        this.mobPools.put("fgroup10", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.RAVAGER, 1).add(EntityType.ZOMBIE, 6))));
        this.mobPools.put("fgroup11", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.RAVAGER, 1).add(ModEntities.VAULT_GUARDIAN, 6))));
        this.mobPools.put("fgroup12", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.BOOGIEMAN, 1).add(ModEntities.BLUE_BLAZE, 3))));
        this.mobPools.put("fgroup13", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.ROBOT, 1))));
        this.mobPools.put("fgroup14", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.ARENA_BOSS, 1))));
        this.mobPools.put("testgroup", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.MAGMA_CUBE, 4))));
        this.mobPools.put("wutax", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.ZOMBIE, 4).add(EntityType.SPIDER, 1).add(ModEntities.VAULT_FIGHTER_TYPES.get(0), 1)), new RaidConfig.Level(50, new RaidConfig.MobPool().add(EntityType.ZOMBIE, 16).add(EntityType.HUSK, 8).add(EntityType.SPIDER, 1).add(ModEntities.VAULT_FIGHTER_TYPES.get(1), 1).add(ModEntities.VAULT_FIGHTER_TYPES.get(0), 8)), new RaidConfig.Level(75, new RaidConfig.MobPool().add(EntityType.ZOMBIE, 20).add(EntityType.HUSK, 20).add(EntityType.VINDICATOR, 1).add(EntityType.SPIDER, 10).add(ModEntities.VAULT_FIGHTER_TYPES.get(1), 4)), new RaidConfig.Level(150, new RaidConfig.MobPool().add(EntityType.HUSK, 1600).add(EntityType.ZOMBIE, 800).add(EntityType.SPIDER, 800).add(EntityType.CAVE_SPIDER, 100).add(EntityType.VINDICATOR, 100).add(EntityType.RAVAGER, 1).add(ModEntities.VAULT_FIGHTER_TYPES.get(0), 100).add(ModEntities.VAULT_FIGHTER_TYPES.get(1), 100).add(ModEntities.VAULT_FIGHTER_TYPES.get(2), 60).add(ModEntities.VAULT_FIGHTER_TYPES.get(3), 40)), new RaidConfig.Level(250, new RaidConfig.MobPool().add(EntityType.PIGLIN_BRUTE, 400).add(EntityType.HUSK, 800).add(EntityType.ZOMBIE, 600).add(EntityType.SPIDER, 600).add(EntityType.CAVE_SPIDER, 100).add(EntityType.VINDICATOR, 100).add(EntityType.RAVAGER, 1).add(ModEntities.VAULT_FIGHTER_TYPES.get(1), 100).add(ModEntities.VAULT_FIGHTER_TYPES.get(2), 80).add(ModEntities.VAULT_FIGHTER_TYPES.get(3), 60).add(ModEntities.VAULT_FIGHTER_TYPES.get(4), 40))));
        this.mobPools.put("spiderbomb", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.SPIDER, 1)), new RaidConfig.Level(175, new RaidConfig.MobPool().add(EntityType.SPIDER, 3).add(EntityType.CAVE_SPIDER, 1))));
        this.mobPools.put("creepers", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.CREEPER, 1))));
        this.mobPools.put("troll", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.PIG, 1).add(EntityType.RABBIT, 1))));
        this.mobPools.put("silverfish", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.SILVERFISH, 1))));
        this.mobPools.put("guardians", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.PIG, 1)), new RaidConfig.Level(250, new RaidConfig.MobPool().add(ModEntities.VAULT_GUARDIAN, 1))));
        this.mobPools.put("goblins", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.TREASURE_GOBLIN, 1))));
        this.mobPools.put("ravagerfest", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.WITHER_SKELETON, 1)), new RaidConfig.Level(150, new RaidConfig.MobPool().add(EntityType.RAVAGER, 3))));
        this.mobPools.put("vexes", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.SKELETON, 1)), new RaidConfig.Level(115, new RaidConfig.MobPool().add(EntityType.STRAY, 3)), new RaidConfig.Level(150, new RaidConfig.MobPool().add(EntityType.VEX, 3))));
        this.mobPools.put("vaultfighters", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.VAULT_FIGHTER_TYPES.get(0), 1)), new RaidConfig.Level(50, new RaidConfig.MobPool().add(ModEntities.VAULT_FIGHTER_TYPES.get(0), 8).add(ModEntities.VAULT_FIGHTER_TYPES.get(1), 1)), new RaidConfig.Level(100, new RaidConfig.MobPool().add(ModEntities.VAULT_FIGHTER_TYPES.get(0), 8).add(ModEntities.VAULT_FIGHTER_TYPES.get(1), 2).add(ModEntities.VAULT_FIGHTER_TYPES.get(2), 1)), new RaidConfig.Level(150, new RaidConfig.MobPool().add(ModEntities.VAULT_FIGHTER_TYPES.get(0), 8).add(ModEntities.VAULT_FIGHTER_TYPES.get(1), 6).add(ModEntities.VAULT_FIGHTER_TYPES.get(2), 2).add(ModEntities.VAULT_FIGHTER_TYPES.get(3), 1)), new RaidConfig.Level(250, new RaidConfig.MobPool().add(ModEntities.VAULT_FIGHTER_TYPES.get(0), 8).add(ModEntities.VAULT_FIGHTER_TYPES.get(1), 4).add(ModEntities.VAULT_FIGHTER_TYPES.get(2), 6).add(ModEntities.VAULT_FIGHTER_TYPES.get(3), 4).add(ModEntities.VAULT_FIGHTER_TYPES.get(4), 2))));
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(2, 3, "fgroup1"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup2"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup3"))), 100);
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(2, 3, "fgroup1"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup3"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup4"))), 100);
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(2, 3, "fgroup5"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup2"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup6"))), 80);
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(2, 3, "fgroup1"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup2"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup7"))), 80);
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(2, 3, "fgroup2"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup8"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup9"))), 80);
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(2, 3, "fgroup2"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup3"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup10"))), 80);
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(2, 3, "fgroup9"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup9"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup11"))), 80);
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(2, 3, "fgroup14"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup12"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fgroup13"))), 80000);
        this.amountLevels.clear();
        this.amountLevels.add(new AmountLevel(0, 1.0f));
        this.amountLevels.add(new AmountLevel(50, 1.5f));
        this.amountLevels.add(new AmountLevel(100, 2.0f));
        this.amountLevels.add(new AmountLevel(150, 2.5f));
        this.amountLevels.add(new AmountLevel(200, 3.0f));
        this.amountLevels.add(new AmountLevel(250, 4.0f));
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

    private Optional<AmountLevel> getAmountLevel(final List<AmountLevel> levels, final int level) {
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

    static {
        DEFAULT = new Level(0, new MobPool());
    }

    public static class AmountLevel {
        @Expose
        public final int level;
        @Expose
        public final float mobAmountMultiplier;

        public AmountLevel(final int level, final float mobAmountMultiplier) {
            this.level = level;
            this.mobAmountMultiplier = mobAmountMultiplier;
        }

        public float getMobAmountMultiplier() {
            return this.mobAmountMultiplier;
        }
    }

    public static class Level {
        @Expose
        public final int level;
        @Expose
        public final MobPool mobPool;

        public Level(final int level, final MobPool mobPool) {
            this.level = level;
            this.mobPool = mobPool;
        }
    }

    public static class MobPool {
        @Expose
        private final WeightedList<String> mobs;

        public MobPool() {
            this.mobs = new WeightedList<String>();
        }

        public MobPool add(final EntityType<?> type, final int weight) {
            this.mobs.add(type.getRegistryName().toString(), weight);
            return this;
        }

        public WeightedList<String> getMobs() {
            return this.mobs;
        }

        public String getRandomMob() {
            return this.mobs.getRandom(Config.rand);
        }
    }

    public static class WaveSetup {
        @Expose
        private final List<CompoundWave> waves;

        public WaveSetup() {
            this.waves = new ArrayList<CompoundWave>();
        }

        public WaveSetup addWave(final CompoundWave wave) {
            this.waves.add(wave);
            return this;
        }

        public List<CompoundWave> getWaves() {
            return this.waves;
        }
    }

    public static class CompoundWave {
        @Expose
        private final List<ConfiguredWave> waveMobs;

        public CompoundWave(final ConfiguredWave... waveMobs) {
            (this.waveMobs = new ArrayList<ConfiguredWave>()).addAll(Arrays.asList(waveMobs));
        }

        public List<ConfiguredWave> getWaveMobs() {
            return this.waveMobs;
        }
    }

    public static class ConfiguredWave {
        @Expose
        private final int min;
        @Expose
        private final int max;
        @Expose
        private final String mobPool;

        public ConfiguredWave(final int min, final int max, final String mobPool) {
            this.min = min;
            this.max = max;
            this.mobPool = mobPool;
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        public String getMobPool() {
            return this.mobPool;
        }
    }
}
