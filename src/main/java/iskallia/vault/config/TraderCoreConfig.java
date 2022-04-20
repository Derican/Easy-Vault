package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.config.entry.vending.TradeEntry;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.Items;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class TraderCoreConfig extends Config {
    public static class TraderCoreCommonConfig extends TraderCoreConfig {
        @Expose
        private final List<Level> levels;

        public TraderCoreCommonConfig() {
            this.levels = new ArrayList<Level>();
        }

        @Override
        public String getName() {
            return "trader_core_common";
        }

        @Override
        protected void reset() {
            WeightedList<TradeEntry> options = new WeightedList<TradeEntry>();
            options.add(new TradeEntry(new ProductEntry(Items.APPLE, 8, 8, null), new ProductEntry(Items.GOLDEN_APPLE, 1, 1, null), 3), 1);
            this.levels.add(new Level(0, options));
            options = new WeightedList<TradeEntry>();
            options.add(new TradeEntry(new ProductEntry(Items.CARROT, 8, 8, null), new ProductEntry(Items.GOLDEN_CARROT, 1, 1, null), 3), 1);
            this.levels.add(new Level(25, options));
        }

        @Nullable
        public TradeEntry getRandomTrade(final int vaultLevel) {
            final Level levelConfig = this.getForLevel(this.levels, vaultLevel);
            if (levelConfig == null) {
                return null;
            }
            return levelConfig.trades.getRandom(TraderCoreCommonConfig.rand);
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
    }

    public static class Level {
        @Expose
        private final int level;
        @Expose
        private final WeightedList<TradeEntry> trades;

        public Level(final int level, final WeightedList<TradeEntry> trades) {
            this.level = level;
            this.trades = trades;
        }
    }
}
