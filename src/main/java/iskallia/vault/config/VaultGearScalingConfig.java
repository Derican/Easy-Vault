package iskallia.vault.config;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import iskallia.vault.util.data.WeightedList;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VaultGearScalingConfig extends Config {
    @Expose
    private final Map<String, List<Level>> pooledRarityOutcomes;

    public VaultGearScalingConfig() {
        this.pooledRarityOutcomes = new HashMap<String, List<Level>>();
    }

    @Override
    public String getName() {
        return "vault_gear_scaling";
    }

    @Override
    protected void reset() {
        this.pooledRarityOutcomes.clear();
        final Level defaultLevel = new Level(0, new WeightedList<GearRarityOutcome>().add(new GearRarityOutcome(0, "Scrappy"), 1));
        this.pooledRarityOutcomes.put("Scaling", Lists.newArrayList(new Level[]{defaultLevel}));
    }

    @Nullable
    public GearRarityOutcome getGearRollType(final String pool, final int playerLevel) {
        final List<Level> levelConfig = this.pooledRarityOutcomes.get(pool);
        if (levelConfig == null) {
            return null;
        }
        final Level level = this.getForLevel(levelConfig, playerLevel);
        if (level == null) {
            return null;
        }
        return level.outcomes.getRandom(VaultGearScalingConfig.rand);
    }

    @Nullable
    public Level getForLevel(final List<Level> levels, final int level) {
        int i = 0;
        while (i < levels.size()) {
            if (level < levels.get(i).level) {
                if (i == 0) {
                    break;
                }
                return levels.get(i - 1);
            } else {
                if (i == levels.size() - 1) {
                    return levels.get(i);
                }
                ++i;
            }
        }
        return null;
    }

    public static class Level {
        @Expose
        private final int level;
        @Expose
        private final WeightedList<GearRarityOutcome> outcomes;

        public Level(final int level, final WeightedList<GearRarityOutcome> outcomes) {
            this.level = level;
            this.outcomes = outcomes;
        }
    }

    public static class GearRarityOutcome {
        @Expose
        private final int tier;
        @Expose
        private final String rarity;

        public GearRarityOutcome(final int tier, final String rarity) {
            this.tier = tier;
            this.rarity = rarity;
        }

        public int getTier() {
            return this.tier;
        }

        public String getRarity() {
            return this.rarity;
        }
    }
}
