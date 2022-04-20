package iskallia.vault.config;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VaultScalingChestConfig extends Config {
    private static final Random rand;
    @Expose
    private final Map<String, List<Level>> traderChances;
    @Expose
    private final Map<String, List<Level>> statueChances;
    @Expose
    private float megaStatueChance;

    public VaultScalingChestConfig() {
        this.traderChances = new HashMap<String, List<Level>>();
        this.statueChances = new HashMap<String, List<Level>>();
    }

    @Override
    public String getName() {
        return "vault_chest_scaling";
    }

    public boolean isMegaStatue() {
        return VaultScalingChestConfig.rand.nextFloat() < this.megaStatueChance;
    }

    public int traderCount(final ResourceLocation id, final VaultRarity rarity, final int vaultLevel) {
        return this.generateCount(this.traderChances, id, rarity, vaultLevel);
    }

    public int statueCount(final ResourceLocation id, final VaultRarity rarity, final int vaultLevel) {
        return this.generateCount(this.statueChances, id, rarity, vaultLevel);
    }

    private int generateCount(final Map<String, List<Level>> pool, final ResourceLocation id, final VaultRarity rarity, final int vaultLevel) {
        final List<Level> lvls = pool.get(id.toString());
        if (lvls == null) {
            return 0;
        }
        final Level lvl = this.getForLevel(lvls, vaultLevel);
        if (lvl == null) {
            return 0;
        }
        final Float chance = lvl.chances.get(rarity.name());
        if (chance == null) {
            return 0;
        }
        int generatedAmount = MathHelper.floor((float) chance);
        final float decimal = chance - generatedAmount;
        if (VaultScalingChestConfig.rand.nextFloat() < decimal) {
            ++generatedAmount;
        }
        return generatedAmount;
    }

    @Nullable
    public Level getForLevel(final List<Level> levels, final int level) {
        int i = 0;
        while (i < levels.size()) {
            if (level < levels.get(i).level) {
                if (i == 0) {
                    break;
                }
                return levels.get(i - 1);
            } else {
                if (i == levels.size() - 1) {
                    return levels.get(i);
                }
                ++i;
            }
        }
        return null;
    }

    @Override
    protected void reset() {
        this.megaStatueChance = 0.2f;
        this.traderChances.clear();
        this.traderChances.put(ModBlocks.VAULT_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.traderChances.put(ModBlocks.VAULT_ALTAR_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.traderChances.put(ModBlocks.VAULT_TREASURE_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.traderChances.put(ModBlocks.VAULT_COOP_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.traderChances.put(ModBlocks.VAULT_BONUS_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.traderChances.put(VaultRaid.SUMMON_AND_KILL_BOSS.get().getId().toString(), setupCommon(new Level(0)));
        this.traderChances.put(VaultRaid.SCAVENGER_HUNT.get().getId().toString(), setupCommon(new Level(0)));
        this.traderChances.put(VaultRaid.ARCHITECT_EVENT.get().getId().toString(), setupCommon(new Level(0)));
        this.traderChances.put(VaultRaid.ANCIENTS.get().getId().toString(), setupCommon(new Level(0)));
        this.statueChances.clear();
        this.statueChances.put(ModBlocks.VAULT_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.statueChances.put(ModBlocks.VAULT_ALTAR_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.statueChances.put(ModBlocks.VAULT_TREASURE_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.statueChances.put(ModBlocks.VAULT_COOP_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.statueChances.put(ModBlocks.VAULT_BONUS_CHEST.getRegistryName().toString(), setupDefault(new Level(0)));
        this.statueChances.put(VaultRaid.SUMMON_AND_KILL_BOSS.get().getId().toString(), setupCommon(new Level(0)));
        this.statueChances.put(VaultRaid.SCAVENGER_HUNT.get().getId().toString(), setupCommon(new Level(0)));
        this.statueChances.put(VaultRaid.ARCHITECT_EVENT.get().getId().toString(), setupCommon(new Level(0)));
        this.statueChances.put(VaultRaid.ANCIENTS.get().getId().toString(), setupCommon(new Level(0)));
    }

    private static List<Level> setupCommon(final Level level) {
        level.chances.put(VaultRarity.COMMON.name(), 0.2f);
        return Lists.newArrayList(new Level[]{level});
    }

    private static List<Level> setupDefault(final Level level) {
        level.chances.put(VaultRarity.COMMON.name(), 0.0f);
        level.chances.put(VaultRarity.RARE.name(), 0.05f);
        level.chances.put(VaultRarity.EPIC.name(), 0.2f);
        level.chances.put(VaultRarity.OMEGA.name(), 0.5f);
        return Lists.newArrayList(new Level[]{level});
    }

    static {
        rand = new Random();
    }

    public static class Level {
        @Expose
        private final int level;
        @Expose
        private final Map<String, Float> chances;

        public Level(final int level) {
            this.chances = new HashMap<String, Float>();
            this.level = level;
        }
    }
}
