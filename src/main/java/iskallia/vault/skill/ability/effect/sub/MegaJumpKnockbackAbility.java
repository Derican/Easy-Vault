package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.MegaJumpConfig;
import iskallia.vault.skill.ability.config.sub.MegaJumpKnockbackConfig;
import iskallia.vault.skill.ability.effect.MegaJumpAbility;
import iskallia.vault.util.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;

import java.util.List;

public class MegaJumpKnockbackAbility extends MegaJumpAbility<MegaJumpKnockbackConfig> {
    public boolean onAction(MegaJumpKnockbackConfig config, ServerPlayerEntity player, boolean active) {
        if (super.onAction((MegaJumpConfig) config, player, active)) {

            List<LivingEntity> entities = EntityHelper.getNearby((IWorld) player.getCommandSenderWorld(), (Vector3i) player.blockPosition(), config.getRadius(), LivingEntity.class);
            entities.removeIf(e -> e instanceof net.minecraft.entity.player.PlayerEntity);

            for (LivingEntity entity : entities) {
                double xDiff = player.getX() - entity.getX();
                double zDiff = player.getZ() - entity.getZ();
                if (xDiff * xDiff + zDiff * zDiff < 1.0E-4D) {
                    xDiff = (Math.random() - Math.random()) * 0.01D;
                    zDiff = (Math.random() - Math.random()) * 0.01D;
                }
                entity.knockback(0.4F * config.getKnockbackStrengthMultiplier(), xDiff, zDiff);
            }
            return true;
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\MegaJumpKnockbackAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */