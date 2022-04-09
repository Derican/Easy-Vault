// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.client.ClientAbilityData;
import iskallia.vault.skill.ability.AbilityGroup;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AbilityFocusMessage
{
    private final String selectedAbility;
    
    public AbilityFocusMessage(final AbilityGroup<?, ?> ability) {
        this(ability.getParentName());
    }
    
    public AbilityFocusMessage(final String selectedAbility) {
        this.selectedAbility = selectedAbility;
    }
    
    public String getSelectedAbility() {
        return this.selectedAbility;
    }
    
    public static void encode(final AbilityFocusMessage message, final PacketBuffer buffer) {
        buffer.writeUtf(message.selectedAbility);
    }
    
    public static AbilityFocusMessage decode(final PacketBuffer buffer) {
        return new AbilityFocusMessage(buffer.readUtf(32767));
    }
    
    public static void handle(final AbilityFocusMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientAbilityData.updateSelectedAbility(message));
        context.setPacketHandled(true);
    }
}
