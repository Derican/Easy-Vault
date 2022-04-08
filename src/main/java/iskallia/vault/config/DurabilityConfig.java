package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class DurabilityConfig
        extends Config {
    @Expose
    private final Map<Integer, Float> durabilityOverride = new HashMap<>();
    @Expose
    private final Map<Integer, Float> armorDurabilityOverride = new HashMap<>();


    public String getName() {
        return "durability";
    }

    public float getDurabilityIgnoreChance(int unbreakingLevel) {
        return getIgnoreChance(this.durabilityOverride, unbreakingLevel);
    }

    public float getArmorDurabilityIgnoreChance(int unbreakingLevel) {
        return getIgnoreChance(this.armorDurabilityOverride, unbreakingLevel);
    }

    private float getIgnoreChance(Map<Integer, Float> chanceMap, int unbreakingLevel) {
        if (unbreakingLevel < 1) {
            return 0.0F;
        }


        int overrideLevel = chanceMap.keySet().stream().filter(level -> (level.intValue() <= unbreakingLevel)).mapToInt(level -> level.intValue()).max().orElse(0);
        if (overrideLevel <= 0) {
            return 0.0F;
        }
        return ((Float) chanceMap.get(Integer.valueOf(overrideLevel))).floatValue();
    }


    protected void reset() {
        this.durabilityOverride.clear();
        this.armorDurabilityOverride.clear();

        this.durabilityOverride.put(Integer.valueOf(1), Float.valueOf(0.5F));
        this.durabilityOverride.put(Integer.valueOf(2), Float.valueOf(0.66667F));
        this.durabilityOverride.put(Integer.valueOf(3), Float.valueOf(0.75F));
        this.durabilityOverride.put(Integer.valueOf(4), Float.valueOf(0.78F));
        this.durabilityOverride.put(Integer.valueOf(5), Float.valueOf(0.8F));
        this.durabilityOverride.put(Integer.valueOf(6), Float.valueOf(0.82F));
        this.durabilityOverride.put(Integer.valueOf(7), Float.valueOf(0.84F));
        this.durabilityOverride.put(Integer.valueOf(8), Float.valueOf(0.86F));
        this.durabilityOverride.put(Integer.valueOf(9), Float.valueOf(0.88F));
        this.durabilityOverride.put(Integer.valueOf(10), Float.valueOf(0.9F));

        this.armorDurabilityOverride.put(Integer.valueOf(1), Float.valueOf(0.2F));
        this.armorDurabilityOverride.put(Integer.valueOf(2), Float.valueOf(0.27F));
        this.armorDurabilityOverride.put(Integer.valueOf(3), Float.valueOf(0.3F));
        this.armorDurabilityOverride.put(Integer.valueOf(4), Float.valueOf(0.33F));
        this.armorDurabilityOverride.put(Integer.valueOf(5), Float.valueOf(0.36F));
        this.armorDurabilityOverride.put(Integer.valueOf(6), Float.valueOf(0.39F));
        this.armorDurabilityOverride.put(Integer.valueOf(7), Float.valueOf(0.42F));
        this.armorDurabilityOverride.put(Integer.valueOf(8), Float.valueOf(0.45F));
        this.armorDurabilityOverride.put(Integer.valueOf(9), Float.valueOf(0.48F));
        this.armorDurabilityOverride.put(Integer.valueOf(10), Float.valueOf(0.51F));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\DurabilityConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */