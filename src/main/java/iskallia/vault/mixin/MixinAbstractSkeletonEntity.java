package iskallia.vault.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({AbstractSkeletonEntity.class})
public class MixinAbstractSkeletonEntity {
    @Redirect(method = {"performRangedAttack"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addFreshEntity(Lnet/minecraft/entity/Entity;)Z"))
    public boolean applySkeletonDamage(final World world, final Entity entityIn) {
        final AbstractSkeletonEntity shooter = (AbstractSkeletonEntity) (Object) this;
        final AbstractArrowEntity shot = (AbstractArrowEntity) entityIn;
        final double dmg = shooter.getAttributeValue(Attributes.ATTACK_DAMAGE);
        shot.setBaseDamage(dmg + 1.0 + shooter.getCommandSenderWorld().getDifficulty().getId() * 0.11);
        final int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, (LivingEntity) shooter);
        if (power > 0) {
            shot.setBaseDamage(shot.getBaseDamage() + (power + 1) * 0.5);
        }
        return world.addFreshEntity(entityIn);
    }
}
