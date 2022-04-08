package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.ExecuteBuffConfig;
import iskallia.vault.skill.ability.effect.ExecuteAbility;

public class ExecuteBuffAbility
        extends ExecuteAbility<ExecuteBuffConfig> {
    protected boolean removeEffect(ExecuteBuffConfig cfg) {
        return (rand.nextFloat() < cfg.getRegainBuffChance());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\ExecuteBuffAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */