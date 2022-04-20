package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.data.WeightedList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VaultGearCraftingScalingConfig extends Config {
    @Expose
    private final List<Level> tierOutcomes;

    public VaultGearCraftingScalingConfig() {
        this.tierOutcomes = new ArrayList<Level>();
    }

    @Override
    public String getName() {
        return "vault_gear_crafting_scaling";
    }

    @Override
    protected void reset() {
        this.tierOutcomes.clear();
        final Level defaultLevel = new Level(0, new WeightedList<TierOutcome>().add(new TierOutcome(0), 1));
        this.tierOutcomes.add(defaultLevel);
        final Level t1level = new Level(100, new WeightedList<TierOutcome>().add(new TierOutcome(0), 10).add(new TierOutcome(1), 1));
        this.tierOutcomes.add(t1level);
    }

    public int getRandomTier(final int playerLevel) {
        final Level level = this.getForLevel(this.tierOutcomes, playerLevel);
        if (level == null) {
            return 0;
        }
        return Optional.ofNullable(level.outcomes.getRandom(VaultGearCraftingScalingConfig.rand)).map(TierOutcome::getTier).orElse(0);
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
        private final WeightedList<TierOutcome> outcomes;

        public Level(final int level, final WeightedList<TierOutcome> outcomes) {
            this.level = level;
            this.outcomes = outcomes;
        }
    }

    public static class TierOutcome {
        @Expose
        private final int tier;

        public TierOutcome(final int tier) {
            this.tier = tier;
        }

        public int getTier() {
            return this.tier;
        }
    }
}
