package iskallia.vault.skill.ability.effect;

import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.config.CleanseConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;

import java.util.List;
import java.util.stream.Collectors;

public class CleanseAbility<C extends CleanseConfig>
        extends AbilityEffect<C> {
    public String getAbilityGroupName() {
        return "Cleanse";
    }


    public boolean onAction(C config, ServerPlayerEntity player, boolean active) {
        List<EffectInstance> effects = (List<EffectInstance>) player.getActiveEffects().stream().filter(effect -> !effect.getEffect().isBeneficial()).collect(Collectors.toList());

        removeEffects(config, player, effects);

        player.level.playSound((PlayerEntity) player, player.getX(), player.getY(), player.getZ(), ModSounds.CLEANSE_SFX, SoundCategory.PLAYERS, 0.2F, 1.0F);

        player.playNotifySound(ModSounds.CLEANSE_SFX, SoundCategory.PLAYERS, 0.2F, 1.0F);

        return true;
    }

    protected void removeEffects(C config, ServerPlayerEntity player, List<EffectInstance> effects) {
        effects.forEach(effect -> player.removeEffect(effect.getEffect()));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\CleanseAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */