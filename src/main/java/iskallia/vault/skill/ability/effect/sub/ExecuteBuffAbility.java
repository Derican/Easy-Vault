package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.ExecuteBuffConfig;
import iskallia.vault.skill.ability.effect.ExecuteAbility;

public class ExecuteBuffAbility extends ExecuteAbility<ExecuteBuffConfig> {
    @Override
    protected boolean removeEffect(final ExecuteBuffConfig cfg) {
        return ExecuteBuffAbility.rand.nextFloat() < cfg.getRegainBuffChance();
    }
}
