package iskallia.vault.util;

import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import java.util.Arrays;
import java.util.List;


public class VoxelUtils {
    public static VoxelShape combineAll(IBooleanFunction fct, VoxelShape... shapes) {
        return combineAll(fct, Arrays.asList(shapes));
    }

    public static VoxelShape combineAll(IBooleanFunction fct, List<VoxelShape> shapes) {
        if (shapes.isEmpty()) {
            return VoxelShapes.empty();
        }
        VoxelShape first = shapes.get(0);
        for (int i = 1; i < shapes.size(); i++) {
            first = VoxelShapes.joinUnoptimized(first, shapes.get(i), fct);
        }
        return first;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\VoxelUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */