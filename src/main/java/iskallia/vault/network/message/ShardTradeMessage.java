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
    private final Map<Integer, Tuple<ItemStack, Integer>> availableTrades = new HashMap<>();

    private ShardTradeMessage(int randomTradeCost, long seed) {
        this.randomTradeCost = randomTradeCost;
        this.seed = seed;
    }

    public ShardTradeMessage(int randomTradeCost, long seed, Map<Integer, SoulShardTraderData.SelectedTrade> trades) {
        this.randomTradeCost = randomTradeCost;
        this.seed = seed;
        trades.forEach((index, trade) -> {
            Tuple<ItemStack, Integer> tradeTpl = new Tuple(trade.getStack(), Integer.valueOf(trade.getShardCost()));
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

    public static void encode(ShardTradeMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.randomTradeCost);
        buffer.writeLong(message.seed);
        buffer.writeInt(message.availableTrades.size());
        message.availableTrades.forEach((index, tradeTpl) -> {
            buffer.writeInt(index.intValue());
            buffer.writeItem((ItemStack) tradeTpl.getA());
            buffer.writeInt(((Integer) tradeTpl.getB()).intValue());
        });
    }

    public static ShardTradeMessage decode(PacketBuffer buffer) {
        ShardTradeMessage message = new ShardTradeMessage(buffer.readInt(), buffer.readLong());
        int trades = buffer.readInt();
        for (int i = 0; i < trades; i++) {
            int index = buffer.readInt();
            ItemStack tradeStack = buffer.readItem();
            int cost = buffer.readInt();
            message.availableTrades.put(Integer.valueOf(index), new Tuple(tradeStack, Integer.valueOf(cost)));
        }
        return message;
    }

    public static void handle(ShardTradeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientShardTradeData.receive(message));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\ShardTradeMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */