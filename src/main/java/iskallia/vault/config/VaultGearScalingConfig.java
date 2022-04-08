package iskallia.vault.config;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import iskallia.vault.util.data.WeightedList;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VaultGearScalingConfig
        extends Config {
    @Expose
    private final Map<String, List<Level>> pooledRarityOutcomes = new HashMap<>();


    public String getName() {
        return "vault_gear_scaling";
    }


    protected void reset() {
        this.pooledRarityOutcomes.clear();

        Level defaultLevel = new Level(0, (new WeightedList()).add(new GearRarityOutcome(0, "Scrappy"), 1));
        this.pooledRarityOutcomes.put("Scaling", Lists.newArrayList(new Level[]{defaultLevel}));
    }

    @Nullable
    public GearRarityOutcome getGearRollType(String pool, int playerLevel) {
        List<Level> levelConfig = this.pooledRarityOutcomes.get(pool);
        if (levelConfig == null) {
            return null;
        }
        Level level = getForLevel(levelConfig, playerLevel);
        if (level == null) {
            return null;
        }
        return (GearRarityOutcome) level.outcomes.getRandom(rand);
    }

    @Nullable
    public Level getForLevel(List<Level> levels, int level) {
        for (int i = 0; i < levels.size(); i++) {
            if (level < ((Level) levels.get(i)).level) {
                if (i == 0) {
                    break;
                }
                return levels.get(i - 1);
            }
            if (i == levels.size() - 1) {
                return levels.get(i);
            }
        }
        return null;
    }

    public static class Level {
        @Expose
        private final int level;
        @Expose
        private final WeightedList<VaultGearScalingConfig.GearRarityOutcome> outcomes;

        public Level(int level, WeightedList<VaultGearScalingConfig.GearRarityOutcome> outcomes) {
            this.level = level;
            this.outcomes = outcomes;
        }
    }

    public static class GearRarityOutcome {
        @Expose
        private final int tier;
        @Expose
        private final String rarity;

        public GearRarityOutcome(int tier, String rarity) {
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultGearScalingConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */