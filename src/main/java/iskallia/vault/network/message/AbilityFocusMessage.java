package iskallia.vault.network.message;

import iskallia.vault.client.ClientAbilityData;
import iskallia.vault.skill.ability.AbilityGroup;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class AbilityFocusMessage {
    private final String selectedAbility;

    public AbilityFocusMessage(AbilityGroup<?, ?> ability) {
        this(ability.getParentName());
    }

    public AbilityFocusMessage(String selectedAbility) {
        this.selectedAbility = selectedAbility;
    }

    public String getSelectedAbility() {
        return this.selectedAbility;
    }

    public static void encode(AbilityFocusMessage message, PacketBuffer buffer) {
        buffer.writeUtf(message.selectedAbility);
    }

    public static AbilityFocusMessage decode(PacketBuffer buffer) {
        return new AbilityFocusMessage(buffer.readUtf(32767));
    }

    public static void handle(AbilityFocusMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientAbilityData.updateSelectedAbility(message));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\AbilityFocusMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */