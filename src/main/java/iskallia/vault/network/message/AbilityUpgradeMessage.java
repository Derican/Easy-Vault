package iskallia.vault.network.message;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.ability.AbilityGroup;
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

import java.util.function.Supplier;

public class AbilityUpgradeMessage {
    private final String abilityName;

    public AbilityUpgradeMessage(final String abilityName) {
        this.abilityName = abilityName;
    }

    public static void encode(final AbilityUpgradeMessage message, final PacketBuffer buffer) {
        buffer.writeUtf(message.abilityName, 32767);
    }

    public static AbilityUpgradeMessage decode(final PacketBuffer buffer) {
        return new AbilityUpgradeMessage(buffer.readUtf(32767));
    }

    public static void handle(final AbilityUpgradeMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = context.getSender();
            if (sender == null) {
                return;
            } else {
                final AbilityGroup<?, ?> abilityGroup = ModConfigs.ABILITIES.getAbilityGroupByName(message.abilityName);
                final PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld) sender.level);
                final PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get((ServerWorld) sender.level);
                final AbilityTree abilityTree = abilitiesData.getAbilities((PlayerEntity) sender);
                final AbilityNode<?, ?> abilityNode = abilityTree.getNodeByName(message.abilityName);
                final PlayerVaultStats stats = statsData.getVaultStats((PlayerEntity) sender);
                if (stats.getVaultLevel() < ((AbilityConfig) abilityNode.getAbilityConfig()).getLevelRequirement()) {
                    return;
                } else if (abilityNode.getLevel() >= abilityGroup.getMaxLevel()) {
                    return;
                } else {
                    final int requiredSkillPts = abilityGroup.levelUpCost(abilityNode.getSpecialization(), abilityNode.getLevel() + 1);
                    if (stats.getUnspentSkillPts() >= requiredSkillPts) {
                        abilitiesData.upgradeAbility(sender, abilityNode);
                        statsData.spendSkillPts(sender, requiredSkillPts);
                    }
                    return;
                }
            }
        });
        context.setPacketHandled(true);
    }
}
