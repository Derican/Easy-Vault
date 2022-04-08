package iskallia.vault.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.ArrayList;
import java.util.List;


public class PortalPlacer {
    private final BlockPlacer portalPlacer;
    private final BlockPlacer framePlacer;

    public PortalPlacer(BlockPlacer portal, BlockPlacer frame) {
        this.portalPlacer = portal;
        this.framePlacer = frame;
    }

    public List<BlockPos> place(IWorld world, BlockPos pos, Direction facing, int width, int height) {
        pos = pos.relative(Direction.DOWN).relative(facing.getOpposite());

        List<BlockPos> portalPlacements = new ArrayList<>();
        for (int y = 0; y < height + 2; y++) {
            place(world, pos.above(y), facing, this.framePlacer);
            place(world, pos.relative(facing, width + 1).above(y), facing, this.framePlacer);

            for (int x = 1; x < width + 1; x++) {
                if (y == 0 || y == height + 1) {
                    place(world, pos.relative(facing, x).above(y), facing, this.framePlacer);
                } else {
                    BlockPos placePos = pos.relative(facing, x).above(y);
                    if (place(world, placePos, facing, this.portalPlacer)) {
                        portalPlacements.add(placePos);
                    }
                }
            }
        }
        return portalPlacements;
    }

    protected boolean place(IWorld world, BlockPos pos, Direction direction, BlockPlacer provider) {
        return place(world, pos, provider.getState(pos, world.getRandom(), direction));
    }

    protected boolean place(IWorld world, BlockPos pos, BlockState state) {
        if (state != null) {
            return world.setBlock(pos, state, 3);
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\PortalPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */