package iskallia.vault.network.message;

import iskallia.vault.client.ClientTalentData;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


public class KnownTalentsMessage {
    private final List<TalentNode<?>> learnedTalents;

    public KnownTalentsMessage(TalentTree talentTree) {
        this(talentTree.getLearnedNodes());
    }

    private KnownTalentsMessage(List<TalentNode<?>> learnedTalents) {
        this.learnedTalents = learnedTalents;
    }

    public List<TalentNode<?>> getLearnedTalents() {
        return this.learnedTalents;
    }

    public static void encode(KnownTalentsMessage message, PacketBuffer buffer) {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT talents = new ListNBT();
        message.learnedTalents.stream().map(TalentNode::serializeNBT).forEach(talents::add);
        nbt.put("LearnedTalents", (INBT) talents);
        buffer.writeNbt(nbt);
    }

    public static KnownTalentsMessage decode(PacketBuffer buffer) {
        List<TalentNode<?>> abilities = new ArrayList<>();
        CompoundNBT nbt = buffer.readNbt();
        ListNBT learnedTalents = nbt.getList("LearnedTalents", 10);
        for (int i = 0; i < learnedTalents.size(); i++) {
            abilities.add(TalentNode.fromNBT(null, learnedTalents.getCompound(i), 2));
        }
        return new KnownTalentsMessage(abilities);
    }

    public static void handle(KnownTalentsMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientTalentData.updateTalents(message));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\KnownTalentsMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */