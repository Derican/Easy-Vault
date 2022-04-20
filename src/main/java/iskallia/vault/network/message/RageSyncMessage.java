package iskallia.vault.network.message;

import iskallia.vault.util.PlayerRageHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RageSyncMessage {
    private final int rage;

    public RageSyncMessage(final int rage) {
        this.rage = rage;
    }

    public int getRage() {
        return this.rage;
    }

    public static void encode(final RageSyncMessage message, final PacketBuffer buffer) {
        buffer.writeInt(message.rage);
    }

    public static RageSyncMessage decode(final PacketBuffer buffer) {
        return new RageSyncMessage(buffer.readInt());
    }

    public static void handle(final RageSyncMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> PlayerRageHelper.receiveRageUpdate(message));
        context.setPacketHandled(true);
    }
}
