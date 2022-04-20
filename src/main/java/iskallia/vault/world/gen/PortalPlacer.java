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

    public PortalPlacer(final BlockPlacer portal, final BlockPlacer frame) {
        this.portalPlacer = portal;
        this.framePlacer = frame;
    }

    public List<BlockPos> place(final IWorld world, BlockPos pos, final Direction facing, final int width, final int height) {
        pos = pos.relative(Direction.DOWN).relative(facing.getOpposite());
        final List<BlockPos> portalPlacements = new ArrayList<BlockPos>();
        for (int y = 0; y < height + 2; ++y) {
            this.place(world, pos.above(y), facing, this.framePlacer);
            this.place(world, pos.relative(facing, width + 1).above(y), facing, this.framePlacer);
            for (int x = 1; x < width + 1; ++x) {
                if (y == 0 || y == height + 1) {
                    this.place(world, pos.relative(facing, x).above(y), facing, this.framePlacer);
                } else {
                    final BlockPos placePos = pos.relative(facing, x).above(y);
                    if (this.place(world, placePos, facing, this.portalPlacer)) {
                        portalPlacements.add(placePos);
                    }
                }
            }
        }
        return portalPlacements;
    }

    protected boolean place(final IWorld world, final BlockPos pos, final Direction direction, final BlockPlacer provider) {
        return this.place(world, pos, provider.getState(pos, world.getRandom(), direction));
    }

    protected boolean place(final IWorld world, final BlockPos pos, final BlockState state) {
        return state != null && world.setBlock(pos, state, 3);
    }
}
