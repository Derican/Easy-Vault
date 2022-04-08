package iskallia.vault.network.message;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.type.Research;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.world.data.PlayerResearchesData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class ResearchMessage {
    public String researchName;

    public ResearchMessage() {
    }

    public ResearchMessage(String researchName) {
        this.researchName = researchName;
    }

    public static void encode(ResearchMessage message, PacketBuffer buffer) {
        buffer.writeUtf(message.researchName, 32767);
    }

    public static ResearchMessage decode(PacketBuffer buffer) {
        ResearchMessage message = new ResearchMessage();
        message.researchName = buffer.readUtf(32767);
        return message;
    }

    public static void handle(ResearchMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();

            if (sender == null) {
                return;
            }

            Research research = ModConfigs.RESEARCHES.getByName(message.researchName);

            if (research == null) {
                return;
            }

            PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld) sender.level);

            PlayerResearchesData researchesData = PlayerResearchesData.get((ServerWorld) sender.level);

            ResearchTree researchTree = researchesData.getResearches((PlayerEntity) sender);
            int researchCost = researchTree.getResearchCost(research);
            if (ModConfigs.SKILL_GATES.getGates().isLocked(research.getName(), researchTree)) {
                return;
            }
            PlayerVaultStats stats = statsData.getVaultStats((PlayerEntity) sender);
            int currentPoints = research.usesKnowledge() ? stats.getUnspentKnowledgePts() : stats.getUnspentSkillPts();
            if (currentPoints >= researchCost) {
                researchesData.research(sender, research);
                if (research.usesKnowledge()) {
                    statsData.spendKnowledgePts(sender, researchCost);
                } else {
                    statsData.spendSkillPts(sender, researchCost);
                }
            }
        });
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\ResearchMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */