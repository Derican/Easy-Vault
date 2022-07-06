package iskallia.vault.util.calc;

import com.google.common.collect.Lists;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.item.ItemUnidentifiedArtifact;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.network.message.PlayerStatisticsMessage;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.data.PlayerStatsData;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.behaviour.VaultBehaviour;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.objective.raid.RaidChallengeObjective;
import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.player.VaultRunner;
import iskallia.vault.world.vault.player.VaultSpectator;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class PlayerStatisticsCollector {
    private static final Supplier<List<Attribute>> displayedAttributes;

    @SubscribeEvent
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.player;
        if (sPlayer.tickCount % 20 != 0) {
            return;
        }
        final TalentTree talents = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);
        final List<AttributeSnapshot> snapshots = new ArrayList<AttributeSnapshot>();
        final List<Attribute> collectingAttributes = PlayerStatisticsCollector.displayedAttributes.get();
        for (final Attribute attribute : collectingAttributes) {
            double value = sPlayer.getAttribute(attribute).getValue();
            if (attribute == Attributes.MOVEMENT_SPEED) {
                value *= 10.0;
            }
            if (attribute == Attributes.KNOCKBACK_RESISTANCE) {
                value *= 100.0;
            }
            snapshots.add(new AttributeSnapshot(attribute.getDescriptionId(), value, attribute == Attributes.KNOCKBACK_RESISTANCE));
        }
        final float parry = ParryHelper.getPlayerParryChanceUnlimited(sPlayer) * 100.0f;
        snapshots.add(collectingAttributes.indexOf(Attributes.KNOCKBACK_RESISTANCE), new AttributeSnapshot("stat.the_vault.parry", parry, true).setLimit(AttributeLimitHelper.getParryLimit((PlayerEntity) sPlayer) * 100.0f));
        final float resistance = ResistanceHelper.getPlayerResistancePercentUnlimited(sPlayer) * 100.0f;
        snapshots.add(collectingAttributes.indexOf(Attributes.KNOCKBACK_RESISTANCE), new AttributeSnapshot("stat.the_vault.resistance", resistance, true).setLimit(AttributeLimitHelper.getResistanceLimit((PlayerEntity) sPlayer) * 100.0f));
        if (talents.hasLearnedNode(ModConfigs.TALENTS.COMMANDER)) {
            final float summonEternalCooldown = CooldownHelper.getCooldownMultiplierUnlimited(sPlayer, ModConfigs.ABILITIES.SUMMON_ETERNAL) * 100.0f;
            snapshots.add(collectingAttributes.indexOf(Attributes.KNOCKBACK_RESISTANCE), new AttributeSnapshot("stat.the_vault.cooldown_summoneternal", "stat.the_vault.cooldown", summonEternalCooldown, true).setLimit(AttributeLimitHelper.getCooldownReductionLimit((PlayerEntity) sPlayer) * 100.0f));
        }
        final float cooldown = CooldownHelper.getCooldownMultiplierUnlimited(sPlayer, null) * 100.0f;
        snapshots.add(collectingAttributes.indexOf(Attributes.KNOCKBACK_RESISTANCE), new AttributeSnapshot("stat.the_vault.cooldown", cooldown, true).setLimit(AttributeLimitHelper.getCooldownReductionLimit((PlayerEntity) sPlayer) * 100.0f));
        snapshots.add(new AttributeSnapshot("stat.the_vault.chest_rarity", ChestRarityHelper.getIncreasedChestRarity(sPlayer) * 100.0f, true));
        snapshots.add(new AttributeSnapshot("stat.the_vault.thorns_chance", ThornsHelper.getPlayerThornsChance(sPlayer) * 100.0f, true));
        snapshots.add(new AttributeSnapshot("stat.the_vault.thorns_damage", ThornsHelper.getPlayerThornsDamage(sPlayer) * 100.0f, true));
        snapshots.add(new AttributeSnapshot("stat.the_vault.fatal_strike_chance", FatalStrikeHelper.getPlayerFatalStrikeChance(sPlayer) * 100.0f, true));
        snapshots.add(new AttributeSnapshot("stat.the_vault.fatal_strike_damage", FatalStrikeHelper.getPlayerFatalStrikeDamage(sPlayer) * 100.0f, true));
        final CompoundNBT vaultStats = new CompoundNBT();
        final PlayerVaultStatsData vaultStatsData = PlayerVaultStatsData.get(sPlayer.getLevel());
        final PlayerStatsData.Stats vaultPlayerStats = PlayerStatsData.get(sPlayer.getLevel()).get((PlayerEntity) sPlayer);
        final PlayerFavourData favourData = PlayerFavourData.get(sPlayer.getLevel());
        final UUID playerUUID = sPlayer.getUUID();
        final PlayerVaultStats stats = vaultStatsData.getVaultStats(playerUUID);
        final VaultRunsSnapshot vaultRunsSnapshot = VaultRunsSnapshot.ofPlayer(sPlayer);
        vaultStats.put("fastestVault", (INBT) vaultStatsData.getFastestVaultTime().serialize());
        vaultStats.putInt("powerLevel", stats.getTotalSpentSkillPoints() + stats.getUnspentSkillPts());
        vaultStats.putInt("knowledgeLevel", stats.getTotalSpentKnowledgePoints() + stats.getUnspentKnowledgePts());
        vaultStats.putInt("crystalsCrafted", vaultPlayerStats.getCrystals().size());
        vaultStats.putInt("vaultArtifacts", vaultRunsSnapshot.artifacts);
        vaultStats.putInt("vaultTotal", vaultRunsSnapshot.vaultRuns);
        vaultStats.putInt("vaultDeaths", vaultRunsSnapshot.deaths);
        vaultStats.putInt("vaultBails", vaultRunsSnapshot.bails);
        vaultStats.putInt("vaultBossKills", vaultRunsSnapshot.bossKills);
        vaultStats.putInt("vaultRaids", vaultRunsSnapshot.raidsCompleted);
        final CompoundNBT favourStats = new CompoundNBT();
        for (final PlayerFavourData.VaultGodType type : PlayerFavourData.VaultGodType.values()) {
            favourStats.put(type.name(), (INBT) IntNBT.valueOf(favourData.getFavour(playerUUID, type)));
        }
        final CompoundNBT serialized = new CompoundNBT();
        final ListNBT snapshotList = new ListNBT();
        snapshots.forEach(snapshot -> snapshotList.add(snapshot.serialize()));
        serialized.put("attributes", (INBT) snapshotList);
        serialized.put("vaultStats", (INBT) vaultStats);
        serialized.put("favourStats", (INBT) favourStats);
        final PlayerStatisticsMessage pkt = new PlayerStatisticsMessage(serialized);
        ModNetwork.CHANNEL.sendTo(pkt, sPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static int getFinishedRaids(final MinecraftServer srv, final UUID playerId) {
        if (!ModConfigs.RAID_EVENT_CONFIG.isEnabled()) {
            return -1;
        }
        final PlayerStatsData.Stats stats = PlayerStatsData.get(srv).get(playerId);
        if (stats.hasFinishedRaidReward()) {
            return -1;
        }
        int completedRaids = 0;
        for (final VaultRaid recordedRaid : stats.getVaults()) {
            for (final VaultObjective objective : recordedRaid.getAllObjectives()) {
                if (objective instanceof RaidChallengeObjective) {
                    completedRaids += ((RaidChallengeObjective) objective).getCompletedRaids();
                }
            }
        }
        return completedRaids;
    }

    static {
        displayedAttributes = (() -> Lists.newArrayList(new Attribute[]{Attributes.MAX_HEALTH, Attributes.ATTACK_DAMAGE, Attributes.ATTACK_SPEED, Attributes.ARMOR, Attributes.ARMOR_TOUGHNESS, Attributes.KNOCKBACK_RESISTANCE, Attributes.LUCK, (Attribute) ForgeMod.REACH_DISTANCE.get(), Attributes.MOVEMENT_SPEED}));
    }

    public static class VaultRunsSnapshot {
        public int vaultRuns;
        public int deaths;
        public int bails;
        public int bossKills;
        public int artifacts;
        public int raidsCompleted;

        public static VaultRunsSnapshot ofPlayer(final ServerPlayerEntity sPlayer) {
            final PlayerStatsData.Stats vaultPlayerStats = PlayerStatsData.get(sPlayer.getLevel()).get((PlayerEntity) sPlayer);
            final VaultRunsSnapshot snapshot = new VaultRunsSnapshot();
            snapshot.vaultRuns = vaultPlayerStats.getVaults().size();
            for (final VaultRaid recordedRaid : vaultPlayerStats.getVaults()) {
                boolean completedAll = true;
                for (final VaultObjective objective : recordedRaid.getAllObjectives()) {
                    for (final VaultObjective.Crate crate : objective.getCrates()) {
                        for (final ItemStack stack : crate.getContents()) {
                            if (stack.getItem() instanceof ItemUnidentifiedArtifact) {
                                final VaultRunsSnapshot vaultRunsSnapshot = snapshot;
                                ++vaultRunsSnapshot.artifacts;
                            }
                        }
                    }
                    if (objective instanceof RaidChallengeObjective) {
                        final VaultRunsSnapshot vaultRunsSnapshot2 = snapshot;
                        vaultRunsSnapshot2.raidsCompleted += ((RaidChallengeObjective) objective).getCompletedRaids();
                    }
                    if (!objective.isCompleted()) {
                        completedAll = false;
                        break;
                    }
                }
                if (completedAll) {
                    final VaultRunsSnapshot vaultRunsSnapshot3 = snapshot;
                    ++vaultRunsSnapshot3.bossKills;
                } else {
                    final CrystalData data = recordedRaid.getProperties().getBaseOrDefault(VaultRaid.CRYSTAL_DATA, CrystalData.EMPTY);
                    final CrystalData.Type vaultType = data.getType();
                    boolean isOldClassic = false;
                    if (recordedRaid.getPlayers().size() == 1) {
                        final VaultPlayer player = recordedRaid.getPlayers().get(0);
                        final VaultBehaviour behaviour = player.getBehaviours().get(0);
                        final ResourceLocation id = behaviour.getTask().getId();
                        if (VaultRaid.RUNNER_TO_SPECTATOR.getId().equals(id)) {
                            isOldClassic = true;
                        }
                    }
                    if (isOldClassic || vaultType == CrystalData.Type.TROVE || vaultType == CrystalData.Type.RAFFLE) {

                        for (final VaultPlayer vPlayer : recordedRaid.getPlayers()) {
                            if (vPlayer.hasExited()) {
                                if (vPlayer instanceof VaultSpectator) {
                                    final VaultRunsSnapshot vaultRunsSnapshot4 = snapshot;
                                    ++vaultRunsSnapshot4.deaths;
                                    break;
                                }
                                final VaultRunsSnapshot vaultRunsSnapshot5 = snapshot;
                                ++vaultRunsSnapshot5.bails;
                                break;
                            }
                        }
                    } else {
                        boolean done = true;
                        boolean areAllSpectators = true;
                        for (final VaultPlayer vPlayer2 : recordedRaid.getPlayers()) {
                            if (!vPlayer2.hasExited()) {
                                done = false;
                            }
                            if (vPlayer2 instanceof VaultRunner) {
                                areAllSpectators = false;
                            }
                        }
                        if (!done) {
                            continue;
                        }
                        if (areAllSpectators) {
                            final VaultRunsSnapshot vaultRunsSnapshot6 = snapshot;
                            ++vaultRunsSnapshot6.bails;
                        } else {
                            final VaultRunsSnapshot vaultRunsSnapshot7 = snapshot;
                            ++vaultRunsSnapshot7.deaths;
                        }
                    }
                }
            }
            return snapshot;
        }
    }

    public static class AttributeSnapshot {
        private final String unlocAttributeName;
        private final String parentAttributeName;
        private final double value;
        private final boolean isPercentage;
        private double limit;

        public AttributeSnapshot(final String unlocAttributeName, final double value, final boolean isPercentage) {
            this(unlocAttributeName, null, value, isPercentage);
        }

        public AttributeSnapshot(final String unlocAttributeName, final String parentAttributeName, final double value, final boolean isPercentage) {
            this.limit = -1.0;
            this.unlocAttributeName = unlocAttributeName;
            this.parentAttributeName = parentAttributeName;
            this.value = value;
            this.isPercentage = isPercentage;
        }

        private AttributeSnapshot setLimit(final double limit) {
            this.limit = limit;
            return this;
        }

        public String getAttributeName() {
            return this.unlocAttributeName;
        }

        public String getParentAttributeName() {
            return (this.parentAttributeName != null) ? this.parentAttributeName : this.getAttributeName();
        }

        public double getValue() {
            return this.value;
        }

        public boolean isPercentage() {
            return this.isPercentage;
        }

        public boolean hasLimit() {
            return this.limit != -1.0;
        }

        public double getLimit() {
            return this.limit;
        }

        public boolean hasHitLimit() {
            return this.hasLimit() && this.getValue() > this.getLimit();
        }

        public CompoundNBT serialize() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putString("key", this.getAttributeName());
            nbt.putString("parent", this.getParentAttributeName());
            nbt.putDouble("value", this.getValue());
            nbt.putBoolean("isPercentage", this.isPercentage());
            nbt.putDouble("limit", this.getLimit());
            return nbt;
        }

        public static AttributeSnapshot deserialize(final CompoundNBT nbt) {
            return new AttributeSnapshot(nbt.getString("key"), nbt.getString("parent"), nbt.getDouble("value"), nbt.getBoolean("isPercentage")).setLimit(nbt.getDouble("limit"));
        }
    }
}
