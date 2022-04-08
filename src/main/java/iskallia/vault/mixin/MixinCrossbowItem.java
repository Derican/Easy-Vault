package iskallia.vault.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin({CrossbowItem.class})
public class MixinCrossbowItem {
    @Redirect(method = {"fireProjectile"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addEntity(Lnet/minecraft/entity/Entity;)Z"))
    private static boolean applyEntityDamage(World world, Entity entity) {
        if (entity instanceof AbstractArrowEntity) {
            Entity shooter = ((AbstractArrowEntity) entity).getOwner();
            if (shooter instanceof LivingEntity && !(shooter instanceof net.minecraft.entity.player.PlayerEntity)) {
                AttributeModifierManager mgr = ((LivingEntity) shooter).getAttributes();
                if (mgr.hasAttribute(Attributes.ATTACK_DAMAGE)) {
                    ((AbstractArrowEntity) entity).setBaseDamage(mgr.getValue(Attributes.ATTACK_DAMAGE));
                }
            }
        }
        return world.addFreshEntity(entity);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinCrossbowItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */