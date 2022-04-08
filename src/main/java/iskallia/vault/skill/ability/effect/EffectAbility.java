package iskallia.vault.skill.ability.effect;

import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.config.EffectConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;

public abstract class EffectAbility
        extends AbilityEffect<EffectConfig> {
    public void onRemoved(EffectConfig config, PlayerEntity player) {
        player.removeEffect(config.getEffect());
    }


    public void onBlur(EffectConfig config, PlayerEntity player) {
        player.removeEffect(config.getEffect());
    }


    public void onTick(EffectConfig config, PlayerEntity player, boolean active) {
        if (!active) {
            player.removeEffect(config.getEffect());
        } else {
            EffectInstance activeEffect = player.getEffect(config.getEffect());

            EffectInstance newEffect = new EffectInstance(config.getEffect(), 2147483647, config.getAmplifier(), false, (config.getType()).showParticles, (config.getType()).showIcon);

            if (activeEffect == null) {
                player.addEffect(newEffect);
            }
        }
    }


    public boolean onAction(EffectConfig config, ServerPlayerEntity player, boolean active) {
        if (active) {
            playEffects(config, (PlayerEntity) player);
        }
        return true;
    }

    private void playEffects(EffectConfig config, PlayerEntity player) {
        if (config.getEffect() == Effects.INVISIBILITY) {
            player.level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.INVISIBILITY_SFX, SoundCategory.MASTER, 0.175F, 1.0F);

            player.playNotifySound(ModSounds.INVISIBILITY_SFX, SoundCategory.MASTER, 0.7F, 1.0F);
        } else if (config.getEffect() == Effects.NIGHT_VISION) {
            player.level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.NIGHT_VISION_SFX, SoundCategory.MASTER, 0.0375F, 1.0F);

            player.playNotifySound(ModSounds.NIGHT_VISION_SFX, SoundCategory.MASTER, 0.15F, 1.0F);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\EffectAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */