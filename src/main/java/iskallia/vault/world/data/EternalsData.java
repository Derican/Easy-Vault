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
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EternalsData extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_Eternals";
    private final Map<UUID, EternalGroup> playerMap;

    public EternalsData() {
        this("the_vault_Eternals");
    }

    public EternalsData(final String name) {
        super(name);
        this.playerMap = new HashMap<UUID, EternalGroup>();
    }

    public int getTotalEternals() {
        int total = 0;
        for (final EternalGroup group : this.playerMap.values()) {
            total += (int) group.getEternals().stream().filter(eternal -> !eternal.isAncient()).count();
        }
        return total;
    }

    @Nonnull
    public EternalGroup getEternals(final PlayerEntity player) {
        return this.getEternals(player.getUUID());
    }

    @Nonnull
    public EternalGroup getEternals(final UUID player) {
        return this.playerMap.computeIfAbsent(player, uuid -> new EternalGroup());
    }

    public List<String> getAllEternalNamesExcept(@Nullable final String current) {
        final Set<String> names = new HashSet<String>();
        for (final UUID id : this.playerMap.keySet()) {
            final EternalGroup group = this.playerMap.get(id);
            for (final EternalData data : group.getEternals()) {
                names.add(data.getName());
            }
        }
        if (current != null && !current.isEmpty()) {
            names.remove(current);
        }
        return new ArrayList<String>(names);
    }

    public UUID add(final UUID owner, final String name, final boolean isAncient) {
        final UUID eternalId = this.getEternals(owner).addEternal(name, isAncient);
        this.setDirty();
        return eternalId;
    }

    @Nullable
    public UUID getOwnerOf(final UUID eternalId) {
        return this.playerMap.entrySet().stream().filter(e -> e.getValue().containsEternal(eternalId)).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    @Nullable
    public EternalData getEternal(final UUID eternalId) {
        for (final EternalGroup eternalGroup : this.playerMap.values()) {
            final EternalData eternal = eternalGroup.get(eternalId);
            if (eternal != null) {
                return eternal;
            }
        }
        return null;
    }

    public boolean removeEternal(final UUID eternalId) {
        for (final EternalGroup eternalGroup : this.playerMap.values()) {
            if (eternalGroup.removeEternal(eternalId)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public EternalData.EquipmentInventory getEternalEquipmentInventory(final UUID eternalId, final Runnable onChange) {
        final EternalData eternal = this.getEternal(eternalId);
        if (eternal == null) {
            return null;
        }
        return eternal.getEquipmentInventory(onChange);
    }

    public Map<UUID, List<EternalDataSnapshot>> getEternalDataSnapshots() {
        final Map<UUID, List<EternalDataSnapshot>> eternalDataSet = new HashMap<UUID, List<EternalDataSnapshot>>();
        this.playerMap.forEach((playerUUID, eternalGrp) -> {
            final List<EternalDataSnapshot> list = eternalDataSet.put(playerUUID, eternalGrp.getEternalSnapshots());
            return;
        });
        return eternalDataSet;
    }

    public void syncTo(final ServerPlayerEntity sPlayer) {
        final EternalSyncMessage pkt = new EternalSyncMessage(this.getEternalDataSnapshots());
        ModNetwork.CHANNEL.sendTo(pkt, sPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public void syncAll() {
        final EternalSyncMessage pkt = new EternalSyncMessage(this.getEternalDataSnapshots());
        ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), pkt);
    }

    public void setDirty() {
        super.setDirty();
        this.syncAll();
    }

    public void load(final CompoundNBT nbt) {
        final ListNBT playerList = nbt.getList("PlayerEntries", 8);
        final ListNBT eternalsList = nbt.getList("EternalEntries", 10);
        if (playerList.size() != eternalsList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }
        for (int i = 0; i < playerList.size(); ++i) {
            final UUID playerUUID = UUID.fromString(playerList.getString(i));
            this.getEternals(playerUUID).deserializeNBT(eternalsList.getCompound(i));
        }
    }

    public CompoundNBT save(final CompoundNBT nbt) {
        final ListNBT playerList = new ListNBT();
        final ListNBT eternalsList = new ListNBT();
        this.playerMap.forEach((uuid, eternalGroup) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));
            eternalsList.add(eternalGroup.serializeNBT());
            return;
        });
        nbt.put("PlayerEntries", playerList);
        nbt.put("EternalEntries", eternalsList);
        return nbt;
    }

    public static EternalsData get(final ServerWorld world) {
        return get(world.getServer());
    }

    public static EternalsData get(final MinecraftServer srv) {
        return (EternalsData) srv.overworld().getDataStorage().computeIfAbsent((Supplier) EternalsData::new, "the_vault_Eternals");
    }

    public boolean isDirty() {
        return true;
    }

    public class EternalGroup implements INBTSerializable<CompoundNBT> {
        private final Map<UUID, EternalData> eternals;

        public EternalGroup() {
            this.eternals = new HashMap<UUID, EternalData>();
        }

        public List<EternalData> getEternals() {
            return new ArrayList<EternalData>(this.eternals.values());
        }

        public int getNonAncientEternalCount() {
            return (int) this.eternals.entrySet().stream().filter(entry -> !entry.getValue().isAncient()).count();
        }

        public UUID addEternal(final String name, final boolean isAncient) {
            return this.addEternal(EternalData.createEternal(EternalsData.this, name, isAncient)).getId();
        }

        private EternalData addEternal(final EternalData newEternal) {
            this.eternals.put(newEternal.getId(), newEternal);
            this.eternals.values().forEach(eternal -> {
                if (eternal.isAncient()) {
                    eternal.setLevel(eternal.getMaxLevel());
                }
                return;
            });
            return newEternal;
        }

        @Nullable
        public EternalData get(final UUID eternalId) {
            return this.eternals.get(eternalId);
        }

        public boolean containsEternal(final UUID eternalId) {
            return this.get(eternalId) != null;
        }

        public boolean containsEternal(final String name) {
            for (final EternalData eternal : this.eternals.values()) {
                if (eternal.getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        }

        public boolean containsOriginalEternal(final String name, final boolean onlyAncients) {
            for (final EternalData eternal : this.eternals.values()) {
                if ((!onlyAncients || eternal.isAncient()) && eternal.getOriginalName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        }

        public boolean removeEternal(final UUID eternalId) {
            final EternalData eternal = this.eternals.remove(eternalId);
            if (eternal != null) {
                EternalsData.this.setDirty();
                return true;
            }
            return false;
        }

        @Nullable
        public EternalData getRandomAlive(final Random random, final Predicate<EternalData> eternalFilter) {
            final List<EternalData> aliveEternals = this.getEternals().stream().filter(EternalData::isAlive).filter(eternalFilter).collect(Collectors.toList());
            if (aliveEternals.isEmpty()) {
                return null;
            }
            return aliveEternals.get(random.nextInt(aliveEternals.size()));
        }

        @Nullable
        public EternalData getRandomAliveAncient(final Random random, final Predicate<EternalData> eternalFilter) {
            final List<EternalData> aliveEternals = this.getEternals().stream().filter(EternalData::isAlive).filter(EternalData::isAncient).filter(eternalFilter).collect(Collectors.toList());
            if (aliveEternals.isEmpty()) {
                return null;
            }
            return aliveEternals.get(random.nextInt(aliveEternals.size()));
        }

        public List<EternalDataSnapshot> getEternalSnapshots() {
            final List<EternalDataSnapshot> snapshots = new ArrayList<EternalDataSnapshot>();
            this.getEternals().forEach(eternal -> snapshots.add(this.getEternalSnapshot(eternal)));
            return snapshots;
        }

        @Nullable
        public EternalDataSnapshot getEternalSnapshot(final UUID eternalId) {
            final EternalData eternal = this.get(eternalId);
            if (eternal == null) {
                return null;
            }
            return this.getEternalSnapshot(eternal);
        }

        public EternalDataSnapshot getEternalSnapshot(final EternalData eternal) {
            return EternalDataSnapshot.getFromEternal(this, eternal);
        }

        public CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            final ListNBT eternalsList = new ListNBT();
            this.eternals.values().forEach(eternal -> eternalsList.add(eternal.serializeNBT()));
            nbt.put("EternalsList", eternalsList);
            return nbt;
        }

        public void deserializeNBT(final CompoundNBT nbt) {
            this.eternals.clear();
            final ListNBT eternalsList = nbt.getList("EternalsList", 10);
            for (int i = 0; i < eternalsList.size(); ++i) {
                this.addEternal(EternalData.fromNBT(EternalsData.this, eternalsList.getCompound(i)));
            }
        }
    }
}
