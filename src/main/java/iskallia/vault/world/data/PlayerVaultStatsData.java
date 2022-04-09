// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.data;

import iskallia.vault.block.item.TrophyStatueBlockItem;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.util.WeekKey;
import iskallia.vault.world.data.generated.ChallengeCrystalArchive;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
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
import java.util.function.Function;
import java.util.function.Supplier;

public class PlayerVaultStatsData extends WorldSavedData
{
    protected static final String DATA_NAME = "the_vault_PlayerVaultLevels";
    private final Map<UUID, PlayerVaultStats> playerMap;
    private final Map<WeekKey, List<PlayerRecordEntry>> weeklyVaultRecords;
    private final Set<WeekKey> grantedRewards;
    
    public PlayerVaultStatsData() {
        super("the_vault_PlayerVaultLevels");
        this.playerMap = new HashMap<UUID, PlayerVaultStats>();
        this.weeklyVaultRecords = new HashMap<WeekKey, List<PlayerRecordEntry>>();
        this.grantedRewards = new HashSet<WeekKey>();
    }
    
    public PlayerVaultStatsData(final String name) {
        super(name);
        this.playerMap = new HashMap<UUID, PlayerVaultStats>();
        this.weeklyVaultRecords = new HashMap<WeekKey, List<PlayerRecordEntry>>();
        this.grantedRewards = new HashSet<WeekKey>();
    }
    
    public PlayerVaultStats getVaultStats(final PlayerEntity player) {
        return this.getVaultStats(player.getUUID());
    }
    
    public PlayerVaultStats getVaultStats(final UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, PlayerVaultStats::new);
    }
    
    public PlayerVaultStatsData setVaultLevel(final ServerPlayerEntity player, final int level) {
        this.getVaultStats((PlayerEntity)player).setVaultLevel(player.getServer(), level);
        this.setDirty();
        return this;
    }
    
    public PlayerVaultStatsData addVaultExp(final ServerPlayerEntity player, final int exp) {
        this.getVaultStats((PlayerEntity)player).addVaultExp(player.getServer(), exp);
        this.setDirty();
        return this;
    }
    
    public PlayerVaultStatsData spendSkillPts(final ServerPlayerEntity player, final int amount) {
        this.getVaultStats((PlayerEntity)player).spendSkillPoints(player.getServer(), amount);
        this.setDirty();
        return this;
    }
    
    public PlayerVaultStatsData spendKnowledgePts(final ServerPlayerEntity player, final int amount) {
        this.getVaultStats((PlayerEntity)player).spendKnowledgePoints(player.getServer(), amount);
        this.setDirty();
        return this;
    }
    
    public PlayerVaultStatsData addSkillPoint(final ServerPlayerEntity player, final int amount) {
        this.getVaultStats((PlayerEntity)player).addSkillPoints(amount).sync(player.getLevel().getServer());
        this.setDirty();
        return this;
    }
    
    public PlayerVaultStatsData addSkillPointNoSync(final UUID playerId, final int amount) {
        this.getVaultStats(playerId).addSkillPoints(amount);
        this.setDirty();
        return this;
    }
    
    public PlayerVaultStatsData addKnowledgePoints(final ServerPlayerEntity player, final int amount) {
        this.getVaultStats((PlayerEntity)player).addKnowledgePoints(amount).sync(player.getLevel().getServer());
        this.setDirty();
        return this;
    }
    
    public PlayerVaultStatsData reset(final ServerPlayerEntity player) {
        this.getVaultStats((PlayerEntity)player).reset(player.getServer());
        this.setDirty();
        return this;
    }
    
    @Nonnull
    public PlayerRecordEntry getFastestVaultTime() {
        return this.getFastestVaultTime(WeekKey.current());
    }
    
    @Nonnull
    public PlayerRecordEntry getFastestVaultTime(final WeekKey week) {
        return this.weeklyVaultRecords.computeIfAbsent(week, key -> new ArrayList<>()).stream().min(Comparator.comparing(PlayerRecordEntry::getTickCount)).orElse(PlayerRecordEntry.DEFAULT);
    }
    
    public void updateFastestVaultTime(final PlayerEntity player, final int timeTicks) {
        this.updateFastestVaultTime(new PlayerRecordEntry(player.getUUID(), player.getName().getString(), timeTicks));
    }
    
    private void updateFastestVaultTime(final PlayerRecordEntry entry) {
        this.weeklyVaultRecords.computeIfAbsent(WeekKey.current(), key -> new ArrayList()).add(entry);
        this.setDirty();
    }
    
    public boolean setRewardGranted(final WeekKey weekKey) {
        if (this.grantedRewards.add(weekKey)) {
            this.setDirty();
            return true;
        }
        return false;
    }
    
    public boolean hasGeneratedReward(final WeekKey weekKey) {
        return this.grantedRewards.contains(weekKey);
    }
    
    public void generateRecordRewards(final ServerWorld overWorld) {
        final WeekKey week = WeekKey.previous();
        if (this.hasGeneratedReward(week)) {
            return;
        }
        final PlayerRecordEntry previousRecord = this.getFastestVaultTime(week);
        if (previousRecord == PlayerRecordEntry.DEFAULT) {
            return;
        }
        final PlayerVaultStats stats = this.getVaultStats(previousRecord.getPlayerUUID());
        final int vLevel = stats.getVaultLevel();
        final NonNullList<ItemStack> loot = generateTrophyBox(overWorld, vLevel);
        loot.set(4, TrophyStatueBlockItem.getTrophy(overWorld, week));
        loot.set(13, new ItemStack((IItemProvider)ModItems.UNIDENTIFIED_ARTIFACT));
        loot.set(22, ChallengeCrystalArchive.getRandom());
        final ItemStack box = new ItemStack((IItemProvider)Items.WHITE_SHULKER_BOX);
        box.getOrCreateTag().put("BlockEntityTag", (INBT)new CompoundNBT());
        ItemStackHelper.saveAllItems(box.getOrCreateTag().getCompound("BlockEntityTag"), (NonNullList)loot);
        ScheduledItemDropData.get(overWorld).addDrop(previousRecord.getPlayerUUID(), box);
        this.grantedRewards.add(week);
        this.setDirty();
    }
    
    private static NonNullList<ItemStack> generateTrophyBox(final ServerWorld overWorld, final int vaultLevel) {
        final LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(vaultLevel);
        final LootTable bossBonusTbl = overWorld.getServer().getLootTables().get(config.getScavengerCrate());
        final NonNullList<ItemStack> recordLoot = NonNullList.create();
        final LootContext.Builder builder = new LootContext.Builder(overWorld).withRandom(overWorld.random);
        while (recordLoot.size() < 27) {
            recordLoot.addAll((Collection)bossBonusTbl.getRandomItems(builder.create(LootParameterSets.EMPTY)));
        }
        Collections.shuffle((List<?>)recordLoot);
        while (recordLoot.size() > 27) {
            recordLoot.remove(recordLoot.size() - 1);
        }
        return recordLoot;
    }
    
    public static void onStartup(final FMLServerStartedEvent event) {
        get(event.getServer()).generateRecordRewards(event.getServer().overworld());
    }
    
    public static PlayerVaultStatsData get(final ServerWorld world) {
        return get(world.getServer());
    }
    
    public static PlayerVaultStatsData get(final MinecraftServer srv) {
        return (PlayerVaultStatsData)srv.overworld().getDataStorage().computeIfAbsent((Supplier)PlayerVaultStatsData::new, "the_vault_PlayerVaultLevels");
    }
    
    public void load(final CompoundNBT nbt) {
        final ListNBT playerList = nbt.getList("PlayerEntries", 8);
        final ListNBT statEntries = nbt.getList("StatEntries", 10);
        final ListNBT weeklyRecords = nbt.getList("WeeklyRecords", 10);
        final ListNBT weeklyGenerated = nbt.getList("WeeklyRewards", 10);
        if (playerList.size() != statEntries.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }
        for (int i = 0; i < playerList.size(); ++i) {
            final UUID playerUUID = UUID.fromString(playerList.getString(i));
            this.getVaultStats(playerUUID).deserializeNBT(statEntries.getCompound(i));
        }
        for (int i = 0; i < weeklyRecords.size(); ++i) {
            final CompoundNBT tag = weeklyRecords.getCompound(i);
            final WeekKey key = WeekKey.deserialize(tag.getCompound("weekKey"));
            final List<PlayerRecordEntry> recordEntries = new ArrayList<PlayerRecordEntry>();
            final ListNBT entries = tag.getList("entries", 10);
            for (int j = 0; j < entries.size(); ++j) {
                recordEntries.add(PlayerRecordEntry.deserialize(entries.getCompound(j)));
            }
            this.weeklyVaultRecords.put(key, recordEntries);
        }
        for (int i = 0; i < weeklyGenerated.size(); ++i) {
            final WeekKey key2 = WeekKey.deserialize(weeklyGenerated.getCompound(i));
            this.grantedRewards.add(key2);
        }
        if (nbt.contains("RecordEntries", 9)) {
            final ListNBT recordList = nbt.getList("RecordEntries", 10);
            for (int k = 0; k < recordList.size(); ++k) {
                this.updateFastestVaultTime(PlayerRecordEntry.deserialize(recordList.getCompound(k)));
            }
        }
    }
    
    public CompoundNBT save(final CompoundNBT nbt) {
        final ListNBT playerList = new ListNBT();
        final ListNBT statsList = new ListNBT();
        final ListNBT recordWeekList = new ListNBT();
        final ListNBT rewardsWeekly = new ListNBT();
        this.playerMap.forEach((uuid, stats) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));
            statsList.add(stats.serializeNBT());
            return;
        });
        nbt.put("PlayerEntries", (INBT)playerList);
        nbt.put("StatEntries", (INBT)statsList);
        this.weeklyVaultRecords.forEach((weekKey, entries) -> {
            final CompoundNBT tag = new CompoundNBT();
            tag.put("weekKey", (INBT)weekKey.serialize());
            final ListNBT recordEntries = new ListNBT();
            entries.forEach(entry -> recordEntries.add(entry.serialize()));
            tag.put("entries", (INBT)recordEntries);
            recordWeekList.add(tag);
            return;
        });
        nbt.put("WeeklyRecords", (INBT)recordWeekList);
        this.grantedRewards.forEach(week -> rewardsWeekly.add(week.serialize()));
        nbt.put("WeeklyRewards", (INBT)rewardsWeekly);
        return nbt;
    }
    
    public static class PlayerRecordEntry
    {
        private static final PlayerRecordEntry DEFAULT;
        private final UUID playerUUID;
        private final String playerName;
        private final int tickCount;
        
        public PlayerRecordEntry(final UUID playerUUID, final String playerName, final int tickCount) {
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
            final CompoundNBT tag = new CompoundNBT();
            tag.putUUID("playerUUID", this.playerUUID);
            tag.putString("playerName", this.playerName);
            tag.putInt("tickCount", this.tickCount);
            return tag;
        }
        
        public static PlayerRecordEntry deserialize(final CompoundNBT tag) {
            return new PlayerRecordEntry(tag.getUUID("playerUUID"), tag.getString("playerName"), tag.getInt("tickCount"));
        }
        
        static {
            DEFAULT = new PlayerRecordEntry(Util.NIL_UUID, "", 6000);
        }
    }
}
