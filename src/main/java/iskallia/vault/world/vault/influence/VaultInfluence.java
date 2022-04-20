package iskallia.vault.world.vault.influence;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Random;

public class VaultInfluence implements INBTSerializable<CompoundNBT> {
    private final ResourceLocation key;

    public VaultInfluence(final ResourceLocation key) {
        this.key = key;
    }

    public final ResourceLocation getKey() {
        return this.key;
    }

    public void apply(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
    }

    public void remove(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
    }

    public void tick(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
    }

    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    public void deserializeNBT(final CompoundNBT tag) {
    }
}
