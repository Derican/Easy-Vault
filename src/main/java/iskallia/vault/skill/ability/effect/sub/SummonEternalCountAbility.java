package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.SummonEternalConfig;
import iskallia.vault.skill.ability.config.sub.SummonEternalCountConfig;
import iskallia.vault.skill.ability.effect.SummonEternalAbility;
import iskallia.vault.world.data.EternalsData;

public class SummonEternalCountAbility
        extends SummonEternalAbility<SummonEternalCountConfig> {
    protected int getEternalCount(EternalsData.EternalGroup eternals, SummonEternalCountConfig config) {
        return super.getEternalCount(eternals, (SummonEternalConfig) config) + config.getAdditionalCount();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\SummonEternalCountAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */