// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.gen.layout;

import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.Direction;

public class SpiralHelper
{
    public static Vector3i getSpiralPosition(final int index, final Direction facing, final Direction rotation) {
        final int k = (int)Math.ceil((Math.sqrt(index + 1) - 1.0) / 2.0);
        final int a = 2 * k;
        int b = (a + 1) * (a + 1);
        int x;
        int y;
        if (index + 1 >= b - a) {
            x = k - (b - index - 1);
            y = -k;
        }
        else {
            b -= a;
            if (index + 1 >= b - a) {
                x = -k;
                y = -k + (b - index - 1);
            }
            else {
                b -= a;
                if (index + 1 >= b - a) {
                    x = -k + (b - index - 1);
                    y = k;
                }
                else {
                    x = k;
                    y = k - (b - index - a - 1);
                }
            }
        }
        if (facing == Direction.EAST) {
            if (rotation == Direction.SOUTH) {
                y *= -1;
            }
        }
        else if (facing == Direction.WEST) {
            x *= -1;
            if (rotation == Direction.SOUTH) {
                y *= -1;
            }
        }
        else if (facing == Direction.NORTH) {
            final int temp = x;
            x = y;
            y = -temp;
            if (rotation == Direction.WEST) {
                x *= -1;
            }
        }
        else {
            if (facing != Direction.SOUTH) {
                return Vector3i.ZERO;
            }
            final int temp = x;
            x = y;
            y = temp;
            if (rotation == Direction.WEST) {
                x *= -1;
            }
        }
        return new Vector3i(x, 0, y);
    }
}
