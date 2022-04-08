package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;

public class VeinMinerConfig extends AbilityConfig {
    @Expose
    private final int blockLimit;

    public VeinMinerConfig(int cost, int blockLimit) {
        super(cost, AbilityConfig.Behavior.HOLD_TO_ACTIVATE);
        this.blockLimit = blockLimit;
    }

    public int getBlockLimit() {
        return this.blockLimit;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\VeinMinerConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */