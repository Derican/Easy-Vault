// 
// Decompiled by Procyon v0.6.0
// 

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

public class ResearchMessage
{
    public String researchName;
    
    public ResearchMessage() {
    }
    
    public ResearchMessage(final String researchName) {
        this.researchName = researchName;
    }
    
    public static void encode(final ResearchMessage message, final PacketBuffer buffer) {
        buffer.writeUtf(message.researchName, 32767);
    }
    
    public static ResearchMessage decode(final PacketBuffer buffer) {
        final ResearchMessage message = new ResearchMessage();
        message.researchName = buffer.readUtf(32767);
        return message;
    }
    
    public static void handle(final ResearchMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = context.getSender();
            if (sender == null) {
                return;
            }
            else {
                final Research research = ModConfigs.RESEARCHES.getByName(message.researchName);
                if (research == null) {
                    return;
                }
                else {
                    final PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld)sender.level);
                    final PlayerResearchesData researchesData = PlayerResearchesData.get((ServerWorld)sender.level);
                    final ResearchTree researchTree = researchesData.getResearches((PlayerEntity)sender);
                    final int researchCost = researchTree.getResearchCost(research);
                    if (ModConfigs.SKILL_GATES.getGates().isLocked(research.getName(), researchTree)) {
                        return;
                    }
                    else {
                        final PlayerVaultStats stats = statsData.getVaultStats((PlayerEntity)sender);
                        final int currentPoints = research.usesKnowledge() ? stats.getUnspentKnowledgePts() : stats.getUnspentSkillPts();
                        if (currentPoints >= researchCost) {
                            researchesData.research(sender, research);
                            if (research.usesKnowledge()) {
                                statsData.spendKnowledgePts(sender, researchCost);
                            }
                            else {
                                statsData.spendSkillPts(sender, researchCost);
                            }
                        }
                        return;
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
