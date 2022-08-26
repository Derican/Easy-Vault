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
    public static final EnumProperty<DoubleBlockHalf> HALF;
    private static final VoxelShape SHAPE_TOP;
    private static final VoxelShape SHAPE_BOTTOM;

    public VaultRaidControllerBlock() {
        super(AbstractBlock.Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(-1.0f, 3600000.0f).noCollission().noOcclusion().noDrops());
        this.registerDefaultState((this.stateDefinition.any()).setValue(VaultRaidControllerBlock.HALF, DoubleBlockHalf.LOWER));
    }

    private static VoxelShape makeShape() {
        final VoxelShape m1 = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
        final VoxelShape m2 = Block.box(2.0, 2.0, 2.0, 14.0, 29.0, 14.0);
        return VoxelUtils.combineAll(IBooleanFunction.OR, m1, m2);
    }

    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        if (state.getValue(VaultRaidControllerBlock.HALF) == DoubleBlockHalf.UPPER) {
            return VaultRaidControllerBlock.SHAPE_TOP;
        }
        return VaultRaidControllerBlock.SHAPE_BOTTOM;
    }

    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        if (state.getValue(VaultRaidControllerBlock.HALF) == DoubleBlockHalf.UPPER) {
            final BlockState downState = world.getBlockState(pos.below());
            if (!(downState.getBlock() instanceof VaultRaidControllerBlock)) {
                return ActionResultType.SUCCESS;
            }
            return this.use(downState, world, pos.below(), player, hand, hit);
        } else {
            if (!world.isClientSide() && world instanceof ServerWorld && hand == Hand.MAIN_HAND) {
                this.startRaid((ServerWorld) world, pos);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.SUCCESS;
        }
    }

    private void startRaid(final ServerWorld world, final BlockPos pos) {
        final VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
        if (vault == null || vault.getActiveRaid() != null) {
            return;
        }
        final TileEntity tile = world.getBlockEntity(pos);
        if (!(tile instanceof VaultRaidControllerTileEntity)) {
            return;
        }
        final VaultRaidControllerTileEntity ctrl = (VaultRaidControllerTileEntity) tile;
        if (!ctrl.didTriggerRaid() && vault.triggerRaid(world, pos)) {
            ctrl.setTriggeredRaid(true);
        }
    }

    public void onRemove(final BlockState state, final World world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        if (!state.is(newState.getBlock())) {
            if (state.getValue(VaultRaidControllerBlock.HALF) == DoubleBlockHalf.UPPER) {
                final BlockState otherState = world.getBlockState(pos.below());
                if (otherState.is(state.getBlock())) {
                    world.removeBlock(pos.below(), isMoving);
                }
            } else {
                final BlockState otherState = world.getBlockState(pos.above());
                if (otherState.is(state.getBlock())) {
                    world.removeBlock(pos.above(), isMoving);
                }
            }
        }
    }

    public List<ItemStack> getDrops(final BlockState state, final LootContext.Builder builder) {
        return Lists.newArrayList();
    }

    public boolean hasTileEntity(final BlockState state) {
        return state.getValue(VaultRaidControllerBlock.HALF) == DoubleBlockHalf.LOWER;
    }

    @Nullable
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        if (this.hasTileEntity(state)) {
            return ModBlocks.RAID_CONTROLLER_TILE_ENTITY.create();
        }
        return null;
    }

    public BlockRenderType getRenderShape(final BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(VaultRaidControllerBlock.HALF);
    }

    static {
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
        SHAPE_TOP = makeShape().move(0.0, -1.0, 0.0);
        SHAPE_BOTTOM = makeShape();
    }
}
