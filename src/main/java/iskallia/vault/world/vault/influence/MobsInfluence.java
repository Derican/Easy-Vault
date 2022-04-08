package iskallia.vault.world.vault.influence;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.VaultSpawner;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class MobsInfluence extends VaultInfluence {
    public static final ResourceLocation ID = Vault.id("mobs");

    private int mobsAdded;

    MobsInfluence() {
        super(ID);
    }

    public MobsInfluence(int mobsAdded) {
        this();
        this.mobsAdded = mobsAdded;
    }


    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
        player.getProperties().get(VaultRaid.SPAWNER).ifPresent(spawner -> {
            ((VaultSpawner) spawner.getBaseValue()).addMaxMobs(this.mobsAdded);
            spawner.updateNBT();
        });
    }


    public void remove(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
        player.getProperties().get(VaultRaid.SPAWNER).ifPresent(spawner -> {
            ((VaultSpawner) spawner.getBaseValue()).addMaxMobs(-this.mobsAdded);
            spawner.updateNBT();
        });
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.putInt("mobsAdded", this.mobsAdded);
        return tag;
    }


    public void deserializeNBT(CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.mobsAdded = tag.getInt("mobsAdded");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\influence\MobsInfluence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */