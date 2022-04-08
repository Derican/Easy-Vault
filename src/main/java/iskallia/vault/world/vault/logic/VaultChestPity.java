package iskallia.vault.world.vault.logic;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.data.WeightedDoubleList;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.task.IVaultTask;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class VaultChestPity
        implements INBTSerializable<CompoundNBT>, IVaultTask {
    private final Map<UUID, Integer> ticksElapsed = new HashMap<>();


    public void execute(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        int ticks = ((Integer) this.ticksElapsed.getOrDefault(player.getPlayerId(), Integer.valueOf(0))).intValue();
        this.ticksElapsed.put(player.getPlayerId(), Integer.valueOf(ticks + 1));
    }

    public String getRandomChestRarity(WeightedDoubleList<String> chestWeights, PlayerEntity player, Random rand) {
        int ticks = getTicksElapsed(player);
        String rarity = (String) ModConfigs.VAULT_CHEST_META.getPityAdjustedRarity(chestWeights, ticks).getRandom(rand);
        resetTicks(player);
        return rarity;
    }

    public int getTicksElapsed(PlayerEntity player) {
        return ((Integer) this.ticksElapsed.getOrDefault(player.getUUID(), Integer.valueOf(0))).intValue();
    }

    public void resetTicks(PlayerEntity player) {
        this.ticksElapsed.put(player.getUUID(), Integer.valueOf(0));
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        this.ticksElapsed.forEach((uuid, ticks) -> nbt.putInt(uuid.toString(), ticks.intValue()));
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.ticksElapsed.clear();

        for (String key : nbt.getAllKeys()) {
            UUID playerUUID;
            try {
                playerUUID = UUID.fromString(key);
            } catch (IllegalArgumentException exc) {
                continue;
            }
            this.ticksElapsed.put(playerUUID, Integer.valueOf(nbt.getInt(key)));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\VaultChestPity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */