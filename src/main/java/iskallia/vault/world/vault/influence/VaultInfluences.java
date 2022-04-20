package iskallia.vault.world.vault.influence;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class VaultInfluences implements INBTSerializable<CompoundNBT>, Iterable<VaultInfluence> {
    private final List<VaultInfluence> influences;
    protected boolean initialized;

    public VaultInfluences() {
        this.influences = new ArrayList<VaultInfluence>();
        this.initialized = false;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized() {
        this.initialized = true;
    }

    public void addInfluence(final VaultInfluence influence, final VaultRaid vault, final ServerWorld world) {
        this.influences.add(influence);
        final Random rand = world.getRandom();
        vault.getPlayers().forEach(vPlayer -> influence.apply(vault, vPlayer, world, rand));
    }

    public void tick(final VaultRaid vault, final VaultPlayer vPlayer, final ServerWorld world) {
        this.forEach(influence -> influence.tick(vault, vPlayer, world));
    }

    public <T extends VaultInfluence> List<VaultInfluence> getInfluences(final Class<T> influenceClass) {
        return this.influences.stream().filter(influence -> influenceClass.isAssignableFrom(influence.getClass())).map(influence -> influence).collect(Collectors.toList());
    }

    public Iterator<VaultInfluence> iterator() {
        return this.influences.iterator();
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("initialized", this.initialized);
        final ListNBT influenceList = new ListNBT();
        for (final VaultInfluence influence : this.influences) {
            final CompoundNBT ct = new CompoundNBT();
            ct.putString("id", influence.getKey().toString());
            ct.put("data", (INBT) influence.serializeNBT());
        }
        tag.put("influences", (INBT) influenceList);
        return tag;
    }

    public void deserializeNBT(final CompoundNBT tag) {
        this.initialized = tag.getBoolean("initialized");
        final ListNBT influenceList = tag.getList("influences", 10);
        for (int i = 0; i < influenceList.size(); ++i) {
            final CompoundNBT ct = influenceList.getCompound(i);
            VaultInfluenceRegistry.getInfluence(new ResourceLocation(ct.getString("id"))).ifPresent(influence -> {
                influence.deserializeNBT(ct.getCompound("data"));
                this.influences.add(influence);
                return;
            });
        }
    }
}
