package iskallia.vault.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
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
    protected boolean completed = false;
    protected boolean started = false;
    protected int tick = 0;
    protected int delay = 0;

    protected BlockPos shockwave;
    private final Predicate<LivingEntity> filter;

    public AOEGoal(T entity, Predicate<LivingEntity> filter) {
        super(entity);
        this.filter = filter;
    }


    public boolean canUse() {
        return (getRandom().nextInt(120) == 0 && ((MobEntity) getEntity()).getTarget() != null);
    }


    public boolean canContinueToUse() {
        return !this.completed;
    }


    public void start() {
        ((MobEntity) getEntity()).setDeltaMovement(((MobEntity) getEntity()).getDeltaMovement().add(0.0D, 1.1D, 0.0D));
        this.delay = 5;
    }


    public void tick() {
        if (this.completed) {
            return;
        }

        if (!this.started && this.delay < 0 && ((MobEntity) getEntity()).isOnGround()) {
            getWorld().playSound(null, ((MobEntity) getEntity()).getX(), ((MobEntity) getEntity()).getY(), ((MobEntity) getEntity()).getZ(), SoundEvents.DRAGON_FIREBALL_EXPLODE, ((MobEntity)
                    getEntity()).getSoundSource(), 1.0F, 1.0F);

            ((ServerWorld) getWorld()).sendParticles((IParticleData) ParticleTypes.EXPLOSION, ((MobEntity)
                            getEntity()).getX() + 0.5D, ((MobEntity)
                            getEntity()).getY() + 0.1D, ((MobEntity)
                            getEntity()).getZ() + 0.5D, 10,
                    getRandom().nextGaussian() * 0.02D,
                    getRandom().nextGaussian() * 0.02D,
                    getRandom().nextGaussian() * 0.02D, 1.0D);

            this.shockwave = ((MobEntity) getEntity()).blockPosition();
            this.started = true;
        }


        if (this.started) {
            double max = 50.0D;
            double distance = (this.tick * 2);
            double nextDistance = (this.tick * 2 + 2);

            if (distance >= max) {
                this.completed = true;

                return;
            }
            getWorld().getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(this.shockwave)).inflate(max, max, max), e -> {
                if (e == getEntity() || e.isSpectator() || !this.filter.test(e))
                    return false;
                double d = Math.sqrt(e.blockPosition().distSqr((Vector3i) this.shockwave));
                return (d >= distance && d < nextDistance);
            }).forEach(e -> {
                Vector3d direction = (new Vector3d(e.getX() - this.shockwave.getX(), e.getY() - this.shockwave.getY(), e.getZ() - this.shockwave.getZ())).scale(0.5D);

                direction = direction.normalize().add(0.0D, 1.0D - 0.02D * (this.tick + 1), 0.0D);

                e.setDeltaMovement(e.getDeltaMovement().add(direction));

                e.hurt(DamageSource.GENERIC, 8.0F / (this.tick + 1));
            });

            this.tick++;
        } else {
            this.delay--;
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\ai\AOEGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */