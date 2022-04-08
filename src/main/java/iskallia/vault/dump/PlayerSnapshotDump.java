package iskallia.vault.dump;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import iskallia.vault.Vault;
import iskallia.vault.config.EternalAuraConfig;
import iskallia.vault.entity.eternal.EternalData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.calc.*;
import iskallia.vault.world.data.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.time.Instant;
import java.util.*;

public class PlayerSnapshotDump {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public static String createAndSerializeSnapshot(ServerPlayerEntity sPlayer) {
        return GSON.toJson(createSnapshot(sPlayer));
    }

    public static PlayerSnapshot createSnapshot(ServerPlayerEntity sPlayer) {
        PlayerSnapshot snapshot = new PlayerSnapshot(sPlayer);
        ServerWorld sWorld = sPlayer.getLevel();
        snapshot.inVault = (sWorld.dimension() == Vault.VAULT_KEY);

        PlayerVaultStats stats = PlayerVaultStatsData.get(sWorld).getVaultStats((PlayerEntity) sPlayer);
        snapshot.vaultLevel = stats.getVaultLevel();
        snapshot.levelPercent = stats.getExp() / stats.getTnl();

        AttributeModifierManager mgr = sPlayer.getAttributes();
        for (Attribute attribute : ForgeRegistries.ATTRIBUTES) {
            if (mgr.hasAttribute(attribute)) {
                ResourceLocation attrId = attribute.getRegistryName();
                snapshot.attributes.put((attrId == null) ? attribute.getDescriptionId() : attrId.toString(),
                        Double.valueOf(mgr.getValue(attribute)));
            }
        }
        snapshot.parry = ParryHelper.getPlayerParryChance(sPlayer);
        snapshot.resistance = ResistanceHelper.getPlayerResistancePercent(sPlayer);
        snapshot.cooldownReduction = CooldownHelper.getCooldownMultiplier(sPlayer, null);
        snapshot.fatalStrikeChance = FatalStrikeHelper.getPlayerFatalStrikeChance(sPlayer);
        snapshot.fatalStrikeDamage = FatalStrikeHelper.getPlayerFatalStrikeDamage(sPlayer);
        snapshot.thornsChance = ThornsHelper.getThornsChance((LivingEntity) sPlayer);
        snapshot.thornsDamage = ThornsHelper.getThornsDamage((LivingEntity) sPlayer);

        Arrays.<EquipmentSlotType>stream(EquipmentSlotType.values()).forEach(slotType -> {
            ItemStack stack = sPlayer.getItemBySlot(slotType);

            if (!stack.isEmpty()) {
                snapshot.equipment.put(slotType.name(), new SerializableItemStack(stack));
            }
        });
        AbilityTree abilities = PlayerAbilitiesData.get(sWorld).getAbilities((PlayerEntity) sPlayer);
        abilities.getLearnedNodes().forEach(node -> {
            if (node.getSpecialization() != null) {
                snapshot.abilities.put(node.getGroup().getParentName() + ": " + node.getSpecializationName(), Integer.valueOf(node.getLevel()));
            } else {
                snapshot.abilities.put(node.getGroup().getParentName(), Integer.valueOf(node.getLevel()));
            }
        });

        TalentTree talents = PlayerTalentsData.get(sWorld).getTalents((PlayerEntity) sPlayer);
        talents.getLearnedNodes().forEach(node -> snapshot.talents.put(node.getGroup().getParentName(), Integer.valueOf(node.getLevel())));


        ResearchTree researches = PlayerResearchesData.get(sWorld).getResearches((PlayerEntity) sPlayer);
        snapshot.researches.addAll(researches.getResearchesDone());

        PlayerStatisticsCollector.VaultRunsSnapshot vaultRunsSnapshot = PlayerStatisticsCollector.VaultRunsSnapshot.ofPlayer(sPlayer);
        snapshot.vaultRuns = vaultRunsSnapshot.vaultRuns;
        snapshot.vaultWins = vaultRunsSnapshot.bossKills;
        snapshot.vaultDeaths = vaultRunsSnapshot.deaths;
        snapshot.artifactCount = vaultRunsSnapshot.artifacts;
        snapshot.powerLevel = stats.getTotalSpentSkillPoints() + stats.getUnspentSkillPts();

        PlayerFavourData favourData = PlayerFavourData.get(sWorld);
        for (PlayerFavourData.VaultGodType type : PlayerFavourData.VaultGodType.values()) {
            snapshot.favors.put(type.getName(), Integer.valueOf(favourData.getFavour(sPlayer.getUUID(), type)));
        }

        EternalsData.EternalGroup group = EternalsData.get(sWorld).getEternals((PlayerEntity) sPlayer);
        for (EternalData eternal : group.getEternals()) {
            String auraName = null;
            if (eternal.getAbilityName() != null) {
                EternalAuraConfig.AuraConfig cfg = ModConfigs.ETERNAL_AURAS.getByName(eternal.getAbilityName());
                if (cfg != null) {
                    auraName = cfg.getDisplayName();
                }
            }

            EternalInformation eternalSnapshot = new EternalInformation(eternal.getName(), eternal.getLevel(), eternal.isAncient(), auraName);
            eternal.getEquipment().forEach((slot, stack) -> {
                if (!stack.isEmpty()) {
                    eternalSnapshot.equipment.put(slot.name(), new SerializableItemStack(stack));
                }
            });
            snapshot.eternals.add(eternalSnapshot);
        }

        return snapshot;
    }

    public static class PlayerSnapshot {
        protected final UUID playerUUID;
        protected final String playerNickname;
        protected final long timestamp;
        protected int vaultRuns;
        protected int vaultWins;
        protected int vaultDeaths;
        protected int artifactCount;
        protected boolean inVault = false;
        protected int powerLevel = 0;
        protected int vaultLevel = 0;
        protected float levelPercent = 0.0F;
        protected Map<String, Double> attributes = new LinkedHashMap<>();
        protected Map<String, Integer> favors = new LinkedHashMap<>();
        protected float parry;
        protected float resistance;
        protected float cooldownReduction;
        protected float fatalStrikeChance;
        protected float fatalStrikeDamage;
        protected float thornsChance;
        protected float thornsDamage;
        protected Map<String, PlayerSnapshotDump.SerializableItemStack> equipment = new LinkedHashMap<>();
        protected Map<String, Integer> abilities = new LinkedHashMap<>();
        protected Map<String, Integer> talents = new LinkedHashMap<>();
        protected Set<String> researches = new LinkedHashSet<>();
        protected Set<PlayerSnapshotDump.EternalInformation> eternals = new LinkedHashSet<>();

        public PlayerSnapshot(ServerPlayerEntity playerEntity) {
            this.playerUUID = playerEntity.getUUID();
            this.playerNickname = playerEntity.getName().getString();
            this.timestamp = Instant.now().getEpochSecond();
        }
    }


    public static class EternalInformation {
        private final String name;
        private final int level;
        private final boolean isAncient;
        private final String auraName;
        private Map<String, PlayerSnapshotDump.SerializableItemStack> equipment = new LinkedHashMap<>();

        public EternalInformation(String name, int level, boolean isAncient, String auraName) {
            this.name = name;
            this.level = level;
            this.isAncient = isAncient;
            this.auraName = auraName;
        }
    }

    public static class SerializableItemStack {
        private final String itemKey;
        private final int count;
        private final String nbt;

        private SerializableItemStack(ItemStack stack) {
            this.itemKey = stack.getItem().getRegistryName().toString();
            this.count = stack.getCount();
            if (stack.hasTag()) {
                this.nbt = stack.getTag().toString();
            } else {
                this.nbt = null;
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\dump\PlayerSnapshotDump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */