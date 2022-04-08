package iskallia.vault.network.message;

import iskallia.vault.client.ClientAbilityData;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.util.MiscUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class AbilityActivityMessage {
    private final String selectedAbility;
    private final int cooldownTicks;
    private final int maxCooldownTicks;
    private final AbilityTree.ActivityFlag activeFlag;

    public AbilityActivityMessage(AbilityGroup<?, ?> ability, int cooldownTicks, int maxCooldownTicks, AbilityTree.ActivityFlag activeFlag) {
        this(ability.getParentName(), cooldownTicks, maxCooldownTicks, activeFlag);
    }

    private AbilityActivityMessage(String selectedAbility, int cooldownTicks, int maxCooldownTicks, AbilityTree.ActivityFlag activeFlag) {
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

    public static void encode(AbilityActivityMessage message, PacketBuffer buffer) {
        buffer.writeUtf(message.selectedAbility);
        buffer.writeInt(message.cooldownTicks);
        buffer.writeInt(message.maxCooldownTicks);
        buffer.writeInt(message.activeFlag.ordinal());
    }

    public static AbilityActivityMessage decode(PacketBuffer buffer) {
        String selectedAbility = buffer.readUtf(32767);
        int cooldownTicks = buffer.readInt();
        int maxCooldownTicks = buffer.readInt();
        AbilityTree.ActivityFlag activeFlag = (AbilityTree.ActivityFlag) MiscUtils.getEnumEntry(AbilityTree.ActivityFlag.class, buffer.readInt());
        return new AbilityActivityMessage(selectedAbility, cooldownTicks, maxCooldownTicks, activeFlag);
    }

    public static void handle(AbilityActivityMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientAbilityData.updateActivity(message));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\AbilityActivityMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */