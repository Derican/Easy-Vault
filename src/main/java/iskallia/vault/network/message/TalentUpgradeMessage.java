package iskallia.vault.network.message;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class TalentUpgradeMessage {
    private final String talentName;

    public TalentUpgradeMessage(String talentName) {
        this.talentName = talentName;
    }

    public static void encode(TalentUpgradeMessage message, PacketBuffer buffer) {
        buffer.writeUtf(message.talentName);
    }

    public static TalentUpgradeMessage decode(PacketBuffer buffer) {
        return new TalentUpgradeMessage(buffer.readUtf(32767));
    }

    public static void handle(TalentUpgradeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();

            if (sender == null) {
                return;
            }

            TalentGroup<?> talentGroup = ModConfigs.TALENTS.getByName(message.talentName);

            PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld) sender.level);

            PlayerTalentsData abilitiesData = PlayerTalentsData.get((ServerWorld) sender.level);

            TalentTree talentTree = abilitiesData.getTalents((PlayerEntity) sender);

            if (ModConfigs.SKILL_GATES.getGates().isLocked(talentGroup, talentTree)) {
                return;
            }
            TalentNode<?> talentNode = talentTree.getNodeByName(message.talentName);
            PlayerVaultStats stats = statsData.getVaultStats((PlayerEntity) sender);
            if (talentNode.getLevel() >= talentGroup.getMaxLevel()) {
                return;
            }
            if (stats.getVaultLevel() < talentNode.getGroup().getTalent(talentNode.getLevel() + 1).getLevelRequirement()) {
                return;
            }
            int requiredSkillPts = talentGroup.cost(talentNode.getLevel() + 1);
            if (stats.getUnspentSkillPts() >= requiredSkillPts) {
                abilitiesData.upgradeTalent(sender, talentNode);
                statsData.spendSkillPts(sender, requiredSkillPts);
            }
        });
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\TalentUpgradeMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */