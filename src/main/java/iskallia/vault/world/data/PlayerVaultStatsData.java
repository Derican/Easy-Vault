package iskallia.vault.world.data;

import iskallia.vault.block.item.TrophyStatueBlockItem;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.util.WeekKey;
import iskallia.vault.world.data.generated.ChallengeCrystalArchive;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import javax.annotation.Nonnull;
import java.util.*;

public class PlayerVaultStatsData extends WorldSavedData {
    private final Map<UUID, PlayerVaultStats> playerMap = new HashMap<>();
    protected static final String DATA_NAME = "the_vault_PlayerVaultLevels";
    private final Map<WeekKey, List<PlayerRecordEntry>> weeklyVaultRecords = new HashMap<>();
    private final Set<WeekKey> grantedRewards = new HashSet<>();

    public PlayerVaultStatsData() {
        super("the_vault_PlayerVaultLevels");
    }

    public PlayerVaultStatsData(String name) {
        super(name);
    }

    public PlayerVaultStats getVaultStats(PlayerEntity player) {
        return getVaultStats(player.getUUID());
    }

    public PlayerVaultStats getVaultStats(UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, PlayerVaultStats::new);
    }


    public PlayerVaultStatsData setVaultLevel(ServerPlayerEntity player, int level) {
        getVaultStats((PlayerEntity) player).setVaultLevel(player.getServer(), level);

        setDirty();
        return this;
    }

    public PlayerVaultStatsData addVaultExp(ServerPlayerEntity player, int exp) {
        getVaultStats((PlayerEntity) player).addVaultExp(player.getServer(), exp);

        setDirty();
        return this;
    }

    public PlayerVaultStatsData spendSkillPts(ServerPlayerEntity player, int amount) {
        getVaultStats((PlayerEntity) player).spendSkillPoints(player.getServer(), amount);

        setDirty();
        return this;
    }

    public PlayerVaultStatsData spendKnowledgePts(ServerPlayerEntity player, int amount) {
        getVaultStats((PlayerEntity) player).spendKnowledgePoints(player.getServer(), amount);

        setDirty();
        return this;
    }

    public PlayerVaultStatsData addSkillPoint(ServerPlayerEntity player, int amount) {
        getVaultStats((PlayerEntity) player)
                .addSkillPoints(amount)
                .sync(player.getLevel().getServer());

        setDirty();
        return this;
    }

    public PlayerVaultStatsData addSkillPointNoSync(UUID playerId, int amount) {
        getVaultStats(playerId).addSkillPoints(amount);
        setDirty();
        return this;
    }

    public PlayerVaultStatsData addKnowledgePoints(ServerPlayerEntity player, int amount) {
        getVaultStats((PlayerEntity) player)
                .addKnowledgePoints(amount)
                .sync(player.getLevel().getServer());

        setDirty();
        return this;
    }

    public PlayerVaultStatsData reset(ServerPlayerEntity player) {
        getVaultStats((PlayerEntity) player).reset(player.getServer());

        setDirty();
        return this;
    }

    @Nonnull
    public PlayerRecordEntry getFastestVaultTime() {
        return getFastestVaultTime(WeekKey.current());
    }

    @Nonnull
    public PlayerRecordEntry getFastestVaultTime(WeekKey week) {
        return ((List<PlayerRecordEntry>) this.weeklyVaultRecords.computeIfAbsent(week, key -> new ArrayList()))
                .stream()
                .min(Comparator.comparing(PlayerRecordEntry::getTickCount))
                .orElse(PlayerRecordEntry.DEFAULT);
    }

    public void updateFastestVaultTime(PlayerEntity player, int timeTicks) {
        updateFastestVaultTime(new PlayerRecordEntry(player.getUUID(), player.getName().getString(), timeTicks));
    }

    private void updateFastestVaultTime(PlayerRecordEntry entry) {
        ((List<PlayerRecordEntry>) this.weeklyVaultRecords.computeIfAbsent(WeekKey.current(), key -> new ArrayList())).add(entry);
        setDirty();
    }

    public boolean setRewardGranted(WeekKey weekKey) {
        if (this.grantedRewards.add(weekKey)) {
            setDirty();
            return true;
        }
        return false;
    }

    public boolean hasGeneratedReward(WeekKey weekKey) {
        return this.grantedRewards.contains(weekKey);
    }

    public void generateRecordRewards(ServerWorld overWorld) {
        WeekKey week = WeekKey.previous();
        if (hasGeneratedReward(week)) {
            return;
        }
        PlayerRecordEntry previousRecord = getFastestVaultTime(week);
        if (previousRecord == PlayerRecordEntry.DEFAULT) {
            return;
        }
        PlayerVaultStats stats = getVaultStats(previousRecord.getPlayerUUID());
        int vLevel = stats.getVaultLevel();
        NonNullList<ItemStack> loot = generateTrophyBox(overWorld, vLevel);

        loot.set(4, TrophyStatueBlockItem.getTrophy(overWorld, week));
        loot.set(13, new ItemStack((IItemProvider) ModItems.UNIDENTIFIED_ARTIFACT));
        loot.set(22, ChallengeCrystalArchive.getRandom());

        ItemStack box = new ItemStack((IItemProvider) Items.WHITE_SHULKER_BOX);
        box.getOrCreateTag().put("BlockEntityTag", (INBT) new CompoundNBT());
        ItemStackHelper.saveAllItems(box.getOrCreateTag().getCompound("BlockEntityTag"), loot);

        ScheduledItemDropData.get(overWorld).addDrop(previousRecord.getPlayerUUID(), box);
        this.grantedRewards.add(week);
        setDirty();
    }

    private static NonNullList<ItemStack> generateTrophyBox(ServerWorld overWorld, int vaultLevel) {
        LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(vaultLevel);

        LootTable bossBonusTbl = overWorld.getServer().getLootTables().get(config.getScavengerCrate());
        NonNullList<ItemStack> recordLoot = NonNullList.create();
        LootContext.Builder builder = (new LootContext.Builder(overWorld)).withRandom(overWorld.random);
        while (recordLoot.size() < 27) {
            recordLoot.addAll(bossBonusTbl.getRandomItems(builder.create(LootParameterSets.EMPTY)));
        }
        Collections.shuffle((List<?>) recordLoot);
        while (recordLoot.size() > 27) {
            recordLoot.remove(recordLoot.size() - 1);
        }
        return recordLoot;
    }

    public static void onStartup(FMLServerStartedEvent event) {
        get(event.getServer()).generateRecordRewards(event.getServer().overworld());
    }


    public static PlayerVaultStatsData get(ServerWorld world) {
        return get(world.getServer());
    }

    public static PlayerVaultStatsData get(MinecraftServer srv) {
        return (PlayerVaultStatsData) srv.overworld().getDataStorage().computeIfAbsent(PlayerVaultStatsData::new, "the_vault_PlayerVaultLevels");
    }


    public void load(CompoundNBT nbt) {
        ListNBT playerList = nbt.getList("PlayerEntries", 8);
        ListNBT statEntries = nbt.getList("StatEntries", 10);
        ListNBT weeklyRecords = nbt.getList("WeeklyRecords", 10);
        ListNBT weeklyGenerated = nbt.getList("WeeklyRewards", 10);

        if (playerList.size() != statEntries.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }
        int i;
        for (i = 0; i < playerList.size(); i++) {
            UUID playerUUID = UUID.fromString(playerList.getString(i));
            getVaultStats(playerUUID).deserializeNBT(statEntries.getCompound(i));
        }

        for (i = 0; i < weeklyRecords.size(); i++) {
            CompoundNBT tag = weeklyRecords.getCompound(i);
            WeekKey key = WeekKey.deserialize(tag.getCompound("weekKey"));

            List<PlayerRecordEntry> recordEntries = new ArrayList<>();
            ListNBT entries = tag.getList("entries", 10);
            for (int j = 0; j < entries.size(); j++) {
                recordEntries.add(PlayerRecordEntry.deserialize(entries.getCompound(j)));
            }
            this.weeklyVaultRecords.put(key, recordEntries);
        }

        for (i = 0; i < weeklyGenerated.size(); i++) {
            WeekKey key = WeekKey.deserialize(weeklyGenerated.getCompound(i));
            this.grantedRewards.add(key);
        }


        if (nbt.contains("RecordEntries", 9)) {
            ListNBT recordList = nbt.getList("RecordEntries", 10);
            for (int j = 0; j < recordList.size(); j++) {
                updateFastestVaultTime(PlayerRecordEntry.deserialize(recordList.getCompound(j)));
            }
        }
    }


    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT playerList = new ListNBT();
        ListNBT statsList = new ListNBT();
        ListNBT recordWeekList = new ListNBT();
        ListNBT rewardsWeekly = new ListNBT();

        this.playerMap.forEach((uuid, stats) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));

            statsList.add(stats.serializeNBT());
        });
        nbt.put("PlayerEntries", (INBT) playerList);
        nbt.put("StatEntries", (INBT) statsList);

        this.weeklyVaultRecords.forEach((weekKey, entries) -> {
            CompoundNBT tag = new CompoundNBT();

            tag.put("weekKey", (INBT) weekKey.serialize());

            ListNBT recordEntries = new ListNBT();
            entries.forEach(());
            tag.put("entries", (INBT) recordEntries);
            recordWeekList.add(tag);
        });
        nbt.put("WeeklyRecords", (INBT) recordWeekList);

        this.grantedRewards.forEach(week -> rewardsWeekly.add(week.serialize()));
        nbt.put("WeeklyRewards", (INBT) rewardsWeekly);

        return nbt;
    }

    public static class PlayerRecordEntry {
        private static final PlayerRecordEntry DEFAULT = new PlayerRecordEntry(Util.NIL_UUID, "", 6000);

        private final UUID playerUUID;
        private final String playerName;
        private final int tickCount;

        public PlayerRecordEntry(UUID playerUUID, String playerName, int tickCount) {
            this.playerUUID = playerUUID;
            this.playerName = playerName;
            this.tickCount = tickCount;
        }

        public UUID getPlayerUUID() {
            return this.playerUUID;
        }

        public String getPlayerName() {
            return this.playerName;
        }

        public int getTickCount() {
            return this.tickCount;
        }

        public CompoundNBT serialize() {
            CompoundNBT tag = new CompoundNBT();
            tag.putUUID("playerUUID", this.playerUUID);
            tag.putString("playerName", this.playerName);
            tag.putInt("tickCount", this.tickCount);
            return tag;
        }

        public static PlayerRecordEntry deserialize(CompoundNBT tag) {
            return new PlayerRecordEntry(tag.getUUID("playerUUID"), tag.getString("playerName"), tag.getInt("tickCount"));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\PlayerVaultStatsData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */