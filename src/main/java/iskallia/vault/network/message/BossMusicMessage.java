package iskallia.vault.network.message;

import iskallia.vault.client.ClientVaultRaidData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class BossMusicMessage {
    private final boolean state;

    public BossMusicMessage(boolean state) {
        this.state = state;
    }

    public static void encode(BossMusicMessage message, PacketBuffer buffer) {
        buffer.writeBoolean(message.state);
    }

    public static BossMusicMessage decode(PacketBuffer buffer) {
        return new BossMusicMessage(buffer.readBoolean());
    }

    public boolean isInFight() {
        return this.state;
    }

    public static void handle(BossMusicMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientVaultRaidData.receiveBossUpdate(message));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\BossMusicMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */