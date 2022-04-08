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

public class AbilitySelectSpecializationMessage {
    private final String ability;
    @Nullable
    private final String specialization;

    public AbilitySelectSpecializationMessage(String ability, @Nullable String specialization) {
        this.ability = ability;
        this.specialization = specialization;
    }

    public static void encode(AbilitySelectSpecializationMessage message, PacketBuffer buffer) {
        buffer.writeUtf(message.ability);
        buffer.writeBoolean((message.specialization != null));
        if (message.specialization != null) {
            buffer.writeUtf(message.specialization);
        }
    }

    public static AbilitySelectSpecializationMessage decode(PacketBuffer buffer) {
        return new AbilitySelectSpecializationMessage(buffer.readUtf(32767), buffer.readBoolean() ? buffer.readUtf(32767) : null);
    }

    public static void handle(AbilitySelectSpecializationMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();

            if (sender == null) {
                return;
            }

            String specialization = message.specialization;

            PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get((ServerWorld) sender.level);

            AbilityTree abilityTree = abilitiesData.getAbilities((PlayerEntity) sender);

            AbilityNode<?, ?> abilityNode = abilityTree.getNodeByName(message.ability);

            if (abilityNode == null) {
                return;
            }
            PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld) sender.level);
            PlayerVaultStats stats = statsData.getVaultStats((PlayerEntity) sender);
            if (specialization != null) {
                if (!abilityNode.getGroup().hasSpecialization(specialization)) {
                    return;
                }
                AbilityConfig specConfig = abilityNode.getGroup().getAbilityConfig(specialization, abilityNode.getLevel());
                if (specConfig == null) {
                    return;
                }
                if (stats.getVaultLevel() < specConfig.getLevelRequirement()) {
                    return;
                }
            } else if (abilityNode.getSpecialization() == null) {
                return;
            }
            abilitiesData.selectSpecialization(sender, abilityNode.getGroup().getParentName(), specialization);
        });
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\AbilitySelectSpecializationMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */