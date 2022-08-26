package iskallia.vault.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
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

public class ThrowProjectilesGoal<T extends MobEntity> extends GoalTask<T> {
    private final int chance;
    private final int count;
    private final Projectile projectile;
    private ItemStack oldStack;
    private int progress;

    public ThrowProjectilesGoal(final T entity, final int chance, final int count, final Projectile projectile) {
        super(entity);
        this.chance = chance;
        this.count = count;
        this.projectile = projectile;
    }

    public boolean canUse() {
        return this.getEntity().getTarget() != null && this.getWorld().random.nextInt(this.chance) == 0;
    }

    public boolean canContinueToUse() {
        return this.getEntity().getTarget() != null && this.progress < this.count;
    }

    public void start() {
        this.oldStack = this.getEntity().getItemBySlot(EquipmentSlotType.OFFHAND);
        this.getEntity().setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.SNOWBALL));
    }

    public void tick() {
        if (this.getWorld().random.nextInt(3) == 0) {
            final Entity throwEntity = this.projectile.create(this.getWorld(), this.getEntity());
            final LivingEntity target = this.getEntity().getTarget();
            if (target != null) {
                final double d0 = target.getEyeY() - 1.100000023841858;
                final double d2 = target.getX() - this.getEntity().getX();
                final double d3 = d0 - throwEntity.getY();
                final double d4 = target.getZ() - this.getEntity().getZ();
                final float f = MathHelper.sqrt(d2 * d2 + d4 * d4) * 0.2f;
                this.shoot(throwEntity, d2, d3 + f, d4, 1.6f, 4.0f, this.getWorld().random);
                this.getWorld().playSound(null, this.getEntity().blockPosition(), SoundEvents.SNOW_GOLEM_SHOOT, SoundCategory.HOSTILE, 1.0f, 0.4f / (this.getWorld().random.nextFloat() * 0.4f + 0.8f));
                this.getWorld().addFreshEntity(throwEntity);
            }
            ++this.progress;
        }
    }

    public void shoot(final Entity projectile, final double x, final double y, final double z, final float velocity, final float inaccuracy, final Random rand) {
        final Vector3d vector3d = new Vector3d(x, y, z).normalize().add(rand.nextGaussian() * 0.007499999832361937 * inaccuracy, rand.nextGaussian() * 0.007499999832361937 * inaccuracy, rand.nextGaussian() * 0.007499999832361937 * inaccuracy).scale(velocity);
        projectile.setDeltaMovement(vector3d);
        final float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
        projectile.yRot = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * 57.2957763671875);
        projectile.xRot = (float) (MathHelper.atan2(vector3d.y, f) * 57.2957763671875);
        projectile.yRotO = projectile.yRot;
        projectile.xRotO = projectile.xRot;
    }

    public void stop() {
        this.getEntity().setItemSlot(EquipmentSlotType.OFFHAND, this.oldStack);
        this.oldStack = ItemStack.EMPTY;
        this.progress = 0;
    }

    public interface Projectile {
        Entity create(final World p0, final LivingEntity p1);
    }
}
