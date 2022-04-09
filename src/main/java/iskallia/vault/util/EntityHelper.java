// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;

import java.util.List;

public class EntityHelper
{
    private static final AxisAlignedBB BOX;
    
    public static void changeHealth(final LivingEntity entity, final int healthChange) {
        final float health = entity.getHealth();
        entity.setHealth(health + healthChange);
        if (entity.isDeadOrDying()) {
            entity.die((entity.getLastDamageSource() != null) ? entity.getLastDamageSource() : DamageSource.GENERIC);
        }
    }
    
    public static <T extends Entity> T changeSize(final T entity, final float size, final Runnable callback) {
        changeSize(entity, size);
        callback.run();
        return entity;
    }
    
    public static <T extends Entity> T changeSize(final T entity, final float size) {
//        entity.dimensions = entity.getDimensions(Pose.STANDING).scale(size);
        entity.refreshDimensions();
        return entity;
    }
    
    public static void giveItem(final PlayerEntity player, final ItemStack itemStack) {
        final boolean added = player.inventory.add(itemStack);
        if (!added) {
            player.drop(itemStack, false, false);
        }
    }
    
    public static <T extends Entity> List<T> getNearby(final IWorld world, final Vector3i pos, final float radius, final Class<T> entityClass) {
        final AxisAlignedBB selectBox = EntityHelper.BOX.move((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()).inflate((double)radius);
        return world.getEntitiesOfClass(entityClass, selectBox, entity -> entity.isAlive() && !entity.isSpectator());
    }
    
    static {
        BOX = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    }
}
