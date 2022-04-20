package iskallia.vault.block.base;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public abstract class FacedBlock extends Block {
    public static final DirectionProperty FACING;

    public FacedBlock(final AbstractBlock.Properties properties) {
        super(properties);
    }

    @Nonnull
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FacedBlock.FACING, context.getHorizontalDirection());
    }

    public BlockState rotate(final BlockState state, final Rotation rot) {
        return state.setValue(FacedBlock.FACING, rot.rotate(state.getValue(FacedBlock.FACING)));
    }

    public BlockState mirror(final BlockState state, final Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FacedBlock.FACING)));
    }

    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FacedBlock.FACING});
    }

    public boolean isPathfindable(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public int getDustColor(final BlockState state, final IBlockReader reader, final BlockPos pos) {
        return state.getMapColor(reader, pos).col;
    }

    static {
        FACING = HorizontalBlock.FACING;
    }
}
