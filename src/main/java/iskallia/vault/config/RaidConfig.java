package iskallia.vault.config;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.entity.EntityType;

import javax.annotation.Nullable;
import java.util.*;

public class RaidConfig extends Config {
    private static final Level DEFAULT;
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
        this.mobPools.put("ranged", Lists.newArrayList(new Level[]{new Level(0, new MobPool().add((EntityType<?>) EntityType.SKELETON, 1)), new Level(75, new MobPool().add((EntityType<?>) EntityType.SKELETON, 1).add((EntityType<?>) EntityType.STRAY, 1))}));
        this.mobPools.put("melee", Lists.newArrayList(new Level[]{new Level(0, new MobPool().add((EntityType<?>) EntityType.ZOMBIE, 1)), new Level(50, new MobPool().add((EntityType<?>) EntityType.ZOMBIE, 2).add((EntityType<?>) EntityType.VINDICATOR, 1))}));
        final WaveSetup waveSetup = new WaveSetup().addWave(new CompoundWave(new ConfiguredWave[]{new ConfiguredWave(2, 3, "ranged"), new ConfiguredWave(2, 3, "melee")})).addWave(new CompoundWave(new ConfiguredWave[]{new ConfiguredWave(4, 5, "ranged"), new ConfiguredWave(4, 6, "melee")})).addWave(new CompoundWave(new ConfiguredWave[]{new ConfiguredWave(6, 7, "ranged"), new ConfiguredWave(5, 8, "melee")}));
        this.raidWaves.add(waveSetup, 1);
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
        private final int level;
        @Expose
        private final float mobAmountMultiplier;

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
        private final int level;
        @Expose
        private final MobPool mobPool;

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
        private int min;
        @Expose
        private int max;
        @Expose
        private String mobPool;

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
