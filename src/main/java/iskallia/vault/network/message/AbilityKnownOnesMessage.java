package iskallia.vault.network.message;

import iskallia.vault.client.ClientAbilityData;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


public class AbilityKnownOnesMessage {
    private final List<AbilityNode<?, ?>> learnedAbilities;

    public AbilityKnownOnesMessage(AbilityTree abilityTree) {
        this(abilityTree.getLearnedNodes());
    }

    private AbilityKnownOnesMessage(List<AbilityNode<?, ?>> learnedAbilities) {
        this.learnedAbilities = learnedAbilities;
    }

    public List<AbilityNode<?, ?>> getLearnedAbilities() {
        return this.learnedAbilities;
    }

    public static void encode(AbilityKnownOnesMessage message, PacketBuffer buffer) {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT abilities = new ListNBT();
        message.learnedAbilities.stream().map(AbilityNode::serializeNBT).forEach(abilities::add);
        nbt.put("LearnedAbilities", (INBT) abilities);
        buffer.writeNbt(nbt);
    }

    public static AbilityKnownOnesMessage decode(PacketBuffer buffer) {
        ArrayList<AbilityNode<?, ?>> abilities = new ArrayList<>();
        CompoundNBT nbt = buffer.readNbt();
        ListNBT learnedAbilities = nbt.getList("LearnedAbilities", 10);
        for (int i = 0; i < learnedAbilities.size(); i++) {
            abilities.add(AbilityNode.fromNBT(learnedAbilities.getCompound(i)));
        }
        return new AbilityKnownOnesMessage(abilities);
    }

    public static void handle(AbilityKnownOnesMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientAbilityData.updateAbilities(message));


        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\AbilityKnownOnesMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */