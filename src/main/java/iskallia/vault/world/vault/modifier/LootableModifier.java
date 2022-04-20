package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.data.RandomListAccess;
import iskallia.vault.util.data.WeightedDoubleList;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class LootableModifier extends TexturedVaultModifier {
    @Expose
    private final String pool;
    @Expose
    private final Map<String, Float> resultMultipliers;

    public LootableModifier(final String name, final ResourceLocation icon, final String affectedPool, final Map<String, Float> resultMultipliers) {
        super(name, icon);
        this.resultMultipliers = new HashMap<String, Float>();
        this.pool = affectedPool;
        this.resultMultipliers.putAll(resultMultipliers);
    }

    public RandomListAccess<String> adjustLootWeighting(final String pool, final RandomListAccess<String> weightedList) {
        if (pool.equalsIgnoreCase(this.pool)) {
            final WeightedDoubleList<String> resultList = new WeightedDoubleList<String>();
            weightedList.forEach((entry, weight) -> resultList.add(entry, weight.doubleValue() * this.resultMultipliers.getOrDefault(entry, 1.0f)));
            return resultList;
        }
        return weightedList;
    }

    public float getAverageMultiplier() {
        return (float) this.resultMultipliers.values().stream().mapToDouble(Float::doubleValue).average().orElse(1.0);
    }

    public static Map<String, Float> getDefaultOreModifiers(final float multiplier) {
        final Map<String, Float> oreResults = new HashMap<String, Float>();
        oreResults.put(ModBlocks.BENITOITE_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.LARIMAR_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.ALEXANDRITE_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.WUTODIE_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.PAINITE_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.BLACK_OPAL_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.ISKALLIUM_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.PUFFIUM_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.GORGINITE_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.SPARKLETINE_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.ASHIUM_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.BOMIGNITE_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.FUNSOIDE_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.TUBIUM_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.UPALINE_ORE.getRegistryName().toString(), multiplier);
        oreResults.put(ModBlocks.ECHO_ORE.getRegistryName().toString(), multiplier);
        return oreResults;
    }
}
