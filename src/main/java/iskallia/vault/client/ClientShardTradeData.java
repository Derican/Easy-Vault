package iskallia.vault.client;

import iskallia.vault.config.SoulShardConfig;
import iskallia.vault.network.message.ShardGlobalTradeMessage;
import iskallia.vault.network.message.ShardTradeMessage;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClientShardTradeData {
    private static int randomTradeCost;
    private static long tradeSeed;
    private static Map<Integer, Tuple<ItemStack, Integer>> availableTrades;
    private static WeightedList<SoulShardConfig.ShardTrade> shardTrades;

    public static void receive(final ShardTradeMessage message) {
        ClientShardTradeData.randomTradeCost = message.getRandomTradeCost();
        ClientShardTradeData.tradeSeed = message.getTradeSeed();
        ClientShardTradeData.availableTrades = message.getAvailableTrades();
    }

    public static void receiveGlobal(final ShardGlobalTradeMessage message) {
        ClientShardTradeData.shardTrades = message.getShardTrades();
    }

    public static int getRandomTradeCost() {
        return ClientShardTradeData.randomTradeCost;
    }

    public static long getTradeSeed() {
        return ClientShardTradeData.tradeSeed;
    }

    public static void nextSeed() {
        final Random r = new Random(ClientShardTradeData.tradeSeed);
        for (int i = 0; i < 3; ++i) {
            r.nextLong();
        }
        ClientShardTradeData.tradeSeed = r.nextLong();
    }

    public static Map<Integer, Tuple<ItemStack, Integer>> getAvailableTrades() {
        return Collections.unmodifiableMap((Map<? extends Integer, ? extends Tuple<ItemStack, Integer>>) ClientShardTradeData.availableTrades);
    }

    public static Tuple<ItemStack, Integer> getTradeInfo(final int trade) {
        return ClientShardTradeData.availableTrades.get(trade);
    }

    public static WeightedList<SoulShardConfig.ShardTrade> getShardTrades() {
        return ClientShardTradeData.shardTrades;
    }

    static {
        ClientShardTradeData.availableTrades = new HashMap<Integer, Tuple<ItemStack, Integer>>();
        ClientShardTradeData.shardTrades = new WeightedList<SoulShardConfig.ShardTrade>();
    }
}
