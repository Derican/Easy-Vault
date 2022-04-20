package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.event.ActiveFlags;
import iskallia.vault.skill.ability.config.sub.MegaJumpDamageConfig;
import iskallia.vault.skill.ability.effect.MegaJumpAbility;
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

public class MegaJumpDamageAbility extends MegaJumpAbility<MegaJumpDamageConfig> {
    @Override
    public boolean onAction(final MegaJumpDamageConfig config, final ServerPlayerEntity player, final boolean active) {
        if (super.onAction(config, player, active)) {
            final List<LivingEntity> entities = EntityHelper.getNearby((IWorld) player.getCommandSenderWorld(), (Vector3i) player.blockPosition(), config.getRadius(), LivingEntity.class);
            entities.removeIf(e -> e instanceof PlayerEntity);
            final float atk = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * config.getPercentAttackDamageDealt();
            final DamageSource src = DamageSource.playerAttack((PlayerEntity) player);
            for (final LivingEntity entity : entities) {
                ActiveFlags.IS_AOE_ATTACKING.runIfNotSet(() -> {
                    if (entity.hurt(src, atk)) {
                        double xDiff = player.getX() - entity.getX();
                        double zDiff = player.getZ() - entity.getZ();
                        if (xDiff * xDiff + zDiff * zDiff < 1.0E-4) {
                            xDiff = (Math.random() - Math.random()) * 0.01;
                            zDiff = (Math.random() - Math.random()) * 0.01;
                        }
                        entity.knockback(0.4f * config.getKnockbackStrengthMultiplier(), xDiff, zDiff);
                        ServerScheduler.INSTANCE.schedule(0, () -> PlayerDamageHelper.applyMultiplier(player, 0.95f, PlayerDamageHelper.Operation.STACKING_MULTIPLY, true, config.getCooldown()));
                    }
                    return;
                });
            }
            return true;
        }
        return false;
    }
}
