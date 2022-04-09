// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.CleanseHealConfig;
import iskallia.vault.skill.ability.effect.CleanseAbility;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;

import java.util.List;

public class CleanseHealAbility extends CleanseAbility<CleanseHealConfig>
{
    @Override
    protected void removeEffects(final CleanseHealConfig config, final ServerPlayerEntity player, final List<EffectInstance> effects) {
        super.removeEffects(config, player, effects);
        for (final EffectInstance ignored : effects) {
            player.heal(config.getHealthPerEffectRemoved());
        }
    }
}
