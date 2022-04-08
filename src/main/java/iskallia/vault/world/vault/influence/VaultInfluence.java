package iskallia.vault.world.vault.influence;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Random;

public class VaultInfluence
        implements INBTSerializable<CompoundNBT> {
    private final ResourceLocation key;

    public VaultInfluence(ResourceLocation key) {
        this.key = key;
    }

    public final ResourceLocation getKey() {
        return this.key;
    }


    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
    }

    public void remove(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
    }

    public void tick(VaultRaid vault, VaultPlayer player, ServerWorld world) {
    }

    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    public void deserializeNBT(CompoundNBT tag) {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\influence\VaultInfluence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */