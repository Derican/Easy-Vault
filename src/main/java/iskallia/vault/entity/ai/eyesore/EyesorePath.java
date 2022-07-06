package iskallia.vault.entity.ai.eyesore;

import iskallia.vault.entity.EyesoreEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

public class EyesorePath
{
    public Vector3d target;
    public double speed;
    
    public void stayInRange(final Vector3d center, final Entity other, final double speed, final double distance, final double error) {
        this.speed = speed;
        if (this.target == null || Math.abs(this.target.distanceTo(other.position()) - distance) > error) {
            final Vector3d dir = center.subtract(other.position());
            this.target = dir.normalize().scale(distance).add(other.position());
        }
        else {
            this.target = null;
        }
    }
    
    public void stayInRange(final EyesoreEntity entity, final Entity other, final double speed, final double distance, final double error) {
        this.speed = speed;
        if (this.target == null || Math.abs(this.target.distanceTo(other.position()) - distance) > error) {
            final Vector3d dir = entity.position().subtract(other.position());
            this.target = dir.normalize().scale(distance).add(other.position());
        }
        else {
            this.target = null;
        }
    }
    
    public void tick(final EyesoreEntity entity) {
        if (this.target == null) {
            return;
        }
        if (this.target.distanceTo(entity.position()) <= 1.0) {
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.1));
        }
        else {
            entity.setDeltaMovement(this.target.subtract(entity.position()).normalize().scale(this.speed));
        }
    }
}
