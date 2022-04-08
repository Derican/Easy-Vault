package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.VeinMinerConfig;

public class VeinMinerFortuneConfig extends VeinMinerConfig {
    @Expose
    private final int additionalFortuneLevel;

    public VeinMinerFortuneConfig(int cost, int blockLimit, int additionalFortuneLevel) {
        super(cost, blockLimit);
        this.additionalFortuneLevel = additionalFortuneLevel;
    }

    public int getAdditionalFortuneLevel() {
        return this.additionalFortuneLevel;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\VeinMinerFortuneConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */