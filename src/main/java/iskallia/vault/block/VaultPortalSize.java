package iskallia.vault.block;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class VaultPortalSize {
    private final IWorld world;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int portalBlockCount;

    public VaultPortalSize(IWorld worldIn, BlockPos pos, Direction.Axis axisIn, AbstractBlock.IPositionPredicate positionPredicate) {
        this.world = worldIn;
        this.axis = axisIn;
        this.rightDir = (axisIn == Direction.Axis.X) ? Direction.WEST : Direction.SOUTH;
        this.positionPredicate = positionPredicate;
        this.bottomLeft = computeBottomLeft(pos);
        if (this.bottomLeft == null) {
            this.bottomLeft = pos;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = computeWidth();
            if (this.width > 0)
                this.height = computeHeight();
        }
    }

    @Nullable
    private BlockPos bottomLeft;
    private int height;
    private int width;
    private AbstractBlock.IPositionPredicate positionPredicate;

    public static Optional<VaultPortalSize> getPortalSize(IWorld world, BlockPos pos, Direction.Axis axis, AbstractBlock.IPositionPredicate positionPredicate) {
        return getPortalSize(world, pos, size ->
                (size.isValid() && size.portalBlockCount == 0), axis, positionPredicate);
    }


    public static Optional<VaultPortalSize> getPortalSize(IWorld world, BlockPos pos, Predicate<VaultPortalSize> sizePredicate, Direction.Axis axis, AbstractBlock.IPositionPredicate positionPredicate) {
        Optional<VaultPortalSize> optional = Optional.<VaultPortalSize>of(new VaultPortalSize(world, pos, axis, positionPredicate)).filter(sizePredicate);
        if (optional.isPresent()) {
            return optional;
        }
        Direction.Axis direction$axis = (axis == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
        return Optional.<VaultPortalSize>of(new VaultPortalSize(world, pos, direction$axis, positionPredicate)).filter(sizePredicate);
    }


    private static boolean canConnect(BlockState state) {
        return (state.isAir() || state.is((Block) ModBlocks.VAULT_PORTAL) || state.is((Block) ModBlocks.OTHER_SIDE_PORTAL));
    }

    @Nullable
    private BlockPos computeBottomLeft(BlockPos pos) {
        for (int i = Math.max(0, pos.getY() - 21); pos.getY() > i && canConnect(this.world.getBlockState(pos.below())); pos = pos.below())
            ;

        Direction direction = this.rightDir.getOpposite();
        int j = computeWidth(pos, direction) - 1;
        return (j < 0) ? null : pos.relative(direction, j);
    }

    public Direction.Axis getAxis() {
        return this.axis;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public BlockPos getBottomLeft() {
        return this.bottomLeft;
    }

    private int computeWidth() {
        int i = computeWidth(this.bottomLeft, this.rightDir);
        return (i >= 2 && i <= 21) ? i : 0;
    }

    private int computeWidth(BlockPos pos, Direction direction) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int i = 0; i <= 21; i++) {
            blockpos$mutable.set((Vector3i) pos).move(direction, i);
            BlockState blockstate = this.world.getBlockState((BlockPos) blockpos$mutable);
            if (!canConnect(blockstate)) {
                if (this.positionPredicate.test(blockstate, (IBlockReader) this.world, (BlockPos) blockpos$mutable)) {
                    return i;
                }

                break;
            }
            BlockState blockstate1 = this.world.getBlockState((BlockPos) blockpos$mutable.move(Direction.DOWN));
            if (!this.positionPredicate.test(blockstate1, (IBlockReader) this.world, (BlockPos) blockpos$mutable)) {
                break;
            }
        }

        return 0;
    }

    private int computeHeight() {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        int i = getFrameColumnCount(blockpos$mutable);
        return (i >= 3 && i <= 21 && computeHeight(blockpos$mutable, i)) ? i : 0;
    }

    private boolean computeHeight(BlockPos.Mutable mutablePos, int upDisplacement) {
        for (int i = 0; i < this.width; i++) {
            BlockPos.Mutable blockpos$mutable = mutablePos.set((Vector3i) this.bottomLeft).move(Direction.UP, upDisplacement).move(this.rightDir, i);
            if (!this.positionPredicate.test(this.world.getBlockState((BlockPos) blockpos$mutable), (IBlockReader) this.world, (BlockPos) blockpos$mutable)) {
                return false;
            }
        }

        return true;
    }

    private int getFrameColumnCount(BlockPos.Mutable mutablePos) {
        for (int i = 0; i < 21; i++) {
            mutablePos.set((Vector3i) this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
            if (!this.positionPredicate.test(this.world.getBlockState((BlockPos) mutablePos), (IBlockReader) this.world, (BlockPos) mutablePos)) {
                return i;
            }

            mutablePos.set((Vector3i) this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
            if (!this.positionPredicate.test(this.world.getBlockState((BlockPos) mutablePos), (IBlockReader) this.world, (BlockPos) mutablePos)) {
                return i;
            }

            for (int j = 0; j < this.width; j++) {
                mutablePos.set((Vector3i) this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
                BlockState blockstate = this.world.getBlockState((BlockPos) mutablePos);
                if (!canConnect(blockstate)) {
                    return i;
                }

                if (blockstate.is((Block) ModBlocks.VAULT_PORTAL) || blockstate.is((Block) ModBlocks.OTHER_SIDE_PORTAL)) {
                    this.portalBlockCount++;
                }
            }
        }

        return 21;
    }

    public boolean isValid() {
        return (this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21);
    }

    public void placePortalBlocks(Consumer<BlockPos> placer) {
        BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1)
                        .relative(this.rightDir, this.width - 1))
                .forEach(placer);
    }

    public boolean validatePortal() {
        return (isValid() && this.portalBlockCount == this.width * this.height);
    }

    public Direction getRightDir() {
        return this.rightDir;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultPortalSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */