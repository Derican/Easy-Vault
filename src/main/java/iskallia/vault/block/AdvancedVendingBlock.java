package iskallia.vault.block;

import iskallia.vault.block.entity.AdvancedVendingTileEntity;
import iskallia.vault.container.AdvancedVendingContainer;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.ItemTraderCore;
import iskallia.vault.vending.TraderCore;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class AdvancedVendingBlock extends Block {
    public static final DirectionProperty FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF;

    public AdvancedVendingBlock() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL).strength(2.0f, 3600000.0f).sound(SoundType.METAL).noOcclusion());
        this.registerDefaultState((this.stateDefinition.any().setValue(AdvancedVendingBlock.FACING, Direction.NORTH)).setValue(AdvancedVendingBlock.HALF, DoubleBlockHalf.LOWER));
    }

    public boolean hasTileEntity(final BlockState state) {
        return state.getValue(AdvancedVendingBlock.HALF) == DoubleBlockHalf.LOWER;
    }

    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        if (state.getValue(AdvancedVendingBlock.HALF) == DoubleBlockHalf.LOWER) {
            return ModBlocks.ADVANCED_VENDING_MACHINE_TILE_ENTITY.create();
        }
        return null;
    }

    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final BlockPos pos = context.getClickedPos();
        final World world = context.getLevel();
        if (pos.getY() < 255 && world.getBlockState(pos.above()).canBeReplaced(context)) {
            return (this.defaultBlockState().setValue(AdvancedVendingBlock.FACING, context.getHorizontalDirection())).setValue(AdvancedVendingBlock.HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AdvancedVendingBlock.HALF});
        builder.add(new Property[]{AdvancedVendingBlock.FACING});
    }

    public void playerWillDestroy(final World worldIn, final BlockPos pos, final BlockState state, final PlayerEntity player) {
        if (!worldIn.isClientSide && player.isCreative()) {
            final DoubleBlockHalf half = (DoubleBlockHalf) state.getValue(AdvancedVendingBlock.HALF);
            if (half == DoubleBlockHalf.UPPER) {
                final BlockPos blockpos = pos.below();
                final BlockState blockstate = worldIn.getBlockState(blockpos);
                if (blockstate.getBlock() == state.getBlock() && blockstate.getValue(AdvancedVendingBlock.HALF) == DoubleBlockHalf.LOWER) {
                    worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                    worldIn.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
                }
            }
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    public BlockState updateShape(final BlockState stateIn, final Direction facing, final BlockState facingState, final IWorld worldIn, final BlockPos currentPos, final BlockPos facingPos) {
        final DoubleBlockHalf half = (DoubleBlockHalf) stateIn.getValue(AdvancedVendingBlock.HALF);
        if (facing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (facing == Direction.UP)) {
            return ((facingState.is((Block) this) && facingState.getValue(AdvancedVendingBlock.HALF) != half) ? stateIn.setValue(AdvancedVendingBlock.FACING, facingState.getValue(AdvancedVendingBlock.FACING)) : Blocks.AIR.defaultBlockState());
        }
        return (half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.canSurvive((IWorldReader) worldIn, currentPos)) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public void setPlacedBy(final World worldIn, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        worldIn.setBlock(pos.above(), state.setValue(AdvancedVendingBlock.HALF, DoubleBlockHalf.UPPER), 3);
    }

    public void onRemove(final BlockState state, final World worldIn, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        if (worldIn.isClientSide) {
            return;
        }
        if (!newState.isAir()) {
            return;
        }
        final AdvancedVendingTileEntity machine = getAdvancedVendingMachineTile(worldIn, pos, state);
        if (machine == null) {
            return;
        }
        if (state.getValue(AdvancedVendingBlock.HALF) == DoubleBlockHalf.LOWER) {
            final ItemStack stack = new ItemStack((IItemProvider) this.getBlock());
            final CompoundNBT machineNBT = machine.serializeNBT();
            final CompoundNBT stackNBT = new CompoundNBT();
            stackNBT.put("BlockEntityTag", (INBT) machineNBT);
            stack.setTag(stackNBT);
            this.dropVendingMachine(stack, worldIn, pos);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    private void dropVendingMachine(final ItemStack stack, final World world, final BlockPos pos) {
        final ItemEntity entity = new ItemEntity(world, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), stack);
        world.addFreshEntity((Entity) entity);
    }

    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        final ItemStack heldStack = player.getItemInHand(hand);
        final AdvancedVendingTileEntity machine = getAdvancedVendingMachineTile(world, pos, state);
        if (machine == null) {
            return ActionResultType.SUCCESS;
        }
        if (!world.isClientSide() && player.isShiftKeyDown()) {
            final ItemStack core = machine.getTraderCoreStack();
            if (!player.addItem(core)) {
                player.drop(core, false);
            }
            machine.sendUpdates();
            return ActionResultType.SUCCESS;
        }
        if (heldStack.getItem() instanceof ItemTraderCore) {
            final TraderCore coreToInsert = ItemTraderCore.getCoreFromStack(heldStack);
            machine.addCore(coreToInsert);
            heldStack.shrink(1);
            return ActionResultType.SUCCESS;
        }
        if (world.isClientSide) {
            playOpenSound();
            return ActionResultType.SUCCESS;
        }
        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) new INamedContainerProvider() {
            public ITextComponent getDisplayName() {
                return (ITextComponent) new StringTextComponent("Advanced Vending Machine");
            }

            @Nullable
            public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity playerEntity) {
                final BlockState blockState = world.getBlockState(pos);
                final BlockPos vendingMachinePos = AdvancedVendingBlock.getVendingMachinePos(blockState, pos);
                return new AdvancedVendingContainer(windowId, world, vendingMachinePos, playerInventory, playerEntity);
            }
        }, buffer -> {
            final BlockState blockState = world.getBlockState(pos);
            buffer.writeBlockPos(getVendingMachinePos(blockState, pos));
            return;
        });
        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    public static void playOpenSound() {
        final Minecraft minecraft = Minecraft.getInstance();
        minecraft.getSoundManager().play((ISound) SimpleSound.forUI(ModSounds.VENDING_MACHINE_SFX, 1.0f, 0.3f));
    }

    public static BlockPos getVendingMachinePos(final BlockState state, final BlockPos pos) {
        return (state.getValue(AdvancedVendingBlock.HALF) == DoubleBlockHalf.UPPER) ? pos.below() : pos;
    }

    public static AdvancedVendingTileEntity getAdvancedVendingMachineTile(final World world, final BlockPos pos, final BlockState state) {
        final BlockPos vendingMachinePos = getVendingMachinePos(state, pos);
        final TileEntity tileEntity = world.getBlockEntity(vendingMachinePos);
        if (!(tileEntity instanceof AdvancedVendingTileEntity)) {
            return null;
        }
        return (AdvancedVendingTileEntity) tileEntity;
    }

    static {
        FACING = HorizontalBlock.FACING;
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    }
}
