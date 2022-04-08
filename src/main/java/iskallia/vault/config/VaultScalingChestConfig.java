package iskallia.vault.config;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import iskallia.vault.world.vault.logic.objective.SummonAndKillBossObjective;
import iskallia.vault.world.vault.logic.objective.ancient.AncientObjective;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VaultScalingChestConfig extends Config {
    private static final Random rand = new Random();
    @Expose
    private final Map<String, List<Level>> traderChances = new HashMap<>();
    @Expose
    private final Map<String, List<Level>> statueChances = new HashMap<>();
    @Expose
    private float megaStatueChance;

    public String getName() {
        return "vault_chest_scaling";
    }

    public boolean isMegaStatue() {
        return (rand.nextFloat() < this.megaStatueChance);
    }

    public int traderCount(ResourceLocation id, VaultRarity rarity, int vaultLevel) {
        return generateCount(this.traderChances, id, rarity, vaultLevel);
    }

    public int statueCount(ResourceLocation id, VaultRarity rarity, int vaultLevel) {
        return generateCount(this.statueChances, id, rarity, vaultLevel);
    }

    private int generateCount(Map<String, List<Level>> pool, ResourceLocation id, VaultRarity rarity, int vaultLevel) {
        List<Level> lvls = pool.get(id.toString());
        if (lvls == null) {
            return 0;
        }
        Level lvl = getForLevel(lvls, vaultLevel);
        if (lvl == null) {
            return 0;
        }
        Float chance = (Float) lvl.chances.get(rarity.name());
        if (chance == null) {
            return 0;
        }
        int generatedAmount = MathHelper.floor(chance.floatValue());
        float decimal = chance.floatValue() - generatedAmount;
        if (rand.nextFloat() < decimal) {
            generatedAmount++;
        }
        return generatedAmount;
    }

    @Nullable
    public Level getForLevel(List<Level> levels, int level) {
        for (int i = 0; i < levels.size(); i++) {
            if (level < ((Level) levels.get(i)).level) {
                if (i == 0) {
                    break;
                }
                return levels.get(i - 1);
            }
            if (i == levels.size() - 1) {
                return levels.get(i);
            }
        }
        return null;
    }


    protected void reset() {
        this.megaStatueChance = 0.2F;

        this.traderChances.clear();

        this.traderChances.put(ModBlocks.VAULT_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.traderChances.put(ModBlocks.VAULT_ALTAR_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.traderChances.put(ModBlocks.VAULT_TREASURE_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.traderChances.put(ModBlocks.VAULT_COOP_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.traderChances.put(ModBlocks.VAULT_BONUS_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.traderChances.put(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId().toString(), setupCommon(new Level(0)));
        this.traderChances.put(((ScavengerHuntObjective) VaultRaid.SCAVENGER_HUNT.get()).getId().toString(), setupCommon(new Level(0)));
        this.traderChances.put(((ArchitectObjective) VaultRaid.ARCHITECT_EVENT.get()).getId().toString(), setupCommon(new Level(0)));
        this.traderChances.put(((AncientObjective) VaultRaid.ANCIENTS.get()).getId().toString(), setupCommon(new Level(0)));

        this.statueChances.clear();

        this.statueChances.put(ModBlocks.VAULT_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.statueChances.put(ModBlocks.VAULT_ALTAR_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.statueChances.put(ModBlocks.VAULT_TREASURE_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.statueChances.put(ModBlocks.VAULT_COOP_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.statueChances.put(ModBlocks.VAULT_BONUS_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.statueChances.put(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId().toString(), setupCommon(new Level(0)));
        this.statueChances.put(((ScavengerHuntObjective) VaultRaid.SCAVENGER_HUNT.get()).getId().toString(), setupCommon(new Level(0)));
        this.statueChances.put(((ArchitectObjective) VaultRaid.ARCHITECT_EVENT.get()).getId().toString(), setupCommon(new Level(0)));
        this.statueChances.put(((AncientObjective) VaultRaid.ANCIENTS.get()).getId().toString(), setupCommon(new Level(0)));
    }

    private static List<Level> setupCommon(Level level) {
        level.chances.put(VaultRarity.COMMON.name(), Float.valueOf(0.2F));
        return Lists.newArrayList( new Level[]{level});
    }

    private static List<Level> setupDefault(Level level) {
        level.chances.put(VaultRarity.COMMON.name(), Float.valueOf(0.0F));
        level.chances.put(VaultRarity.RARE.name(), Float.valueOf(0.05F));
        level.chances.put(VaultRarity.EPIC.name(), Float.valueOf(0.2F));
        level.chances.put(VaultRarity.OMEGA.name(), Float.valueOf(0.5F));
        return Lists.newArrayList( new Level[]{level});
    }

    public static class Level {
        @Expose
        private final int level;
        @Expose
        private final Map<String, Float> chances = new HashMap<>();

        public Level(int level) {
            this.level = level;
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultScalingChestConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */