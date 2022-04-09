// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.PlayerTalent;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TalentUpgradeMessage
{
    private final String talentName;
    
    public TalentUpgradeMessage(final String talentName) {
        this.talentName = talentName;
    }
    
    public static void encode(final TalentUpgradeMessage message, final PacketBuffer buffer) {
        buffer.writeUtf(message.talentName);
    }
    
    public static TalentUpgradeMessage decode(final PacketBuffer buffer) {
        return new TalentUpgradeMessage(buffer.readUtf(32767));
    }
    
    public static void handle(final TalentUpgradeMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = context.getSender();
            if (sender == null) {
                return;
            }
            else {
                final TalentGroup<?> talentGroup = ModConfigs.TALENTS.getByName(message.talentName);
                final PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld)sender.level);
                final PlayerTalentsData abilitiesData = PlayerTalentsData.get((ServerWorld)sender.level);
                final TalentTree talentTree = abilitiesData.getTalents((PlayerEntity)sender);
                if (ModConfigs.SKILL_GATES.getGates().isLocked(talentGroup, talentTree)) {
                    return;
                }
                else {
                    final TalentNode<?> talentNode = talentTree.getNodeByName(message.talentName);
                    final PlayerVaultStats stats = statsData.getVaultStats((PlayerEntity)sender);
                    if (talentNode.getLevel() >= talentGroup.getMaxLevel()) {
                        return;
                    }
                    else if (stats.getVaultLevel() < ((PlayerTalent)talentNode.getGroup().getTalent(talentNode.getLevel() + 1)).getLevelRequirement()) {
                        return;
                    }
                    else {
                        final int requiredSkillPts = talentGroup.cost(talentNode.getLevel() + 1);
                        if (stats.getUnspentSkillPts() >= requiredSkillPts) {
                            abilitiesData.upgradeTalent(sender, talentNode);
                            statsData.spendSkillPts(sender, requiredSkillPts);
                        }
                        return;
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
