package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.VeinMinerConfig;

public class VeinMinerDurabilityConfig extends VeinMinerConfig {
    @Expose
    private final float noDurabilityUsageChance;

    public VeinMinerDurabilityConfig(int cost, int blockLimit, float noDurabilityUsageChance) {
        super(cost, blockLimit);
        this.noDurabilityUsageChance = noDurabilityUsageChance;
    }

    public float getNoDurabilityUsageChance() {
        return this.noDurabilityUsageChance;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\VeinMinerDurabilityConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */