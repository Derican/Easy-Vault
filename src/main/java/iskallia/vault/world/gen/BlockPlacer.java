package iskallia.vault.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

@FunctionalInterface
public interface BlockPlacer {
    BlockState getState(final BlockPos p0, final Random p1, final Direction p2);
}
