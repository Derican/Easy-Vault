package iskallia.vault.world.vault.logic.condition;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class VaultCondition
        implements IVaultCondition, INBTSerializable<CompoundNBT> {
    public static final Map<ResourceLocation, VaultCondition> REGISTRY = new HashMap<>();

    private ResourceLocation id;

    protected IVaultCondition condition;


    protected VaultCondition() {
    }

    protected VaultCondition(ResourceLocation id, IVaultCondition condition) {
        this.id = id;
        this.condition = condition;
    }

    public ResourceLocation getId() {
        return this.id;
    }


    public boolean test(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        return this.condition.test(vault, player, world);
    }

    public VaultCondition negate() {
        return new CompoundVaultCondition(this, null, "~", this.condition.negate());
    }

    public VaultCondition and(VaultCondition other) {
        return new CompoundVaultCondition(this, other, "&", this.condition.and(other));
    }

    public VaultCondition or(VaultCondition other) {
        return new CompoundVaultCondition(this, other, "|", this.condition.or(other));
    }

    public VaultCondition xor(VaultCondition other) {
        return new CompoundVaultCondition(this, other, "^", this.condition.xor(other));
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", getId().toString());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
    }

    public static VaultCondition fromNBT(CompoundNBT nbt) {
        if (nbt.contains("Id", 8)) {
            return REGISTRY.get(new ResourceLocation(nbt.getString("Id")));
        }

        return CompoundVaultCondition.fromNBT(nbt);
    }

    public static VaultCondition register(ResourceLocation id, IVaultCondition condition) {
        VaultCondition vaultCondition = new VaultCondition(id, condition);
        REGISTRY.put(vaultCondition.getId(), vaultCondition);
        return vaultCondition;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\condition\VaultCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */