package iskallia.vault.entity.ai;

import iskallia.vault.entity.AggressiveCowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class CowDashAttackGoal extends Goal {
    protected final AggressiveCowEntity entity;
    private final float dashStrength;

    public CowDashAttackGoal(final AggressiveCowEntity entity, final float dashStrength) {
        this.entity = entity;
        this.dashStrength = 0.4f;
    }

    public boolean canUse() {
        final LivingEntity target = this.entity.getTarget();
        if (!(target instanceof PlayerEntity) || !target.isAlive()) {
            return false;
        }
        if (!this.entity.canDash()) {
            return false;
        }
        final double dist = this.entity.distanceToSqr(target.getX(), target.getY(), target.getZ());
        final double attackReach = this.entity.getBbWidth() * 2.0f * this.entity.getBbWidth() * 2.0f + target.getBbWidth();
        return dist >= attackReach * 4.0 && dist <= attackReach * 16.0;
    }

    public boolean canContinueToUse() {
        return false;
    }

    public void tick() {
        final LivingEntity target = this.entity.getTarget();
        if (!(target instanceof PlayerEntity) || !target.isAlive()) {
            return;
        }
        Vector3d dir = target.getEyePosition(1.0f).subtract(this.entity.position());
        dir = dir.multiply(this.dashStrength, this.dashStrength, this.dashStrength);
        if (dir.y() <= 0.4) {
            dir = new Vector3d(dir.x(), 0.4, dir.z());
        }
        this.entity.push(dir.x, dir.y, dir.z);
        this.entity.onDash();
    }
}
