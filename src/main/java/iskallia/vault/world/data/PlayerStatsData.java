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


public class PlayerStatsData
        extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_PlayerStats";
    protected VMapNBT<UUID, Stats> playerStats = VMapNBT.ofUUID(Stats::new);

    public PlayerStatsData() {
        this("the_vault_PlayerStats");
    }

    public PlayerStatsData(String name) {
        super(name);
    }

    public Stats get(PlayerEntity player) {
        return get(player.getUUID());
    }

    public Stats get(UUID playerId) {
        return (Stats) this.playerStats.computeIfAbsent(playerId, uuid -> new Stats());
    }

    public void onVaultFinished(UUID playerId, VaultRaid vault) {
        (get(playerId)).vaults.add(vault);
        setDirty();
    }

    public void onCrystalCrafted(UUID playerId, List<RequiredItem> recipe, CrystalData.Type type) {
        (get(playerId)).crystals.add(new CrystalStat(recipe, type));
        setDirty();
    }

    public void onRaffleCompleted(UUID playerId, WeightedList<String> contributors, String winner) {
        (get(playerId)).raffles.add(new RaffleStat(contributors, winner));
        setDirty();
    }

    public void setRaidRewardReceived(UUID playerId) {
        (get(playerId)).finishedRaidReward = true;
        setDirty();
    }


    public void load(CompoundNBT nbt) {
        this.playerStats.deserializeNBT(nbt.getList("Stats", 10));
    }


    public CompoundNBT save(CompoundNBT nbt) {
        nbt.put("Stats", (INBT) this.playerStats.serializeNBT());
        return nbt;
    }

    public static PlayerStatsData get(ServerWorld world) {
        return get(world.getServer());
    }

    public static PlayerStatsData get(MinecraftServer srv) {
        return (PlayerStatsData) srv.overworld().getDataStorage().computeIfAbsent(PlayerStatsData::new, "the_vault_PlayerStats");
    }

    public static class Stats
            implements INBTSerializable<CompoundNBT> {
        protected VListNBT<VaultRaid, CompoundNBT> vaults = VListNBT.of(VaultRaid::new);
        protected VListNBT<CrystalStat, CompoundNBT> crystals = VListNBT.of(CrystalStat::new);
        protected VListNBT<RaffleStat, CompoundNBT> raffles = VListNBT.of(RaffleStat::new);
        private boolean finishedRaidReward = false;

        public List<VaultRaid> getVaults() {
            return Collections.unmodifiableList((List) this.vaults);
        }

        public List<CrystalStat> getCrystals() {
            return Collections.unmodifiableList((List) this.crystals);
        }

        public List<RaffleStat> getRaffles() {
            return Collections.unmodifiableList((List) this.raffles);
        }

        public boolean hasFinishedRaidReward() {
            return this.finishedRaidReward;
        }


        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("Vaults", (INBT) this.vaults.serializeNBT());
            nbt.put("Crystals", (INBT) this.crystals.serializeNBT());
            nbt.put("Raffles", (INBT) this.raffles.serializeNBT());
            nbt.putBoolean("finishedRaidReward", this.finishedRaidReward);
            return nbt;
        }


        public void deserializeNBT(CompoundNBT nbt) {
            this.vaults.deserializeNBT(nbt.getList("Vaults", 10));
            this.crystals.deserializeNBT(nbt.getList("Crystals", 10));
            this.raffles.deserializeNBT(nbt.getList("Raffles", 10));
            this.finishedRaidReward = nbt.getBoolean("finishedRaidReward");
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\PlayerStatsData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */