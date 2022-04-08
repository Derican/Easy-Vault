package iskallia.vault.network.message;

import iskallia.vault.client.ClientPartyData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class PartyStatusMessage {
    private final ListNBT serializedParties;

    public PartyStatusMessage(ListNBT serializedParties) {
        this.serializedParties = serializedParties;
    }

    public static void encode(PartyStatusMessage message, PacketBuffer buffer) {
        CompoundNBT tag = new CompoundNBT();
        tag.put("list", (INBT) message.serializedParties);
        buffer.writeNbt(tag);
    }

    public static PartyStatusMessage decode(PacketBuffer buffer) {
        return new PartyStatusMessage(buffer.readNbt().getList("list", 10));
    }

    public static void handle(PartyStatusMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientPartyData.receivePartyUpdate(message.serializedParties));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\PartyStatusMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */