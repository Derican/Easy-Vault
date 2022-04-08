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


public class VaultInfluences
        implements INBTSerializable<CompoundNBT>, Iterable<VaultInfluence> {
    private final List<VaultInfluence> influences = new ArrayList<>();
    protected boolean initialized = false;

    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized() {
        this.initialized = true;
    }

    public void addInfluence(VaultInfluence influence, VaultRaid vault, ServerWorld world) {
        this.influences.add(influence);

        Random rand = world.getRandom();
        vault.getPlayers().forEach(vPlayer -> influence.apply(vault, vPlayer, world, rand));
    }

    public void tick(VaultRaid vault, VaultPlayer vPlayer, ServerWorld world) {
        forEach(influence -> influence.tick(vault, vPlayer, world));
    }

    public <T extends VaultInfluence> List<T> getInfluences(Class<T> influenceClass) {
        return (List<T>) this.influences.stream()
                .filter(influence -> influenceClass.isAssignableFrom(influence.getClass()))
                .map(influence -> influence)
                .collect(Collectors.toList());
    }


    public Iterator<VaultInfluence> iterator() {
        return this.influences.iterator();
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("initialized", this.initialized);

        ListNBT influenceList = new ListNBT();
        for (VaultInfluence influence : this.influences) {
            CompoundNBT ct = new CompoundNBT();
            ct.putString("id", influence.getKey().toString());
            ct.put("data", (INBT) influence.serializeNBT());
        }
        tag.put("influences", (INBT) influenceList);
        return tag;
    }


    public void deserializeNBT(CompoundNBT tag) {
        this.initialized = tag.getBoolean("initialized");

        ListNBT influenceList = tag.getList("influences", 10);
        for (int i = 0; i < influenceList.size(); i++) {
            CompoundNBT ct = influenceList.getCompound(i);
            VaultInfluenceRegistry.getInfluence(new ResourceLocation(ct.getString("id"))).ifPresent(influence -> {
                influence.deserializeNBT(ct.getCompound("data"));
                this.influences.add(influence);
            });
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\influence\VaultInfluences.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */