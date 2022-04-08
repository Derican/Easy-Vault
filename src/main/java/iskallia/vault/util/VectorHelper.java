package iskallia.vault.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

public class VectorHelper {
    public static Vector3d getDirectionNormalized(Vector3d destination, Vector3d origin) {
        return (new Vector3d(destination.x - origin.x, destination.y - origin.y, destination.z - origin.z)).normalize();
    }

    public static Vector3d getVectorFromPos(BlockPos pos) {
        return new Vector3d(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vector3d add(Vector3d a, Vector3d b) {
        return new Vector3d(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vector3d subtract(Vector3d a, Vector3d b) {
        return new Vector3d(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vector3d multiply(Vector3d velocity, float speed) {
        return new Vector3d(velocity.x * speed, velocity.y * speed, velocity.z * speed);
    }

    public static Vector3d getMovementVelocity(Vector3d current, Vector3d target, float speed) {
        return multiply(getDirectionNormalized(target, current), speed);
    }

    public static Vector2f normalize(Vector2f v) {
        float length = (float) Math.sqrt((v.x * v.x + v.y * v.y));
        return new Vector2f(v.x / length, v.y / length);
    }

    public static Vector2f rotateDegrees(Vector2f v, float angleDeg) {
        float angle = (float) Math.toRadians(angleDeg);

        float cosAngle = MathHelper.cos(angle);
        float sinAngle = MathHelper.sin(angle);

        return new Vector2f(v.x * cosAngle - v.y * sinAngle, v.x * sinAngle + v.y * cosAngle);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\VectorHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */