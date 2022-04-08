package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.event.ActiveFlags;
import iskallia.vault.skill.ability.config.DashConfig;
import iskallia.vault.skill.ability.config.sub.DashDamageConfig;
import iskallia.vault.skill.ability.effect.DashAbility;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.util.PlayerDamageHelper;
import iskallia.vault.util.ServerScheduler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;

import java.util.List;

public class DashDamageAbility extends DashAbility<DashDamageConfig> {
    public boolean onAction(DashDamageConfig config, ServerPlayerEntity player, boolean active) {
        if (super.onAction((DashConfig) config, player, active)) {
            List<LivingEntity> other = EntityHelper.getNearby((IWorld) player.getCommandSenderWorld(), (Vector3i) player.blockPosition(), config.getRadiusOfAttack(), LivingEntity.class);
            other.removeIf(e -> e instanceof PlayerEntity);

            float atk = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            for (LivingEntity entity : other) {
                ActiveFlags.IS_AOE_ATTACKING.runIfNotSet(() -> entity.hurt(DamageSource.playerAttack((PlayerEntity) player), atk * config.getAttackDamagePercentPerDash()));


                ServerScheduler.INSTANCE.schedule(0, () -> PlayerDamageHelper.applyMultiplier(player, 0.95F, PlayerDamageHelper.Operation.STACKING_MULTIPLY, true, config.getCooldown()));
            }


            return true;
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\DashDamageAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */