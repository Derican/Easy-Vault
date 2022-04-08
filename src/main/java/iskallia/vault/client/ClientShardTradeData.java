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
    private static Map<Integer, Tuple<ItemStack, Integer>> availableTrades = new HashMap<>();

    private static WeightedList<SoulShardConfig.ShardTrade> shardTrades = new WeightedList();

    public static void receive(ShardTradeMessage message) {
        randomTradeCost = message.getRandomTradeCost();
        tradeSeed = message.getTradeSeed();
        availableTrades = message.getAvailableTrades();
    }

    public static void receiveGlobal(ShardGlobalTradeMessage message) {
        shardTrades = message.getShardTrades();
    }

    public static int getRandomTradeCost() {
        return randomTradeCost;
    }

    public static long getTradeSeed() {
        return tradeSeed;
    }

    public static void nextSeed() {
        Random r = new Random(tradeSeed);
        for (int i = 0; i < 3; i++) {
            r.nextLong();
        }
        tradeSeed = r.nextLong();
    }

    public static Map<Integer, Tuple<ItemStack, Integer>> getAvailableTrades() {
        return Collections.unmodifiableMap(availableTrades);
    }

    public static Tuple<ItemStack, Integer> getTradeInfo(int trade) {
        return availableTrades.get(Integer.valueOf(trade));
    }

    public static WeightedList<SoulShardConfig.ShardTrade> getShardTrades() {
        return shardTrades;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\ClientShardTradeData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */