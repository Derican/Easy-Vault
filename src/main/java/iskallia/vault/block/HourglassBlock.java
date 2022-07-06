package iskallia.vault.block;

import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.nbt.INBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IWorld;
import net.minecraft.util.Direction;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateContainer;

import javax.annotation.Nullable;

import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import iskallia.vault.util.MiscUtils;

import java.util.*;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import iskallia.vault.block.entity.HourglassTileEntity;
import iskallia.vault.init.ModItems;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.Hand;
import iskallia.vault.init.ModBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultStart;
import iskallia.vault.world.vault.logic.objective.TreasureHuntObjective;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.state.Property;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.ToolType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.EnumProperty;

import net.minecraft.block.Block;

public class HourglassBlock extends Block {
    private static final Random rand;
    public static final EnumProperty<DoubleBlockHalf> HALF;

    public HourglassBlock() {
        super(AbstractBlock.Properties.of(Material.GLASS, MaterialColor.COLOR_BROWN).noOcclusion().requiresCorrectToolForDrops().harvestTool(ToolType.AXE).harvestLevel(1).strength(3.0f, 3600000.0f));
        this.registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any()).setValue((Property) HourglassBlock.HALF, (Comparable) DoubleBlockHalf.LOWER));
    }

    public boolean removedByPlayer(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final boolean willHarvest, final FluidState fluid) {
        if (world instanceof ServerWorld) {
            final VaultRaid vault = VaultRaidData.get((ServerWorld) world).getAt((ServerWorld) world, pos);
            final Optional<TreasureHuntObjective> opt = vault.getActiveObjective(TreasureHuntObjective.class);
            if (opt.isPresent()) {
                final Set<VaultPiece> rooms = vault.getGenerator().getPiecesAt(pos, VaultStart.class);
                if (!rooms.isEmpty()) {
                    return false;
                }
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    public boolean hasTileEntity(final BlockState state) {
        return state.getValue((Property) HourglassBlock.HALF) == DoubleBlockHalf.LOWER;
    }

    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        if (state.getValue((Property) HourglassBlock.HALF) == DoubleBlockHalf.LOWER) {
            return ModBlocks.HOURGLASS_TILE_ENTITY.create();
        }
        return null;
    }

    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        if (world.isClientSide()) {
            return ActionResultType.SUCCESS;
        }
        if (state.getValue((Property) HourglassBlock.HALF) == DoubleBlockHalf.UPPER) {
            final BlockState down = world.getBlockState(pos.below());
            return down.use(world, player, hand, hit.withPosition(pos.below()));
        }
        final ItemStack interacted = player.getItemInHand(hand);
        if (ModItems.VAULT_SAND.equals(interacted.getItem())) {
            final VaultRaid vault = VaultRaidData.get((ServerWorld) world).getAt((ServerWorld) world, pos);
            if (vault != null) {
                final CompoundNBT sandNBT = interacted.getTag();
                if (sandNBT == null) {
                    return ActionResultType.SUCCESS;
                }
                final UUID vaultId = sandNBT.getUUID("vault_id");
                if (!vaultId.equals(vault.getProperties().getValue(VaultRaid.IDENTIFIER))) {
                    return ActionResultType.SUCCESS;
                }
            }
            final TileEntity te = world.getBlockEntity(pos);
            if (te instanceof HourglassTileEntity) {
                final HourglassTileEntity hourglass = (HourglassTileEntity) te;
                if (hourglass.addSand(player, 1)) {
                    if (!player.isCreative()) {
                        interacted.shrink(1);
                    }
                    if (hourglass.getFilledPercentage() >= 1.0f) {
                        this.playFullEffects(world, pos);
                    } else {
                        world.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.SAND_BREAK, SoundCategory.BLOCKS, 0.6f, 1.0f);
                    }
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    private void playFullEffects(final World world, final BlockPos pos) {
        for (int i = 0; i < 30; ++i) {
            final Vector3d offset = MiscUtils.getRandomOffset(pos, HourglassBlock.rand, 2.0f);
            ((ServerWorld) world).sendParticles((IParticleData) ParticleTypes.HAPPY_VILLAGER, offset.x, offset.y, offset.z, 3, 0.0, 0.0, 0.0, 1.0);
        }
        world.playSound((PlayerEntity) null, pos, SoundEvents.PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final BlockPos pos = context.getClickedPos();
        final World world = context.getLevel();
        if (World.isInWorldBounds(pos) && world.getBlockState(pos.above()).canBeReplaced(context)) {
            return (BlockState) this.defaultBlockState().setValue((Property) HourglassBlock.HALF, (Comparable) DoubleBlockHalf.LOWER);
        }
        return null;
    }

    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) HourglassBlock.HALF});
    }

    public void playerWillDestroy(final World worldIn, final BlockPos pos, final BlockState state, final PlayerEntity player) {
        if (!worldIn.isClientSide() && player.isCreative()) {
            final DoubleBlockHalf half = (DoubleBlockHalf) state.getValue((Property) HourglassBlock.HALF);
            if (half == DoubleBlockHalf.UPPER) {
                final BlockPos blockpos = pos.below();
                final BlockState blockstate = worldIn.getBlockState(blockpos);
                if (blockstate.getBlock() == state.getBlock() && blockstate.getValue((Property) HourglassBlock.HALF) == DoubleBlockHalf.LOWER) {
                    worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                    worldIn.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
                }
            }
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    public BlockState updateShape(final BlockState state, final Direction facing, final BlockState facingState, final IWorld worldIn, final BlockPos currentPos, final BlockPos facingPos) {
        final DoubleBlockHalf half = (DoubleBlockHalf) state.getValue((Property) HourglassBlock.HALF);
        if (facing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (facing == Direction.UP)) {
            return (facingState.is((Block) this) && facingState.getValue((Property) HourglassBlock.HALF) != half) ? state : Blocks.AIR.defaultBlockState();
        }
        return (half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.canSurvive((IWorldReader) worldIn, currentPos)) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, worldIn, currentPos, facingPos);
    }

    public void setPlacedBy(final World worldIn, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        worldIn.setBlock(pos.above(), (BlockState) state.setValue((Property) HourglassBlock.HALF, (Comparable) DoubleBlockHalf.UPPER), 3);
    }

    public void onRemove(final BlockState state, final World world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        if (!state.is(newState.getBlock()) || !newState.hasTileEntity()) {
            final TileEntity te = getBlockTileEntity(world, pos, state);
            if (te instanceof HourglassTileEntity && state.getValue((Property) HourglassBlock.HALF) == DoubleBlockHalf.LOWER) {
                final ItemStack stack = new ItemStack((IItemProvider) ModBlocks.HOURGLASS);
                stack.getOrCreateTag().put("BlockEntityTag", (INBT) te.serializeNBT());
                Block.popResource(world, pos, stack);
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    public static BlockPos getTileEntityPos(final BlockState state, final BlockPos pos) {
        return (state.getValue((Property) HourglassBlock.HALF) == DoubleBlockHalf.UPPER) ? pos.below() : pos;
    }

    public static TileEntity getBlockTileEntity(final World world, final BlockPos pos, final BlockState state) {
        final BlockPos vendingMachinePos = getTileEntityPos(state, pos);
        return world.getBlockEntity(vendingMachinePos);
    }

    static {
        rand = new Random();
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    }
}
