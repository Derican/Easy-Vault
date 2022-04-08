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
    @Redirect(method = {"attackEntityWithRangedAttack"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addEntity(Lnet/minecraft/entity/Entity;)Z"))
    public boolean applySkeletonDamage(World world, Entity entityIn) {
        AbstractSkeletonEntity shooter = (AbstractSkeletonEntity) this;
        AbstractArrowEntity shot = (AbstractArrowEntity) entityIn;

        double dmg = shooter.getAttributeValue(Attributes.ATTACK_DAMAGE);
        shot.setBaseDamage(dmg + 1.0D + shooter.getCommandSenderWorld().getDifficulty().getId() * 0.11D);
        int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, (LivingEntity) shooter);
        if (power > 0) {
            shot.setBaseDamage(shot.getBaseDamage() + (power + 1) * 0.5D);
        }

        return world.addFreshEntity(entityIn);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinAbstractSkeletonEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */