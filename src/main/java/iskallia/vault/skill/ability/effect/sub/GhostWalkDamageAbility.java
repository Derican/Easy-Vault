// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.GhostWalkDamageConfig;
import iskallia.vault.skill.ability.effect.GhostWalkAbility;

public class GhostWalkDamageAbility extends GhostWalkAbility<GhostWalkDamageConfig>
{
    @Override
    protected boolean doRemoveWhenDealingDamage() {
        return false;
    }
    
    @Override
    protected boolean preventsDamage() {
        return false;
    }
}
