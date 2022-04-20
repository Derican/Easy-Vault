package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LootTablesConfig extends Config {
    @Expose
    protected List<Level> LEVELS;

    public LootTablesConfig() {
        this.LEVELS = new ArrayList<Level>();
    }

    @Override
    public String getName() {
        return "loot_table";
    }

    @Override
    protected void reset() {
        final Level level = new Level(0);
        level.VAULT_CHEST.put(VaultRarity.COMMON.name(), Vault.sId("chest/lvl0/breadcrumb_common"));
        level.VAULT_CHEST.put(VaultRarity.RARE.name(), Vault.sId("chest/lvl0/breadcrumb_rare"));
        level.VAULT_CHEST.put(VaultRarity.EPIC.name(), Vault.sId("chest/lvl0/breadcrumb_epic"));
        level.VAULT_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId("chest/lvl0/breadcrumb_omega"));
        level.TREASURE_CHEST.put(VaultRarity.COMMON.name(), Vault.sId("chest/lvl0/treasure_common"));
        level.TREASURE_CHEST.put(VaultRarity.RARE.name(), Vault.sId("chest/lvl0/treasure_rare"));
        level.TREASURE_CHEST.put(VaultRarity.EPIC.name(), Vault.sId("chest/lvl0/treasure_epic"));
        level.TREASURE_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId("chest/lvl0/treasure_omega"));
        level.ALTAR_CHEST.put(VaultRarity.COMMON.name(), Vault.sId("chest/lvl0/altar_common"));
        level.ALTAR_CHEST.put(VaultRarity.RARE.name(), Vault.sId("chest/lvl0/altar_rare"));
        level.ALTAR_CHEST.put(VaultRarity.EPIC.name(), Vault.sId("chest/lvl0/altar_epic"));
        level.ALTAR_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId("chest/lvl0/altar_omega"));
        level.COOP_CHEST.put(VaultRarity.COMMON.name(), Vault.sId("chest/lvl0/coop_common"));
        level.COOP_CHEST.put(VaultRarity.RARE.name(), Vault.sId("chest/lvl0/coop_rare"));
        level.COOP_CHEST.put(VaultRarity.EPIC.name(), Vault.sId("chest/lvl0/coop_epic"));
        level.COOP_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId("chest/lvl0/coop_omega"));
        level.BONUS_CHEST.put(VaultRarity.COMMON.name(), Vault.sId("chest/lvl0/bonus_common"));
        level.BONUS_CHEST.put(VaultRarity.RARE.name(), Vault.sId("chest/lvl0/bonus_rare"));
        level.BONUS_CHEST.put(VaultRarity.EPIC.name(), Vault.sId("chest/lvl0/bonus_epic"));
        level.BONUS_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId("chest/lvl0/bonus_omega"));
        level.ALTAR = Vault.sId("chest/altar");
        level.BOSS_CRATE = Vault.sId("chest/lvl0/boss");
        level.SCAVENGER_CRATE = Vault.sId("chest/lvl0/scavenger");
        level.ANCIENT_ETERNAL_BOX = Vault.sId("chest/lvl0/ancient_additional");
        level.BOSS_BONUS_CRATE = Vault.sId("chest/lvl0/boss_bonus");
        level.VAULT_FIGHTER = Vault.sId("entities/lvl0/vault_fighter");
        level.COW = Vault.sId("entities/lvl0/cow");
        level.TREASURE_GOBLIN = Vault.sId("entities/lvl0/treasure_goblin");
        level.ARTIFACT_CHANCE = 0.01f;
        level.SUB_FIGHTER_RAFFLE_SEAL_CHANCE = 0.01f;
        level.CRYSTAL_TYPE.add(CrystalData.Type.CLASSIC, 4);
        level.CRYSTAL_TYPE.add(CrystalData.Type.COOP, 1);
        this.LEVELS.add(level);
    }

    public Level getForLevel(final int level) {
        int i = 0;
        while (i < this.LEVELS.size()) {
            if (level < this.LEVELS.get(i).MIN_LEVEL) {
                if (i == 0) {
                    break;
                }
                return this.LEVELS.get(i - 1);
            } else {
                if (i == this.LEVELS.size() - 1) {
                    return this.LEVELS.get(i);
                }
                ++i;
            }
        }
        return null;
    }

    public static class Level {
        @Expose
        public int MIN_LEVEL;
        @Expose
        public Map<String, String> VAULT_CHEST;
        @Expose
        public Map<String, String> TREASURE_CHEST;
        @Expose
        public Map<String, String> ALTAR_CHEST;
        @Expose
        public Map<String, String> COOP_CHEST;
        @Expose
        public Map<String, String> BONUS_CHEST;
        @Expose
        public String ALTAR;
        @Expose
        public String BOSS_CRATE;
        @Expose
        public String SCAVENGER_CRATE;
        @Expose
        public String ANCIENT_ETERNAL_BOX;
        @Expose
        public String BOSS_BONUS_CRATE;
        @Expose
        public String VAULT_FIGHTER;
        @Expose
        public String COW;
        @Expose
        public String TREASURE_GOBLIN;
        @Expose
        public float ARTIFACT_CHANCE;
        @Expose
        public float SUB_FIGHTER_RAFFLE_SEAL_CHANCE;
        @Expose
        public WeightedList<CrystalData.Type> CRYSTAL_TYPE;

        public Level(final int minLevel) {
            this.VAULT_CHEST = new LinkedHashMap<String, String>();
            this.TREASURE_CHEST = new LinkedHashMap<String, String>();
            this.ALTAR_CHEST = new LinkedHashMap<String, String>();
            this.COOP_CHEST = new LinkedHashMap<String, String>();
            this.BONUS_CHEST = new LinkedHashMap<String, String>();
            this.CRYSTAL_TYPE = new WeightedList<CrystalData.Type>();
            this.MIN_LEVEL = minLevel;
        }

        public ResourceLocation getChest(final VaultRarity rarity) {
            return new ResourceLocation((String) this.VAULT_CHEST.get(rarity.name()));
        }

        public ResourceLocation getTreasureChest(final VaultRarity rarity) {
            return new ResourceLocation((String) this.TREASURE_CHEST.get(rarity.name()));
        }

        public ResourceLocation getAltarChest(final VaultRarity rarity) {
            return new ResourceLocation((String) this.ALTAR_CHEST.get(rarity.name()));
        }

        public ResourceLocation getCoopChest(final VaultRarity rarity) {
            return new ResourceLocation((String) this.COOP_CHEST.get(rarity.name()));
        }

        public ResourceLocation getBonusChest(final VaultRarity rarity) {
            return new ResourceLocation((String) this.BONUS_CHEST.get(rarity.name()));
        }

        public ResourceLocation getAltar() {
            return new ResourceLocation(this.ALTAR);
        }

        public ResourceLocation getBossCrate() {
            return new ResourceLocation(this.BOSS_CRATE);
        }

        public ResourceLocation getScavengerCrate() {
            return new ResourceLocation(this.SCAVENGER_CRATE);
        }

        public ResourceLocation getAncientEternalBonusBox() {
            return new ResourceLocation(this.ANCIENT_ETERNAL_BOX);
        }

        public ResourceLocation getBossBonusCrate() {
            return new ResourceLocation(this.BOSS_BONUS_CRATE);
        }

        public ResourceLocation getVaultFighter() {
            return new ResourceLocation(this.VAULT_FIGHTER);
        }

        public ResourceLocation getCow() {
            return new ResourceLocation(this.COW);
        }

        public ResourceLocation getTreasureGoblin() {
            return new ResourceLocation(this.TREASURE_GOBLIN);
        }

        public float getArtifactChance() {
            return this.ARTIFACT_CHANCE;
        }

        public float getSubFighterRaffleChance() {
            return this.SUB_FIGHTER_RAFFLE_SEAL_CHANCE;
        }
    }
}
