// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.GhostWalkRegenerationConfig;
import iskallia.vault.skill.ability.effect.GhostWalkAbility;

public class GhostWalkRegenerationAbility extends GhostWalkAbility<GhostWalkRegenerationConfig>
{
    @Override
    protected boolean preventsDamage() {
        return false;
    }
}
