// 
// Decompiled by Procyon v0.6.0
// 

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

public class ResearchTree implements INBTSerializable<CompoundNBT>
{
    protected UUID playerUUID;
    protected List<String> researchesDone;
    
    public ResearchTree(final UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.researchesDone = new LinkedList<String>();
    }
    
    public List<String> getResearchesDone() {
        return this.researchesDone;
    }
    
    public boolean isResearched(final String researchName) {
        return this.researchesDone.contains(researchName);
    }
    
    public void research(final String researchName) {
        this.researchesDone.add(researchName);
    }
    
    public void resetAll() {
        this.researchesDone.clear();
    }
    
    public int getResearchCost(final Research research) {
        float cost = (float)research.getCost();
        final ResearchGroupConfig config = ModConfigs.RESEARCH_GROUPS;
        final ResearchGroup thisGroup = config.getResearchGroup(research);
        final String thisGroupId = config.getResearchGroupId(thisGroup);
        for (final String doneResearch : this.getResearchesDone()) {
            final ResearchGroup otherGroup = config.getResearchGroup(doneResearch);
            if (otherGroup != null) {
                cost += otherGroup.getGroupIncreasedResearchCost(thisGroupId);
            }
        }
        return Math.max(1, Math.round(cost));
    }
    
    public String restrictedBy(final Item item, final Restrictions.Type restrictionType) {
        for (final Research research : ModConfigs.RESEARCHES.getAll()) {
            if (this.researchesDone.contains(research.getName())) {
                continue;
            }
            if (research.restricts(item, restrictionType)) {
                return research.getName();
            }
        }
        return null;
    }
    
    public String restrictedBy(final Block block, final Restrictions.Type restrictionType) {
        for (final Research research : ModConfigs.RESEARCHES.getAll()) {
            if (this.researchesDone.contains(research.getName())) {
                continue;
            }
            if (research.restricts(block, restrictionType)) {
                return research.getName();
            }
        }
        return null;
    }
    
    public String restrictedBy(final EntityType<?> entityType, final Restrictions.Type restrictionType) {
        for (final Research research : ModConfigs.RESEARCHES.getAll()) {
            if (this.researchesDone.contains(research.getName())) {
                continue;
            }
            if (research.restricts(entityType, restrictionType)) {
                return research.getName();
            }
        }
        return null;
    }
    
    public void sync(final MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.playerUUID, (ServerPlayerEntity player) -> ModNetwork.CHANNEL.sendTo(new ResearchTreeMessage(this, player.getUUID()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }
    
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putUUID("playerUUID", this.playerUUID);
        final ListNBT researches = new ListNBT();
        for (int i = 0; i < this.researchesDone.size(); ++i) {
            final CompoundNBT research = new CompoundNBT();
            research.putString("name", (String)this.researchesDone.get(i));
            researches.add(i, (INBT)research);
        }
        nbt.put("researches", (INBT)researches);
        return nbt;
    }
    
    public void deserializeNBT(final CompoundNBT nbt) {
        this.playerUUID = nbt.getUUID("playerUUID");
        final ListNBT researches = nbt.getList("researches", 10);
        this.researchesDone = new LinkedList<String>();
        for (int i = 0; i < researches.size(); ++i) {
            final CompoundNBT researchNBT = researches.getCompound(i);
            final String name = researchNBT.getString("name");
            this.researchesDone.add(name);
        }
    }
}
