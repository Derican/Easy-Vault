package iskallia.vault.network.message;

import iskallia.vault.client.ClientShardTradeData;
import iskallia.vault.world.data.SoulShardTraderData;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ShardTradeMessage {
    private final int randomTradeCost;
    private final long seed;
    private final Map<Integer, Tuple<ItemStack, Integer>> availableTrades;

    private ShardTradeMessage(final int randomTradeCost, final long seed) {
        this.availableTrades = new HashMap<Integer, Tuple<ItemStack, Integer>>();
        this.randomTradeCost = randomTradeCost;
        this.seed = seed;
    }

    public ShardTradeMessage(final int randomTradeCost, final long seed, final Map<Integer, SoulShardTraderData.SelectedTrade> trades) {
        this.availableTrades = new HashMap<Integer, Tuple<ItemStack, Integer>>();
        this.randomTradeCost = randomTradeCost;
        this.seed = seed;
        trades.forEach((index, trade) -> {
            final Tuple<ItemStack, Integer> tradeTpl = (Tuple<ItemStack, Integer>) new Tuple(trade.getStack(), trade.getShardCost());
            this.availableTrades.put(index, tradeTpl);
        });
    }

    public int getRandomTradeCost() {
        return this.randomTradeCost;
    }

    public long getTradeSeed() {
        return this.seed;
    }

    public Map<Integer, Tuple<ItemStack, Integer>> getAvailableTrades() {
        return this.availableTrades;
    }

    public static void encode(final ShardTradeMessage message, final PacketBuffer buffer) {
        buffer.writeInt(message.randomTradeCost);
        buffer.writeLong(message.seed);
        buffer.writeInt(message.availableTrades.size());
        message.availableTrades.forEach((index, tradeTpl) -> {
            buffer.writeInt((int) index);
            buffer.writeItem((ItemStack) tradeTpl.getA());
            buffer.writeInt((int) tradeTpl.getB());
        });
    }

    public static ShardTradeMessage decode(final PacketBuffer buffer) {
        final ShardTradeMessage message = new ShardTradeMessage(buffer.readInt(), buffer.readLong());
        for (int trades = buffer.readInt(), i = 0; i < trades; ++i) {
            final int index = buffer.readInt();
            final ItemStack tradeStack = buffer.readItem();
            final int cost = buffer.readInt();
            message.availableTrades.put(index, (Tuple<ItemStack, Integer>) new Tuple(tradeStack, cost));
        }
        return message;
    }

    public static void handle(final ShardTradeMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientShardTradeData.receive(message));
        context.setPacketHandled(true);
    }
}
