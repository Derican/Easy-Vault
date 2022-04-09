// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import java.util.Arrays;
import java.util.List;

public class VoxelUtils
{
    public static VoxelShape combineAll(final IBooleanFunction fct, final VoxelShape... shapes) {
        return combineAll(fct, Arrays.asList(shapes));
    }
    
    public static VoxelShape combineAll(final IBooleanFunction fct, final List<VoxelShape> shapes) {
        if (shapes.isEmpty()) {
            return VoxelShapes.empty();
        }
        VoxelShape first = shapes.get(0);
        for (int i = 1; i < shapes.size(); ++i) {
            first = VoxelShapes.joinUnoptimized(first, (VoxelShape)shapes.get(i), fct);
        }
        return first;
    }
}
