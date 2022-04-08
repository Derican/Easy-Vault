package iskallia.vault.block;

import com.google.common.collect.Lists;
import iskallia.vault.block.entity.VaultRaidControllerTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.VoxelUtils;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class VaultRaidControllerBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape SHAPE_TOP = makeShape().move(0.0D, -1.0D, 0.0D);
    private static final VoxelShape SHAPE_BOTTOM = makeShape();

    public VaultRaidControllerBlock() {
        super(AbstractBlock.Properties.of(Material.GLASS)
                .sound(SoundType.GLASS)
                .strength(-1.0F, 3600000.0F)
                .noCollission()
                .noOcclusion()
                .noDrops());

        registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any())
                .setValue((Property) HALF, (Comparable) DoubleBlockHalf.LOWER));
    }

    private static VoxelShape makeShape() {
        VoxelShape m1 = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
        VoxelShape m2 = Block.box(2.0D, 2.0D, 2.0D, 14.0D, 29.0D, 14.0D);
        return VoxelUtils.combineAll(IBooleanFunction.OR, new VoxelShape[]{m1, m2});
    }


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.getValue((Property) HALF) == DoubleBlockHalf.UPPER) {
            return SHAPE_TOP;
        }
        return SHAPE_BOTTOM;
    }


    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (state.getValue((Property) HALF) == DoubleBlockHalf.UPPER) {
            BlockState downState = world.getBlockState(pos.below());
            if (!(downState.getBlock() instanceof VaultRaidControllerBlock)) {
                return ActionResultType.SUCCESS;
            }
            return use(downState, world, pos.below(), player, hand, hit);
        }
        if (!world.isClientSide() && world instanceof ServerWorld && hand == Hand.MAIN_HAND) {
            startRaid((ServerWorld) world, pos);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    private void startRaid(ServerWorld world, BlockPos pos) {
        VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
        if (vault == null || vault.getActiveRaid() != null) {
            return;
        }
        TileEntity tile = world.getBlockEntity(pos);
        if (!(tile instanceof VaultRaidControllerTileEntity)) {
            return;
        }
        VaultRaidControllerTileEntity ctrl = (VaultRaidControllerTileEntity) tile;
        if (!ctrl.didTriggerRaid() && vault.triggerRaid(world, pos)) {
            ctrl.setTriggeredRaid(true);
        }
    }


    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        if (!state.is(newState.getBlock())) {
            if (state.getValue((Property) HALF) == DoubleBlockHalf.UPPER) {
                BlockState otherState = world.getBlockState(pos.below());
                if (otherState.is(state.getBlock())) {
                    world.removeBlock(pos.below(), isMoving);
                }
            } else {
                BlockState otherState = world.getBlockState(pos.above());
                if (otherState.is(state.getBlock())) {
                    world.removeBlock(pos.above(), isMoving);
                }
            }
        }
    }


    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Lists.newArrayList();
    }


    public boolean hasTileEntity(BlockState state) {
        return (state.getValue((Property) HALF) == DoubleBlockHalf.LOWER);
    }


    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (hasTileEntity(state)) {
            return ModBlocks.RAID_CONTROLLER_TILE_ENTITY.create();
        }
        return null;
    }


    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) HALF});
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultRaidControllerBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */