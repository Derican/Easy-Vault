package iskallia.vault.block;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class CatalystDecryptionTableBlock extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public CatalystDecryptionTableBlock() {
        super(AbstractBlock.Properties.of(Material.STONE)
                .strength(1.5F, 6.0F)
                .noOcclusion());
    }


    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return (BlockState) defaultBlockState().setValue((Property) FACING, (Comparable) context.getHorizontalDirection().getOpposite());
    }


    public VoxelShape getOcclusionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return LecternBlock.SHAPE_COMMON;
    }


    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return LecternBlock.SHAPE_COLLISION;
    }


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch ((Direction) state.getValue((Property) FACING)) {
            case NORTH:
                return LecternBlock.SHAPE_NORTH;
            case SOUTH:
                return LecternBlock.SHAPE_SOUTH;
            case EAST:
                return LecternBlock.SHAPE_EAST;
            case WEST:
                return LecternBlock.SHAPE_WEST;
        }
        return LecternBlock.SHAPE_COMMON;
    }


    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide()) {
            return ActionResultType.SUCCESS;
        }
        TileEntity te = world.getBlockEntity(pos);
        if (!(te instanceof iskallia.vault.block.entity.CatalystDecryptionTableTileEntity)) {
            return ActionResultType.SUCCESS;
        }
        if (!(player instanceof ServerPlayerEntity)) {
            return ActionResultType.SUCCESS;
        }

        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, buffer -> buffer.writeBlockPos(pos));
        return ActionResultType.SUCCESS;
    }


    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof iskallia.vault.block.entity.CatalystDecryptionTableTileEntity) {
                tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
                    }
                });
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }


    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }


    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
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


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.CATALYST_DECRYPTION_TABLE_TILE_ENTITY.create();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\CatalystDecryptionTableBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */