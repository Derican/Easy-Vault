package iskallia.vault.world.data;

import iskallia.vault.entity.eternal.EternalData;
import iskallia.vault.entity.eternal.EternalDataSnapshot;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.EternalSyncMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EternalsData extends WorldSavedData {
    private final Map<UUID, EternalGroup> playerMap = new HashMap<>();
    protected static final String DATA_NAME = "the_vault_Eternals";

    public EternalsData() {
        this("the_vault_Eternals");
    }

    public EternalsData(String name) {
        super(name);
    }

    public int getTotalEternals() {
        int total = 0;
        for (EternalGroup group : this.playerMap.values()) {

            total = (int) (total + group.getEternals().stream().filter(eternal -> !eternal.isAncient()).count());
        }
        return total;
    }

    @Nonnull
    public EternalGroup getEternals(PlayerEntity player) {
        return getEternals(player.getUUID());
    }

    @Nonnull
    public EternalGroup getEternals(UUID player) {
        return this.playerMap.computeIfAbsent(player, uuid -> new EternalGroup());
    }


    public List<String> getAllEternalNamesExcept(@Nullable String current) {
        Set<String> names = new HashSet<>();
        for (UUID id : this.playerMap.keySet()) {
            EternalGroup group = this.playerMap.get(id);
            for (EternalData data : group.getEternals()) {
                names.add(data.getName());
            }
        }
        if (current != null && !current.isEmpty()) names.remove(current);
        return new ArrayList<>(names);
    }


    public UUID add(UUID owner, String name, boolean isAncient) {
        UUID eternalId = getEternals(owner).addEternal(name, isAncient);
        setDirty();
        return eternalId;
    }

    @Nullable
    public UUID getOwnerOf(UUID eternalId) {
        return this.playerMap.entrySet().stream()
                .filter(e -> ((EternalGroup) e.getValue()).containsEternal(eternalId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public EternalData getEternal(UUID eternalId) {
        for (EternalGroup eternalGroup : this.playerMap.values()) {
            EternalData eternal = eternalGroup.get(eternalId);
            if (eternal != null) {
                return eternal;
            }
        }
        return null;
    }

    public boolean removeEternal(UUID eternalId) {
        for (EternalGroup eternalGroup : this.playerMap.values()) {
            if (eternalGroup.removeEternal(eternalId)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public EternalData.EquipmentInventory getEternalEquipmentInventory(UUID eternalId, Runnable onChange) {
        EternalData eternal = getEternal(eternalId);
        if (eternal == null) {
            return null;
        }
        return eternal.getEquipmentInventory(onChange);
    }

    public Map<UUID, List<EternalDataSnapshot>> getEternalDataSnapshots() {
        Map<UUID, List<EternalDataSnapshot>> eternalDataSet = new HashMap<>();
        this.playerMap.forEach((playerUUID, eternalGrp) -> (List) eternalDataSet.put(playerUUID, eternalGrp.getEternalSnapshots()));
        return eternalDataSet;
    }

    public void syncTo(ServerPlayerEntity sPlayer) {
        EternalSyncMessage pkt = new EternalSyncMessage(getEternalDataSnapshots());
        ModNetwork.CHANNEL.sendTo(pkt, sPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public void syncAll() {
        EternalSyncMessage pkt = new EternalSyncMessage(getEternalDataSnapshots());
        ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), pkt);
    }


    public void setDirty() {
        super.setDirty();
        syncAll();
    }


    public void load(CompoundNBT nbt) {
        ListNBT playerList = nbt.getList("PlayerEntries", 8);
        ListNBT eternalsList = nbt.getList("EternalEntries", 10);

        if (playerList.size() != eternalsList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }

        for (int i = 0; i < playerList.size(); i++) {
            UUID playerUUID = UUID.fromString(playerList.getString(i));
            getEternals(playerUUID).deserializeNBT(eternalsList.getCompound(i));
        }
    }


    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT playerList = new ListNBT();
        ListNBT eternalsList = new ListNBT();

        this.playerMap.forEach((uuid, eternalGroup) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));

            eternalsList.add(eternalGroup.serializeNBT());
        });
        nbt.put("PlayerEntries", (INBT) playerList);
        nbt.put("EternalEntries", (INBT) eternalsList);

        return nbt;
    }

    public static EternalsData get(ServerWorld world) {
        return get(world.getServer());
    }

    public static EternalsData get(MinecraftServer srv) {
        return (EternalsData) srv.overworld().getDataStorage().computeIfAbsent(EternalsData::new, "the_vault_Eternals");
    }


    public boolean isDirty() {
        return true;
    }


    public class EternalGroup
            implements INBTSerializable<CompoundNBT> {
        private final Map<UUID, EternalData> eternals = new HashMap<>();

        public List<EternalData> getEternals() {
            return new ArrayList<>(this.eternals.values());
        }

        public int getNonAncientEternalCount() {
            return

                    (int) this.eternals.entrySet().stream().filter(entry -> !((EternalData) entry.getValue()).isAncient()).count();
        }

        public UUID addEternal(String name, boolean isAncient) {
            return addEternal(EternalData.createEternal(EternalsData.this, name, isAncient)).getId();
        }

        private EternalData addEternal(EternalData newEternal) {
            this.eternals.put(newEternal.getId(), newEternal);
            this.eternals.values().forEach(eternal -> {
                if (eternal.isAncient()) {
                    eternal.setLevel(eternal.getMaxLevel());
                }
            });
            return newEternal;
        }

        @Nullable
        public EternalData get(UUID eternalId) {
            return this.eternals.get(eternalId);
        }

        public boolean containsEternal(UUID eternalId) {
            return (get(eternalId) != null);
        }

        public boolean containsEternal(String name) {
            for (EternalData eternal : this.eternals.values()) {
                if (eternal.getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        }

        public boolean containsOriginalEternal(String name, boolean onlyAncients) {
            for (EternalData eternal : this.eternals.values()) {
                if ((!onlyAncients || eternal.isAncient()) && eternal.getOriginalName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        }

        public boolean removeEternal(UUID eternalId) {
            EternalData eternal = this.eternals.remove(eternalId);
            if (eternal != null) {
                EternalsData.this.setDirty();
                return true;
            }
            return false;
        }


        @Nullable
        public EternalData getRandomAlive(Random random, Predicate<EternalData> eternalFilter) {
            List<EternalData> aliveEternals = (List<EternalData>) getEternals().stream().filter(EternalData::isAlive).filter(eternalFilter).collect(Collectors.toList());
            if (aliveEternals.isEmpty()) {
                return null;
            }
            return aliveEternals.get(random.nextInt(aliveEternals.size()));
        }


        @Nullable
        public EternalData getRandomAliveAncient(Random random, Predicate<EternalData> eternalFilter) {
            List<EternalData> aliveEternals = (List<EternalData>) getEternals().stream().filter(EternalData::isAlive).filter(EternalData::isAncient).filter(eternalFilter).collect(Collectors.toList());
            if (aliveEternals.isEmpty()) {
                return null;
            }
            return aliveEternals.get(random.nextInt(aliveEternals.size()));
        }

        public List<EternalDataSnapshot> getEternalSnapshots() {
            List<EternalDataSnapshot> snapshots = new ArrayList<>();
            getEternals().forEach(eternal -> snapshots.add(getEternalSnapshot(eternal)));
            return snapshots;
        }

        @Nullable
        public EternalDataSnapshot getEternalSnapshot(UUID eternalId) {
            EternalData eternal = get(eternalId);
            if (eternal == null) {
                return null;
            }
            return getEternalSnapshot(eternal);
        }

        public EternalDataSnapshot getEternalSnapshot(EternalData eternal) {
            return EternalDataSnapshot.getFromEternal(this, eternal);
        }


        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT eternalsList = new ListNBT();
            this.eternals.values().forEach(eternal -> eternalsList.add(eternal.serializeNBT()));
            nbt.put("EternalsList", (INBT) eternalsList);
            return nbt;
        }


        public void deserializeNBT(CompoundNBT nbt) {
            this.eternals.clear();
            ListNBT eternalsList = nbt.getList("EternalsList", 10);

            for (int i = 0; i < eternalsList.size(); i++)
                addEternal(EternalData.fromNBT(EternalsData.this, eternalsList.getCompound(i)));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\EternalsData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */