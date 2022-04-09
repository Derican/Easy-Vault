// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

public class VectorHelper
{
    public static Vector3d getDirectionNormalized(final Vector3d destination, final Vector3d origin) {
        return new Vector3d(destination.x - origin.x, destination.y - origin.y, destination.z - origin.z).normalize();
    }
    
    public static Vector3d getVectorFromPos(final BlockPos pos) {
        return new Vector3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }
    
    public static Vector3d add(final Vector3d a, final Vector3d b) {
        return new Vector3d(a.x + b.x, a.y + b.y, a.z + b.z);
    }
    
    public static Vector3d subtract(final Vector3d a, final Vector3d b) {
        return new Vector3d(a.x - b.x, a.y - b.y, a.z - b.z);
    }
    
    public static Vector3d multiply(final Vector3d velocity, final float speed) {
        return new Vector3d(velocity.x * speed, velocity.y * speed, velocity.z * speed);
    }
    
    public static Vector3d getMovementVelocity(final Vector3d current, final Vector3d target, final float speed) {
        return multiply(getDirectionNormalized(target, current), speed);
    }
    
    public static Vector2f normalize(final Vector2f v) {
        final float length = (float)Math.sqrt(v.x * v.x + v.y * v.y);
        return new Vector2f(v.x / length, v.y / length);
    }
    
    public static Vector2f rotateDegrees(final Vector2f v, final float angleDeg) {
        final float angle = (float)Math.toRadians(angleDeg);
        final float cosAngle = MathHelper.cos(angle);
        final float sinAngle = MathHelper.sin(angle);
        return new Vector2f(v.x * cosAngle - v.y * sinAngle, v.x * sinAngle + v.y * cosAngle);
    }
}
