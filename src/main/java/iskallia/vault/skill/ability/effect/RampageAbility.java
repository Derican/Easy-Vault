package iskallia.vault.skill.ability.effect;

import iskallia.vault.init.ModEffects;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.config.RampageConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;

public class RampageAbility<C extends RampageConfig>
        extends AbilityEffect<C> {
    public String getAbilityGroupName() {
        return "Rampage";
    }


    public boolean onAction(C config, ServerPlayerEntity player, boolean active) {
        if (player.hasEffect(ModEffects.RAMPAGE)) {
            return false;
        }


        EffectInstance newEffect = new EffectInstance(config.getEffect(), config.getDurationTicks(), config.getAmplifier(), false, (config.getType()).showParticles, (config.getType()).showIcon);

        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.RAMPAGE_SFX, SoundCategory.PLAYERS, 0.2F, 1.0F);

        player.playNotifySound(ModSounds.RAMPAGE_SFX, SoundCategory.PLAYERS, 0.2F, 1.0F);

        player.addEffect(newEffect);
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\RampageAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */