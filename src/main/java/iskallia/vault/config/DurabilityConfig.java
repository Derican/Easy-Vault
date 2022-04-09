// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class DurabilityConfig extends Config
{
    @Expose
    private final Map<Integer, Float> durabilityOverride;
    @Expose
    private final Map<Integer, Float> armorDurabilityOverride;
    
    public DurabilityConfig() {
        this.durabilityOverride = new HashMap<Integer, Float>();
        this.armorDurabilityOverride = new HashMap<Integer, Float>();
    }
    
    @Override
    public String getName() {
        return "durability";
    }
    
    public float getDurabilityIgnoreChance(final int unbreakingLevel) {
        return this.getIgnoreChance(this.durabilityOverride, unbreakingLevel);
    }
    
    public float getArmorDurabilityIgnoreChance(final int unbreakingLevel) {
        return this.getIgnoreChance(this.armorDurabilityOverride, unbreakingLevel);
    }
    
    private float getIgnoreChance(final Map<Integer, Float> chanceMap, final int unbreakingLevel) {
        if (unbreakingLevel < 1) {
            return 0.0f;
        }
        final int overrideLevel = chanceMap.keySet().stream().filter(level -> level <= unbreakingLevel).mapToInt(level -> level).max().orElse(0);
        if (overrideLevel <= 0) {
            return 0.0f;
        }
        return chanceMap.get(overrideLevel);
    }
    
    @Override
    protected void reset() {
        this.durabilityOverride.clear();
        this.armorDurabilityOverride.clear();
        this.durabilityOverride.put(1, 0.5f);
        this.durabilityOverride.put(2, 0.66667f);
        this.durabilityOverride.put(3, 0.75f);
        this.durabilityOverride.put(4, 0.78f);
        this.durabilityOverride.put(5, 0.8f);
        this.durabilityOverride.put(6, 0.82f);
        this.durabilityOverride.put(7, 0.84f);
        this.durabilityOverride.put(8, 0.86f);
        this.durabilityOverride.put(9, 0.88f);
        this.durabilityOverride.put(10, 0.9f);
        this.armorDurabilityOverride.put(1, 0.2f);
        this.armorDurabilityOverride.put(2, 0.27f);
        this.armorDurabilityOverride.put(3, 0.3f);
        this.armorDurabilityOverride.put(4, 0.33f);
        this.armorDurabilityOverride.put(5, 0.36f);
        this.armorDurabilityOverride.put(6, 0.39f);
        this.armorDurabilityOverride.put(7, 0.42f);
        this.armorDurabilityOverride.put(8, 0.45f);
        this.armorDurabilityOverride.put(9, 0.48f);
        this.armorDurabilityOverride.put(10, 0.51f);
    }
}
