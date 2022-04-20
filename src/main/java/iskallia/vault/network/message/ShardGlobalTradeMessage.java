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

    public ShardGlobalTradeMessage(final WeightedList<SoulShardConfig.ShardTrade> shardTrades) {
        this.shardTrades = shardTrades;
    }

    public WeightedList<SoulShardConfig.ShardTrade> getShardTrades() {
        return this.shardTrades;
    }

    public static void encode(final ShardGlobalTradeMessage message, final PacketBuffer buffer) {
        buffer.writeInt(message.shardTrades.size());
        message.shardTrades.forEach((trade, nbr) -> {
            final SingleItemEntry entry = trade.getItemEntry();
            buffer.writeUtf(entry.ITEM);
            buffer.writeBoolean(entry.NBT != null);
            if (entry.NBT != null) {
                buffer.writeUtf(trade.getItemEntry().NBT);
            }
            buffer.writeInt(trade.getMinPrice());
            buffer.writeInt(trade.getMaxPrice());
            buffer.writeInt(nbr.intValue());
        });
    }

    public static ShardGlobalTradeMessage decode(final PacketBuffer buffer) {
        final WeightedList<SoulShardConfig.ShardTrade> trades = new WeightedList<SoulShardConfig.ShardTrade>();
        for (int tradeCount = buffer.readInt(), i = 0; i < tradeCount; ++i) {
            final String item = buffer.readUtf(32767);
            String nbt = null;
            if (buffer.readBoolean()) {
                nbt = buffer.readUtf(32767);
            }
            final int min = buffer.readInt();
            final int max = buffer.readInt();
            final int weight = buffer.readInt();
            final SoulShardConfig.ShardTrade trade = new SoulShardConfig.ShardTrade(new SingleItemEntry(item, nbt), min, max);
            trades.add(trade, weight);
        }
        return new ShardGlobalTradeMessage(trades);
    }

    public static void handle(final ShardGlobalTradeMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientShardTradeData.receiveGlobal(message));
        context.setPacketHandled(true);
    }
}
