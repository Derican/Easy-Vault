package iskallia.vault.client;

import iskallia.vault.util.calc.PlayerStatisticsCollector;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.*;

public class ClientStatisticsData {
    private static final List<PlayerStatisticsCollector.AttributeSnapshot> attributeValues = new ArrayList<>();
    private static final Map<PlayerFavourData.VaultGodType, Integer> favourStats = new HashMap<>();
    private static CompoundNBT serializedVaultStats = new CompoundNBT();

    public static void receiveUpdate(CompoundNBT statisticsData) {
        attributeValues.clear();
        favourStats.clear();

        ListNBT attributes = statisticsData.getList("attributes", 10);
        for (int i = 0; i < attributes.size(); i++) {
            attributeValues.add(PlayerStatisticsCollector.AttributeSnapshot.deserialize(attributes.getCompound(i)));
        }
        CompoundNBT favourData = statisticsData.getCompound("favourStats");
        for (PlayerFavourData.VaultGodType type : PlayerFavourData.VaultGodType.values()) {
            favourStats.put(type, Integer.valueOf(favourData.getInt(type.name())));
        }

        serializedVaultStats = statisticsData.getCompound("vaultStats");
    }

    public static List<PlayerStatisticsCollector.AttributeSnapshot> getPlayerAttributeSnapshots() {
        return Collections.unmodifiableList(attributeValues);
    }

    public static int getFavour(PlayerFavourData.VaultGodType type) {
        return ((Integer) favourStats.getOrDefault(type, Integer.valueOf(0))).intValue();
    }

    public static CompoundNBT getSerializedVaultStats() {
        return serializedVaultStats;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\ClientStatisticsData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */