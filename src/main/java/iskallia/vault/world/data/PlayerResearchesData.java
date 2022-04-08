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

public class PlayerResearchesData
        extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_PlayerResearches";
    private Map<UUID, ResearchTree> playerMap = new HashMap<>();

    public PlayerResearchesData() {
        super("the_vault_PlayerResearches");
    }

    public PlayerResearchesData(String name) {
        super(name);
    }

    public ResearchTree getResearches(PlayerEntity player) {
        return getResearches(player.getUUID());
    }

    public ResearchTree getResearches(UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, ResearchTree::new);
    }


    public PlayerResearchesData research(ServerPlayerEntity player, Research research) {
        ResearchTree researchTree = getResearches((PlayerEntity) player);
        researchTree.research(research.getName());

        researchTree.sync(player.getServer());

        setDirty();
        return this;
    }

    public PlayerResearchesData resetResearchTree(ServerPlayerEntity player) {
        ResearchTree researchTree = getResearches((PlayerEntity) player);
        researchTree.resetAll();

        researchTree.sync(player.getServer());

        setDirty();
        return this;
    }


    public void load(CompoundNBT nbt) {
        ListNBT playerList = nbt.getList("PlayerEntries", 8);
        ListNBT researchesList = nbt.getList("ResearchEntries", 10);

        if (playerList.size() != researchesList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }

        for (int i = 0; i < playerList.size(); i++) {
            UUID playerUUID = UUID.fromString(playerList.getString(i));
            getResearches(playerUUID).deserializeNBT(researchesList.getCompound(i));
        }
    }


    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT playerList = new ListNBT();
        ListNBT researchesList = new ListNBT();

        this.playerMap.forEach((uuid, researchTree) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));

            researchesList.add(researchTree.serializeNBT());
        });
        nbt.put("PlayerEntries", (INBT) playerList);
        nbt.put("ResearchEntries", (INBT) researchesList);

        return nbt;
    }

    public static PlayerResearchesData get(ServerWorld world) {
        return (PlayerResearchesData) world.getServer().overworld()
                .getDataStorage().computeIfAbsent(PlayerResearchesData::new, "the_vault_PlayerResearches");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\PlayerResearchesData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */