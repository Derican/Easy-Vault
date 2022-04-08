package iskallia.vault.network.message;

import iskallia.vault.client.ClientDamageData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class PlayerDamageMultiplierMessage {
    private final float multiplier;

    public PlayerDamageMultiplierMessage(float multiplier) {
        this.multiplier = multiplier;
    }

    public float getMultiplier() {
        return this.multiplier;
    }

    public static void encode(PlayerDamageMultiplierMessage message, PacketBuffer buffer) {
        buffer.writeFloat(message.multiplier);
    }

    public static PlayerDamageMultiplierMessage decode(PacketBuffer buffer) {
        return new PlayerDamageMultiplierMessage(buffer.readFloat());
    }

    public static void handle(PlayerDamageMultiplierMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientDamageData.receiveUpdate(message));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\PlayerDamageMultiplierMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */