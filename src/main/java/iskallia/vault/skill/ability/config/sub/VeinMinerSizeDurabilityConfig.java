package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.VeinMinerConfig;

public class VeinMinerSizeDurabilityConfig extends VeinMinerConfig {
    @Expose
    private final float doubleDurabilityCostChance;

    public VeinMinerSizeDurabilityConfig(int cost, int blockLimit, float doubleDurabilityCostChance) {
        super(cost, blockLimit);
        this.doubleDurabilityCostChance = doubleDurabilityCostChance;
    }

    public float getDoubleDurabilityCostChance() {
        return this.doubleDurabilityCostChance;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\VeinMinerSizeDurabilityConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */