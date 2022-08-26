package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.util.ResourceLocation;

import java.util.*;

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
        {
            final Level level = new Level(0);
            level.VAULT_CHEST.put(VaultRarity.COMMON.name(), Vault.sId("chest/lvl0/vaultchestcommon"));
            level.VAULT_CHEST.put(VaultRarity.RARE.name(), Vault.sId("chest/lvl0/vaultchestrare"));
            level.VAULT_CHEST.put(VaultRarity.EPIC.name(), Vault.sId("chest/lvl0/vaultchestepic"));
            level.VAULT_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId("chest/lvl0/vaultchestomega"));
            level.TREASURE_CHEST.put(VaultRarity.COMMON.name(), Vault.sId("chest/lvl0/treasurecommon"));
            level.TREASURE_CHEST.put(VaultRarity.RARE.name(), Vault.sId("chest/lvl0/treasurecommon"));
            level.TREASURE_CHEST.put(VaultRarity.EPIC.name(), Vault.sId("chest/lvl0/treasurecommon"));
            level.TREASURE_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId("chest/lvl0/treasurecommon"));
            level.ALTAR_CHEST.put(VaultRarity.COMMON.name(), Vault.sId("chest/lvl0/godaltarcommon"));
            level.ALTAR_CHEST.put(VaultRarity.RARE.name(), Vault.sId("chest/lvl0/godaltarrare"));
            level.ALTAR_CHEST.put(VaultRarity.EPIC.name(), Vault.sId("chest/lvl0/godaltarepic"));
            level.ALTAR_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId("chest/lvl0/godaltaromega"));
            level.COOP_CHEST.put(VaultRarity.COMMON.name(), Vault.sId("chest/lvl0/vaultchestepic"));
            level.COOP_CHEST.put(VaultRarity.RARE.name(), Vault.sId("chest/lvl0/vaultchestepic"));
            level.COOP_CHEST.put(VaultRarity.EPIC.name(), Vault.sId("chest/lvl0/vaultchestepic"));
            level.COOP_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId("chest/lvl0/vaultchestepic"));
            level.BONUS_CHEST.put(VaultRarity.COMMON.name(), Vault.sId("chest/lvl0/gildedcommon"));
            level.BONUS_CHEST.put(VaultRarity.RARE.name(), Vault.sId("chest/lvl0/gildedrare"));
            level.BONUS_CHEST.put(VaultRarity.EPIC.name(), Vault.sId("chest/lvl0/gildedepic"));
            level.BONUS_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId("chest/lvl0/gildedomega"));
            level.ALTAR = Vault.sId("chest/altar");
            level.BOSS_CRATE = Vault.sId("chest/lvl0/bosscrate");
            level.SCAVENGER_CRATE = Vault.sId("chest/lvl0/bosscratenew");
            level.BOSS_BONUS_CRATE = Vault.sId("chest/lvl0/championbox");
            level.VAULT_FIGHTER = Vault.sId("entities/lvl0/vault_fighter");
            level.COW = Vault.sId("entities/lvl0/cow");
            level.TREASURE_GOBLIN = Vault.sId("entities/lvl0/treasure_goblin");
            level.ANCIENT_ETERNAL_BOX = Vault.sId("chest/lvl0/ancientbox");
            level.ARTIFACT_CHANCE = 0.00f;
            level.SUB_FIGHTER_RAFFLE_SEAL_CHANCE = 0.005f;
            level.CRYSTAL_TYPE.add(CrystalData.Type.CLASSIC, 1);
            this.LEVELS.add(level);
        }
        for (int i : Arrays.asList(25, 50, 75, 100, 150, 200, 250)) {
            final Level level = new Level(i);
            level.VAULT_CHEST.put(VaultRarity.COMMON.name(), Vault.sId(String.format("chest/lvl%d/vaultchestcommon", i)));
            level.VAULT_CHEST.put(VaultRarity.RARE.name(), Vault.sId(String.format("chest/lvl%d/vaultchestrare", i)));
            level.VAULT_CHEST.put(VaultRarity.EPIC.name(), Vault.sId(String.format("chest/lvl%d/vaultchestepic", i)));
            level.VAULT_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId(String.format("chest/lvl%d/vaultchestomega", i)));
            level.TREASURE_CHEST.put(VaultRarity.COMMON.name(), Vault.sId(String.format("chest/lvl%d/treasurecommon", i)));
            level.TREASURE_CHEST.put(VaultRarity.RARE.name(), Vault.sId(String.format("chest/lvl%d/treasurerare", i)));
            level.TREASURE_CHEST.put(VaultRarity.EPIC.name(), Vault.sId(String.format("chest/lvl%d/treasurerare", i)));
            level.TREASURE_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId(String.format("chest/lvl%d/treasureomega", i)));
            level.ALTAR_CHEST.put(VaultRarity.COMMON.name(), Vault.sId(String.format("chest/lvl%d/godaltarcommon", i)));
            level.ALTAR_CHEST.put(VaultRarity.RARE.name(), Vault.sId(String.format("chest/lvl%d/godaltarrare", i)));
            level.ALTAR_CHEST.put(VaultRarity.EPIC.name(), Vault.sId(String.format("chest/lvl%d/godaltarepic", i)));
            level.ALTAR_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId(String.format("chest/lvl%d/godaltaromega", i)));
            level.COOP_CHEST.put(VaultRarity.COMMON.name(), Vault.sId(String.format("chest/lvl%d/coopchest", i)));
            level.COOP_CHEST.put(VaultRarity.RARE.name(), Vault.sId(String.format("chest/lvl%d/coopchest", i)));
            level.COOP_CHEST.put(VaultRarity.EPIC.name(), Vault.sId(String.format("chest/lvl%d/coopchest", i)));
            level.COOP_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId(String.format("chest/lvl%d/coopchest", i)));
            level.BONUS_CHEST.put(VaultRarity.COMMON.name(), Vault.sId(String.format("chest/lvl%d/gildedcommon", i)));
            level.BONUS_CHEST.put(VaultRarity.RARE.name(), Vault.sId(String.format("chest/lvl%d/gildedrare", i)));
            level.BONUS_CHEST.put(VaultRarity.EPIC.name(), Vault.sId(String.format("chest/lvl%d/gildedepic", i)));
            level.BONUS_CHEST.put(VaultRarity.OMEGA.name(), Vault.sId(String.format("chest/lvl%d/gildedomega", i)));
            level.ALTAR = Vault.sId("chest/altar");
            level.BOSS_CRATE = Vault.sId(String.format("chest/lvl%d/bosscratenew", i));
            level.SCAVENGER_CRATE = Vault.sId(String.format("chest/lvl%d/scavengecrate", i));
            level.BOSS_BONUS_CRATE = Vault.sId(String.format("chest/lvl%d/championbox", i));
            level.VAULT_FIGHTER = Vault.sId(String.format("entities/lvl%d/vault_fighter", i));
            level.COW = Vault.sId(String.format("entities/lvl%d/cow", i));
            level.TREASURE_GOBLIN = Vault.sId(String.format("entities/lvl%d/treasure_goblin", i));
            level.ANCIENT_ETERNAL_BOX = Vault.sId(String.format("chest/lvl%d/ancientbox", i));
            level.ARTIFACT_CHANCE = 0.01f * (i - 25) / 25;
            level.SUB_FIGHTER_RAFFLE_SEAL_CHANCE = 0.005f * (i - 25) / 25;
            level.CRYSTAL_TYPE.add(CrystalData.Type.CLASSIC, 4);
            level.CRYSTAL_TYPE.add(CrystalData.Type.COOP, 1);
            this.LEVELS.add(level);
        }
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
            return new ResourceLocation(this.VAULT_CHEST.get(rarity.name()));
        }

        public ResourceLocation getTreasureChest(final VaultRarity rarity) {
            return new ResourceLocation(this.TREASURE_CHEST.get(rarity.name()));
        }

        public ResourceLocation getAltarChest(final VaultRarity rarity) {
            return new ResourceLocation(this.ALTAR_CHEST.get(rarity.name()));
        }

        public ResourceLocation getCoopChest(final VaultRarity rarity) {
            return new ResourceLocation(this.COOP_CHEST.get(rarity.name()));
        }

        public ResourceLocation getBonusChest(final VaultRarity rarity) {
            return new ResourceLocation(this.BONUS_CHEST.get(rarity.name()));
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
