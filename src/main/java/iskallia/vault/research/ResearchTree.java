package iskallia.vault.research;

import iskallia.vault.config.ResearchGroupConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.ResearchTreeMessage;
import iskallia.vault.research.group.ResearchGroup;
import iskallia.vault.research.type.Research;
import iskallia.vault.util.NetcodeUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ResearchTree
        implements INBTSerializable<CompoundNBT> {
    protected UUID playerUUID;
    protected List<String> researchesDone;

    public ResearchTree(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.researchesDone = new LinkedList<>();
    }

    public List<String> getResearchesDone() {
        return this.researchesDone;
    }

    public boolean isResearched(String researchName) {
        return this.researchesDone.contains(researchName);
    }

    public void research(String researchName) {
        this.researchesDone.add(researchName);
    }

    public void resetAll() {
        this.researchesDone.clear();
    }

    public int getResearchCost(Research research) {
        float cost = research.getCost();
        ResearchGroupConfig config = ModConfigs.RESEARCH_GROUPS;

        ResearchGroup thisGroup = config.getResearchGroup(research);
        String thisGroupId = config.getResearchGroupId(thisGroup);

        for (String doneResearch : getResearchesDone()) {
            ResearchGroup otherGroup = config.getResearchGroup(doneResearch);
            if (otherGroup != null) {
                cost += otherGroup.getGroupIncreasedResearchCost(thisGroupId);
            }
        }

        return Math.max(1, Math.round(cost));
    }

    public String restrictedBy(Item item, Restrictions.Type restrictionType) {
        for (Research research : ModConfigs.RESEARCHES.getAll()) {
            if (!this.researchesDone.contains(research.getName()) &&
                    research.restricts(item, restrictionType)) return research.getName();
        }
        return null;
    }

    public String restrictedBy(Block block, Restrictions.Type restrictionType) {
        for (Research research : ModConfigs.RESEARCHES.getAll()) {
            if (!this.researchesDone.contains(research.getName()) &&
                    research.restricts(block, restrictionType)) return research.getName();
        }
        return null;
    }

    public String restrictedBy(EntityType<?> entityType, Restrictions.Type restrictionType) {
        for (Research research : ModConfigs.RESEARCHES.getAll()) {
            if (!this.researchesDone.contains(research.getName()) &&
                    research.restricts(entityType, restrictionType)) return research.getName();
        }
        return null;
    }

    public void sync(MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.playerUUID, (ServerPlayerEntity player) -> ModNetwork.CHANNEL.sendTo(new ResearchTreeMessage(this, player.getUUID()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putUUID("playerUUID", this.playerUUID);

        ListNBT researches = new ListNBT();
        for (int i = 0; i < this.researchesDone.size(); i++) {
            CompoundNBT research = new CompoundNBT();
            research.putString("name", this.researchesDone.get(i));
            researches.add(i, (INBT) research);
        }
        nbt.put("researches", (INBT) researches);

        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.playerUUID = nbt.getUUID("playerUUID");

        ListNBT researches = nbt.getList("researches", 10);
        this.researchesDone = new LinkedList<>();
        for (int i = 0; i < researches.size(); i++) {
            CompoundNBT researchNBT = researches.getCompound(i);
            String name = researchNBT.getString("name");
            this.researchesDone.add(name);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\research\ResearchTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */