package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.event.ActiveFlags;
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
    @Override
    public boolean onAction(final DashDamageConfig config, final ServerPlayerEntity player, final boolean active) {
        if (super.onAction(config, player, active)) {
            final List<LivingEntity> other = EntityHelper.getNearby(player.getCommandSenderWorld(), player.blockPosition(), config.getRadiusOfAttack(), LivingEntity.class);
            other.removeIf(e -> e instanceof PlayerEntity);
            final float atk = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            for (final LivingEntity entity : other) {
                ActiveFlags.IS_AOE_ATTACKING.runIfNotSet(() -> entity.hurt(DamageSource.playerAttack(player), atk * config.getAttackDamagePercentPerDash()));
                ServerScheduler.INSTANCE.schedule(0, () -> PlayerDamageHelper.applyMultiplier(player, 0.95f, PlayerDamageHelper.Operation.STACKING_MULTIPLY, true, config.getCooldown()));
            }
            return true;
        }
        return false;
    }
}
