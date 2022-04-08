package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.data.WeightedList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VaultGearCraftingScalingConfig
        extends Config {
    @Expose
    private final List<Level> tierOutcomes = new ArrayList<>();


    public String getName() {
        return "vault_gear_crafting_scaling";
    }


    protected void reset() {
        this.tierOutcomes.clear();

        Level defaultLevel = new Level(0, (new WeightedList()).add(new TierOutcome(0), 1));
        this.tierOutcomes.add(defaultLevel);


        Level t1level = new Level(100, (new WeightedList()).add(new TierOutcome(0), 10).add(new TierOutcome(1), 1));
        this.tierOutcomes.add(t1level);
    }

    public int getRandomTier(int playerLevel) {
        Level level = getForLevel(this.tierOutcomes, playerLevel);
        if (level == null) {
            return 0;
        }
        return ((Integer) Optional.<TierOutcome>ofNullable(level.outcomes.getRandom(rand)).map((outcome)->outcome.getTier()).orElse(Integer.valueOf(0))).intValue();
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
        private final WeightedList<VaultGearCraftingScalingConfig.TierOutcome> outcomes;

        public Level(int level, WeightedList<VaultGearCraftingScalingConfig.TierOutcome> outcomes) {
            this.level = level;
            this.outcomes = outcomes;
        }
    }


    public static class TierOutcome {
        @Expose
        private final int tier;

        public TierOutcome(int tier) {
            this.tier = tier;
        }

        public int getTier() {
            return this.tier;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultGearCraftingScalingConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */