package iskallia.vault.skill.ability.effect.sub;

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
    @Override
    protected void removeEffects(final CleanseEffectConfig config, final ServerPlayerEntity player, final List<EffectInstance> effects) {
        super.removeEffects(config, player, effects);
        final List<String> addEffects = config.getPossibleEffects();
        if (!addEffects.isEmpty()) {
            for (final EffectInstance ignored : effects) {
                final String effectStr = addEffects.get(CleanseEffectAbility.rand.nextInt(addEffects.size()));
                Registry.MOB_EFFECT.getOptional(new ResourceLocation(effectStr)).ifPresent(effect -> {
                    final EffectTalent.CombinedEffects grantedEffects = EffectTalent.getEffectData(player, player.getLevel(), effect);
                    if (grantedEffects.getDisplayEffect() != null && grantedEffects.getAmplifier() >= 0) {
                        final EffectTalent.Type type = grantedEffects.getDisplayEffect().getType();
                        new EffectInstance(effect, 600, grantedEffects.getAmplifier() + config.getEffectAmplifier() + 1, false, type.showParticles, type.showIcon);
                    } else {
                        player.addEffect(new EffectInstance(effect, 600, config.getEffectAmplifier(), false, false, true));
                    }
                });
            }
        }
    }
}
