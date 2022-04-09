// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class AbilitySelectSpecializationMessage
{
    private final String ability;
    @Nullable
    private final String specialization;
    
    public AbilitySelectSpecializationMessage(final String ability, @Nullable final String specialization) {
        this.ability = ability;
        this.specialization = specialization;
    }
    
    public static void encode(final AbilitySelectSpecializationMessage message, final PacketBuffer buffer) {
        buffer.writeUtf(message.ability);
        buffer.writeBoolean(message.specialization != null);
        if (message.specialization != null) {
            buffer.writeUtf(message.specialization);
        }
    }
    
    public static AbilitySelectSpecializationMessage decode(final PacketBuffer buffer) {
        return new AbilitySelectSpecializationMessage(buffer.readUtf(32767), buffer.readBoolean() ? buffer.readUtf(32767) : null);
    }
    
    public static void handle(final AbilitySelectSpecializationMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = context.getSender();
            if (sender == null) {
                return;
            }
            else {
                final String specialization = message.specialization;
                final PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get((ServerWorld)sender.level);
                final AbilityTree abilityTree = abilitiesData.getAbilities((PlayerEntity)sender);
                final AbilityNode<?, ?> abilityNode = abilityTree.getNodeByName(message.ability);
                if (abilityNode == null) {
                    return;
                }
                else {
                    final PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld)sender.level);
                    final PlayerVaultStats stats = statsData.getVaultStats((PlayerEntity)sender);
                    if (specialization != null) {
                        if (!abilityNode.getGroup().hasSpecialization(specialization)) {
                            return;
                        }
                        else {
                            final AbilityConfig specConfig = (AbilityConfig)abilityNode.getGroup().getAbilityConfig(specialization, abilityNode.getLevel());
                            if (specConfig == null) {
                                return;
                            }
                            else if (stats.getVaultLevel() < specConfig.getLevelRequirement()) {
                                return;
                            }
                        }
                    }
                    else if (abilityNode.getSpecialization() == null) {
                        return;
                    }
                    abilitiesData.selectSpecialization(sender, abilityNode.getGroup().getParentName(), specialization);
                    return;
                }
            }
        });
        context.setPacketHandled(true);
    }
}
