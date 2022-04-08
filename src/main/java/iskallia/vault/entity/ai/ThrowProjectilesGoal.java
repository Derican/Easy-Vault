package iskallia.vault.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

public class ThrowProjectilesGoal<T extends MobEntity>
        extends GoalTask<T> {
    private final int chance;
    private final int count;
    private final Projectile projectile;
    private ItemStack oldStack;
    private int progress;

    public ThrowProjectilesGoal(T entity, int chance, int count, Projectile projectile) {
        super(entity);
        this.chance = chance;
        this.count = count;
        this.projectile = projectile;
    }


    public boolean canUse() {
        return (((MobEntity) getEntity()).getTarget() != null && (getWorld()).random.nextInt(this.chance) == 0);
    }


    public boolean canContinueToUse() {
        return (((MobEntity) getEntity()).getTarget() != null && this.progress < this.count);
    }


    public void start() {
        this.oldStack = ((MobEntity) getEntity()).getItemBySlot(EquipmentSlotType.OFFHAND);
        ((MobEntity) getEntity()).setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack((IItemProvider) Items.SNOWBALL));
    }


    public void tick() {
        if ((getWorld()).random.nextInt(3) == 0) {
            Entity throwEntity = this.projectile.create(getWorld(), (LivingEntity) getEntity());

            LivingEntity target = ((MobEntity) getEntity()).getTarget();

            if (target != null) {
                double d0 = target.getEyeY() - 1.100000023841858D;
                double d1 = target.getX() - ((MobEntity) getEntity()).getX();
                double d2 = d0 - throwEntity.getY();
                double d3 = target.getZ() - ((MobEntity) getEntity()).getZ();
                float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
                shoot(throwEntity, d1, d2 + f, d3, 1.6F, 4.0F, (getWorld()).random);
                getWorld().playSound(null, ((MobEntity) getEntity()).blockPosition(), SoundEvents.SNOW_GOLEM_SHOOT, SoundCategory.HOSTILE, 1.0F, 0.4F / ((getWorld()).random.nextFloat() * 0.4F + 0.8F));
                getWorld().addFreshEntity(throwEntity);
            }

            this.progress++;
        }
    }


    public void shoot(Entity projectile, double x, double y, double z, float velocity, float inaccuracy, Random rand) {
        Vector3d vector3d = (new Vector3d(x, y, z)).normalize().add(rand.nextGaussian() * 0.007499999832361937D * inaccuracy, rand.nextGaussian() * 0.007499999832361937D * inaccuracy, rand.nextGaussian() * 0.007499999832361937D * inaccuracy).scale(velocity);

        projectile.setDeltaMovement(vector3d);
        float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
        projectile.yRot = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * 57.2957763671875D);
        projectile.xRot = (float) (MathHelper.atan2(vector3d.y, f) * 57.2957763671875D);
        projectile.yRotO = projectile.yRot;
        projectile.xRotO = projectile.xRot;
    }


    public void stop() {
        ((MobEntity) getEntity()).setItemSlot(EquipmentSlotType.OFFHAND, this.oldStack);
        this.oldStack = ItemStack.EMPTY;
        this.progress = 0;
    }

    public static interface Projectile {
        Entity create(World param1World, LivingEntity param1LivingEntity);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\ai\ThrowProjectilesGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */