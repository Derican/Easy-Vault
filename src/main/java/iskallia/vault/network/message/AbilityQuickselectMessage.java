package iskallia.vault.network.message;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AbilityQuickselectMessage {
    private final String abilityName;

    public AbilityQuickselectMessage(String abilityName) {
        this.abilityName = abilityName;
    }

    public static void encode(AbilityQuickselectMessage pkt, PacketBuffer buffer) {
        buffer.writeUtf(pkt.abilityName);
    }

    public static AbilityQuickselectMessage decode(PacketBuffer buffer) {
        return new AbilityQuickselectMessage(buffer.readUtf(32767));
    }

    public static void handle(AbilityQuickselectMessage pkt, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();

            if (sender == null) {
                return;
            }

            AbilityGroup<?, ?> ability = ModConfigs.ABILITIES.getAbilityGroupByName(pkt.abilityName);

            if (ability == null) {
                return;
            }

            PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get((ServerWorld) sender.level);

            AbilityTree abilityTree = abilitiesData.getAbilities((PlayerEntity) sender);

            AbilityNode<?, ?> abilityNode = abilityTree.getNodeOf(ability);

            if (!abilityNode.isLearned()) {
                return;
            }

            abilityTree.quickSelectAbility(sender.server, ability.getParentName());
            if (!abilityNode.equals(abilityTree.getSelectedAbility()) || abilityNode.getAbilityConfig().getBehavior() != AbilityConfig.Behavior.RELEASE_TO_PERFORM || abilityTree.isOnCooldown(abilityNode)) {
                return;
            }
            abilityTree.keyUp(sender.server);
        });
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\AbilityQuickselectMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */