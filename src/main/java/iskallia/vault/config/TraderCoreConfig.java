package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.config.entry.vending.TradeEntry;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.Items;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class TraderCoreConfig
        extends Config {
    public static class TraderCoreCommonConfig
            extends TraderCoreConfig {
        @Expose
        private final List<TraderCoreConfig.Level> levels = new ArrayList<>();


        public String getName() {
            return "trader_core_common";
        }


        protected void reset() {
            WeightedList<TradeEntry> options = new WeightedList();
            options.add(new TradeEntry(new ProductEntry(Items.APPLE, 8, 8, null), new ProductEntry(Items.GOLDEN_APPLE, 1, 1, null), 3), 1);


            this.levels.add(new TraderCoreConfig.Level(0, options));

            options = new WeightedList();
            options.add(new TradeEntry(new ProductEntry(Items.CARROT, 8, 8, null), new ProductEntry(Items.GOLDEN_CARROT, 1, 1, null), 3), 1);


            this.levels.add(new TraderCoreConfig.Level(25, options));
        }

        @Nullable
        public TradeEntry getRandomTrade(int vaultLevel) {
            TraderCoreConfig.Level levelConfig = getForLevel(this.levels, vaultLevel);
            if (levelConfig == null) {
                return null;
            }
            return (TradeEntry) levelConfig.trades.getRandom(rand);
        }

        @Nullable
        public TraderCoreConfig.Level getForLevel(List<TraderCoreConfig.Level> levels, int level) {
            for (int i = 0; i < levels.size(); i++) {
                if (level < ((TraderCoreConfig.Level) levels.get(i)).level) {
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
    }

    public static class Level {
        @Expose
        private final int level;
        @Expose
        private final WeightedList<TradeEntry> trades;

        public Level(int level, WeightedList<TradeEntry> trades) {
            this.level = level;
            this.trades = trades;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\TraderCoreConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */