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

public class VaultMetaChestConfig
        extends Config {
    @Expose
    private final Map<String, Map<String, Float>> catalystChances = new HashMap<>();
    @Expose
    private final Map<String, Map<String, Float>> runeChances = new HashMap<>();
    @Expose
    private final Map<String, Float> pityWeight = new HashMap<>();


    public String getName() {
        return "vault_chest_meta";
    }

    public float getCatalystChance(ResourceLocation chestKey, VaultRarity chestRarity) {
        return ((Float) ((Map) this.catalystChances.getOrDefault(chestKey.toString(), Collections.emptyMap()))
                .getOrDefault(chestRarity.name(), Float.valueOf(0.0F))).floatValue();
    }

    public float getRuneChance(ResourceLocation chestKey, VaultRarity chestRarity) {
        return ((Float) ((Map) this.runeChances.getOrDefault(chestKey.toString(), Collections.emptyMap()))
                .getOrDefault(chestRarity.name(), Float.valueOf(0.0F))).floatValue();
    }

    public WeightedDoubleList<String> getPityAdjustedRarity(WeightedDoubleList<String> chestWeights, int ticksSinceLastChest) {
        float multiplier = ticksSinceLastChest / 1200.0F;
        WeightedDoubleList<String> adjusted = new WeightedDoubleList();
        chestWeights.forEach((rarityKey, weight) -> {
            float modifier = ((Float) this.pityWeight.getOrDefault(rarityKey, Float.valueOf(1.0F))).floatValue();
            float newWeight = weight.floatValue() + weight.floatValue() * modifier * multiplier;
            if (newWeight > 0.0F) {
                adjusted.add(rarityKey, newWeight);
            }
        });
        return adjusted;
    }


    protected void reset() {
        this.pityWeight.clear();
        this.pityWeight.put(VaultRarity.COMMON.name(), Float.valueOf(-0.2F));
        this.pityWeight.put(VaultRarity.RARE.name(), Float.valueOf(-0.14F));
        this.pityWeight.put(VaultRarity.EPIC.name(), Float.valueOf(0.1F));
        this.pityWeight.put(VaultRarity.OMEGA.name(), Float.valueOf(0.3F));

        this.catalystChances.clear();
        setupEmptyChances(ModBlocks.VAULT_CHEST, this.catalystChances);
        setupEmptyChances(ModBlocks.VAULT_ALTAR_CHEST, this.catalystChances);
        setupEmptyChances(ModBlocks.VAULT_TREASURE_CHEST, this.catalystChances);
        setupEmptyChances(ModBlocks.VAULT_COOP_CHEST, this.catalystChances);
        setupEmptyChances(ModBlocks.VAULT_BONUS_CHEST, this.catalystChances);

        this.runeChances.clear();
        setupEmptyChances(ModBlocks.VAULT_CHEST, this.runeChances);
        setupEmptyChances(ModBlocks.VAULT_ALTAR_CHEST, this.runeChances);
        setupEmptyChances(ModBlocks.VAULT_TREASURE_CHEST, this.runeChances);
        setupEmptyChances(ModBlocks.VAULT_COOP_CHEST, this.runeChances);
        setupEmptyChances(ModBlocks.VAULT_BONUS_CHEST, this.runeChances);

        Map<String, Float> chestChances = this.catalystChances.get(ModBlocks.VAULT_CHEST.getRegistryName().toString());
        chestChances.put(VaultRarity.RARE.name(), Float.valueOf(0.1F));
        chestChances.put(VaultRarity.EPIC.name(), Float.valueOf(0.4F));
        chestChances.put(VaultRarity.OMEGA.name(), Float.valueOf(0.5F));

        chestChances = this.catalystChances.get(ModBlocks.VAULT_ALTAR_CHEST.getRegistryName().toString());
        for (VaultRarity rarity : VaultRarity.values()) {
            chestChances.put(rarity.name(), Float.valueOf(1.0F));
        }
    }

    private void setupEmptyChances(Block block, Map<String, Map<String, Float>> mapOut) {
        Map<String, Float> chances = new HashMap<>();
        for (VaultRarity rarity : VaultRarity.values()) {
            chances.put(rarity.name(), Float.valueOf(0.0F));
        }
        mapOut.put(block.getRegistryName().toString(), chances);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultMetaChestConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */