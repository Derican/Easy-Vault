package iskallia.vault.entity.ai;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Supplier;

public class FollowEntityGoal<T extends MobEntity, O extends LivingEntity> extends GoalTask<T> {
    private O owner;
    private final double followSpeed;
    private final PathNavigator navigator;
    private int timeToRecalcPath;
    private final float maxDist;
    private final float minDist;
    private float oldWaterCost;
    private final boolean teleportToLeaves;
    private final Supplier<Optional<O>> ownerSupplier;

    public FollowEntityGoal(final T entity, final double speed, final float minDist, final float maxDist, final boolean teleportToLeaves, final Supplier<Optional<O>> ownerSupplier) {
        super(entity);
        this.followSpeed = speed;
        this.navigator = entity.getNavigation();
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.teleportToLeaves = teleportToLeaves;
        this.ownerSupplier = ownerSupplier;
        this.setFlags((EnumSet) EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(this.getEntity().getNavigation() instanceof GroundPathNavigator) && !(this.getEntity().getNavigation() instanceof FlyingPathNavigator)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    public boolean canUse() {
        final O owner = this.ownerSupplier.get().orElse(null);
        if (owner == null) {
            return false;
        }
        if (owner.isSpectator()) {
            return false;
        }
        if (owner.distanceToSqr(this.getEntity()) < this.minDist * this.minDist) {
            return false;
        }
        this.owner = owner;
        return true;
    }

    public boolean canContinueToUse() {
        return !this.navigator.isDone() && this.getEntity().distanceToSqr((Entity) this.owner) > this.maxDist * this.maxDist;
    }

    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.getEntity().getPathfindingMalus(PathNodeType.WATER);
        this.getEntity().setPathfindingMalus(PathNodeType.WATER, 0.0f);
    }

    public void stop() {
        this.owner = null;
        this.navigator.stop();
        this.getEntity().setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
    }

    public void tick() {
        this.getEntity().getLookControl().setLookAt((Entity) this.owner, 10.0f, (float) this.getEntity().getMaxHeadXRot());
        final int timeToRecalcPath = this.timeToRecalcPath - 1;
        this.timeToRecalcPath = timeToRecalcPath;
        if (timeToRecalcPath > 0) {
            return;
        }
        if (!this.getEntity().isLeashed() && !this.getEntity().isPassenger()) {
            if (this.getEntity().distanceToSqr((Entity) this.owner) >= 144.0) {
                this.tryToTeleportNearEntity();
            } else {
                this.navigator.moveTo((Entity) this.owner, this.followSpeed);
            }
        }
        this.timeToRecalcPath = 10;
    }

    private void tryToTeleportNearEntity() {
        final BlockPos blockpos = this.owner.blockPosition();
        for (int i = 0; i < 10; ++i) {
            final int j = this.nextInt(-3, 3);
            final int k = this.nextInt(-1, 1);
            final int l = this.nextInt(-3, 3);
            final boolean flag = this.tryToTeleportToLocation(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
                return;
            }
        }
    }

    private boolean tryToTeleportToLocation(final int x, final int y, final int z) {
        if (Math.abs(x - this.owner.getX()) < 2.0 && Math.abs(z - this.owner.getZ()) < 2.0) {
            return false;
        }
        if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
            return false;
        }
        this.getEntity().moveTo(x + 0.5, (double) y, z + 0.5, this.getEntity().yRot, this.getEntity().xRot);
        this.navigator.stop();
        return true;
    }

    private boolean isTeleportFriendlyBlock(final BlockPos pos) {
        final PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic((IBlockReader) this.getWorld(), pos.mutable());
        if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
        }
        final BlockState blockstate = this.getWorld().getBlockState(pos.below());
        if (!this.teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
            return false;
        }
        final BlockPos blockpos = pos.subtract((Vector3i) this.getEntity().blockPosition());
        return this.getWorld().noCollision(this.getEntity(), this.getEntity().getBoundingBox().move(blockpos));
    }

    private int nextInt(final int min, final int max) {
        return this.getWorld().getRandom().nextInt(max - min + 1) + min;
    }
}
