package iskallia.vault.world.data;

import iskallia.vault.altar.AltarInfusionRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.*;

public class PlayerVaultAltarData extends WorldSavedData {
    private Map<UUID, AltarInfusionRecipe> playerMap = new HashMap<>();
    protected static final String DATA_NAME = "the_vault_PlayerAltarRecipes";
    private HashMap<UUID, List<BlockPos>> playerAltars = new HashMap<>();

    public PlayerVaultAltarData() {
        super("the_vault_PlayerAltarRecipes");
    }

    public PlayerVaultAltarData(String name) {
        super(name);
    }


    public AltarInfusionRecipe getRecipe(PlayerEntity player) {
        return getRecipe(player.getUUID());
    }

    public AltarInfusionRecipe getRecipe(UUID uuid) {
        return this.playerMap.get(uuid);
    }

    public AltarInfusionRecipe getRecipe(ServerWorld world, BlockPos pos, ServerPlayerEntity player) {
        AltarInfusionRecipe recipe = this.playerMap.computeIfAbsent(player.getUUID(), k -> new AltarInfusionRecipe(world, pos, player));
        setDirty();
        return recipe;
    }

    public boolean hasRecipe(UUID uuid) {
        return this.playerMap.containsKey(uuid);
    }

    public PlayerVaultAltarData addRecipe(UUID uuid, AltarInfusionRecipe recipe) {
        this.playerMap.put(uuid, recipe);

        setDirty();
        return this;
    }

    public PlayerVaultAltarData removeRecipe(UUID uuid) {
        this.playerMap.remove(uuid);

        setDirty();
        return this;
    }

    public List<BlockPos> getAltars(UUID uuid) {
        if (uuid == null) {
            return new ArrayList<>();
        }
        this.playerAltars.computeIfAbsent(uuid, k -> new ArrayList());
        setDirty();
        return this.playerAltars.get(uuid);
    }


    public PlayerVaultAltarData addAltar(UUID uuid, BlockPos altarPos) {
        getAltars(uuid).add(altarPos);
        setDirty();
        return this;
    }

    public PlayerVaultAltarData removeAltar(UUID uuid, BlockPos altarPos) {
        getAltars(uuid).remove(altarPos);
        setDirty();
        return this;
    }


    public void load(CompoundNBT nbt) {
        ListNBT playerList = nbt.getList("PlayerEntries", 8);
        ListNBT recipeList = nbt.getList("AltarRecipeEntries", 10);
        ListNBT playerBlockPosList = nbt.getList("PlayerBlockPosEntries", 8);
        ListNBT blockPosList = nbt.getList("BlockPosEntries", 9);

        if (playerList.size() != recipeList.size() || playerBlockPosList.size() != blockPosList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }
        int i;
        for (i = 0; i < playerList.size(); i++) {
            UUID playerUUID = UUID.fromString(playerList.getString(i));
            this.playerMap.put(playerUUID, AltarInfusionRecipe.deserialize(recipeList.getCompound(i)));
        }

        for (i = 0; i < playerBlockPosList.size(); i++) {
            UUID playerUUID = UUID.fromString(playerBlockPosList.getString(i));
            List<BlockPos> positions = new ArrayList<>();
            for (INBT compound : blockPosList.getList(i)) {
                CompoundNBT posTag = (CompoundNBT) compound;
                BlockPos pos = NBTUtil.readBlockPos(posTag);
                positions.add(pos);
            }
            this.playerAltars.put(playerUUID, positions);
        }
    }


    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT playerList = new ListNBT();
        ListNBT recipeList = new ListNBT();

        ListNBT playerBlockPosList = new ListNBT();
        ListNBT blockPosList = new ListNBT();

        this.playerMap.forEach((uuid, recipe) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));

            recipeList.add(AltarInfusionRecipe.serialize(recipe));
        });
        this.playerAltars.forEach((uuid, altarPositions) -> {
            playerBlockPosList.add(StringNBT.valueOf(uuid.toString()));

            ListNBT positions = new ListNBT();
            altarPositions.forEach(());
            blockPosList.add(positions);
        });
        nbt.put("PlayerEntries", (INBT) playerList);
        nbt.put("AltarRecipeEntries", (INBT) recipeList);
        nbt.put("PlayerBlockPosEntries", (INBT) playerBlockPosList);
        nbt.put("BlockPosEntries", (INBT) blockPosList);

        return nbt;
    }

    public static PlayerVaultAltarData get(ServerWorld world) {
        return (PlayerVaultAltarData) world.getServer().overworld()
                .getDataStorage().computeIfAbsent(PlayerVaultAltarData::new, "the_vault_PlayerAltarRecipes");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\PlayerVaultAltarData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */