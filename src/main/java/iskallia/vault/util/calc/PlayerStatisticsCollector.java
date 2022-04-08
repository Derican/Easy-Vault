package iskallia.vault.util.calc;

import com.google.common.collect.Lists;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.network.message.PlayerStatisticsMessage;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.data.PlayerStatsData;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.objective.raid.RaidChallengeObjective;
import iskallia.vault.world.vault.player.VaultPlayer;
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
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;


@EventBusSubscriber
public class PlayerStatisticsCollector {
    private static final Supplier<List<Attribute>> displayedAttributes = () -> Lists.newArrayList((Object[]) new Attribute[]{Attributes.MAX_HEALTH, Attributes.ATTACK_DAMAGE, Attributes.ATTACK_SPEED, Attributes.ARMOR, Attributes.ARMOR_TOUGHNESS, Attributes.KNOCKBACK_RESISTANCE, Attributes.LUCK, (Attribute) ForgeMod.REACH_DISTANCE.get(), Attributes.MOVEMENT_SPEED});

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayerEntity)) {
            return;
        }

        ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.player;
        if (sPlayer.tickCount % 20 != 0) {
            return;
        }

        TalentTree talents = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);


        List<AttributeSnapshot> snapshots = new ArrayList<>();
        List<Attribute> collectingAttributes = displayedAttributes.get();
        for (Attribute attribute : collectingAttributes) {
            double value = sPlayer.getAttribute(attribute).getValue();
            if (attribute == Attributes.MOVEMENT_SPEED) {
                value *= 10.0D;
            }
            if (attribute == Attributes.KNOCKBACK_RESISTANCE) {
                value *= 100.0D;
            }
            snapshots.add(new AttributeSnapshot(attribute.getDescriptionId(), value, (attribute == Attributes.KNOCKBACK_RESISTANCE)));
        }
        float parry = ParryHelper.getPlayerParryChanceUnlimited(sPlayer) * 100.0F;
        snapshots.add(collectingAttributes.indexOf(Attributes.KNOCKBACK_RESISTANCE), (new AttributeSnapshot("stat.the_vault.parry", parry, true))

                .setLimit((AttributeLimitHelper.getParryLimit((PlayerEntity) sPlayer) * 100.0F)));
        float resistance = ResistanceHelper.getPlayerResistancePercentUnlimited(sPlayer) * 100.0F;
        snapshots.add(collectingAttributes.indexOf(Attributes.KNOCKBACK_RESISTANCE), (new AttributeSnapshot("stat.the_vault.resistance", resistance, true))

                .setLimit((AttributeLimitHelper.getResistanceLimit((PlayerEntity) sPlayer) * 100.0F)));

        if (talents.hasLearnedNode((TalentGroup) ModConfigs.TALENTS.COMMANDER)) {
            float summonEternalCooldown = CooldownHelper.getCooldownMultiplierUnlimited(sPlayer, (AbilityGroup<?, ?>) ModConfigs.ABILITIES.SUMMON_ETERNAL) * 100.0F;
            snapshots.add(collectingAttributes.indexOf(Attributes.KNOCKBACK_RESISTANCE), (new AttributeSnapshot("stat.the_vault.cooldown_summoneternal", "stat.the_vault.cooldown", summonEternalCooldown, true))

                    .setLimit((AttributeLimitHelper.getCooldownReductionLimit((PlayerEntity) sPlayer) * 100.0F)));
        }
        float cooldown = CooldownHelper.getCooldownMultiplierUnlimited(sPlayer, null) * 100.0F;
        snapshots.add(collectingAttributes.indexOf(Attributes.KNOCKBACK_RESISTANCE), (new AttributeSnapshot("stat.the_vault.cooldown", cooldown, true))

                .setLimit((AttributeLimitHelper.getCooldownReductionLimit((PlayerEntity) sPlayer) * 100.0F)));

        snapshots.add(new AttributeSnapshot("stat.the_vault.chest_rarity", (
                ChestRarityHelper.getIncreasedChestRarity(sPlayer) * 100.0F), true));
        snapshots.add(new AttributeSnapshot("stat.the_vault.thorns_chance", (
                ThornsHelper.getPlayerThornsChance(sPlayer) * 100.0F), true));
        snapshots.add(new AttributeSnapshot("stat.the_vault.thorns_damage", (
                ThornsHelper.getPlayerThornsDamage(sPlayer) * 100.0F), true));
        snapshots.add(new AttributeSnapshot("stat.the_vault.fatal_strike_chance", (
                FatalStrikeHelper.getPlayerFatalStrikeChance(sPlayer) * 100.0F), true));
        snapshots.add(new AttributeSnapshot("stat.the_vault.fatal_strike_damage", (
                FatalStrikeHelper.getPlayerFatalStrikeDamage(sPlayer) * 100.0F), true));

        CompoundNBT vaultStats = new CompoundNBT();
        PlayerVaultStatsData vaultStatsData = PlayerVaultStatsData.get(sPlayer.getLevel());
        PlayerStatsData.Stats vaultPlayerStats = PlayerStatsData.get(sPlayer.getLevel()).get((PlayerEntity) sPlayer);
        PlayerFavourData favourData = PlayerFavourData.get(sPlayer.getLevel());

        UUID playerUUID = sPlayer.getUUID();
        PlayerVaultStats stats = vaultStatsData.getVaultStats(playerUUID);

        VaultRunsSnapshot vaultRunsSnapshot = VaultRunsSnapshot.ofPlayer(sPlayer);

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

        CompoundNBT favourStats = new CompoundNBT();
        for (PlayerFavourData.VaultGodType type : PlayerFavourData.VaultGodType.values()) {
            favourStats.put(type.name(), (INBT) IntNBT.valueOf(favourData.getFavour(playerUUID, type)));
        }


        CompoundNBT serialized = new CompoundNBT();

        ListNBT snapshotList = new ListNBT();
        snapshots.forEach(snapshot -> snapshotList.add(snapshot.serialize()));
        serialized.put("attributes", (INBT) snapshotList);
        serialized.put("vaultStats", (INBT) vaultStats);
        serialized.put("favourStats", (INBT) favourStats);

        PlayerStatisticsMessage pkt = new PlayerStatisticsMessage(serialized);
        ModNetwork.CHANNEL.sendTo(pkt, sPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static class VaultRunsSnapshot {
        public int vaultRuns;
        public int deaths;
        public int bails;

        public static VaultRunsSnapshot ofPlayer(ServerPlayerEntity sPlayer) {
            PlayerStatsData.Stats vaultPlayerStats = PlayerStatsData.get(sPlayer.getLevel()).get((PlayerEntity) sPlayer);
            VaultRunsSnapshot snapshot = new VaultRunsSnapshot();

            snapshot.vaultRuns = vaultPlayerStats.getVaults().size();

            for (VaultRaid recordedRaid : vaultPlayerStats.getVaults()) {
                boolean completedAll = true;
                for (VaultObjective objective : recordedRaid.getAllObjectives()) {
                    for (VaultObjective.Crate crate : objective.getCrates()) {
                        for (ItemStack stack : crate.getContents()) {
                            if (stack.getItem() instanceof iskallia.vault.item.ItemUnidentifiedArtifact) {
                                snapshot.artifacts++;
                            }
                        }
                    }
                    if (objective instanceof RaidChallengeObjective) {
                        snapshot.raidsCompleted += ((RaidChallengeObjective) objective).getCompletedRaids();
                    }

                    if (!objective.isCompleted()) {
                        completedAll = false;
                        break;
                    }
                }
                if (completedAll) {
                    snapshot.bossKills++;

                    continue;
                }
                CrystalData data = (CrystalData) recordedRaid.getProperties().getBaseOrDefault(VaultRaid.CRYSTAL_DATA, CrystalData.EMPTY);
                CrystalData.Type vaultType = data.getType();

                if (vaultType != CrystalData.Type.COOP) {
                    for (VaultPlayer vPlayer : recordedRaid.getPlayers()) {
                        if (vPlayer.hasExited()) {
                            if (vPlayer instanceof iskallia.vault.world.vault.player.VaultSpectator) {
                                snapshot.deaths++;
                                continue;
                            }
                            snapshot.bails++;
                        }
                    }

                    continue;
                }
                boolean done = true;
                boolean areAllSpectators = true;
                for (VaultPlayer vPlayer : recordedRaid.getPlayers()) {
                    if (!vPlayer.hasExited()) {
                        done = false;
                    }
                    if (vPlayer instanceof iskallia.vault.world.vault.player.VaultRunner) {
                        areAllSpectators = false;
                    }
                }
                if (done) {
                    if (areAllSpectators) {
                        snapshot.bails++;
                        continue;
                    }
                    snapshot.deaths++;
                }
            }


            return snapshot;
        }

        public int bossKills;
        public int artifacts;
        public int raidsCompleted;
    }

    public static int getFinishedRaids(MinecraftServer srv, UUID playerId) {
        if (!ModConfigs.RAID_EVENT_CONFIG.isEnabled()) {
            return -1;
        }
        PlayerStatsData.Stats stats = PlayerStatsData.get(srv).get(playerId);
        if (stats.hasFinishedRaidReward()) {
            return -1;
        }

        int completedRaids = 0;
        for (VaultRaid recordedRaid : stats.getVaults()) {
            for (VaultObjective objective : recordedRaid.getAllObjectives()) {
                if (objective instanceof RaidChallengeObjective) {
                    completedRaids += ((RaidChallengeObjective) objective).getCompletedRaids();
                }
            }
        }
        return completedRaids;
    }


    public static class AttributeSnapshot {
        private final String unlocAttributeName;
        private final String parentAttributeName;
        private final double value;
        private final boolean isPercentage;
        private double limit = -1.0D;

        public AttributeSnapshot(String unlocAttributeName, double value, boolean isPercentage) {
            this(unlocAttributeName, null, value, isPercentage);
        }

        public AttributeSnapshot(String unlocAttributeName, String parentAttributeName, double value, boolean isPercentage) {
            this.unlocAttributeName = unlocAttributeName;
            this.parentAttributeName = parentAttributeName;
            this.value = value;
            this.isPercentage = isPercentage;
        }

        private AttributeSnapshot setLimit(double limit) {
            this.limit = limit;
            return this;
        }

        public String getAttributeName() {
            return this.unlocAttributeName;
        }

        public String getParentAttributeName() {
            return (this.parentAttributeName != null) ? this.parentAttributeName : getAttributeName();
        }

        public double getValue() {
            return this.value;
        }

        public boolean isPercentage() {
            return this.isPercentage;
        }

        public boolean hasLimit() {
            return (this.limit != -1.0D);
        }

        public double getLimit() {
            return this.limit;
        }

        public boolean hasHitLimit() {
            return (hasLimit() && getValue() > getLimit());
        }

        public CompoundNBT serialize() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("key", getAttributeName());
            nbt.putString("parent", getParentAttributeName());
            nbt.putDouble("value", getValue());
            nbt.putBoolean("isPercentage", isPercentage());
            nbt.putDouble("limit", getLimit());
            return nbt;
        }

        public static AttributeSnapshot deserialize(CompoundNBT nbt) {
            return (new AttributeSnapshot(nbt.getString("key"), nbt.getString("parent"), nbt
                    .getDouble("value"), nbt.getBoolean("isPercentage")))
                    .setLimit(nbt.getDouble("limit"));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\calc\PlayerStatisticsCollector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */