package iskallia.vault.block;

import iskallia.vault.container.KeyPressContainer;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class KeyPressBlock extends FallingBlock {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    private static final VoxelShape PART_BASE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
    private static final VoxelShape PART_LOWER_X = Block.box(3.0D, 4.0D, 4.0D, 13.0D, 5.0D, 12.0D);
    private static final VoxelShape PART_MID_X = Block.box(4.0D, 5.0D, 6.0D, 12.0D, 10.0D, 10.0D);
    private static final VoxelShape PART_UPPER_X = Block.box(0.0D, 10.0D, 3.0D, 16.0D, 16.0D, 13.0D);
    private static final VoxelShape PART_LOWER_Z = Block.box(4.0D, 4.0D, 3.0D, 12.0D, 5.0D, 13.0D);
    private static final VoxelShape PART_MID_Z = Block.box(6.0D, 5.0D, 4.0D, 10.0D, 10.0D, 12.0D);
    private static final VoxelShape PART_UPPER_Z = Block.box(3.0D, 10.0D, 0.0D, 13.0D, 16.0D, 16.0D);
    private static final VoxelShape X_AXIS_AABB = VoxelShapes.or(PART_BASE, new VoxelShape[]{PART_LOWER_X, PART_MID_X, PART_UPPER_X});
    private static final VoxelShape Z_AXIS_AABB = VoxelShapes.or(PART_BASE, new VoxelShape[]{PART_LOWER_Z, PART_MID_Z, PART_UPPER_Z});

    public KeyPressBlock() {
        super(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL)
                .sound(SoundType.ANVIL)
                .strength(2.0F, 3600000.0F));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return (BlockState) defaultBlockState().setValue((Property) FACING, (Comparable) context.getHorizontalDirection().getClockWise());
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = (Direction) state.getValue((Property) FACING);
        return (direction.getAxis() == Direction.Axis.X) ? X_AXIS_AABB : Z_AXIS_AABB;
    }


    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (world.isClientSide) return ActionResultType.SUCCESS;

        NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {

            public ITextComponent getDisplayName() {
                return (ITextComponent) new StringTextComponent("Key Press");
            }


            @Nullable
            public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
                return (Container) new KeyPressContainer(windowId, player);
            }
        });


        return ActionResultType.SUCCESS;
    }

    protected void falling(FallingBlockEntity fallingEntity) {
        fallingEntity.setHurtsEntities(true);
    }

    public void onLand(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
        if (!fallingBlock.isSilent()) {
            worldIn.levelEvent(1031, pos, 0);
        }
    }

    public void onBroken(World worldIn, BlockPos pos, FallingBlockEntity fallingBlock) {
        if (!fallingBlock.isSilent()) {
            worldIn.levelEvent(1029, pos, 0);
        }
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.setValue((Property) FACING, (Comparable) rot.rotate((Direction) state.getValue((Property) FACING)));
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\KeyPressBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */