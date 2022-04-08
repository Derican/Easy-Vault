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

public class SoulShardConfig
        extends Config {
    @Expose
    private int shardTradePrice;
    @Expose
    private final WeightedList<ShardTrade> shardTrades = new WeightedList();
    @Expose
    private DropRange defaultShardDrops;
    @Expose
    private final Map<String, DropRange> shardDrops = new HashMap<>();


    public String getName() {
        return "soul_shard";
    }


    protected void reset() {
        this.shardTradePrice = 1000;

        this.shardTrades.clear();
        this.shardTrades.add(new ShardTrade(new SingleItemEntry((IItemProvider) ModItems.SKILL_ESSENCE), 1500, 2500), 1);
        this.shardTrades.add(new ShardTrade(new SingleItemEntry((IItemProvider) ModItems.STAR_ESSENCE), 900, 1200), 1);

        this.defaultShardDrops = new DropRange(1, 1, 1.0F);

        this.shardDrops.clear();
        this.shardDrops.put(EntityType.ZOMBIE.getRegistryName().toString(), new DropRange(1, 1, 0.5F));
    }

    public int getShardTradePrice() {
        return this.shardTradePrice;
    }

    public ShardTrade getRandomTrade(Random random) {
        return (ShardTrade) this.shardTrades.getRandom(random);
    }

    public int getRandomShards(EntityType<?> type) {
        DropRange range = this.shardDrops.get(type.getRegistryName().toString());
        if (range == null) {
            return this.defaultShardDrops.getRandomAmount();
        }
        return range.getRandomAmount();
    }

    public WeightedList<ShardTrade> getShardTrades() {
        return this.shardTrades;
    }

    public void syncTo(ServerPlayerEntity player) {
        ModNetwork.CHANNEL.sendTo(new ShardGlobalTradeMessage(getShardTrades()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static class DropRange {
        @Expose
        private final int min;

        public DropRange(int min, int max, float chance) {
            this.min = min;
            this.max = max;
            this.chance = chance;
        }

        @Expose
        private final int max;
        @Expose
        private final float chance;

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

        public ShardTrade(SingleItemEntry item, int minPrice, int maxPrice) {
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\SoulShardConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */