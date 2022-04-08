package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.CleanseConfig;
import iskallia.vault.skill.ability.config.sub.CleanseApplyConfig;
import iskallia.vault.skill.ability.effect.CleanseAbility;
import iskallia.vault.util.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;

import java.util.List;

public class CleanseApplyAbility
        extends CleanseAbility<CleanseApplyConfig> {
    protected void removeEffects(CleanseApplyConfig config, ServerPlayerEntity player, List<EffectInstance> effects) {
        super.removeEffects((CleanseConfig) config, player, effects);

        for (EffectInstance effect : effects) {
            List<LivingEntity> other = EntityHelper.getNearby((IWorld) player.getCommandSenderWorld(), (Vector3i) player.blockPosition(), config.getApplyRadius(), LivingEntity.class);
            other.removeIf(e -> e instanceof net.minecraft.entity.player.PlayerEntity);

            if (!other.isEmpty()) {
                LivingEntity e = other.get(rand.nextInt(other.size()));
                e.addEffect(effect);
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\CleanseApplyAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */