package iskallia.vault.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

@FunctionalInterface
public interface BlockPlacer {
    BlockState getState(BlockPos paramBlockPos, Random paramRandom, Direction paramDirection);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\BlockPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */