package iskallia.vault.network.message;

import iskallia.vault.client.ClientPartyData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class PartyMembersMessage {
    private ListNBT serializedMembers;

    public PartyMembersMessage(ListNBT serializedMembers) {
        this.serializedMembers = serializedMembers;
    }

    public static void encode(PartyMembersMessage message, PacketBuffer buffer) {
        CompoundNBT tag = new CompoundNBT();
        tag.put("list", (INBT) message.serializedMembers);
        buffer.writeNbt(tag);
    }

    public static PartyMembersMessage decode(PacketBuffer buffer) {
        return new PartyMembersMessage(buffer.readNbt().getList("list", 10));
    }

    public static void handle(PartyMembersMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientPartyData.receivePartyMembers(message.serializedMembers));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\PartyMembersMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */