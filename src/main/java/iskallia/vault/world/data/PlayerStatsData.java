package iskallia.vault.world.data;

import iskallia.vault.altar.RequiredItem;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.nbt.VMapNBT;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.stats.CrystalStat;
import iskallia.vault.world.stats.RaffleStat;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayerStatsData extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_PlayerStats";
    protected VMapNBT<UUID, Stats> playerStats;

    public PlayerStatsData() {
        this("the_vault_PlayerStats");
    }

    public PlayerStatsData(final String name) {
        super(name);
        this.playerStats = VMapNBT.ofUUID(Stats::new);
    }

    public Stats get(final PlayerEntity player) {
        return this.get(player.getUUID());
    }

    public Stats get(final UUID playerId) {
        return this.playerStats.computeIfAbsent(playerId, uuid -> new Stats());
    }

    public void onVaultFinished(final UUID playerId, final VaultRaid vault) {
        this.get(playerId).vaults.add(vault);
        this.setDirty();
    }

    public void onCrystalCrafted(final UUID playerId, final List<RequiredItem> recipe, final CrystalData.Type type) {
        this.get(playerId).crystals.add(new CrystalStat(recipe, type));
        this.setDirty();
    }

    public void onRaffleCompleted(final UUID playerId, final WeightedList<String> contributors, final String winner) {
        this.get(playerId).raffles.add(new RaffleStat(contributors, winner));
        this.setDirty();
    }

    public void setRaidRewardReceived(final UUID playerId) {
        this.get(playerId).finishedRaidReward = true;
        this.setDirty();
    }

    public void load(final CompoundNBT nbt) {
        this.playerStats.deserializeNBT(nbt.getList("Stats", 10));
    }

    public CompoundNBT save(final CompoundNBT nbt) {
        nbt.put("Stats", this.playerStats.serializeNBT());
        return nbt;
    }

    public static PlayerStatsData get(final ServerWorld world) {
        return get(world.getServer());
    }

    public static PlayerStatsData get(final MinecraftServer srv) {
        return (PlayerStatsData) srv.overworld().getDataStorage().computeIfAbsent((Supplier) PlayerStatsData::new, "the_vault_PlayerStats");
    }

    public static class Stats implements INBTSerializable<CompoundNBT> {
        protected VListNBT<VaultRaid, CompoundNBT> vaults;
        protected VListNBT<CrystalStat, CompoundNBT> crystals;
        protected VListNBT<RaffleStat, CompoundNBT> raffles;
        private boolean finishedRaidReward;

        public Stats() {
            this.vaults = VListNBT.of(VaultRaid::new);
            this.crystals = VListNBT.of(CrystalStat::new);
            this.raffles = VListNBT.of(RaffleStat::new);
            this.finishedRaidReward = false;
        }

        public List<VaultRaid> getVaults() {
            return Collections.unmodifiableList(this.vaults);
        }

        public List<CrystalStat> getCrystals() {
            return Collections.unmodifiableList(this.crystals);
        }

        public List<RaffleStat> getRaffles() {
            return Collections.unmodifiableList(this.raffles);
        }

        public boolean hasFinishedRaidReward() {
            return this.finishedRaidReward;
        }

        public CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.put("Vaults", this.vaults.serializeNBT());
            nbt.put("Crystals", this.crystals.serializeNBT());
            nbt.put("Raffles", this.raffles.serializeNBT());
            nbt.putBoolean("finishedRaidReward", this.finishedRaidReward);
            return nbt;
        }

        public void deserializeNBT(final CompoundNBT nbt) {
            this.vaults.deserializeNBT(nbt.getList("Vaults", 10));
            this.crystals.deserializeNBT(nbt.getList("Crystals", 10));
            this.raffles.deserializeNBT(nbt.getList("Raffles", 10));
            this.finishedRaidReward = nbt.getBoolean("finishedRaidReward");
        }
    }
}
