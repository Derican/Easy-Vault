// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Random;

public class MathUtilities
{
    private static final Random rand;
    
    public static float randomFloat(final float min, final float max) {
        if (min >= max) {
            return min;
        }
        return min + MathUtilities.rand.nextFloat() * (max - min);
    }
    
    public static int getRandomInt(final int min, final int max) {
        if (min >= max) {
            return min;
        }
        return min + MathUtilities.rand.nextInt(max - min);
    }
    
    public static double map(final double value, final double x0, final double y0, final double x1, final double y1) {
        return x1 + (y1 - x1) * ((value - x0) / (y0 - x0));
    }
    
    public static double length(final Vector2f vec) {
        return Math.sqrt(vec.x * vec.x + vec.y * vec.y);
    }
    
    public static double extractYaw(final Vector3d vec) {
        return Math.atan2(vec.z(), vec.x());
    }
    
    public static double extractPitch(final Vector3d vec) {
        return Math.asin(vec.y() / vec.length());
    }
    
    public static Vector3d rotatePitch(final Vector3d vec, final float pitch) {
        final float f = MathHelper.cos(pitch);
        final float f2 = MathHelper.sin(pitch);
        final double d0 = vec.x();
        final double d2 = vec.y() * f + vec.z() * f2;
        final double d3 = vec.z() * f - vec.y() * f2;
        return new Vector3d(d0, d2, d3);
    }
    
    public static Vector3d rotateYaw(final Vector3d vec, final float yaw) {
        final float f = MathHelper.cos(yaw);
        final float f2 = MathHelper.sin(yaw);
        final double d0 = vec.x() * f + vec.z() * f2;
        final double d2 = vec.y();
        final double d3 = vec.z() * f - vec.x() * f2;
        return new Vector3d(d0, d2, d3);
    }
    
    public static Vector3d rotateRoll(final Vector3d vec, final float roll) {
        final float f = MathHelper.cos(roll);
        final float f2 = MathHelper.sin(roll);
        final double d0 = vec.x() * f + vec.y() * f2;
        final double d2 = vec.y() * f - vec.x() * f2;
        final double d3 = vec.z();
        return new Vector3d(d0, d2, d3);
    }
    
    static {
        rand = new Random();
    }
}
