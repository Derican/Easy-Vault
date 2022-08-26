package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.ShardGlobalTradeMessage;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SoulShardConfig extends Config {
    @Expose
    private int shardTradePrice;
    @Expose
    private final WeightedList<ShardTrade> shardTrades;
    @Expose
    private DropRange defaultShardDrops;
    @Expose
    private final Map<String, DropRange> shardDrops;

    public SoulShardConfig() {
        this.shardTrades = new WeightedList<ShardTrade>();
        this.shardDrops = new HashMap<String, DropRange>();
    }

    @Override
    public String getName() {
        return "soul_shard";
    }

    @Override
    protected void reset() {
        this.shardTradePrice = 1000;
        this.shardTrades.clear();
        this.shardTrades.add(new ShardTrade(new SingleItemEntry(ModItems.SKILL_ESSENCE), 1500, 2500), 1);
        this.shardTrades.add(new ShardTrade(new SingleItemEntry(ModItems.STAR_ESSENCE), 900, 1200), 1);
        this.defaultShardDrops = new DropRange(1, 1, 1.0f);
        this.shardDrops.clear();
        this.shardDrops.put(EntityType.ZOMBIE.getRegistryName().toString(), new DropRange(1, 1, 0.5f));
    }

    public int getShardTradePrice() {
        return this.shardTradePrice;
    }

    public ShardTrade getRandomTrade(final Random random) {
        return this.shardTrades.getRandom(random);
    }

    public int getRandomShards(final EntityType<?> type) {
        final DropRange range = this.shardDrops.get(type.getRegistryName().toString());
        if (range == null) {
            return this.defaultShardDrops.getRandomAmount();
        }
        return range.getRandomAmount();
    }

    public WeightedList<ShardTrade> getShardTrades() {
        return this.shardTrades;
    }

    public void syncTo(final ServerPlayerEntity player) {
        ModNetwork.CHANNEL.sendTo(new ShardGlobalTradeMessage(this.getShardTrades()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static class DropRange {
        @Expose
        private final int min;
        @Expose
        private final int max;
        @Expose
        private final float chance;

        public DropRange(final int min, final int max, final float chance) {
            this.min = min;
            this.max = max;
            this.chance = chance;
        }

        public int getRandomAmount() {
            if (Config.rand.nextFloat() > this.chance) {
                return 0;
            }
            return MathUtilities.getRandomInt(this.min, this.max + 1);
        }
    }

    public static class ShardTrade {
        @Expose
        private final SingleItemEntry item;
        @Expose
        private final int minPrice;
        @Expose
        private final int maxPrice;

        public ShardTrade(final SingleItemEntry item, final int minPrice, final int maxPrice) {
            this.item = item;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
        }

        public ItemStack getItem() {
            return this.item.createItemStack();
        }

        public SingleItemEntry getItemEntry() {
            return this.item;
        }

        public int getMinPrice() {
            return this.minPrice;
        }

        public int getMaxPrice() {
            return this.maxPrice;
        }
    }
}
