package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.util.data.WeightedDoubleList;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VaultMetaChestConfig extends Config {
    @Expose
    private final Map<String, Map<String, Float>> catalystChances;
    @Expose
    private final Map<String, Map<String, Float>> runeChances;
    @Expose
    private final Map<String, Float> pityWeight;

    public VaultMetaChestConfig() {
        this.catalystChances = new HashMap<String, Map<String, Float>>();
        this.runeChances = new HashMap<String, Map<String, Float>>();
        this.pityWeight = new HashMap<String, Float>();
    }

    @Override
    public String getName() {
        return "vault_chest_meta";
    }

    public float getCatalystChance(final ResourceLocation chestKey, final VaultRarity chestRarity) {
        return this.catalystChances.getOrDefault(chestKey.toString(), Collections.emptyMap()).getOrDefault(chestRarity.name(), 0.0f);
    }

    public float getRuneChance(final ResourceLocation chestKey, final VaultRarity chestRarity) {
        return this.runeChances.getOrDefault(chestKey.toString(), Collections.emptyMap()).getOrDefault(chestRarity.name(), 0.0f);
    }

    public WeightedDoubleList<String> getPityAdjustedRarity(final WeightedDoubleList<String> chestWeights, final int ticksSinceLastChest) {
        final float multiplier = ticksSinceLastChest / 1200.0f;
        final WeightedDoubleList<String> adjusted = new WeightedDoubleList<String>();
        chestWeights.forEach((rarityKey, weight) -> {
            final float modifier = this.pityWeight.getOrDefault(rarityKey, 1.0f);
            final float newWeight = weight.floatValue() + weight.floatValue() * modifier * multiplier;
            if (newWeight > 0.0f) {
                adjusted.add(rarityKey, newWeight);
            }
            return;
        });
        return adjusted;
    }

    @Override
    protected void reset() {
        this.pityWeight.clear();
        this.pityWeight.put(VaultRarity.COMMON.name(), -0.2f);
        this.pityWeight.put(VaultRarity.RARE.name(), -0.14f);
        this.pityWeight.put(VaultRarity.EPIC.name(), 0.1f);
        this.pityWeight.put(VaultRarity.OMEGA.name(), 0.3f);
        this.catalystChances.clear();
        this.setupEmptyChances(ModBlocks.VAULT_CHEST, this.catalystChances);
        this.setupEmptyChances(ModBlocks.VAULT_ALTAR_CHEST, this.catalystChances);
        this.setupEmptyChances(ModBlocks.VAULT_TREASURE_CHEST, this.catalystChances);
        this.setupEmptyChances(ModBlocks.VAULT_COOP_CHEST, this.catalystChances);
        this.setupEmptyChances(ModBlocks.VAULT_BONUS_CHEST, this.catalystChances);
        this.runeChances.clear();
        this.setupEmptyChances(ModBlocks.VAULT_CHEST, this.runeChances);
        this.setupEmptyChances(ModBlocks.VAULT_ALTAR_CHEST, this.runeChances);
        this.setupEmptyChances(ModBlocks.VAULT_TREASURE_CHEST, this.runeChances);
        this.setupEmptyChances(ModBlocks.VAULT_COOP_CHEST, this.runeChances);
        this.setupEmptyChances(ModBlocks.VAULT_BONUS_CHEST, this.runeChances);
        Map<String, Float> chestChances = this.catalystChances.get(ModBlocks.VAULT_CHEST.getRegistryName().toString());
        chestChances.put(VaultRarity.RARE.name(), 0.1f);
        chestChances.put(VaultRarity.EPIC.name(), 0.4f);
        chestChances.put(VaultRarity.OMEGA.name(), 0.5f);
        chestChances = this.catalystChances.get(ModBlocks.VAULT_ALTAR_CHEST.getRegistryName().toString());
        for (final VaultRarity rarity : VaultRarity.values()) {
            chestChances.put(rarity.name(), 1.0f);
        }
    }

    private void setupEmptyChances(final Block block, final Map<String, Map<String, Float>> mapOut) {
        final Map<String, Float> chances = new HashMap<String, Float>();
        for (final VaultRarity rarity : VaultRarity.values()) {
            chances.put(rarity.name(), 0.0f);
        }
        mapOut.put(block.getRegistryName().toString(), chances);
    }
}
