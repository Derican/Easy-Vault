package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.event.ActiveFlags;
import iskallia.vault.skill.ability.config.MegaJumpConfig;
import iskallia.vault.skill.ability.config.sub.MegaJumpDamageConfig;
import iskallia.vault.skill.ability.effect.MegaJumpAbility;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.util.ServerScheduler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IWorld;

import java.util.Iterator;
import java.util.List;

public class MegaJumpDamageAbility extends MegaJumpAbility<MegaJumpDamageConfig> {
    public boolean onAction(MegaJumpDamageConfig config, ServerPlayerEntity player, boolean active) {
        if (super.onAction((MegaJumpConfig) config, player, active)) {

            List<LivingEntity> entities = EntityHelper.getNearby((IWorld) player.getCommandSenderWorld(), (Vector3i) player.blockPosition(), config.getRadius(), LivingEntity.class);
            entities.removeIf(e -> e instanceof PlayerEntity);

            float atk = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * config.getPercentAttackDamageDealt();
            DamageSource src = DamageSource.playerAttack((PlayerEntity) player);
            for (Iterator<LivingEntity> iterator = entities.iterator(); iterator.hasNext(); ) {
                LivingEntity entity = iterator.next();
                ActiveFlags.IS_AOE_ATTACKING.runIfNotSet(() -> {
                    if (entity.hurt(src, atk)) {
                        double xDiff = player.getX() - entity.getX();

                        double zDiff = player.getZ() - entity.getZ();

                        if (xDiff * xDiff + zDiff * zDiff < 1.0E-4D) {
                            xDiff = (Math.random() - Math.random()) * 0.01D;

                            zDiff = (Math.random() - Math.random()) * 0.01D;
                        }

                        entity.knockback(0.4F * config.getKnockbackStrengthMultiplier(), xDiff, zDiff);
                        ServerScheduler.INSTANCE.schedule(0, ());
                    }
                });
            }

            return true;
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\MegaJumpDamageAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */