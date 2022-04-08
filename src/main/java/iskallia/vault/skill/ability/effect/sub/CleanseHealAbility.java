package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.CleanseConfig;
import iskallia.vault.skill.ability.config.sub.CleanseHealConfig;
import iskallia.vault.skill.ability.effect.CleanseAbility;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;

import java.util.List;

public class CleanseHealAbility
        extends CleanseAbility<CleanseHealConfig> {
    protected void removeEffects(CleanseHealConfig config, ServerPlayerEntity player, List<EffectInstance> effects) {
        super.removeEffects((CleanseConfig) config, player, effects);

        for (EffectInstance ignored : effects)
            player.heal(config.getHealthPerEffectRemoved());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\CleanseHealAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */