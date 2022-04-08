package iskallia.vault.skill.talent;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.KnownTalentsMessage;
import iskallia.vault.skill.talent.type.PlayerTalent;
import iskallia.vault.util.NetcodeUtils;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class TalentTree {
    public static final int CURRENT_VERSION = 2;
    private final List<TalentNode<?>> nodes = new ArrayList<>();
    private final UUID uuid;
    private int version;

    public TalentTree(UUID uuid) {
        this.uuid = uuid;
        add(null, (TalentNode<?>[]) ModConfigs.TALENTS.getAll().stream()
                .map(talentGroup -> new TalentNode<>(talentGroup, 0))
                .toArray(x$0 -> new TalentNode[x$0]));
    }

    public List<TalentNode<?>> getNodes() {
        return this.nodes;
    }

    public List<TalentNode<?>> getLearnedNodes() {
        return (List<TalentNode<?>>) getNodes().stream()
                .filter(TalentNode::isLearned)
                .collect(Collectors.toList());
    }

    public <T extends PlayerTalent> List<TalentNode<T>> getLearnedNodes(Class<T> talentGroupType) {
        return (List<TalentNode<T>>) getNodes().stream()
                .filter(TalentNode::isLearned)
                .filter(talentNode -> talentGroupType.isAssignableFrom(talentNode.getTalent().getClass()))
                .map(node -> node)
                .collect(Collectors.toList());
    }

    public boolean hasLearnedNode(TalentGroup<?> talentGroup) {
        return getLearnedNodes().stream().anyMatch(node -> node.getGroup().getParentName().equals(talentGroup.getParentName()));
    }

    public <T extends PlayerTalent> Collection<T> getTalents(Class<T> talentType) {
        return (Collection<T>) getNodes().stream()
                .filter(TalentNode::isLearned)
                .map(TalentNode::getTalent)
                .filter(talent -> talentType.isAssignableFrom(talent.getClass()))
                .map(talent -> talent)
                .collect(Collectors.toList());
    }

    @Nonnull
    public <T extends PlayerTalent> TalentNode<T> getNodeOf(TalentGroup<T> talentGroup) {
        return getNodeByName(talentGroup.getParentName());
    }


    @Nonnull
    public <T extends PlayerTalent> TalentNode<T> getNodeByName(String name) {
        Optional<TalentNode<T>> talentWrapped = this.nodes.stream().filter(node -> node.getGroup().getParentName().equals(name)).map(node -> node).findFirst();
        if (!talentWrapped.isPresent()) {
            TalentNode<?> talentNode = new TalentNode(ModConfigs.TALENTS.getByName(name), 0);
            this.nodes.add(talentNode);
            return (TalentNode) talentNode;
        }
        return talentWrapped.get();
    }


    public TalentTree upgradeTalent(MinecraftServer server, TalentNode<?> talentNode) {
        remove(server, (TalentNode<?>[]) new TalentNode[]{talentNode});

        TalentNode<?> upgradedTalentNode = new TalentNode(talentNode.getGroup(), talentNode.getLevel() + 1);
        add(server, (TalentNode<?>[]) new TalentNode[]{upgradedTalentNode});
        return this;
    }

    public TalentTree downgradeTalent(MinecraftServer server, TalentNode<?> talentNode) {
        remove(server, (TalentNode<?>[]) new TalentNode[]{talentNode});

        int targetLevel = talentNode.getLevel() - 1;
        TalentNode<?> upgradedTalentNode = new TalentNode(talentNode.getGroup(), Math.max(targetLevel, 0));
        add(server, (TalentNode<?>[]) new TalentNode[]{upgradedTalentNode});
        return this;
    }


    public TalentTree add(MinecraftServer server, TalentNode<?>... nodes) {
        for (TalentNode<?> node : nodes) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (node.isLearned()) {
                    node.getTalent().onAdded((PlayerEntity) player);
                }
            });
            this.nodes.add(node);
        }

        return this;
    }

    public TalentTree tick(MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> this.nodes.stream().filter(TalentNode::isLearned).forEach(()));


        return this;
    }

    public TalentTree remove(MinecraftServer server, TalentNode<?>... nodes) {
        for (TalentNode<?> node : nodes) {
            NetcodeUtils.runIfPresent(server, this.uuid, player -> {
                if (node.isLearned()) {
                    node.getTalent().onRemoved((PlayerEntity) player);
                }
            });
            this.nodes.remove(node);
        }

        return this;
    }

    public void sync(MinecraftServer server) {
        syncTree(server);
    }

    public void syncTree(MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> ModNetwork.CHANNEL.sendTo(new KnownTalentsMessage(this), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("version", this.version);

        ListNBT list = new ListNBT();
        this.nodes.stream().map(TalentNode::serializeNBT).forEach(list::add);
        nbt.put("Nodes", (INBT) list);

        return nbt;
    }

    public void deserialize(CompoundNBT nbt, boolean migrateData) {
        int currentVersion = nbt.contains("version", 3) ? nbt.getInt("version") : 0;

        ListNBT list = nbt.getList("Nodes", 10);
        this.nodes.clear();
        for (int i = 0; i < list.size(); i++) {
            TalentNode<?> talent = TalentNode.fromNBT(this.uuid, list.getCompound(i), migrateData ? currentVersion : 2);
            if (talent != null) {
                add(null, (TalentNode<?>[]) new TalentNode[]{talent});
            }
        }

        this.version = 2;
    }

    @Nullable
    static TalentNode<?> migrate(@Nullable UUID playerId, String talentName, int talentLevel, int vFrom) {
        TalentGroup<?> group = ModConfigs.TALENTS.getTalent(talentName).orElse(null);
        if (vFrom >= 2) {
            return (group == null) ? null : new TalentNode(group, talentLevel);
        }
        int refundedCost = 0;
        MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        if (vFrom <= 0 && (
                talentName.equalsIgnoreCase("Commander") || talentName
                        .equalsIgnoreCase("Ward") || talentName
                        .equalsIgnoreCase("Barbaric") || talentName
                        .equalsIgnoreCase("Frenzy") || talentName
                        .equalsIgnoreCase("Glass Cannon"))) {

            refundedCost += getRefundAmount(talentLevel, new int[]{3, 5, 6, 7, 9});
            talentLevel = 0;
        }

        if (vFrom <= 1 &&
                talentName.equalsIgnoreCase("Soul Hunter")) {

            refundedCost += getRefundAmount(talentLevel, new int[]{1, 2, 3, 5});
            talentLevel = 0;
        }

        if (vFrom <= 2) ;


        if (playerId != null && srv != null && Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            PlayerVaultStatsData.get(srv).addSkillPointNoSync(playerId, refundedCost);
        }
        return (group == null) ? null : new TalentNode(group, talentLevel);
    }

    private static int getRefundAmount(int currentLevel, int[] levelCost) {
        int totalRefund = 0;
        for (int i = 0; i < Math.min(currentLevel, levelCost.length); i++) {
            totalRefund += levelCost[i];
        }
        return totalRefund;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\TalentTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */