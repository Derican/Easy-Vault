package iskallia.vault.network.message;

import iskallia.vault.util.PlayerRageHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class RageSyncMessage {
    private final int rage;

    public RageSyncMessage(int rage) {
        this.rage = rage;
    }

    public int getRage() {
        return this.rage;
    }

    public static void encode(RageSyncMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.rage);
    }

    public static RageSyncMessage decode(PacketBuffer buffer) {
        return new RageSyncMessage(buffer.readInt());
    }

    public static void handle(RageSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> PlayerRageHelper.receiveRageUpdate(message));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\RageSyncMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */