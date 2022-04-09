// 
// Decompiled by Procyon v0.6.0
// 

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

public class AbilityQuickselectMessage
{
    private final String abilityName;
    
    public AbilityQuickselectMessage(final String abilityName) {
        this.abilityName = abilityName;
    }
    
    public static void encode(final AbilityQuickselectMessage pkt, final PacketBuffer buffer) {
        buffer.writeUtf(pkt.abilityName);
    }
    
    public static AbilityQuickselectMessage decode(final PacketBuffer buffer) {
        return new AbilityQuickselectMessage(buffer.readUtf(32767));
    }
    
    public static void handle(final AbilityQuickselectMessage pkt, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = context.getSender();
            if (sender == null) {
                return;
            }
            else {
                final AbilityGroup<?, ?> ability = ModConfigs.ABILITIES.getAbilityGroupByName(pkt.abilityName);
                if (ability == null) {
                    return;
                }
                else {
                    final PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get((ServerWorld)sender.level);
                    final AbilityTree abilityTree = abilitiesData.getAbilities((PlayerEntity)sender);
                    final AbilityNode<?, ?> abilityNode = abilityTree.getNodeOf(ability);
                    if (!abilityNode.isLearned()) {
                        return;
                    }
                    else {
                        abilityTree.quickSelectAbility(sender.server, ability.getParentName());
                        if (!abilityNode.equals(abilityTree.getSelectedAbility()) || ((AbilityConfig)abilityNode.getAbilityConfig()).getBehavior() != AbilityConfig.Behavior.RELEASE_TO_PERFORM || abilityTree.isOnCooldown(abilityNode)) {
                            return;
                        }
                        else {
                            abilityTree.keyUp(sender.server);
                            return;
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
