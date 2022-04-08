package iskallia.vault.network.message;

import iskallia.vault.client.ClientStatisticsData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class PlayerStatisticsMessage {
    private final CompoundNBT statisticsData;

    public PlayerStatisticsMessage(CompoundNBT statisticsData) {
        this.statisticsData = statisticsData;
    }

    public static void encode(PlayerStatisticsMessage message, PacketBuffer buffer) {
        buffer.writeNbt(message.statisticsData);
    }

    public static PlayerStatisticsMessage decode(PacketBuffer buffer) {
        return new PlayerStatisticsMessage(buffer.readNbt());
    }

    public static void handle(PlayerStatisticsMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientStatisticsData.receiveUpdate(message.statisticsData));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\PlayerStatisticsMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */