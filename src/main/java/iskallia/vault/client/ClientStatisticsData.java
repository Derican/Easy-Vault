// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client;

import iskallia.vault.util.calc.PlayerStatisticsCollector;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.*;

public class ClientStatisticsData
{
    private static final List<PlayerStatisticsCollector.AttributeSnapshot> attributeValues;
    private static final Map<PlayerFavourData.VaultGodType, Integer> favourStats;
    private static CompoundNBT serializedVaultStats;
    
    public static void receiveUpdate(final CompoundNBT statisticsData) {
        ClientStatisticsData.attributeValues.clear();
        ClientStatisticsData.favourStats.clear();
        final ListNBT attributes = statisticsData.getList("attributes", 10);
        for (int i = 0; i < attributes.size(); ++i) {
            ClientStatisticsData.attributeValues.add(PlayerStatisticsCollector.AttributeSnapshot.deserialize(attributes.getCompound(i)));
        }
        final CompoundNBT favourData = statisticsData.getCompound("favourStats");
        for (final PlayerFavourData.VaultGodType type : PlayerFavourData.VaultGodType.values()) {
            ClientStatisticsData.favourStats.put(type, favourData.getInt(type.name()));
        }
        ClientStatisticsData.serializedVaultStats = statisticsData.getCompound("vaultStats");
    }
    
    public static List<PlayerStatisticsCollector.AttributeSnapshot> getPlayerAttributeSnapshots() {
        return Collections.unmodifiableList((List<? extends PlayerStatisticsCollector.AttributeSnapshot>)ClientStatisticsData.attributeValues);
    }
    
    public static int getFavour(final PlayerFavourData.VaultGodType type) {
        return ClientStatisticsData.favourStats.getOrDefault(type, 0);
    }
    
    public static CompoundNBT getSerializedVaultStats() {
        return ClientStatisticsData.serializedVaultStats;
    }
    
    static {
        attributeValues = new ArrayList<PlayerStatisticsCollector.AttributeSnapshot>();
        favourStats = new HashMap<PlayerFavourData.VaultGodType, Integer>();
        ClientStatisticsData.serializedVaultStats = new CompoundNBT();
    }
}
