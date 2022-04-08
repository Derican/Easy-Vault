package iskallia.vault.entity.ai;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Supplier;


public class FollowEntityGoal<T extends MobEntity, O extends LivingEntity>
        extends GoalTask<T> {
    private O owner;
    private final double followSpeed;
    private final PathNavigator navigator;
    private int timeToRecalcPath;

    public FollowEntityGoal(T entity, double speed, float minDist, float maxDist, boolean teleportToLeaves, Supplier<Optional<O>> ownerSupplier) {
        super(entity);
        this.followSpeed = speed;
        this.navigator = entity.getNavigation();
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.teleportToLeaves = teleportToLeaves;
        this.ownerSupplier = ownerSupplier;

        setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));

        if (!(((MobEntity) getEntity()).getNavigation() instanceof net.minecraft.pathfinding.GroundPathNavigator) && !(((MobEntity) getEntity()).getNavigation() instanceof net.minecraft.pathfinding.FlyingPathNavigator))
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
    }

    private final float maxDist;
    private final float minDist;
    private float oldWaterCost;
    private final boolean teleportToLeaves;
    private final Supplier<Optional<O>> ownerSupplier;

    public boolean canUse() {
        LivingEntity livingEntity = ((Optional<LivingEntity>) this.ownerSupplier.get()).orElse(null);

        if (livingEntity == null)
            return false;
        if (livingEntity.isSpectator())
            return false;
        if (livingEntity.distanceToSqr((Entity) getEntity()) < (this.minDist * this.minDist)) {
            return false;
        }
        this.owner = (O) livingEntity;
        return true;
    }


    public boolean canContinueToUse() {
        if (this.navigator.isDone()) {
            return false;
        }

        return (((MobEntity) getEntity()).distanceToSqr((Entity) this.owner) > (this.maxDist * this.maxDist));
    }

    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = ((MobEntity) getEntity()).getPathfindingMalus(PathNodeType.WATER);
        ((MobEntity) getEntity()).setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    public void stop() {
        this.owner = null;
        this.navigator.stop();
        ((MobEntity) getEntity()).setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
    }

    public void tick() {
        ((MobEntity) getEntity()).getLookControl().setLookAt((Entity) this.owner, 10.0F, ((MobEntity) getEntity()).getMaxHeadXRot());
        if (--this.timeToRecalcPath > 0)
            return;
        if (!((MobEntity) getEntity()).isLeashed() && !((MobEntity) getEntity()).isPassenger()) {
            if (((MobEntity) getEntity()).distanceToSqr((Entity) this.owner) >= 144.0D) {
                tryToTeleportNearEntity();
            } else {
                this.navigator.moveTo((Entity) this.owner, this.followSpeed);
            }
        }

        this.timeToRecalcPath = 10;
    }

    private void tryToTeleportNearEntity() {
        BlockPos blockpos = this.owner.blockPosition();

        for (int i = 0; i < 10; i++) {
            int j = nextInt(-3, 3);
            int k = nextInt(-1, 1);
            int l = nextInt(-3, 3);
            boolean flag = tryToTeleportToLocation(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
                return;
            }
        }
    }

    private boolean tryToTeleportToLocation(int x, int y, int z) {
        if (Math.abs(x - this.owner.getX()) < 2.0D && Math.abs(z - this.owner.getZ()) < 2.0D)
            return false;
        if (!isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
            return false;
        }
        ((MobEntity) getEntity()).moveTo(x + 0.5D, y, z + 0.5D, ((MobEntity) getEntity()).yRot, ((MobEntity) getEntity()).xRot);
        this.navigator.stop();
        return true;
    }


    private boolean isTeleportFriendlyBlock(BlockPos pos) {
        PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic((IBlockReader) getWorld(), pos.mutable());

        if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
        }
        BlockState blockstate = getWorld().getBlockState(pos.below());
        if (!this.teleportToLeaves && blockstate.getBlock() instanceof net.minecraft.block.LeavesBlock) {
            return false;
        }
        BlockPos blockpos = pos.subtract((Vector3i) ((MobEntity) getEntity()).blockPosition());
        return getWorld().noCollision((Entity) getEntity(), ((MobEntity) getEntity()).getBoundingBox().move(blockpos));
    }


    private int nextInt(int min, int max) {
        return getWorld().getRandom().nextInt(max - min + 1) + min;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\ai\FollowEntityGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */