package iskallia.vault.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Predicate;

public class AOEGoal<T extends MobEntity> extends GoalTask<T> {
    protected boolean completed;
    protected boolean started;
    protected int tick;
    protected int delay;
    protected BlockPos shockwave;
    private final Predicate<LivingEntity> filter;

    public AOEGoal(final T entity, final Predicate<LivingEntity> filter) {
        super(entity);
        this.completed = false;
        this.started = false;
        this.tick = 0;
        this.delay = 0;
        this.filter = filter;
    }

    public boolean canUse() {
        return this.getRandom().nextInt(120) == 0 && this.getEntity().getTarget() != null;
    }

    public boolean canContinueToUse() {
        return !this.completed;
    }

    public void start() {
        this.getEntity().setDeltaMovement(this.getEntity().getDeltaMovement().add(0.0, 1.1, 0.0));
        this.delay = 5;
    }

    public void tick() {
        if (this.completed) {
            return;
        }
        if (!this.started && this.delay < 0 && this.getEntity().isOnGround()) {
            this.getWorld().playSound((PlayerEntity) null, this.getEntity().getX(), this.getEntity().getY(), this.getEntity().getZ(), SoundEvents.DRAGON_FIREBALL_EXPLODE, this.getEntity().getSoundSource(), 1.0f, 1.0f);
            ((ServerWorld) this.getWorld()).sendParticles((IParticleData) ParticleTypes.EXPLOSION, this.getEntity().getX() + 0.5, this.getEntity().getY() + 0.1, this.getEntity().getZ() + 0.5, 10, this.getRandom().nextGaussian() * 0.02, this.getRandom().nextGaussian() * 0.02, this.getRandom().nextGaussian() * 0.02, 1.0);
            this.shockwave = this.getEntity().blockPosition();
            this.started = true;
        }
        if (this.started) {
            final double max = 50.0;
            final double distance = this.tick * 2;
            final double nextDistance = this.tick * 2 + 2;
            if (distance >= max) {
                this.completed = true;
                return;
            }
            this.getWorld().getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(this.shockwave).inflate(max, max, max), e -> {
                if (e == this.getEntity() || e.isSpectator() || !this.filter.test(e)) {
                    return false;
                } else {
                    final double d = Math.sqrt(e.blockPosition().distSqr((Vector3i) this.shockwave));
                    return d >= distance && d < nextDistance;
                }
            }).forEach(e -> {
                final Vector3d direction = new Vector3d(e.getX() - this.shockwave.getX(), e.getY() - this.shockwave.getY(), e.getZ() - this.shockwave.getZ()).scale(0.5);
                final Vector3d direction2 = direction.normalize().add(0.0, 1.0 - 0.02 * (this.tick + 1), 0.0);
                e.setDeltaMovement(e.getDeltaMovement().add(direction2));
                e.hurt(DamageSource.GENERIC, 8.0f / (this.tick + 1));
                return;
            });
            ++this.tick;
        } else {
            --this.delay;
        }
    }

    public void stop() {
        this.completed = false;
        this.started = false;
        this.tick = 0;
        this.delay = 0;
        this.shockwave = null;
    }
}
