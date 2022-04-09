// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.client.ClientStatisticsData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerStatisticsMessage
{
    private final CompoundNBT statisticsData;
    
    public PlayerStatisticsMessage(final CompoundNBT statisticsData) {
        this.statisticsData = statisticsData;
    }
    
    public static void encode(final PlayerStatisticsMessage message, final PacketBuffer buffer) {
        buffer.writeNbt(message.statisticsData);
    }
    
    public static PlayerStatisticsMessage decode(final PacketBuffer buffer) {
        return new PlayerStatisticsMessage(buffer.readNbt());
    }
    
    public static void handle(final PlayerStatisticsMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientStatisticsData.receiveUpdate(message.statisticsData));
        context.setPacketHandled(true);
    }
}
