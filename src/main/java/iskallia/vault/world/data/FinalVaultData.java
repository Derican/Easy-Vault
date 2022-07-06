// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.data;

import java.util.function.Supplier;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import java.util.UUID;
import iskallia.vault.nbt.VMapNBT;
import net.minecraft.world.storage.WorldSavedData;

public class FinalVaultData extends WorldSavedData
{
    protected static final String DATA_NAME = "the_vault_FinalVault";
    private VMapNBT<UUID, Integer> timesCompleted;
    
    public FinalVaultData() {
        super("the_vault_FinalVault");
        this.timesCompleted = VMapNBT.ofUUIDToInt();
    }
    
    public int getTimesCompleted(final UUID player) {
        return this.timesCompleted.getOrDefault(player, 0);
    }
    
    public void onCompleted(final UUID player) {
        this.timesCompleted.put(player, this.timesCompleted.getOrDefault(player, 0) + 1);
        this.setDirty(true);
    }
    
    public void load(final CompoundNBT nbt) {
        this.timesCompleted.deserializeNBT(nbt.getList("timesCompleted", 10));
    }
    
    public CompoundNBT save(final CompoundNBT nbt) {
        nbt.put("timesCompleted", (INBT)this.timesCompleted.serializeNBT());
        return nbt;
    }
    
    public static FinalVaultData get(final ServerWorld world) {
        return (FinalVaultData)world.getServer().overworld().getDataStorage().computeIfAbsent((Supplier)FinalVaultData::new, "the_vault_FinalVault");
    }
}
