package iskallia.vault.config;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.entity.EntityType;

import javax.annotation.Nullable;
import java.util.*;

public class RaidConfig extends Config {
    private static final Level DEFAULT = new Level(0, new MobPool());
    @Expose
    private final Map<String, List<Level>> mobPools = new HashMap<>();
    @Expose
    private final WeightedList<WaveSetup> raidWaves = new WeightedList();
    @Expose
    private final List<AmountLevel> amountLevels = new ArrayList<>();

    @Nullable
    public MobPool getPool(String pool, int level) {
        List<Level> mobLevelPool = this.mobPools.get(pool);
        if (mobLevelPool == null) {
            return null;
        }
        return (getForLevel(mobLevelPool, level).orElse(DEFAULT)).mobPool;
    }

    public float getMobCountMultiplier(int level) {
        return ((Float) getAmountLevel(this.amountLevels, level)
                .<Float>map(AmountLevel::getMobAmountMultiplier)
                .orElse(Float.valueOf(1.0F))).floatValue();
    }

    @Nullable
    public WaveSetup getRandomWaveSetup() {
        return (WaveSetup) this.raidWaves.getRandom(rand);
    }


    public String getName() {
        return "raid";
    }


    protected void reset() {
        this.mobPools.clear();
        this.raidWaves.clear();

        this.mobPools.put("ranged", Lists.newArrayList( new Level[]{new Level(0, (new MobPool())

                .add(EntityType.SKELETON, 1)), new Level(75, (new MobPool())

                .add(EntityType.SKELETON, 1)
                .add(EntityType.STRAY, 1))}));


        this.mobPools.put("melee", Lists.newArrayList( new Level[]{new Level(0, (new MobPool())

                .add(EntityType.ZOMBIE, 1)), new Level(50, (new MobPool())

                .add(EntityType.ZOMBIE, 2)
                .add(EntityType.VINDICATOR, 1))}));


        WaveSetup waveSetup = (new WaveSetup()).addWave(new CompoundWave(new ConfiguredWave[]{new ConfiguredWave(2, 3, "ranged"), new ConfiguredWave(2, 3, "melee")})).addWave(new CompoundWave(new ConfiguredWave[]{new ConfiguredWave(4, 5, "ranged"), new ConfiguredWave(4, 6, "melee")})).addWave(new CompoundWave(new ConfiguredWave[]{new ConfiguredWave(6, 7, "ranged"), new ConfiguredWave(5, 8, "melee")}));
        this.raidWaves.add(waveSetup, 1);

        this.amountLevels.clear();
        this.amountLevels.add(new AmountLevel(0, 1.0F));
        this.amountLevels.add(new AmountLevel(50, 1.5F));
        this.amountLevels.add(new AmountLevel(100, 2.0F));
        this.amountLevels.add(new AmountLevel(150, 2.5F));
        this.amountLevels.add(new AmountLevel(200, 3.0F));
        this.amountLevels.add(new AmountLevel(250, 4.0F));
    }

    private Optional<Level> getForLevel(List<Level> levels, int level) {
        for (int i = 0; i < levels.size(); i++) {
            if (level < ((Level) levels.get(i)).level) {
                if (i == 0) {
                    break;
                }
                return Optional.of(levels.get(i - 1));
            }
            if (i == levels.size() - 1) {
                return Optional.of(levels.get(i));
            }
        }
        return Optional.empty();
    }

    private Optional<AmountLevel> getAmountLevel(List<AmountLevel> levels, int level) {
        for (int i = 0; i < levels.size(); i++) {
            if (level < ((AmountLevel) levels.get(i)).level) {
                if (i == 0) {
                    break;
                }
                return Optional.of(levels.get(i - 1));
            }
            if (i == levels.size() - 1) {
                return Optional.of(levels.get(i));
            }
        }
        return Optional.empty();
    }

    public static class AmountLevel {
        @Expose
        private final int level;
        @Expose
        private final float mobAmountMultiplier;

        public AmountLevel(int level, float mobAmountMultiplier) {
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
        private final RaidConfig.MobPool mobPool;

        public Level(int level, RaidConfig.MobPool mobPool) {
            this.level = level;
            this.mobPool = mobPool;
        }
    }

    public static class MobPool {
        @Expose
        private final WeightedList<String> mobs = new WeightedList();

        public MobPool add(EntityType<?> type, int weight) {
            this.mobs.add(type.getRegistryName().toString(), weight);
            return this;
        }

        public WeightedList<String> getMobs() {
            return this.mobs;
        }

        public String getRandomMob() {
            return (String) this.mobs.getRandom(Config.rand);
        }
    }

    public static class WaveSetup {
        @Expose
        private final List<RaidConfig.CompoundWave> waves = new ArrayList<>();

        public WaveSetup addWave(RaidConfig.CompoundWave wave) {
            this.waves.add(wave);
            return this;
        }

        public List<RaidConfig.CompoundWave> getWaves() {
            return this.waves;
        }
    }

    public static class CompoundWave {
        @Expose
        private final List<RaidConfig.ConfiguredWave> waveMobs = new ArrayList<>();

        public CompoundWave(RaidConfig.ConfiguredWave... waveMobs) {
            this.waveMobs.addAll(Arrays.asList(waveMobs));
        }

        public List<RaidConfig.ConfiguredWave> getWaveMobs() {
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

        public ConfiguredWave(int min, int max, String mobPool) {
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\RaidConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */