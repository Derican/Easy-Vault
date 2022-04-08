package iskallia.vault.network.message;

import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.StageManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;


public class ResearchTreeMessage {
    public ResearchTree researchTree;
    public UUID playerUUID;

    public ResearchTreeMessage() {
    }

    public ResearchTreeMessage(ResearchTree researchTree, UUID playerUUID) {
        this.researchTree = researchTree;
        this.playerUUID = playerUUID;
    }

    public static void encode(ResearchTreeMessage message, PacketBuffer buffer) {
        buffer.writeUUID(message.playerUUID);
        buffer.writeNbt(message.researchTree.serializeNBT());
    }

    public static ResearchTreeMessage decode(PacketBuffer buffer) {
        ResearchTreeMessage message = new ResearchTreeMessage();
        message.researchTree = new ResearchTree(buffer.readUUID());
        message.researchTree.deserializeNBT(Objects.<CompoundNBT>requireNonNull(buffer.readNbt()));
        return message;
    }

    public static void handle(ResearchTreeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> StageManager.RESEARCH_TREE = message.researchTree);


        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\ResearchTreeMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */