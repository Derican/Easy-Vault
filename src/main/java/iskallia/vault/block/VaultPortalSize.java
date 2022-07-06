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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class VaultPortalSize {
    private final IWorld world;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int portalBlockCount;
    @Nullable
    private BlockPos bottomLeft;
    private int height;
    private int width;
    private AbstractBlock.IPositionPredicate positionPredicate;

    public VaultPortalSize(final IWorld worldIn, final BlockPos pos, final Direction.Axis axisIn, final AbstractBlock.IPositionPredicate positionPredicate) {
        this.world = worldIn;
        this.axis = axisIn;
        this.rightDir = ((axisIn == Direction.Axis.X) ? Direction.WEST : Direction.SOUTH);
        this.positionPredicate = positionPredicate;
        this.bottomLeft = this.computeBottomLeft(pos);
        if (this.bottomLeft == null) {
            this.bottomLeft = pos;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.computeWidth();
            if (this.width > 0) {
                this.height = this.computeHeight();
            }
        }
    }

    public static Optional<VaultPortalSize> getPortalSize(final IWorld world, final BlockPos pos, final Direction.Axis axis, final AbstractBlock.IPositionPredicate positionPredicate) {
        return getPortalSize(world, pos, size -> size.isValid() && size.portalBlockCount == 0, axis, positionPredicate);
    }

    public static Optional<VaultPortalSize> getPortalSize(final IWorld world, final BlockPos pos, final Predicate<VaultPortalSize> sizePredicate, final Direction.Axis axis, final AbstractBlock.IPositionPredicate positionPredicate) {
        final Optional<VaultPortalSize> optional = Optional.of(new VaultPortalSize(world, pos, axis, positionPredicate)).filter(sizePredicate);
        if (optional.isPresent()) {
            return optional;
        }
        final Direction.Axis direction$axis = (axis == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
        return Optional.of(new VaultPortalSize(world, pos, direction$axis, positionPredicate)).filter(sizePredicate);
    }

    public static List<BlockPos> getFrame(final IWorld world, final BlockPos pos) {
        final List<BlockPos> positions = new ArrayList<BlockPos>();
        final Optional<VaultPortalSize> portalSize = findPortalSizeFromPortalBlock(world, pos);
        if (portalSize.isPresent()) {
            final VaultPortalSize size = portalSize.get();
            final BlockPos current = (size.bottomLeft == null) ? null : size.bottomLeft.relative(size.rightDir.getOpposite()).below();
            if (current != null) {
                positions.add(current);
                findAndAddPositions(world, positions, size, current);
            }
        }
        return positions;
    }

    private static void findAndAddPositions(final IWorld world, final List<BlockPos> positions, final VaultPortalSize size, BlockPos current) {
        for (int up = 0; up <= size.height; ++up) {
            if (!VaultPortalBlock.FRAME.test(world.getBlockState(current.above()), (IBlockReader) world, current.above())) {
                current = current.above();
                positions.add(current);
                break;
            }
            current = current.above();
            positions.add(current);
        }
        for (int right = 0; right <= size.width; ++right) {
            if (!VaultPortalBlock.FRAME.test(world.getBlockState(current.relative(size.rightDir)), (IBlockReader) world, current.relative(size.rightDir))) {
                current = current.relative(size.rightDir);
                positions.add(current);
                break;
            }
            current = current.relative(size.rightDir);
            positions.add(current);
        }
        for (int down = 0; down <= size.height; ++down) {
            if (!VaultPortalBlock.FRAME.test(world.getBlockState(current.below()), (IBlockReader) world, current.below())) {
                current = current.below();
                positions.add(current);
                break;
            }
            current = current.below();
            positions.add(current);
        }
        for (int left = 0; left < size.width; ++left) {
            if (!VaultPortalBlock.FRAME.test(world.getBlockState(current.relative(size.rightDir.getOpposite())), (IBlockReader) world, current.relative(size.rightDir.getOpposite()))) {
                positions.add(current.above());
                break;
            }
            current = current.relative(size.rightDir.getOpposite());
            positions.add(current);
        }
    }

    private static Optional<VaultPortalSize> findPortalSizeFromPortalBlock(final IWorld world, final BlockPos pos) {
        Optional<VaultPortalSize> portalSize = getPortalSize(world, pos.north(), VaultPortalSize::isValid, Direction.Axis.Z, VaultPortalBlock.FRAME);
        if (!portalSize.isPresent()) {
            portalSize = getPortalSize(world, pos.south(), VaultPortalSize::isValid, Direction.Axis.Z, VaultPortalBlock.FRAME);
        }
        if (!portalSize.isPresent()) {
            portalSize = getPortalSize(world, pos.east(), VaultPortalSize::isValid, Direction.Axis.X, VaultPortalBlock.FRAME);
        }
        if (!portalSize.isPresent()) {
            portalSize = getPortalSize(world, pos.west(), VaultPortalSize::isValid, Direction.Axis.X, VaultPortalBlock.FRAME);
        }
        return portalSize;
    }

    private static boolean canConnect(final BlockState state) {
        return state.isAir() || state.is((Block) ModBlocks.VAULT_PORTAL) || state.is((Block) ModBlocks.OTHER_SIDE_PORTAL);
    }

    @Nullable
    private BlockPos computeBottomLeft(BlockPos pos) {
        for (int i = Math.max(0, pos.getY() - 21); pos.getY() > i && canConnect(this.world.getBlockState(pos.below())); pos = pos.below()) {
        }
        final Direction direction = this.rightDir.getOpposite();
        final int j = this.computeWidth(pos, direction) - 1;
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
        final int i = this.computeWidth(this.bottomLeft, this.rightDir);
        return (i >= 2 && i <= 21) ? i : 0;
    }

    private int computeWidth(final BlockPos pos, final Direction direction) {
        final BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        int i = 0;
        while (i <= 21) {
            blockpos$mutable.set((Vector3i) pos).move(direction, i);
            final BlockState blockstate = this.world.getBlockState((BlockPos) blockpos$mutable);
            if (!canConnect(blockstate)) {
                if (this.positionPredicate.test(blockstate, (IBlockReader) this.world, (BlockPos) blockpos$mutable)) {
                    return i;
                }
                break;
            } else {
                final BlockState blockstate2 = this.world.getBlockState((BlockPos) blockpos$mutable.move(Direction.DOWN));
                if (!this.positionPredicate.test(blockstate2, (IBlockReader) this.world, (BlockPos) blockpos$mutable)) {
                    break;
                }
                ++i;
            }
        }
        return 0;
    }

    private int computeHeight() {
        final BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        final int i = this.getFrameColumnCount(blockpos$mutable);
        return (i >= 3 && i <= 21 && this.computeHeight(blockpos$mutable, i)) ? i : 0;
    }

    private boolean computeHeight(final BlockPos.Mutable mutablePos, final int upDisplacement) {
        for (int i = 0; i < this.width; ++i) {
            final BlockPos.Mutable blockpos$mutable = mutablePos.set((Vector3i) this.bottomLeft).move(Direction.UP, upDisplacement).move(this.rightDir, i);
            if (!this.positionPredicate.test(this.world.getBlockState((BlockPos) blockpos$mutable), (IBlockReader) this.world, (BlockPos) blockpos$mutable)) {
                return false;
            }
        }
        return true;
    }

    private int getFrameColumnCount(final BlockPos.Mutable mutablePos) {
        for (int i = 0; i < 21; ++i) {
            mutablePos.set((Vector3i) this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
            if (!this.positionPredicate.test(this.world.getBlockState((BlockPos) mutablePos), (IBlockReader) this.world, (BlockPos) mutablePos)) {
                return i;
            }
            mutablePos.set((Vector3i) this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
            if (!this.positionPredicate.test(this.world.getBlockState((BlockPos) mutablePos), (IBlockReader) this.world, (BlockPos) mutablePos)) {
                return i;
            }
            for (int j = 0; j < this.width; ++j) {
                mutablePos.set((Vector3i) this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
                final BlockState blockstate = this.world.getBlockState((BlockPos) mutablePos);
                if (!canConnect(blockstate)) {
                    return i;
                }
                if (blockstate.is((Block) ModBlocks.VAULT_PORTAL) || blockstate.is((Block) ModBlocks.OTHER_SIDE_PORTAL)) {
                    ++this.portalBlockCount;
                }
            }
        }
        return 21;
    }

    public boolean isValid() {
        return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }

    public void placePortalBlocks(final Consumer<BlockPos> placer) {
        BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).forEach(placer);
    }

    public boolean validatePortal() {
        return this.isValid() && this.portalBlockCount == this.width * this.height;
    }

    public Direction getRightDir() {
        return this.rightDir;
    }
}
