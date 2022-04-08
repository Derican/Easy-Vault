package iskallia.vault.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Random;


public class MathUtilities {
    private static final Random rand = new Random();


    public static float randomFloat(float min, float max) {
        if (min >= max) return min;
        return min + rand.nextFloat() * (max - min);
    }


    public static int getRandomInt(int min, int max) {
        if (min >= max) return min;
        return min + rand.nextInt(max - min);
    }

    public static double map(double value, double x0, double y0, double x1, double y1) {
        return x1 + (y1 - x1) * (value - x0) / (y0 - x0);
    }

    public static double length(Vector2f vec) {
        return Math.sqrt((vec.x * vec.x + vec.y * vec.y));
    }

    public static double extractYaw(Vector3d vec) {
        return Math.atan2(vec.z(), vec.x());
    }

    public static double extractPitch(Vector3d vec) {
        return Math.asin(vec.y() / vec.length());
    }

    public static Vector3d rotatePitch(Vector3d vec, float pitch) {
        float f = MathHelper.cos(pitch);
        float f1 = MathHelper.sin(pitch);
        double d0 = vec.x();
        double d1 = vec.y() * f + vec.z() * f1;
        double d2 = vec.z() * f - vec.y() * f1;
        return new Vector3d(d0, d1, d2);
    }

    public static Vector3d rotateYaw(Vector3d vec, float yaw) {
        float f = MathHelper.cos(yaw);
        float f1 = MathHelper.sin(yaw);
        double d0 = vec.x() * f + vec.z() * f1;
        double d1 = vec.y();
        double d2 = vec.z() * f - vec.x() * f1;
        return new Vector3d(d0, d1, d2);
    }

    public static Vector3d rotateRoll(Vector3d vec, float roll) {
        float f = MathHelper.cos(roll);
        float f1 = MathHelper.sin(roll);
        double d0 = vec.x() * f + vec.y() * f1;
        double d1 = vec.y() * f - vec.x() * f1;
        double d2 = vec.z();
        return new Vector3d(d0, d1, d2);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\MathUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */