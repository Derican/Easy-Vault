// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.influence;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.VaultSpawner;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class MobsInfluence extends VaultInfluence
{
    public static final ResourceLocation ID;
    private int mobsAdded;
    
    MobsInfluence() {
        super(MobsInfluence.ID);
    }
    
    public MobsInfluence(final int mobsAdded) {
        this();
        this.mobsAdded = mobsAdded;
    }
    
    @Override
    public void apply(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
        player.getProperties().get(VaultRaid.SPAWNER).ifPresent(spawner -> {
            ((VaultSpawner)spawner.getBaseValue()).addMaxMobs(this.mobsAdded);
            spawner.updateNBT();
        });
    }
    
    @Override
    public void remove(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
        player.getProperties().get(VaultRaid.SPAWNER).ifPresent(spawner -> {
            ((VaultSpawner)spawner.getBaseValue()).addMaxMobs(-this.mobsAdded);
            spawner.updateNBT();
        });
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        tag.putInt("mobsAdded", this.mobsAdded);
        return tag;
    }
    
    @Override
    public void deserializeNBT(final CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.mobsAdded = tag.getInt("mobsAdded");
    }
    
    static {
        ID = Vault.id("mobs");
    }
}
