package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.CleanseConfig;
import iskallia.vault.skill.ability.config.sub.CleanseEffectConfig;
import iskallia.vault.skill.ability.effect.CleanseAbility;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class CleanseEffectAbility extends CleanseAbility<CleanseEffectConfig> {
    protected void removeEffects(CleanseEffectConfig config, ServerPlayerEntity player, List<EffectInstance> effects) {
        super.removeEffects((CleanseConfig) config, player, effects);

        List<String> addEffects = config.getPossibleEffects();
        if (!addEffects.isEmpty())
            for (EffectInstance ignored : effects) {
                String effectStr = addEffects.get(rand.nextInt(addEffects.size()));
                Registry.MOB_EFFECT.getOptional(new ResourceLocation(effectStr)).ifPresent(effect -> {
                    EffectTalent.CombinedEffects grantedEffects = EffectTalent.getEffectData((PlayerEntity) player, player.getLevel(), effect);
                    if (grantedEffects.getDisplayEffect() != null && grantedEffects.getAmplifier() >= 0) {
                        EffectTalent.Type type = grantedEffects.getDisplayEffect().getType();
                        new EffectInstance(effect, 600, grantedEffects.getAmplifier() + config.getEffectAmplifier() + 1, false, type.showParticles, type.showIcon);
                    } else {
                        player.addEffect(new EffectInstance(effect, 600, config.getEffectAmplifier(), false, false, true));
                    }
                });
            }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\CleanseEffectAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */