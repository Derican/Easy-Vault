// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.GhostWalkParryConfig;
import iskallia.vault.skill.ability.effect.GhostWalkAbility;

public class GhostWalkParryAbility extends GhostWalkAbility<GhostWalkParryConfig>
{
    @Override
    protected boolean preventsDamage() {
        return false;
    }
    
    @Override
    protected boolean doRemoveWhenDealingDamage() {
        return false;
    }
}
