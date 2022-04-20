package iskallia.vault.world.vault.logic.condition;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class VaultCondition implements IVaultCondition, INBTSerializable<CompoundNBT> {
    public static final Map<ResourceLocation, VaultCondition> REGISTRY;
    private ResourceLocation id;
    protected IVaultCondition condition;

    protected VaultCondition() {
    }

    protected VaultCondition(final ResourceLocation id, final IVaultCondition condition) {
        this.id = id;
        this.condition = condition;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public boolean test(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        return this.condition.test(vault, player, world);
    }

    @Override
    public VaultCondition negate() {
        return new CompoundVaultCondition(this, null, "~", this.condition.negate());
    }

    public VaultCondition and(final VaultCondition other) {
        return new CompoundVaultCondition(this, other, "&", this.condition.and(other));
    }

    public VaultCondition or(final VaultCondition other) {
        return new CompoundVaultCondition(this, other, "|", this.condition.or(other));
    }

    public VaultCondition xor(final VaultCondition other) {
        return new CompoundVaultCondition(this, other, "^", this.condition.xor(other));
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", this.getId().toString());
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
    }

    public static VaultCondition fromNBT(final CompoundNBT nbt) {
        if (nbt.contains("Id", 8)) {
            return VaultCondition.REGISTRY.get(new ResourceLocation(nbt.getString("Id")));
        }
        return CompoundVaultCondition.fromNBT(nbt);
    }

    public static VaultCondition register(final ResourceLocation id, final IVaultCondition condition) {
        final VaultCondition vaultCondition = new VaultCondition(id, condition);
        VaultCondition.REGISTRY.put(vaultCondition.getId(), vaultCondition);
        return vaultCondition;
    }

    static {
        REGISTRY = new HashMap<ResourceLocation, VaultCondition>();
    }
}
