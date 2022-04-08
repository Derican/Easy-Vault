package iskallia.vault.network.message;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
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

    public AbilityUpgradeMessage(String abilityName) {
        this.abilityName = abilityName;
    }

    public static void encode(AbilityUpgradeMessage message, PacketBuffer buffer) {
        buffer.writeUtf(message.abilityName, 32767);
    }

    public static AbilityUpgradeMessage decode(PacketBuffer buffer) {
        return new AbilityUpgradeMessage(buffer.readUtf(32767));
    }

    public static void handle(AbilityUpgradeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();

            if (sender == null) {
                return;
            }

            AbilityGroup<?, ?> abilityGroup = ModConfigs.ABILITIES.getAbilityGroupByName(message.abilityName);

            PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld) sender.level);

            PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get((ServerWorld) sender.level);

            AbilityTree abilityTree = abilitiesData.getAbilities((PlayerEntity) sender);

            AbilityNode<?, ?> abilityNode = abilityTree.getNodeByName(message.abilityName);
            PlayerVaultStats stats = statsData.getVaultStats((PlayerEntity) sender);
            if (stats.getVaultLevel() < abilityNode.getAbilityConfig().getLevelRequirement()) {
                return;
            }
            if (abilityNode.getLevel() >= abilityGroup.getMaxLevel()) {
                return;
            }
            int requiredSkillPts = abilityGroup.levelUpCost(abilityNode.getSpecialization(), abilityNode.getLevel() + 1);
            if (stats.getUnspentSkillPts() >= requiredSkillPts) {
                abilitiesData.upgradeAbility(sender, abilityNode);
                statsData.spendSkillPts(sender, requiredSkillPts);
            }
        });
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\AbilityUpgradeMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */