// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.effect;

import iskallia.vault.init.ModEffects;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.config.RampageConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;

public class RampageAbility<C extends RampageConfig> extends AbilityEffect<C>
{
    @Override
    public String getAbilityGroupName() {
        return "Rampage";
    }
    
    @Override
    public boolean onAction(final C config, final ServerPlayerEntity player, final boolean active) {
        if (player.hasEffect(ModEffects.RAMPAGE)) {
            return false;
        }
        final EffectInstance newEffect = new EffectInstance(config.getEffect(), config.getDurationTicks(), config.getAmplifier(), false, config.getType().showParticles, config.getType().showIcon);
        player.level.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), ModSounds.RAMPAGE_SFX, SoundCategory.PLAYERS, 0.2f, 1.0f);
        player.playNotifySound(ModSounds.RAMPAGE_SFX, SoundCategory.PLAYERS, 0.2f, 1.0f);
        player.addEffect(newEffect);
        return false;
    }
}
