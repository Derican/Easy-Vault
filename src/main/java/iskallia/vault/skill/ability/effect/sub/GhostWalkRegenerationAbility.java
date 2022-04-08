package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.GhostWalkRegenerationConfig;
import iskallia.vault.skill.ability.effect.GhostWalkAbility;

public class GhostWalkRegenerationAbility
        extends GhostWalkAbility<GhostWalkRegenerationConfig> {
    protected boolean preventsDamage() {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\GhostWalkRegenerationAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */