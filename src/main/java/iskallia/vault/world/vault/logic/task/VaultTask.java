// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.task;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class VaultTask implements IVaultTask, INBTSerializable<CompoundNBT>
{
    public static final Map<ResourceLocation, VaultTask> REGISTRY;
    public static final VaultTask EMPTY;
    private ResourceLocation id;
    protected IVaultTask task;
    
    protected VaultTask() {
    }
    
    public VaultTask(final ResourceLocation id, final IVaultTask task) {
        this.id = id;
        this.task = task;
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    @Override
    public void execute(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        this.task.execute(vault, player, world);
    }
    
    public VaultTask then(final VaultTask other) {
        return new CompoundVaultTask(this, other, ">", this.task.then(other));
    }
    
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", this.getId().toString());
        return nbt;
    }
    
    public void deserializeNBT(final CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
    }
    
    public static VaultTask fromNBT(final CompoundNBT nbt) {
        if (nbt.contains("Id", 8)) {
            return VaultTask.REGISTRY.get(new ResourceLocation(nbt.getString("Id")));
        }
        return CompoundVaultTask.fromNBT(nbt);
    }
    
    public static VaultTask register(final ResourceLocation id, final IVaultTask task) {
        final VaultTask vaultTask = new VaultTask(id, task);
        VaultTask.REGISTRY.put(id, vaultTask);
        return vaultTask;
    }
    
    static {
        REGISTRY = new HashMap<ResourceLocation, VaultTask>();
        EMPTY = register(Vault.id("empty"), (vault, player, world) -> {});
    }
}
