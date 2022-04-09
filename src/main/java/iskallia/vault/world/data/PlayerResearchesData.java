// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.data;

import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.type.Research;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayerResearchesData extends WorldSavedData
{
    protected static final String DATA_NAME = "the_vault_PlayerResearches";
    private Map<UUID, ResearchTree> playerMap;
    
    public PlayerResearchesData() {
        super("the_vault_PlayerResearches");
        this.playerMap = new HashMap<UUID, ResearchTree>();
    }
    
    public PlayerResearchesData(final String name) {
        super(name);
        this.playerMap = new HashMap<UUID, ResearchTree>();
    }
    
    public ResearchTree getResearches(final PlayerEntity player) {
        return this.getResearches(player.getUUID());
    }
    
    public ResearchTree getResearches(final UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, ResearchTree::new);
    }
    
    public PlayerResearchesData research(final ServerPlayerEntity player, final Research research) {
        final ResearchTree researchTree = this.getResearches((PlayerEntity)player);
        researchTree.research(research.getName());
        researchTree.sync(player.getServer());
        this.setDirty();
        return this;
    }
    
    public PlayerResearchesData resetResearchTree(final ServerPlayerEntity player) {
        final ResearchTree researchTree = this.getResearches((PlayerEntity)player);
        researchTree.resetAll();
        researchTree.sync(player.getServer());
        this.setDirty();
        return this;
    }
    
    public void load(final CompoundNBT nbt) {
        final ListNBT playerList = nbt.getList("PlayerEntries", 8);
        final ListNBT researchesList = nbt.getList("ResearchEntries", 10);
        if (playerList.size() != researchesList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }
        for (int i = 0; i < playerList.size(); ++i) {
            final UUID playerUUID = UUID.fromString(playerList.getString(i));
            this.getResearches(playerUUID).deserializeNBT(researchesList.getCompound(i));
        }
    }
    
    public CompoundNBT save(final CompoundNBT nbt) {
        final ListNBT playerList = new ListNBT();
        final ListNBT researchesList = new ListNBT();
        this.playerMap.forEach((uuid, researchTree) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));
            researchesList.add(researchTree.serializeNBT());
            return;
        });
        nbt.put("PlayerEntries", (INBT)playerList);
        nbt.put("ResearchEntries", (INBT)researchesList);
        return nbt;
    }
    
    public static PlayerResearchesData get(final ServerWorld world) {
        return (PlayerResearchesData)world.getServer().overworld().getDataStorage().computeIfAbsent((Supplier)PlayerResearchesData::new, "the_vault_PlayerResearches");
    }
}
