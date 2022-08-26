package iskallia.vault.skill.ability.effect;

import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.config.EffectConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;

public abstract class EffectAbility extends AbilityEffect<EffectConfig> {
    @Override
    public void onRemoved(final EffectConfig config, final PlayerEntity player) {
        player.removeEffect(config.getEffect());
    }

    @Override
    public void onBlur(final EffectConfig config, final PlayerEntity player) {
        player.removeEffect(config.getEffect());
    }

    @Override
    public void onTick(final EffectConfig config, final PlayerEntity player, final boolean active) {
        if (!active) {
            player.removeEffect(config.getEffect());
        } else {
            final EffectInstance activeEffect = player.getEffect(config.getEffect());
            final EffectInstance newEffect = new EffectInstance(config.getEffect(), Integer.MAX_VALUE, config.getAmplifier(), false, config.getType().showParticles, config.getType().showIcon);
            if (activeEffect == null) {
                player.addEffect(newEffect);
            }
        }
    }

    @Override
    public boolean onAction(final EffectConfig config, final ServerPlayerEntity player, final boolean active) {
        if (active) {
            this.playEffects(config, player);
        }
        return true;
    }

    private void playEffects(final EffectConfig config, final PlayerEntity player) {
        if (config.getEffect() == Effects.INVISIBILITY) {
            player.level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.INVISIBILITY_SFX, SoundCategory.MASTER, 0.175f, 1.0f);
            player.playNotifySound(ModSounds.INVISIBILITY_SFX, SoundCategory.MASTER, 0.7f, 1.0f);
        } else if (config.getEffect() == Effects.NIGHT_VISION) {
            player.level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.NIGHT_VISION_SFX, SoundCategory.MASTER, 0.0375f, 1.0f);
            player.playNotifySound(ModSounds.NIGHT_VISION_SFX, SoundCategory.MASTER, 0.15f, 1.0f);
        }
    }
}
