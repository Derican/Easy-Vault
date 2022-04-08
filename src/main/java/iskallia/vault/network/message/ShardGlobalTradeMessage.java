package iskallia.vault.network.message;

import iskallia.vault.client.ClientShardTradeData;
import iskallia.vault.config.SoulShardConfig;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class ShardGlobalTradeMessage {
    private final WeightedList<SoulShardConfig.ShardTrade> shardTrades;

    public ShardGlobalTradeMessage(WeightedList<SoulShardConfig.ShardTrade> shardTrades) {
        this.shardTrades = shardTrades;
    }

    public WeightedList<SoulShardConfig.ShardTrade> getShardTrades() {
        return this.shardTrades;
    }

    public static void encode(ShardGlobalTradeMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.shardTrades.size());
        message.shardTrades.forEach((trade, nbr) -> {
            SingleItemEntry entry = trade.getItemEntry();
            buffer.writeUtf(entry.ITEM);
            buffer.writeBoolean((entry.NBT != null));
            if (entry.NBT != null) {
                buffer.writeUtf((trade.getItemEntry()).NBT);
            }
            buffer.writeInt(trade.getMinPrice());
            buffer.writeInt(trade.getMaxPrice());
            buffer.writeInt(nbr.intValue());
        });
    }

    public static ShardGlobalTradeMessage decode(PacketBuffer buffer) {
        WeightedList<SoulShardConfig.ShardTrade> trades = new WeightedList();
        int tradeCount = buffer.readInt();
        for (int i = 0; i < tradeCount; i++) {
            String item = buffer.readUtf(32767);
            String nbt = null;
            if (buffer.readBoolean()) {
                nbt = buffer.readUtf(32767);
            }
            int min = buffer.readInt();
            int max = buffer.readInt();
            int weight = buffer.readInt();
            SoulShardConfig.ShardTrade trade = new SoulShardConfig.ShardTrade(new SingleItemEntry(item, nbt), min, max);
            trades.add(trade, weight);
        }
        return new ShardGlobalTradeMessage(trades);
    }

    public static void handle(ShardGlobalTradeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientShardTradeData.receiveGlobal(message));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\ShardGlobalTradeMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */