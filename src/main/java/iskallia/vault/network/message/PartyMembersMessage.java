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

    public PartyMembersMessage(final ListNBT serializedMembers) {
        this.serializedMembers = serializedMembers;
    }

    public static void encode(final PartyMembersMessage message, final PacketBuffer buffer) {
        final CompoundNBT tag = new CompoundNBT();
        tag.put("list", (INBT) message.serializedMembers);
        buffer.writeNbt(tag);
    }

    public static PartyMembersMessage decode(final PacketBuffer buffer) {
        return new PartyMembersMessage(buffer.readNbt().getList("list", 10));
    }

    public static void handle(final PartyMembersMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientPartyData.receivePartyMembers(message.serializedMembers));
        context.setPacketHandled(true);
    }
}
