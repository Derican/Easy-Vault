// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.client.ClientAbilityData;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.util.MiscUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AbilityActivityMessage
{
    private final String selectedAbility;
    private final int cooldownTicks;
    private final int maxCooldownTicks;
    private final AbilityTree.ActivityFlag activeFlag;
    
    public AbilityActivityMessage(final AbilityGroup<?, ?> ability, final int cooldownTicks, final int maxCooldownTicks, final AbilityTree.ActivityFlag activeFlag) {
        this(ability.getParentName(), cooldownTicks, maxCooldownTicks, activeFlag);
    }
    
    private AbilityActivityMessage(final String selectedAbility, final int cooldownTicks, final int maxCooldownTicks, final AbilityTree.ActivityFlag activeFlag) {
        this.selectedAbility = selectedAbility;
        this.cooldownTicks = cooldownTicks;
        this.maxCooldownTicks = maxCooldownTicks;
        this.activeFlag = activeFlag;
    }
    
    public String getSelectedAbility() {
        return this.selectedAbility;
    }
    
    public int getCooldownTicks() {
        return this.cooldownTicks;
    }
    
    public int getMaxCooldownTicks() {
        return this.maxCooldownTicks;
    }
    
    public AbilityTree.ActivityFlag getActiveFlag() {
        return this.activeFlag;
    }
    
    public static void encode(final AbilityActivityMessage message, final PacketBuffer buffer) {
        buffer.writeUtf(message.selectedAbility);
        buffer.writeInt(message.cooldownTicks);
        buffer.writeInt(message.maxCooldownTicks);
        buffer.writeInt(message.activeFlag.ordinal());
    }
    
    public static AbilityActivityMessage decode(final PacketBuffer buffer) {
        final String selectedAbility = buffer.readUtf(32767);
        final int cooldownTicks = buffer.readInt();
        final int maxCooldownTicks = buffer.readInt();
        final AbilityTree.ActivityFlag activeFlag = MiscUtils.getEnumEntry(AbilityTree.ActivityFlag.class, buffer.readInt());
        return new AbilityActivityMessage(selectedAbility, cooldownTicks, maxCooldownTicks, activeFlag);
    }
    
    public static void handle(final AbilityActivityMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientAbilityData.updateActivity(message));
        context.setPacketHandled(true);
    }
}
