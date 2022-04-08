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
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public abstract class FacedBlock extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public FacedBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Nonnull
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return (BlockState) defaultBlockState()
                .setValue((Property) FACING, (Comparable) context.getHorizontalDirection());
    }


    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.setValue((Property) FACING, (Comparable) rot.rotate((Direction) state.getValue((Property) FACING)));
    }


    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation((Direction) state.getValue((Property) FACING)));
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) FACING});
    }


    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public int getDustColor(BlockState state, IBlockReader reader, BlockPos pos) {
        return (state.getMapColor(reader, pos)).col;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\base\FacedBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */