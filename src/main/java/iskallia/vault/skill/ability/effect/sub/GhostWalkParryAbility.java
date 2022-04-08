package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.GhostWalkParryConfig;
import iskallia.vault.skill.ability.effect.GhostWalkAbility;

public class GhostWalkParryAbility
        extends GhostWalkAbility<GhostWalkParryConfig> {
    protected boolean preventsDamage() {
        return false;
    }


    protected boolean doRemoveWhenDealingDamage() {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\GhostWalkParryAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */