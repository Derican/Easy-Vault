package iskallia.vault.entity.ai;

import iskallia.vault.entity.AggressiveCowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

public class CowDashAttackGoal
        extends Goal {
    protected final AggressiveCowEntity entity;
    private final float dashStrength;

    public CowDashAttackGoal(AggressiveCowEntity entity, float dashStrength) {
        this.entity = entity;
        this.dashStrength = 0.4F;
    }


    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        if (!(target instanceof net.minecraft.entity.player.PlayerEntity) || !target.isAlive()) {
            return false;
        }
        if (!this.entity.canDash()) {
            return false;
        }
        double dist = this.entity.distanceToSqr(target.getX(), target.getY(), target.getZ());
        double attackReach = (this.entity.getBbWidth() * 2.0F * this.entity.getBbWidth() * 2.0F + target.getBbWidth());
        return (dist >= attackReach * 4.0D && dist <= attackReach * 16.0D);
    }


    public boolean canContinueToUse() {
        return false;
    }


    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (!(target instanceof net.minecraft.entity.player.PlayerEntity) || !target.isAlive()) {
            return;
        }
        Vector3d dir = target.getEyePosition(1.0F).subtract(this.entity.position());
        dir = dir.multiply(this.dashStrength, this.dashStrength, this.dashStrength);
        if (dir.y() <= 0.4D) {
            dir = new Vector3d(dir.x(), 0.4D, dir.z());
        }
        this.entity.push(dir.x, dir.y, dir.z);
        this.entity.onDash();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\ai\CowDashAttackGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */